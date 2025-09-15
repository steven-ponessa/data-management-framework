<#assign keyColNm = "">
<#assign keyVarNm = "">
<#assign keyParmNm = "">
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq == 1>
		<#assign keyColNm = dbcolumn.name>
		<#assign keyParmNm = dbcolumn.nameParmCase>
		<#assign keyVarNm = dbcolumn.nameVarCase>
	</#if>
</#list>

	
	/**
	 * ${db2table.name} - ${db2table.nameParmCasePlural}
	 */
	
	/**
	 * 
	 * @param <T> - Object type being retrieved/returned.
	 * @param ${keyVarNm} - ${keyParmNm} PathVariable. Note that the path variable name must match the table column name.
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
	@GetMapping(path={"/${db2table.groupNm}/${db2table.nameParmCasePlural}","/${db2table.groupNm}/${db2table.nameParmCasePlural}/{${keyParmNm}}"},produces = { "application/json", "application/xml"})
	public <T> ResponseEntity<Object> retrieveAll${db2table.nameProperCase}ByCode(
			  @PathVariable(name="${keyParmNm}", required=false) String ${keyVarNm}
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
		if (${keyVarNm}!=null) {
			pathVarMap = new HashMap<>();
			pathVarMap.put("${keyParmNm}", ${keyVarNm});
		}
		
		return ${db2table.groupNmVariableCase}DaoService.find(${db2table.nameProperCase}Dim.class
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
	
	
	@PostMapping(value="/${db2table.groupNm}/${db2table.nameParmCasePlural}", consumes="application/json", produces="application/json")
	public ResponseEntity<${db2table.nameProperCase}Dim> insert${db2table.nameProperCase}(@RequestBody ${db2table.nameProperCase}Dim ${db2table.nameProperCase}) throws IllegalArgumentException, IllegalAccessException, SQLException  {
		return ${db2table.groupNmVariableCase}DaoService.insert(fbs${db2table.nameProperCase});
	}
	
	@DeleteMapping("/${db2table.groupNm}/${db2table.nameParmCasePlural}")
	public  ResponseEntity<Integer> deleteAll${db2table.nameProperCase}s(@RequestParam(defaultValue = "") @Parameter(description = "Add filter in format of a SQL WHERE clause.") String filters) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return ${db2table.groupNmVariableCase}DaoService.delete(${db2table.nameProperCase}Dim.class, filters);
	}
	
	@DeleteMapping("/${db2table.groupNm}/${db2table.nameParmCasePlural}/{${keyParmNm}}")
	public ResponseEntity<Integer> delete${db2table.nameProperCase}(@PathVariable(name="${keyParmNm}") @Parameter(description = "${db2table.nameProperCase} Code") String ${keyVarNm}) throws IllegalArgumentException, IllegalAccessException, SQLException {
		${db2table.nameProperCase}Dim ${db2table.nameVariableCase} = new ${db2table.nameProperCase}Dim(${keyVarNm});
		return ${db2table.groupNmVariableCase}DaoService.delete(${db2table.nameProperCase}Dim.class, ${db2table.nameVariableCase});
	}	
	
	@PostMapping(value="/${db2table.groupNm}/${db2table.nameParmCasePlural}/idl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> idl${db2table.nameProperCase}s(@RequestParam("new-file") MultipartFile newFile
							, @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="${db2table.nameParmCasePlural}.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ${db2table.groupNmVariableCase}DaoService.etl(${db2table.nameProperCase}Dim.class, null, newFile, keyLength, outputFileName);
	}

	@PostMapping(value="/${db2table.groupNm}/${db2table.nameParmCasePlural}/etl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EtlResponse> etl${db2table.nameProperCase}s(@RequestParam("old-file") MultipartFile oldFile
			                , @RequestParam("new-file") MultipartFile newFile
			                , @RequestParam(name="key-length",defaultValue="1") int keyLength
			                , @RequestParam(name="output-file-name",defaultValue="${db2table.nameParmCasePlural}.csv") String outputFileName) throws CsvValidationException, IOException, EtlException, IllegalArgumentException, IllegalAccessException, SQLException {
		return ${db2table.groupNmVariableCase}DaoService.etl(${db2table.nameProperCase}Dim.class, oldFile, newFile, keyLength, outputFileName);
	}	