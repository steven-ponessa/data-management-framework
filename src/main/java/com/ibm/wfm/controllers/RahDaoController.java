package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EbuDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.Wf360FutureSkillsDim;
import com.ibm.wfm.beans.rah.LaborGroupDim;
import com.ibm.wfm.beans.rah.LaborPoolLevel1Dim;
import com.ibm.wfm.beans.rah.LaborPoolLevel2Dim;
import com.ibm.wfm.beans.rah.LaborTypeDim;
import com.ibm.wfm.beans.rah.RahServiceAreaDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.RahDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class RahDaoController {
	@Autowired
	private RahDaoService wfmRahDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	//Spring 4.3.3+
	//@GetMapping(value={"/rah/ebu","/rah/ebu/{ebu-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path="/rah/ebus/{ebu-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EbuDim> retrieveEbu(
	//public ResponseEntity<List<EbuDim>> retrieveEbu(
			  @PathVariable(name="ebu-cd") String ebuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"EBU_CD='"+ebuCd.trim()+"'";

		return wfmRahDaoService.findAll(EbuDim.class, filters, null, size,null);
	}
	
	@GetMapping(path="/rah/ebus",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<EbuDim> retrieveAllEbus(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return wfmRahDaoService.findAll(EbuDim.class, filters, null, size);
	}	
	
	@GetMapping(path={"/rah2/ebus","/rah2/ebus/{ebu-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllEbusByCode(
			  @PathVariable(name="ebu-cd", required=false) String ebuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (ebuCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ebu-cd", ebuCd);
		}
		
		return wfmRahDaoService.find(EbuDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv,false,null, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/rah/service-areas","/rah/service-areas/{service-area-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllServiceAreasByCode(
			  @PathVariable(name="service-area-cd", required=false) String serviceAreaCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (serviceAreaCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("svc-area-cd", serviceAreaCd);
		}
		
		return wfmRahDaoService.find(RahServiceAreaDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, false, null, resultSetMaxSize, request);
	}
	
	@GetMapping(path="/rah/wf360-future-skills",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<Wf360FutureSkillsDim> retrieveAllFutureSkills(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return wfmRahDaoService.findAll(Wf360FutureSkillsDim.class, filters, null, size);
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

		return wfmRahDaoService.returnCsv(futureSkills, "wf360-future-skills.csv", request);
	}
	

	/**
	 * LABOR_TYPE_DIM - labor-type-dims
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param lbrTypKey - lbr-typ-key PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-rah/labor-type-dims","/wfm-rah/labor-type-dims/{lbr-typ-key}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllLaborTypeDimByCode(
			  @PathVariable(name="lbr-typ-key", required=false) String lbrTypKey
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
		if (lbrTypKey!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("lbr-typ-key", lbrTypKey);
		}
		
		return wfmRahDaoService.find(LaborTypeDim.class
				, pathVarMap                         // Map<String, Object> pathVarMap
				, filters                            // String filters
				, orderByCols                        // String orderByCols
				, includeParentage                   // boolean includeParentage
				, false                              // boolean selfReferencing
				, "Top"                              // String topNodeNm
				, returnCsv                          // boolean returnCsv
				, includePit                         // boolean includePit
				, pit                                // String pit
				, false                              // boolean edsOnly
				, offset                             // int offset
				, resultSetMaxSize                   // String resultSetMaxSize
				, null                               // String pathName
				, null                               // Integer orgMask
				, request                            // HttpServletRequest request
		                                                     );
	}
	
	
	/*
	 * Only expose GET APIs
	
	@PostMapping(value="/wfm-rah/labor-type-dims", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborTypeDim(@RequestBody LaborTypeDim laborTypeDim) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmRahDaoService.insert(laborTypeDim);
	}
	
	@DeleteMapping("/wfm-rah/labor-type-dims")
	public  ResponseEntity<Integer> deleteAllLaborTypeDims(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.delete(LaborTypeDim.class, filters);
	}
	
	@DeleteMapping("/wfm-rah/labor-type-dims/{lbr-typ-key}")
	public ResponseEntity<Integer> deleteLaborTypeDim(@PathVariable(name="lbr-typ-key") @Parameter(description = "LaborTypeDim Code") String lbrTypKey) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborTypeDim laborTypeDim = new LaborTypeDim(Integer.valueOf(lbrTypKey));
		return wfmRahDaoService.delete(LaborTypeDim.class, laborTypeDim);
	}	
	
	@PostMapping(value="/wfm-rah/labor-type-dims/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborTypeDims(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-type-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-rah/labor-type-dims/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborTypeDims(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-type-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	*/

	
	/**
	 * LABOR_POOL_LEVEL_1_DIM - labor-pool-level-1-dims
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param lbrPoolLvl1Key - lbr-pool-lvl-1-key PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-rah/labor-pool-level-1-dims","/wfm-rah/labor-pool-level-1-dims/{lbr-pool-lvl-1-key}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllLaborPoolLevel1DimByCode(
			  @PathVariable(name="lbr-pool-lvl-1-key", required=false) String lbrPoolLvl1Key
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
		if (lbrPoolLvl1Key!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("lbr-pool-lvl-1-key", lbrPoolLvl1Key);
		}
		
		return wfmRahDaoService.find(LaborPoolLevel1Dim.class
				, pathVarMap                         // Map<String, Object> pathVarMap
				, filters                            // String filters
				, orderByCols                        // String orderByCols
				, includeParentage                   // boolean includeParentage
				, false                              // boolean selfReferencing
				, "Top"                              // String topNodeNm
				, returnCsv                          // boolean returnCsv
				, includePit                         // boolean includePit
				, pit                                // String pit
				, false                              // boolean edsOnly
				, offset                             // int offset
				, resultSetMaxSize                   // String resultSetMaxSize
				, null                               // String pathName
				, null                               // Integer orgMask
				, request                            // HttpServletRequest request
		                                                     );
	}
	
	/*
	 * Only expose GET APIs
	 *	
	@PostMapping(value="/wfm-rah/labor-pool-level-1-dims", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborPoolLevel1Dim(@RequestBody LaborPoolLevel1Dim laborPoolLevel1Dim) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmRahDaoService.insert(laborPoolLevel1Dim);
	}
	
	@DeleteMapping("/wfm-rah/labor-pool-level-1-dims")
	public  ResponseEntity<Integer> deleteAllLaborPoolLevel1Dims(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.delete(LaborPoolLevel1Dim.class, filters);
	}
	
	@DeleteMapping("/wfm-rah/labor-pool-level-1-dims/{lbr-pool-lvl-1-key}")
	public ResponseEntity<Integer> deleteLaborPoolLevel1Dim(@PathVariable(name="lbr-pool-lvl-1-key") @Parameter(description = "LaborPoolLevel1Dim Code") String lbrPoolLvl1Key) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborPoolLevel1Dim laborPoolLevel1Dim = new LaborPoolLevel1Dim(Integer.valueOf(lbrPoolLvl1Key));
		return wfmRahDaoService.delete(LaborPoolLevel1Dim.class, laborPoolLevel1Dim);
	}	
	
	@PostMapping(value="/wfm-rah/labor-pool-level-1-dims/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborPoolLevel1Dims(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pool-level-1-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborPoolLevel1Dim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-rah/labor-pool-level-1-dims/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborPoolLevel1Dims(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pool-level-1-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborPoolLevel1Dim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	*/
	
	/**
	 * LABOR_POOL_LEVEL_2_DIM - labor-pool-level-2-dims
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param lbrPoolLvl2Key - lbr-pool-lvl-2-key PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-rah/labor-pool-level-2-dims","/wfm-rah/labor-pool-level-2-dims/{lbr-pool-lvl-2-key}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllLaborPoolLevel2DimByCode(
			  @PathVariable(name="lbr-pool-lvl-2-key", required=false) String lbrPoolLvl2Key
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(required=false) @Parameter(description = "Path name") String pathName
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (lbrPoolLvl2Key!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("lbr-pool-lvl-2-key", lbrPoolLvl2Key);
		}
		
		return wfmRahDaoService.find(LaborPoolLevel2Dim.class
				, pathVarMap                         // Map<String, Object> pathVarMap
				, filters                            // String filters
				, orderByCols                        // String orderByCols
				, includeParentage                   // boolean includeParentage
				, false                              // boolean selfReferencing
				, "Top"                              // String topNodeNm
				, returnCsv                          // boolean returnCsv
				, includePit                         // boolean includePit
				, pit                                // String pit
				, false                              // boolean edsOnly
				, offset                             // int offset
				, resultSetMaxSize                   // String resultSetMaxSize
				, pathName                           // String pathName
				, null                               // Integer orgMask
				, request                            // HttpServletRequest request
		                                                     );
	}
	
	/*
	 * Only expose GET APIs
	@PostMapping(value="/wfm-rah/labor-pool-level-2-dims", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborPoolLevel2Dim(@RequestBody LaborPoolLevel2Dim laborPoolLevel2Dim) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmRahDaoService.insert(laborPoolLevel2Dim);
	}
	
	@DeleteMapping("/wfm-rah/labor-pool-level-2-dims")
	public  ResponseEntity<Integer> deleteAllLaborPoolLevel2Dims(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.delete(LaborPoolLevel2Dim.class, filters);
	}
	
	@DeleteMapping("/wfm-rah/labor-pool-level-2-dims/{lbr-pool-lvl-2-key}")
	public ResponseEntity<Integer> deleteLaborPoolLevel2Dim(@PathVariable(name="lbr-pool-lvl-2-key") @Parameter(description = "LaborPoolLevel2Dim Code") String lbrPoolLvl2Key) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborPoolLevel2Dim laborPoolLevel2Dim = new LaborPoolLevel2Dim(Integer.valueOf(lbrPoolLvl2Key));
		return wfmRahDaoService.delete(LaborPoolLevel2Dim.class, laborPoolLevel2Dim);
	}	
	
	@PostMapping(value="/wfm-rah/labor-pool-level-2-dims/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborPoolLevel2Dims(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pool-level-2-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborPoolLevel2Dim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-rah/labor-pool-level-2-dims/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborPoolLevel2Dims(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pool-level-2-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborPoolLevel2Dim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	*/	

	
	/**
	 * LABOR_GROUP_DIM - labor-group-dims
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param lbrTypKey - lbr-typ-key PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-rah/labor-group-dims","/wfm-rah/labor-group-dims/{lbr-grp-key}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllLaborGroupDimByCode(
			  @PathVariable(name="lbr-grp-key", required=false) String lbrGrpKey
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
		if (lbrGrpKey!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("lbr-grp-key", lbrGrpKey);
		}
		
		return wfmRahDaoService.find(
				   LaborGroupDim.class                 // Class type
				  , pathVarMap                         // Map<String, Object> pathVarMap
				  , filters                            // String filters
				  , orderByCols                        // String orderByCols
				  , includeParentage                   // boolean includeParentage
				  , false                              // boolean selfReferencing
				  , "Top"                              // String topNodeNm
				  , returnCsv                          // boolean returnCsv
				  , includePit                         // boolean includePit
				  , pit                                // String pit
				  , false                              // boolean edsOnly
				  , offset                             // int offset
				  , resultSetMaxSize                   // String resultSetMaxSize
				  , null                               // String pathName
				  , null                               // Integer orgMask
				  , request                            // HttpServletRequest request
		                                                     );
	}
	
	/*
	 * Only expose GET APIs
	 *
	@PostMapping(value="/wfm-rah/labor-group-dims", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborGroupDim(@RequestBody LaborGroupDim laborGroupDim) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmRahDaoService.insert(laborGroupDim);
	}
	
	@DeleteMapping("/wfm-rah/labor-group-dims")
	public  ResponseEntity<Integer> deleteAllLaborGroupDims(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.delete(LaborGroupDim.class, filters);
	}
	
	@DeleteMapping("/wfm-rah/labor-group-dims/{lbr-grp-key}")
	public ResponseEntity<Integer> deleteLaborGroupDim(@PathVariable(name="lbr-grp-key") @Parameter(description = "LaborGroupDim Code") String lbrGrpKey) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborGroupDim laborGroupDim = new LaborGroupDim(Integer.valueOf(lbrGrpKey));
		return wfmRahDaoService.delete(LaborGroupDim.class, laborGroupDim);
	}	
	
	@PostMapping(value="/wfm-rah/labor-group-dims/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborGroupDims(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-group-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborGroupDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-rah/labor-group-dims/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborGroupDims(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-group-dims.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmRahDaoService.etl(LaborGroupDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	*/

}
