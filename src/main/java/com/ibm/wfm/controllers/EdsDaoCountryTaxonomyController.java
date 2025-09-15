package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
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

import com.ibm.wfm.beans.BmsCountry2Dim;
import com.ibm.wfm.beans.BmsCountryDim;
import com.ibm.wfm.beans.BmsGeographyDim;
import com.ibm.wfm.beans.BmsMarketDim;
import com.ibm.wfm.beans.BmsRegionDim;
import com.ibm.wfm.beans.CountryDeliveryOrgAssocDim;
import com.ibm.wfm.beans.DeliveryAreaDim;
import com.ibm.wfm.beans.DeliveryAreaGeographyAssocDim;
import com.ibm.wfm.beans.DeliveryAreaMarketDim;
import com.ibm.wfm.beans.DeliveryOrganizationDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoCountryTaxonomyController {
	
	@Autowired
	private EdsDaoService edsDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * Geographic Taxonomy
	 */
	/* Geography */
	@GetMapping(path={"/eds-country-tax/geographies","/eds-country-tax/geographies/{geo-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGeographies(
			  @PathVariable(name="geo-cd", required=false) String geoCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		//edsDaoService.setT(GeographyDim.class);
		
		Map<String, Object> pathVarMap = null; 
		if (geoCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		
		return edsDaoService.find(BmsGeographyDim.class, pathVarMap, filters, orderByCols, false, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	@PostMapping(value="/eds-country-tax/geographies", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeography(@RequestBody BmsGeographyDim geography) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(geography);
	}
	
	@DeleteMapping("/eds-country-tax/geographies")
	public  ResponseEntity<Integer> deleteAllGeographies(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BmsGeographyDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/geographies/{geo-cd}")
	public ResponseEntity<Integer> deleteGeography(@PathVariable(name="geo-cd") @Parameter(description = "Geography Code") String geoCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsGeographyDim geography = new BmsGeographyDim(geoCd);
		return edsDaoService.delete(BmsGeographyDim.class, geography);
	}
	
	@PostMapping(value="/eds-country-tax/geographies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographies(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-conferences.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsGeographyDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/geographiess/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographies(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="fbs-conferences.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsGeographyDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	
	/**
	 * MARKET
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

	@GetMapping(path={"/eds-country-tax/markets","/eds-country-tax/markets/{mrkt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllMarketByCode(
			  @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (mrktCd!=null) {
			pathVarMap =new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		
		return edsDaoService.find(BmsMarketDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	/**
	 * 
	 * @param mrktCd - mrkt-cd PathVariable. Note that the path variable name must match the table column name.
	 */
	@GetMapping(path={"/eds-country-tax/geographies/{geo-cd}/markets","/eds-country-tax/geographies/{geo-cd}/markets/{mrkt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllMarketByGeoMrktCode(
			  @PathVariable(name="geo-cd") String geoCd
			, @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("geo-cd", geoCd);
		if (mrktCd!=null) pathVarMap.put("mrkt-cd", mrktCd);
		
		return edsDaoService.find(BmsMarketDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/markets", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertMarket(@RequestBody BmsMarketDim fbsMarket) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsMarket);
	}
	
	@DeleteMapping("/eds-country-tax/markets")
	public  ResponseEntity<Integer> deleteAllMarkets(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BmsMarketDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/markets/{mrkt-cd}")
	public ResponseEntity<Integer> deleteMarket(@PathVariable(name="mrkt-cd") @Parameter(description = "FBS Market Code") String mrktCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsMarketDim market = new BmsMarketDim(mrktCd);
		return edsDaoService.delete(BmsMarketDim.class, market);
	}	
	
	@PostMapping(value="/eds-country-tax/markets/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlMarkets(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsMarketDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/markets/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlMarkets(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsMarketDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * BMS_REGION - regions
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
	@GetMapping(path={"/eds-country-tax/regions","/eds-country-tax/regions/{rgn-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllBmsRegionByCode(
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
		
		return edsDaoService.find(BmsRegionDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/regions", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBmsRegion(@RequestBody BmsRegionDim fbsBmsRegion) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsBmsRegion);
	}
	
	@DeleteMapping("/eds-country-tax/regions")
	public  ResponseEntity<Integer> deleteAllBmsRegions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BmsRegionDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/regions/{rgn-cd}")
	public ResponseEntity<Integer> deleteBmsRegion(@PathVariable(name="rgn-cd") @Parameter(description = "FBS BmsRegion Code") String rgnCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsRegionDim bmsRegion = new BmsRegionDim(rgnCd);
		return edsDaoService.delete(BmsRegionDim.class, bmsRegion);
	}	
	
	@PostMapping(value="/eds-country-tax/regions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBmsRegions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsRegionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/regions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBmsRegions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsRegionDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	
	/**
	 * COUNTRY
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param ctryCd - ctry-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/countries","/eds-country-tax/countries/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryByCode(
			  @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (ctryCd!=null) {
			pathVarMap = new HashMap<>();
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd);
			else
				pathVarMap.put("ctry-iso-id", ctryCd);
		}
		
		return edsDaoService.find(BmsCountryDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-country-tax/countries-alt","/eds-country-tax/countries-alt/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryAltByCode(
			  @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (ctryCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ctry-cd", ctryCd);
		}
		
		return edsDaoService.find(BmsCountry2Dim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}	
	
	@GetMapping(path={"/eds-country-tax/geographies/{geo-cd}/countries","/eds-country-tax/geographies/{geo-cd}/countries/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryByGeo(
			  @PathVariable(name="geo-cd") String geoCd
			, @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>(); 
		pathVarMap.put("geo-cd", geoCd);
		if (ctryCd!=null) {
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd);
			else
				pathVarMap.put("ctry-iso-id", ctryCd);
		}

		
		return edsDaoService.find(BmsCountry2Dim.class, pathVarMap, filters, orderByCols, true, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-country-tax/geographies/{geo-cd}/markets/{mrkt-cd}/countries","/eds-country-tax/geographies/{geo-cd}/markets/{mrkt-cd}/countries/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryByGeoMrkt(
			  @PathVariable(name="geo-cd") String geoCd
			, @PathVariable(name="mrkt-cd") String mrktCd
			, @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>(); 
		pathVarMap.put("geo-cd", geoCd);
		pathVarMap.put("mrkt-cd", mrktCd);
		if (ctryCd!=null) {
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd);
			else
				pathVarMap.put("ctry-iso-id", ctryCd);
		}

		
		return edsDaoService.find(BmsCountry2Dim.class, pathVarMap, filters, orderByCols, true, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-country-tax/geographies/{geo-cd}/markets/{mrkt-cd}/regions/{rgn-cd}/countries"
			         ,"/eds-country-tax/geographies/{geo-cd}/markets/{mrkt-cd}/regions/{rgn-cd}/countries/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryByGeoMrktRgn(
			  @PathVariable(name="geo-cd") String geoCd
			, @PathVariable(name="mrkt-cd") String mrktCd
			, @PathVariable(name="rgn-cd") String rgnCd
			, @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>(); 
		pathVarMap.put("geo-cd", geoCd);
		pathVarMap.put("mrkt-cd", mrktCd);
		pathVarMap.put("rgn-cd", rgnCd);
		if (ctryCd!=null) {
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd);
			else
				pathVarMap.put("ctry-iso-id", ctryCd);
		}

		
		return edsDaoService.find(BmsCountryDim.class, pathVarMap, filters, orderByCols, true, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}	
	
	
	@PostMapping(value="/eds-country-tax/countries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCountry(@RequestBody BmsCountryDim fbsCountry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsCountry);
	}
	
	@DeleteMapping("/eds-country-tax/countries")
	public  ResponseEntity<Integer> deleteAllCountrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BmsCountryDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/countries/{ctry-cd}")
	public ResponseEntity<Integer> deleteCountry(@PathVariable(name="ctry-cd") @Parameter(description = "FBS Country Code") String ctryCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsCountryDim country = new BmsCountryDim(ctryCd);
		return edsDaoService.delete(BmsCountryDim.class, country);
	}	
	
	@PostMapping(value="/eds-country-tax/countries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCountrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countrys.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsCountryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/countries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCountrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countrys.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BmsCountryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * COUNTRY_DELIVERY_ORG_ASSOC - country-delivery-org-assocs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param ctryCd - ctry-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/country-delivery-org-assocs","/eds-country-tax/country-delivery-org-assocs/{ctry-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryDeliveryOrgAssocByCode(
			  @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (ctryCd!=null) {
			pathVarMap = new HashMap<>();
			if (ctryCd.length()>2)
				pathVarMap.put("ctry-cd", ctryCd);
			else
				pathVarMap.put("ctry-iso-id", ctryCd);
		}
		
		return edsDaoService.find(CountryDeliveryOrgAssocDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/country-delivery-org-assocs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCountryDeliveryOrgAssoc(@RequestBody CountryDeliveryOrgAssocDim fbsCountryDeliveryOrgAssoc) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsCountryDeliveryOrgAssoc);
	}
	
	@DeleteMapping("/eds-country-tax/country-delivery-org-assocs")
	public  ResponseEntity<Integer> deleteAllCountryDeliveryOrgAssocs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(CountryDeliveryOrgAssocDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/country-delivery-org-assocs/{ctry-cd}/{dlvry-org-cd}")
	public ResponseEntity<Integer> deleteCountryDeliveryOrgAssoc(
			  @PathVariable(name="ctry-cd") @Parameter(description = "CountryDeliveryOrgAssoc Code") String ctryCd
			, @PathVariable(name="dlvry-org-cd") @Parameter(description = "CountryDeliveryOrgAssoc Code") String dlvryOrgCd) 
					throws IllegalArgumentException, IllegalAccessException, SQLException {
		CountryDeliveryOrgAssocDim countryDeliveryOrgAssoc = new CountryDeliveryOrgAssocDim(ctryCd,dlvryOrgCd);
		return edsDaoService.delete(CountryDeliveryOrgAssocDim.class, countryDeliveryOrgAssoc);
	}	
	
	@PostMapping(value="/eds-country-tax/country-delivery-org-assocs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCountryDeliveryOrgAssocs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="country-delivery-org-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(CountryDeliveryOrgAssocDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/country-delivery-org-assocs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCountryDeliveryOrgAssocs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="country-delivery-org-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(CountryDeliveryOrgAssocDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * DELIVERY_ORGANIZATION - delivery-organizations
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param dlvryOrgCd - dlvry-org-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/delivery-organizations","/eds-country-tax/delivery-organizations/{dlvry-org-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllDeliveryOrganizationByCode(
			  @PathVariable(name="dlvry-org-cd", required=false) String dlvryOrgCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include only EDS data?") boolean edsOnly
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		if (dlvryOrgCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("dlvry-org-cd", dlvryOrgCd);
		}
		
		return edsDaoService.find(DeliveryOrganizationDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, edsOnly, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/delivery-organizations", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertDeliveryOrganization(@RequestBody DeliveryOrganizationDim fbsDeliveryOrganization) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsDeliveryOrganization);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-organizations")
	public  ResponseEntity<Integer> deleteAllDeliveryOrganizations(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(DeliveryOrganizationDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-organizations/{dlvry-org-cd}")
	public ResponseEntity<Integer> deleteDeliveryOrganization(@PathVariable(name="dlvry-org-cd") @Parameter(description = "FBS DeliveryOrganization Code") String dlvryOrgCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		DeliveryOrganizationDim deliveryOrganization = new DeliveryOrganizationDim(dlvryOrgCd);
		return edsDaoService.delete(DeliveryOrganizationDim.class, deliveryOrganization);
	}	
	
	@PostMapping(value="/eds-country-tax/delivery-organizations/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlDeliveryOrganizations(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-organizations.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryOrganizationDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/delivery-organizations/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlDeliveryOrganizations(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-organizations.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryOrganizationDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * DELIVERY_AREA - delivery-areas
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param dlvryAreaCd - dlvry-area-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/delivery-areas","/eds-country-tax/delivery-areas/{dlvry-area-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllDeliveryAreaByCode(
			  @PathVariable(name="dlvry-area-cd", required=false) String dlvryAreaCd
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
		if (dlvryAreaCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("dlvry-area-cd", dlvryAreaCd);
		}
		
		return edsDaoService.find(DeliveryAreaDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/delivery-areas", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertDeliveryArea(@RequestBody DeliveryAreaDim fbsDeliveryArea) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsDeliveryArea);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-areas")
	public  ResponseEntity<Integer> deleteAllDeliveryAreas(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(DeliveryAreaDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-areas/{dlvry-area-cd}")
	public ResponseEntity<Integer> deleteDeliveryArea(@PathVariable(name="dlvry-area-cd") @Parameter(description = "FBS DeliveryArea Code") String dlvryAreaCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		DeliveryAreaDim deliveryArea = new DeliveryAreaDim(dlvryAreaCd);
		return edsDaoService.delete(DeliveryAreaDim.class, deliveryArea);
	}	
	
	@PostMapping(value="/eds-country-tax/delivery-areas/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlDeliveryAreas(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-areas.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/delivery-areas/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlDeliveryAreas(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-areas.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * DELIVERY_AREA_GEOGRAPHY_ASSOC - delivery-area-geography-assocs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param dlvryAreaGeoCd - dlvry-area-geo-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/delivery-area-geography-assocs","/eds-country-tax/delivery-area-geography-assocs/{dlvry-area-geo-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllDeliveryAreaGeographyAssocByCode(
			  @PathVariable(name="dlvry-area-geo-cd", required=false) String dlvryAreaGeoCd
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
		if (dlvryAreaGeoCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("dlvry-area-geo-cd", dlvryAreaGeoCd);
		}
		
		return edsDaoService.find(DeliveryAreaGeographyAssocDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/delivery-area-geography-assocs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertDeliveryAreaGeographyAssoc(@RequestBody DeliveryAreaGeographyAssocDim fbsDeliveryAreaGeographyAssoc) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsDeliveryAreaGeographyAssoc);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-area-geography-assocs")
	public  ResponseEntity<Integer> deleteAllDeliveryAreaGeographyAssocs(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(DeliveryAreaGeographyAssocDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-area-geography-assocs/{dlvry-area-geo-cd}")
	public ResponseEntity<Integer> deleteDeliveryAreaGeographyAssoc(@PathVariable(name="dlvry-area-geo-cd") @Parameter(description = "FBS DeliveryAreaGeographyAssoc Code") String dlvryAreaGeoCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		DeliveryAreaGeographyAssocDim deliveryAreaGeographyAssoc = new DeliveryAreaGeographyAssocDim(dlvryAreaGeoCd);
		return edsDaoService.delete(DeliveryAreaGeographyAssocDim.class, deliveryAreaGeographyAssoc);
	}	
	
	@PostMapping(value="/eds-country-tax/delivery-area-geography-assocs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlDeliveryAreaGeographyAssocs(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-area-geography-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaGeographyAssocDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/delivery-area-geography-assocs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlDeliveryAreaGeographyAssocs(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-area-geography-assocs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaGeographyAssocDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * DELIVERY_AREA_MARKET - delivery-area-markets
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param dlvryAreaMrktCd - dlvry-area-mrkt-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-country-tax/delivery-area-markets","/eds-country-tax/delivery-area-markets/{dlvry-area-mrkt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllDeliveryAreaMarketByCode(
			  @PathVariable(name="dlvry-area-mrkt-cd", required=false) String dlvryAreaMrktCd
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
		if (dlvryAreaMrktCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("dlvry-area-mrkt-cd", dlvryAreaMrktCd);
		}
		
		return edsDaoService.find(DeliveryAreaMarketDim.class, pathVarMap, filters, orderByCols, includeParentage, "WW Top", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-country-tax/delivery-area-markets", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertDeliveryAreaMarket(@RequestBody DeliveryAreaMarketDim fbsDeliveryAreaMarket) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsDeliveryAreaMarket);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-area-markets")
	public  ResponseEntity<Integer> deleteAllDeliveryAreaMarkets(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(DeliveryAreaMarketDim.class, filters);
	}
	
	@DeleteMapping("/eds-country-tax/delivery-area-markets/{dlvry-area-mrkt-cd}")
	public ResponseEntity<Integer> deleteDeliveryAreaMarket(@PathVariable(name="dlvry-area-mrkt-cd") @Parameter(description = "FBS DeliveryAreaMarket Code") String dlvryAreaMrktCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		DeliveryAreaMarketDim deliveryAreaMarket = new DeliveryAreaMarketDim(dlvryAreaMrktCd);
		return edsDaoService.delete(DeliveryAreaMarketDim.class, deliveryAreaMarket);
	}	
	
	@PostMapping(value="/eds-country-tax/delivery-area-markets/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlDeliveryAreaMarkets(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-area-markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaMarketDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-country-tax/delivery-area-markets/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlDeliveryAreaMarkets(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="delivery-area-markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(DeliveryAreaMarketDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	
}