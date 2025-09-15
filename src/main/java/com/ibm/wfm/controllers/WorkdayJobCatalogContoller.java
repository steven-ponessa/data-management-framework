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
import com.ibm.wfm.beans.PrimaryJobCategoryDim;
import com.ibm.wfm.beans.SecondaryJobCategoryDim;
import com.ibm.wfm.beans.WdJobProfileDim;
import com.ibm.wfm.beans.WdJobProfileJrsAssocDim;
import com.ibm.wfm.beans.WorkdayJobCatalogDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.WorkdayJobCatalogDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")

public class WorkdayJobCatalogContoller {
	//WorkdayJobCatalogDaoService
	@Autowired
	private WorkdayJobCatalogDaoService workdayDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * WORKDAY_JOB_CATALOG - workday-job-catalogs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param priJobCatCd - pri-job-cat-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/workday/job-catalogs"
			         ,"/workday/job-catalogs/{pri-job-cat-cd}/{sec-job-cat-cd}/{comp-grd-cd}/{jrs-cd}"
			         ,"/workday/job-catalogs/pjc-cd/{pri-job-cat-cd}"
			         ,"/workday/job-catalogs/pjc-cd/{pri-job-cat-cd}/sjc-cd/{sec-job-cat-cd}"
			         ,"/workday/job-catalogs/pjc-cd/{pri-job-cat-cd}/sjc-cd/{sec-job-cat-cd}/comp-grd-cd/{comp-grd-cd}"
			         ,"/workday/job-catalogs/pjc-cd/{pri-job-cat-cd}/sjc-cd/{sec-job-cat-cd}/comp-grd-cd/{comp-grd-cd}/jrs-cd/{jrs-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllWorkdayJobCatalogByCode(
			  @PathVariable(name="pri-job-cat-cd", required=false) String priJobCatCd
			, @PathVariable(name="sec-job-cat-cd", required=false) String secJobCatCd
			, @PathVariable(name="comp-grd-cd", required=false) String compGrdCd
			, @PathVariable(name="jrs-cd", required=false) String jrsCd
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
		if (priJobCatCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("pri-job-cat-cd", priJobCatCd);
		}
		if (secJobCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("sec-job-cat-cd", secJobCatCd);
		}
		if (compGrdCd!=null) {
			if (pathVarMap==null)pathVarMap = new HashMap<>();
			pathVarMap.put("comp-grd-cd", compGrdCd);
		}
		if (jrsCd!=null) {
			if (pathVarMap==null)pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}
		
		return workdayDaoService.find(WorkdayJobCatalogDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/workday/job-catalogs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertWorkdayJobCatalog(@RequestBody WorkdayJobCatalogDim fbsWorkdayJobCatalog) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return workdayDaoService.insert(fbsWorkdayJobCatalog);
	}
	
