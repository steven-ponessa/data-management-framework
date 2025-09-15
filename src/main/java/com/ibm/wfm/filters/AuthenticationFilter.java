package com.ibm.wfm.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.models.W3UserDetail;
import com.ibm.wfm.utils.Helpers;
import com.ibm.wfm.utils.JwtDecoderUtil;

@Component
@CrossOrigin(origins = "https://localhost:3000")
public class AuthenticationFilter implements Filter {
	
	@Value("${spring.security.oauth2.client.registration.w3id.client-name}")
	private String clientName;
	@Value("${spring.security.oauth2.client.registration.w3id.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.w3id.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.registration.w3id.redirect-uri}")
	private String redirectUri;
	@Value("${spring.security.oauth2.client.registration.w3id.response-type}")
	private String responseType;
	@Value("${spring.security.oauth2.client.registration.w3id.scope}")
	private String scope;
	@Value("${spring.security.oauth2.client.registration.w3id.authorization-grant-type}")
	private String grantType;
	@Value("${spring.security.oauth2.client.provider.w3id.token-uri}")
	private String tokenEndpointUrl;
	@Value("${spring.security.oauth2.client.provider.w3id.authorization-uri}")
	private String authorizationEndpointUrl;

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Bean
	public FilterRegistrationBean<AuthenticationFilter> registerFilter() {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(this); //new AuthenticationFilter());
		                                  //without 'this' filter is not in the application context so it will not have access to configuration 
		registrationBean.addUrlPatterns("/api/v1/sso/*");
		return registrationBean;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println(
				"===> AuthenticationFilter.doFilter(ServletRequest request, ServletResponse response, FilterChain chain)");
		String url = null;
		String queryString = null;
		String uri = null;
		String schema = null;
		String server = null;
		int port = -1;

		if (request instanceof HttpServletRequest) {
			
			schema = ((HttpServletRequest) request).getScheme().toString();
			server = ((HttpServletRequest) request).getServerName().toString();
			url = ((HttpServletRequest) request).getRequestURL().toString();
			port = ((HttpServletRequest) request).getServerPort();
			queryString = ((HttpServletRequest) request).getQueryString();

			String wfmDmfUserCookie = Helpers.readServletCookie((HttpServletRequest) request, "wfm-dmf-user");
			
			System.out.println("     AuthenticationFilter.doFilter wfmDmfUserCookie="+wfmDmfUserCookie);

			if (wfmDmfUserCookie == null) {
				System.out.println("     AuthenticationFilter.doFilter (wfmDmfUserCookie==null) url="+url+ (queryString == null ? "" : "?" + queryString));
				
				Cookie wfmDmfOriginalUrlCookie = new Cookie("wfm-dmf-origial-url", url + (queryString == null ? "" : "?" + queryString));
				wfmDmfOriginalUrlCookie.setMaxAge(86400);
				wfmDmfOriginalUrlCookie.setSecure(true);
				wfmDmfOriginalUrlCookie.setHttpOnly(true);
				wfmDmfOriginalUrlCookie.setPath("/");
				((HttpServletResponse) response).addCookie(wfmDmfOriginalUrlCookie);

				String redirectUrl = this.redirectUri.replace("{baseUrl}", schema+"://"+server+(server.equalsIgnoreCase("localhost")?(":"+port):""));
				
				System.out.println("     AuthenticationFilter.doFilter (wfmDmfUserCookie==null) redirectUrl="+redirectUrl);
				System.out.println("     AuthenticationFilter.doFilter (wfmDmfUserCookie==null) sendRedirect="+this.authorizationEndpointUrl+"?scope=openid&response_type=code&client_id="
						+this.clientId+"&nonce="+Helpers.getRandomNonce(15)+"&redirect_uri="+redirectUrl);
				
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				// Add the Access-Control-Allow-Origin header to the response.
				httpResponse.setHeader("Access-Control-Allow-Origin", "https://localhost:3000"); //httpRequest.getHeader("Origin"));
				
				//((HttpServletResponse) response).sendRedirect(

				httpResponse.sendRedirect(
						this.authorizationEndpointUrl+"?scope=openid&response_type=code&client_id="
						+this.clientId+"&nonce="+Helpers.getRandomNonce(15)+"&redirect_uri="+redirectUrl);
				
				return;

			}
			else {				
				String payload = JwtDecoderUtil.decodeJwtTokenPayload(wfmDmfUserCookie);
				
				System.out.println("     AuthenticationFilter.doFilter (else) payload="+payload);
				
				ObjectMapper mapper = new ObjectMapper();
				W3UserDetail w3UserInfo = mapper.readValue(payload, W3UserDetail.class);
				
				String[] urlParts = url.split("/");
				
				String method = ((HttpServletRequest) request).getMethod();
				
				String blueGroup = null;
				String resourceName = null;
				
				if (urlParts.length>6) {
					blueGroup= "wfm-dmf-"+method.toLowerCase()+"-"+urlParts[6];
					resourceName = urlParts[6];
				}
				
				System.out.println("     AuthenticationFilter.doFilter (else) method="+method+", bluegroup="+blueGroup+", resourceName="+resourceName);

				if (!resourceName.equals("sso-login") && blueGroup!=null && !w3UserInfo.hasBluegroup(blueGroup)) {
					System.out.println("     AuthenticationFilter.doFilter (else) SENDING UNAUTHORIZED.");
					((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
					return;
				}
			}

		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// Add the Access-Control-Allow-Origin header to the response.
		httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
		
		chain.doFilter(request, response);
	}


}