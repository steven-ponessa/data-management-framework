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

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GtmSegmentDim;
import com.ibm.wfm.beans.IndustryClassDim;
import com.ibm.wfm.beans.IndustryDim;
import com.ibm.wfm.beans.IndustryGroupDim;
import com.ibm.wfm.beans.IndustrySolutionUnitDim;
import com.ibm.wfm.beans.IsicDim;
import com.ibm.wfm.beans.SectorDim;
import com.ibm.wfm.beans.TotalIndustryDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class EdsDaoIndustryTaxonomyController extends AbstractDaoController {
	
	@Autowired
	private EdsDaoService edsIndustryTaxDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * TOTAL_INDUSTRY - total-industries
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param totIndCd - tot-ind-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/total-industries","/eds-industry-tax/total-industries/{tot-ind-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllTotalIndustryByCode(
			  @PathVariable(name="tot-ind-cd", required=false) String totIndCd
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
		if (totIndCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("tot-ind-cd", totIndCd);
		}
		
		return edsIndustryTaxDaoService.find(TotalIndustryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-industry-tax/total-industries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertTotalIndustry(@RequestBody TotalIndustryDim fbsTotalIndustry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsTotalIndustry);
	}
	
	@DeleteMapping("/eds-industry-tax/total-industries")
	public  ResponseEntity<Integer> deleteAllTotalIndustrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(TotalIndustryDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/total-industries/{tot-ind-cd}")
	public ResponseEntity<Integer> deleteTotalIndustry(@PathVariable(name="tot-ind-cd") @Parameter(description = "FBS TotalIndustry Code") String totIndCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		TotalIndustryDim totalIndustry = new TotalIndustryDim(totIndCd);
		return edsIndustryTaxDaoService.delete(TotalIndustryDim.class, totalIndustry);
	}	
	
	@PostMapping(value="/eds-industry-tax/total-industries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlTotalIndustrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="total-industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(TotalIndustryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/total-industries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlTotalIndustrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="total-industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(TotalIndustryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * GTM_SEGMENT - gtm-segments
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param gtmSgmtCd - gtm-sgmt-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/gtm-segments","/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllGtmSegmentByCode(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
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
		if (gtmSgmtCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		
		return edsIndustryTaxDaoService.find(GtmSegmentDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@PostMapping(value="/eds-industry-tax/gtm-segments", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertGtmSegment(@RequestBody GtmSegmentDim fbsGtmSegment) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsGtmSegment);
	}
	
	@DeleteMapping("/eds-industry-tax/gtm-segments")
	public  ResponseEntity<Integer> deleteAllGtmSegments(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(GtmSegmentDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}")
	public ResponseEntity<Integer> deleteGtmSegment(@PathVariable(name="gtm-sgmt-cd") @Parameter(description = "FBS GtmSegment Code") String gtmSgmtCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		GtmSegmentDim gtmSegment = new GtmSegmentDim(gtmSgmtCd);
		return edsIndustryTaxDaoService.delete(GtmSegmentDim.class, gtmSegment);
	}	
	
	@PostMapping(value="/eds-industry-tax/gtm-segments/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlGtmSegments(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gtm-segments.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(GtmSegmentDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/gtm-segments/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlGtmSegments(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="gtm-segments.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(GtmSegmentDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * SECTOR - sectors
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param sctrCd - sctr-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/sectors","/eds-industry-tax/sectors/{sctr-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllSectorByCode(
			  @PathVariable(name="sctr-cd", required=false) String sctrCd
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
		if (sctrCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("sctr-cd", sctrCd);
		}
		
		return edsIndustryTaxDaoService.find(SectorDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path= { 
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveSectorsByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			//pathVarMap = new HashMap<>();
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		
		return edsIndustryTaxDaoService.find(SectorDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	
	@PostMapping(value="/eds-industry-tax/sectors", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertSector(@RequestBody SectorDim fbsSector) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsSector);
	}
	
	@DeleteMapping("/eds-industry-tax/sectors")
	public  ResponseEntity<Integer> deleteAllSectors(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(SectorDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/sectors/{sctr-cd}")
	public ResponseEntity<Integer> deleteSector(@PathVariable(name="sctr-cd") @Parameter(description = "FBS Sector Code") String sctrCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		SectorDim sector = new SectorDim(sctrCd);
		return edsIndustryTaxDaoService.delete(SectorDim.class, sector);
	}	
	
	@PostMapping(value="/eds-industry-tax/sectors/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlSectors(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sectors.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(SectorDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/sectors/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlSectors(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="sectors.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(SectorDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	
	/**
	 * INDUSTRY_GROUP - industry-groups
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param indGrpCd - ind-grp-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/industry-groups","/eds-industry-tax/industry-groups/{ind-grp-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIndustryGroupByCode(
			  @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
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
		if (indGrpCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryGroupDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path= {
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveIndGrpBySectorByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			//pathVarMap = new HashMap<>();
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		if (indGrpCd!=null) {
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryGroupDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}		
	
	
	@PostMapping(value="/eds-industry-tax/industry-groups", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIndustryGroup(@RequestBody IndustryGroupDim fbsIndustryGroup) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsIndustryGroup);
	}
	
	@DeleteMapping("/eds-industry-tax/industry-groups")
	public  ResponseEntity<Integer> deleteAllIndustryGroups(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(IndustryGroupDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/industry-groups/{ind-grp-cd}")
	public ResponseEntity<Integer> deleteIndustryGroup(@PathVariable(name="ind-grp-cd") @Parameter(description = "FBS IndustryGroup Code") String indGrpCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IndustryGroupDim industryGroup = new IndustryGroupDim(indGrpCd);
		return edsIndustryTaxDaoService.delete(IndustryGroupDim.class, industryGroup);
	}	
	
	@PostMapping(value="/eds-industry-tax/industry-groups/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIndustryGroups(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryGroupDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/industry-groups/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIndustryGroups(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-groups.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryGroupDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * INDUSTRY_SOLUTION_UNIT - industry-solution-units
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param isuCd - isu-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/isus","/eds-industry-tax/isus/{isu-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIndustrySolutionUnitByCode(
			  @PathVariable(name="isu-cd", required=false) String isuCd
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
		if (isuCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("isu-cd", isuCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustrySolutionUnitDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	
	@GetMapping(path= {
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveIsuByIndGrpBySectorByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
			, @PathVariable(name="isu-cd", required=false) String isuCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			//pathVarMap = new HashMap<>();
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		if (indGrpCd!=null) {
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		if (isuCd!=null) {
			pathVarMap.put("isu-cd", isuCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustrySolutionUnitDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}		
	
	
	@PostMapping(value="/eds-industry-tax/isus", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIndustrySolutionUnit(@RequestBody IndustrySolutionUnitDim fbsIndustrySolutionUnit) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsIndustrySolutionUnit);
	}
	
	@DeleteMapping("/eds-industry-tax/isus")
	public  ResponseEntity<Integer> deleteAllIndustrySolutionUnits(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(IndustrySolutionUnitDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/isus/{isu-cd}")
	public ResponseEntity<Integer> deleteIndustrySolutionUnit(@PathVariable(name="isu-cd") @Parameter(description = "FBS IndustrySolutionUnit Code") String isuCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IndustrySolutionUnitDim industrySolutionUnit = new IndustrySolutionUnitDim(isuCd);
		return edsIndustryTaxDaoService.delete(IndustrySolutionUnitDim.class, industrySolutionUnit);
	}	
	
	@PostMapping(value="/eds-industry-tax/isus/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIndustrySolutionUnits(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-solution-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustrySolutionUnitDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/isus/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIndustrySolutionUnits(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-solution-units.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustrySolutionUnitDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * INDUSTRY - industries
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param indCd - ind-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/industries","/eds-industry-tax/industries/{ind-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIndustryByCode(
			  @PathVariable(name="ind-cd", required=false) String indCd
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
		if (indCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ind-cd", indCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path= {
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries/{ind-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveInsutryByIsuByIndGrpBySectorByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
			, @PathVariable(name="isu-cd", required=false) String isuCd
			, @PathVariable(name="ind-cd", required=false) String indCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		if (indGrpCd!=null) {
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		if (isuCd!=null) {
			pathVarMap.put("isu-cd", isuCd);
		}
		if (indCd!=null) {
			pathVarMap.put("ind-cd", indCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	
	@PostMapping(value="/eds-industry-tax/industries", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIndustry(@RequestBody IndustryDim fbsIndustry) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsIndustry);
	}
	
	@DeleteMapping("/eds-industry-tax/industries")
	public  ResponseEntity<Integer> deleteAllIndustrys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(IndustryDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/industries/{ind-cd}")
	public ResponseEntity<Integer> deleteIndustry(@PathVariable(name="ind-cd") @Parameter(description = "FBS Industry Code") String indCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IndustryDim industry = new IndustryDim(indCd);
		return edsIndustryTaxDaoService.delete(IndustryDim.class, industry);
	}	
	
	@PostMapping(value="/eds-industry-tax/industries/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIndustrys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/industries/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIndustrys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industries.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * INDUSTRY_CLASS - industry-classs
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param indClssCd - ind-clss-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/industry-classs","/eds-industry-tax/industry-classs/{ind-clss-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIndustryClassByCode(
			  @PathVariable(name="ind-clss-cd", required=false) String indClssCd
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
		if (indClssCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("ind-clss-cd", indClssCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryClassDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path= {
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries/{ind-cd}/industry-classs"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries/{ind-cd}/industry-classs/{ind-clss-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveICByInsutryByIsuByIndGrpBySectorByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
			, @PathVariable(name="isu-cd", required=false) String isuCd
			, @PathVariable(name="ind-cd", required=false) String indCd
			, @PathVariable(name="ind-clss-cd", required=false) String indClssCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		if (indGrpCd!=null) {
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		if (isuCd!=null) {
			pathVarMap.put("isu-cd", isuCd);
		}
		if (indCd!=null) {
			pathVarMap.put("ind-cd", indCd);
		}
		if (indClssCd!=null) {
			pathVarMap.put("ind-clss-cd", indClssCd);
		}
		
		return edsIndustryTaxDaoService.find(IndustryClassDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}	
	
	@PostMapping(value="/eds-industry-tax/industry-classs", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIndustryClass(@RequestBody IndustryClassDim fbsIndustryClass) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsIndustryClass);
	}
	
	@DeleteMapping("/eds-industry-tax/industry-classs")
	public  ResponseEntity<Integer> deleteAllIndustryClasss(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(IndustryClassDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/industry-classs/{ind-clss-cd}")
	public ResponseEntity<Integer> deleteIndustryClass(@PathVariable(name="ind-clss-cd") @Parameter(description = "FBS IndustryClass Code") String indClssCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IndustryClassDim industryClass = new IndustryClassDim(indClssCd);
		return edsIndustryTaxDaoService.delete(IndustryClassDim.class, industryClass);
	}	
	
	@PostMapping(value="/eds-industry-tax/industry-classs/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIndustryClasss(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-classs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryClassDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/industry-classs/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIndustryClasss(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="industry-classs.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IndustryClassDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * ISIC - isics
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param isicCd - isic-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/eds-industry-tax/isics","/eds-industry-tax/isics/{isic-cd}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAllIsicByCode(
			  @PathVariable(name="isic-cd", required=false) String isicCd
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
		if (isicCd!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("isic-cd", isicCd);
		}
		
		return edsIndustryTaxDaoService.find(IsicDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}
	
	@GetMapping(path= {
			"/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries/{ind-cd}/industry-classs/isics"
		,   "/eds-industry-tax/gtm-segments/{gtm-sgmt-cd}/sectors/{sctr-cd}/industry-groups/{ind-grp-cd}/isus/{isu-cd}/industries/{ind-cd}/industry-classs/{ind-clss-cd}/isics/{isic-cd}"
			         },produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveIsicByICByInsutryByIsuByIndGrpBySectorByGtmSegment(
			  @PathVariable(name="gtm-sgmt-cd", required=false) String gtmSgmtCd
			, @PathVariable(name="sctr-cd", required=false) String sctrCd
			, @PathVariable(name="ind-grp-cd", required=false) String indGrpCd
			, @PathVariable(name="isu-cd", required=false) String isuCd
			, @PathVariable(name="ind-cd", required=false) String indCd
			, @PathVariable(name="ind-clss-cd", required=false) String indClssCd
			, @PathVariable(name="isic-cd", required=false) String isicCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "") @Parameter(description = "Override the SQL ORDER BY clause (default is by natural key).") String orderByCols
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Return results as CSV") boolean returnCsv
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException {
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("tot-ind-cd", "70TI");
		if (gtmSgmtCd!=null) {
			pathVarMap.put("gtm-sgmt-cd", gtmSgmtCd);
		}
		if (sctrCd!=null) {
			pathVarMap.put("sctr-cd", sctrCd);
		}
		if (indGrpCd!=null) {
			pathVarMap.put("ind-grp-cd", indGrpCd);
		}
		if (isuCd!=null) {
			pathVarMap.put("isu-cd", isuCd);
		}
		if (indCd!=null) {
			pathVarMap.put("ind-cd", indCd);
		}
		if (indClssCd!=null) {
			pathVarMap.put("ind-clss-cd", indClssCd);
		}
		if (isicCd!=null) {
			pathVarMap.put("isic-cd", isicCd);
		}
		
		return edsIndustryTaxDaoService.find(IsicDim.class, pathVarMap, filters, orderByCols, true, null, returnCsv, includePit, pit, resultSetMaxSize, request);
	}		
	
	@PostMapping(value="/eds-industry-tax/isics", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertIsic(@RequestBody IsicDim fbsIsic) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return edsIndustryTaxDaoService.insert(fbsIsic);
	}
	
	@DeleteMapping("/eds-industry-tax/isics")
	public  ResponseEntity<Integer> deleteAllIsics(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.delete(IsicDim.class, filters);
	}
	
	@DeleteMapping("/eds-industry-tax/isics/{isic-cd}")
	public ResponseEntity<Integer> deleteIsic(@PathVariable(name="isic-cd") @Parameter(description = "FBS Isic Code") String isicCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		IsicDim isic = new IsicDim(isicCd);
		return edsIndustryTaxDaoService.delete(IsicDim.class, isic);
	}	
	
	@PostMapping(value="/eds-industry-tax/isics/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlIsics(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="isics.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IsicDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/eds-industry-tax/isics/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlIsics(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="isics.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return edsIndustryTaxDaoService.etl(IsicDim.class, oldFile, newFile, keyLength, outputFileName);
	}		

}