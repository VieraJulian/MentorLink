package com.mentorlink.users.infrastructure.vault;

import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;

@Component
public class VaultSecretWriter {

    private final VaultTemplate vaultTemplate;

    public static final String BASE_PATH = "secret/data/keycloak/";


    public VaultSecretWriter(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public void storeClientSecret(String realm, String clientId, String clientSecret) {
        String path = BASE_PATH + realm + "/" + clientId;
        vaultTemplate.write(path, Map.of("data", Map.of("client-secret", clientSecret)));
    }
}
