package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.KyndrylCountryDim;
import com.ibm.wfm.beans.KyndrylCtryIndSctrAssocDim;
import com.ibm.wfm.beans.KyndrylIndustrySectorDim;
import com.ibm.wfm.beans.KyndrylOrganizationMarketDim;
import com.ibm.wfm.beans.KyndrylStrategicMarketDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.KyndrylDaoService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class KyndrylDaoController {
	@Autowired
	private KyndrylDaoService kyndrylDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * KYNDRYL_ORGANIZATION_MARKET
	 */
	/* KYNDRYL_ORGANIZATION_MARKET */
	@GetMapping(path="/kyndryl/organization-markets",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylOrganizationMarketDim> retrieveAllOrgMarkets(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylOrganizationMarketDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylOrganizationMarketDim.class, filters, pit, size);
		}
	}
	
	@GetMapping(path="/kyndryl/organization-markets/{org-market-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylOrganizationMarketDim> retrieveOrgMarket(
			  @PathVariable(name="org-market-cd") String orgMarketCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {

		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"ORG_MKT_CD='"+orgMarketCd.trim()+"'";
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylOrganizationMarketDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylOrganizationMarketDim.class, filters, pit, size);
		}
	}
	
	@GetMapping("/kyndryl/organization-markets/csv")
	public ResponseEntity<Resource> retrieveAllOrgMarketsCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /kyndryl/organization-markets
		String orgMarketslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/kyndryl/organization-markets")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<KyndrylOrganizationMarketDim[]> responseEntity = new RestTemplate().getForEntity(orgMarketslUri, KyndrylOrganizationMarketDim[].class); //uriVariables); 
		List<KyndrylOrganizationMarketDim> laborModels = Arrays.asList(responseEntity.getBody());

		return kyndrylDaoService.returnCsv(laborModels, "kyndryl-org-markets.csv", request);
	}
	
	@DeleteMapping("/kyndryl/organization-markets")
	public int deleteAllOrgMarkets() {
		return kyndrylDaoService.deleteAll(KyndrylOrganizationMarketDim.class);
	}
	
	@DeleteMapping("/kyndryl/organization-markets/{org-market-cd}")
	public int deleteOrgMarket(@PathVariable(name="org-market-cd") String orgMarketCd) {
		
		KyndrylOrganizationMarketDim kyndrylOrganizationMarketDim = new KyndrylOrganizationMarketDim(orgMarketCd);
		
		return kyndrylDaoService.delete(Arrays.asList(kyndrylOrganizationMarketDim));
	}
	
	@PostMapping(value="/kyndryl/organization-markets/etl-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsOrgMarketUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="kyndryl-org-market.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		
		EtlResponse etlResponse = kyndrylDaoService.etl(KyndrylOrganizationMarketDim.class
				                                          , fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}
	
	/* KYNDRYL_STRATEGIC_MARKET */
	@GetMapping(path="/kyndryl/strategic-markets",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylOrganizationMarketDim> retrieveAllStrategicMarkets(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylOrganizationMarketDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylOrganizationMarketDim.class, filters, pit, size);
		}
	}
	
	@GetMapping(path="/kyndryl/strategic-markets/{strategic-market-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylStrategicMarketDim> retrieveStategicMarket(
			  @PathVariable(name="strategic-market-cd") String strategicMarketCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"STRATEGIC_MKT_CD='"+strategicMarketCd.trim()+"'";
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylStrategicMarketDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylStrategicMarketDim.class, filters, pit, size);
		}
	}
	
	@GetMapping("/kyndryl/strategic-markets/csv")
	public ResponseEntity<Resource> retrieveAllStrategicMarketsCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /kyndryl/strategic-markets
		String orgMarketslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/kyndryl/strategic-markets")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<KyndrylStrategicMarketDim[]> responseEntity = new RestTemplate().getForEntity(orgMarketslUri, KyndrylStrategicMarketDim[].class); //uriVariables); 
		List<KyndrylStrategicMarketDim> laborModels = Arrays.asList(responseEntity.getBody());

		return kyndrylDaoService.returnCsv(laborModels, "kyndryl-strategic-markets.csv", request);
	}
	
	@DeleteMapping("/kyndryl/strategic-markets")
	public int deleteAllStrategicMarkets() {
		return kyndrylDaoService.deleteAll(KyndrylStrategicMarketDim.class);
	}
	
	@DeleteMapping("/kyndryl/strategic-markets/{strategic-market-cd}")
	public int deleteStrategicMarket(@PathVariable(name="strategic-market-cd") String strategicMarketCd) {
		
		KyndrylStrategicMarketDim kyndrylOrganizationMarketDim = new KyndrylStrategicMarketDim(strategicMarketCd);
		
		return kyndrylDaoService.delete(Arrays.asList(kyndrylOrganizationMarketDim));
	}
	
	@PostMapping(value="/kyndryl/strategic-markets/etl-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsStategicMarketUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="kyndryl-strategic-market.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		
		EtlResponse etlResponse = kyndrylDaoService.etl(KyndrylStrategicMarketDim.class
				                                          , fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/* KYNDRYL_COUNTRY_DIM */
	@GetMapping(path="/kyndryl/countries",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylCountryDim> retrieveAllCountries(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylCountryDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylCountryDim.class, filters, pit, size);
		}
	}
	
	@GetMapping(path="/kyndryl/countries/{country-iso-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylCountryDim> retrieveCountries(
			  @PathVariable(name="country-iso-cd") String ctryIsoCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {

		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"CTRY_ISO_CD='"+ctryIsoCd.trim()+"'";
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylCountryDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylCountryDim.class, filters, pit, size);
		}
	}
	
	@GetMapping("/kyndryl/countries/csv")
	public ResponseEntity<Resource> retrieveAllCountriesCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /kyndryl/countries
		String orgMarketslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/kyndryl/countries")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<KyndrylCountryDim[]> responseEntity = new RestTemplate().getForEntity(orgMarketslUri, KyndrylCountryDim[].class); //uriVariables); 
		List<KyndrylCountryDim> laborModels = Arrays.asList(responseEntity.getBody());

		return kyndrylDaoService.returnCsv(laborModels, "kyndryl-countries.csv", request);
	}
	
	@DeleteMapping("/kyndryl/countries")
	public int deleteAllCountries() {
		return kyndrylDaoService.deleteAll(KyndrylCountryDim.class);
	}
	
	@DeleteMapping("/kyndryl/countries/{country-iso-cd}")
	public int deleteKyndrylCountries(@PathVariable(name="country-iso-cd") String ctryIsoCd) {
		
		KyndrylCountryDim kyndrylCountryDim = new KyndrylCountryDim(ctryIsoCd);
		
		return kyndrylDaoService.delete(Arrays.asList(kyndrylCountryDim));
	}
	
	@PostMapping(value="/kyndryl/countries/etl-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsCountriesUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="kyndryl-countries.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);

		EtlResponse etlResponse = kyndrylDaoService.etl(KyndrylCountryDim.class
				                                          , fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/* KYNDRYL_INDUSTRY_SECTOR_DIM */
	@GetMapping(path="/kyndryl/industry-sectors",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylIndustrySectorDim> retrieveAllIndustrySectors(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylIndustrySectorDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylIndustrySectorDim.class, filters, pit, size);
		}
	}
	
	@GetMapping(path="/kyndryl/industry-sectors/{industry-sector-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylIndustrySectorDim> retrieveIndustrySectors(
			  @PathVariable(name="industry-sector-cd") String indSctrCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"IND_SCTR_CD='"+indSctrCd.trim()+"'";
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylIndustrySectorDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylIndustrySectorDim.class, filters, pit, size);
		}
	}
	
	@GetMapping("/kyndryl/industry-sectors/csv")
	public ResponseEntity<Resource> retrieveAllIndustrySectorsCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /kyndryl/industry-sectors
		String orgMarketslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/kyndryl/industry-sectors")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<KyndrylIndustrySectorDim[]> responseEntity = new RestTemplate().getForEntity(orgMarketslUri, KyndrylIndustrySectorDim[].class); //uriVariables); 
		List<KyndrylIndustrySectorDim> industrySectors = Arrays.asList(responseEntity.getBody());

		return kyndrylDaoService.returnCsv(industrySectors, "kyndryl-industry-sectors.csv", request);
	}
	
	@DeleteMapping("/kyndryl/industry-sectors")
	public int deleteAllIndustrySectors() {
		return kyndrylDaoService.deleteAll(KyndrylIndustrySectorDim.class);
	}
	
	@DeleteMapping("/kyndryl/industry-sectors/{industry-sector-cd}")
	public int deleteIndustrySectors(@PathVariable(name="industry-sector-cd") String indSctrCd) {
		
		KyndrylIndustrySectorDim kyndrylIndustrySectorDim = new KyndrylIndustrySectorDim(indSctrCd);
		
		return kyndrylDaoService.delete(Arrays.asList(kyndrylIndustrySectorDim));
	}
	
	@PostMapping(value="/kyndryl/industry-sectors/etl-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsIndustrySectorsUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="kyndryl-industry-sectors.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		
		EtlResponse etlResponse = kyndrylDaoService.etl(KyndrylIndustrySectorDim.class
				                                          , fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
	
	/* KYNDRYL_CTRY_IND_SCTR_ASSOC_DIM */
	@GetMapping(path="/kyndryl/ctry-ind-sctrs",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylCtryIndSctrAssocDim> retrieveAllCtryIndSctr(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylCtryIndSctrAssocDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylCtryIndSctrAssocDim.class, filters, pit, size);
		}
	}
	
	@GetMapping(path="/kyndryl/ctry-ind-sctrs/{country-iso-cd}/{industry-sector-cd}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<KyndrylCtryIndSctrAssocDim> retrieveCtryIndSctr(
			  @PathVariable(name="country-iso-cd") String ctryIsoCd
      , @PathVariable(name="industry-sector-cd") String indSctrCd
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException, IOException {
		
		//Bad implementation - add String key to findAll() method strateically
		filters+= (filters.trim().length()>0?" AND ":"")+"CTRY_ISO_CD='"+ctryIsoCd.trim()+"' AND IND_SCTR_CD='"+indSctrCd.trim()+"'";
		
		boolean includeParentage = false;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (includeParentage)
			return kyndrylDaoService.findAllTax(KyndrylCtryIndSctrAssocDim.class, filters, null, size, "WW"); //SP - 2021-09-08
		else {
			if (!useScd) pit=null;
			return kyndrylDaoService.findAll(KyndrylCtryIndSctrAssocDim.class, filters, pit, size);
		}
	}
	
	@GetMapping("/kyndryl/ctry-ind-sctrs/csv")
	public ResponseEntity<Resource> retrieveAllCtryIndSctrCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(required=false, defaultValue="false") @Parameter(description = "Include Point in Time data?") boolean includePit
			, @RequestParam(required=false, defaultValue="") @Parameter(description = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0)") String pit
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /kyndryl/ctry-ind-sctrs
		String orgMarketslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/kyndryl/ctry-ind-sctrs")
                .queryParam("filters", filters)
                .queryParam("includePit", includePit)
                .queryParam("pit", pit)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<KyndrylCtryIndSctrAssocDim[]> responseEntity = new RestTemplate().getForEntity(orgMarketslUri, KyndrylCtryIndSctrAssocDim[].class); //uriVariables); 
		List<KyndrylCtryIndSctrAssocDim> ctryIndSctrAssoc = Arrays.asList(responseEntity.getBody());

		return kyndrylDaoService.returnCsv(ctryIndSctrAssoc, "kyndryl-ctry-ind-sctrs.csv", request);
	}
	
	@DeleteMapping("/kyndryl/ctry-ind-sctrs")
	public int deleteAllCtryIndSctr() {
		return kyndrylDaoService.deleteAll(KyndrylCtryIndSctrAssocDim.class);
	}
	
	@DeleteMapping("/kyndryl/ctry-ind-sctrs/{industry-sector-cd}")
	public int deleteCtryIndSctr(@PathVariable(name="country-iso-cd") String ctryIsoCd
                              ,@PathVariable(name="industry-sector-cd") String indSctrCd) {
		
		KyndrylCtryIndSctrAssocDim kyndrylIndustrySectorDim = new KyndrylCtryIndSctrAssocDim(ctryIsoCd, indSctrCd);
		
		return kyndrylDaoService.delete(Arrays.asList(kyndrylIndustrySectorDim));
	}
	
	@PostMapping(value="/kyndryl/ctry-ind-sctrs/etl-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EtlResponse etl2EdsCtryIndSctrUploads(@RequestParam("oldFile") MultipartFile oldFile
			                , @RequestParam("newFile") MultipartFile newFile
			                , @RequestParam(name="Output File Name",defaultValue="kyndryl-ctry-ind-sctrs.csv") String outputFileNm) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		String oldFileName = fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageService.storeFile(newFile);
		
		EtlResponse etlResponse = kyndrylDaoService.etl(KyndrylCtryIndSctrAssocDim.class
				                                          , fileStorageProperties.getUploadDir()+"/"+oldFileName
				                                          , fileStorageProperties.getUploadDir()+"/"+newFileName
				                                          , 1
				                                          , fileStorageProperties.getUploadDir()+"/"+outputFileNm);
		return etlResponse;
		
	}	
		

}
