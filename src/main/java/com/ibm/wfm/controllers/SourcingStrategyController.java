package com.ibm.wfm.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.SkillsValueFrameworkDim;
import com.ibm.wfm.beans.SourcingStrategyCountryDim;
import com.ibm.wfm.beans.SourcingStrategyDefinitionDim;
import com.ibm.wfm.beans.SourcingStrategyDim;
import com.ibm.wfm.beans.SourcingStrategyValidationBean;
import com.ibm.wfm.beans.sdss.SdssExtractMetadata;
import com.ibm.wfm.beans.sdss.SdssReferenceData;
import com.ibm.wfm.beans.sdss.SdssStagingDim;
import com.ibm.wfm.beans.sdss.SdssValidationExcelRenderer;
import com.ibm.wfm.beans.sdss.SdssValidationResponse;
import com.ibm.wfm.beans.sdss.SdssValidationZipRenderer;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.CsvSortService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.LadderComparatorService;
import com.ibm.wfm.services.SdssReferenceDataService;
import com.ibm.wfm.services.SdssValidationService;
import com.ibm.wfm.services.SourcingStrategyDaoService;
import com.ibm.wfm.services.SourcingStrategyValidationService;
import com.ibm.wfm.utils.ExcelReader;
import com.ibm.wfm.utils.Helpers;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class SourcingStrategyController {
	@Autowired
	private SourcingStrategyDaoService sourcingStrategyDaoService;
	
	private FileStorageService fileStorageService;
	private FileStorageProperties fileStorageProperties;
	private final SdssReferenceDataService sdssReferenceDataService;
	
    public SourcingStrategyController(
            SdssReferenceDataService sdssReferenceDataService,
            FileStorageService fileStorageService,
            FileStorageProperties fileStorageProperties) {

        this.sdssReferenceDataService =
            sdssReferenceDataService;

        this.fileStorageService =
            fileStorageService;

        this.fileStorageProperties =
            fileStorageProperties;
    }
	
	/**
	 * SOURCING_STRATEGY - sourcing-strategies
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param srcStrgyCd - src-strgy-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-wfm/sourcing-strategies","/eds-wfm/sourcing-strategies/{src-strgy-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyByCode(
			  @PathVariable(name="src-strgy-cd", required=false) String srcStrgyCd
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
		if (srcStrgyCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("src-strgy-cd", srcStrgyCd);
		}
		
		return sourcingStrategyDaoService.find(SourcingStrategyDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/sourcing-strategies", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSourcingStrategy(@RequestBody SourcingStrategyDim fbsSourcingStrategy) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sourcingStrategyDaoService.insert(fbsSourcingStrategy);
	}
	
	@DeleteMapping("/eds-wfm/sourcing-strategies")
	public  ResponseEntity<Integer> deleteAllSourcingStrategys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.delete(SourcingStrategyDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/sourcing-strategies/{src-strgy-cd}")
	public ResponseEntity<Integer> deleteSourcingStrategy(@PathVariable(name="src-strgy-cd") @Parameter(description = "FBS SourcingStrategy Code") String srcStrgyCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SourcingStrategyDim sourcingStrategy = new SourcingStrategyDim(srcStrgyCd);
		return sourcingStrategyDaoService.delete(SourcingStrategyDim.class, sourcingStrategy);
	}	
	
	@PostMapping(value="/eds-wfm/sourcing-strategies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSourcingStrategys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/sourcing-strategies/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSourcingStrategys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * SOURCING_STRATEGY_COUNTRY - sourcing-strategy-countries
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
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllFilterSourcingStrategyMrktCode(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(required=false) @Parameter(description = "Geography Type Code") String geoTypCd
			, @RequestParam(required=false) @Parameter(description = "Geography Code") String geoCd
			, @RequestParam(required=false) @Parameter(description = "Market Code") String mrktCd
			, @RequestParam(required=false) @Parameter(description = "Market Region Code") String mrktRgnCd
			, @RequestParam(required=false) @Parameter(description = "Country Code (ISO or legacy financial)") String ctryCd
			, @RequestParam(required=false) @Parameter(description = "Band Category Code") String bandCatCd
			, @RequestParam(required=false) @Parameter(description = "JRS Code") String jrsCd
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (geoTypCd!=null || geoCd!=null || mrktCd!=null || mrktRgnCd!=null || ctryCd!=null || bandCatCd!=null || jrsCd!=null) {
			pathVarMap = new HashMap<>();
			
			if (geoTypCd!=null) pathVarMap.put("geo-typ-cd", geoTypCd.toUpperCase());
			if (geoCd!=null) pathVarMap.put("geo-cd", geoCd.toUpperCase());
			if (mrktCd!=null) pathVarMap.put("mrkt-cd", mrktCd.toUpperCase());
			if (mrktRgnCd!=null) pathVarMap.put("mrkt-rgn-cd", mrktRgnCd.toUpperCase());
			if (ctryCd!=null) { 
				if (ctryCd.length()>2)
					pathVarMap.put("ctry-cd", ctryCd.toUpperCase());
				else
					pathVarMap.put("ctry-iso-cd", ctryCd);
			}
			if (bandCatCd!=null) pathVarMap.put("band-cat-nm", Helpers.toBandCategoryName(bandCatCd));
			
			if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);
		}
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries/{geo-typ-cd}/{geo-cd}/{mrkt-cd}/{mrkt-rgn-cd}/{ctry-cd}/{brand-cat-cd}/{jrs-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveFullSourcingStrategyCode(
			  @PathVariable(name="geo-typ-cd", required=true) String geoTypCd
			, @PathVariable(name="geo-cd", required=true) String geoCd
			, @PathVariable(name="mrkt-cd", required=true) String mrktCd
			, @PathVariable(name="mrkt-rgn-cd", required=true) String mrktRgnCd
			, @PathVariable(name="ctry-cd", required=true) String ctryCd
			, @PathVariable(name="brand-cat-cd", required=true) String bandCatCd
			, @PathVariable(name="jrs-cd", required=true) String jrsCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();

		pathVarMap.put("geo-typ-cd", geoTypCd.toUpperCase());
		pathVarMap.put("geo-cd", geoCd.toUpperCase());
		pathVarMap.put("mrkt-cd", mrktCd.toUpperCase());
		pathVarMap.put("mrkt-rgn-cd", mrktRgnCd.toUpperCase());
		if (ctryCd!=null) { 
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd.toUpperCase());
			else
				pathVarMap.put("ctry-iso-cd", ctryCd);
		}
		if (bandCatCd!=null) pathVarMap.put("band-cat-nm", Helpers.toBandCategoryName(bandCatCd));
		if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);
		
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries/{geo-typ-cd}/{geo-cd}/{mrkt-cd}/{mrkt-rgn-cd}/{ctry-cd}/{jrs-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyCode(
			  @PathVariable(name="geo-typ-cd", required=true) String geoTypCd
			, @PathVariable(name="geo-cd", required=true) String geoCd
			, @PathVariable(name="mrkt-cd", required=true) String mrktCd
			, @PathVariable(name="mrkt-rgn-cd", required=true) String mrktRgnCd
			, @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @PathVariable(name="jrs-cd", required=false) String jrsCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();

		pathVarMap.put("geo-typ-cd", geoTypCd.toUpperCase());
		pathVarMap.put("geo-cd", geoCd.toUpperCase());
		pathVarMap.put("mrkt-cd", mrktCd.toUpperCase());
		pathVarMap.put("mrkt-rgn-cd", mrktRgnCd.toUpperCase());
		if (ctryCd!=null) { 
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd.toUpperCase());
			else
				pathVarMap.put("ctry-iso-cd", ctryCd);
		}
		if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);
		
		
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries/{mrkt-cd}/{ctry-cd}/{jrs-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyMrktCode(
			  @PathVariable(name="mrkt-cd", required=true) String mrktCd
			, @PathVariable(name="ctry-cd", required=true) String ctryCd
			, @PathVariable(name="jrs-cd", required=true) String jrsCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (mrktCd!=null || ctryCd!=null || jrsCd!=null) {
			pathVarMap = new HashMap<>();

			if (mrktCd!=null) pathVarMap.put("mrkt-cd", mrktCd);
			if (ctryCd!=null) { 
				if (ctryCd.length()>2)
					pathVarMap.put("ctry-cd", ctryCd);
				else
					pathVarMap.put("ctry-iso-cd", ctryCd);
			}
			if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);
			
			//sp: 2025-03-21 - default the geo-typ-cd to DOM so MySA will continue working until they can cut over to explicitly specifying geo-typ-cd
			pathVarMap.put("geo-typ-cd", "DOM");
		}
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries/{ctry-cd}/{jrs-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyCountryByCode(
			  @PathVariable(name="ctry-cd", required=true) String ctryCd
			, @PathVariable(name="jrs-cd", required=false) String jrsCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;

		pathVarMap = new HashMap<>();

		if (ctryCd.length()>2)
			pathVarMap.put("ctry-cd", ctryCd);
		else
			pathVarMap.put("ctry-iso-cd", ctryCd);
		
		if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);

		
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy-countries/{geo-typ-cd}/{mrkt-cd}/{ctry-cd}/{jrs-cd}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyCountryByGeoTypeNCtryCode(
			  @PathVariable(name="geo-typ-cd", required=true) String geoTypCd
			, @PathVariable(name="mrkt-cd", required=true) String mrktCd  
			, @PathVariable(name="ctry-cd", required=true) String ctryCd
			, @PathVariable(name="jrs-cd", required=true) String jrsCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;

		pathVarMap = new HashMap<>();

		if (geoTypCd!=null) pathVarMap.put("geo-typ-cd", geoTypCd);
		if (mrktCd!=null) pathVarMap.put("mrkt-cd", mrktCd);
		
		if (ctryCd.length()>2)
			pathVarMap.put("ctry-cd", ctryCd);
		else
			pathVarMap.put("ctry-iso-cd", ctryCd);
		
		if (jrsCd!=null) pathVarMap.put("jrs-cd", jrsCd);

		
		return sourcingStrategyDaoService.find(SourcingStrategyCountryDim.class, pathVarMap, filters, orderByCols, false, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-wfm/sourcing-strategy-countries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSourcingStrategyCountry(@RequestBody SourcingStrategyCountryDim sourcingStrategyCountry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sourcingStrategyDaoService.insert(sourcingStrategyCountry);
	}
	
	@DeleteMapping("/eds-wfm/sourcing-strategy-countries")
	public  ResponseEntity<Integer> deleteAllSourcingStrategyCountrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.delete(SourcingStrategyCountryDim.class, filters);
	}
	
	@DeleteMapping("/eds-wfm/sourcing-strategy-countries/{geo-type-cd}/{geo-cd}/{mrkt-cd}/{mrkt-rgn-cd}/{ctry-cd}/{band-cat-nm}/{jrs-cd}")
	public ResponseEntity<Integer> deleteSourcingStrategyCountry(
			  @PathVariable(name="geo-type-cd") @Parameter(description = "Geography Type Code") String geoTypeCd
			, @PathVariable(name="geo-cd") @Parameter(description = "Geography Code") String geoCd
			, @PathVariable(name="mrkt-cd") @Parameter(description = "Market Code") String mrktCd
			, @PathVariable(name="mrkt-rgn-cd") @Parameter(description = "Market Region Code") String mrktRgnCd
			, @PathVariable(name="ctry-cd") @Parameter(description = "Country Code") String ctryCd
			, @PathVariable(name="band-cat-nm") @Parameter(description = "Band Category Name") String bandCatNm
			, @PathVariable(name="jrs-cd") @Parameter(description = "JRS Code") String jrsCd
			) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SourcingStrategyCountryDim sourcingStrategyCountry = new SourcingStrategyCountryDim(geoTypeCd,geoCd,mrktCd, mrktRgnCd,ctryCd, bandCatNm, jrsCd);
		return sourcingStrategyDaoService.delete(SourcingStrategyCountryDim.class, sourcingStrategyCountry);
	}	
	
	@PostMapping(value="/eds-wfm/sourcing-strategy-countries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSourcingStrategyCountrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategy-countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyCountryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-wfm/sourcing-strategy-countries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSourcingStrategyCountrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategy-countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyCountryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	
	@GetMapping(path={"/eds-wfm/sourcing-strategy/{sourcing-strategy-cd}/validate","/eds/sourcing-strategy/{sourcing-strategy-cd}/validate"}
			,produces = MediaType.APPLICATION_JSON_VALUE)
	public SourcingStrategyValidationBean validateSourcingStategy(
			    @PathVariable(required=true,name="sourcing-strategy-cd") @Parameter(description = "Sourcing Strategy Code") String sourcingStrategyCd
			  , @RequestParam(required=false, name="role-seat-type-cd",defaultValue="")  @Parameter(description = "Role/Seat Type Code") String roleSeatTypeCd
			  , @RequestParam(required=false, name="pref-resource-channel",defaultValue="")  @Parameter(description = "Prefered Resource Channel") String prefResourceChannel
			  , @RequestParam(required=false, name="gic-provider-country",defaultValue="")  @Parameter(description = "GIC Provider Country Code") String gicProviderCountry
			  , @RequestParam(required=false, name="contract-owning-country",defaultValue="")  @Parameter(description = "Contract Owning Country Code") String contractOwningCountry
			  , @RequestParam(required=false, name="contract-owning-market",defaultValue="")  @Parameter(description = "Contract Owning Market Code") String contractOwningMarket
			  , @RequestParam(required=false, name="req-cntrct-accept-gbl-delivery",defaultValue="")  @Parameter(description = "Request contractor / Accept Global Contractors (GIC)") String reqCntrctAcceptGblDelivery
			  , @RequestParam(required=false, name="band-high",defaultValue="")  @Parameter(description = "Request Band High") String bandHigh
			  , @RequestParam(required=false, name="band-low",defaultValue="")  @Parameter(description = "Request Band Low") String bandLow
			) throws SQLException, ClassNotFoundException {
		
		/* Put parameter values in Map, keyed on parameter name */
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("role-seat-type-cd",roleSeatTypeCd.trim().length()==0?null:roleSeatTypeCd.trim());
		parameterMap.put("pref-resource-channel",prefResourceChannel.trim().length()==0?null:prefResourceChannel);
		parameterMap.put("gic-provider-country",gicProviderCountry.trim().length()==0?null:gicProviderCountry);
		parameterMap.put("contract-owning-country",contractOwningCountry.trim().length()==0?null:contractOwningCountry);
		parameterMap.put("contract-owning-market",contractOwningMarket.trim().length()==0?null:contractOwningMarket);
		parameterMap.put("req-cntrct-accept-gbl-delivery",reqCntrctAcceptGblDelivery.trim().length()==0?null:reqCntrctAcceptGblDelivery);
		parameterMap.put("band-high",bandHigh.trim().length()==0?null:bandHigh);
		parameterMap.put("band-low",bandLow.trim().length()==0?null:bandLow);
		//parameterMap.put("sourcing-strategy",sourcingStrategyCd);
		
		/* Add the expressions for Affiliates */
		Map<String, String> expressionMap = new HashMap<String, String>();
		expressionMap.put("role-seat-type-cd", "trim() eq 'Geo'");
		expressionMap.put("pref-resource-channel", "trim() eq 'Affiliate'");
		
		SourcingStrategyValidationService ssvs = SourcingStrategyValidationService.getInstance();
		return ssvs.validate(sourcingStrategyCd, parameterMap, expressionMap);
	}
	
	/**
	 * SOURCING_STRATEGY_DEFINITION - sourcing-strategy-definitions
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
	@GetMapping(path={"/sourcing-strategy/sourcing-strategy-definitions"
			,"/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}/{geo-cd}/{mrkt-cd}/{rgn-cd}/{ctry-cd}"
			,"/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}/{geo-cd}/{mrkt-cd}/{rgn-cd}"
			,"/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}/{geo-cd}/{mrkt-cd}"
			,"/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}/{geo-cd}"
			,"/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}"
			}
			,produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSourcingStrategyDefinitionByCode(
			  @PathVariable(name="jrs-cd", required=false) String jrsCd
			, @PathVariable(name="geo-cd", required=false) String geoCd
			, @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @PathVariable(name="rgn-cd", required=false) String rgnCd
			, @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Only retur EDS") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (jrsCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("jrs-cd", jrsCd);
		}
		if (geoCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		if (mrktCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		if (rgnCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("rgn-cd", rgnCd);
		}
		if (ctryCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("ctry-cd", ctryCd);
		}
		
		if (pathVarMap==null)
			return sourcingStrategyDaoService.find(SourcingStrategyDefinitionDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
		else
			return sourcingStrategyDaoService.findWithInheritence(
					SourcingStrategyDefinitionDim.class 
					, pathVarMap
					, filters
					, orderByCols
					, returnCsv
					, includePit
					, pit
					, edsOnly
					, resultSetMaxSize
					, request);
	}
	
	
	@PostMapping(value="/sourcing-strategy/sourcing-strategy-definitions", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSourcingStrategyDefinition(@RequestBody SourcingStrategyDefinitionDim fbsSourcingStrategyDefinition) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sourcingStrategyDaoService.insert(fbsSourcingStrategyDefinition);
	}
	
	@DeleteMapping("/sourcing-strategy/sourcing-strategy-definitions")
	public  ResponseEntity<Integer> deleteAllSourcingStrategyDefinitions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.delete(SourcingStrategyDefinitionDim.class, filters);
	}
	
	@DeleteMapping("/sourcing-strategy/sourcing-strategy-definitions/{jrs-cd}")
	public ResponseEntity<Integer> deleteSourcingStrategyDefinition(@PathVariable(name="jrs-cd") @Parameter(description = "Job Role Specialty Code") String jrsCd
			, @PathVariable(name="geo-cd") @Parameter(description = "Geography Code") String geoCd
			, @PathVariable(name="mrkt-cd") @Parameter(description = "Market Code") String mrktCd
			, @PathVariable(name="rgn-cd") @Parameter(description = "Market-Region Code") String rgnCd
			, @PathVariable(name="ctry-cd") @Parameter(description = "Country Code") String ctryCd
			 ) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SourcingStrategyDefinitionDim sourcingStrategyDefinition = new SourcingStrategyDefinitionDim(jrsCd, geoCd, mrktCd, rgnCd, ctryCd);
		return sourcingStrategyDaoService.delete(SourcingStrategyDefinitionDim.class, sourcingStrategyDefinition);
	}	
	
	@PostMapping(value="/sourcing-strategy/sourcing-strategy-definitions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSourcingStrategyDefinitions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategy-definitions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyDefinitionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sourcing-strategy/sourcing-strategy-definitions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSourcingStrategyDefinitions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sourcing-strategy-definitions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SourcingStrategyDefinitionDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * SKILLS_VALUE_FRAMEWORK - skills-value-frameworks
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param esvfId - esvf-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/sourcing-strategy/skills-value-frameworks","/sourcing-strategy/skills-value-frameworks/{esvf-id}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSkillsValueFrameworkByCode(
			  @PathVariable(name="esvf-id", required=false) String esvfId
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
		if (esvfId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("esvf-id", esvfId);
		}
		
		return sourcingStrategyDaoService.find(SkillsValueFrameworkDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/sourcing-strategy/skills-value-frameworks", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSkillsValueFramework(@RequestBody SkillsValueFrameworkDim fbsSkillsValueFramework) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sourcingStrategyDaoService.insert(fbsSkillsValueFramework);
	}
	
	@DeleteMapping("/sourcing-strategy/skills-value-frameworks")
	public  ResponseEntity<Integer> deleteAllSkillsValueFrameworks(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.delete(SkillsValueFrameworkDim.class, filters);
	}
	
	@DeleteMapping("/sourcing-strategy/skills-value-frameworks/{esvf-id}")
	public ResponseEntity<Integer> deleteSkillsValueFramework(
			@PathVariable(name = "esvf-id") @Parameter(description = "FBS SkillsValueFramework Code") String esvfId)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		SkillsValueFrameworkDim skillsValueFramework = new SkillsValueFrameworkDim(esvfId);
		return sourcingStrategyDaoService.delete(SkillsValueFrameworkDim.class, skillsValueFramework);
	}
	
	@PostMapping(value="/sourcing-strategy/skills-value-frameworks/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSkillsValueFrameworks(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills-value-frameworks.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SkillsValueFrameworkDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sourcing-strategy/skills-value-frameworks/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSkillsValueFrameworks(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="skills-value-frameworks.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SkillsValueFrameworkDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	@GetMapping(path={"/sourcing-strategy/skills-value-frameworks/count"},produces = MediaType.APPLICATION_JSON_VALUE)
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
		return sourcingStrategyDaoService.countAll(SkillsValueFrameworkDim.class, pathVarMap, filters, includePit, null, false);
		                                  
	}
	
	/**
	 * SDSS_STAGING - sdss-staging
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param esvfId - esvf-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/sourcing-strategy/sdss-staging","/sourcing-strategy/sdss-staging/{esvf-id}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSdssStagingByCode(
			  @PathVariable(name="esvf-id", required=false) String esvfId
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
		if (esvfId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("esvf-id", esvfId);
		}
		
		return sourcingStrategyDaoService.find(SdssStagingDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/sourcing-strategy/sdss-staging", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSdssStaging(@RequestBody SdssStagingDim fbsSdssStaging) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sourcingStrategyDaoService.insert(fbsSdssStaging);
	}
	
	@DeleteMapping("/sourcing-strategy/sdss-staging")
	public  ResponseEntity<Integer> deleteAllSdssStagings(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.delete(SdssStagingDim.class, filters);
	}
	
	@DeleteMapping("/sourcing-strategy/sdss-staging/{esvf-id}")
	public ResponseEntity<Integer> deleteSdssStaging(@PathVariable(name="esvf-id") @Parameter(description = "FBS SdssStaging Code") String esvfId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SdssStagingDim SdssStaging = new SdssStagingDim(esvfId);
		return sourcingStrategyDaoService.delete(SdssStagingDim.class, SdssStaging);
	}	
	
	@PostMapping(value="/sourcing-strategy/sdss-staging/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSdssStagings(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sdss-staging.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SdssStagingDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sourcing-strategy/sdss-staging/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSdssStagings(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sdss-staging.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sourcingStrategyDaoService.etl(SdssStagingDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	@GetMapping(path={"/sourcing-strategy/sdss-staging/count"},produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized ResponseEntity<Count> retrieveCountSdssStaging (
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

		return sourcingStrategyDaoService.countAll(SdssStagingDim.class, pathVarMap, filters, includePit, null, false);
		                                  
	}	
	
	@PostMapping(value = "/sourcing-strategy/sdss-staging/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadExcel(@RequestHeader("X-User-Id") String uploadUserid
	                                   , @RequestHeader("X-User-Type") String userType
									   , @RequestParam("file") MultipartFile file
			                           , @RequestParam(name = "tab-name", defaultValue = "Data", required = false) String tabName
			                           , @RequestParam(name = "validate-only", defaultValue = "false", required = false) boolean validateOnly
			                           , @RequestParam(
			                                  name = "return-valid-records",
			                                  defaultValue = "false"
			                              )
			                              boolean returnValidRecords
			                           , @RequestParam(name="exception-cd", required = false) String exceptionCd   
			                           , @RequestParam(name="exception-reason-txt", required = false) String exceptionReasonTxt 
			                           , HttpServletRequest request
			               			   , HttpServletResponse response
			                           ) throws Exception {
		
		// 1. parse/save workbook
		String excelFileName = fileStorageService.storeFile(file);
	
		// 2. Validate data 
		//    - Instantiate an SdssReferenceData object.  This object will pull the JRS and Geo taxonomy and the list of
		//      sourcing strategies using WFM-DMF APIs.
		//SdssReferenceData referenceData = new SdssReferenceData();
		SdssReferenceData referenceData =
			    new SdssReferenceData(sdssReferenceDataService);
		
		SdssValidationService validationService = new SdssValidationService(referenceData);
		
		SdssValidationResponse validationResponse = validationService.validateExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		
		boolean hasErrors = validationResponse.hasErrors();
		
		Path uploadedFile = Path.of(fileStorageProperties.getUploadDir()+"/"+excelFileName);
		
	    if (hasErrors && returnValidRecords) {
	    	Files.deleteIfExists(uploadedFile);
	        writeValidationZipResponse(
	        		response,
	                validationResponse,
	                uploadedFile
	        );

	        return null;
	    }

	    if (hasErrors) {
	    	Files.deleteIfExists(uploadedFile);
	        writeValidationErrorsResponse(
	        		response,
	                validationResponse,
	                uploadedFile
	        );

	        return null;
	    }

	    // Note: This has not been tested - need to run through a file with no errors.
	    if (validateOnly) {
	        Files.deleteIfExists(uploadedFile);

	        return ResponseEntity.ok(validationResponse);
	    }		
		
		
		//3. Read meta data tab
		SdssExtractMetadata metadata = ExcelReader.readSdssMetadataSheet(fileStorageProperties.getUploadDir()+"/"+excelFileName, "Extract Metadata");
		metadata.show();
		
		//4. validate metadata
	
		//5. regenerate old CSV using PIT query
		ResponseEntity<Object> oldCsvResponse =
		    sourcingStrategyDaoService.find(
                SdssStagingDim.class,
                null,                       // pathVarMap
                extractFiltersOnly(metadata.filterParam()),      // get filters
                "",                         // orderByCols
                false,                      // includeParentage
                null,                       // topNodeNm
                true,                       // returnCsv
                true,                       // includePit
                metadata.extractedAt(),      // pit
                -1,                         // offset
                "All",                      // resultSetMaxSize
                request
        );
		
		if (oldCsvResponse.getBody() == null) {
		    throw new IllegalStateException("No data returned from PIT query.");
		}

		
		//6. convert uploaded sheet to new CSV
		Path newCsv = convertExcelSheetToCsv(
		        Paths.get(fileStorageProperties.getUploadDir(), excelFileName),
		        tabName,
		        "sdss-new-"
		);
		
		//7. sort the old and new files.
        List<Integer> keyListInteger = Arrays.asList(
        		2,    // Geography Type
        		3,    // Geography
        		4,    // Market
        		5,    // Market Region
        		6,    // Country
        		7,    // Growth Platform
        		8,    // Service Line
        		9,    // Practice
        		11,   // JRS
        		12    // Band Category Name
        		);

        // Convert List<Integer> to int[]
        int[] keyList = keyListInteger.stream().mapToInt(Integer::intValue).toArray();
        
        CsvSortService.sortCsv(
				Paths.get(fileStorageProperties.getUploadDir(), "sdss-stagins.csv"),
		        Paths.get(fileStorageProperties.getUploadDir(), "sdss-old-sorted.csv"),
		        keyList
		);
        
        CsvSortService.sortCsv(
		        newCsv,
		        Paths.get(fileStorageProperties.getUploadDir(), "sdss-new-sorted.csv"),
		        keyList
		);
		
		//7-8. call existing comparator - call transactional apply method
		//TO-DO
        Path originalCsv = Paths.get(fileStorageProperties.getUploadDir(), "sdss-old-sorted.csv");
        Path updatedCsv = Paths.get(fileStorageProperties.getUploadDir(), "sdss-new-sorted.csv");
        Path deltaCsv = Paths.get(fileStorageProperties.getUploadDir(),"sdss-delta.csv");
        
        int[] columnList = {
        		10,    // JRS Name
        		13,    // Sourcing Strategy
        		14,    // Market Sourcing Strategy Addendum
        		15,    // Scarcity Index
        		16     // Anticipated Demand for Skills
        };

        boolean success = LadderComparatorService.processCsv(
        		originalCsv,
        		updatedCsv,
                deltaCsv,
                keyList,
                columnList
        );
        
        if (!success) {
        	System.out.println("##### Unable to generate delta file.");
        	throw new EtlException(
        		"Unable to generate delta file."
        	);
        }
        
		EtlResponse etlResponse = sourcingStrategyDaoService.deltaApply(SdssStagingDim.class
				                                                      , fileStorageProperties.getUploadDir()+"/sdss-delta.csv"
				                                                      , uploadUserid
				                                                      , exceptionCd
				                                                      , exceptionReasonTxt);
	
		return ResponseEntity.ok(etlResponse);
	}
	
	private String extractFiltersOnly(String filterParam) {

	    if (filterParam == null || filterParam.isBlank()) {
	        return "";
	    }

	    String cleaned = filterParam.trim();

	    if (cleaned.startsWith("?")) {
	        cleaned = cleaned.substring(1);
	    }

	    for (String pair : cleaned.split("&")) {
	        String[] parts = pair.split("=", 2);

	        if (parts.length == 2 && parts[0].equalsIgnoreCase("filters")) {
	            return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
	        }
	    }

	    return cleaned;
	}
	
	private Path convertExcelSheetToCsv(
	        Path excelFile,
	        String tabName,
	        String prefix
	) throws IOException {

	    //Path csvFile = Files.createTempFile(prefix, ".csv");
		Path csvFile = Paths.get(
			    fileStorageProperties.getUploadDir(),
			    "sdss-new.csv"
			);

	    DataFormatter formatter = new DataFormatter();

	    try (
	            Workbook workbook = WorkbookFactory.create(excelFile.toFile());
	            BufferedWriter writer = Files.newBufferedWriter(csvFile);
	            CSVWriter csvWriter = new CSVWriter(writer)
	    ) {
	        Sheet sheet = workbook.getSheet(tabName);

	        if (sheet == null) {
	            throw new IllegalArgumentException(
	                    "Workbook does not contain sheet named '" + tabName + "'"
	            );
	        }

	        for (Row row : sheet) {

	            int lastCellNum = row.getLastCellNum();

	            if (lastCellNum < 0) {
	                continue;
	            }

	            String[] values = new String[lastCellNum];
   
	            for (int i = 0; i < lastCellNum; i++) {
	                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	                values[i] = formatter.formatCellValue(cell);
	            }

	            csvWriter.writeNext(values);
	        }
	    }

	    return csvFile;
	}
	
	private void writeValidationErrorsResponse(
	        HttpServletResponse servletResponse,
	        SdssValidationResponse validationResponse,
	        Path uploadedFile
	) throws IOException {

	    servletResponse.setStatus(
	            HttpServletResponse.SC_BAD_REQUEST
	    );

	    servletResponse.setContentType(
	            "application/vnd.openxmlformats-officedocument"
	                    + ".spreadsheetml.sheet"
	    );

	    servletResponse.setHeader(
	            HttpHeaders.CONTENT_DISPOSITION,
	            "attachment; filename=\"sdss-validation-errors.xlsx\""
	    );

	    try {
	        SdssValidationExcelRenderer.write(
	                validationResponse,
	                servletResponse.getOutputStream()
	        );

	        servletResponse.flushBuffer();
	    } finally {
	        Files.deleteIfExists(uploadedFile);
	    }
	}
	
	private void writeValidationZipResponse(
	        HttpServletResponse servletResponse,
	        SdssValidationResponse validationResponse,
	        Path uploadedFile
	) throws IOException {

	    servletResponse.setStatus(
	            HttpServletResponse.SC_OK
	    );

	    servletResponse.setContentType(
	            "application/zip"
	    );

	    servletResponse.setHeader(
	            HttpHeaders.CONTENT_DISPOSITION,
	            "attachment; filename=\"sdss-validation-results.zip\""
	    );

	    servletResponse.setHeader(
	            "X-SDSS-Validation-Status",
	            "INVALID"
	    );

	    try {
	        SdssValidationZipRenderer.write(
	                validationResponse,
	                uploadedFile,
	                servletResponse.getOutputStream()
	        );

	        servletResponse.flushBuffer();
	    } finally {
	        Files.deleteIfExists(uploadedFile);
	    }
	}

}
