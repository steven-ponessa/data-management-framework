package com.ibm.wfm.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FutureSkillsDim;
import com.ibm.wfm.beans.ServiceAreaDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.SkillDaoService;
import com.ibm.wfm.utils.DataMarshaller;
import com.ibm.wfm.utils.Helpers;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class SkillDaoController {
	
	@Autowired
	private SkillDaoService skillDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * SKILL FUTURE_SKILLS_DIM
	 */
	@GetMapping(path="/skill/future-skills",produces = { "application/json", "application/xml"})
	public List<FutureSkillsDim> retrieveAllCurrentFutureSkills(
			  @RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default) to get all data.") String size
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) {
		skillDaoService.setT(FutureSkillsDim.class);
		skillDaoService.setTableNm("SKILL.FUTURE_SKILLS_DIM_V");
		skillDaoService.setScdTableNm("SKILL.FUTURE_SKILLS_SCD_V");
		
		int fetchFirstXRows = 0;
		if (Helpers.isInteger(size)) fetchFirstXRows = Integer.parseInt(size);
		return skillDaoService.findAll(filters, fetchFirstXRows);
	}
	
	@GetMapping("/skill/future-skills/csv")
	public ResponseEntity<Resource> retrieveAllCurrentFutureSkills(
			  @RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default) to get all data.") String size
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, HttpServletRequest request) throws CsvValidationException, IOException, SecurityException {
		
		int fetchFirstXRows = 0;
		if (Helpers.isInteger(size)) fetchFirstXRows = Integer.parseInt(size);
		List<FutureSkillsDim> futureSkills = skillDaoService.findAll(filters, fetchFirstXRows);

		String returnFile = "future-skills.csv";
		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFile);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		DataMarshaller.writeCsv2PrintWriter(FutureSkillsDim.class, printWriter, futureSkills);
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();

		
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(returnFile);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());


        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
		
	}
	
	@GetMapping(path="/skill/future-skills/idl",produces = { "application/json", "application/xml"})
	public EtlResponse futureSkillsIdl() throws IOException, IllegalArgumentException, IllegalAccessException, SQLException {
		EtlResponse etlResponse = new EtlResponse();
		
		skillDaoService.setT(FutureSkillsDim.class);
		skillDaoService.setTableNm("SKILL.FUTURE_SKILLS_DIM_V");
		skillDaoService.setScdTableNm("SKILL.FUTURE_SKILLS_SCD_V");
		
		/*
		 * Retrieve the UT by calling the /api/v1/ut api using the default parameters of 
		 *   ocstatus: O (open offerings)
		 *   utlevel10: 10J00 (GBS)
		 */
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("wf360/future-skills")
                .toUriString();
		
		ResponseEntity<FutureSkillsDim[]> responseEntity = new RestTemplate().getForEntity(utUri, FutureSkillsDim[].class); //uriVariables); 
		FutureSkillsDim[] futureSkills = responseEntity.getBody();
		
		//copy into new List<POJO> via map
		
		int inserts = skillDaoService.insertAll(Arrays.asList(futureSkills));
		
		etlResponse.setInsertCnt(inserts);
		etlResponse.setInsertUpdateAppliedCnt(inserts);
		
		return etlResponse;
	}
	
	/*
	 * EDS SERVICE_AREA_DIM
	 */
	@DeleteMapping("/skill/future-skills")
	public int deleteAllServiceAreas() {
		skillDaoService.setT(FutureSkillsDim.class);
		skillDaoService.setTableNm("SKILL.FUTURE_SKILLS_DIM");
		skillDaoService.setScdTableNm("SKILL.FUTURE_SKILLS_SCD_V");
		return skillDaoService.deleteAll();
	}

}
