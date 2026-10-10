package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.DataSourceDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.SqlParameterDim;
import com.ibm.wfm.beans.SqlTemplateDim;
import com.ibm.wfm.beans.dmf.AuthorizationsDim;
import com.ibm.wfm.beans.dmf.UserNotificationDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.services.DynamicSqlService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.AsyncHttpServletRequestWrapper;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

@RestController
@RequestMapping("/api/v1")
public class DynamicSqlController {
	
	@Autowired
	private DynamicSqlService dmfDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/*
	 * Data Sources
	 */
	@GetMapping(path="/dmf/data-sources",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<DataSourceDim> retrieveAllDataSources(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(DataSourceDim.class, filters, null, size);
	}	
	
	@GetMapping(path="/dmf/data-sources/{data-source}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<DataSourceDim> retrieveDataSource(
			  @PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		filters+= (filters.trim().length()>0?" AND ":"")+"NAME='"+dataSourceNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(DataSourceDim.class, filters, null, size);
	}
	
	@GetMapping(path="/dmf/data-sources/{data-source}/connect",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> connectToDataSource(
			  @PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm
			) throws SQLException, ClassNotFoundException {

		return dmfDaoService.connectToDatasource(dataSourceNm);
	}
	
	@GetMapping("/dmf/data-sources/csv")
	public ResponseEntity<Resource> retrieveAllDataSourcesCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /eds/brands
		String dataSourcesUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/data-sources")
                .queryParam("filters", filters)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<DataSourceDim[]> responseEntity = new RestTemplate().getForEntity(dataSourcesUri, DataSourceDim[].class); //uriVariables); 
		List<DataSourceDim> dataSources = Arrays.asList(responseEntity.getBody());

		return dmfDaoService.returnCsv(dataSources, "data-sources.csv", request);
	}

	@PostMapping(value="/dmf/data-sources", consumes="application/json", produces="application/json")
	public DataSourceDim insertDataSource(@RequestBody DataSourceDim dataSourceDim) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		
		int i =  dmfDaoService.insertAll(Arrays.asList(dataSourceDim));
		if (i==1) return dataSourceDim;
		return null;
		
	}
	
	@DeleteMapping("/dmf/data-sources")
	public int deleteAllDataSources() {
		return dmfDaoService.deleteAll(SqlTemplateDim.class);
	}
	
	@DeleteMapping("/dmf/data-sources/{data-source}")
	public int deleteDataSource(@PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm) {
		
		DataSourceDim dataSourcDim = new DataSourceDim(dataSourceNm);
		
		return dmfDaoService.delete(Arrays.asList(dataSourcDim));
	}	
	
