package com.mentorlink.users.infrastructure.outputs.auth.adapter;

import com.mentorlink.users.domain.port.outbound.IAuthProvider;
import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UpdateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.exception.KeycloakUserCreationException;
import com.mentorlink.users.infrastructure.outputs.auth.exception.TokenRequestException;
import com.mentorlink.users.infrastructure.outputs.auth.mapper.IUserIdentityMapper;
import com.mentorlink.users.infrastructure.vault.VaultSecretReader;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class KeycloakServiceImpl implements IAuthProvider {

    @Value("${KEYCLOAK_TARGET_REALM}")
    private String targetRealm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;

    @Value("${KEYCLOAK_SERVER_URL}")
    private String serverUrl;

    private final IUserIdentityMapper iUserIdentityMapper;

    private final VaultSecretReader vaultSecretReader;

    public KeycloakServiceImpl(IUserIdentityMapper iUserIdentityMapper, VaultSecretReader vaultSecretReader) {
        this.vaultSecretReader = vaultSecretReader;
        this.iUserIdentityMapper = iUserIdentityMapper;
    }

    @Override
    public UserIdentity createUser(@NotNull CreateIdentity createIdentity) {
        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        // Create user
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(createIdentity.username());
        userRepresentation.setEmail(createIdentity.email());
        userRepresentation.setFirstName(createIdentity.firstname());
        userRepresentation.setLastName(createIdentity.lastname());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);
        status = response.getStatus();

        if (status != 201) {
            throw new KeycloakUserCreationException("Failed to create user in Keycloak. Status: " + status);
        }

        // Get userId
        String path = response.getLocation().getPath();
        String userId = path.substring(path.lastIndexOf("/") + 1);

        // Set password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(createIdentity.password());
        credentialRepresentation.setTemporary(false);
        usersResource.get(userId).resetPassword(credentialRepresentation);

        // Assign role
        String roleName = createIdentity.role();
        RealmResource realmResource = KeycloakProvider.getRealmsResource();
        RoleRepresentation roleRepresentation = realmResource.roles().get(roleName).toRepresentation();
        realmResource.users().get(userId).roles().realmLevel().add(List.of(roleRepresentation));

        // Get created user
        UserRepresentation createdUser = usersResource.get(userId).toRepresentation();

        // Get roles
        List<String> roles = realmResource.users().get(userId).roles().realmLevel().listAll().stream()
                .map(RoleRepresentation::getName)
                .toList();

        List<String> priority = List.of("admin", "professional", "client");

        String role = priority.stream()
                .filter(roles::contains)
                .findFirst()
                .orElse("client");

        return iUserIdentityMapper.userRepresentationToUserIdentity(createdUser, role);
    }

    @Override
    public String loginUser(String username, String password) {
        String clientSecret = vaultSecretReader.getClientSecret(targetRealm, clientId);
        String tokenUrl = serverUrl + "/realms/" + targetRealm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("username", username);
        form.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        Map<String, Object> body = new RestTemplate().postForObject(tokenUrl, request, Map.class);

        if (body == null || !body.containsKey("access_token")) {
            throw new TokenRequestException("Failed to retrieve access token from Keycloak");
        }

        return body.get("access_token").toString();
    }

    @Override
    public UserIdentity updateUser(String id, UpdateIdentity updateIdentity) {
        return null;
    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public Optional<UserIdentity> findUserById(String id) {
        return Optional.empty();
    }
}
