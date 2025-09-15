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

import com.ibm.wfm.beans.AccountTypeDim;
import com.ibm.wfm.beans.CicCenterDim;
import com.ibm.wfm.beans.CicCenterGroupDim;
import com.ibm.wfm.beans.CicManagementDim;
import com.ibm.wfm.beans.CicTypeDim;
import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EbuXrefDim;
import com.ibm.wfm.beans.EmployeeBandMapDim;
import com.ibm.wfm.beans.EmployeeTypeDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GeographyTypeDim;
import com.ibm.wfm.beans.LaborModelDim;
import com.ibm.wfm.beans.LaborPoolDim;
import com.ibm.wfm.beans.LaborTypeDim;
import com.ibm.wfm.beans.OnsiteModelDim;
import com.ibm.wfm.beans.SectorIndustryDim;
import com.ibm.wfm.beans.ServicesCicXrefDim;
import com.ibm.wfm.beans.UtilizationCategoryDim;
import com.ibm.wfm.beans.UtilizationLobDim;
import com.ibm.wfm.beans.UtilizationLocationDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.FileHelpers;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoWfmController {
	
	@Autowired
	private EdsDaoService edsWfmDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * Labor Model
	 */
	/* Labor Model */
	@GetMapping(path={"/eds-wfm/labor-models","/eds-wfm/labor-models/{labor-model-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllLaborModels(
			  @PathVariable(name="labor-model-cd", required=false) String laborModelCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (laborModelCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("labor-model-code", laborModelCode);
		}
		
		edsWfmDaoService.setT(LaborModelDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_MODEL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_MODEL_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(LaborModelDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, 0					//offset
				, resultSetMaxSize
				, request);
	}
	
	
	@GetMapping("/eds-wfm/labor-models/csv")
	public ResponseEntity<Resource> retrieveAllLaborModelsCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /eds-wfm/labor-models
		String laborModelUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/eds-wfm/labor-models")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<LaborModelDim[]> responseEntity = new RestTemplate().getForEntity(laborModelUri, LaborModelDim[].class); //uriVariables); 
		List<LaborModelDim> laborModels = Arrays.asList(responseEntity.getBody());

		return edsWfmDaoService.returnCsv(laborModels, "labor-models.csv", request);
	}
	
	@DeleteMapping("/eds-wfm/labor-models")
	public int deleteAllLaborModels() {
		edsWfmDaoService.setT(LaborModelDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_MODEL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_MODEL_SCD_V");
		return edsWfmDaoService.deleteAll();
	}
	
	@DeleteMapping("/eds-wfm/labor-models/{labor-model-cd}")
	public int deleteLaborModel(@PathVariable(name="labor-model-cd") String laborModelCd) {
		edsWfmDaoService.setT(LaborModelDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_MODEL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_MODEL_SCD_V");
		
		LaborModelDim laborModelDim = new LaborModelDim(laborModelCd);
		
		return edsWfmDaoService.delete(Arrays.asList(laborModelDim));
	}
	
	@PostMapping(value="/eds-wfm/labor-models/etl-upload")
	public EtlResponse etl2EdsLaborModelUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="labor-model.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		edsWfmDaoService.setT(LaborModelDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_MODEL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_MODEL_SCD_V");
		
		EtlResponse etlResponse = edsWfmDaoService.etl(fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	@PostMapping(value="/eds-wfm/labor-models/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsLaborModel(@RequestParam("oldFileName") String oldFileName
			                , @RequestParam("newFileName") String newFileName
			                , @RequestParam(name="outputFileName",defaultValue="labor-model.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		edsWfmDaoService.setT(LaborModelDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_MODEL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_MODEL_SCD_V");
		
		String oldFn = fileStorageProperties.getUploadDir()+"/"+oldFileName;
		if (!FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+oldFileName)) oldFn=null;
		
		EtlResponse etlResponse = edsWfmDaoService.etl(oldFn
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/**
	 * EBU_XREF - ebu-xrefs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param ebuCd - ebu-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/ebu-xrefs","/eds-wfm/ebu-xrefs/{ebu-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllEbuXrefByCode(
			  @PathVariable(name="ebu-cd", required=false) String ebuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (ebuCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ebu-cd", ebuCd);
		}
		
		return edsWfmDaoService.find(EbuXrefDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/ebu-xrefs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertEbuXref(@RequestBody EbuXrefDim fbsEbuXref) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsEbuXref);
	}
	
	@DeleteMapping("/eds-wfm/ebu-xrefs")
	public  ResponseEntity<Integer> deleteAllEbuXrefs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(EbuXrefDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/ebu-xrefs/{ebu-cd}")
	public ResponseEntity<Integer> deleteEbuXref(@PathVariable(name="ebu-cd") @Parameter(description = "FBS EbuXref Code") String ebuCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		EbuXrefDim ebuXref = new EbuXrefDim(ebuCd);
		return edsWfmDaoService.delete(EbuXrefDim.class, ebuXref);
	}	
	
	@PostMapping(value="/eds-wfm/ebu-xrefs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlEbuXrefs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="ebu-xrefs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EbuXrefDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/ebu-xrefs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlEbuXrefs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="ebu-xrefs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EbuXrefDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	@PostMapping(value="/eds-wfm/ebu-xrefs/idl-excel")
	public ResponseEntity<Integer> idlExcelEbuXrefs(@RequestParam("excel-file-name") MultipartFile excelFileNm
							, @RequestParam(name="tab-name",defaultValue="Sheet1") String tabNm
			                ) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		edsWfmDaoService.setT(EbuXrefDim.class);
		edsWfmDaoService.setTableNm("REFT.EBU_XREF_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.EBU_XREF_SCD_V");
		
		return edsWfmDaoService.idlExcel(EbuXrefDim.class, excelFileNm, tabNm);
	}
	

	
	/**
	 * LABOR_TYPE - labor-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param laborTypeCd - labor-type-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/labor-types","/eds-wfm/labor-types/{labor-type-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllLaborTypeByCode(
			  @PathVariable(name="labor-type-cd", required=false) String laborTypeCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (laborTypeCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("labor-type-cd", laborTypeCd);
		}
		
		return edsWfmDaoService.find(LaborTypeDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/labor-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborType(@RequestBody LaborTypeDim fbsLaborType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsLaborType);
	}
	
	@DeleteMapping("/eds-wfm/labor-types")
	public  ResponseEntity<Integer> deleteAllLaborTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(LaborTypeDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/labor-types/{labor-type-cd}")
	public ResponseEntity<Integer> deleteLaborType(@PathVariable(name="labor-type-cd") @Parameter(description = "FBS LaborType Code") String laborTypeCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborTypeDim laborType = new LaborTypeDim(laborTypeCd);
		return edsWfmDaoService.delete(LaborTypeDim.class, laborType);
	}	
	
	@PostMapping(value="/eds-wfm/labor-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(LaborTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/labor-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(LaborTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * ACCOUNT_TYPE - account-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param accountTypCode - account-typ-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/account-types","/eds-wfm/account-types/{account-typ-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllAccountTypeByCode(
			  @PathVariable(name="account-typ-code", required=false) String accountTypCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (accountTypCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("account-typ-code", accountTypCode);
		}
		
		return edsWfmDaoService.find(AccountTypeDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/account-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertAccountType(@RequestBody AccountTypeDim fbsAccountType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsAccountType);
	}
	
	@DeleteMapping("/eds-wfm/account-types")
	public  ResponseEntity<Integer> deleteAllAccountTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(AccountTypeDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/account-types/{account-typ-code}")
	public ResponseEntity<Integer> deleteAccountType(@PathVariable(name="account-typ-code") @Parameter(description = "FBS AccountType Code") String accountTypCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		AccountTypeDim accountType = new AccountTypeDim(accountTypCode);
		return edsWfmDaoService.delete(AccountTypeDim.class, accountType);
	}	
	
	@PostMapping(value="/eds-wfm/account-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlAccountTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="account-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(AccountTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/account-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlAccountTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="account-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(AccountTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	

	
	/**
	 * EMPLOYEE_TYPE - employee-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param employeeTypCode - employee-typ-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/employee-types","/eds-wfm/employee-types/{employee-typ-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllEmployeeTypeByCode(
			  @PathVariable(name="employee-typ-code", required=false) String employeeTypCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (employeeTypCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("employee-typ-code", employeeTypCode);
		}
		
		return edsWfmDaoService.find(EmployeeTypeDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/employee-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertEmployeeType(@RequestBody EmployeeTypeDim fbsEmployeeType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsEmployeeType);
	}
	
	@DeleteMapping("/eds-wfm/employee-types")
	public  ResponseEntity<Integer> deleteAllEmployeeTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(EmployeeTypeDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/employee-types/{employee-typ-code}")
	public ResponseEntity<Integer> deleteEmployeeType(@PathVariable(name="employee-typ-code") @Parameter(description = "FBS EmployeeType Code") String employeeTypCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		EmployeeTypeDim employeeType = new EmployeeTypeDim(employeeTypCode);
		return edsWfmDaoService.delete(EmployeeTypeDim.class, employeeType);
	}	
	
	@PostMapping(value="/eds-wfm/employee-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlEmployeeTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="employee-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EmployeeTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/employee-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlEmployeeTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="employee-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EmployeeTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	/**
	 * GEOGRAPHY_TYPE - geography-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param geographyTypCode - geography-typ-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/geography-types","/eds-wfm/geography-types/{geography-type-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGeographyTypeByCode(
			  @PathVariable(name="geography-type-code", required=false) String geographyTypeCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (geographyTypeCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geography-typ-code", geographyTypeCode);
		}
		
		return edsWfmDaoService.find(GeographyTypeDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/geography-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeographyType(@RequestBody GeographyTypeDim fbsGeographyType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsGeographyType);
	}
	
	@DeleteMapping("/eds-wfm/geography-types")
	public  ResponseEntity<Integer> deleteAllGeographyTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(GeographyTypeDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/geography-types/{geography-typ-code}")
	public ResponseEntity<Integer> deleteGeographyType(@PathVariable(name="geography-typ-code") @Parameter(description = "FBS GeographyType Code") String geographyTypCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GeographyTypeDim geographyType = new GeographyTypeDim(geographyTypCode);
		return edsWfmDaoService.delete(GeographyTypeDim.class, geographyType);
	}	
	
	@PostMapping(value="/eds-wfm/geography-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographyTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(GeographyTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/geography-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographyTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(GeographyTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * LABOR_POOL - labor-pools
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param laborPoolCode - labor-pool-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/labor-pools","/eds-wfm/labor-pools/{labor-pool-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllLaborPoolByCode(
			  @PathVariable(name="labor-pool-code", required=false) String laborPoolCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (laborPoolCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("labor-pool-code", laborPoolCode);
		}
		
		edsWfmDaoService.setT(LaborPoolDim.class);
		edsWfmDaoService.setTableNm("REFT.LABOR_POOL_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.LABOR_POOL_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(LaborPoolDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, 0					//offset
				, resultSetMaxSize
				, request);
	}
	
	
	@PostMapping(value="/eds-wfm/labor-pools", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborPool(@RequestBody LaborPoolDim fbsLaborPool) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsLaborPool);
	}
	
	@DeleteMapping("/eds-wfm/labor-pools")
	public  ResponseEntity<Integer> deleteAllLaborPools(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(LaborPoolDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/labor-pools/{labor-pool-code}")
	public ResponseEntity<Integer> deleteLaborPool(@PathVariable(name="labor-pool-code") @Parameter(description = "FBS LaborPool Code") String laborPoolCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborPoolDim laborPool = new LaborPoolDim(laborPoolCode);
		return edsWfmDaoService.delete(LaborPoolDim.class, laborPool);
	}	
	
	@PostMapping(value="/eds-wfm/labor-pools/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborPools(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pools.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(LaborPoolDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/labor-pools/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborPools(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pools.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(LaborPoolDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * ONSITE_MODEL - onsite-models
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param onsiteModelCode - onsite-model-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/onsite-models","/eds-wfm/onsite-models/{onsite-model-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllOnsiteModelByCode(
			  @PathVariable(name="onsite-model-code", required=false) String onsiteModelCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (onsiteModelCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("onsite-model-code", onsiteModelCode);
		}
		
		return edsWfmDaoService.find(OnsiteModelDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/onsite-models", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertOnsiteModel(@RequestBody OnsiteModelDim fbsOnsiteModel) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsOnsiteModel);
	}
	
	@DeleteMapping("/eds-wfm/onsite-models")
	public  ResponseEntity<Integer> deleteAllOnsiteModels(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(OnsiteModelDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/onsite-models/{onsite-model-code}")
	public ResponseEntity<Integer> deleteOnsiteModel(@PathVariable(name="onsite-model-code") @Parameter(description = "FBS OnsiteModel Code") String onsiteModelCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		OnsiteModelDim onsiteModel = new OnsiteModelDim(onsiteModelCode);
		return edsWfmDaoService.delete(OnsiteModelDim.class, onsiteModel);
	}	
	
	@PostMapping(value="/eds-wfm/onsite-models/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOnsiteModels(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="onsite-models.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(OnsiteModelDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/onsite-models/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOnsiteModels(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="onsite-models.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(OnsiteModelDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * UTILIZATION_CATEGORY - utilization-categories
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param utilCatCode - util-cat-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/utilization-categories","/eds-wfm/utilization-categories/{util-cat-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllUtilizationCategoryByCode(
			  @PathVariable(name="util-cat-code", required=false) String utilCatCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (utilCatCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("util-cat-code", utilCatCode);
		}
		
		return edsWfmDaoService.find(UtilizationCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/utilization-categories", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertUtilizationCategory(@RequestBody UtilizationCategoryDim fbsUtilizationCategory) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsUtilizationCategory);
	}
	
	@DeleteMapping("/eds-wfm/utilization-categories")
	public  ResponseEntity<Integer> deleteAllUtilizationCategorys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(UtilizationCategoryDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/utilization-categories/{util-cat-code}")
	public ResponseEntity<Integer> deleteUtilizationCategory(@PathVariable(name="util-cat-code") @Parameter(description = "FBS UtilizationCategory Code") String utilCatCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		UtilizationCategoryDim utilizationCategory = new UtilizationCategoryDim(utilCatCode);
		return edsWfmDaoService.delete(UtilizationCategoryDim.class, utilizationCategory);
	}	
	
	@PostMapping(value="/eds-wfm/utilization-categories/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlUtilizationCategorys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationCategoryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/utilization-categories/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlUtilizationCategorys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationCategoryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * UTILIZATION_LOB - utilization-lobs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param utilLobCode - util-lob-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/utilization-lobs","/eds-wfm/utilization-lobs/{util-lob-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllUtilizationLobByCode(
			  @PathVariable(name="util-lob-code", required=false) String utilLobCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (utilLobCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("util-lob-code", utilLobCode);
		}
		
		return edsWfmDaoService.find(UtilizationLobDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/utilization-lobs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertUtilizationLob(@RequestBody UtilizationLobDim fbsUtilizationLob) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsUtilizationLob);
	}
	
	@DeleteMapping("/eds-wfm/utilization-lobs")
	public  ResponseEntity<Integer> deleteAllUtilizationLobs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(UtilizationLobDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/utilization-lobs/{util-lob-code}")
	public ResponseEntity<Integer> deleteUtilizationLob(@PathVariable(name="util-lob-code") @Parameter(description = "FBS UtilizationLob Code") String utilLobCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		UtilizationLobDim utilizationLob = new UtilizationLobDim(utilLobCode);
		return edsWfmDaoService.delete(UtilizationLobDim.class, utilizationLob);
	}	
	
	@PostMapping(value="/eds-wfm/utilization-lobs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlUtilizationLobs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-lobs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationLobDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/utilization-lobs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlUtilizationLobs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-lobs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationLobDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * UTILIZATION_LOCATION - utilization-locations
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param utilLocationCode - util-location-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/utilization-locations","/eds-wfm/utilization-locations/{util-location-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllUtilizationLocationByCode(
			  @PathVariable(name="util-location-code", required=false) String utilLocationCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (utilLocationCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("util-location-code", utilLocationCode);
		}
		
		return edsWfmDaoService.find(UtilizationLocationDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/utilization-locations", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertUtilizationLocation(@RequestBody UtilizationLocationDim fbsUtilizationLocation) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsUtilizationLocation);
	}
	
	@DeleteMapping("/eds-wfm/utilization-locations")
	public  ResponseEntity<Integer> deleteAllUtilizationLocations(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(UtilizationLocationDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/utilization-locations/{util-location-code}")
	public ResponseEntity<Integer> deleteUtilizationLocation(@PathVariable(name="util-location-code") @Parameter(description = "FBS UtilizationLocation Code") String utilLocationCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		UtilizationLocationDim utilizationLocation = new UtilizationLocationDim(utilLocationCode);
		return edsWfmDaoService.delete(UtilizationLocationDim.class, utilizationLocation);
	}	
	
	@PostMapping(value="/eds-wfm/utilization-locations/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlUtilizationLocations(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-locations.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationLocationDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/utilization-locations/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlUtilizationLocations(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="utilization-locations.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(UtilizationLocationDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * CIC_CENTER - cic-centers
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param cicCenterCd - cic-center-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/cic-centers","/eds-wfm/cic-centers/{cic-center-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCicCenterByCode(
			  @PathVariable(name="cic-center-cd", required=false) String cicCenterCd
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
		if (cicCenterCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("cic-center-cd", cicCenterCd);
		}
		
		edsWfmDaoService.setT(CicCenterDim.class);
		edsWfmDaoService.setTableNm("REFT.CIC_CENTER_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.CIC_CENTER_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(CicCenterDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, offset			//offset
				, resultSetMaxSize
				, request);
	}
	
	@GetMapping(path={"/eds-wfm/cic-centers/count"},produces = { "application/json", "application/xml"})
	public synchronized ResponseEntity<Count> retrieveCountCicCenterByCode(
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
		return edsWfmDaoService.countAll(CicCenterDim.class, pathVarMap, filters, includePit, null, false);
	}
	
	
	@PostMapping(value="/eds-wfm/cic-centers", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCicCenter(@RequestBody CicCenterDim fbsCicCenter) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsCicCenter);
	}
	
	@DeleteMapping("/eds-wfm/cic-centers")
	public  ResponseEntity<Integer> deleteAllCicCenters(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(CicCenterDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/cic-centers/{cic-center-cd}")
	public ResponseEntity<Integer> deleteCicCenter(@PathVariable(name="cic-center-cd") @Parameter(description = "FBS CicCenter Code") String cicCenterCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CicCenterDim cicCenter = new CicCenterDim(cicCenterCd);
		return edsWfmDaoService.delete(CicCenterDim.class, cicCenter);
	}	
	
	@PostMapping(value="/eds-wfm/cic-centers/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCicCenters(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-centers.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicCenterDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/cic-centers/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCicCenters(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-centers.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicCenterDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	
	/**
	 * CIC_CENTER_GROUP - cic-center-groups
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param cicCenterGroupCode - cic-center-group-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/cic-center-groups","/eds-wfm/cic-center-groups/{cic-center-group-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCicCenterGroupByCode(
			  @PathVariable(name="cic-center-group-code", required=false) String cicCenterGroupCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (cicCenterGroupCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("cic-center-group-code", cicCenterGroupCode);
		}
		
		edsWfmDaoService.setT(CicCenterGroupDim.class);
		edsWfmDaoService.setTableNm("REFT.CIC_CENTER_GROUP_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.CIC_CENTER_GROUP_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(CicCenterGroupDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, 0					//offset
				, resultSetMaxSize
				, request);
	}
	
	
	@PostMapping(value="/eds-wfm/cic-center-groups", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCicCenterGroup(@RequestBody CicCenterGroupDim fbsCicCenterGroup) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsCicCenterGroup);
	}
	
	@DeleteMapping("/eds-wfm/cic-center-groups")
	public  ResponseEntity<Integer> deleteAllCicCenterGroups(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(CicCenterGroupDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/cic-center-groups/{cic-center-group-code}")
	public ResponseEntity<Integer> deleteCicCenterGroup(@PathVariable(name="cic-center-group-code") @Parameter(description = "FBS CicCenterGroup Code") String cicCenterGroupCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CicCenterGroupDim cicCenterGroup = new CicCenterGroupDim(cicCenterGroupCode);
		return edsWfmDaoService.delete(CicCenterGroupDim.class, cicCenterGroup);
	}	
	
	@PostMapping(value="/eds-wfm/cic-center-groups/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCicCenterGroups(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-center-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicCenterGroupDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/cic-center-groups/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCicCenterGroups(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-center-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicCenterGroupDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * SECTOR_INDUSTRY - sector-industries
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param sectorIndustryCode - sector-industry-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/sector-industries","/eds-wfm/sector-industries/{sector-industry-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSectorIndustryByCode(
			  @PathVariable(name="sector-industry-code", required=false) String sectorIndustryCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (sectorIndustryCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("sector-industry-code", sectorIndustryCode);
		}
		
		edsWfmDaoService.setT(SectorIndustryDim.class);
		edsWfmDaoService.setTableNm("REFT.SECTOR_INDUSTRY_DIM_V");
		edsWfmDaoService.setScdTableNm("REFT.SECTOR_INDUSTRY_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(SectorIndustryDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, 0					//offset
				, resultSetMaxSize
				, request);
	}
	
	
	@PostMapping(value="/eds-wfm/sector-industries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSectorIndustry(@RequestBody SectorIndustryDim fbsSectorIndustry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsSectorIndustry);
	}
	
	@DeleteMapping("/eds-wfm/sector-industries")
	public  ResponseEntity<Integer> deleteAllSectorIndustrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(SectorIndustryDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/sector-industries/{sector-industry-code}")
	public ResponseEntity<Integer> deleteSectorIndustry(@PathVariable(name="sector-industry-code") @Parameter(description = "FBS SectorIndustry Code") String sectorIndustryCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SectorIndustryDim sectorIndustry = new SectorIndustryDim(sectorIndustryCode);
		return edsWfmDaoService.delete(SectorIndustryDim.class, sectorIndustry);
	}	
	
	@PostMapping(value="/eds-wfm/sector-industries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSectorIndustrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sector-industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(SectorIndustryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/sector-industries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSectorIndustrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sector-industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(SectorIndustryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * EMPLOYEE_BAND_MAP - employee-band-maps
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param empBandMapCode - emp-band-map-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/employee-band-maps","/eds-wfm/employee-band-maps/{emp-band-map-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllEmployeeBandMapByCode(
			  @PathVariable(name="emp-band-map-code", required=false) String empBandMapCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (empBandMapCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("emp-band-map-code", empBandMapCode);
		}
		
		//return edsWfmDaoService.find(EmployeeBandMapDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
		//edsWfmDaoService.setT(EmployeeBandMapDim.class);
		//edsWfmDaoService.setTableNm("REFT.SECTOR_INDUSTRY_DIM_V");
		//edsWfmDaoService.setScdTableNm("REFT.SECTOR_INDUSTRY_SCD_V");
		
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		
		return edsWfmDaoService.find(EmployeeBandMapDim.class
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, true 				//self-referencing
				, null 				//topNodeName
				, returnCsv
				, includePit
				, pit
				, false 			//edsOnly
				, 0					//offset
				, resultSetMaxSize
				, request);
	}
	
	
	@PostMapping(value="/eds-wfm/employee-band-maps", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertEmployeeBandMap(@RequestBody EmployeeBandMapDim fbsEmployeeBandMap) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsEmployeeBandMap);
	}
	
	@DeleteMapping("/eds-wfm/employee-band-maps")
	public  ResponseEntity<Integer> deleteAllEmployeeBandMaps(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(EmployeeBandMapDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/employee-band-maps/{emp-band-map-code}")
	public ResponseEntity<Integer> deleteEmployeeBandMap(@PathVariable(name="emp-band-map-code") @Parameter(description = "FBS EmployeeBandMap Code") String empBandMapCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		EmployeeBandMapDim employeeBandMap = new EmployeeBandMapDim(empBandMapCode);
		return edsWfmDaoService.delete(EmployeeBandMapDim.class, employeeBandMap);
	}	
	
	@PostMapping(value="/eds-wfm/employee-band-maps/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlEmployeeBandMaps(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="employee-band-maps.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EmployeeBandMapDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/employee-band-maps/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlEmployeeBandMaps(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="employee-band-maps.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(EmployeeBandMapDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	
	/**
	 * SERVICES_CIC_XREF - services-cic-xrefs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param code - code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/services-cic-xrefs","/eds-wfm/services-cic-xrefs/{code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllServicesCicXrefByCode(
			  @PathVariable(name="code", required=false) String code
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (code!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("code", code);
		}
		
		return edsWfmDaoService.find(ServicesCicXrefDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/services-cic-xrefs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertServicesCicXref(@RequestBody ServicesCicXrefDim fbsServicesCicXref) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsServicesCicXref);
	}
	
	@DeleteMapping("/eds-wfm/services-cic-xrefs")
	public  ResponseEntity<Integer> deleteAllServicesCicXrefs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(ServicesCicXrefDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/services-cic-xrefs/{code}")
	public ResponseEntity<Integer> deleteServicesCicXref(@PathVariable(name="code") @Parameter(description = "FBS ServicesCicXref Code") String code) throws IllegalArgumentException, IllegalAccessException, SQLException {
		ServicesCicXrefDim servicesCicXref = new ServicesCicXrefDim(code);
		return edsWfmDaoService.delete(ServicesCicXrefDim.class, servicesCicXref);
	}	
	
	@PostMapping(value="/eds-wfm/services-cic-xrefs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlServicesCicXrefs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="services-cic-xrefs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(ServicesCicXrefDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/services-cic-xrefs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlServicesCicXrefs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="services-cic-xrefs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(ServicesCicXrefDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * CIC_MANAGEMENT - cic-managements
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param cicManagementCode - cic-management-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/cic-managements","/eds-wfm/cic-managements/{cic-management-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCicManagementByCode(
			  @PathVariable(name="cic-management-code", required=false) String cicManagementCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (cicManagementCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("cic-management-code", cicManagementCode);
		}
		
		return edsWfmDaoService.find(CicManagementDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/cic-managements", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCicManagement(@RequestBody CicManagementDim fbsCicManagement) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsCicManagement);
	}
	
	@DeleteMapping("/eds-wfm/cic-managements")
	public  ResponseEntity<Integer> deleteAllCicManagements(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(CicManagementDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/cic-managements/{cic-management-code}")
	public ResponseEntity<Integer> deleteCicManagement(@PathVariable(name="cic-management-code") @Parameter(description = "FBS CicManagement Code") String cicManagementCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CicManagementDim cicManagement = new CicManagementDim(cicManagementCode);
		return edsWfmDaoService.delete(CicManagementDim.class, cicManagement);
	}	
	
	@PostMapping(value="/eds-wfm/cic-managements/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCicManagements(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-managements.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicManagementDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/cic-managements/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCicManagements(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-managements.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicManagementDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * CIC_TYPE - cic-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param cicTypeCode - cic-type-code PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/cic-types","/eds-wfm/cic-types/{cic-type-code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCicTypeByCode(
			  @PathVariable(name="cic-type-code", required=false) String cicTypeCode
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (cicTypeCode!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("cic-type-code", cicTypeCode);
		}
		
		return edsWfmDaoService.find(CicTypeDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/cic-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCicType(@RequestBody CicTypeDim fbsCicType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsWfmDaoService.insert(fbsCicType);
	}
	
	@DeleteMapping("/eds-wfm/cic-types")
	public  ResponseEntity<Integer> deleteAllCicTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.delete(CicTypeDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/cic-types/{cic-type-code}")
	public ResponseEntity<Integer> deleteCicType(@PathVariable(name="cic-type-code") @Parameter(description = "FBS CicType Code") String cicTypeCode) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CicTypeDim cicType = new CicTypeDim(cicTypeCode);
		return edsWfmDaoService.delete(CicTypeDim.class, cicType);
	}	
	
	@PostMapping(value="/eds-wfm/cic-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCicTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/cic-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCicTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="cic-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsWfmDaoService.etl(CicTypeDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
}