	/*
	 * SQL Template
	 */
	@GetMapping(path="/dmf/sql-templates",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<SqlTemplateDim> retrieveAllSqlTemplates(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(name="result-set-max-size", defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(name="order-by", defaultValue = "") @Parameter(description = "Override sort order.") String orderByCols
			) throws SQLException, ClassNotFoundException {

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (orderByCols.trim().length()==0) orderByCols=null;

		return dmfDaoService.findAll(SqlTemplateDim.class, filters, null, size,orderByCols);
	}	
	
	@GetMapping(path="/dmf/sql-templates/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<SqlTemplateDim> retrieveSqlTemplate(
			  @PathVariable(name="name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		filters+= (filters.trim().length()>0?" AND ":"")+"NAME='"+sqlTemplatedNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(SqlTemplateDim.class, filters, null, size);
	}	
	
	@GetMapping("/dmf/sql-templates/csv")
	public ResponseEntity<Resource> retrieveSqlTemplatesCsv(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, HttpServletRequest request
			) throws SQLException, ClassNotFoundException, CsvValidationException, IOException, SecurityException {
		
		//Call /eds/brands
		String dataSourcesUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/data-sources")
                .queryParam("filters", filters)
                .queryParam("resultSetMaxSize", resultSetMaxSize)
                .toUriString();
		
		ResponseEntity<DataSourceDim[]> responseEntity = new RestTemplate().getForEntity(dataSourcesUri, DataSourceDim[].class); //uriVariables); 
		List<DataSourceDim> dataSources = Arrays.asList(responseEntity.getBody());

		return dmfDaoService.returnCsv(dataSources, "data-sources.csv", request);
	}
	
	@PostMapping(value="/dmf/sql-templates", consumes="application/json", produces="application/json")
	public SqlTemplateDim insertSqlTemplate(@RequestBody SqlTemplateDim sqlTemplate) throws IllegalArgumentException, IllegalAccessException, SQLException  {

		int i = dmfDaoService.insertAll(Arrays.asList(sqlTemplate));
		if (i==1) return sqlTemplate;
		return null;
		
	}
	
	@PutMapping(value="/dmf/sql-templates", consumes="application/json", produces="application/json")
	public SqlTemplateDim updateSqlTemplate(@RequestBody SqlTemplateDim sqlTemplate) throws IllegalArgumentException, IllegalAccessException, SQLException  {

		int i = dmfDaoService.updateAll(Arrays.asList(sqlTemplate));
		if (i==1) return sqlTemplate;
		return null;
		
	}
	
	@DeleteMapping("/dmf/sql-templates")
	public int deleteAllSqlTemplate() {
		return dmfDaoService.deleteAll(SqlTemplateDim.class);
	}
	
	@DeleteMapping("/dmf/sql-templates/{name}")
	public int deleteSqlTemplate(@PathVariable(name="name") @Parameter(description = "SQL Template name") String sqlTemplateNm) {
		
		SqlTemplateDim sqlTemplateDim = new SqlTemplateDim(sqlTemplateNm);
		
		return dmfDaoService.delete(Arrays.asList(sqlTemplateDim));
	}
	
	/*
	 * SQL Parameters
	 */
	@GetMapping(path="/dmf/sql-templates/{sql-template-name}/parameters",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<SqlParameterDim> retrieveSqlParametes(
			  @PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		filters+= (filters.trim().length()>0?" AND ":"")+"SQL_TEMPLATE_NM='"+sqlTemplatedNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(SqlParameterDim.class, filters, null, size);
	}	
	
	@GetMapping(path="/dmf/sql-templates/{sql-template-name}/parameters/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> List<SqlTemplateDim> retrieveSqlParameter(
			  @PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @PathVariable(name="name") @Parameter(description = "Parameter Name") String parameterNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {

		filters+= (filters.trim().length()>0?" AND ":"")+"SQL_TEMPLATE_NM='"+sqlTemplatedNm.trim()+"'" +" AND NAME='"+parameterNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(SqlTemplateDim.class, filters, null, size);
	}	
	
	@PostMapping(value="/dmf/sql-templates/{sql-template-name}/parameters", consumes="application/json", produces="application/json")
	public SqlParameterDim insertSqlParameter(@RequestBody SqlParameterDim sqlParameter) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		
		int  i = dmfDaoService.insertAll(Arrays.asList(sqlParameter));
		if (i==1) return sqlParameter;
		return null;
		
	}
	

	@DeleteMapping("/dmf/sql-templates/{sql-template-name}/parameters")
	public int deleteAllSqlParameters(@PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm) {

		return dmfDaoService.deleteAll(DataSourceDim.class, "SQL_TEMPLATE_NM='"+sqlTemplatedNm+"'");
	}

	@DeleteMapping("/dmf/sql-templates/{sql-template-name}/parameters/{name}")
	public int deleteSqlParameter(@PathVariable(name="sql-template-name") @Parameter(description = "SQL Template name") String sqlTemplateNm
			, @PathVariable(name="name") @Parameter(description = "Parameter name") String parameterNm) {
		
		SqlParameterDim sqlParameterDim = new SqlParameterDim(sqlTemplateNm, parameterNm);
		
		
		return dmfDaoService.delete(Arrays.asList(sqlParameterDim));
	}	

	@CrossOrigin(origins = "http://127.0.0.1:4000")
	@GetMapping(path="/dyn/sql/{data-source-name}/{query-name}",produces = { "application/json"})
	public ResponseEntity getSqlResults(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JacksonException, IOException {
		
		int resultSetMaxSize = -1;
		if (resultSetMaxSizeObj!=null) resultSetMaxSize = resultSetMaxSizeObj;
		String[] keys = null;
		if (keyList.trim().length()>0) keys = keyList.split(",");
		
		return dmfDaoService.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, request, toCsv, offset, resultSetMaxSize);

	}
	
	@GetMapping(path="/dyn/sql/async/{data-source-name}/{query-name}",produces = { "application/json"})
	public CompletableFuture<ResponseEntity<?>> runDynamicSqlAsync(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JacksonException, IOException, InterruptedException, ExecutionException {

	    // Wrap the original HttpServletRequest before async execution
	    // Capture the current request attributes
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    AsyncHttpServletRequestWrapper requestWrapper = new AsyncHttpServletRequestWrapper(request);


	    return CompletableFuture.supplyAsync(() -> {
	    	// Restore the request attributes in the new thread
	        RequestContextHolder.setRequestAttributes(requestAttributes);
	    	
	        int resultSetMaxSize = (resultSetMaxSizeObj != null) ? resultSetMaxSizeObj : -1;
	        String[] keys = keyList.trim().isEmpty() ? null : keyList.split(",");

	        try {
	            return dmfDaoService.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, requestWrapper, toCsv, offset, resultSetMaxSize);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }
	    });
	}

	
	/**
	 * AUTHORIZATIONS - authorizationss
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param userid - userid PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/dmf/authorizations","/dmf/authorizations/{userid}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllAuthorizationsByCode(
			  @PathVariable(name="userid", required=false) String userid
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
		if (userid!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("userid", userid);
		}
		
	    try {
		
	    	ResponseEntity<Object> response = dmfDaoService.find(AuthorizationsDim.class
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
	    	return toJsonResponse(response);
			
	    } catch (JacksonException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Stored authorization JSON is invalid for userid: " + userid);
	
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving authorization for userid: " + userid + " - " + e.getMessage());
	    }
	}
	
	
	@PostMapping(value="/dmf/authorizationss", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertAuthorizations(@RequestBody AuthorizationsDim authorizations) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return dmfDaoService.insert(authorizations);
	}
	
	@DeleteMapping("/dmf/authorizationss")
	public  ResponseEntity<Integer> deleteAllAuthorizationss(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.delete(AuthorizationsDim.class, filters);
	}
	
	@DeleteMapping("/dmf/authorizationss/{userid}")
	public ResponseEntity<Integer> deleteAuthorizations(@PathVariable(name="userid") @Parameter(description = "Authorizations Code") String userid) throws IllegalArgumentException, IllegalAccessException, SQLException {
		AuthorizationsDim authorizations = new AuthorizationsDim(userid);
		return dmfDaoService.delete(AuthorizationsDim.class, authorizations);
	}	
	
	@PostMapping(value="/dmf/authorizationss/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlAuthorizationss(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="authorizationss.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.etl(AuthorizationsDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/dmf/authorizationss/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlAuthorizationss(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="authorizationss.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.etl(AuthorizationsDim.class, oldFile, newFile, keyLength, outputFileName);
	}
	
	private ResponseEntity<Object> toJsonResponse(
	        ResponseEntity<Object> response
	) throws JacksonException {

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        return response;
	    }

	    Object body = response.getBody();

	    if (body == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // ---------------------------------------------------------------
	    // Single authorization
	    // ---------------------------------------------------------------

	    if (body instanceof AuthorizationsDim authorization) {

	        JsonNode jsonNode = objectMapper.readTree(
	            authorization.getAuthJson()
	        );

	        return ResponseEntity.ok(jsonNode);
	    }

	    // ---------------------------------------------------------------
	    // Multiple authorizations
	    // ---------------------------------------------------------------

	    if (body instanceof List<?> list) {

	        ArrayNode jsonArray =
	            objectMapper.createArrayNode();

	        for (Object item : list) {

	            if (!(item instanceof AuthorizationsDim authorization)) {
	                return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(
	                        "Unexpected authorization response type"
	                    );
	            }

	            JsonNode jsonNode = objectMapper.readTree(
	                authorization.getAuthJson()
	            );

	            jsonArray.add(jsonNode);
	        }

	        return ResponseEntity.ok(jsonArray);
	    }

	    return ResponseEntity
	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
	        .body(
	            "Unexpected authorization response type"
	        );
	}
	

	
	/**
	 * USER_NOTIFICATION - user-notifications
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param userid - userid PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/dmf/user-notifications/{userid}","/dmf/user-notifications/{userid}/{message-id}"},produces = MediaType.APPLICATION_JSON_VALUE)
	public <T> ResponseEntity<Object> retrieveAllUserNotificationByCode(
			  @PathVariable(name="userid") String userid
			, @PathVariable(name="message-id", required=false) String messageId
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
		
		Map<String, Object> pathVarMap = new HashMap<>();
		pathVarMap.put("userid", userid);
		if (messageId!=null) {
			pathVarMap.put("message-id", messageId);
		}
		
		return dmfDaoService.find(UserNotificationDim.class
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
	
	
	@PostMapping(value="/dmf/user-notifications", consumes="application/json", produces="application/json")
	public ResponseEntity<Integer> insertUserNotification(@RequestBody UserNotificationDim userNotification) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return dmfDaoService.insert(userNotification);
	}
	
	@PutMapping(path = { "/dmf/user-notifications/{userid}/read",
			"/dmf/user-notifications/{userid}/{message-id}/read" }, produces = "application/json")
	public ResponseEntity<Integer> markReadUserNotification(@PathVariable(name = "userid") String userid,
			@PathVariable(name = "message-id", required = false) String messageId)
			throws IllegalArgumentException, IllegalAccessException, SQLException {

		String sql = "UPDATE DMF.USER_NOTIFICATION_DIM_V " +
			         "SET READ_IND='Y' " +
			         "WHERE USERID=?";

		if (messageId != null && !messageId.isBlank()) {
		    sql += " AND MESSAGE_ID=?";
		    return dmfDaoService.putUpdateQuery(
		            sql,
		            userid,
		            Integer.valueOf(messageId));
		}
		return dmfDaoService.putUpdateQuery(sql, userid);
	}
	
	@DeleteMapping("/dmf/user-notifications/{userid}")
	public  ResponseEntity<Integer> deleteAllUserNotifications(
			@PathVariable(name = "userid") @Parameter(description = "Userid") String userid
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.deleteBy(UserNotificationDim.class, "USERID", userid);
	}
	
	@DeleteMapping("/dmf/user-notifications/{userid}/{message-id}")
	public ResponseEntity<Integer> deleteUserNotification(
			@PathVariable(name = "userid") @Parameter(description = "Userid") String userid
			, @PathVariable(name = "message-id") @Parameter(description = "messageId") Integer messageId)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		UserNotificationDim userNotification = new UserNotificationDim(userid, messageId);
		return dmfDaoService.delete(UserNotificationDim.class, userNotification);
	}
	
	@PostMapping(value="/dmf/user-notifications/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idlUserNotifications(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="user-notifications.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.etl(UserNotificationDim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/dmf/user-notifications/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etlUserNotifications(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="user-notifications.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return dmfDaoService.etl(UserNotificationDim.class, oldFile, newFile, keyLength, outputFileName);
	}		
	
}
