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
import com.ibm.wfm.beans.prom.GrOpnsetDtlTDim;
import com.ibm.wfm.beans.prom.GrOpnsetPosTDim;
import com.ibm.wfm.beans.prom.GrProjTDim;
import com.ibm.wfm.beans.prom.GrdsClntTDim;
import com.ibm.wfm.beans.prom.KafkaAvailDtTDim;
import com.ibm.wfm.beans.prom.KafkaMgsTDim;
import com.ibm.wfm.beans.prom.OpnsetDtlTDim;
import com.ibm.wfm.beans.prom.OpnsetLangTDim;
import com.ibm.wfm.beans.prom.OpnsetPosCandStatTDim;
import com.ibm.wfm.beans.prom.OpnsetPosCandTDim;
import com.ibm.wfm.beans.prom.OpnsetPosTDim;
import com.ibm.wfm.beans.prom.OpnsetTDim;
import com.ibm.wfm.beans.prom.SubResrcReqTDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.PromDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class WfmIhubController {
	@Autowired
	//private EdsDaoService wfmIhubDaoService; //For test, switches Database to WFM_Bridge
	private PromDaoService wfmIhubDaoService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * OPNSET_T - opnset-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param openSeatId - open-seat-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-ts","/wfm-ihub/opnset-ts/{open-seat-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetTByCode(
			  @PathVariable(name="open-seat-id", required=false) String openSeatId
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
		if (openSeatId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-id", Integer.valueOf(openSeatId));
		}
		
		return wfmIhubDaoService.find(OpnsetTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOpnsetT(@RequestBody OpnsetTDim fbsOpnsetT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = false;
		String returningColumnName = "OPNSET_ID";
		return wfmIhubDaoService.insert(fbsOpnsetT, returnGeneratedKeys, returningColumnName);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-ts/{open-seat-id}")
	public ResponseEntity<Integer> deleteOpnsetT(@PathVariable(name="open-seat-id") @Parameter(description = "FBS OpnsetT Code") String openSeatId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetTDim opnsetT = new OpnsetTDim(Long.valueOf(openSeatId));
		return wfmIhubDaoService.delete(OpnsetTDim.class, opnsetT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * OPNSET_DTL_T - opnset-dtl-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetId - opnset-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-dtl-ts","/wfm-ihub/opnset-dtl-ts/{opnset-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetDtlTByCode(
			  @PathVariable(name="opnset-id", required=false) String opnsetId
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
		if (opnsetId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-id", opnsetId);
		}
		
		return wfmIhubDaoService.find(OpnsetDtlTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-dtl-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOpnsetDtlT(@RequestBody OpnsetDtlTDim fbsOpnsetDtlT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmIhubDaoService.insert(fbsOpnsetDtlT);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-dtl-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetDtlTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetDtlTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-dtl-ts/{opnset-id}")
	public ResponseEntity<Integer> deleteOpnsetDtlT(@PathVariable(name="opnset-id") @Parameter(description = "FBS OpnsetDtlT Code") String opnsetId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetDtlTDim opnsetDtlT = new OpnsetDtlTDim(Long.valueOf(opnsetId));
		return wfmIhubDaoService.delete(OpnsetDtlTDim.class, opnsetDtlT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-dtl-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetDtlTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-dtl-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetDtlTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-dtl-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetDtlTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-dtl-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetDtlTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * OPNSET_POS_T - opnset-pos-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetPosId - opnset-pos-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-pos-ts","/wfm-ihub/opnset-pos-ts/{opnset-pos-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetPosTByCode(
			  @PathVariable(name="opnset-pos-id", required=false) String opnsetPosId
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
		if (opnsetPosId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-pos-id", opnsetPosId);
		}
		
		return wfmIhubDaoService.find(OpnsetPosTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-ts", consumes="application/json", produces="application/json")
    @Operation(summary = "Create a new resource")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Resource created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
	public ResponseEntity<Integer> insertOpnsetPosT(@RequestBody OpnsetPosTDim fbsOpnsetPosT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = false;
		return wfmIhubDaoService.insert(fbsOpnsetPosT, returnGeneratedKeys, null);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetPosTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetPosTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-ts/{opnset-pos-id}")
	public ResponseEntity<Integer> deleteOpnsetPosT(@PathVariable(name="opnset-pos-id") @Parameter(description = "FBS OpnsetPosT Code") String opnsetPosId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetPosTDim opnsetPosT = new OpnsetPosTDim(Long.valueOf(opnsetPosId));
		return wfmIhubDaoService.delete(OpnsetPosTDim.class, opnsetPosT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetPosTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-pos-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetPosTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	/**
	 * OPNSET_LANG_T - opnset-lang-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetId - opnset-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-lang-ts","/wfm-ihub/opnset-lang-ts/{opnset-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetLangTByCode(
			  @PathVariable(name="opnset-id", required=false) String opnsetId
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
		if (opnsetId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-id", opnsetId);
		}
		
		return wfmIhubDaoService.find(OpnsetLangTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-lang-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOpnsetLangT(@RequestBody OpnsetLangTDim fbsOpnsetLangT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmIhubDaoService.insert(fbsOpnsetLangT);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-lang-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetLangTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetLangTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-lang-ts/{opnset-id}/{lang-cd}")
	public ResponseEntity<Integer> deleteOpnsetLangT(@PathVariable(name="opnset-id") @Parameter(description = "FBS OpnsetLangT Code") String opnsetId
			                                       , @PathVariable(name="lang-cd") @Parameter(description = "FBS OpnsetLangT Code") String langCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetLangTDim opnsetLangT = new OpnsetLangTDim(Long.valueOf(opnsetId), langCd);
		return wfmIhubDaoService.delete(OpnsetLangTDim.class, opnsetLangT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-lang-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetLangTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-lang-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetLangTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-lang-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetLangTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-lang-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetLangTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	/**
	 * SUB_RESRC_REQ_T - sub-resrc-req-ts
	 */
    /**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param subResrcReqId - sub-resrc-req-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/sub-resrc-req-ts","/wfm-ihub/sub-resrc-req-ts/{sub-resrc-req-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSubResrcReqTByCode(
			  @PathVariable(name="sub-resrc-req-id", required=false) String subResrcReqId
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
		if (subResrcReqId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("sub-resrc-req-id", subResrcReqId);
		}
		
		return wfmIhubDaoService.find(SubResrcReqTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/sub-resrc-req-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSubResrcReqT(@RequestBody SubResrcReqTDim fbsSubResrcReqT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = true;
		return wfmIhubDaoService.insert(fbsSubResrcReqT, returnGeneratedKeys, null);
	}
	
	@DeleteMapping("/wfm-ihub/sub-resrc-req-ts")
	public  ResponseEntity<Integer> deleteAllSubResrcReqTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(SubResrcReqTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/sub-resrc-req-ts/{sub-resrc-req-id}")
	public ResponseEntity<Integer> deleteSubResrcReqT(@PathVariable(name="sub-resrc-req-id") @Parameter(description = "FBS SubResrcReqT Code") String subResrcReqId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SubResrcReqTDim subResrcReqT = new SubResrcReqTDim(Long.valueOf(subResrcReqId));
		return wfmIhubDaoService.delete(SubResrcReqTDim.class, subResrcReqT);
	}	
	
	@PostMapping(value="/wfm-ihub/sub-resrc-req-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSubResrcReqTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sub-resrc-req-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(SubResrcReqTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/sub-resrc-req-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSubResrcReqTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sub-resrc-req-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(SubResrcReqTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * GR_OPNSET_DTL_T - gr-opnset-dtl-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetId - opnset-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/gr-opnset-dtl-ts","/wfm-ihub/gr-opnset-dtl-ts/{opnset-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGrOpnsetDtlTByCode(
			  @PathVariable(name="opnset-id", required=false) String opnsetId
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
		if (opnsetId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-id", opnsetId);
		}
		
		return wfmIhubDaoService.find(GrOpnsetDtlTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/gr-opnset-dtl-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGrOpnsetDtlT(@RequestBody GrOpnsetDtlTDim fbsGrOpnsetDtlT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmIhubDaoService.insert(fbsGrOpnsetDtlT);
	}
	
	@DeleteMapping("/wfm-ihub/gr-opnset-dtl-ts")
	public  ResponseEntity<Integer> deleteAllGrOpnsetDtlTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(GrOpnsetDtlTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/gr-opnset-dtl-ts/{opnset-id}")
	public ResponseEntity<Integer> deleteGrOpnsetDtlT(@PathVariable(name="opnset-id") @Parameter(description = "FBS GrOpnsetDtlT Code") String opnsetId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrOpnsetDtlTDim grOpnsetDtlT = new GrOpnsetDtlTDim(Long.valueOf(opnsetId));
		return wfmIhubDaoService.delete(GrOpnsetDtlTDim.class, grOpnsetDtlT);
	}	
	
	@PostMapping("/wfm-ihub/gr-opnset-dtl-ts/idl")
	public ResponseEntity<EtlResponse> idlGrOpnsetDtlTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gr-opnset-dtl-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrOpnsetDtlTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping("/wfm-ihub/gr-opnset-dtl-ts/etl")
	public ResponseEntity<EtlResponse> etlGrOpnsetDtlTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gr-opnset-dtl-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrOpnsetDtlTDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * GR_OPNSET_POS_T - gr-opnset-pos-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param grOpnsetPosId - gr-opnset-pos-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/gr-opnset-pos-ts","/wfm-ihub/gr-opnset-pos-ts/{gr-opnset-pos-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGrOpnsetPosTByCode(
			  @PathVariable(name="gr-opnset-pos-id", required=false) String grOpnsetPosId
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
		if (grOpnsetPosId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("gr-opnset-pos-id", grOpnsetPosId);
		}
		
		return wfmIhubDaoService.find(GrOpnsetPosTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/gr-opnset-pos-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGrOpnsetPosT(@RequestBody GrOpnsetPosTDim fbsGrOpnsetPosT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = false;
		return wfmIhubDaoService.insert(fbsGrOpnsetPosT, returnGeneratedKeys, null);
		//return wfmIhubDaoService.insert(fbsGrOpnsetPosT);
	}
	
	@DeleteMapping("/wfm-ihub/gr-opnset-pos-ts")
	public  ResponseEntity<Integer> deleteAllGrOpnsetPosTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(GrOpnsetPosTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/gr-opnset-pos-ts/{gr-opnset-pos-id}")
	public ResponseEntity<Integer> deleteGrOpnsetPosT(@PathVariable(name="gr-opnset-pos-id") @Parameter(description = "FBS GrOpnsetPosT Code") String grOpnsetPosId
			) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrOpnsetPosTDim grOpnsetPosT = new GrOpnsetPosTDim(Long.valueOf(grOpnsetPosId));
		return wfmIhubDaoService.delete(GrOpnsetPosTDim.class, grOpnsetPosT);
	}	
	
	@PostMapping("/wfm-ihub/gr-opnset-pos-ts/idl")
	public ResponseEntity<EtlResponse> idlGrOpnsetPosTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gr-opnset-pos-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrOpnsetPosTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping("/wfm-ihub/gr-opnset-pos-ts/etl")
	public ResponseEntity<EtlResponse> etlGrOpnsetPosTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gr-opnset-pos-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrOpnsetPosTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * OPNSET_POS_CAND_T - opnset-pos-cand-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetPosCandId - opnset-pos-cand-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-pos-cand-ts","/wfm-ihub/opnset-pos-cand-ts/{opnset-pos-cand-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetPosCandTByCode(
			  @PathVariable(name="opnset-pos-cand-id", required=false) String opnsetPosCandId
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
		if (opnsetPosCandId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-pos-cand-id", opnsetPosCandId);
		}
		
		return wfmIhubDaoService.find(OpnsetPosCandTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-cand-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOpnsetPosCandT(@RequestBody OpnsetPosCandTDim fbsOpnsetPosCandT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = true;
		return wfmIhubDaoService.insert(fbsOpnsetPosCandT, returnGeneratedKeys, null);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-cand-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetPosCandTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetPosCandTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-cand-ts/{opnset-pos-cand-id}")
	public ResponseEntity<Integer> deleteOpnsetPosCandT(@PathVariable(name="opnset-pos-cand-id") @Parameter(description = "OpnsetPosCandT Code") String opnsetPosCandId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetPosCandTDim opnsetPosCandT = new OpnsetPosCandTDim(Long.valueOf(opnsetPosCandId));
		return wfmIhubDaoService.delete(OpnsetPosCandTDim.class, opnsetPosCandT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-cand-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetPosCandTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-cand-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosCandTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-pos-cand-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetPosCandTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-cand-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosCandTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * OPNSET_POS_CAND_STAT_T - opnset-pos-cand-stat-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param opnsetPosCandStatId - opnset-pos-cand-stat-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/opnset-pos-cand-stat-ts","/wfm-ihub/opnset-pos-cand-stat-ts/{opnset-pos-cand-stat-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOpnsetPosCandStatTByCode(
			  @PathVariable(name="opnset-pos-cand-stat-id", required=false) String opnsetPosCandStatId
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
		if (opnsetPosCandStatId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("opnset-pos-cand-stat-id", opnsetPosCandStatId);
		}
		
		return wfmIhubDaoService.find(OpnsetPosCandStatTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-cand-stat-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOpnsetPosCandStatT(@RequestBody OpnsetPosCandStatTDim fbsOpnsetPosCandStatT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = true;
		return wfmIhubDaoService.insert(fbsOpnsetPosCandStatT, returnGeneratedKeys, null);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-cand-stat-ts")
	public  ResponseEntity<Integer> deleteAllOpnsetPosCandStatTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(OpnsetPosCandStatTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/opnset-pos-cand-stat-ts/{opnset-pos-cand-stat-id}")
	public ResponseEntity<Integer> deleteOpnsetPosCandStatT(@PathVariable(name="opnset-pos-cand-stat-id") @Parameter(description = "OpnsetPosCandStatT Code") String opnsetPosCandStatId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OpnsetPosCandStatTDim opnsetPosCandStatT = new OpnsetPosCandStatTDim(Integer.valueOf(opnsetPosCandStatId));
		return wfmIhubDaoService.delete(OpnsetPosCandStatTDim.class, opnsetPosCandStatT);
	}	
	
	@PostMapping(value="/wfm-ihub/opnset-pos-cand-stat-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOpnsetPosCandStatTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-cand-stat-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosCandStatTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/opnset-pos-cand-stat-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOpnsetPosCandStatTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="opnset-pos-cand-stat-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(OpnsetPosCandStatTDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * KAFKA_MGS_T - kafka-mgs-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param  -  PathVariable. Note that the path variable name must match the table column name.
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
	 * @throws IOException  REFERENCE_TX_ID
	 */
	@GetMapping(path={"/wfm-ihub/kafka-mgs-ts/assignments","/wfm-ihub/kafka-mgs-ts/assignments/{reference-tx-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllKafkaMgsTAssignments(
			  @PathVariable(name="reference-tx-id", required=false) String referenceTxId
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
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tx-type-cd", "GSASSIGNMENT");
		if (referenceTxId!=null) {
			pathVarMap.put("reference-tx-id", referenceTxId);
		}
		
		return wfmIhubDaoService.find(KafkaMgsTDim.class
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
	
	@GetMapping(path={"/wfm-ihub/kafka-mgs-ts/resource-requests","/wfm-ihub/kafka-mgs-ts/resource-requests/{reference-tx-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllKafkaMgsTResourceRequest(
			  @PathVariable(name="reference-tx-id", required=false) String referenceTxId
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
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tx-type-cd", "GSRESOURCEREQUEST");
		if (referenceTxId!=null) {
			pathVarMap.put("reference-tx-id", referenceTxId);
		}
		
		return wfmIhubDaoService.find(KafkaMgsTDim.class
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
	
	/**
	 * KAFKA_AVAIL_DT_T - kafka-avail-dt-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param talentId - talent-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/kafka-avail-dt-ts","/wfm-ihub/kafka-avail-dt-ts/{talent-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllKafkaAvailDtTByCode(
			  @PathVariable(name="talent-id", required=false) String talentId
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
		if (talentId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("talent-id", talentId);
		}
		
		return wfmIhubDaoService.find(KafkaAvailDtTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/kafka-avail-dt-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertKafkaAvailDtT(@RequestBody KafkaAvailDtTDim kafkaAvailDtT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		boolean returnGeneratedKeys = false;
		return wfmIhubDaoService.insert(kafkaAvailDtT, returnGeneratedKeys, null);
	}
	
	/**
	 * GR_PROJ_T - gr-proj-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param grProjId - gr-proj-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/gr-proj-ts","/wfm-ihub/gr-proj-ts/{gr-proj-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGrProjTByCode(
			  @PathVariable(name="gr-proj-id", required=false) String grProjId
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
		if (grProjId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("gr-proj-id", grProjId);
		}
		
		return wfmIhubDaoService.find(GrProjTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/gr-proj-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGrProjT(@RequestBody GrProjTDim grProjT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmIhubDaoService.insert(grProjT);
	}
	
	@DeleteMapping("/wfm-ihub/gr-proj-ts")
	public  ResponseEntity<Integer> deleteAllGrProjTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(GrProjTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/gr-proj-ts/{gr-proj-id}")
	public ResponseEntity<Integer> deleteGrProjT(@PathVariable(name="gr-proj-id") @Parameter(description = "GrProjT Code") String grProjId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrProjTDim grProjT = new GrProjTDim(Long.valueOf(grProjId));
		return wfmIhubDaoService.delete(GrProjTDim.class, grProjT);
	}		
	
	/**
	 * GRDS_CLNT_T - grds-clnt-ts
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param grdsClntCd - grds-clnt-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/grds-clnt-ts","/wfm-ihub/grds-clnt-ts/{grds-clnt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGrdsClntTByCode(
			  @PathVariable(name="grds-clnt-cd", required=false) String grdsClntCd
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
		if (grdsClntCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("grds-clnt-cd", grdsClntCd);
		}
		
		return wfmIhubDaoService.find(GrdsClntTDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/grds-clnt-ts", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGrdsClntT(@RequestBody GrdsClntTDim grdsClntT) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmIhubDaoService.insert(grdsClntT);
	}
	
	@DeleteMapping("/wfm-ihub/grds-clnt-ts")
	public  ResponseEntity<Integer> deleteAllGrdsClntTs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.delete(GrdsClntTDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/grds-clnt-ts/{grds-clnt-cd}")
	public ResponseEntity<Integer> deleteGrdsClntT(@PathVariable(name="grds-clnt-cd") @Parameter(description = "GrdsClntT Code") String grdsClntCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrdsClntTDim grdsClntT = new GrdsClntTDim(Integer.valueOf(grdsClntCd));
		return wfmIhubDaoService.delete(GrdsClntTDim.class, grdsClntT);
	}	
	
	@PostMapping(value="/wfm-ihub/grds-clnt-ts/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGrdsClntTs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="grds-clnt-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrdsClntTDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/grds-clnt-ts/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGrdsClntTs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="grds-clnt-ts.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmIhubDaoService.etl(GrdsClntTDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
}
