package com.ibm.wfm.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ibm.wfm.utils.Helpers;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Validates API keys from a static local map for testing purposes.
 */
@Service
public class ApiKeyAuthService {

    // Map of API Key to role(s)
    private static final Map<String, List<String>> apiKeyRoleMap = Map.of(
        "local-admin-key", List.of("ROLE_ADMIN"),
        "local-user-key", List.of("ROLE_USER")
    );

    /**
     * Returns an Authentication token if the API key is valid.
     */
    public Optional<UsernamePasswordAuthenticationToken> isValidApiKey(String apiKey, HttpServletRequest request) {
        if (apiKey == null || !apiKeyRoleMap.containsKey(apiKey)) {
            return Optional.empty();
        }
        
        String wfmDmfAuthCookie = Helpers.readServletCookie((HttpServletRequest) request, "wfm-dmf-auth");
        if (wfmDmfAuthCookie != null) {
        	
        }

        List<String> roles = apiKeyRoleMap.get(apiKey);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // Username is optional here, you can derive it from key or leave as "apikey-user"
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("apikey-user", apiKey, authorities);

        return Optional.of(authentication);
    }
}