package com.mentorlink.users.infrastructure.outputs.auth.adapter.config;

import com.mentorlink.users.infrastructure.vault.VaultSecretReader;
import com.mentorlink.users.infrastructure.vault.VaultSecretWriter;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyCloakRealmInitializer {

    @Value("${KEYCLOAK_SERVER_URL}")
    private String serverUrl;

    @Value("${KEYCLOAK_MASTER_REALM}")
    private String masterRealm;

    @Value("${KEYCLOAK_ADMIN_CLIENT_ID}")
    private String adminClientId;

    @Value("${KEYCLOAK_ADMIN_USER}")
    private String adminUser;

    @Value("${KEYCLOAK_ADMIN_PASS}")
    private String adminPass;

    @Value("${KEYCLOAK_TARGET_REALM}")
    private String targetRealm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;

    private final VaultSecretWriter vaultSecretWriter;

    private final VaultSecretReader vaultSecretReader;

    public KeyCloakRealmInitializer(VaultSecretWriter vaultSecretWriter, VaultSecretReader vaultSecretReader) {
        this.vaultSecretReader = vaultSecretReader;
        this.vaultSecretWriter = vaultSecretWriter;
    }

    @PostConstruct
    public void initializeRealm() {

         Keycloak keycloak = KeycloakBuilder.builder()
                 .serverUrl(serverUrl)
                 .realm(masterRealm)
                 .clientId(adminClientId)
                 .username(adminUser)
                 .password(adminPass)
                 .build();

        boolean exists = keycloak.realms().findAll().stream()
                .anyMatch(r -> r.getRealm().equals(targetRealm));

        if (!exists) {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(targetRealm);
            realm.setEnabled(true);
            keycloak.realms().create(realm);

            List<String> roles = List.of("admin", "client", "professional");
            for (String roleName : roles) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(roleName);
                role.setDescription(roleName.toLowerCase() + " role realm");
                keycloak.realm(targetRealm).roles().create(role);
            }

            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientId);
            client.setEnabled(true);
            client.setPublicClient(false);
            client.setDirectAccessGrantsEnabled(true);
            client.setServiceAccountsEnabled(true);
            client.setProtocol("openid-connect");

            Response response = keycloak.realm(targetRealm).clients().create(client);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create client: " + response.getStatusInfo());
            }

            String internalId = CreatedResponseUtil.getCreatedId(response);

            String clientSecret = keycloak.realm(targetRealm)
                    .clients()
                    .get(internalId)
                    .getSecret()
                    .getValue();

            vaultSecretWriter.storeClientSecret(targetRealm, clientId, clientSecret);
        }
    }
}
