package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EnhancedExpertiseTaxonomyDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GrowthPlatformDim;
import com.ibm.wfm.beans.JobRoleDescriptionDim;
import com.ibm.wfm.beans.JobRoleDim;
import com.ibm.wfm.beans.JrsCompensationGradeDim;
import com.ibm.wfm.beans.JrsDescriptionDim;
import com.ibm.wfm.beans.JrsDim;
import com.ibm.wfm.beans.JrsSkillAssocDim;
import com.ibm.wfm.beans.OfferingComponentDim;
import com.ibm.wfm.beans.OfferingDim;
import com.ibm.wfm.beans.OfferingPortfolioDim;
import com.ibm.wfm.beans.PracticeDim;
import com.ibm.wfm.beans.ServiceAreaDim;
import com.ibm.wfm.beans.ServiceLineDim;
import com.ibm.wfm.beans.SkillDim;
import com.ibm.wfm.beans.SkillJrsDim;
import com.ibm.wfm.beans.SpecialtyDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoJrsTaxonomyController {

	@Autowired
	private EdsDaoService edsDaoService;
	//private IhubDaoService edsDaoService;

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
	@GetMapping(path = { "/eds-ut-jrs-tax/brands", "/eds-ut-jrs-tax/brands/{brand-cd}" }, produces = {
			"application/json", "application/xml" })
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

		return edsDaoService.find(BrandDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/brands", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertBrand(@RequestBody BrandDim fbsBrand)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsBrand);
	}

	@DeleteMapping("/eds-ut-jrs-tax/brands")
	public ResponseEntity<Integer> deleteAllBrands(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BrandDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/brands/{brand-cd}")
	public ResponseEntity<Integer> deleteBrand(
			@PathVariable(name = "brand-cd") @Parameter(description = "FBS Brand Code") String brandCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		BrandDim brand = new BrandDim(brandCd);
		return edsDaoService.delete(BrandDim.class, brand);
	}

	@PostMapping(value="/eds-ut-jrs-tax/brands/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBrands(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "brands.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(BrandDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/brands/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBrands(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "brands.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(BrandDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/*
	 * @GetMapping(path="/eds/growth-platforms",produces = { "application/json",
	 * "application/xml"}) public <T> List<T> retrieveAllGrowthPlatformsTaxCsv(
	 * 
	 * @RequestParam(defaultValue = "") @Parameter(description =
	 * "Add filter in format of a SQL WHERE clause.") String filters
	 * , @RequestParam(required=false, defaultValue="false") @Parameter(description =
	 * "Include parent nodes?") boolean includeParentage
	 * , @RequestParam(required=false, defaultValue="false") @Parameter(description =
	 * "Include Point in Time data?") boolean includePit
	 * , @RequestParam(required=false, defaultValue="") @Parameter(description =
	 * "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)"
	 * ) String pit , @RequestParam(defaultValue = "All") @Parameter(description =
	 * "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize )
	 * throws SQLException, ClassNotFoundException, IOException {
	 * edsDaoService.setT(GrowthPlatformDim.class);
	 * edsDaoService.setTableNm("REFT.GROWTH_PLATFORM_DIM_V");
	 * edsDaoService.setScdTableNm("REFT.GROWTH_PLATFORM_SCD_V");
	 * 
	 * boolean useScd = false; if (includePit==true || (pit!=null &&
	 * pit.trim().length()>0)) useScd = true; int size = -1; if
	 * (!resultSetMaxSize.equalsIgnoreCase("all")) size =
	 * Integer.parseInt(resultSetMaxSize);
	 * 
	 * if (includeParentage) return edsDaoService.findAllTax(filters, null, size,
	 * false); else { if (!useScd) pit=null; return edsDaoService.findAll(filters,
	 * pit, size); } }
	 */

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
	@GetMapping(path = { "/eds-ut-jrs-tax/growth-platforms",
			"/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}" }, produces = { "application/json",
					"application/xml" })
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

		return edsDaoService.find(GrowthPlatformDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(GrowthPlatformDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/growth-platforms", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertGrowthPlatform(@RequestBody GrowthPlatformDim fbsGrowthPlatform)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsGrowthPlatform);
	}

	@DeleteMapping("/eds-ut-jrs-tax/growth-platforms")
	public ResponseEntity<Integer> deleteAllGrowthPlatforms(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(GrowthPlatformDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}")
	public ResponseEntity<Integer> deleteGrowthPlatform(
			@PathVariable(name = "growth-platform-cd") @Parameter(description = "FBS GrowthPlatform Code") String growthPlatformCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrowthPlatformDim growthPlatform = new GrowthPlatformDim(growthPlatformCd);
		return edsDaoService.delete(GrowthPlatformDim.class, growthPlatform);
	}

	@PostMapping(value="/eds-ut-jrs-tax/growth-platforms/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGrowthPlatforms(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "growth-platforms.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(GrowthPlatformDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/growth-platforms/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGrowthPlatforms(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "growth-platforms.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(GrowthPlatformDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path = { "/eds-ut-jrs-tax/service-lines",
			"/eds-ut-jrs-tax/service-lines/{service-line-cd}" }, produces = { "application/json", "application/xml" })
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

		return edsDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}/service-lines",
			"/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(ServiceLineDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/service-lines", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertServiceLine(@RequestBody ServiceLineDim fbsServiceLine)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsServiceLine);
	}

	@DeleteMapping("/eds-ut-jrs-tax/service-lines")
	public ResponseEntity<Integer> deleteAllServiceLines(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(ServiceLineDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/service-lines/{service-line-cd}")
	public ResponseEntity<Integer> deleteServiceLine(
			@PathVariable(name = "service-line-cd") @Parameter(description = "FBS ServiceLine Code") String serviceLineCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		ServiceLineDim serviceLine = new ServiceLineDim(serviceLineCd);
		return edsDaoService.delete(ServiceLineDim.class, serviceLine);
	}

	@PostMapping(value="/eds-ut-jrs-tax/service-lines/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlServiceLines(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-lines.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(ServiceLineDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/service-lines/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlServiceLines(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-lines.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(ServiceLineDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path = { "/eds-ut-jrs-tax/practices", "/eds-ut-jrs-tax/practices/{practice-cd}" }, produces = {
			"application/json", "application/xml" })
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

		return edsDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/service-lines/{service-line-cd}/practices",
			"/eds-ut-jrs-tax/service-line/{service-line-cd}/practices/{practice-cd}" }, produces = { "application/json",
					"application/xml" })
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

		return edsDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(PracticeDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/practices", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertPractice(@RequestBody PracticeDim fbsPractice)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsPractice);
	}

	@DeleteMapping("/eds-ut-jrs-tax/practices")
	public ResponseEntity<Integer> deleteAllPractices(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(PracticeDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/practices/{practice-cd}")
	public ResponseEntity<Integer> deletePractice(
			@PathVariable(name = "practice-cd") @Parameter(description = "FBS Practice Code") String practiceCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		PracticeDim practice = new PracticeDim(practiceCd);
		return edsDaoService.delete(PracticeDim.class, practice);
	}

	@PostMapping(value="/eds-ut-jrs-tax/practices/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlPractices(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "practices.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(PracticeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/practices/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlPractices(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "practices.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(PracticeDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path = { "/eds-ut-jrs-tax/service-areas",
			"/eds-ut-jrs-tax/service-areas/{service-area-cd}" }, produces = { "application/json", "application/xml" })
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

		return edsDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/practices/{practice-cd}/service-areas",
			"/eds-ut-jrs-tax/practices/{practice-cd}/service-areas/{service-area-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(ServiceAreaDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/service-areas", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertServiceArea(@RequestBody ServiceAreaDim fbsServiceArea)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsServiceArea);
	}

	@DeleteMapping("/eds-ut-jrs-tax/service-areas")
	public ResponseEntity<Integer> deleteAllServiceAreas(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(ServiceAreaDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/service-areas/{service-area-cd}")
	public ResponseEntity<Integer> deleteServiceArea(
			@PathVariable(name = "service-area-cd") @Parameter(description = "FBS ServiceArea Code") String serviceAreaCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		ServiceAreaDim serviceArea = new ServiceAreaDim(serviceAreaCd);
		return edsDaoService.delete(ServiceAreaDim.class, serviceArea);
	}

	@PostMapping(value="/eds-ut-jrs-tax/service-areas/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlServiceAreas(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-areas.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(ServiceAreaDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/service-areas/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlServiceAreas(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "service-areas.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(ServiceAreaDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/*
	 * job-role
	 */
	@GetMapping(path = "/eds-ut-jrs-tax/job-roles", produces = { "application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllJobRoles(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {

		edsDaoService.setT(JobRoleDim.class);
		

		//String sql = "SELECT DISTINCT JOB_ROLE_CD, JOB_ROLE_NM FROM REFT.JRS_DIM_V ORDER BY JOB_ROLE_NM";
		String sql = "SELECT DISTINCT JOB_ROLE_CD, JOB_ROLE_NM, CMPNSTN_GRD_LST, INCENTIVE_FLG FROM REFT.JRS_DIM_V ORDER BY JOB_ROLE_NM";

		//return edsDaoService.getListForSql(JobRoleDim.class, edsDaoService.getConnection(), sql);
		return edsDaoService.getListForSql(JobRoleDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
	}

	@GetMapping(path = "/eds-ut-jrs-tax/practices/{practice-cd}/job-roles", produces = { "application/json",
			"application/xml" })
	//public <T> List<T> retrieveJobRolesByPractice(@PathVariable(name = "practice-cd") String practiceCd,
	public <T> ResponseEntity<Object> retrieveJobRolesByPractice(@PathVariable(name = "practice-cd") String practiceCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			//@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		edsDaoService.setT(JobRoleDim.class);
		//edsDaoService.setTableNm("REFT.JRS_DIM_V");
		//edsDaoService.setScdTableNm("REFT.JRS_SCD_V");

		String sql = "SELECT DISTINCT JRS.JOB_ROLE_CD, JRS.JOB_ROLE_NM, CMPNSTN_GRD_LST, INCENTIVE_FLG FROM REFT.JRS_DIM_V JRS INNER JOIN REFT.SERVICE_AREA_DIM_V SA ON JRS.SERVICE_AREA_CD=SA.SERVICE_AREA_CD"
				+ " WHERE SA.PRACTICE_CD";
		if (practiceCd.equalsIgnoreCase("generic"))
			sql += " IN  ('GBS204','GBS208','GBS209','GBS213')";
		else
			sql += ("='" + practiceCd + "'");
		if (filters != null && filters.trim().length() > 0)
			sql += " AND ( " + filters + " )";
		sql += " ORDER BY 2";
		
		//return edsDaoService.getListForSql(JobRoleDim.class, edsDaoService.getConnection(), sql);
		return edsDaoService.getListForSql(JobRoleDim.class, edsDaoService.getConnection(), sql, returnCsv, request);

	}

	@GetMapping(path = "/eds-ut-jrs-tax/practices/{practice-cd}/job-roles/{job-role-cd}/specialties", produces = {
			"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveSpecialtiesByPracticeAndJobRoles(@PathVariable(name = "practice-cd") String practiceCd,
			@PathVariable(name = "job-role-cd") String jobRoleCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			//@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			//@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		edsDaoService.setT(SpecialtyDim.class);

		String sql = "SELECT DISTINCT JRS.SPECIALTY_CD, JRS.SPECIALTY_NM, JRS.JOB_ROLE_CD, JRS.CMPNSTN_GRD_LST, JRS.INCENTIVE_FLG FROM REFT.JRS_DIM_V JRS INNER JOIN REFT.SERVICE_AREA_DIM_V SA ON JRS.SERVICE_AREA_CD=SA.SERVICE_AREA_CD"
				+ " WHERE SA.PRACTICE_CD";
		if (practiceCd.equalsIgnoreCase("generic"))
			sql += " IN  ('GBS204','GBS208','GBS209','GBS213')";
		else
			sql += ("='" + practiceCd + "'");
		sql += " AND JOB_ROLE_CD='" + jobRoleCd + "'";
		sql += " ORDER BY 2";
		return edsDaoService.getListForSql(SpecialtyDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
	}

	/*
	 * job-role
	 */
	@GetMapping(path = "/eds-ut-jrs-tax/specialties", produces = { "application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllSpecialtiess(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			//@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			//@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		edsDaoService.setT(SpecialtyDim.class);

		String sql = "SELECT DISTINCT SPECIALTY_CD, SPECIALTY_NM, JOB_ROLE_CD FROM REFT.JRS_DIM_V ORDER BY SPECIALTY_NM";
		return edsDaoService.getListForSql(SpecialtyDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
	}

	@GetMapping(path = "/eds-ut-jrs-tax/job-roles/{job-role-cd}/specialties", produces = { "application/json",
			"application/xml" })
	public <T> ResponseEntity<Object> retrieveSpecialtiesForJobRole(@PathVariable(name = "job-role-cd") String jobRoleCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			//@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			//@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		edsDaoService.setT(SpecialtyDim.class);


		String sql = "SELECT  SPECIALTY_CD, SPECIALTY_NM, JOB_ROLE_CD FROM REFT.JRS_DIM_V WHERE JOB_ROLE_CD='"
				+ jobRoleCd + "' ORDER BY SPECIALTY_NM";
		return edsDaoService.getListForSql(SpecialtyDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
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
	@GetMapping(path = { "/eds-ut-jrs-tax/jrss", "/eds-ut-jrs-tax/jrss/{jrs-cd}" }, produces = { "application/json",
			"application/xml" })
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

		return edsDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/service-areas/{service-area-cd}/jrss",
			"/eds-ut-jrs-tax/service-areas/{service-area-cd}/jrss/{jrs-cd}" }, produces = { "application/json",
					"application/xml" })
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

		return edsDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}/jrss",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/practices/{practice-cd}/service-areas/{service-area-cd}/jrss/{jrs-cd}" }, produces = {
					"application/json", "application/xml" })
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

		return edsDaoService.find(JrsDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit,
				pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/jrss", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertJrs(@RequestBody JrsDim fbsJrs)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsJrs);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrss")
	public ResponseEntity<Integer> deleteAllJrss(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(JrsDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrss/{jrs-cd}")
	public ResponseEntity<Integer> deleteJrs(
			@PathVariable(name = "jrs-cd") @Parameter(description = "FBS Jrs Code") String jrsCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsDim jrs = new JrsDim(jrsCd);
		return edsDaoService.delete(JrsDim.class, jrs);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrss/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrss(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrss.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrss/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrss(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrss.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * OFFERING_PORTFOLIO - offering-portfolios
	 */

	/**
	 * 
	 * @param <T>                 - Object type being retrieved/returned.
	 * @param offeringPortfolioCd - offering-portfolio-cd PathVariable. Note that
	 *                            the path variable name must match the table column
	 *                            name.
	 * @param filters             - filter in format of a SQL WHERE clause.
	 *                            Default="".
	 * @param orderByCols         - Order by columns. Default="" (orders by natural
	 *                            key).
	 * @param includeParentage    - Return parent taxonomy of object. Omit for root
	 *                            of taxonomy. Default="".
	 * @param returnCsv           - API to return results as CSV. Default=false.
	 * @param includePit          - API to include Point in Time (PIT) data.
	 *                            Default=false.
	 * @param pit                 - Specific Point in Time (format:
	 *                            yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                            2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                            state).
	 * @param resultSetMaxSize    - Size of the result to be returned, use ‘ALL’ to
	 *                            get all data. Default="All".
	 * @param request             - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/eds-ut-jrs-tax/offering-portfolios",
			"/eds-ut-jrs-tax/offering-portfolios/{offering-portfolio-cd}" }, produces = { "application/json",
					"application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingPortfolioByCode(
			@PathVariable(name = "offering-portfolio-cd", required = false) String offeringPortfolioCd,
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
		if (offeringPortfolioCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("offering-portfolio-cd", offeringPortfolioCd);
		}

		return edsDaoService.find(OfferingPortfolioDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios/{offering-portfolio-cd}" }, produces = {
					"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingPortfoliosByBrandGpServiceLine(
			@PathVariable(name = "offering-portfolio-cd", required = false) String offeringPortfolioCd,
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
		if (offeringPortfolioCd != null) {
			pathVarMap.put("offering-portfolio-cd", offeringPortfolioCd);
		}

		return edsDaoService.find(OfferingPortfolioDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/offering-portfolios", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertOfferingPortfolio(
			@RequestBody OfferingPortfolioDim fbsOfferingPortfolio)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsOfferingPortfolio);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offering-portfolios")
	public ResponseEntity<Integer> deleteAllOfferingPortfolios(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(OfferingPortfolioDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offering-portfolios/{offering-portfolio-cd}")
	public ResponseEntity<Integer> deleteOfferingPortfolio(
			@PathVariable(name = "offering-portfolio-cd") @Parameter(description = "FBS OfferingPortfolio Code") String offeringPortfolioCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		OfferingPortfolioDim offeringPortfolio = new OfferingPortfolioDim(offeringPortfolioCd);
		return edsDaoService.delete(OfferingPortfolioDim.class, offeringPortfolio);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offering-portfolios/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOfferingPortfolios(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offering-portfolios.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingPortfolioDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offering-portfolios/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOfferingPortfolios(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offering-portfolios.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingPortfolioDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * OFFERING - offerings
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param offeringCd       - offering-cd PathVariable. Note that the path
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
	@GetMapping(path = { "/eds-ut-jrs-tax/offerings", "/eds-ut-jrs-tax/offerings/{offering-cd}" }, produces = {
			"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingByCode(
			@PathVariable(name = "offering-cd", required = false) String offeringCd,
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
		if (offeringCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cd", offeringCd);
		}

		return edsDaoService.find(OfferingDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios/{offering-portfolio-cd}/offerings",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios/{offering-portfolio-cd}/offerings/{offering-cd}" }, produces = {
					"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingByBrandGpSlOfferingPorttfolioCode(
			@PathVariable(name = "offering-cd", required = false) String offeringCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@PathVariable(name = "offering-portfolio-cd") String offeringPortfolioCd,
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
		pathVarMap.put("offering-portfolio-cd", offeringPortfolioCd);
		if (offeringCd != null) {
			pathVarMap.put("offering-cd", offeringCd);
		}

		return edsDaoService.find(OfferingDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/offerings", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertOffering(@RequestBody OfferingDim fbsOffering)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsOffering);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offerings")
	public ResponseEntity<Integer> deleteAllOfferings(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(OfferingDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offerings/{offering-cd}")
	public ResponseEntity<Integer> deleteOffering(
			@PathVariable(name = "offering-cd") @Parameter(description = "FBS Offering Code") String offeringCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		OfferingDim offering = new OfferingDim(offeringCd);
		return edsDaoService.delete(OfferingDim.class, offering);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offerings/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOfferings(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offerings.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offerings/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOfferings(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offerings.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * OFFERING_COMPONENT - offering-components
	 */

	/**
	 * 
	 * @param <T>                 - Object type being retrieved/returned.
	 * @param offeringComponentCd - offering-component-cd PathVariable. Note that
	 *                            the path variable name must match the table column
	 *                            name.
	 * @param filters             - filter in format of a SQL WHERE clause.
	 *                            Default="".
	 * @param orderByCols         - Order by columns. Default="" (orders by natural
	 *                            key).
	 * @param includeParentage    - Return parent taxonomy of object. Omit for root
	 *                            of taxonomy. Default="".
	 * @param returnCsv           - API to return results as CSV. Default=false.
	 * @param includePit          - API to include Point in Time (PIT) data.
	 *                            Default=false.
	 * @param pit                 - Specific Point in Time (format:
	 *                            yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                            2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                            state).
	 * @param resultSetMaxSize    - Size of the result to be returned, use ‘ALL’ to
	 *                            get all data. Default="All".
	 * @param request             - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/eds-ut-jrs-tax/offering-components",
			"/eds-ut-jrs-tax/offering-components/{offering-component-cd}" }, produces = { "application/json",
					"application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingComponentByCode(
			@PathVariable(name = "offering-component-cd", required = false) String offeringComponentCd,
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
		if (offeringComponentCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("offering-component-cd", offeringComponentCd);
		}

		return edsDaoService.find(OfferingComponentDim.class, pathVarMap, filters, orderByCols, includeParentage, null,
				returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@GetMapping(path = {
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios/{offering-portfolio-cd}/offerings/{offering-cd}/offering-components",
			"/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}/service-lines/{service-line-cd}/offering-portfolios/{offering-portfolio-cd}/offerings/{offering-cd}/offering-components/{offering-component-cd}" }, produces = {
					"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllOfferingByBrandGpSlOpOfferingCode(
			@PathVariable(name = "offering-component-cd", required = false) String offeringComponentCd,
			@PathVariable(name = "brand-cd") String brandCd,
			@PathVariable(name = "growth-platform-cd") String growthPlatformCd,
			@PathVariable(name = "service-line-cd") String serviceLineCd,
			@PathVariable(name = "offering-portfolio-cd") String offeringPortfolioCd,
			@PathVariable(name = "offering-cd", required = false) String offeringCd,
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
		pathVarMap.put("offering-portfolio-cd", offeringPortfolioCd);
		pathVarMap.put("offering-cd", offeringCd);
		if (offeringComponentCd != null) {
			pathVarMap.put("offering-component-cd", offeringComponentCd);
		}

		return edsDaoService.find(OfferingComponentDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv,
				includePit, pit, edsOnly, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/offering-components", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertOfferingComponent(
			@RequestBody OfferingComponentDim fbsOfferingComponent)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsOfferingComponent);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offering-components")
	public ResponseEntity<Integer> deleteAllOfferingComponents(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(OfferingComponentDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/offering-components/{offering-component-cd}")
	public ResponseEntity<Integer> deleteOfferingComponent(
			@PathVariable(name = "offering-component-cd") @Parameter(description = "FBS OfferingComponent Code") String offeringComponentCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		OfferingComponentDim offeringComponent = new OfferingComponentDim(offeringComponentCd);
		return edsDaoService.delete(OfferingComponentDim.class, offeringComponent);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offering-components/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlOfferingComponents(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offering-components.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingComponentDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/offering-components/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlOfferingComponents(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "offering-components.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(OfferingComponentDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * JOB_ROLE_DESCRIPTION - job-role-descriptions
	 */

	/**
	 * 
	 * @param <T>              - Object type being retrieved/returned.
	 * @param jobRoleCd        - job-role-cd PathVariable. Note that the path
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
	@GetMapping(path = { "/eds-ut-jrs-tax/job-role-descriptions",
			"/eds-ut-jrs-tax/job-role-descriptions/{job-role-cd}" }, produces = { "application/json",
					"application/xml" })
	public <T> ResponseEntity<Object> retrieveAllJobRoleDescriptionByCode(
			@PathVariable(name = "job-role-cd", required = false) String jobRoleCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			// , @RequestParam(required=false, defaultValue="false") @Parameter(description =
			// "Include parent nodes?") boolean includeParentage
			,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (jobRoleCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("job-role-cd", jobRoleCd);
		}

		return edsDaoService.find(JobRoleDescriptionDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv,
				includePit, pit, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/job-role-descriptions", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertJobRoleDescription(
			@RequestBody JobRoleDescriptionDim fbsJobRoleDescription)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsJobRoleDescription);
	}

	@DeleteMapping("/eds-ut-jrs-tax/job-role-descriptions")
	public ResponseEntity<Integer> deleteAllJobRoleDescriptions(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(JobRoleDescriptionDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/job-role-descriptions/{job-role-cd}")
	public ResponseEntity<Integer> deleteJobRoleDescription(
			@PathVariable(name = "job-role-cd") @Parameter(description = "FBS JobRoleDescription Code") String jobRoleCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		JobRoleDescriptionDim jobRoleDescription = new JobRoleDescriptionDim(jobRoleCd);
		return edsDaoService.delete(JobRoleDescriptionDim.class, jobRoleDescription);
	}

	@PostMapping(value="/eds-ut-jrs-tax/job-role-descriptions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJobRoleDescriptions(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "job-role-descriptions.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JobRoleDescriptionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/job-role-descriptions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJobRoleDescriptions(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "job-role-descriptions.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JobRoleDescriptionDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * JRS_DESCRIPTION - jrs-descriptions
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
	@GetMapping(path = { "/eds-ut-jrs-tax/jrs-descriptions", "/eds-ut-jrs-tax/jrs-descriptions/{jrs-cd}" }, produces = {
			"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllJrsDescriptionByCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (jrsCd != null) {
			pathVarMap = new HashMap<>();
			if (Character.isDigit(jrsCd.charAt(0)))
				pathVarMap.put("jrs-cd", jrsCd);
			else
				pathVarMap.put("jrs-nm", jrsCd);
		}

		return edsDaoService.find(JrsDescriptionDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv,
				includePit, pit, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/jrs-descriptions", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertJrsDescription(@RequestBody JrsDescriptionDim fbsJrsDescription)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsJrsDescription);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrs-descriptions")
	public ResponseEntity<Integer> deleteAllJrsDescriptions(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(JrsDescriptionDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrs-descriptions/{jrs-cd}")
	public ResponseEntity<Integer> deleteJrsDescription(
			@PathVariable(name = "jrs-cd") @Parameter(description = "FBS JrsDescription Code") String jrsCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsDescriptionDim jrsDescription = new JrsDescriptionDim(jrsCd);
		return edsDaoService.delete(JrsDescriptionDim.class, jrsDescription);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrs-descriptions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrsDescriptions(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrs-descriptions.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsDescriptionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrs-descriptions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrsDescriptions(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrs-descriptions.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsDescriptionDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	/**
	 * JRS_COMPENSATION_GRADE - jrs-compensation-grades
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
	@GetMapping(path = { "/eds-ut-jrs-tax/jrs-compensation-grades",
			"/eds-ut-jrs-tax/jrs-compensation-grades/{jrs-cd}" }, produces = { "application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveAllJrsCompensationGradeByCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (jrsCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}

		return edsDaoService.find(JrsCompensationGradeDim.class, pathVarMap, filters, orderByCols, false, null,
				returnCsv, includePit, pit, resultSetMaxSize, request);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/jrs-compensation-grades", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertJrsCompensationGrade(
			@RequestBody JrsCompensationGradeDim fbsJrsCompensationGrade)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsJrsCompensationGrade);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrs-compensation-grades")
	public ResponseEntity<Integer> deleteAllJrsCompensationGrades(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(JrsCompensationGradeDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/jrs-compensation-grades/{jrs-cd}/{compensation-grade-cd}")
	public ResponseEntity<Integer> deleteJrsCompensationGrade(
			@PathVariable(name = "jrs-cd") @Parameter(description = "JR/S Code") String jrsCd,
			@PathVariable(name = "compensation-grade-cd") @Parameter(description = "Compensation Grade Code") String cmpnstnGrdCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsCompensationGradeDim jrsCompensationGrade = new JrsCompensationGradeDim(jrsCd, cmpnstnGrdCd);
		return edsDaoService.delete(JrsCompensationGradeDim.class, jrsCompensationGrade);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrs-compensation-grades/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrsCompensationGrades(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrs-compensation-grades.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsCompensationGradeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrs-compensation-grades/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrsCompensationGrades(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "jrs-compensation-grades.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(JrsCompensationGradeDim.class, oldFile, newFile, keyLength, outputFileName);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/jrs-compensation-grades-list",
			"/eds-ut-jrs-tax/jrs-compensation-grades-list/{jrs-cd}" }, produces = { "application/json",
					"application/xml" })
	public <T> ResponseEntity<Object> retrieveJrsCompensationGradeList(@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		edsDaoService.setT(JrsCompensationGradeDim.class);
		edsDaoService.setTableNm("REFT.JRS_COMPENSATION_GRADE_DIM_V");

		String sql = "SELECT JRS_CD, LISTAGG(CMPNSTN_GRD_CD, ', ' ) WITHIN GROUP (ORDER BY CMPNSTN_GRD_CD) CMPNSTN_GRD_CD FROM REFT.JRS_COMPENSATION_GRADE_DIM_V";
		if (jrsCd != null) {
			sql += (" WHERE JRS_CD = '" + jrsCd + "'");
		}
		sql += " GROUP BY JRS_CD";
		//return edsDaoService.getListForSql(JrsCompensationGradeDim.class, edsDaoService.getConnection(), sql);
		return edsDaoService.getListForSql(SpecialtyDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
	}

	/**
	 * ENHANCED_EXPERTISE_TAXONOMY - enhanced-expertise-taxonomies
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
	@GetMapping(path = { "/eds-ut-jrs-tax/enhanced-expertise-taxonomies",
			"/eds-ut-jrs-tax/enhanced-expertise-taxonomies/{jrs-cd}" }, produces = { "application/json",
					"application/xml" })
	public synchronized <T> ResponseEntity<Object> retrieveAllEnhancedExpertiseTaxonomyByCode(
			@PathVariable(name = "jrs-cd", required = false) String jrsCd,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "-1") @Parameter(description = "Size of the offset.") int offset,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {

		Map<String, Object> pathVarMap = null;
		if (jrsCd != null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}

		return edsDaoService.find(EnhancedExpertiseTaxonomyDim.class, pathVarMap, filters, orderByCols, false, null,
				returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-ut-jrs-tax/enhanced-expertise-taxonomies/count"},produces = { "application/json", "application/xml"})
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
		return edsDaoService.countAll(EnhancedExpertiseTaxonomyDim.class, pathVarMap, filters, includePit, null, false);
	}

	// @MatrixVariable
	@GetMapping(path = { "/eds-ut-jrs-tax/enhanced-expertise-taxonomies/q/{q-values}" }, produces = {
			"application/json", "application/xml" })
	//@RequestMapping(value = "/eds-ut-jrs-tax/enhanced-expertise-taxonomies/q/{q-values}", method = RequestMethod.GET)
	public <T> ResponseEntity<Object> retrieveAllEnhancedExpertiseTaxonomyQuery(
			@MatrixVariable(pathVar = "q-values", required = true) Map<String, Object> pathVarMap,
			//@PathVariable(name = "q-values", required = true) String pathVarMapString,
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters,
			@RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Include Point in Time data?") boolean includePit,
			@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request) throws SQLException, ClassNotFoundException, IOException {


		return edsDaoService.find(EnhancedExpertiseTaxonomyDim.class, pathVarMap, filters, orderByCols, false, null,
				returnCsv, includePit, pit, resultSetMaxSize, request);

	}

	@GetMapping(path = { "/eds-ut-jrs-tax/enhanced-expertise-taxonomies/aaa/{employee}" }, produces = {
			"application/json", "application/xml" })
	@ResponseBody
	public ResponseEntity<Map<String, String>> getEmployeeData(
			@MatrixVariable(pathVar = "employee") Map<String, String> matrixVars) {
		return new ResponseEntity<>(matrixVars, HttpStatus.OK);
	}

	@GetMapping(value = "/pets/{petId}")
	public void findPet(@PathVariable String petId, @MatrixVariable int q) {

		// petId == 42
		// q == 11

	}

	@PostMapping(value = "/eds-ut-jrs-tax/enhanced-expertise-taxonomies", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> insertEnhancedExpertiseTaxonomy(
			@RequestBody EnhancedExpertiseTaxonomyDim fbsEnhancedExpertiseTaxonomy)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsEnhancedExpertiseTaxonomy);
	}

	@DeleteMapping("/eds-ut-jrs-tax/enhanced-expertise-taxonomies")
	public ResponseEntity<Integer> deleteAllEnhancedExpertiseTaxonomys(
			@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(EnhancedExpertiseTaxonomyDim.class, filters);
	}

	@DeleteMapping("/eds-ut-jrs-tax/enhanced-expertise-taxonomies/{jrs-cd}")
	public ResponseEntity<Integer> deleteEnhancedExpertiseTaxonomy(
			@PathVariable(name = "jrs-cd") @Parameter(description = "FBS EnhancedExpertiseTaxonomy Code") String jrsCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		EnhancedExpertiseTaxonomyDim enhancedExpertiseTaxonomy = new EnhancedExpertiseTaxonomyDim(jrsCd);
		return edsDaoService.delete(EnhancedExpertiseTaxonomyDim.class, enhancedExpertiseTaxonomy);
	}

	@PostMapping(value="/eds-ut-jrs-tax/enhanced-expertise-taxonomies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlEnhancedExpertiseTaxonomys(@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "enhanced-expertise-taxonomies.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(EnhancedExpertiseTaxonomyDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/enhanced-expertise-taxonomies/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlEnhancedExpertiseTaxonomys(@RequestParam("old-file") MultipartFile oldFile,
			@RequestParam("new-file") MultipartFile newFile,
			@RequestParam(name = "key-length", defaultValue = "1") int keyLength,
			@RequestParam(name = "output-file-name", defaultValue = "enhanced-expertise-taxonomies.csv") String outputFileName)
			throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException,
			SQLException {
		return edsDaoService.etl(EnhancedExpertiseTaxonomyDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path={"/eds-ut-jrs-tax/jrs-skill-assocs","/eds-ut-jrs-tax/jrs-skill-assocs/{jrs-cd}/{skill-id}"},produces = { "application/json", "application/xml"})
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
		
		return edsDaoService.find(JrsSkillAssocDim.class
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
	
	@GetMapping(path={"/eds-ut-jrs-tax/jrs-skill-assocs/{jrs-cd}/skills"},produces = { "application/json", "application/xml"})
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
		
		return edsDaoService.find(JrsSkillAssocDim.class
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
	
	
	@PostMapping(value="/eds-ut-jrs-tax/jrs-skill-assocs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertJrsSkillAssoc(@RequestBody JrsSkillAssocDim fbsJrsSkillAssoc) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsJrsSkillAssoc);
	}
	
	@DeleteMapping("/eds-ut-jrs-tax/jrs-skill-assocs")
	public  ResponseEntity<Integer> deleteAllJrsSkillAssocs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(JrsSkillAssocDim.class, filters);
	}
	
	@DeleteMapping("/eds-ut-jrs-tax/jrs-skill-assocs/{jrs-cd}/{skill-id}")
	public ResponseEntity<Integer> deleteJrsSkillAssoc(@PathVariable(name="jrs-cd") @Parameter(description = "Jrs Code") String jrsCd
			                                         , @PathVariable(name="skill-id") @Parameter(description = "Skill Id") String skillId
			) throws IllegalArgumentException, IllegalAccessException, SQLException {
		JrsSkillAssocDim jrsSkillAssoc = new JrsSkillAssocDim(jrsCd, Integer.valueOf(skillId));
		return edsDaoService.delete(JrsSkillAssocDim.class, jrsSkillAssoc);
	}	
	
	@PostMapping(value="/eds-ut-jrs-tax/jrs-skill-assocs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlJrsSkillAssocs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-skill-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(JrsSkillAssocDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/jrs-skill-assocs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlJrsSkillAssocs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="jrs-skill-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(JrsSkillAssocDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path={"/eds-ut-jrs-tax/skills","/eds-ut-jrs-tax/skills/{skill-id}"},produces = { "application/json", "application/xml"})
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
		
		return edsDaoService.find(SkillDim.class
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
	
	
	@PostMapping(value="/eds-ut-jrs-tax/skills", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSkill(@RequestBody SkillDim fbsSkill) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsSkill);
	}
	
	@DeleteMapping("/eds-ut-jrs-tax/skills")
	public  ResponseEntity<Integer> deleteAllSkills(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(SkillDim.class, filters);
	}
	
	@DeleteMapping("/eds-ut-jrs-tax/skills/{skill-id}")
	public ResponseEntity<Integer> deleteSkill(@PathVariable(name="skill-id") @Parameter(description = "FBS Skill Code") String skillId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SkillDim skill = new SkillDim(Integer.valueOf(skillId));
		return edsDaoService.delete(SkillDim.class, skill);
	}	
	
	@PostMapping(value="/eds-ut-jrs-tax/skills/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSkills(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(SkillDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-ut-jrs-tax/skills/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSkills(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(SkillDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	@GetMapping(path = "/eds-ut-jrs-tax/jrss/{jrs-cd}/skills", produces = {
			"application/json", "application/xml" })
	public <T> ResponseEntity<Object> retrieveSkillsForJrs(@PathVariable(name = "jrs-cd") String jrsCd,
			@RequestParam(required = false, defaultValue = "false") @Parameter(description = "Return results as CSV") boolean returnCsv,
			//@RequestParam(required = false, defaultValue = "") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit,
			@RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize,
			HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		
		edsDaoService.setT(SkillDim.class);

		String sql = "SELECT S.SKILL_SK_ID, S.SKILL_ID, S.SKILL_NM, S.SKILL_DESC FROM REFT.SKILL_DIM_V S INNER JOIN REFT.JRS_SKILL_ASSOC_DIM_V A ON S.SKILL_ID=A.SKILL_ID WHERE A.JRS_CD='"+jrsCd+"' ORDER BY S.SKILL_ID";

		return edsDaoService.getListForSql(SkillDim.class, edsDaoService.getConnection(), sql, returnCsv, request);
	}
	
	/*
	 * SKILLS JRS
	 */
	@GetMapping(path={"/eds-ut-jrs-tax/jrs-skills","/eds-ut-jrs-tax/jrs-skills/{jrs-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllJrsSkill(
			  @PathVariable(name="jrs-cd", required=false) String jrsCd
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
		
		return edsDaoService.find(SkillJrsDim.class
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

}