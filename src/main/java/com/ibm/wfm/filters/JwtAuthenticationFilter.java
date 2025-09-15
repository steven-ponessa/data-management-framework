package com.ibm.wfm.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.wfm.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {
	
	    String authHeader = request.getHeader("Authorization");
	
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String jwtToken = authHeader.substring(7);
	
	        try {
	            String username = jwtUtil.getUsername(jwtToken);
	            List<String> bluegroups = jwtUtil.getBluegroups(jwtToken);
	
	            List<SimpleGrantedAuthority> authorities = bluegroups.stream()
	                    .map(bg -> new SimpleGrantedAuthority("ROLE_" + bg.toUpperCase()))
	                    .collect(Collectors.toList());
	
	            UsernamePasswordAuthenticationToken auth =
	                    new UsernamePasswordAuthenticationToken(username, null, authorities);
	
	            SecurityContextHolder.getContext().setAuthentication(auth);
	            System.out.println("JWT Authenticated user: " + username + " with roles: " + authorities);
	
	        } catch (Exception e) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }
	    }
	
	    filterChain.doFilter(request, response);
	}
}