---
title: Creating RESTful APIs
permalink: /en/exploring-services/creating-restful-apis
abstract: >- 
  This article will walk you through the process of creating a set of RESTful APIs based on a sample database schema.
---

# Creating RESTful APIs

We'll start with a database schema that has hierarchical relationships. Since it is college football time, we’ll model the NCAA’s Football Bowl Sub-division (FBS) structure that consists of conferences, that contain divisions, which, in turn, has teams.

{: style="text-align: center;" }
![fbs-database-schema]({{ site.baseurl }}/assets/images/docs/fbs-database-schema.png){: style="width:70%;"}

Although this article is not intended to go into the details of how the framework's components cooperate we need just a high level here (see [learning the framework -> Component model](https://pages.github.ibm.com/IBM-Services-WFM/ms-spring-server/en/learning-framework/component-model){:target="_blank"} for details).


![spring-boot-microservice-architecture]({{ site.baseurl }}/assets/images/docs/spring-boot-microservice-architecture.png){: style="width:100%;"}

1. **Controller**s - Define the API endpoints
1. **Service**s - Does the work. Services within the framework subclass `AbstractDaoService` and inherit the functionality required. <br/><br/>The `AbstractDaoService` implements the `DaoInterface` that has a single method, `getConnection()`. This method is responsible to return a JDBC connection to the backend database. Therefore, the only thing that a new service has to do is implement the `getConnection()` method; which it can do using the `java.sql.DriverManager` utility's `getConnection()` method, passing in a JDBC connection string. The configuration for the JDBC connection string is stored in **Configuration** property files, where sensitive information such as userids, passwords, and API keys are substituted at run time with environment variables stored in the Cloud/Cirrus as secretes.
1. **Bean**s - In memory instances of the database rows and columns. The beans are temporary and only exist during the life of the request.



# Defining the Beans (POJOs)

