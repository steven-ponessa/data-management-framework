package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.ibm.wfm.beans.ihub.Assignment;
import com.ibm.wfm.beans.ihub.AssignmentCalendarDay;
import com.ibm.wfm.beans.ihub.AssignmentScheduleBundle;
import com.ibm.wfm.beans.ihub.BrandIhubDim;
import com.ibm.wfm.beans.ihub.GrowthPlatformDim;
import com.ibm.wfm.beans.ihub.JrsDim;
import com.ibm.wfm.beans.ihub.JrsSkillAssocDim;
import com.ibm.wfm.beans.ihub.PracticeDim;
import com.ibm.wfm.beans.ihub.Schedule;
import com.ibm.wfm.beans.ihub.ScheduleException;
import com.ibm.wfm.beans.ihub.ServiceAreaDim;
import com.ibm.wfm.beans.ihub.ServiceLineDim;
import com.ibm.wfm.beans.ihub.SkillDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.repositories.AssignmentScheduleRepository;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.IhubAssignmentCalendarService;
import com.ibm.wfm.services.IhubDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class IhubDmfController {

	@Autowired
	private IhubDaoService ihubDaoService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	/*
	 * JRS Taxonomy
	 */
	/**
	 * BRAND - brands
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param brandCd          - brand-cd PathVariable. Note that the path variable
	 *                         name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/brands", "/wfm-ihub/brands/{brand-cd}"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllBrandByCode(
			@PathVariable(name = "brand-cd", required = false) String brandCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (brandCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("brand-cd", brandCd);
		}

		return ihubDaoService.find(BrandIhubDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/brands", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertBrand(@RequestBody BrandIhubDim fbsBrand)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsBrand);
	}

	@DeleteMapping("/wfm-ihub/brands")
	public ResponseEntity<Integer> deleteAllBrands(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(BrandIhubDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/brands/{brand-cd}")
	public ResponseEntity<Integer> deleteBrand(
			@PathVariable(name = "brand-cd") @Parameter(description = "FBS Brand Code") String brandCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		BrandIhubDim brand = new BrandIhubDim(brandCd);
		return ihubDaoService.delete(BrandIhubDim.class, brand);
	}

	@PostMapping(value="/wfm-ihub/brands/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBrands(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "brands.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(BrandIhubDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/brands/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBrands(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "brands.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(BrandIhubDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * GROWTH_PLATFORM - growth-platforms
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param growthPlatformCd - growth-platform-cd PathVariable. Note that the path
	 *                         variable name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/growth-platforms",
			"/wfm-ihub/growth-platforms/{growth-platform-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllGrowthPlatformByCode(
			@PathVariable(name = "growth-platform-cd", required = false) String growthPlatformCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (growthPlatformCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("growth-platform-cd", growthPlatformCd);
		}

		return ihubDaoService.find(GrowthPlatformDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/brands/{brand-cd}/growth-platforms",
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllGrowthPlatformByBrandGrowthPlatformCode(
			@PathVariable(name = "growth-platform-cd", required = false) String growthPlatformCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("brand-cd", brandCd);
		if (growthPlatformCd != null) {
			pathVarMap.put("growth-platform-cd", growthPlatformCd);
		}

		return ihubDaoService.find(GrowthPlatformDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/growth-platforms", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertGrowthPlatform(@RequestBody GrowthPlatformDim fbsGrowthPlatform)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsGrowthPlatform);
	}

	@DeleteMapping("/wfm-ihub/growth-platforms")
	public ResponseEntity<Integer> deleteAllGrowthPlatforms(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(GrowthPlatformDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/growth-platforms/{growth-platform-cd}")
	public ResponseEntity<Integer> deleteGrowthPlatform(
			@PathVariable(name = "growth-platform-cd") @Parameter(description = "FBS GrowthPlatform Code") String growthPlatformCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrowthPlatformDim growthPlatform = new GrowthPlatformDim(growthPlatformCd);
		return ihubDaoService.delete(GrowthPlatformDim.class, growthPlatform);
	}

	@PostMapping(value="/wfm-ihub/growth-platforms/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGrowthPlatforms(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "growth-platforms.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(GrowthPlatformDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/growth-platforms/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGrowthPlatforms(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "growth-platforms.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(GrowthPlatformDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * SERVICE_LINE - service-lines
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param serviceLineCd    - service-line-cd PathVariable. Note that the path
	 *                         variable name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/service-lines",
			"/wfm-ihub/service-lines/{service-line-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllServiceLineByCode(
			@PathVariable(name = "service-line-cd", required = false) String serviceLineCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (serviceLineCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("service-line-cd", serviceLineCd);
		}

		return ihubDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/growth-platforms/{growth-platform-cd}/service-lines",
			"/wfm-ihub/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}" }
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllServiceLineByGrowthPlatformServiceLineCode(
			@PathVariable(name = "service-line-cd", required = false) String serviceLineCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("growth-platform-cd", growthPlatformCd);
		if (serviceLineCd != null) {
			pathVarMap.put("service-line-cd", serviceLineCd);
		}

		return ihubDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines",
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}" }
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllServiceLineByBrandGrowthPlatformServiceLineCode(
			@PathVariable(name = "service-line-cd", required = false) String serviceLineCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("brand-cd", brandCd);
		pathVarMap.put("growth-platform-cd", growthPlatformCd);
		if (serviceLineCd != null) {
			pathVarMap.put("service-line-cd", serviceLineCd);
		}

		return ihubDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/service-lines", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertServiceLine(@RequestBody ServiceLineDim fbsServiceLine)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsServiceLine);
	}

	@DeleteMapping("/wfm-ihub/service-lines")
	public ResponseEntity<Integer> deleteAllServiceLines(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(ServiceLineDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/service-lines/{service-line-cd}")
	public ResponseEntity<Integer> deleteServiceLine(
			@PathVariable(name = "service-line-cd") @Parameter(description = "FBS ServiceLine Code") String serviceLineCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		ServiceLineDim serviceLine = new ServiceLineDim(serviceLineCd);
		return ihubDaoService.delete(ServiceLineDim.class, serviceLine);
	}

	@PostMapping(value="/wfm-ihub/service-lines/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlServiceLines(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-lines.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(ServiceLineDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/service-lines/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlServiceLines(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-lines.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(ServiceLineDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * PRACTICE - practices
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param practiceCd       - practice-cd PathVariable. Note that the path
	 *                         variable name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/practices", "/wfm-ihub/practices/{practice-cd}" }
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllPracticeByCode(
			@PathVariable(name = "practice-cd", required = false) String practiceCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (practiceCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("practice-cd", practiceCd);
		}

		return ihubDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/service-lines/{service-line-cd}/practices",
			"/wfm-ihub/service-line/{service-line-cd}/practices/{practice-cd}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllPracticeByServiceLine(
			@PathVariable(name = "practice-cd", required = false) String practiceCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("service-line-cd", serviceLineCd);
		if (practiceCd != null) {
			pathVarMap.put("practice-cd", practiceCd);
		}

		return ihubDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices",
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllPracticeByBrandGpServiceLine(
			@PathVariable(name = "practice-cd", required = false) String practiceCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			// , @RequestParam(required=false, defaultValue="false") @Parameter(description =
			// "Include parent nodes?") boolean includeParentage
			,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("brand-cd", brandCd);
		pathVarMap.put("growth-platform-cd", growthPlatformCd);
		pathVarMap.put("service-line-cd", serviceLineCd);
		if (practiceCd != null) {
			pathVarMap.put("practice-cd", practiceCd);
		}

		return ihubDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/practices", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertPractice(@RequestBody PracticeDim fbsPractice)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsPractice);
	}

	@DeleteMapping("/wfm-ihub/practices")
	public ResponseEntity<Integer> deleteAllPractices(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(PracticeDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/practices/{practice-cd}")
	public ResponseEntity<Integer> deletePractice(
			@PathVariable(name = "practice-cd") @Parameter(description = "FBS Practice Code") String practiceCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		PracticeDim practice = new PracticeDim(practiceCd);
		return ihubDaoService.delete(PracticeDim.class, practice);
	}

	@PostMapping(value="/wfm-ihub/practices/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlPractices(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "practices.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(PracticeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/practices/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlPractices(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "practices.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(PracticeDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * SERVICE_AREA - service-areas
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param serviceAreaCd    - service-area-cd PathVariable. Note that the path
	 *                         variable name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/service-areas",
			"/wfm-ihub/service-areas/{service-area-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllServiceAreaByCode(
			@PathVariable(name = "service-area-cd", required = false) String serviceAreaCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (serviceAreaCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("service-area-cd", serviceAreaCd);
		}

		return ihubDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/practices/{practice-cd}/service-areas",
			"/wfm-ihub/practices/{practice-cd}/service-areas/{service-area-cd}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized <T> ResponseEntity<Object> retrieveAllServiceAreaByPracticeCode(
			@PathVariable(name = "service-area-cd", required = false) String serviceAreaCd,
			@PathVariable(name = "practice-cd") String practiceCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("practice-cd", practiceCd);
		if (serviceAreaCd != null) {
			pathVarMap.put("service-area-cd", serviceAreaCd);
		}

		return ihubDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas",
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllServiceAreaByBrandGpSlPracticeCode(
			@PathVariable(name = "service-area-cd", required = false) String serviceAreaCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@PathVariable(name = "practice-cd") String practiceCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			// , @RequestParam(required=false, defaultValue="false") @Parameter(description =
			// "Include parent nodes?") boolean includeParentage
			,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("brand-cd", brandCd);
		pathVarMap.put("growth-platform-cd", growthPlatformCd);
		pathVarMap.put("service-line-cd", serviceLineCd);
		pathVarMap.put("practice-cd", practiceCd);
		if (serviceAreaCd != null) {
			pathVarMap.put("service-area-cd", serviceAreaCd);
		}

		return ihubDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/service-areas", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertServiceArea(@RequestBody ServiceAreaDim fbsServiceArea)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsServiceArea);
	}

	@DeleteMapping("/wfm-ihub/service-areas")
	public ResponseEntity<Integer> deleteAllServiceAreas(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(ServiceAreaDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/service-areas/{service-area-cd}")
	public ResponseEntity<Integer> deleteServiceArea(
			@PathVariable(name = "service-area-cd") @Parameter(description = "FBS ServiceArea Code") String serviceAreaCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		ServiceAreaDim serviceArea = new ServiceAreaDim(serviceAreaCd);
		return ihubDaoService.delete(ServiceAreaDim.class, serviceArea);
	}

	@PostMapping(value="/wfm-ihub/service-areas/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlServiceAreas(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-areas.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(ServiceAreaDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/service-areas/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlServiceAreas(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-areas.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(ServiceAreaDim.class, oldFile, newFile, keyLength, outputFileName);
	}


	/**
	 * JRS - jrss
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param jrsCd            - jrs-cd PathVariable. Note that the path variable
	 *                         name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/wfm-ihub/jrss", "/wfm-ihub/jrss/{jrs-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllJrsByCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (jrsCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}

		return ihubDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/wfm-ihub/service-areas/{service-area-cd}/jrss",
			"/wfm-ihub/service-areas/{service-area-cd}/jrss/{jrs-cd}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllJrsByServiceAreaCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@PathVariable(name = "service-area-cd") String serviceAreaCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include parent nodes?") boolean includeParentage,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("service-area-cd", serviceAreaCd);
		if (jrsCd != null) {
			pathVarMap.put("jrs-cd", jrsCd);
		}

		return ihubDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}/jrss",
			"/wfm-ihub/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}/jrss/{jrs-cd}" }
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllJrsByBrandGpSlPracticeSaCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@PathVariable(name = "practice-cd") String practiceCd,
			@PathVariable(name = "service-area-cd") String serviceAreaCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include only EDS data?") boolean edsOnly,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("brand-cd", brandCd);
		pathVarMap.put("growth-platform-cd", growthPlatformCd);
		pathVarMap.put("service-line-cd", serviceLineCd);
		pathVarMap.put("practice-cd", practiceCd);
		pathVarMap.put("service-area-cd", serviceAreaCd);
		if (jrsCd != null) {
			pathVarMap.put("jrs-cd", jrsCd);
		}

		return ihubDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit,
				pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/wfm-ihub/jrss", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertJrs(@RequestBody JrsDim fbsJrs)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.insert(fbsJrs);
	}

	@DeleteMapping("/wfm-ihub/jrss")
	public ResponseEntity<Integer> deleteAllJrss(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(JrsDim.class, filters);
	}

	@DeleteMapping("/wfm-ihub/jrss/{jrs-cd}")
	public ResponseEntity<Integer> deleteJrs(
			@PathVariable(name = "jrs-cd") @Parameter(description = "FBS Jrs Code") String jrsCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsDim jrs = new JrsDim(jrsCd);
		return ihubDaoService.delete(JrsDim.class, jrs);
	}

	@PostMapping(value="/wfm-ihub/jrss/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrss(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrss.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(JrsDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/jrss/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrss(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrss.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return ihubDaoService.etl(JrsDim.class, oldFile, newFile, keyLength, outputFileName);
	}


	/**
	 * JRS_SKILL_ASSOC - jrs-skill-assocs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param jrsCd - jrs-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/jrs-skill-assocs","/wfm-ihub/jrs-skill-assocs/{jrs-cd}/{skill-id}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllJrsSkillAssocByCode(
			  @PathVariable(name="jrs-cd", required=false) String jrsCd
			, @PathVariable(name="skill-id", required=false) String skillId
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (jrsCd!=null) {
			if (pathVarMap == null) pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}
		if (skillId!=null) {
			if (pathVarMap == null) pathVarMap = new HashMap<>();
			pathVarMap.put("skill-id", Integer.valueOf(skillId));
		}
		
		return ihubDaoService.find(JrsSkillAssocDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , false //, includeParentage
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
	
	@GetMapping(path={"/wfm-ihub/jrs-skill-assocs/{jrs-cd}/skills"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllJrsSkillAssocByJrsCode(
			  @PathVariable(name="jrs-cd") String jrsCd
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
		if (jrsCd!=null) {
			if (pathVarMap == null) pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}
		
		return ihubDaoService.find(JrsSkillAssocDim.class
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
	
	
	@PostMapping(value="/wfm-ihub/jrs-skill-assocs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertJrsSkillAssoc(@RequestBody JrsSkillAssocDim fbsJrsSkillAssoc) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return ihubDaoService.insert(fbsJrsSkillAssoc);
	}
	
	@DeleteMapping("/wfm-ihub/jrs-skill-assocs")
	public  ResponseEntity<Integer> deleteAllJrsSkillAssocs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(JrsSkillAssocDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/jrs-skill-assocs/{jrs-cd}/{skill-id}")
	public ResponseEntity<Integer> deleteJrsSkillAssoc(@PathVariable(name="jrs-cd") @Parameter(description = "Jrs Code") String jrsCd
			                                         , @PathVariable(name="skill-id") @Parameter(description = "Skill Id") String skillId
			) throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsSkillAssocDim jrsSkillAssoc = new JrsSkillAssocDim(jrsCd, Integer.valueOf(skillId));
		return ihubDaoService.delete(JrsSkillAssocDim.class, jrsSkillAssoc);
	}	
	
	@PostMapping(value="/wfm-ihub/jrs-skill-assocs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrsSkillAssocs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-skill-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.etl(JrsSkillAssocDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/jrs-skill-assocs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrsSkillAssocs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-skill-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.etl(JrsSkillAssocDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	/**
	 * SKILL - skills
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param skillId - skill-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-ihub/skills","/wfm-ihub/skills/{skill-id}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSkillByCode(
			  @PathVariable(name="skill-id", required=false) String skillId
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (skillId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("skill-id", skillId);
		}
		
		return ihubDaoService.find(SkillDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , false      //includeParentage
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
	
	
	@PostMapping(value="/wfm-ihub/skills", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSkill(@RequestBody SkillDim fbsSkill) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return ihubDaoService.insert(fbsSkill);
	}
	
	@DeleteMapping("/wfm-ihub/skills")
	public  ResponseEntity<Integer> deleteAllSkills(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.delete(SkillDim.class, filters);
	}
	
	@DeleteMapping("/wfm-ihub/skills/{skill-id}")
	public ResponseEntity<Integer> deleteSkill(@PathVariable(name="skill-id") @Parameter(description = "FBS Skill Code") String skillId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SkillDim skill = new SkillDim(Integer.valueOf(skillId));
		return ihubDaoService.delete(SkillDim.class, skill);
	}	
	
	@PostMapping(value="/wfm-ihub/skills/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSkills(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.etl(SkillDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-ihub/skills/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSkills(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ihubDaoService.etl(SkillDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	@GetMapping(path = "/wfm-ihub/jrss/{jrs-cd}/skills", produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveSkillsForJrs(@PathVariable(name = "jrs-cd") String jrsCd,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			//@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		
		//ihubDaoService.setT(SkillDim.class);

		String sql = "SELECT S.SKILL_SK_ID, S.SKILL_ID, S.SKILL_NM, S.SKILL_DESC FROM REFT.SKILL_DIM_V S INNER JOIN REFT.JRS_SKILL_ASSOC_DIM_V A ON S.SKILL_ID=A.SKILL_ID WHERE A.JRS_CD='"+jrsCd+"' ORDER BY S.SKILL_ID";

		return ihubDaoService.getListForSql(SkillDim.class, ihubDaoService.getConnection(), sql, returnCsv, request);
	}
	
	/*
	 * get 13 week calendar
	 */
	
	@GetMapping(
		    path = {"/wfm-ihub/assignment-calendar/{assignmentId}", "/wfm-ihub/assignment-calendar"},
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Object> getAssignment13WeekCalendar(
		        @PathVariable(name = "assignmentId", required = false) String assignmentId,
		        @RequestParam(name = "assignmentId", required = false) String assignmentIdParam,
		        @RequestParam(required = false, defaultValue = "false")
		        @Parameter(description = "Return results as CSV")
		        boolean returnCsv) {

		    String resolvedAssignmentId =
		            assignmentId != null && !assignmentId.isBlank() ? assignmentId : assignmentIdParam;

		    if (resolvedAssignmentId == null || resolvedAssignmentId.isBlank()) {
		        return ResponseEntity.badRequest()
		                .body("A value must be provided for assignmentId.");
		    }

		    try (java.sql.Connection conn = ihubDaoService.getConnection()) {

		    	AssignmentScheduleRepository assignmentScheduleRepository = new AssignmentScheduleRepository();
		        AssignmentScheduleBundle bundle =
		                assignmentScheduleRepository.loadAssignmentScheduleBundle(conn, resolvedAssignmentId);

		        if (bundle == null || bundle.getAssignment() == null || bundle.getSchedule() == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                    .body("No Assignment/Schedule data found for assignmentId=" + resolvedAssignmentId);
		        }

		        IhubAssignmentCalendarService assignmentCalendarService = new IhubAssignmentCalendarService();
		        List<AssignmentCalendarDay> calendar =
		        		assignmentCalendarService.generate13WeekCalendar(bundle);

		        if (returnCsv) {
		            String csv = buildAssignmentCalendarCsv(calendar);
		            return ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_DISPOSITION,
		                            "attachment; filename=assignment-calendar-" + resolvedAssignmentId + ".csv")
		                    .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
		                    .body(csv);
		        }

		        return ResponseEntity.ok(calendar);

		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("Error generating 13-week calendar for assignmentId=" + resolvedAssignmentId
		                        + ": " + e.getMessage());
		    }
		}
	
	@GetMapping(
		    path = {"/wfm-ihub/assignment-schedule/{assignmentId}", "/wfm-ihub/assignment-schedule"},
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Object> getAssignmentScheduleBundle(
		        @PathVariable(name = "assignmentId", required = false) String assignmentId,
		        @RequestParam(name = "assignmentId", required = false) String assignmentIdParam,
		        @RequestParam(required = false, defaultValue = "false")
		        @Parameter(description = "Return results as CSV")
		        boolean returnCsv) {

		    String resolvedAssignmentId =
		            (assignmentId != null && !assignmentId.isBlank())
		                    ? assignmentId
		                    : assignmentIdParam;

		    if (resolvedAssignmentId == null || resolvedAssignmentId.isBlank()) {
		        return ResponseEntity.badRequest()
		                .body("A value must be provided for assignmentId.");
		    }

		    try (java.sql.Connection conn = ihubDaoService.getConnection()) {

		    	AssignmentScheduleRepository assignmentScheduleRepository = new AssignmentScheduleRepository();
		        AssignmentScheduleBundle bundle =
		                assignmentScheduleRepository.loadAssignmentScheduleBundle(conn, resolvedAssignmentId);

		        if (bundle == null || bundle.getAssignment() == null || bundle.getSchedule() == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                    .body("No Assignment/Schedule data found for assignmentId=" + resolvedAssignmentId);
		        }
		        if (returnCsv) {
		            String csv = buildAssignmentScheduleBundleCsv(bundle);

		            return ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_DISPOSITION,
		                            "attachment; filename=assignment-schedule-" + resolvedAssignmentId + ".csv")
		                    .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
		                    .body(csv);
		        }

		        return ResponseEntity.ok(bundle);

		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("Error retrieving AssignmentScheduleBundle for assignmentId=" + resolvedAssignmentId
		                        + ": " + e.getMessage());
		    }
		}
	
	/*
	 * CSV Helper Methods
	 */
	private String buildAssignmentCalendarCsv(List<AssignmentCalendarDay> calendar) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("ASSIGNMENT_ID,")
	      .append("ASSIGNMENT_NAME,")
	      .append("SCHEDULE_ID,")
	      .append("SCHEDULE_NAME,")
	      .append("DATE_DT,")
	      .append("EFFECTIVE_HOURS,")
	      .append("HOURS_SOURCE,")
	      .append("EXCEPTION_ID")
	      .append("\n");

	    for (AssignmentCalendarDay day : calendar) {
	        sb.append(csv(day.getAssignmentId())).append(",")
	          .append(csv(day.getAssignmentName())).append(",")
	          .append(csv(day.getScheduleId())).append(",")
	          .append(csv(day.getScheduleName())).append(",")
	          .append(csv(day.getDate())).append(",")
	          .append(csv(day.getEffectiveHours())).append(",")
	          .append(csv(day.getHoursSource())).append(",")
	          .append(csv(day.getExceptionId()))
	          .append("\n");
	    }

	    return sb.toString();
	}	
	
	private String buildAssignmentScheduleBundleCsv(AssignmentScheduleBundle bundle) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("ASSIGNMENT_ID,")
	      .append("ASSIGNMENT_NAME,")
	      .append("ASG_START_DT,")
	      .append("ASG_END_DT,")
	      .append("SCHEDULE_ID,")
	      .append("SCHEDULE_NAME,")
	      .append("PSE_WEEK_TOTAL_HOURS,")
	      .append("PSE_SCHEDULED_DAYS,")
	      .append("PSE_SCHEDULED_HOURS,")
	      .append("PSE_IS_BLOCKING,")
	      .append("T_MO,")
	      .append("T_TU,")
	      .append("T_WE,")
	      .append("T_TH,")
	      .append("T_FR,")
	      .append("T_SA,")
	      .append("T_SU,")
	      .append("EXCEPTION_ID,")
	      .append("EXC_START_DT,")
	      .append("EXC_END_DT,")
	      .append("LASTMODIFIEDDATE,")
	      .append("X_MO,")
	      .append("X_TU,")
	      .append("X_WE,")
	      .append("X_TH,")
	      .append("X_FR,")
	      .append("X_SA,")
	      .append("X_SU")
	      .append("\n");

	    Assignment a = bundle.getAssignment();
	    Schedule s = bundle.getSchedule();
	    List<ScheduleException> exceptions = bundle.getScheduleExceptions();

	    if (exceptions == null || exceptions.isEmpty()) {
	        appendBundleCsvRow(sb, a, s, null);
	    } else {
	        for (ScheduleException e : exceptions) {
	            appendBundleCsvRow(sb, a, s, e);
	        }
	    }

	    return sb.toString();
	}
	
	private void appendBundleCsvRow(
	        StringBuilder sb,
	        Assignment a,
	        Schedule s,
	        ScheduleException e) {

	    sb.append(csv(a != null ? a.getAssignmentId() : null)).append(",")
	      .append(csv(a != null ? a.getAssignmentName() : null)).append(",")
	      .append(csv(a != null ? a.getStartDate() : null)).append(",")
	      .append(csv(a != null ? a.getEndDate() : null)).append(",")
	      .append(csv(s != null ? s.getScheduleId() : null)).append(",")
	      .append(csv(s != null ? s.getScheduleName() : null)).append(",")
	      .append(csv(s != null ? s.getWeekTotalHours() : null)).append(",")
	      .append(csv(s != null ? s.getScheduledDays() : null)).append(",")
	      .append(csv(s != null ? s.getScheduledHours() : null)).append(",")
	      .append(csv(s != null ? s.getBlocking() : null)).append(",")
	      .append(csv(s != null ? s.getMondayHours() : null)).append(",")
	      .append(csv(s != null ? s.getTuesdayHours() : null)).append(",")
	      .append(csv(s != null ? s.getWednesdayHours() : null)).append(",")
	      .append(csv(s != null ? s.getThursdayHours() : null)).append(",")
	      .append(csv(s != null ? s.getFridayHours() : null)).append(",")
	      .append(csv(s != null ? s.getSaturdayHours() : null)).append(",")
	      .append(csv(s != null ? s.getSundayHours() : null)).append(",");

	    if (e != null) {
	        sb.append(csv(e.getExceptionId())).append(",")
	          .append(csv(e.getStartDate())).append(",")
	          .append(csv(e.getEndDate())).append(",")
	          .append(csv(e.getLastModifiedDate())).append(",")
	          .append(csv(e.getMondayHours())).append(",")
	          .append(csv(e.getTuesdayHours())).append(",")
	          .append(csv(e.getWednesdayHours())).append(",")
	          .append(csv(e.getThursdayHours())).append(",")
	          .append(csv(e.getFridayHours())).append(",")
	          .append(csv(e.getSaturdayHours())).append(",")
	          .append(csv(e.getSundayHours()));
	    } else {
	        sb.append(",,,,,,,,,,");
	    }

	    sb.append("\n");
	}
	
	private String csv(Object value) {
	    if (value == null) {
	        return "";
	    }

	    String text = String.valueOf(value);

	    boolean mustQuote =
	            text.contains(",") ||
	            text.contains("\"") ||
	            text.contains("\n") ||
	            text.contains("\r");

	    if (text.contains("\"")) {
	        text = text.replace("\"", "\"\"");
	    }

	    return mustQuote ? "\"" + text + "\"" : text;
	}	
	

}