package com.staroot.jwtClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtClientKeyUtils {
    public static PublicKey readPublicKeyFromFile(String filePath) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            return getPublicKeyFromString(new String(keyBytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read public key", e);
        }
    }
    public static PublicKey getPublicKeyFromString(String keyString) {
        try {
            String publicKeyPEM = keyString
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get public key from string", e);
        }
    }
}