**Note**: The Java code for beans can be 100% generated using the [DAO Artifact Generator services](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/dao-artifact-generator-controller){:target="_blank"}. A simple SQL template is available where tables are defined on one tab and the column definitions are defined on subsequent tabs (if you're defining multiple beans) and the code is generated. This section describes the content of the generated code.

**Annotations** are Java types that are preceded by an `@` symbol. Java has had annotations since the 1.5 release and they have shaped the way applications are designed. Annotation assigns extra metadata to the source code it’s bound to.  The Spring framework and Hibernate are great examples of frameworks that rely heavily on annotations to enable various behaviors.

The following annotations are used within the definition of the beans.

1. `@DbTable`, which define:
    - `baseTableName` - Base Table name. That is, the table without any suffix such as `_DIM`, `_SCD`, `_DIM_V`, or `_SCD_V`
    - `beanName` - Java Bean short name (not required)
    - `parentBaseTableName` - Parent Base Table name. Only required for child/dependent tables. For example, in the FBS Football schema, Division is a child table of 
    Conference and Team is a child table of Division.
    - `parentBeanName` - Parent Java Bean short name. Only required for child/dependent tables.
1. `@DbColumn`, which define:
    - `columnName` - Column name.
    - `isId` - Identifies the column as the surrogate key. The default value is false, so it only needs to be specified for the surrogate key column.
    - `keySeq` - Key sequence of the natural key. This value is an integer where the default = -1 (it can be omitted for columns that don't make up the natural key).
    An integer is used as opposed to an boolean (true/false) to support composite natural keys.
    - `isScd` - Identifies if the column exists to manage a Slow Changing Dimension. The default value is false.
    - `foreignKeySeq` - Key sequence of the foreign key (the link between a child and parent tables). This value is an integer where the default = -1 (it can be omitted for columns that don't make up the foreign key).  An integer is used as opposed to an boolean (true/false) to support composite foreign keys.
    - `assocParentKey` - Similar to the foreign key and is used so that the framework can support many-to-many relationships between entites (that are implmented using an **association** table).
    - `assocChildKey` - Similar to the foreign key and is used so that the framework can support many-to-many relationships between entites (that are implmented using an **association** table).
1. `@ExcelSheet` - Used if data is sourced from an Excel file.  Note that the DAO Artifact Generator services uses these annotation to process the uploaded Excel spreadsheet with table and column definitions.
    - `columnName` - Column name within the spreadsheet tab.
    - `columnNum` - Column number within the spreadsheet tab.  Note that either or both `columnName` and/or `columnNum` may be specified.
    - `ignore` - Directs the framework to not look for the column within the spreadsheet tab.
    
The follow Java code snippet shows the bean generated and used for the Conference table.
<pre name="code" class="java">
@DbTable(beanName="FbsConferenceDim",baseTableName="TEST.FBS_CONFERENCE")
public class FbsConferenceDim extends NaryTreeNode {
	@DbColumn(columnName="CONF_ID",isId=true)
	private int        confId;
	@DbColumn(columnName="CONF_CD",keySeq=1)
	private String     confCd;
	@DbColumn(columnName="CONF_NM")
	private String     confNm;
	...
</pre>

The remainder of the code are basic Java constructors, getters, and setters.  You can see that `CONF_ID` is the generatd surrogate key for the table and `CONF_CD` is the non-composite natural key. Again, it is highly recommended to generate rather than write these beans by hand.

This next Java code snipped shows the bean used for the Division tablee, which is a child of the Conference table.
<pre name="code" class="java">
@DbTable(beanName="FbsDivisionDim",baseTableName="TEST.FBS_DIVISION"
        ,parentBeanName="FbsConferenceDim",parentBaseTableName="TEST.FBS_CONFERENCE")
public class FbsDivisionDim extends NaryTreeNode {
	@DbColumn(columnName="DIV_ID",isId=true)
	private int        divId;
	@DbColumn(columnName="DIV_CD",keySeq=1)
	private String     divCd;
	@DbColumn(columnName="DIV_NM")
	private String     divNm;
	@DbColumn(columnName="CONF_CD",foreignKeySeq=1)
	private String     confCd;
	...
</pre>

Here you see that `CONF_CD` is the foreign key to the parent Conference table.

# Defining the Service

As discussed the Service inherits its functionality from `AbstractDaoService`, so it only needs to implemement the `getConnection()` method.

<pre name="code" class="java">
package com.ibm.wfm.services;

import java.sql.Connection;
import ...

@Component
public class FbsFootballDaoService extends AbstractDaoService {
	private Class t = FbsFootballDim.class
	
	&#64;Autowired
	private FbsDatasourceProperties FbsProp;
	
	public FbsFootballDaoService() {
		super.setT(t);
	}

	&#64;Override
	public Connection getConnection() {
		String jdbcUrlName = FbsProp.getUrl()
		                     .replace("{userid}", System.getenv("fbs-dao-userid"))
		                     .replace("{password}", System.getenv("fbs-dao-password"))
		                     //or
		                     //.replace("{api-key}", System.getenv("fbs-api-key"))
		                     ;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(jdbcUrlName);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}
</pre>

The `FbsDatasourceProperties`, which is `@Autowired` (automatic dependency injection), contains the JDBC URL for the connection with placeholders for confidential information like userid and passwords, which are retrieved from secerets at runtime.

# Define the Controller
**Note**: The Java code for the base API endpoints can be generated using the [DAO Artifact Generator services](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/dao-artifact-generator-controller){:target="_blank"}.

All controllers `@autowire` (dependency injection) a service (subclass of `AbstractDaoService`) to provide the functionality required by the APIs. Also, all controller methods invoked by an API call return a `ResponseEntity` that represents the whole HTTP response: status code, headers, and body. As a result, we can use it to fully configure the HTTP response. 

## API Endpoints

Based on the schema, and the Southeastern Conference, 

{: style="text-align: center;" }
![seca]({{ site.baseurl }}/assets/images/docs/sec.png){: style="width:50%;"}

We should have, at a minmum, the following APIs.

| Usage	                  | URL     |
|:------------------------|:--------|
| All conferences    |    _&lt;host&gt;_/api/v1/fbs-football/conferences |
| Specific conference (_SEC_)    |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_ |
| All divisions    |    _&lt;host&gt;_/api/v1/fbs-football/divisions |
| Specific Division (_EAST_)   |    _&lt;host&gt;_/api/v1/fbs-football/divisions/_EAST_ |
| All divisions in specific conference (_SEC_)    |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/divisions |
| Specific division (_WEST_) in specific conference (_SEC_)  |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/divisions/_WEST_ |
| All teams    |    _&lt;host&gt;_/api/v1/fbs-football/teams |
| Specific team (_ALA_-Alabama)  |    _&lt;host&gt;_/api/v1/fbs-football/teams/_ALA_ |
| All teams in specific conference (_SEC_)   |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/teams |
| Specific team (_ALA_) in specific conference (_SEC_)  |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/teams/_ALA_ |
| All teams in a specific conference (_SEC_) and division (_WEST_)   |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/conferences/_WEST_/teams |
| Specific team (_ALA_) in a specific conference (_SEC_) and division (_WEST_)   |    _&lt;host&gt;_/api/v1/fbs-football/conferences/_SEC_/conferences/_WEST_/teams/_ALA_ |

Along with the paths and path variables, the framework supports multiple optional parameters. 

|   | Name | Description |
|:-:|:-----|:------------|
| 1 | **`filters`** | Filter in format of a SQL WHERE clause using database column names. For example `NICKNAME='Tigers'`. Default="". <br/><br/> |
| 2 | **`orderByCols`** | Order by columns, comma separated using database column names. Default is the result set returned is orders by the table's natural key.<sup>1</sup> |
| 3 | **`includeParentage`** | Return parent taxonomy of object. Omit for root of taxonomy. Default="".<sup>2</sup> |
| 4 | **`returnCsv`** | API to return results as CSV. Default=false. |
| 5 | **`includePit`** | API to include Point in Time (PIT) data. Default=false. |
| 6 | **`pit`** | Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0). Default="" (returns CURRENT state).<sup>3</sup>  <br/><br/>Consumer may also use **`CURRENT TIMESTAMP`** or **`CURRENT TIMESTAMP - _&lt;time unit&gt;_`**, e.g., `current timestamp – 19 hours` |
| 7 | **`resultSetMaxSize`** | Size of the result to be returned, use ‘ALL’ to get all data. Default="All”. |

**Footnotes**
1. Ignored when generating taxonomy (`includeParentage=true`). When full taxonomy requested, `DataManagerType4.getSelectTaxonomyQuery()` generates `ORDER BY` based on the natural keys of the tables starting at the root.
1. Omit from API access point definition for root of the taxonomy
1. Required when looking at point in time (PIT) data (`includePit=true`) within a taxonomy (`includeParentage=true`). If not specified, `CURRENT TIMESTAMP` is used which will return the current state of the taxonomy.

## Controller definition
Now to look at the controller, the Java code includes the controller definition and the definition of the API access points. Starting with the basic definition, you'll seee that the code includes thee Springboot `@RestController` annotation. `@RestController` is a convenience annotation for creating Restful controllers. It is a specialization of `@Component` and is autodetected through classpath scanning. It adds the `@Controller` and `@ResponseBody` annotations, which tells a controller that the object returned is automatically serialized into JSON or XML and passed back into the HttpResponse object.

You'll also see that the Service (`FbsFootballDaoService`) is `@Autowired` (automatic dependency injection) into the controller.

<pre name="code" class="java">
@RestController
@RequestMapping("/api/v1")
public class FbsFootballController {
	
	private static final Logger logger = LoggerFactory.getLogger(FbsFootballController.class);
	
	@Autowired
	private FbsFootballDaoService fbsFootballDaoService;
	...	
</pre> 

## GET/SELECT API endpoints

For defining the GET API Endpoints, the `AbstractDaoService` (that the `FbsFootballService` extended) contains the high level `find()` method. The definition of the endpoints (that can be generated with the [DAO Artifact Generator services](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/dao-artifact-generator-controller){:target="_blank"}) entails
1. Defining the endpoint
1. Setting up the method and the method's parameters
1. Calling the service's `find()` method

To first look at the `find()` method.
<pre name="code" class="java">
public &lt;T&gt; ResponseEntity&lt;Object&gt; find(
  Class type          //The object class to be returned
, Map&lt;String, Object&gt; pathVarMap  //map of all path variables and their values
, String filters      //in format of a SQL WHERE clause using database column names.
, String orderByCols  //comma separated using database column names. 
                      //Default is the result set returned is orders by the table's 
                      //natural key
, boolean includeParentage   //Return parent taxonomy of object. Omit for root of 
                             //taxonomy.
, String topNodeNm    //Use only for taxonomies where there is no natural top node.
, boolean returnCsv   //Tells the API to return results as a CSV file
, boolean includePit  //API to include Point in Time (PIT) data.
, String pit          //Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, 
                      //example = 2021-06-28-00.00.00.0)
, String resultSetMaxSize      //Size of the result to be returned, use ‘ALL’ to 
                               //get all data.
, HttpServletRequest request   //Required to process parameters and to return CSV
) 
throws SQLException, ClassNotFoundException, IOException 
</pre>

### Defining root of a taxonomy

Lets set up the first API endpoint to access Conferences. The first thing we'll do is define the mapping using the `@GetMapping` annotation.  This annotation is for mapping HTTP GET requests onto specific handler methods.  Specifically, `@GetMapping` is a composed annotation that acts as a shortcut for `@RequestMapping(method = RequestMethod.GET)`.

We'll define 2 paths, one without and one with the `conf-cd` path variable. Note that the path variable name must match the database column name or match the column name with underscores (`_`) replaced with hyphens (`-`), and in lower case. Note also that to support both paths, you have set the `@PathVariable` declaration as `required=false`.

Also, since Conference is the root of the taxonomy, the endpoint definition does not include the `includeParentage` parameter and `false` is passed to the service's `find()` method. Likewise `null` is passed to the `find()` method for `topNodeNm`.

<pre name="code" class="java">
@GetMapping(path={"/fbs-football/conferences","/fbs-football/conferences/{conf-cd}"}
           ,produces = { "application/json", "application/xml"})
public &lt;T&gt; ResponseEntity&lt;Object&gt; retrieveAllConferenceByCode(
	  @PathVariable(name="conf-cd", required=false) String confCd
	, @RequestParam(defaultValue = "") 
	  @ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
	  String filters
	, @RequestParam(defaultValue = "") 
	  @ApiParam(value = "Override the SQL ORDER BY clause (default is by natural key).") 
	  String orderByCols
	, @RequestParam(required=false, defaultValue="false") 
	  @ApiParam(value = "Return results as CSV") 
	  boolean returnCsv
	, @RequestParam(required=false, defaultValue="false") 
	  @ApiParam(value = "Include Point in Time data?") 
	  boolean includePit
	, @RequestParam(required=false, defaultValue="") 
	  @ApiParam(value = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.ss...") 
	  String pit
	, @RequestParam(defaultValue = "All") 
	  @ApiParam(value = "Size of the result to be returned, use ‘ALL’ to get all data.") 
	  String resultSetMaxSize
	, HttpServletRequest request
) throws SQLException, ClassNotFoundException, IOException {

	Map&lt;String, Object&gt; pathVarMap = null; 
	if (confCd!=null) {
		pathVarMap = new HashMap<>();
		pathVarMap.put("conf-cd", confCd);
	}
		
	return fbsFootballDaoService.find(FbsConferenceDim.class
	                                , pathVarMap
	                                , filters
	                                , orderByCols
	                                , false  //includeParentage - false since conf is root
	                                , null   //topNodeNm - null since conf is root
	                                , returnCsv
	                                , includePit
	                                , pit
	                                , resultSetMaxSize
	                                , request);
}
</pre>

### Defining leaf of a taxonomy
We'll set up the an API endpoint to access the leaf (lowest node) of the taxonomy, Team. For team, we'll need endpoint definitions that include `@PathVariable`s for the conference and division codes (parent nodes) as well as endpoints for the Team directly.  For just Team, it will look just like Conference but with `includeParentage` as a `@RequestParam`.

<pre name="code" class="java">
@GetMapping(path={"/fbs-football/teams","/fbs-football/teams/{teams-cd}"}
           ,produces = { "application/json", "application/xml"})
public &lt;T&gt; ResponseEntity&lt;Object&gt; retrieveAllTeamByCode(
...
	, @RequestParam(required=false, defaultValue="false") 
	  @ApiParam(value = "Include parent nodes?") 
	  boolean includeParentage
...
</pre>

Finally, we'll configure thee full path of Conference, Division, and Team.  Here we'll have up to 3 `@PathVariable`s, `conf-cd`, `div-cd`, and `team-cd`.
Note too that the framework allows any combination, the only requirement is that the parameter names and values be put into the `Map&lt;String, Object&gt; pathVarMap`. In the current frameworks' sample code, there is a Conference + Team endpoint defined to allow users to get a list of teams within a conference, regardless of Division.

**Important**, when defining an endpoint for access to a node within a taxonomy, if you want to include a `@PathVariable` or allow a filter on a grandparent, great grandparent, or higher, you must include parentage (i.e., do not have `includeParentage` as a parameter and pass `true` to the seervice's `find()` method).

<pre name="code" class="java">
@GetMapping(path={"/fbs-football/conferences/{conf-cd}/divisions/{div-cd}/teams"
		   ,"/fbs-football/conferences/{conf-cd}/divisions/{div-cd}/teams/{team-cd}"}
           ,produces = { "application/json", "application/xml"})
public &lt;T&gt; ResponseEntity&lt;Object&gt; retrieveAllFbsTeamByDivConfCode(
		  @PathVariable(name="conf-cd") String confCd
		, @PathVariable(name="div-cd") String divCd
		, @PathVariable(name="team-cd", required=false) String teamCd
		, @RequestParam(defaultValue = "") 
		  @ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
		  String filters
		//, @RequestParam(defaultValue = "") ... String orderByCols
		//, @RequestParam(required=false, ... boolean includeParentage
		, @RequestParam(required=false, defaultValue="false") 
		  @ApiParam(value = "Return results as CSV") 
		  boolean returnCsv
		, @RequestParam(required=false, defaultValue="false") 
		  @ApiParam(value = "Include Point in Time data?") 
		  boolean includePit
		, @RequestParam(required=false, defaultValue="") 
		  @ApiParam(value = "Specific Point in Time (format: y...") 
		  String pit
		, @RequestParam(defaultValue = "All") 
		  @ApiParam(value = "Size of the results, use ‘ALL’ to get all data.") 
		  String resultSetMaxSize
		, HttpServletRequest request
		) throws SQLException, ClassNotFoundException, IOException {
	
	Map&lt;String, Object&gt; pathVarMap = new HashMap<>();
	pathVarMap.put("conf-cd", confCd);
	pathVarMap.put("div-cd", divCd);
	if (teamCd!=null)
		pathVarMap.put("team-cd", teamCd);
	
	return fbsFootballDaoService.find(FbsTeamDim.class
	                                , pathVarMap
	                                , filters
	                            , null    //orderBy (natural keys from root down)
	                            , true    //includeParentage 
	                            , null    //topNodeNm (top node will be conference)
	                                , returnCsv
	                                , includePit
	                                , pit
	                                , resultSetMaxSize
	                                , request);
}
</pre>

			                            
## POST/INSERT API

## DELETE APIs

## Initial Data Load (IDL) API

## Extract, Transformation, &amp; Load (ETL/Delta) API
