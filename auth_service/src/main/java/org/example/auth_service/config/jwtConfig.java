package org.example.auth_service.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.*;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class jwtConfig {

    private RSAPrivateKey loadPrivateKey() throws Exception{
        String key = new String(
                new ClassPathResource("/keys/private_key.pem")
                        .getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte [] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory
                .getInstance("RSA")
                .generatePrivate(spec);
    }

    private RSAPublicKey loadPublicKey () throws Exception{
        String key = new String(
                new ClassPathResource("keys/public_key.pem")
                        .getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(spec);

}
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {

        RSAKey rsaKey = new RSAKey.Builder(loadPublicKey())
                .privateKey(loadPrivateKey())
                .build();

        ImmutableJWKSet<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(
                        new com.nimbusds.jose.jwk.JWKSet(rsaKey)
                );

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception{
         NimbusJwtDecoder decoder =NimbusJwtDecoder
                .withPublicKey(loadPublicKey())
                .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(
                        "ticketsync-auth-service"
                )
        );
        return decoder;
    }
}
