package com.mentorlink.users.infrastructure.vault;

import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;

@Component
public class VaultSecretReader {

    private final VaultTemplate vaultTemplate;

    public static final String BASE_PATH = "secret/data/keycloak/";

    public VaultSecretReader(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public String getClientSecret(String realm, String clientId) {
        String path = BASE_PATH + realm + "/" + clientId;
        VaultResponse response = vaultTemplate.read(path);
        if (response.getData() == null) return null;

        Map<String, Object> raw = response.getData();
        Map<String, Object> nested = (Map<String, Object>) raw.get("data");
        return nested != null ? (String) nested.get("client-secret") : null;
    }
}
