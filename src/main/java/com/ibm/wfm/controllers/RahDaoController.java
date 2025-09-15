package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EbuDim;
import com.ibm.wfm.beans.RahServiceAreaDim;
import com.ibm.wfm.beans.Wf360FutureSkillsDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.RahDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class RahDaoController {
	@Autowired
	private RahDaoService rahDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	//Spring 4.3.3+
	//@GetMapping(value={"/rah/ebu","/rah/ebu/{ebu-cd}"},produces = { "application/json", "application/xml"})
	@GetMapping(path="/rah/ebus/{ebu-cd}",produces = { "application/json", "application/xml"})
	public List<EbuDim> retrieveEbu(
	//public ResponseEntity<List<EbuDim>> retrieveEbu(
			  @PathVariable(name="ebu-cd") String ebuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		rahDaoService.setT(EbuDim.class);
		rahDaoService.setTableNm("METRICS.EBU_DIM");
		rahDaoService.setForceTableNm("METRICS.EBU_DIM");
		rahDaoService.setScdTableNm("");
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"EBU_CD='"+ebuCd.trim()+"'";

		return rahDaoService.findAll(filters, null, size,null);
		//return rahDaoService.findAllRe(filters, null, size,null);
	}
	
	@GetMapping(path="/rah/ebus",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveAllEbus(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		rahDaoService.setT(EbuDim.class);
		rahDaoService.setForceTableNm("METRICS.EBU_DIM");
		rahDaoService.setScdTableNm("");
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return rahDaoService.findAll(filters, null, size);
	}	
	
	@GetMapping(path={"/rah2/ebus","/rah2/ebus/{ebu-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllEbusByCode(
			  @PathVariable(name="ebu-cd", required=false) String ebuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		rahDaoService.setForceTableNm("METRICS.EBU_DIM");
		Map<String, Object> pathVarMap = null;
		if (ebuCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ebu-cd", ebuCd);
		}
		
		return rahDaoService.find(EbuDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv,false,null, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/rah/service-areas","/rah/service-areas/{service-area-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllServiceAreasByCode(
			  @PathVariable(name="service-area-cd", required=false) String serviceAreaCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		rahDaoService.setForceTableNm("METRICS.SERVICE_AREA_DIM");
		Map<String, Object> pathVarMap = null;
		if (serviceAreaCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("svc-area-cd", serviceAreaCd);
		}
		
		return rahDaoService.find(RahServiceAreaDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, false, null, resultSetMaxSize, request);
	}
	
	@GetMapping(path="/rah/wf360-future-skills",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveAllFutureSkills(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		rahDaoService.setT(Wf360FutureSkillsDim.class);
		rahDaoService.setTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setBaseTableNm("STAGING.WF360_FUTURE_SKILLS_DIM");
		rahDaoService.setScdTableNm("");
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return rahDaoService.findAll(filters, null, size);
	}
	
	@GetMapping("/rah/wf360-future-skills/csv")
	public ResponseEntity<Resource> retrieveAllFutureSkillsCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /eds/brands
		String futureSkillsUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/rah/wf360-future-skills")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<Wf360FutureSkillsDim[]> responseEntity = new RestTemplate().getForEntity(futureSkillsUri, Wf360FutureSkillsDim[].class); //uriVariables); 
		List<Wf360FutureSkillsDim> futureSkills = Arrays.asList(responseEntity.getBody());

		return rahDaoService.returnCsv(futureSkills, "wf360-future-skills.csv", request);
	}

}
