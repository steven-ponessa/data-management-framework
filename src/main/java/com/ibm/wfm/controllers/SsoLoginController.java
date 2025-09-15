package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.models.W3UserDetail;
import com.ibm.wfm.utils.Helpers;
import com.ibm.wfm.utils.JwtDecoderUtil;

@RestController
@RequestMapping("/api/v1/s")
@CrossOrigin(origins = "https://localhost:3000")
public class SsoLoginController {
	
	@GetMapping(path={"/sso-login/login"},produces = { "application/json", "application/xml"})
	public ResponseEntity<W3UserDetail> ssoLogin( HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {
		

		String wfmDmfUserCookie = Helpers.readServletCookie((HttpServletRequest) request, "wfm-dmf-user");
		String payload = JwtDecoderUtil.decodeJwtTokenPayload(wfmDmfUserCookie);
		ObjectMapper mapper = new ObjectMapper();
		W3UserDetail w3UserInfo = mapper.readValue(payload, W3UserDetail.class);
		
		return ResponseEntity.ok().body(w3UserInfo);
	}
	
	@GetMapping(path={"/sso-login/logout"},produces = { "application/json", "application/xml"})
	public ResponseEntity<Object> ssoLogout( HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {
		
		 ResponseCookie deleteSpringCookie = ResponseCookie
			        .from("wfm-dmf-user", null)
			        .maxAge(0)
			        .build();

		 return ResponseEntity
			        .ok()
			        .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
			        .build();

	}

}
