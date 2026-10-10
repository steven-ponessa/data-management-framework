package com.ibm.wfm.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.sdss.CountryDim;
import com.ibm.wfm.beans.sdss.GeographyDim;
import com.ibm.wfm.beans.sdss.GeographyTypeDim;
import com.ibm.wfm.beans.sdss.MarketDim;
import com.ibm.wfm.beans.sdss.RegionDim;
import com.ibm.wfm.beans.sdss.SdssBulkRequest;
import com.ibm.wfm.beans.sdss.SdssChangeRequestPendingDim;
import com.ibm.wfm.beans.sdss.SdssExceptionDim;
import com.ibm.wfm.beans.sdss.SdssValidationExcelRenderer;
import com.ibm.wfm.beans.sdss.SdssValidationResponse;
import com.ibm.wfm.beans.sdss.SdssValidationZipRenderer;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.SdssCrValidationService;
import com.ibm.wfm.services.SdssDaoService;
import com.ibm.wfm.services.SdssReferenceDataService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class SdssController {
	@Autowired
	private SdssDaoService sdssDaoService;
	
	private FileStorageService fileStorageService;
	private FileStorageProperties fileStorageProperties;
	private final SdssReferenceDataService sdssReferenceDataService;
	
    public SdssController(
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
	@GetMapping(path={"/sdss/geography-types","/sdss/geography-types/{geo-typ-cd}"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllGeographyTypeByCode(
			  @PathVariable(name="geo-typ-cd", required=false) String geoTypCd
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
		if (geoTypCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}
		
		return sdssDaoService.find(GeographyTypeDim.class
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
	
	
	@PostMapping(value="/sdss/geography-types", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeographyType(@RequestBody GeographyTypeDim geographyType) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(geographyType);
	}
	
	@DeleteMapping("/sdss/geography-types")
	public  ResponseEntity<Integer> deleteAllGeographyTypes(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(GeographyTypeDim.class, filters);
	}
	
	@DeleteMapping("/sdss/geography-types/{geo-typ-cd}")
	public ResponseEntity<Integer> deleteGeographyType(@PathVariable(name="geo-typ-cd") @Parameter(description = "GeographyType Code") String geoTypCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GeographyTypeDim geographyType = new GeographyTypeDim(geoTypCd);
		return sdssDaoService.delete(GeographyTypeDim.class, geographyType);
	}	
	
	@PostMapping(value="/sdss/geography-types/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographyTypes(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(GeographyTypeDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/geography-types/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographyTypes(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geography-types.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(GeographyTypeDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path={"/sdss/geographies","/sdss/geographies/{geo-cd}","/sdss/geography-types/{geo-typ-cd}/geographies"}
	, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllGeographyByCode(
			  @PathVariable(name="geo-cd", required=false) String geoCd
			, @PathVariable(name="geo-typ-cd", required=false) String geoTypCd  
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
		if (geoTypCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}
		if (geoCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		
		return sdssDaoService.find(GeographyDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , includeParentage
		                                                     , false      //self-referencing
				                                             , "Top"       //topNodeName
		                                                     , returnCsv
		                                                     , includePit
		                                                     , pit
		                                                     , false      //edsOnly
		                                                     , offset
		                                                     , resultSetMaxSize
		                                                     , request);
	}
	
	
	@PostMapping(value="/sdss/geographies", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGeography(@RequestBody GeographyDim geography) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(geography);
	}
	
	@DeleteMapping("/sdss/geographies")
	public  ResponseEntity<Integer> deleteAllGeographys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(GeographyDim.class, filters);
	}
	
	@DeleteMapping("/sdss/geographies/{geo-cd}")
	public ResponseEntity<Integer> deleteGeography(@PathVariable(name="geo-cd") @Parameter(description = "Geography Code") String geoCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GeographyDim geography = new GeographyDim(geoCd);
		return sdssDaoService.delete(GeographyDim.class, geography);
	}	
	
	@PostMapping(value="/sdss/geographies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGeographys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(GeographyDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/geographies/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGeographys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(GeographyDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path={"/sdss/markets","/sdss/markets/{mrkt-cd}"
				, "/sdss/geography-types/{geo-typ-cd}/geographies/{geo-cd}/markets"
				, "/sdss/geographies/{geo-cd}/markets"
				}
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllMarketByCode(
			  @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @PathVariable(name="geo-cd", required=false) String geoCd
			, @PathVariable(name="geo-typ-cd", required=false) String geoTypCd 
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
		if (mrktCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		if (geoCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		if (geoTypCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}

		
		return sdssDaoService.find(MarketDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , includeParentage
		                                                     , false      //self-referencing
				                                             , "Top"       //topNodeName
		                                                     , returnCsv
		                                                     , includePit
		                                                     , pit
		                                                     , false      //edsOnly
		                                                     , offset
		                                                     , resultSetMaxSize
		                                                     , request);
	}
	
	
	@PostMapping(value="/sdss/markets", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertMarket(@RequestBody MarketDim market) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(market);
	}
	
	@DeleteMapping("/sdss/markets")
	public  ResponseEntity<Integer> deleteAllMarkets(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(MarketDim.class, filters);
	}
	
	@DeleteMapping("/sdss/markets/{mrkt-cd}")
	public ResponseEntity<Integer> deleteMarket(@PathVariable(name="mrkt-cd") @Parameter(description = "Market Code") String mrktCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		MarketDim market = new MarketDim(mrktCd);
		return sdssDaoService.delete(MarketDim.class, market);
	}	
	
	@PostMapping(value="/sdss/markets/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlMarkets(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(MarketDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/markets/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlMarkets(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="markets.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(MarketDim.class, oldFile, newFile, keyLength, outputFileName);
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
	@GetMapping(path={"/sdss/regions","/sdss/regions/{rgn-cd}"
				,"/sdss/geography-types/{geo-typ-cd}/geographies/{geo-cd}/markets/{mrkt-cd}/regions"
				,"/sdss/markets/{mrkt-cd}/regions"
				}
				,produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllRegionByCode(
			  @PathVariable(name="rgn-cd", required=false) String rgnCd
			, @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @PathVariable(name="geo-cd", required=false) String geoCd
			, @PathVariable(name="geo-typ-cd", required=false) String geoTypCd 
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
		if (rgnCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("rgn-cd", rgnCd);
		}
		if (mrktCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		if (geoCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		if (geoTypCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}
		
		return sdssDaoService.find(RegionDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , includeParentage
		                                                     , false      //self-referencing
				                                             , "Top"       //topNodeName
		                                                     , returnCsv
		                                                     , includePit
		                                                     , pit
		                                                     , false      //edsOnly
		                                                     , offset
		                                                     , resultSetMaxSize
		                                                     , request);
	}
	
	
	@PostMapping(value="/sdss/regions", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertRegion(@RequestBody RegionDim region) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(region);
	}
	
	@DeleteMapping("/sdss/regions")
	public  ResponseEntity<Integer> deleteAllRegions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(RegionDim.class, filters);
	}
	
	@DeleteMapping("/sdss/regions/{rgn-cd}")
	public ResponseEntity<Integer> deleteRegion(@PathVariable(name="rgn-cd") @Parameter(description = "Region Code") String rgnCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		RegionDim region = new RegionDim(rgnCd);
		return sdssDaoService.delete(RegionDim.class, region);
	}	
	
	@PostMapping(value="/sdss/regions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlRegions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(RegionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/regions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlRegions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="regions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(RegionDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * COUNTRY - countries
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
	@GetMapping(path={"/sdss/countries","/sdss/countries/{ctry-cd}"
			    , "/sdss/geography-types/{geo-typ-cd}/geographies/{geo-cd}/markets/{mrkt-cd}/regions/{rgn-cd}/countries"
			    , "/sdss/regions/{rgn-cd}/countries"
			    }
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllCountryByCode(
			  @PathVariable(name="ctry-cd", required=false) String ctryCd
			, @PathVariable(name="rgn-cd", required=false) String rgnCd
			, @PathVariable(name="mrkt-cd", required=false) String mrktCd
			, @PathVariable(name="geo-cd", required=false) String geoCd
			, @PathVariable(name="geo-typ-cd", required=false) String geoTypCd   
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
		if (ctryCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ctry-cd", ctryCd);
		}
		if (rgnCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("rgn-cd", rgnCd);
		}
		if (mrktCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("mrkt-cd", mrktCd);
		}
		if (geoCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-cd", geoCd);
		}
		if (geoTypCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("geo-typ-cd", geoTypCd);
		}
		
		return sdssDaoService.find(CountryDim.class
		                                                     , pathVarMap
		                                                     , filters
		                                                     , orderByCols
		                                                     , includeParentage
		                                                     , false      //self-referencing
				                                             , "Top"       //topNodeName
		                                                     , returnCsv
		                                                     , includePit
		                                                     , pit
		                                                     , false      //edsOnly
		                                                     , offset
		                                                     , resultSetMaxSize
		                                                     , request);
	}
	
	
	@PostMapping(value="/sdss/countries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertCountry(@RequestBody CountryDim country) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(country);
	}
	
	@DeleteMapping("/sdss/countries")
	public  ResponseEntity<Integer> deleteAllCountrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(CountryDim.class, filters);
	}
	
	@DeleteMapping("/sdss/countries/{ctry-cd}")
	public ResponseEntity<Integer> deleteCountry(@PathVariable(name="ctry-cd") @Parameter(description = "Country Code") String ctryCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		CountryDim country = new CountryDim(ctryCd);
		return sdssDaoService.delete(CountryDim.class, country);
	}	
	
	@PostMapping(value="/sdss/countries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlCountrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(CountryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/countries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlCountrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="countries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(CountryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	

	
	/**
	 * SDSS_EXCEPTION - sdss-exceptions
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param exceptionCd - exception-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/sdss/sdss-exceptions","/sdss/sdss-exceptions/{exception-cd}"}
	, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSdssExceptionByCode(
			  @PathVariable(name="exception-cd", required=false) String exceptionCd
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
		if (exceptionCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("exception-cd", exceptionCd);
		}
		
		return sdssDaoService.find(SdssExceptionDim.class
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
	
	
	@PostMapping(value="/sdss/sdss-exceptions", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSdssException(@RequestBody SdssExceptionDim sdssException) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return sdssDaoService.insert(sdssException);
	}
	
	@DeleteMapping("/sdss/sdss-exceptions")
	public  ResponseEntity<Integer> deleteAllSdssExceptions(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.delete(SdssExceptionDim.class, filters);
	}
	
	@DeleteMapping("/sdss/sdss-exceptions/{exception-cd}")
	public ResponseEntity<Integer> deleteSdssException(@PathVariable(name="exception-cd") @Parameter(description = "SdssException Code") String exceptionCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SdssExceptionDim sdssException = new SdssExceptionDim(exceptionCd);
		return sdssDaoService.delete(SdssExceptionDim.class, sdssException);
	}	
	
	@PostMapping(value="/sdss/sdss-exceptions/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSdssExceptions(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sdss-exceptions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(SdssExceptionDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/sdss/sdss-exceptions/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSdssExceptions(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sdss-exceptions.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return sdssDaoService.etl(SdssExceptionDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * SDSS_CHANGE_REQUEST_PENDING - sdss-change-request-pendings
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param changeId - change-id PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/sdss/sdss-change-request-pendings","/sdss/sdss-change-request-pendings/{change-id}"}
	, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllSdssChangeRequestPendingByCode(
			  @PathVariable(name="change-id", required=false) String changeId
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
		if (changeId!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("change-id", changeId);
		}
		
		return sdssDaoService.find(SdssChangeRequestPendingDim.class
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

	@GetMapping(path={"/sdss/sdss-change-request-pendings/count"},produces = MediaType.APPLICATION_JSON_VALUE)
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

		return sdssDaoService.countAll(SdssChangeRequestPendingDim.class, pathVarMap, filters, includePit, null, false);
		                                  
	}	
	
	@PostMapping(value="/sdss/sdss-change-request-pendings", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> updateSdssChangeRequest(@RequestBody SdssChangeRequestPendingDim sdssChangeRequest) throws IllegalArgumentException, IllegalAccessException, SQLException  {

		String approvalType = "";
		String requestStatusCd=sdssChangeRequest.getRequestStatusCd();
		if ("approver".equals(sdssChangeRequest.getUserType().toLowerCase())) {
			approvalType = "PRIMARY";
			if ("A".equalsIgnoreCase(requestStatusCd)) requestStatusCd+="1";
		}
		else if ("secondary approver".equals(sdssChangeRequest.getUserType().toLowerCase())) {
			approvalType = "SECONDARY";
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		String rejectionReason = null;
		if ("R".equalsIgnoreCase(sdssChangeRequest.getRequestStatusCd())) {
			rejectionReason = sdssChangeRequest.getRejectionReason();
		}
		
		
		String sql = "UPDATE REFT.SDSS_CHANGE_REQUEST "+
		//String sql = "UPDATE SDSS.SDSS_CHANGE_REQUEST "+
		             "SET "+ approvalType+"_APRVL_STATUS=?,"+
		                     approvalType+"_APRVR_ID=?,"+
		                     approvalType+"_APRVL_TMS=CURRENT TIMESTAMP, "+
		                     (approvalType == "PRIMARY"? 
		                    		 "SECONDARY_APRVL_STATUS='P',SECONDARY_APRVR_ID=NULL,SECONDARY_APRVL_TMS=NULL, "
		                    		 :""
		                      )+
		                     "REQUEST_STATUS_CD=?, "+
		                     "REJECTION_REASON=? "+
		             "WHERE  CHANGE_ID=?"
		             ;
		
		return sdssDaoService.putUpdateQuery(sql
				                           , sdssChangeRequest.getRequestStatusCd()
				                           , sdssChangeRequest.getIntranetId()
				                           , requestStatusCd
				                           , rejectionReason
				                           , sdssChangeRequest.getChangeId());
	}
	
	@PostMapping(
		    value = "/sdss/sdss-change-request-pendings/bulk-requests",
		    consumes = "application/json",
		    produces = "application/json"
	)
	public ResponseEntity<Integer> updateSdssChangeRequests(
	        @RequestHeader("X-User-Id") String userId,
	        @RequestHeader("X-User-Type") String userType,
	        @RequestBody SdssBulkRequest request)
	        		throws IllegalArgumentException, IllegalAccessException, SQLException {
		
		String approvalType = "";
		String requestStatusCd=(request.getAction().equalsIgnoreCase("approve")?"A":"R");
		if ("approver".equals(userType.toLowerCase())) {
			approvalType = "PRIMARY";
			if ("A".equalsIgnoreCase(requestStatusCd)) requestStatusCd+="1";
		}
		else if ("secondary approver".equals(userType.toLowerCase())) {
			approvalType = "SECONDARY";
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
				
		List<Long> changeIds = request.getChangeIds();

		if (changeIds == null || changeIds.isEmpty()) {
		    throw new IllegalArgumentException(
		        "At least one changeId is required."
		    );
		}

		String placeholders = String.join(
		    ",",
		    Collections.nCopies(changeIds.size(), "?")
		);		
		
		String sql =
			    "UPDATE REFT.SDSS_CHANGE_REQUEST " +
			    "SET " + approvalType + "_APRVL_STATUS=?, " +
			            approvalType + "_APRVR_ID=?, " +
			            approvalType + "_APRVL_TMS=CURRENT TIMESTAMP, " +
			            ("PRIMARY".equals(approvalType)
			                ? "SECONDARY_APRVL_STATUS='P', " +
			                  "SECONDARY_APRVR_ID=NULL, " +
			                  "SECONDARY_APRVL_TMS=NULL, "
			                : "") +
			            "REQUEST_STATUS_CD=?, " +
			            "REJECTION_REASON=? " +
			    "WHERE CHANGE_ID IN (" + placeholders + ")";

			List<Object> params = new ArrayList<>();		

			params.add(request.getAction().equalsIgnoreCase("approve")?"A":"R");
			params.add(userId);
			params.add(requestStatusCd);
			params.add(request.getRejectionReason());
			params.addAll(changeIds);

			return sdssDaoService.putUpdateQuery(
			    sql,
			    params.toArray()
			);
	}	
	
	@PostMapping(value = "/sdss/sdss-change-request-pendings/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
		System.out.println("excelFileName: " + excelFileName);
		System.out.println("uploadUserid: " + uploadUserid);
		System.out.println("userType: " + userType);
		
		// 2. Validate data 
		SdssCrValidationService validationService = new SdssCrValidationService();
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
		
		
		
		String responseTxt = "File uploaded";
		return ResponseEntity.ok("{ \"message\": \""+responseTxt+"\" }");
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
	            "attachment; filename=\"sdss-cr-validation-errors.xlsx\""
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
	            "attachment; filename=\"sdss-cr-validation-results.zip\""
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
