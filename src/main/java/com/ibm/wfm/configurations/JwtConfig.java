package com.ibm.wfm.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Configuration class for JWT decoding using IBM IAM public keys.
 */
@Configuration
public class JwtConfig {

    /**
     * Creates a JwtDecoder bean configured with IBM IAM JWK endpoint.
     * @return JwtDecoder instance
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // IBM IAM public key endpoint (default region)
        return NimbusJwtDecoder.withJwkSetUri("https://iam.cloud.ibm.com/identity/keys").build();
    }
}