package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.FbsConferenceDim;
import com.ibm.wfm.beans.FbsDivisionDim;
import com.ibm.wfm.beans.FbsTeamDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FbsFootballDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Thee controller takes care of mapping request data to the defined request handler method. Once response body is generated from the handler method, 
 * it converts it to JSON or XML response.
 * 
 * @author Steve Poneessa
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/api/v1")
public class FbsFootballController {
	
	private static final Logger logger = LoggerFactory.getLogger(FbsFootballController.class);
	
	@Autowired
	private FbsFootballDaoService fbsFootballDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * FBS Conferences
	 */
	
	/**
	 * 
	 * @param confCd - conf-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path={"/s/fbs-football/conferences","/s/fbs-football/conferences/{conf-cd}"},produces = { "application/json", "application/xml"})
	@PreAuthorize("hasAnyRole('wfm-dmf-get-fbs-football', 'wfm-dmf-adminxx')")
	public synchronized <T> ResponseEntity<Object> retrieveAllConferenceByCodeSecure(
			  @PathVariable(name="conf-cd", required=false) String confCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		System.out.println("retrieveAllConferenceByCode, confCd="+confCd);
		Map<String, Object> pathVarMap = null; 
		if (confCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("conf-cd", confCd);
		}
		
		return fbsFootballDaoService.find(FbsConferenceDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/fbs-football/conferences","/fbs-football/conferences/{conf-cd}"},produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllConferenceByCode(
			  @PathVariable(name="conf-cd", required=false) String confCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		System.out.println("retrieveAllConferenceByCode, confCd="+confCd);
		Map<String, Object> pathVarMap = null; 
		if (confCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("conf-cd", confCd);
		}
		
		return fbsFootballDaoService.find(FbsConferenceDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@PostMapping(value="/s/fbs-football/conferences", consumes="application/json", produces="application/json")
	@PreAuthorize("hasAnyRole('wfm-dmf-post-fbs-football', 'wfm-dmf-adminxx')")
	public ResponseEntity<Integer> insertConference(@RequestBody FbsConferenceDim fbsConference) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return fbsFootballDaoService.insert(fbsConference);
	}
	
	@DeleteMapping("/s/fbs-football/conferences")
	@PreAuthorize("hasAnyRole('wfm-dmf-delete-fbs-football', 'wfm-dmf-adminxx')")
	public  ResponseEntity<Integer> deleteAllConferences(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.delete(FbsConferenceDim.class, filters);
	}
	
	@DeleteMapping("/s/fbs-football/conferences/{conf-cd}")
	@PreAuthorize("hasAnyRole('wfm-dmf-delete-fbs-football', 'wfm-dmf-adminxx')")
	public ResponseEntity<Integer> deleteConference(@PathVariable(name="conf-cd") @Parameter(description = "FBS Conference Code") String confCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		FbsConferenceDim fbsConferenceDim = new FbsConferenceDim(confCd);
		return fbsFootballDaoService.delete(FbsConferenceDim.class, fbsConferenceDim);
	}	
	
	@PostMapping(value="/fbs-football/conferences/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlConferences(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-conferences.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsConferenceDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/fbs-football/conferences/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlConferences(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-conferences.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsConferenceDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	
	/**
	 * FBS Divisions
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param filters - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols - Order by columns. Default="" (orders by natural key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of taxonomy. Default="".
	 * @param returnCsv - API to return results as CSV. Default=false.
	 * @param includePit - API to include Point in Time (PIT) data. Default=false.
	 * @param pit - Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0). Default="" (return CURRENT state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get all data. Default="All".
	 * @param request - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path="/s/fbs-football/divisions",produces = { "application/json", "application/xml"})
	@PreAuthorize("hasAnyRole('wfm-dmf-get-fbs-football', 'wfm-dmf-adminxx')")
	public synchronized <T> ResponseEntity<Object> retrieveAllDivisions(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the result to be returned, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
                                   //find(type,paramVarMap, filters, orderByCols, includeParentage, topNodeNm, returnCsv, includePit, pit, resultSetMaxSize, request)
		System.out.println("/fbs-football/divisions retrieveAllDivisions");
		return fbsFootballDaoService.find(FbsDivisionDim.class, null, filters, orderByCols, includeParentage, "FBS", returnCsv, includePit, pit, resultSetMaxSize, request);	
	}
	
	/**
	 * 
	 * @param divCd - div-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/s/fbs-football/divisions/{div-cd}",produces = { "application/json", "application/xml"})
	@PreAuthorize("hasAnyRole('wfm-dmf-get-fbs-football', 'wfm-dmf-adminxx')")
	public <T> ResponseEntity<Object> retrieveAllDivisionByCode(
			  @PathVariable(name="div-cd") String divCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("div-cd", divCd);
		
		return fbsFootballDaoService.find(FbsDivisionDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * 
	 * @param confCd - conf-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/fbs-football/conferences/{conf-cd}/divisions",produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllDivisionByConfCode(
			  @PathVariable(name="conf-cd") String confCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		
		return fbsFootballDaoService.find(FbsDivisionDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * 
	 * @param confCd - conf-cd PathVariable. Note that the path variable name must match the table column name.
	 * @param divCd - div-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/fbs-football/conferences/{conf-cd}/divisions/{div-cd}",produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllDivisionByConfDivCode(
			  @PathVariable(name="conf-cd") String confCd
			, @PathVariable(name="div-cd") String divCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		pathVarMap.put("div-cd", divCd);
		
		return fbsFootballDaoService.find(FbsDivisionDim.class, pathVarMap, filters, orderByCols, includeParentage, "top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@PostMapping(value="/s/fbs-football/divisions", consumes="application/json", produces="application/json")
	@PreAuthorize("hasAnyRole('wfm-dmf-post-fbs-football', 'wfm-dmf-adminxx')")
	public ResponseEntity<Integer> insertDivision(@RequestBody FbsDivisionDim fbsDivision) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return fbsFootballDaoService.insert(fbsDivision);
	}
	
	@DeleteMapping("/s/fbs-football/divisions")
	@PreAuthorize("hasAnyRole('wfm-dmf-delete-fbs-football', 'wfm-dmf-adminxx')")
	public  ResponseEntity<Integer> deleteAllDivisions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.delete(FbsDivisionDim.class, filters);
	}
	
	@DeleteMapping("/s/fbs-football/divisions/{div-cd}")
	@PreAuthorize("hasAnyRole('wfm-dmf-delete-fbs-football', 'wfm-dmf-adminxx')")
	public ResponseEntity<Integer> deleteDataSource(@PathVariable(name="div-cd") @Parameter(description = "FBS Division Code") String divCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		FbsDivisionDim fbsDivisionDim = new FbsDivisionDim(divCd);
		return fbsFootballDaoService.delete(FbsDivisionDim.class, fbsDivisionDim);
	}	
	
	@PostMapping(value="/fbs-football/divisions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlDivisions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-divisions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsDivisionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/fbs-football/divisions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlDivisions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-divisions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsDivisionDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	/**
	 * FBS_TEAM
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param filters - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols - Order by columns. Default="" (orders by natural key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of taxonomy. Default="".
	 * @param returnCsv - API to return results as CSV. Default=false.
	 * @param includePit - API to include Point in Time (PIT) data. Default=false.
	 * @param pit - Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0). Default="" (return CURRENT state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get all data. Default="All".
	 * @param request - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path="/fbs-football/teams/xxx",produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllFbsTeams(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the result to be returned, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
                                   //find(type,paramVarMap, filters, orderByCols, includeParentage, topNodeNm, returnCsv, includePit, pit, resultSetMaxSize, request)
		return fbsFootballDaoService.find(FbsTeamDim.class, null, filters, orderByCols, includeParentage, "FBS", returnCsv, includePit, pit, resultSetMaxSize, request);	
	}
	
	/**
	 * 
	 * @param teamCd - team-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path={"/fbs-football/teams","/fbs-football/teams/{team-cd}"},produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllFbsTeamByCode(
			  @PathVariable(name="team-cd", required=false) String teamCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		System.out.println("/fbs-football/teams retrieveAllFbsTeamByCode, teamCd="+teamCd);
		Map<String, Object> pathVarMap = null;
		if (teamCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("team-cd", teamCd);
		}
		
		return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/fbs-football/teams/count"},produces = { "application/json", "application/xml"})
	public synchronized ResponseEntity<Count> retrieveCountFbsTeamByCode(
              @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;

		//return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
		return fbsFootballDaoService.countAll(FbsTeamDim.class, pathVarMap, filters, includePit, null, false);
		                                  
	}
	
	/**
	 * @param confCd - conf-cd PathVariable
	 * @param teamCd - team-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/fbs-football/conferences/{conf-cd}/teams/{team-cd}",produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllFbsTeamByConfCode(
			  @PathVariable(name="conf-cd") String confCd
			, @PathVariable(name="team-cd") String teamCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			//, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		pathVarMap.put("team-cd", teamCd);
		
		return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, null, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * @param confCd - conf-cd PathVariable
	 * @param divCd - div-cd PathVariable
	 * @param teamCd - team-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path={"/fbs-football/conferences/{conf-cd}/divisions/{div-cd}/teams"
			         ,"/fbs-football/conferences/{conf-cd}/divisions/{div-cd}/teams/{team-cd}"}
	           ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllFbsTeamByDivConfCode(
			  @PathVariable(name="conf-cd") String confCd
			, @PathVariable(name="div-cd") String divCd
			, @PathVariable(name="team-cd", required=false) String teamCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			//, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		pathVarMap.put("div-cd", divCd);
		if (teamCd!=null)
			pathVarMap.put("team-cd", teamCd);
		
		return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, null, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * @param confCd - conf-cd PathVariable
	 * @param divCd - div-cd PathVariable
	 * @param teamCd - team-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/fbs-football/conferences/{conf-cd}/divisions/{div-cd}/teams/xxx",produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllFbsTeamsByDivConfCode(
			  @PathVariable(name="conf-cd") String confCd
			, @PathVariable(name="div-cd") String divCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			//, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		pathVarMap.put("div-cd", divCd);
		
		return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, null, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * @param confCd - conf-cd PathVariable
	 * @param divCd - div-cd PathVariable
	 * @param teamCd - team-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path="/fbs-football/conferences/{conf-cd}/teams",produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllFbsTeamsByConfCode(
			  @PathVariable(name="conf-cd") String confCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			//, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
		
		return fbsFootballDaoService.find(FbsTeamDim.class, pathVarMap, filters, null, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/fbs-football/teams", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertFbsTeam(@RequestBody FbsTeamDim fbsFbsTeam) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return fbsFootballDaoService.insert(fbsFbsTeam);
	}
	
	@DeleteMapping("/fbs-football/teams")
	public  ResponseEntity<Integer> deleteAllFbsTeams(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.delete(FbsTeamDim.class, filters);
	}
	
	@DeleteMapping("/fbs-football/teams/{team-cd}")
	public ResponseEntity<Integer> deleteFbsTeam(@PathVariable(name="team-cd") @Parameter(description = "FBS FbsTeam Code") String teamCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		FbsTeamDim fbsTeam = new FbsTeamDim(teamCd);
		return fbsFootballDaoService.delete(FbsTeamDim.class, fbsTeam);
	}	
	
	@PostMapping(value="/fbs-football/teams/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlFbsTeams(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-teams.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsTeamDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/fbs-football/teams/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlFbsTeams(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-teams.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return fbsFootballDaoService.etl(FbsTeamDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	
}