package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.wfm.beans.ApplicationInfo;
import com.ibm.wfm.beans.BmsOfferingCategoryDim;
import com.ibm.wfm.beans.BmsOfferingComponentDim;
import com.ibm.wfm.beans.BmsOfferingDim;
import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.EdsDaoService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.LadderComparatorService;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class WfmDmfController {
	@Autowired
	private EdsDaoService wfmDmfDaoService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	private ApplicationInfo applicationInfo;
    
    @Autowired
    private ConfigurableEnvironment env;
	
	@GetMapping(path={"/wfm-dmf/application-info","/wfm-dmf/about"}
    , produces = { "application/json", "application/xml"})
    public ResponseEntity<Map<String, String>> getAppInfo() {
		
        String activeProfile = null;
        for (String profile : env.getActiveProfiles()) {
        	if (activeProfile==null) activeProfile="["+profile;
        	else activeProfile+=","+profile;
        }
        if (activeProfile==null) activeProfile="[]";
        else activeProfile+="]";

        //System.out.println("\n=== ALL PROPERTIES LOADED ===");
        //for (PropertySource<?> propertySource : env.getPropertySources()) {
        //    Object source = propertySource.getSource();
        //    if (source instanceof Map) {
        //        ((Map<?, ?>) source).forEach((key, value) -> {
        //            System.out.println(key + "=" + value);
        //        });
        //    }
        //}		
		
		return ResponseEntity.ok().body(Map.of(
				"name", applicationInfo.getName(),
				"description", applicationInfo.getDescription(),
                "version", applicationInfo.getVersion(),
                "springBootVersion", applicationInfo.getSpringBootVersion(),
                "openApiVersion", applicationInfo.getOpenApiVersion(),
                "releaseNotes", applicationInfo.getReleaseNotes(),
                "license", applicationInfo.getLicense(),
                "activeProfiles", activeProfile
				));
    }
	
	@GetMapping(path={"/wfm-dmf/dynamic/sql/async/{data-source-name}/{query-name}"}
    , produces = { "application/json", "application/xml"})
    public ResponseEntity<?> someOtherEndpoint(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="idl",defaultValue="false")  @Parameter(description = "Initial data load?") boolean idl
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JsonGenerationException, JsonMappingException, IOException, InterruptedException, ExecutionException {

		try {
		    CompletableFuture<ResponseEntity<?>> future =
		    		wfmDmfDaoService.runDynamicSqlAsync(dataSourceNm, dynamicSqlNm, true, keyList, offset, resultSetMaxSizeObj, request);

		    // Block until CSV is generated
		    ResponseEntity<?> responseEntity = future.join(); // or future.get()

		    if (responseEntity.getStatusCode().is2xxSuccessful() && 
		        responseEntity.getBody() instanceof Resource resource) {

		        String filename = resource.getFilename();
		        System.out.println("CSV file generated: " + filename);
		        
		        String sourceDirectory = fileStorageProperties.getUploadDir();
		        
		        String lastFilename = filename.substring(0, filename.length() - 4)+"-1.csv";
		        String deltaFilename = filename.substring(0, filename.length() - 4)+"-delta.csv";
		        
		        if (!FileStorageService.fileExists(sourceDirectory+"/"+lastFilename) || idl) {
		        	FileStorageService.renameFile(sourceDirectory+"/"+lastFilename, sourceDirectory+"/"+filename.substring(0, filename.length() - 4)+"-x.csv");
		        	FileStorageService.createEmptyFile(sourceDirectory+"/"+lastFilename);
		        }
		        
		        if (LadderComparatorService.processCsv(sourceDirectory+"/"+lastFilename
		        		                             , sourceDirectory+"/"+filename
		        		                             , 1
		        		                             , sourceDirectory+"/"+deltaFilename)) {
		        	FileStorageService.renameFile(sourceDirectory+"/"+filename, sourceDirectory+"/"+lastFilename);
		            // Load file as Resource
		            Resource returnResource = fileStorageService.loadFileAsResource(deltaFilename);

		            // Try to determine file's content type
		            String contentType = null;

		            contentType = request.getServletContext().getMimeType(returnResource.getFile().getAbsolutePath());

		            // Fallback to the default content type if type could not be determined
		            if(contentType == null) {
		                contentType = "application/octet-stream";
		            }

		            return ResponseEntity.ok()
		                    .contentType(MediaType.parseMediaType(contentType))
		                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + returnResource.getFilename() + "\"")
		                    .body(returnResource);
		        }
		        else {
		        	System.out.println("sucks");
		            ResponseEntity<?> errorResponse = ResponseEntity
		                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                    .body(Map.of(
		                            "error", "Failed to generate CSV",
		                            "statusCode", responseEntity.getStatusCode(),
		                            "message", "Failed to generate CSV"
		                    ));

		            return errorResponse;		        	
		        }
		    } else {
		        System.err.println("Failed to generate CSV: " + responseEntity.getStatusCode());
	            ResponseEntity<?> errorResponse = ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Map.of(
	                            "error", "Failed to generate CSV",
	                            "statusCode", responseEntity.getStatusCode(),
	                            "message", "Failed to generate CSV"
	                    ));

	            return errorResponse;
		    }
		} catch (Exception e) {
		    e.printStackTrace();
            ResponseEntity<?> errorResponse = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to generate CSV",
                            "exception", e.getClass().getName(),
                            "message", e.getMessage()
                    ));

            return errorResponse;

		}
    }
	
	/**
	 * BMS_OFFERING_CATEGORY - bms-offering-categories
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param offeringCatCd - offering-cat-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-dmf/bms-offering-categories","/wfm-dmf/bms-offering-categories/{offering-cat-cd}"}
	                , produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllBmsOfferingCategoryByCode(
			  @PathVariable(name="offering-cat-cd", required=false) String offeringCatCd
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
		if (offeringCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cat-cd", offeringCatCd);
		}
		
		return wfmDmfDaoService.find(BmsOfferingCategoryDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/wfm-dmf/bms-offering-categories/count"},produces = { "application/json", "application/xml"})
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
		return wfmDmfDaoService.countAll(BmsOfferingCategoryDim.class, pathVarMap, filters, includePit, null, false);
	}
	
	
	@PostMapping(value="/wfm-dmf/bms-offering-categories", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBmsOfferingCategory(@RequestBody BmsOfferingCategoryDim fbsBmsOfferingCategory) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmDmfDaoService.insert(fbsBmsOfferingCategory);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offering-categories")
	public  ResponseEntity<Integer> deleteAllBmsOfferingCategorys(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.delete(BmsOfferingCategoryDim.class, filters);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offering-categories/{offering-cat-cd}")
	public ResponseEntity<Integer> deleteBmsOfferingCategory(@PathVariable(name="offering-cat-cd") @Parameter(description = "FBS BmsOfferingCategory Code") String offeringCatCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsOfferingCategoryDim bmsOfferingCategory = new BmsOfferingCategoryDim(offeringCatCd);
		return wfmDmfDaoService.delete(BmsOfferingCategoryDim.class, bmsOfferingCategory);
	}	
	
	@PostMapping(value="/wfm-dmf/bms-offering-categories/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBmsOfferingCategorys(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offering-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingCategoryDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-dmf/bms-offering-categories/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBmsOfferingCategorys(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offering-categories.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingCategoryDim.class, oldFile, newFile, keyLength, outputFileName);
	}		

	/**
	 * BMS_OFFERING - bms-offerings
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param offeringCd - offering-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-dmf/bms-offerings","/wfm-dmf/bms-offerings/{offering-cd}"
	        , "/wfm-dmf/bms-offering-categories/{offering-cat-cd}/bms-offerings", "/wfm-dmf/bms-offering-categories/{offering-cat-cd}/bms-offerings/{offering-cd}"}
			, produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllBmsOfferingByCode(
			  @PathVariable(name="offering-cd", required=false) String offeringCd
			, @PathVariable(name="offering-cat-cd", required=false) String offeringCatCd
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
		if (offeringCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cat-cd", offeringCatCd);
		}
		if (offeringCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cd", offeringCd);
		}
		
		return wfmDmfDaoService.find(BmsOfferingDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/wfm-dmf/bms-offerings/count"},produces = { "application/json", "application/xml"})
	public synchronized ResponseEntity<Count> retrieveCountOfferng(
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
		return wfmDmfDaoService.countAll(BmsOfferingDim.class, pathVarMap, filters, includePit, null, false);
	}	
	
	
	
	@PostMapping(value="/wfm-dmf/bms-offerings", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBmsOffering(@RequestBody BmsOfferingDim fbsBmsOffering) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmDmfDaoService.insert(fbsBmsOffering);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offerings")
	public  ResponseEntity<Integer> deleteAllBmsOfferings(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.delete(BmsOfferingDim.class, filters);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offerings/{offering-cd}")
	public ResponseEntity<Integer> deleteBmsOffering(@PathVariable(name="offering-cd") @Parameter(description = "FBS BmsOffering Code") String offeringCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsOfferingDim bmsOffering = new BmsOfferingDim(offeringCd);
		return wfmDmfDaoService.delete(BmsOfferingDim.class, bmsOffering);
	}	
	
	@PostMapping(value="/wfm-dmf/bms-offerings/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBmsOfferings(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offerings.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-dmf/bms-offerings/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBmsOfferings(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offerings.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingDim.class, oldFile, newFile, keyLength, outputFileName);
	}	
	
	/**
	 * BMS_OFFERING_COMPONENT - bms-offering-components
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param offeringCompCd - offering-comp-cd PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/wfm-dmf/bms-offering-components","/wfm-dmf/bms-offering-components/{offering-comp-cd}"
	        , "/wfm-dmf/bms-offering-categories/{offering-cat-cd}/bms-offerings/{offering-cd}/bms-offering-components"
            , "/wfm-dmf/bms-offering-categories/{offering-cat-cd}/bms-offerings/{offering-cd}/bms-offering-components/{offering-comp-cd}"}
	, produces = { "application/json", "application/xml"})
	public synchronized <T> ResponseEntity<Object> retrieveAllBmsOfferingComponentByCode(
			  @PathVariable(name="offering-cat-cd", required=false) String offeringCatCd
			, @PathVariable(name="offering-cd", required=false) String offeringCd
			, @PathVariable(name="offering-comp-cd", required=false) String offeringCompCd
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
		if (offeringCatCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cat-cd", offeringCatCd);
		}
		if (offeringCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-cd", offeringCd);
		}
		if (offeringCompCd!=null) {
			if (pathVarMap==null) pathVarMap = new HashMap<>();
			pathVarMap.put("offering-comp-cd", offeringCompCd);
		}
		
		return wfmDmfDaoService.find(BmsOfferingComponentDim.class, pathVarMap, filters, orderByCols, includeParentage, null, returnCsv, includePit, pit, offset, resultSetMaxSize, request);
	}
	
	@GetMapping(path={"/wfm-dmf/bms-offering-components/count"},produces = { "application/json", "application/xml"})
	public synchronized ResponseEntity<Count> retrieveCountOfferngComponents(
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
		return wfmDmfDaoService.countAll(BmsOfferingComponentDim.class, pathVarMap, filters, includePit, null, false);
	}	
	
	@PostMapping(value="/wfm-dmf/bms-offering-components", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertBmsOfferingComponent(@RequestBody BmsOfferingComponentDim fbsBmsOfferingComponent) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return wfmDmfDaoService.insert(fbsBmsOfferingComponent);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offering-components")
	public  ResponseEntity<Integer> deleteAllBmsOfferingComponents(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.delete(BmsOfferingComponentDim.class, filters);
	}
	
	@DeleteMapping("/wfm-dmf/bms-offering-components/{offering-comp-cd}")
	public ResponseEntity<Integer> deleteBmsOfferingComponent(@PathVariable(name="offering-comp-cd") @Parameter(description = "FBS BmsOfferingComponent Code") String offeringCompCd) throws IllegalArgumentException, IllegalAccessException, SQLException {
		BmsOfferingComponentDim bmsOfferingComponent = new BmsOfferingComponentDim(offeringCompCd);
		return wfmDmfDaoService.delete(BmsOfferingComponentDim.class, bmsOfferingComponent);
	}	
	
	@PostMapping(value="/wfm-dmf/bms-offering-components/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlBmsOfferingComponents(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offering-components.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingComponentDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/wfm-dmf/bms-offering-components/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlBmsOfferingComponents(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="bms-offering-components.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return wfmDmfDaoService.etl(BmsOfferingComponentDim.class, oldFile, newFile, keyLength, outputFileName);
	}		

}
