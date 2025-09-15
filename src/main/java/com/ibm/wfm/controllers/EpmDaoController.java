package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.epm.GlobalBuyingGroupDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EpmDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class EpmDaoController {
	
	@Autowired
	private EpmDaoService epmDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	

	
	/**
	 * GLOBAL_BUYING_GROUP - global-buying-groups
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param globalBuyingGroupId - global-buying-group-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/epm/global-buying-groups","/epm/global-buying-groups/{global-buying-group-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGlobalBuyingGroupByCode(
			  @PathVariable(name="global-buying-group-id", required=false) String globalBuyingGroupId
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
		
		Map<String, Object> pathVarMap = null;
		if (globalBuyingGroupId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("global-buying-group-id", globalBuyingGroupId);
		}
		
		return epmDaoService.find(GlobalBuyingGroupDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , includeParentage
		                                                     , false      //self-referencing
				                                             , null       //topNodeName
		                                                     , returnCsv
		                                                     , includePit
		                                                     , pit
		                                                     , false      //edsOnly
		                                                     , offset
		                                                     , resultSetMaxSize
		                                                     , request);
	}
	
	
	@PostMapping(value="/epm/global-buying-groups", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGlobalBuyingGroup(@RequestBody GlobalBuyingGroupDim fbsGlobalBuyingGroup) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return epmDaoService.insert(fbsGlobalBuyingGroup);
	}
	
	@DeleteMapping("/epm/global-buying-groups")
	public  ResponseEntity<Integer> deleteAllGlobalBuyingGroups(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return epmDaoService.delete(GlobalBuyingGroupDim.class, filters);
	}
	
	@DeleteMapping("/epm/global-buying-groups/{global-buying-group-id}")
	public ResponseEntity<Integer> deleteGlobalBuyingGroup(@PathVariable(name="global-buying-group-id") @Parameter(description = "FBS GlobalBuyingGroup Code") String globalBuyingGroupId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GlobalBuyingGroupDim globalBuyingGroup = new GlobalBuyingGroupDim(globalBuyingGroupId);
		return epmDaoService.delete(GlobalBuyingGroupDim.class, globalBuyingGroup);
	}	
	
	@PostMapping(value="/epm/global-buying-groups/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGlobalBuyingGroups(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="global-buying-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return epmDaoService.etl(GlobalBuyingGroupDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/epm/global-buying-groups/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGlobalBuyingGroups(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="global-buying-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return epmDaoService.etl(GlobalBuyingGroupDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

}
