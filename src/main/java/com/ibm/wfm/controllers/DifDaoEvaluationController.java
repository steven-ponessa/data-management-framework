package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

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
import com.ibm.wfm.beans.JrsDim;
import com.ibm.wfm.beans.JrsEvaluationErrorDim;
import com.ibm.wfm.beans.SpecialtyDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/v1")
public class DifDaoEvaluationController extends AbstractDaoController {
	
	@Autowired
	private EdsDaoService difEvaluationDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * JRS_EVALUATION_ERROR - jrs-evaluation-errors
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param inetEmailId - inet-email-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/dif-evaluation/jrs-evaluation-errors","/dif-evaluation/jrs-evaluation-errors/{inet-email-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllJrsEvaluationErrorByCode(
			  @PathVariable(name="inet-email-id", required=false) String inetEmailId
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (inetEmailId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("inet-email-id", inetEmailId);
		}
		
		return difEvaluationDaoService.find(JrsEvaluationErrorDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path="/dif-evaluation/jrs-evaluation-errors/mgr-inet-ids",produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveMgrInetIds(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		difEvaluationDaoService.setTableNm("DIF.JRS_EVALUATION_ERROR_DIM_V");
		difEvaluationDaoService.setScdTableNm("DIF.JRS_EVALUATION_ERROR_SCD_V");

		String sql = "SELECT DISTINCT MGR_INET_EMAIL_ID FROM DIF.JRS_EVALUATION_ERROR_DIM_V ";
		if (filters.trim().length()>0) sql+=(filters+" ");
		sql+="ORDER BY MGR_INET_EMAIL_ID";
		//return difEvaluationDaoService.getListForSql(JrsEvaluationErrorDim.class, difEvaluationDaoService.getConnection(), sql);
		return difEvaluationDaoService.getListForSql(JrsEvaluationErrorDim.class, difEvaluationDaoService.getConnection(), sql, returnCsv, request);
	}
	
	@GetMapping(path="/dif-evaluation/jrs-evaluation-errors/mgr-inet-ids/{mgr-inet-id}/professionals",produces = { "application/json", "application/xml"})
	public <T>ResponseEntity<Object>  retrieveProfessionalForMgrInetId(
			  @PathVariable(name="mgr-inet-id") String mgrInetId
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		difEvaluationDaoService.setT(JrsEvaluationErrorDim.class);
		difEvaluationDaoService.setTableNm("DIF.JRS_EVALUATION_ERROR_DIM_V");
		difEvaluationDaoService.setScdTableNm("DIF.JRS_EVALUATION_ERROR_SCD_V");

		String sql = "SELECT * FROM DIF.JRS_EVALUATION_ERROR_DIM_V ";
		if (filters.trim().length()>0) sql+=(filters+" AND ");
		else sql+= "WHERE ";
		sql+=("MGR_INET_EMAIL_ID='"+mgrInetId.trim()+"' ");
		sql+="ORDER BY INET_EMAIL_ID";
		return difEvaluationDaoService.getListForSql(JrsEvaluationErrorDim.class, difEvaluationDaoService.getConnection(), sql, returnCsv, request);
	}
	
	
	@PostMapping(value="/dif-evaluation/jrs-evaluation-errors", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertJrsEvaluationError(@RequestBody JrsEvaluationErrorDim fbsJrsEvaluationError) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return difEvaluationDaoService.insert(fbsJrsEvaluationError);
	}
	
	@DeleteMapping("/dif-evaluation/jrs-evaluation-errors")
	public  ResponseEntity<Integer> deleteAllJrsEvaluationErrors(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return difEvaluationDaoService.delete(JrsEvaluationErrorDim.class, filters);
	}
	
	@DeleteMapping("/dif-evaluation/jrs-evaluation-errors/{inet-email-id}")
	public ResponseEntity<Integer> deleteJrsEvaluationError(@PathVariable(name="inet-email-id") @Parameter(description = "FBS JrsEvaluationError Code") String inetEmailId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsEvaluationErrorDim jrsEvaluationError = new JrsEvaluationErrorDim(inetEmailId);
		return difEvaluationDaoService.delete(JrsEvaluationErrorDim.class, jrsEvaluationError);
	}	
	
	@PostMapping(value="/dif-evaluation/jrs-evaluation-errors/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrsEvaluationErrors(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-evaluation-errors.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return difEvaluationDaoService.etl(JrsEvaluationErrorDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/dif-evaluation/jrs-evaluation-errors/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrsEvaluationErrors(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-evaluation-errors.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return difEvaluationDaoService.etl(JrsEvaluationErrorDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

}