	@DeleteMapping("/workday/job-catalogs")
	public  ResponseEntity<Integer> deleteAllWorkdayJobCatalogs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.delete(WorkdayJobCatalogDim.class, filters);
	}
	
	@DeleteMapping("/workday/job-catalogs/pjc-cd/{pri-job-cat-cd}/sjc-cd/{sec-job-cat-cd}/comp-grd-cd/{comp-grd-cd}/jrs-cd/{jrs-cd}")
	public ResponseEntity<Integer> deleteWorkdayJobCatalog(@PathVariable(name="pri-job-cat-cd") @Parameter(description = "WD Primay Job Catalog Code") String priJobCatCd
			                                             , @PathVariable(name="sec-job-cat-cd") @Parameter(description = "WD Secondary Job Catalog Code") String secJobCatCd
			                                             , @PathVariable(name="comp-grd-cd") @Parameter(description = "Comp Grade Code") String compGrdCd
			                                             , @PathVariable(name="jrs-cd") @Parameter(description = "Comp Grade Code") String jrsCd
			                                             ) throws IllegalArgumentException, IllegalAccessException, SQLException {
		WorkdayJobCatalogDim workdayJobCatalog = new WorkdayJobCatalogDim(priJobCatCd, secJobCatCd, compGrdCd, jrsCd);
		return workdayDaoService.delete(WorkdayJobCatalogDim.class, workdayJobCatalog);
	}	
	
	@PostMapping(value="/workday/job-catalogs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlWorkdayJobCatalogs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="workday-job-catalogs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WorkdayJobCatalogDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/workday/job-catalogs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlWorkdayJobCatalogs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="workday-job-catalogs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WorkdayJobCatalogDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	
	/**
	 * PRIMARY_JOB_CATEGORY - primary-job-categories
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param priJobCatCd - pri-job-cat-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/workday/primary-job-categories","/workday/primary-job-categories/{pri-job-cat-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllPrimaryJobCategoryByCode(
			  @PathVariable(name="pri-job-cat-cd", required=false) String priJobCatCd
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
		if (priJobCatCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("pri-job-cat-cd", priJobCatCd);
		}
		
		return workdayDaoService.find(PrimaryJobCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/workday/primary-job-categories", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertPrimaryJobCategory(@RequestBody PrimaryJobCategoryDim fbsPrimaryJobCategory) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return workdayDaoService.insert(fbsPrimaryJobCategory);
	}
	
	@DeleteMapping("/workday/primary-job-categories")
	public  ResponseEntity<Integer> deleteAllPrimaryJobCategorys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.delete(PrimaryJobCategoryDim.class, filters);
	}
	
	@DeleteMapping("/workday/primary-job-categories/{pri-job-cat-cd}")
	public ResponseEntity<Integer> deletePrimaryJobCategory(@PathVariable(name="pri-job-cat-cd") @Parameter(description = "FBS PrimaryJobCategory Code") String priJobCatCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		PrimaryJobCategoryDim primaryJobCategory = new PrimaryJobCategoryDim(priJobCatCd);
		return workdayDaoService.delete(PrimaryJobCategoryDim.class, primaryJobCategory);
	}	
	
	@PostMapping(value="/workday/primary-job-categories/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlPrimaryJobCategorys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="primary-job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(PrimaryJobCategoryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/workday/primary-job-categories/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlPrimaryJobCategorys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="primary-job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(PrimaryJobCategoryDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * SECONDARY_JOB_CATEGORY - secondary-job-categories
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param secJobCatCd - sec-job-cat-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/workday/primary-job-categories/{pri-job-cat-cd}/secondary-job-categories","/workday/primary-job-categories/{pri-job-cat-cd}/secondary-job-categories/{sec-job-cat-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSecondaryJobCategoryByCode(
			  @PathVariable(name="pri-job-cat-cd", required=true) String priJobCatCd
			, @PathVariable(name="sec-job-cat-cd", required=false) String secJobCatCd
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
		pathVarMap = new HashMap<>();
		pathVarMap.put("pri-job-cat-cd", priJobCatCd);
		if (secJobCatCd!=null) {
			pathVarMap.put("sec-job-cat-cd", secJobCatCd);
		}
		
		return workdayDaoService.find(SecondaryJobCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/workday/secondary-job-categories","/workday/secondary-job-categories/{sec-job-cat-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSecondaryJobCategoryByCodeX(
			  @PathVariable(name="sec-job-cat-cd", required=false) String secJobCatCd
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
		if (secJobCatCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("sec-job-cat-cd", secJobCatCd);
		}
		
		return workdayDaoService.find(SecondaryJobCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/workday/secondary-job-categories", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSecondaryJobCategory(@RequestBody SecondaryJobCategoryDim fbsSecondaryJobCategory) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return workdayDaoService.insert(fbsSecondaryJobCategory);
	}
	
	@DeleteMapping("/workday/secondary-job-categories")
	public  ResponseEntity<Integer> deleteAllSecondaryJobCategorys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.delete(SecondaryJobCategoryDim.class, filters);
	}
	
	@DeleteMapping("/workday/secondary-job-categories/{sec-job-cat-cd}")
	public ResponseEntity<Integer> deleteSecondaryJobCategory(@PathVariable(name="sec-job-cat-cd") @Parameter(description = "FBS SecondaryJobCategory Code") String secJobCatCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SecondaryJobCategoryDim secondaryJobCategory = new SecondaryJobCategoryDim(secJobCatCd);
		return workdayDaoService.delete(SecondaryJobCategoryDim.class, secondaryJobCategory);
	}	
	
	@PostMapping(value="/workday/secondary-job-categories/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSecondaryJobCategorys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="secondary-job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(SecondaryJobCategoryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/workday/secondary-job-categories/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSecondaryJobCategorys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="secondary-job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(SecondaryJobCategoryDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * WD_JOB_PROFILE - wd-job-profiles
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param wdJobProCd - wd-job-pro-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/workday/wd-job-profiles","/workday/wd-job-profiles/{wd-job-pro-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllWdJobProfileByCode(
			  @PathVariable(name="wd-job-pro-cd", required=false) String wdJobProCd
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
		if (wdJobProCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("wd-job-pro-cd", wdJobProCd);
		}
		
		return workdayDaoService.find(WdJobProfileDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/workday/primary-job-categories/{pri-job-cat-cd}/secondary-job-categories/{sec-job-cat-cd}/wd-job-pro-cd","/workday/primary-job-categories/{pri-job-cat-cd}/secondary-job-categories/{sec-job-cat-cd}/wd-job-pro-cd/{wd-job-pro-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSecondaryJobCategoryByCodeByParent(
			  @PathVariable(name="pri-job-cat-cd", required=true) String priJobCatCd
			, @PathVariable(name="sec-job-cat-cd", required=true) String secJobCatCd
			, @PathVariable(name="wd-job-pro-cd", required=false) String wdJobProCd
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
		
		if (priJobCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("pri-job-cat-cd", priJobCatCd);
		}
		if (secJobCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("sec-job-cat-cd", secJobCatCd);
		}
		if (wdJobProCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("wd-job-pro-cd", wdJobProCd);
		}
		
		return workdayDaoService.find(WdJobProfileDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/workday/wd-job-profiles", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertWdJobProfile(@RequestBody WdJobProfileDim fbsWdJobProfile) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return workdayDaoService.insert(fbsWdJobProfile);
	}
	
	@DeleteMapping("/workday/wd-job-profiles")
	public  ResponseEntity<Integer> deleteAllWdJobProfiles(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.delete(WdJobProfileDim.class, filters);
	}
	
	@DeleteMapping("/workday/wd-job-profiles/{wd-job-pro-cd}")
	public ResponseEntity<Integer> deleteWdJobProfile(@PathVariable(name="wd-job-pro-cd") @Parameter(description = "FBS WdJobProfile Code") String wdJobProCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		WdJobProfileDim wdJobProfile = new WdJobProfileDim(wdJobProCd);
		return workdayDaoService.delete(WdJobProfileDim.class, wdJobProfile);
	}	
	
	@PostMapping(value="/workday/wd-job-profiles/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlWdJobProfiles(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="wd-job-profiles.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WdJobProfileDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/workday/wd-job-profiles/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlWdJobProfiles(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="wd-job-profiles.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WdJobProfileDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * WD_JOB_PROFILE_JRS_ASSOC - wd-job-profile-jrs-assocs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param wdJobProCd - wd-job-pro-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/workday/wd-job-profile-jrs-assocs","/workday/wd-job-profile-jrs-assocs/{wd-job-pro-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllWdJobProfileJrsAssocByCode(
			  @PathVariable(name="wd-job-pro-cd", required=false) String wdJobProCd
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
		if (wdJobProCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("wd-job-pro-cd", wdJobProCd);
		}
		
		return workdayDaoService.find(WdJobProfileJrsAssocDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/workday/wd-job-profile-jrs-assocs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertWdJobProfileJrsAssoc(@RequestBody WdJobProfileJrsAssocDim fbsWdJobProfileJrsAssoc) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return workdayDaoService.insert(fbsWdJobProfileJrsAssoc);
	}
	
	@DeleteMapping("/workday/wd-job-profile-jrs-assocs")
	public  ResponseEntity<Integer> deleteAllWdJobProfileJrsAssocs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.delete(WdJobProfileJrsAssocDim.class, filters);
	}
	
	@DeleteMapping("/workday/wd-job-profile-jrs-assocs/{wd-job-pro-cd}")
	public ResponseEntity<Integer> deleteWdJobProfileJrsAssoc(@PathVariable(name="wd-job-pro-cd", required=true) @Parameter(description = "FBS WdJobProfileJrsAssoc Code") String wdJobProCd
			, @PathVariable(name="comp-grd-cd", required=true) @Parameter(description = "Compendation Grade Code") String compGrdCd
			, @PathVariable(name="jrs-cd", required=true) @Parameter(description = "Job Role Specialty Code") String jrsCd) 
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		WdJobProfileJrsAssocDim wdJobProfileJrsAssoc = new WdJobProfileJrsAssocDim(wdJobProCd, compGrdCd, jrsCd);
		return workdayDaoService.delete(WdJobProfileJrsAssocDim.class, wdJobProfileJrsAssoc);
	}	
	
	@PostMapping(value="/workday/wd-job-profile-jrs-assocs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlWdJobProfileJrsAssocs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="wd-job-profile-jrs-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WdJobProfileJrsAssocDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/workday/wd-job-profile-jrs-assocs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlWdJobProfileJrsAssocs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="wd-job-profile-jrs-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return workdayDaoService.etl(WdJobProfileJrsAssocDim.class, oldFile, newFile, keyLength, outputFileName);
	}		

}
