package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.wfm.beans.DataSourceDim;
import com.ibm.wfm.beans.SqlParameterDim;
import com.ibm.wfm.beans.SqlTemplateDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.DynamicSqlService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.AsyncHttpServletRequestWrapper;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class DynamicSqlController {
	
	@Autowired
	private DynamicSqlService dmfDaoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/*
	 * Data Sources
	 */
	@GetMapping(path="/dmf/data-sources",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveAllDataSources(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(filters, null, size);
	}	
	
	@GetMapping(path="/dmf/data-sources/{data-source}",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveDataSource(
			  @PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");

		filters+= (filters.trim().length()>0?" AND ":"")+"NAME='"+dataSourceNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(filters, null, size);
	}
	
	@GetMapping(path="/dmf/data-sources/{data-source}/connect",produces = { "application/json", "application/xml"})
	public ResponseEntity<Resource> connectToDataSource(
			  @PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");

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
		
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");
		
		int i =  dmfDaoService.insertAll(Arrays.asList(dataSourceDim));
		if (i==1) return dataSourceDim;
		return null;
		
	}
	
	@DeleteMapping("/dmf/data-sources")
	public int deleteAllDataSources() {
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");
		return dmfDaoService.deleteAll();
	}
	
	@DeleteMapping("/dmf/data-sources/{data-source}")
	public int deleteDataSource(@PathVariable(name="data-source") @Parameter(description = "Data source name") String dataSourceNm) {
		dmfDaoService.setT(DataSourceDim.class);
		dmfDaoService.setTableNm("DMF.DATA_SOURCE_DIM_V");
		dmfDaoService.setScdTableNm("");
		
		DataSourceDim dataSourcDim = new DataSourceDim(dataSourceNm);
		
		return dmfDaoService.delete(Arrays.asList(dataSourcDim));
	}	
	
	/*
	 * SQL Template
	 */
	@GetMapping(path="/dmf/sql-templates",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveAllSqlTemplates(
			  @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(name="result-set-max-size", defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			, @RequestParam(name="order-by", defaultValue = "") @Parameter(description = "Override sort order.") String orderByCols
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");

		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);
		if (orderByCols.trim().length()==0) orderByCols=null;

		return dmfDaoService.findAll(filters, null, size,orderByCols);
	}	
	
	@GetMapping(path="/dmf/sql-templates/{name}",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveSqlTemplate(
			  @PathVariable(name="name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");

		filters+= (filters.trim().length()>0?" AND ":"")+"NAME='"+sqlTemplatedNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(filters, null, size);
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
		
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");

		int i = dmfDaoService.insertAll(Arrays.asList(sqlTemplate));
		if (i==1) return sqlTemplate;
		return null;
		
	}
	
	@PutMapping(value="/dmf/sql-templates", consumes="application/json", produces="application/json")
	public SqlTemplateDim updateSqlTemplate(@RequestBody SqlTemplateDim sqlTemplate) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");

		int i = dmfDaoService.updateAll(Arrays.asList(sqlTemplate));
		if (i==1) return sqlTemplate;
		return null;
		
	}
	
	@DeleteMapping("/dmf/sql-templates")
	public int deleteAllSqlTemplate() {
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");
		return dmfDaoService.deleteAll();
	}
	
	@DeleteMapping("/dmf/sql-templates/{name}")
	public int deleteSqlTemplate(@PathVariable(name="name") @Parameter(description = "SQL Template name") String sqlTemplateNm) {
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_TEMPLATE_DIM_V");
		dmfDaoService.setScdTableNm("");
		
		SqlTemplateDim sqlTemplateDim = new SqlTemplateDim(sqlTemplateNm);
		
		return dmfDaoService.delete(Arrays.asList(sqlTemplateDim));
	}
	
	/*
	 * SQL Parameters
	 */
	@GetMapping(path="/dmf/sql-templates/{sql-template-name}/parameters",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveSqlParametes(
			  @PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(SqlParameterDim.class);
		dmfDaoService.setTableNm("DMF.SQL_PARAMETER_DIM_V");
		dmfDaoService.setScdTableNm("");

		filters+= (filters.trim().length()>0?" AND ":"")+"SQL_TEMPLATE_NM='"+sqlTemplatedNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(filters, null, size);
	}	
	
	@GetMapping(path="/dmf/sql-templates/{sql-template-name}/parameters/{name}",produces = { "application/json", "application/xml"})
	public <T> List<T> retrieveSqlParameter(
			  @PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm
			, @PathVariable(name="name") @Parameter(description = "Parameter Name") String parameterNm
			, @RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters
			, @RequestParam(defaultValue = "All") @Parameter(description = "Size of the results, use ‘ALL’ to get all data.") String resultSetMaxSize
			) throws SQLException, ClassNotFoundException {
		dmfDaoService.setT(SqlTemplateDim.class);
		dmfDaoService.setTableNm("DMF.SQL_PARAMETER_DIM_V");
		dmfDaoService.setScdTableNm("");

		filters+= (filters.trim().length()>0?" AND ":"")+"SQL_TEMPLATE_NM='"+sqlTemplatedNm.trim()+"'" +" AND NAME='"+parameterNm.trim()+"'";
		
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		return dmfDaoService.findAll(filters, null, size);
	}	
	
	@PostMapping(value="/dmf/sql-templates/{sql-template-name}/parameters", consumes="application/json", produces="application/json")
	public SqlParameterDim insertSqlParameter(@RequestBody SqlParameterDim sqlParameter) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		
		dmfDaoService.setT(SqlParameterDim.class);
		dmfDaoService.setTableNm("DMF.SQL_PARAMETER_DIM_V");
		dmfDaoService.setScdTableNm("");

		int  i = dmfDaoService.insertAll(Arrays.asList(sqlParameter));
		if (i==1) return sqlParameter;
		return null;
		
	}
	

	@DeleteMapping("/dmf/sql-templates/{sql-template-name}/parameters")
	public int deleteAllSqlParameters(@PathVariable(name="sql-template-name") @Parameter(description = "SQL Template Name") String sqlTemplatedNm) {
		dmfDaoService.setT(SqlParameterDim.class);
		dmfDaoService.setTableNm("DMF.SQL_PARAMETER_DIM_V");
		dmfDaoService.setScdTableNm("");
		return dmfDaoService.deleteAll("SQL_TEMPLATE_NM='"+sqlTemplatedNm+"'");
	}

	@DeleteMapping("/dmf/sql-templates/{sql-template-name}/parameters/{name}")
	public int deleteSqlParameter(@PathVariable(name="sql-template-name") @Parameter(description = "SQL Template name") String sqlTemplateNm
			, @PathVariable(name="name") @Parameter(description = "Parameter name") String parameterNm) {
		dmfDaoService.setT(SqlParameterDim.class);
		dmfDaoService.setTableNm("DMF.SQL_PARAMETER_DIM_V");
		dmfDaoService.setScdTableNm("");
		
		SqlParameterDim sqlParameterDim = new SqlParameterDim(sqlTemplateNm, parameterNm);
		
		
		return dmfDaoService.delete(Arrays.asList(sqlParameterDim));
	}	

	/*
	 * 	public DB2JSONResultSet getSqlResults(@PathVariable(name="source-name") String sourceNm
			, @PathVariable(name="query-name") String queryNm
			, @RequestParam String... inputParms
			)
		http://localhost:8080/api/v1/dyn/sql/bmsiw/myNfl?inputParms=x%3Dy&inputParms=a%3Db
		
		public DB2JSONResultSet getSqlResults(@PathVariable(name="source-name") String sourceNm
			, @PathVariable(name="query-name") String queryNm
			, @RequestParam(name="parms") Map<String,Object> inputParms
			)
			
		Swagger is looking for an object
	 */

	@CrossOrigin(origins = "http://127.0.0.1:4000")
	@GetMapping(path="/dyn/sql/{data-source-name}/{query-name}",produces = { "application/json"})
	public ResponseEntity getSqlResults(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JsonGenerationException, JsonMappingException, IOException {
		
		int resultSetMaxSize = -1;
		if (resultSetMaxSizeObj!=null) resultSetMaxSize = resultSetMaxSizeObj;
		String[] keys = null;
		if (keyList.trim().length()>0) keys = keyList.split(",");
		
		return dmfDaoService.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, request, toCsv, offset, resultSetMaxSize);

	}
	
	/*
    @GetMapping("/future-skills")
    @Async
    public CompletableFuture<ResponseEntity<?>> getFutureSkills(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JsonGenerationException, JsonMappingException, IOException {
        return CompletableFuture.supplyAsync(() -> {
            //List<FutureSkill> skills = futureSkillsService.getLongRunningQuery();
            //return ResponseEntity.ok(skills);
        	return dmfDaoService.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, request, toCsv, offset, resultSetMaxSize);
        });
    }

	// Assisted by watsonx Code Assistant 
	@GetMapping(path="/dyn/sql/async/{data-source-name}/{query-name}",produces = { "application/json"})
	public ResponseEntity runDynamicSqlAsync(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JsonGenerationException, JsonMappingException, IOException, InterruptedException, ExecutionException {

	    CompletableFuture<ResponseEntity<?>> future = CompletableFuture.supplyAsync(() -> {
	        try {
	    		int resultSetMaxSize = -1;
	    		if (resultSetMaxSizeObj!=null) resultSetMaxSize = resultSetMaxSizeObj;
	    		String[] keys = null;
	    		if (keyList.trim().length()>0) keys = keyList.split(",");
	            return dmfDaoService.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, request, toCsv, offset, resultSetMaxSize);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }
	    });

	    return ResponseEntity.status(HttpStatus.ACCEPTED).body(future);
	}
	*/
	@GetMapping(path="/dyn/sql/async/{data-source-name}/{query-name}",produces = { "application/json"})
	public CompletableFuture<ResponseEntity<?>> runDynamicSqlAsync(@PathVariable(name="data-source-name") String dataSourceNm
			, @PathVariable(name="query-name") String dynamicSqlNm
			, @RequestParam(name="to-csv",defaultValue="false")  @Parameter(description = "Save the results to CSV?") boolean toCsv
			, @RequestParam(name="keys",defaultValue="")  @Parameter(description = "List of taxonomy keys") String keyList
			, @RequestParam(name="offset", defaultValue="-1")  @Parameter(description = "Number of rows ot the result table to skip before any rows are retrieved.") Integer offset
			, @RequestParam(name="result-set-max-size", defaultValue="-1")  @Parameter(description = "Size of the results, use -1 to get all data.") Integer resultSetMaxSizeObj
			, HttpServletRequest request
			) throws SQLException, JsonGenerationException, JsonMappingException, IOException, InterruptedException, ExecutionException {

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


}
