package com.mentorlink.users.infrastructure.outputs.auth.adapter;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProvider {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String REALM_NAME = "mentorlink-dev";
    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "admin";
    private static final String PASSWORD_CONSOLE = "admin";
    private static final String CLIENT_SECRET = "6jclaTgyQpLck0oxmNe4uIP247IEw2b9";

    public static RealmResource getRealmsResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                .build())
        .build();

        return keycloak.realm(REALM_NAME);
    }

    public static UsersResource getUserResource(){
        RealmResource realmResource = getRealmsResource();
        return realmResource.users();
    }
}
