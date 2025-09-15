package com.ibm.wfm.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ibm.wfm.filters.ApiKeyFilter;
import com.ibm.wfm.filters.JwtAuthenticationFilter;
import com.ibm.wfm.services.ApiKeyAuthService;
import com.ibm.wfm.services.IbmBluegroupService;
import com.ibm.wfm.services.IbmIamAuthService;
import com.ibm.wfm.utils.JwtUtil;

/**
 * Configures Spring Security for the application. Integrates a custom ApiKeyFilter.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	/*
    private final ApiKeyFilter apiKeyFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(ApiKeyFilter apiKeyFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.apiKeyFilter = apiKeyFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    */
    
    private final ApiKeyAuthService apiKeyAuthService;
    private final IbmIamAuthService ibmIamAuthService;
    private final IbmBluegroupService ibmBluegroupService;
    private final JwtDecoder jwtDecoder;
    private final JwtUtil jwtUtil;

    public SecurityConfig(ApiKeyAuthService apiKeyAuthService,
                          IbmIamAuthService ibmIamAuthService,
                          IbmBluegroupService ibmBluegroupService,
                          JwtDecoder jwtDecoder,
                          JwtUtil jwtUtil) {
    	this.apiKeyAuthService = apiKeyAuthService;
        this.ibmIamAuthService = ibmIamAuthService;
        this.ibmBluegroupService = ibmBluegroupService;
        this.jwtDecoder = jwtDecoder;
        this.jwtUtil = jwtUtil;
    }
    
    /*
    @Bean
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(apiKeyAuthService,ibmIamAuthService, ibmBluegroupService, jwtDecoder, jwtUtil);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }
    */

    /**
     * Defines security filter chain.
     * - Permits public and Swagger paths
     * - Requires authentication for /api/v1/s/**
     * - All others are permitted by default
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter(apiKeyAuthService,ibmIamAuthService, ibmBluegroupService, jwtDecoder, jwtUtil);

        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**"
                                       , "/swagger-ui/**"
                                       , "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/s/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, ApiKeyFilter.class)
                .build();
    }
}