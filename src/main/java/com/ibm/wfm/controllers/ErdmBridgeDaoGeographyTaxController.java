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

import com.ibm.wfm.beans.CountryFeederDim;
import com.ibm.wfm.beans.CountryFinancialDim;
import com.ibm.wfm.beans.CountryIsoDim;
import com.ibm.wfm.beans.CountryMeasurementDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GeographyDim;
import com.ibm.wfm.beans.GlobalDim;
import com.ibm.wfm.beans.IbmGeographyDim;
import com.ibm.wfm.beans.MarketDim;
import com.ibm.wfm.beans.RegionDim;
import com.ibm.wfm.beans.TotalGeographyDim;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class ErdmBridgeDaoGeographyTaxController extends AbstractDaoController {

	@Autowired
	private EdsDaoService erdmGeographyTaxDaoService;


	/**
	 * IBM_GEOGRAPHY - ibm-geographies
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
	@GetMapping(path={"/erdm-geography-tax/ibm-geographies","/erdm-geography-tax/ibm-geographies/{code}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIbmGeographyByCode(
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
		
		return erdmGeographyTaxDaoService.find(IbmGeographyDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/erdm-geography-tax/ibm-geographies", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIbmGeography(@RequestBody IbmGeographyDim fbsIbmGeography) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return erdmGeographyTaxDaoService.insert(fbsIbmGeography);
	}
	
	@DeleteMapping("/erdm-geography-tax/ibm-geographies")
	public  ResponseEntity<Integer> deleteAllIbmGeographys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return erdmGeographyTaxDaoService.delete(IbmGeographyDim.class, filters);
	}
	
	@DeleteMapping("/erdm-geography-tax/ibm-geographies/{code}")
	public ResponseEntity<Integer> deleteIbmGeography(@PathVariable(name="code") @Parameter(description = "FBS IbmGeography Code") String code) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IbmGeographyDim ibmGeography = new IbmGeographyDim(code);
		return erdmGeographyTaxDaoService.delete(IbmGeographyDim.class, ibmGeography);
	}	
	
	@PostMapping(value="/erdm-geography-tax/ibm-geographies/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIbmGeographys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="ibm-geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return erdmGeographyTaxDaoService.etl(IbmGeographyDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/erdm-geography-tax/ibm-geographies/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIbmGeographys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="ibm-geographies.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return erdmGeographyTaxDaoService.etl(IbmGeographyDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * GLOBAL - globals
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param globalCd - global-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/globals","/erdm-geography-tax/globals/{global-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGlobalByCode(
			  @PathVariable(name="global-cd", required=false) String globalCd
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
		if (globalCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("global-cd", globalCd);
		}
		
		return erdmGeographyTaxDaoService.find(GlobalDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * TOTAL_GEOGRAPHY - total-geographies
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param totalGeoCd - total-geo-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/total-geographies","/erdm-geography-tax/total-geographies/{total-geo-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllTotalGeographyByCode(
			  @PathVariable(name="total-geo-cd", required=false) String totalGeoCd
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
		if (totalGeoCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("total-geo-cd", totalGeoCd);
		}
		
		return erdmGeographyTaxDaoService.find(TotalGeographyDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * GEOGRAPHY - geographies
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param geographyCd - geography-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/geographies","/erdm-geography-tax/geographies/{geography-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGeographyByCode(
			  @PathVariable(name="geography-cd", required=false) String geographyCd
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
		if (geographyCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("geography-cd", geographyCd);
		}
		
		return erdmGeographyTaxDaoService.find(GeographyDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * MARKET - markets
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param marketCd - market-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/markets","/erdm-geography-tax/markets/{market-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllMarketByCode(
			  @PathVariable(name="market-cd", required=false) String marketCd
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
		if (marketCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("market-cd", marketCd);
		}
		
		return erdmGeographyTaxDaoService.find(MarketDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * REGION - regions
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param regionCd - region-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/regions","/erdm-geography-tax/regions/{region-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllRegionByCode(
			  @PathVariable(name="region-cd", required=false) String regionCd
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
		if (regionCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("region-cd", regionCd);
		}
		
		return erdmGeographyTaxDaoService.find(RegionDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * COUNTRY_MEASUREMENT - country-measurements
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param countryMeasurementCd - country-measurement-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/country-measurements","/erdm-geography-tax/country-measurements/{country-measurement-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryMeasurementByCode(
			  @PathVariable(name="country-measurement-cd", required=false) String countryMeasurementCd
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
		if (countryMeasurementCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("country-measurement-cd", countryMeasurementCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryMeasurementDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * COUNTRY_FEEDER - country-feeders
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param countryFeederCd - country-feeder-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/country-feeders","/erdm-geography-tax/country-feeders/{country-feeder-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryFeederByCode(
			  @PathVariable(name="country-feeder-cd", required=false) String countryFeederCd
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
		if (countryFeederCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("country-feeder-cd", countryFeederCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryFeederDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * COUNTRY_ISO - country-isos
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param countryIsoCd - country-iso-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/country-isos","/erdm-geography-tax/country-isos/{country-iso-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryIsoByCode(
			  @PathVariable(name="country-iso-cd", required=false) String countryIsoCd
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
		if (countryIsoCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("country-iso-cd", countryIsoCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryIsoDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * COUNTRY_FINACIAL - country-finacials
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param countryFinancialCd - country-financial-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/erdm-geography-tax/country-finacials","/erdm-geography-tax/country-finacials/{country-financial-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryFinancialByCode(
			  @PathVariable(name="country-financial-cd", required=false) String countryFinancialCd
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
		if (countryFinancialCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("country-financial-cd", countryFinancialCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryFinancialDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * MARKET - markets full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets","/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllMarketByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=false) String marketCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t1.compliance-cd", geographyCd);
		if (marketCd!=null) {
			pathVarMap.put("market-cd", marketCd);
		}
		
		return erdmGeographyTaxDaoService.find(MarketDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * Region - regions full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions"
			,"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}"}
	        ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllRegionsByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=true) String marketCd
			, @PathVariable(name="region-cd", required=false) String regionCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t2.compliance-cd", geographyCd);
		pathVarMap.put("market-cd", marketCd);
		if (regionCd!=null) {
			pathVarMap.put("region-cd", regionCd);
		}
		
		return erdmGeographyTaxDaoService.find(RegionDim.class, pathVarMap, filters, orderByCols
				, true /*includeParentage */
				, null /* topNodeNm */
				, returnCsv
				, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * CountryMeasurement - country-measurement-cd full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-measurements"
			,"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-measurements/{country-measurement-cd}"}
	        ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryMeasurementByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=true) String marketCd
			, @PathVariable(name="region-cd", required=true) String regionCd
			, @PathVariable(name="country-measurement-cd", required=false) String countryMeasurementCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t3.compliance-cd", geographyCd);
		pathVarMap.put("market-cd", marketCd);
		pathVarMap.put("region-cd", regionCd);
		if (countryMeasurementCd!=null) {
			pathVarMap.put("country-measurement-cd", countryMeasurementCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryMeasurementDim.class, pathVarMap, filters, orderByCols
				, true /*includeParentage */
				, null /* topNodeNm */
				, returnCsv
				, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * CountryMeasurement - country-measurement-cd full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-measurements/{country-measurement-cd}/country-feeders"
			,"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-measurements/{country-measurement-cd}/country-feeders/{country-feeder-cd}"}
	        ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryFeederByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=true) String marketCd
			, @PathVariable(name="region-cd", required=true) String regionCd
			, @PathVariable(name="country-measurement-cd", required=true) String countryMeasurementCd
			, @PathVariable(name="country-feeder-cd", required=false) String countryFeederCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t4.compliance-cd", geographyCd);
		pathVarMap.put("market-cd", marketCd);
		pathVarMap.put("region-cd", regionCd);
		pathVarMap.put("country-measurement-cd", countryMeasurementCd);
		if (countryFeederCd!=null) {
			pathVarMap.put("country-feeder-cd", countryFeederCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryFeederDim.class, pathVarMap, filters, orderByCols
				, true /*includeParentage */
				, null /* topNodeNm */
				, returnCsv
				, includePit, pit, resultSetMaxSize, request);
	}
	
	/**
	 * CountryIsos - country-iso-cd full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-isos"
			,"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-isos/{country-iso-cd}"}
	        ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryIsoByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=true) String marketCd
			, @PathVariable(name="region-cd", required=true) String regionCd
			, @PathVariable(name="country-iso-cd", required=false) String countryIsoCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t3.compliance-cd", geographyCd);
		pathVarMap.put("market-cd", marketCd);
		pathVarMap.put("region-cd", regionCd);
		if (countryIsoCd!=null) {
			pathVarMap.put("country-iso-cd", countryIsoCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryIsoDim.class, pathVarMap, filters, orderByCols
				, true /*includeParentage */
				, null /* topNodeNm */
				, returnCsv
				, includePit, pit, resultSetMaxSize, request);
	}	
	
	/**
	 * CountryFinancials - country-financial-cd full taxonomy
	 */
	@GetMapping(path={"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-financials"
			,"/erdm-geography-tax/geographies/{geography-cd}/markets/{market-cd}/regions/{region-cd}/country-financials/{country-financial-cd}"}
	        ,produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllCountryFinancialsByGeoCode(
			  @PathVariable(name="geography-cd", required=true) String geographyCd
			, @PathVariable(name="market-cd", required=true) String marketCd
			, @PathVariable(name="region-cd", required=true) String regionCd
			, @PathVariable(name="country-financial-cd", required=false) String countryFinancialCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("global-cd", "IBM");
		pathVarMap.put("total-geo-cd", "51");
		pathVarMap.put("t3.compliance-cd", geographyCd);
		pathVarMap.put("market-cd", marketCd);
		pathVarMap.put("region-cd", regionCd);
		if (countryFinancialCd!=null) {
			pathVarMap.put("country-financial-cd", countryFinancialCd);
		}
		
		return erdmGeographyTaxDaoService.find(CountryFinancialDim.class, pathVarMap, filters, orderByCols
				, true /*includeParentage */
				, null /* topNodeNm */
				, returnCsv
				, includePit, pit, resultSetMaxSize, request);
	}		
	
}
