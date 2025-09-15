package com.ibm.wfm.controllers;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.models.Token;
import com.ibm.wfm.models.W3UserDetail;
import com.ibm.wfm.utils.Helpers;
import com.ibm.wfm.utils.JwtDecoderUtil;


@RestController
public class Oauth2CallbackController {
		
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
	@Value("${spring.security.oauth2.client.provider.w3id.token-endpoint-uri}")
	private String tokenEndpointUrl;
	
	/*
	 * Use Spring Boot RestTemplate for POST API call following redirect.
	 */
	@GetMapping("/oauth2/callback") 
	public void oauth2CallbackRestTemplateWithRedirect ( 
 		   @RequestParam(name="code", required=false) String code
 		 , @RequestParam(name="grant_id", required=false) String grantId
 		 , HttpServletRequest request
 		 , HttpServletResponse response) throws IOException, JsonProcessingException {
		
		System.out.println(
				"===> Oauth2CallbackController.oauth2CallbackRestTemplateWithRedirect(... @RequestParam code, @RequestParam code grant_id, request, repsonse)");
		
		String schema = request.getScheme().toString();
		String server = request.getServerName().toString();
		int port = request.getServerPort();
		
		String host = schema+"://"+server+(server.equalsIgnoreCase("localhost")?(":"+port):"");
		
		Token token = getToken(code
				              ,host+"/oauth2/callback"
				              ,this.clientId
				              ,this.clientSecret
				              ,this.tokenEndpointUrl);
		
		String header = JwtDecoderUtil.decodeJwtTokenHeader(token.getId_token());
		String payload = JwtDecoderUtil.decodeJwtTokenPayload(token.getId_token());
		
		ObjectMapper mapper = new ObjectMapper();
		W3UserDetail w3UserInfo = mapper.readValue(payload, W3UserDetail.class);
		
		String wfmDmfOriginalUrl = Helpers.readServletCookie((HttpServletRequest)request, "wfm-dmf-origial-url");
		System.out.println("/oauth2/callback wfm-dmf-origial-url="+wfmDmfOriginalUrl);
		
		if (wfmDmfOriginalUrl!=null) {
			Cookie wfmDmfUserCookie = new Cookie("wfm-dmf-user", token.getId_token());
			wfmDmfUserCookie.setMaxAge(86400);
			wfmDmfUserCookie.setSecure(true);
			wfmDmfUserCookie.setHttpOnly(true);
			wfmDmfUserCookie.setPath("/");
			((HttpServletResponse)response).addCookie(wfmDmfUserCookie);
			((HttpServletResponse)response).sendRedirect(wfmDmfOriginalUrl);

		}
		else {
			((HttpServletResponse)response).sendError(HttpStatus.SC_FAILED_DEPENDENCY,"wfm-dmf-origial-url expected but not present in the response.");
		}

	}	
	
	public Token getToken(String code, String redirectUri, String clientId, String clientSecret, String accessTokenUri) {
		   
		StringBuilder requestJson = new StringBuilder("grant_type=authorization_code") 
        		.append("&code=")
        		.append(code)
        		.append("&client_id=")
        		.append(clientId)
        		.append("&client_secret=")
        		.append(clientSecret)
        		.append("&redirect_uri=")
        		.append(redirectUri);
 
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); 
		HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(),headers);  
		return new RestTemplate().postForObject(accessTokenUri, entity, Token.class);
		 
	}	

	
}
