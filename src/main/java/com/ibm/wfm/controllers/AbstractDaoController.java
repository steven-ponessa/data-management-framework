package com.ibm.wfm.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.FileHelpers;

public abstract class AbstractDaoController {
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private FileStorageService fileStorageService;
	
	public EtlResponse processIdl(String urlGroup, String rootFileName) {
		return processElt(urlGroup, rootFileName, true);
	}
	
	public EtlResponse processElt(String urlGroup, String rootFileName) {
		return processElt(urlGroup, rootFileName, false);
	}
	
	public EtlResponse processElt(String urlGroup, String rootFileName, boolean isIdl) {
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/{rootFileName}/etl||idl api 
		 */

		// Set tye URI Parameter values
		String oldFileName = null;
		if (!isIdl) oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"-old.csv";
		String newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"-new.csv";
		String outputFileName = rootFileName+"-etl.csv";
		
		// getting the file from disk
		FileSystemResource oldFileResource=null;
		String type = "idl";
		if (!isIdl && FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"-old.csv")) {
			oldFileResource = new FileSystemResource(new File(oldFileName));
			type = "etl";
		}
		FileSystemResource newFileResource = new FileSystemResource(new File(newFileName));
		
		// Build URI for the Offering Portfolio ETL Service
		String etlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/"+rootFileName+"/"+type) //eventually replace eds-ut-jrs-tax with urlGroup
                .toUriString();
		
		// adding headers to the api
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		if (!isIdl && oldFileResource!=null) body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		HttpEntity<MultiValueMap<String, Object>> requestEntity= new HttpEntity<>(body, headers);			
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> offeringPortfolioEtlResponseEntity = new RestTemplate().postForEntity(etlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		//etlResponses.add(offeringPortfolioEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		try {
			FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"-old.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return offeringPortfolioEtlResponseEntity.getBody();
	}

}
