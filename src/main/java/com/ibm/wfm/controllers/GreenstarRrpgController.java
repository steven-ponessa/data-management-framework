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
import com.ibm.wfm.beans.rrpg.CountryRrpgDim;
import com.ibm.wfm.beans.rrpg.GeographyRrpgDim;
import com.ibm.wfm.beans.rrpg.GeographyTypeRrpgDim;
import com.ibm.wfm.beans.rrpg.JobCategoryDim;
import com.ibm.wfm.beans.rrpg.JobRoleRrpgDim;
import com.ibm.wfm.beans.rrpg.LaborPoolRrpgDim;
import com.ibm.wfm.beans.rrpg.MarketRrpgDim;
import com.ibm.wfm.beans.rrpg.RegionRrpgDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class GreenstarRrpgController {
	@Autowired
	private EdsDaoService greenstarRrpgDaoService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileStorageProperties fileStorageProperties;


	/**
	 * JOB_CATEGORY - job-categories
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param jobCatCd - job-cat-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/job-categories","/greenstar-rrpg/job-categories/{job-cat-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllJobCategoryByCode(
			  @PathVariable(name="job-cat-cd", required=false) String jobCatCd
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
		if (jobCatCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("job-cat-cd", jobCatCd);
		}
		
		return greenstarRrpgDaoService.find(JobCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/job-categories", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertJobCategory(@RequestBody JobCategoryDim fbsJobCategory) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsJobCategory);
	}
	
	@DeleteMapping("/greenstar-rrpg/job-categories")
	public  ResponseEntity<Integer> deleteAllJobCategorys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(JobCategoryDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/job-categories/{job-cat-cd}")
	public ResponseEntity<Integer> deleteJobCategory(@PathVariable(name="job-cat-cd") @Parameter(description = "FBS JobCategory Code") String jobCatCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		JobCategoryDim jobCategory = new JobCategoryDim(jobCatCd);
		return greenstarRrpgDaoService.delete(JobCategoryDim.class, jobCategory);
	}	
	
	@PostMapping(value="/greenstar-rrpg/job-categories/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJobCategorys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(JobCategoryDim.class, null, newFile, keyLength, outputFileName);
	}
	
	@PostMapping(value="/greenstar-rrpg/job-categories/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJobCategorys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="job-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(JobCategoryDim.class, oldFile, newFile, keyLength, outputFileName);
	}


	
	/**
	 * JOB_ROLE - job-roles
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param jobRoleCd - job-role-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/job-roles","/greenstar-rrpg/job-roles/{job-role-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllJobRoleByCode(
			  @PathVariable(name="job-role-cd", required=false) String jobRoleCd
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
		if (jobRoleCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("job-role-cd", jobRoleCd);
		}
		
		return greenstarRrpgDaoService.find(JobRoleRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage,  "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/job-roles", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertJobRole(@RequestBody JobRoleRrpgDim fbsJobRole) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsJobRole);
	}
	
	@DeleteMapping("/greenstar-rrpg/job-roles")
	public  ResponseEntity<Integer> deleteAllJobRoles(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(JobRoleRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/job-roles/{job-role-cd}")
	public ResponseEntity<Integer> deleteJobRole(@PathVariable(name="job-role-cd") @Parameter(description = "FBS JobRole Code") String jobRoleCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		JobRoleRrpgDim jobRole = new JobRoleRrpgDim(jobRoleCd);
		return greenstarRrpgDaoService.delete(JobRoleRrpgDim.class, jobRole);
	}	
	
	@PostMapping(value="/greenstar-rrpg/job-roles/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJobRoles(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="job-roles.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(JobRoleRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/job-roles/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJobRoles(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="job-roles.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(JobRoleRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	

	
	/**
	 * GEOGRAPHY_TYPE - geography-types
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param geoTypCd - geo-typ-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/geography-types","/greenstar-rrpg/geography-types/{geo-typ-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGeographyTypeByCode(
			  @PathVariable(name="geo-typ-cd", required=false) String geoTypCd
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
		if (geoTypCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}
		
		return greenstarRrpgDaoService.find(GeographyTypeRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/geography-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeographyType(@RequestBody GeographyTypeRrpgDim fbsGeographyType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsGeographyType);
	}
	
	@DeleteMapping("/greenstar-rrpg/geography-types")
	public  ResponseEntity<Integer> deleteAllGeographyTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(GeographyTypeRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/geography-types/{geo-typ-cd}")
	public ResponseEntity<Integer> deleteGeographyType(@PathVariable(name="geo-typ-cd") @Parameter(description = "FBS GeographyType Code") String geoTypCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GeographyTypeRrpgDim geographyType = new GeographyTypeRrpgDim(geoTypCd);
		return greenstarRrpgDaoService.delete(GeographyTypeRrpgDim.class, geographyType);
	}	
	
	@PostMapping(value="/greenstar-rrpg/geography-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographyTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(GeographyTypeRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/geography-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographyTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(GeographyTypeRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * LABOR_POOL - labor-pools
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param laborPoolCd - labor-pool-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/labor-pools","/greenstar-rrpg/labor-pools/{labor-pool-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllLaborPoolByCode(
			  @PathVariable(name="labor-pool-cd", required=false) String laborPoolCd
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
		if (laborPoolCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("labor-pool-cd", laborPoolCd);
		}
		
		return greenstarRrpgDaoService.find(LaborPoolRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/labor-pools", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertLaborPool(@RequestBody LaborPoolRrpgDim fbsLaborPool) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsLaborPool);
	}
	
	@DeleteMapping("/greenstar-rrpg/labor-pools")
	public  ResponseEntity<Integer> deleteAllLaborPools(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(LaborPoolRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/labor-pools/{labor-pool-cd}")
	public ResponseEntity<Integer> deleteLaborPool(@PathVariable(name="labor-pool-cd") @Parameter(description = "FBS LaborPool Code") String laborPoolCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		LaborPoolRrpgDim laborPool = new LaborPoolRrpgDim(laborPoolCd);
		return greenstarRrpgDaoService.delete(LaborPoolRrpgDim.class, laborPool);
	}	
	
	@PostMapping(value="/greenstar-rrpg/labor-pools/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlLaborPools(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pools.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(LaborPoolRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/labor-pools/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlLaborPools(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="labor-pools.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(LaborPoolRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	

	
	/**
	 * GEOGRAPHY - geographies
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param geoCd - geo-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/geographies","/greenstar-rrpg/geographies/{geo-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGeographyByCode(
			  @PathVariable(name="geo-cd", required=false) String geoCd
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
		if (geoCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		
		return greenstarRrpgDaoService.find(GeographyRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/geographies", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeography(@RequestBody GeographyRrpgDim fbsGeography) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsGeography);
	}
	
	@DeleteMapping("/greenstar-rrpg/geographies")
	public  ResponseEntity<Integer> deleteAllGeographys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(GeographyRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/geographies/{geo-cd}")
	public ResponseEntity<Integer> deleteGeography(@PathVariable(name="geo-cd") @Parameter(description = "FBS Geography Code") String geoCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GeographyRrpgDim geography = new GeographyRrpgDim(geoCd);
		return greenstarRrpgDaoService.delete(GeographyRrpgDim.class, geography);
	}	
	
	@PostMapping(value="/greenstar-rrpg/geographies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(GeographyRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/geographies/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(GeographyRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * MARKET - markets
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param mrktCd - mrkt-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/markets","/greenstar-rrpg/markets/{mrkt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllMarketByCode(
			  @PathVariable(name="mrkt-cd", required=false) String mrktCd
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
		if (mrktCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		
		return greenstarRrpgDaoService.find(MarketRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/markets", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertMarket(@RequestBody MarketRrpgDim fbsMarket) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsMarket);
	}
	
	@DeleteMapping("/greenstar-rrpg/markets")
	public  ResponseEntity<Integer> deleteAllMarkets(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(MarketRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/markets/{mrkt-cd}")
	public ResponseEntity<Integer> deleteMarket(@PathVariable(name="mrkt-cd") @Parameter(description = "FBS Market Code") String mrktCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		MarketRrpgDim market = new MarketRrpgDim(mrktCd);
		return greenstarRrpgDaoService.delete(MarketRrpgDim.class, market);
	}	
	
	@PostMapping(value="/greenstar-rrpg/markets/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlMarkets(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(MarketRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/markets/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlMarkets(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(MarketRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * REGION - regions
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param rgnCd - rgn-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/regions","/greenstar-rrpg/regions/{rgn-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllRegionByCode(
			  @PathVariable(name="rgn-cd", required=false) String rgnCd
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
		if (rgnCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("rgn-cd", rgnCd);
		}
		
		return greenstarRrpgDaoService.find(RegionRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/regions", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertRegion(@RequestBody RegionRrpgDim fbsRegion) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsRegion);
	}
	
	@DeleteMapping("/greenstar-rrpg/regions")
	public  ResponseEntity<Integer> deleteAllRegions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(RegionRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/regions/{rgn-cd}")
	public ResponseEntity<Integer> deleteRegion(@PathVariable(name="rgn-cd") @Parameter(description = "FBS Region Code") String rgnCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		RegionRrpgDim region = new RegionRrpgDim(rgnCd);
		return greenstarRrpgDaoService.delete(RegionRrpgDim.class, region);
	}	
	
	@PostMapping(value="/greenstar-rrpg/regions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlRegions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(RegionRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/regions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlRegions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(RegionRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * COUNTRY - countries
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param ctryIsoId - ctry-iso-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/greenstar-rrpg/countries","/greenstar-rrpg/countries/{ctry-iso-id}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryByCode(
			  @PathVariable(name="ctry-iso-id", required=false) String ctryIsoId
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
		if (ctryIsoId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ctry-iso-id", ctryIsoId);
		}
		
		return greenstarRrpgDaoService.find(CountryRrpgDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/greenstar-rrpg/countries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCountry(@RequestBody CountryRrpgDim fbsCountry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return greenstarRrpgDaoService.insert(fbsCountry);
	}
	
	@DeleteMapping("/greenstar-rrpg/countries")
	public  ResponseEntity<Integer> deleteAllCountrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.delete(CountryRrpgDim.class, filters);
	}
	
	@DeleteMapping("/greenstar-rrpg/countries/{ctry-iso-id}")
	public ResponseEntity<Integer> deleteCountry(@PathVariable(name="ctry-iso-id") @Parameter(description = "FBS Country Code") String ctryIsoId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CountryRrpgDim country = new CountryRrpgDim(ctryIsoId);
		return greenstarRrpgDaoService.delete(CountryRrpgDim.class, country);
	}	
	
	@PostMapping(value="/greenstar-rrpg/countries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCountrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(CountryRrpgDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/greenstar-rrpg/countries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCountrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return greenstarRrpgDaoService.etl(CountryRrpgDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
}