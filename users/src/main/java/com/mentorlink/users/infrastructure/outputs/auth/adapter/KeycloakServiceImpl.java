package com.mentorlink.users.infrastructure.outputs.auth.adapter;

import com.mentorlink.users.domain.port.outbound.IAuthProvider;
import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UpdateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.exception.KeycloakUserCreationException;
import com.mentorlink.users.infrastructure.outputs.auth.mapper.IUserIdentityMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KeycloakServiceImpl implements IAuthProvider {

    private final IUserIdentityMapper iUserIdentityMapper;

    public KeycloakServiceImpl(IUserIdentityMapper iUserIdentityMapper) {
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
