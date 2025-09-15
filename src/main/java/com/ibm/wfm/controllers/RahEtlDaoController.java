package com.ibm.wfm.controllers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.beans.EbuDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FutureSkillsDim;
import com.ibm.wfm.beans.BmsGeographyDim;
import com.ibm.wfm.beans.Wf360FutureSkillsDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.RahEtlDaoService;
import com.ibm.wfm.utils.DataMarshaller;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class RahEtlDaoController {
	@Autowired
	private RahEtlDaoService rahDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	//Spring 4.3.3+

	
	@DeleteMapping("/rah/wf360-future-skills")
	public int deleteAllFutureSkillss() {
		rahDaoService.setT(Wf360FutureSkillsDim.class);
		rahDaoService.setTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setBaseTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setScdTableNm("STAGING.WF360_FUTURE_SKILLS_SCD_V");
		return rahDaoService.deleteAll();
	}
	
	@GetMapping(path="/rah/wf360-future-skills/idl",produces = { "application/json", "application/xml"})
	public EtlResponse futureSkillsIdl() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, SQLException {
		EtlResponse etlResponse = new EtlResponse();	
		rahDaoService.setT(Wf360FutureSkillsDim.class);
		rahDaoService.setTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setBaseTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setScdTableNm("STAGING.WF360_FUTURE_SKILLS_SCD_V");
		
		/*
		 * Retrieve the UT by calling the /api/v1/ut api using the default parameters of 
		 *   ocstatus: O (open offerings)
		 *   utlevel10: 10J00 (GBS)
		 */
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("skill/future-skills")
                .toUriString();
		
		ResponseEntity<FutureSkillsDim[]> responseEntity = new RestTemplate().getForEntity(utUri, FutureSkillsDim[].class); //uriVariables); 
		FutureSkillsDim[] futureSkills = responseEntity.getBody();
		
		//copy into new List<POJO> via map
		File jsonFile = new File(fileStorageProperties.getMapDir()+"/wf36-wmf-future-skills.json");
		HashMap<String, String> map = new ObjectMapper().readValue(jsonFile, HashMap.class);
		//Populate the map.
		List<Wf360FutureSkillsDim> futureSkillsTarget = DataMarshaller.mapList(Arrays.asList(futureSkills), FutureSkillsDim.class, Wf360FutureSkillsDim.class, map);
		futureSkills=null;
		
		int inserts = rahDaoService.insertAll(futureSkillsTarget);
		
		etlResponse.setInsertCnt(inserts);
		etlResponse.setInsertUpdateAppliedCnt(inserts);
		
		return etlResponse;
	}

}
