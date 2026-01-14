package io.github.safeslope;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtKeyProvider {

    @Value("${JWT_PUBLIC_KEY}")
    private String publicKeyBase64;

    public PublicKey publicKey() {
        try {
            byte[] decoded = Base64.getDecoder().decode(publicKeyBase64);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid public key", e);
        }
    }
}

