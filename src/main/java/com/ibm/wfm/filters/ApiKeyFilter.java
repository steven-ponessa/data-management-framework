package com.ibm.wfm.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.wfm.services.ApiKeyAuthService;
import com.ibm.wfm.services.IbmBluegroupService;
import com.ibm.wfm.services.IbmIamAuthService;
import com.ibm.wfm.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom filter that intercepts /api/v1/s/** requests and performs authentication based on X-API-KEY.
 */
//@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyAuthService apiKeyAuthService;
    private final IbmIamAuthService ibmIamAuthService;
    private final IbmBluegroupService ibmBluegroupService;
    private final JwtDecoder jwtDecoder;
    private final JwtUtil jwtUtil;
    
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiKeyFilter(ApiKeyAuthService apiKeyAuthService,
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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
    	
        String path = request.getRequestURI();

        if (!pathMatcher.match("/api/v1/s/**", path)) {
            // Skip filtering if path doesn't match
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String apiKey = request.getHeader("X-API-KEY");

        if (authHeader==null && apiKey!=null) {
            // IBM IAM
			Optional<String> accessToken = ibmIamAuthService.getAccessTokenFromApiKey(apiKey);
			if (accessToken.isPresent()) {	
			    Jwt jwt = jwtDecoder.decode(accessToken.get());

			    List<String> scopes = jwt.getClaimAsStringList("scope");
			    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			    if (scopes != null) {
			        scopes.stream()
			            .map(s -> new SimpleGrantedAuthority("ROLE_" + s.toUpperCase()))
			            .forEach(authorities::add);
			    }
			    
			    String email = jwt.getClaimAsString("email");
			    List<String> bluegroups = null;
			    if (email!=null) {
			    	bluegroups = ibmBluegroupService.getWfmDmfGroups(email);
			    	for (String bluegroup: bluegroups) {
			    		authorities.add(new SimpleGrantedAuthority("ROLE_"+bluegroup));
			    	}
			    }
			
			    UsernamePasswordAuthenticationToken ibmAuth = new UsernamePasswordAuthenticationToken(
			        jwt.getSubject(), null, authorities);
			    
			    SecurityContextHolder.getContext().setAuthentication(ibmAuth);
			    
			    // Return JWT to client
                String customJwt = jwtUtil.generateToken(jwt.getSubject(), bluegroups, 3600_000);
                // Set the token in response headers only, then proceed
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customJwt);

                // continue the filter chain
			    filterChain.doFilter(request, response);
			    return; // Prevent falling through to error handler
			}

            // Step 3: Invalid key — reject
			if (!response.isCommitted()) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
			}
            return;
        }

        // No API key provided, continue unauthenticated
        //filterChain.doFilter(request, response);
    }
}