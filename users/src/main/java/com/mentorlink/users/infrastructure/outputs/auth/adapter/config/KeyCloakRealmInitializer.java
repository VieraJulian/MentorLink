package com.mentorlink.users.infrastructure.outputs.auth.adapter.config;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyCloakRealmInitializer {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String MASTER_REALM = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin";
    private static final String TARGET_REALM = "mentorlink-dev";

    @PostConstruct
    public void initializeRealm() {

         Keycloak keycloak = KeycloakBuilder.builder()
                 .serverUrl(SERVER_URL)
                 .realm(MASTER_REALM)
                 .clientId(ADMIN_CLI)
                 .username(ADMIN_USER)
                 .password(ADMIN_PASS)
                 .build();

        boolean exists = keycloak.realms().findAll().stream()
                .anyMatch(r -> r.getRealm().equals(TARGET_REALM));

        if (!exists) {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(TARGET_REALM);
            realm.setEnabled(true);
            keycloak.realms().create(realm);

            List<String> roles = List.of("admin", "client", "professional");
            for (String roleName : roles) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(roleName);
                role.setDescription(roleName.toLowerCase() + " role realm");
                keycloak.realm(TARGET_REALM).roles().create(role);
            }

            ClientRepresentation client = new ClientRepresentation();
            client.setClientId("mentorlink-api-rest");
            client.setEnabled(true);
            client.setPublicClient(false);
            client.setDirectAccessGrantsEnabled(true);
            client.setServiceAccountsEnabled(true);
            client.setProtocol("openid-connect");

            Response response = keycloak.realm(TARGET_REALM).clients().create(client);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create client: " + response.getStatusInfo());
            }

            String internalId = CreatedResponseUtil.getCreatedId(response);

            String clientSecret = keycloak.realm(TARGET_REALM)
                    .clients()
                    .get(internalId)
                    .getSecret()
                    .getValue();
        }
    }
}
