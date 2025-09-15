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

import com.ibm.wfm.beans.BusinessUnitDim;
import com.ibm.wfm.beans.BusinessUnitTopDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.SubBusinessUnitDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoBusinessUnitTaxonomyController extends AbstractDaoController {
	
	@Autowired
	private EdsDaoService edsDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	

	
	/**
	 * BUSINESS_UNIT_TOP - business-unit-tops
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param busUnitTopCd - bus-unit-top-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-business-unit-tax/business-unit-tops","/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllBusinessUnitTopByCode(
			  @PathVariable(name="bus-unit-top-cd", required=false) String busUnitTopCd
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
		if (busUnitTopCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("bus-unit-top-cd", busUnitTopCd);
		}
		
		return edsDaoService.find(BusinessUnitTopDim.class, pathVarMap, filters, orderByCols, includeParentage, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-business-unit-tax/business-unit-tops", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBusinessUnitTop(@RequestBody BusinessUnitTopDim fbsBusinessUnitTop) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsBusinessUnitTop);
	}
	
	@DeleteMapping("/eds-business-unit-tax/business-unit-tops")
	public  ResponseEntity<Integer> deleteAllBusinessUnitTops(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BusinessUnitTopDim.class, filters);
	}
	
	@DeleteMapping("/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}")
	public ResponseEntity<Integer> deleteBusinessUnitTop(@PathVariable(name="bus-unit-top-cd") @Parameter(description = "FBS BusinessUnitTop Code") String busUnitTopCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BusinessUnitTopDim businessUnitTop = new BusinessUnitTopDim(busUnitTopCd);
		return edsDaoService.delete(BusinessUnitTopDim.class, businessUnitTop);
	}	
	
	@PostMapping(value="/eds-business-unit-tax/business-unit-tops/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBusinessUnitTops(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="business-unit-tops.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BusinessUnitTopDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-business-unit-tax/business-unit-tops/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBusinessUnitTops(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="business-unit-tops.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BusinessUnitTopDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	

	
	/**
	 * BUSINESS_UNIT - business-units
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param busUnitCd - bus-unit-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-business-unit-tax/business-units","/eds-business-unit-tax/business-units/{bus-unit-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllBusinessUnitByCode(
			  @PathVariable(name="bus-unit-cd", required=false) String busUnitCd
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
		if (busUnitCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("bus-unit-cd", busUnitCd);
		}
		
		return edsDaoService.find(BusinessUnitDim.class, pathVarMap, filters, orderByCols, includeParentage, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}/business-units","/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}/business-units/{bus-unit-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllBusinessUnitByCode(
			  @PathVariable(name="bus-unit-top-cd", required=true) String busUnitTopCd
			, @PathVariable(name="bus-unit-cd", required=false) String busUnitCd
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
		pathVarMap.put("bus-unit-top-cd", busUnitTopCd);
		if (busUnitCd!=null) {
			pathVarMap.put("bus-unit-cd", busUnitCd);
		}
		
		return edsDaoService.find(BusinessUnitDim.class, pathVarMap, filters, orderByCols, includeParentage, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	
	@PostMapping(value="/eds-business-unit-tax/business-units", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBusinessUnit(@RequestBody BusinessUnitDim fbsBusinessUnit) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsBusinessUnit);
	}
	
	@DeleteMapping("/eds-business-unit-tax/business-units")
	public  ResponseEntity<Integer> deleteAllBusinessUnits(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(BusinessUnitDim.class, filters);
	}
	
	@DeleteMapping("/eds-business-unit-tax/business-units/{bus-unit-cd}")
	public ResponseEntity<Integer> deleteBusinessUnit(@PathVariable(name="bus-unit-cd") @Parameter(description = "FBS BusinessUnit Code") String busUnitCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BusinessUnitDim businessUnit = new BusinessUnitDim(busUnitCd);
		return edsDaoService.delete(BusinessUnitDim.class, businessUnit);
	}	
	
	@PostMapping(value="/eds-business-unit-tax/business-units/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBusinessUnits(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="business-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BusinessUnitDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-business-unit-tax/business-units/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBusinessUnits(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="business-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(BusinessUnitDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	

	
	/**
	 * SUB_BUSINESS_UNIT - sub-business-units
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param subBusUnitCd - sub-bus-unit-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-business-unit-tax/sub-business-units","/eds-business-unit-tax/sub-business-units/{sub-bus-unit-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSubBusinessUnitByCode(
			  @PathVariable(name="sub-bus-unit-cd", required=false) String subBusUnitCd
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
		if (subBusUnitCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("sub-bus-unit-cd", subBusUnitCd);
		}
		
		return edsDaoService.find(SubBusinessUnitDim.class, pathVarMap, filters, orderByCols, includeParentage, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
	}

	@GetMapping(path={"/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}/business-units/{bus-unit-cd}/sub-business-units","/eds-business-unit-tax/business-unit-tops/{bus-unit-top-cd}/business-units/{bus-unit-cd}/sub-business-units/{sub-bus-unit-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllBusinessUnitByCode(
			  @PathVariable(name="bus-unit-top-cd", required=true) String busUnitTopCd
			, @PathVariable(name="bus-unit-cd", required=true) String busUnitCd
			, @PathVariable(name="sub-bus-unit-cd", required=false) String subBusUnitCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			//, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include parent nodes?") boolean includeParentage
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = null;
		pathVarMap = new HashMap<>();
		pathVarMap.put("bus-unit-top-cd", busUnitTopCd);
		pathVarMap.put("bus-unit-cd", busUnitCd);
		if (subBusUnitCd!=null) {
			pathVarMap.put("sub-bus-unit-cd", subBusUnitCd);
		}
		
		return edsDaoService.find(SubBusinessUnitDim.class, pathVarMap, filters, orderByCols, true, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
		
		//return edsDaoService.find(Object.class, pathVarMap, filters, orderByCols, true, "Total", returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	@PostMapping(value="/eds-business-unit-tax/sub-business-units", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSubBusinessUnit(@RequestBody SubBusinessUnitDim fbsSubBusinessUnit) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsDaoService.insert(fbsSubBusinessUnit);
	}
	
	@DeleteMapping("/eds-business-unit-tax/sub-business-units")
	public  ResponseEntity<Integer> deleteAllSubBusinessUnits(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(SubBusinessUnitDim.class, filters);
	}
	
	@DeleteMapping("/eds-business-unit-tax/sub-business-units/{sub-bus-unit-cd}")
	public ResponseEntity<Integer> deleteSubBusinessUnit(@PathVariable(name="sub-bus-unit-cd") @Parameter(description = "FBS SubBusinessUnit Code") String subBusUnitCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SubBusinessUnitDim subBusinessUnit = new SubBusinessUnitDim(subBusUnitCd);
		return edsDaoService.delete(SubBusinessUnitDim.class, subBusinessUnit);
	}	
	
	@PostMapping(value="/eds-business-unit-tax/sub-business-units/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSubBusinessUnits(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sub-business-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(SubBusinessUnitDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-business-unit-tax/sub-business-units/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSubBusinessUnits(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sub-business-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(SubBusinessUnitDim.class, oldFile, newFile, keyLength, outputFileName);
	}		

}