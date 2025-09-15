---
title: Component Model
permalink: en/learning-framework/component-model
features:
  - mermaid
  - zoom
abstract: >- # this means to ignore newlines until "baseurl:"
  The component model emphasizes the separation of concerns with respect to the functionality available within the framework. It is a reuse-based approach to defining, implementing and composing loosely coupled independent components into the system. An individual software component is a package, a web service, a web resource, or a module that encapsulates a set of related functions (or data).  All system processes are placed into separate components so that all of the data and functions inside each component are semantically related (just as with the contents of classes). Because of this principle, it is often said that components are modular and cohesive.
---

# WFM-DMF Component Model

![Spring-Boot Microservice Architecture]({{ site.baseurl }}/assets/images/docs/spring-boot-microservice-architecture.png){:class="zoom"}

[![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/spring-boot-microservice-architecture.png){: target="_blank"}

| **Component** | **Description** |
|-|-|
| **Application** | The primary entry point of the application is structured around a `@SpringBootApplication`. This annotation is the core of the Spring Boot framework, enabling auto-configuration, component scanning, and Spring application context setup. |
| **Controllers** | Define API endpoints and handle incoming HTTP requests, invoke the appropriate service layer methods, and return the responses. |
| **Services**| Handles the business logic for data operations and acts as the bridge between the Controller layer and the database. It contains a key abstract class, AbstractDaoService, and specific service classes tailored for each business area. |
| **Annotations** | Facilitates Object-Relational Mapping (ORM) and metadata management for database interactions and Excel-based utilities. | 
| **Beans** | Plain Old Java Objects (POJOs) that are annotated with the custom annotations from the Annotations package. These POJOs are utilized for ORM and represent rows from database tables. |
| **Configurations** | Responsible for setting up the application environment, including database connectivity, security, and other necessary configurations. |
| **Utilities** | Various utility classes that support the core operations of the framework. |
| **Exceptions** | Handles exception management across the application. |


# Application

**Application.java**

The primary entry point of the application is structured around a `@SpringBootApplication`. This annotation is the core of the Spring Boot framework, enabling auto-configuration, component scanning, and Spring application context setup. The `main` application class initiates the service components, database connections, and API layer. It is responsible for:
- **Bootstrapping** the Spring application context.
- **Auto-configuring** required Spring components.
- **Enabling component scanning** to discover and register beans, controllers, services, and other components.
  
The `@SpringBootApplication` class handles dependency injection across the application's components, ensuring that all dependencies are correctly managed and initialized when the application starts.

<pre name="code" class="java">
@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class Application {
	
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private SwaggerProperties swaggerProperties;

	public static void main(String[] args) {
		
		SpringApplication application = new SpringApplication(Application.class);
		ApplicationContext applicationContext = application.run( args);
	
	}
	
	/*
	 * Enables CORS requests from any origin to any endpoint in the application.
	 * To lock this down a bit more, the `registry.addMapping()` method returns a 
	 * `CorsRegistration` object, which we can use for additional configuration. 
	 * There’s also an `allowedOrigins()` method that lets us specify an array of 
	 * allowed origins. This can be useful if we need to load this array from an 
	 * external source at runtime.
	 * 
	 * Additionally, there are `allowedMethods`, `allowedHeaders`, `exposedHeaders`,
	 * `maxAge` and `allowCredentials` that we can use to set the response 
	 * headers and customization options.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
			
		    @Override
		    public void configureContentNegotiation(ContentNegotiationConfigurer 
		                                            configurer){
		        configurer.defaultContentType( MediaType.APPLICATION_JSON );
		    }
		};
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public Docket produceApi() {
	    return new Docket(DocumentationType.SPRING_WEB)
	            .apiInfo(apiInfo())
	            .select()       
	            .paths(PathSelectors.regex("/api.*"))
	            .build();
	}

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName()
                                   , swaggerProperties.getContactUrl()
                                   , swaggerProperties.getContactEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .version(swaggerProperties.getVersion())
                .build();
    }
    
}
</pre>

Here's a detailed explanation of the provided code, including its purpose, individual components, and how it functions in the context of a Spring Boot application:

## **Overview**
The code defines the main class of a Spring Boot application. This class is responsible for configuring and starting the application, enabling CORS (Cross-Origin Resource Sharing), setting up Swagger for API documentation, and providing a REST client (`RestTemplate`) for making HTTP requests. Let's go through each section in detail:

## **Key Components**

1. **Class-Level Annotations**
   
   <pre name="code" class="java">
   @SpringBootApplication
   @EnableSwagger2
   @EnableConfigurationProperties({
       FileStorageProperties.class
   })
   </pre>
   
   - **`@SpringBootApplication`**: This is a convenience annotation that combines three commonly used Spring annotations:
     - **`@Configuration`**: Marks the class as a source of bean definitions for the application context.
     - **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration mechanism.
     - **`@ComponentScan`**: Allows Spring to scan the package for components, configurations, and services.
   
   - **`@EnableSwagger2`**: Activates Swagger 2 for the application, enabling API documentation generation for the REST endpoints. Swagger is a popular tool for documenting APIs, allowing developers to see the available endpoints, their parameters, and the expected output in a UI.

   - **`@EnableConfigurationProperties({FileStorageProperties.class})`**: Enables support for the `FileStorageProperties` configuration class. This class typically contains properties that are loaded from a configuration file (like `application.yml` or `application.properties`) and allows for binding them to a strongly typed bean (`FileStorageProperties`).

2. **Logger Initialization**
   
   <pre name="code" class="java">
   private static Logger logger = LoggerFactory.getLogger(Application.class);
   </pre>
   
   - This line initializes a **Logger** using `LoggerFactory` from SLF4J (Simple Logging Facade for Java). It allows logging messages for debugging and tracking application flow. The logger is associated with the `Application` class, making it easy to generate log messages specific to this class.

3. **Autowired Properties**

   <pre name="code" class="java">
   @Autowired
   private SwaggerProperties swaggerProperties;
   </pre>
   
   - **`@Autowired`**: This annotation injects a `SwaggerProperties` bean, which contains custom configuration properties for Swagger (such as title, description, version, contact information, etc.). These properties are used to configure the Swagger UI.

4. **Main Method**
   
   <pre name="code" class="java">
   public static void main(String[] args) {
       SpringApplication application = new SpringApplication(Application.class);
       ApplicationContext applicationContext = application.run(args);
   }
   </pre>
   
   - The `main` method serves as the entry point of the Java application. It uses `SpringApplication.run()` to launch the Spring Boot application.
   - **`SpringApplication`**: This class provides a convenient way to bootstrap the application, starting the embedded web server (e.g., Tomcat) and initializing the Spring context.
   - **`ApplicationContext`**: The `ApplicationContext` instance manages all Spring beans, handling their lifecycle and dependencies.

5. **CORS Configuration**

   <pre name="code" class="java">
   @Bean
   public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry registry) {
               registry.addMapping("/**");
           }

           @Override
           public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
               configurer.defaultContentType(MediaType.APPLICATION_JSON);
           }
       };
   }
   </pre>
   
   - This method returns an instance of `WebMvcConfigurer`, a Spring interface for customizing MVC configuration.
   - **`addCorsMappings(CorsRegistry registry)`**: Configures Cross-Origin Resource Sharing (CORS) settings for the application. 
     - **`registry.addMapping("/**")`**: Allows CORS requests from any origin to all endpoints (`/**`) within the application. This is useful during development but can be restricted for production by specifying allowed origins (e.g., specific domains).
   - **`configureContentNegotiation(ContentNegotiationConfigurer configurer)`**: Sets the default content type to `application/json` for API responses. This ensures that JSON is the default response format unless otherwise specified.

6. **RestTemplate Bean**

   <pre name="code" class="java">
   @Bean
   public RestTemplate getRestTemplate() {
       return new RestTemplate();
   }
   </pre>
   
   - This method creates and returns a **`RestTemplate`** bean, which is a Spring utility for performing HTTP requests. It's a client-side component used to consume RESTful APIs, handle responses, and manage HTTP requests.

7. **Swagger Configuration**

   <pre name="code" class="java">
   @Bean
   public Docket produceApi() {
       return new Docket(DocumentationType.SPRING_WEB)
               .apiInfo(apiInfo())
               .select()       
               .paths(PathSelectors.regex("/api.*"))
               .build();
   }
   </pre>
   
   - **`Docket`**: A primary interface for configuring Swagger in a Spring Boot application. It defines the documentation type (`SPRING_WEB`), specifies the API endpoints to include (using a regular expression `"/api.*"` to match all paths starting with `/api`), and sets the metadata using the `apiInfo()` method.
   - **`apiInfo()`**: A helper method that creates an `ApiInfo` object, which holds API metadata (title, description, contact information, version, etc.). This data is displayed in the Swagger UI.

   <pre name="code" class="java">
   private ApiInfo apiInfo() {
       return new ApiInfoBuilder()
               .title(swaggerProperties.getTitle())
               .description(swaggerProperties.getDescription())
               .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
               .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
               .version(swaggerProperties.getVersion())
               .build();
   }
   </pre>

   - **`ApiInfo`**: Provides metadata about the API. `ApiInfoBuilder` is a builder class used to set the title, description, contact details, terms of service, and version. The values for these fields are pulled from the `SwaggerProperties` bean, which contains the configuration.

## **Purpose of the Code**
The primary purpose of this code is to set up a Spring Boot application that:
- Configures global CORS settings, allowing or restricting access to APIs from different domains.
- Initializes a `RestTemplate` for making outbound HTTP requests.
- Integrates Swagger for documenting and visualizing APIs, enabling easy testing and exploration of available endpoints.
- Uses `SwaggerProperties` for configuring metadata related to the API documentation dynamically.

This setup creates a robust, developer-friendly environment for building and consuming APIs with clear documentation, flexible CORS handling, and easy HTTP communication between microservices.

# Controllers     
The controller layer binds everything together right from the moment a request is intercepted till the response is prepared and sent back. This package defines the RESTful API endpoints using Spring's `@RestController` annotation. Controllers handle incoming HTTP requests, invoke the appropriate service layer methods, and return the responses.  The controller layer is present in the controller package, the best practices suggest that we keep this layer versioned to support multiple versions of the application and the same practice is applied here.

A controller within the framework,

1. Defines the **API endpoints and parameters**. Each controller class contains endpoint methods annotated with `@GetMapping`, `@PostMapping`, `@PutMapping`, and `@DeleteMapping`. These methods are the interface for CRUD operations against backend relational databases. The framework supports complex hierarchical data structures with endpoints like:
  ```
  api/v1/resources-level-1/{resource-level-1-key}/resources-level-2/{resource-level-2-key}/resources-level-3/{resource-level-3-key}
  ```
1. Calls the approriate **Service** (for CRUD, IDL, ETL, or Data Quality)
1. Returns the data returned from the Service within a **Response Entity**
1. Any exceptions thrown during the API's lifecycle are caught and handled by the **`@ControllerAdvice`**

Below us a sample snippet from the `EdsDaoJrsTaxonomyController` class which is a Spring Boot `@RestController` that handles CRUD (Create, Read, Update, Delete) operations for managing entities related to "growth platforms" in a taxonomy. 

<pre name="code" class="java">
@RestController
@RequestMapping("/api/v1")
public class EdsDaoJrsTaxonomyController {

	@Autowired
	private EdsDaoService edsDaoService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	...

	/**
	 * GROWTH_PLATFORM - growth-platforms
	 */
	/**
	 * 
	 * @param &lt;T&gt;              - Object type being retrieved/returned.
	 * @param growthPlatformCd - growth-platform-cd PathVariable. Note that the path
	 *                         variable name must match the table column name.
	 * @param filters          - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols      - Order by columns. Default="" (orders by natural
	 *                         key).
	 * @param includeParentage - Return parent taxonomy of object. Omit for root of
	 *                         taxonomy. Default="".
	 * @param returnCsv        - API to return results as CSV. Default=false.
	 * @param includePit       - API to include Point in Time (PIT) data.
	 *                         Default=false.
	 * @param pit              - Specific Point in Time (format:
	 *                         yyyy-MM-dd-hh.mm.ss.sssssss, example =
	 *                         2021-06-28-00.00.00.0). Default="" (return CURRENT
	 *                         state).
	 * @param resultSetMaxSize - Size of the result to be returned, use ‘ALL’ to get
	 *                         all data. Default="All".
	 * @param request          - HttpServletRequest required to return CSV.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GetMapping(path = { "/eds-ut-jrs-tax/growth-platforms"
	                   , "/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}" }
	                   , produces = { "application/json", "application/xml" })
	public synchronized &lt;T&gt; ResponseEntity&lt;Object&gt; retrieveAllGrowthPlatformByCode(
			  @PathVariable(name = "growth-platform-cd", required = false) 
			  String growthPlatformCd
			, @RequestParam(defaultValue = "") 
			  @ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
			  String filters
			, @RequestParam(defaultValue = "") 
			  @ApiParam(value="Override the SQL ORDER BY clause (default=natural key).")
			  String orderByCols
			, @RequestParam(required = false, defaultValue = "false") 
			  @ApiParam(value = "Include parent nodes?") 
			  boolean includeParentage
			, @RequestParam(required = false, defaultValue = "false") 
			  @ApiParam(value = "Return results as CSV") 
			  boolean returnCsv
			, @RequestParam(required = false, defaultValue = "false") 
			  @ApiParam(value = "Include Point in Time data?") 
			  boolean includePit
			, @RequestParam(required = false, defaultValue = "") 
			  @ApiParam(value = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.....")
			  String pit
			, @RequestParam(required = false, defaultValue = "false") 
			  @ApiParam(value = "Include only EDS data?") 
			  boolean edsOnly
			, @RequestParam(defaultValue = "All") 
			  @ApiParam(value = "Size of the results, use ‘ALL’ to get all data.") 
			  String resultSetMaxSize
			, HttpServletRequest request) 
			  throws SQLException, ClassNotFoundException, IOException {
		Map&lt;String, Object&gt; pathVarMap = null;
		if (growthPlatformCd != null) {
			pathVarMap = new HashMap&lt;&gt;();
			pathVarMap.put("growth-platform-cd", growthPlatformCd);
		}
		return edsDaoService.find(GrowthPlatformDim.class
		                        , pathVarMap
		                        , filters
		                        , orderByCols
		                        , includeParentage
		                        , false     //selfReferencing
		                        , null      //topNodeNm
		                        , returnCsv
		                        , includePit
		                        , pit
		                        , edsOnly
		                        , resultSetMaxSize
		                        , request);
	}

	@GetMapping(path = { "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms"
	       , "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}"}
	       , produces = {"application/json", "application/xml" })
	public synchronized &lt;T&gt; ResponseEntity&lt;Object&gt; 
	                              retrieveAllGrowthPlatformByBrandGrowthPlatformCode(
			@PathVariable(name = "growth-platform-cd", required = false) 
			String growthPlatformCd
	              , @PathVariable(name = "brand-cd") String brandCd
	              , ... see above ...) 
	              throws SQLException, ClassNotFoundException, IOException {
		Map&lt;String, Object&gt; pathVarMap = new HashMap&lt;&gt;();
		pathVarMap.put("brand-cd", brandCd); // brand-cd is required
		if (growthPlatformCd != null) {
			pathVarMap.put("growth-platform-cd", growthPlatformCd);
		}
		return edsDaoService.find(GrowthPlatformDim.class, pathVarMap
		                        , ... see above ...);
	}

	@PostMapping(value = "/eds-ut-jrs-tax/growth-platforms"
	           , consumes = "application/json"
	           , produces = "application/json")
	public ResponseEntity&lt;GrowthPlatformDim&gt; insertGrowthPlatform(
	             @RequestBody GrowthPlatformDim fbsGrowthPlatform)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.insert(fbsGrowthPlatform);
	}

	@DeleteMapping("/eds-ut-jrs-tax/growth-platforms")
	public ResponseEntity&lt;Integer&gt; deleteAllGrowthPlatforms(
			@RequestParam(defaultValue = "") 
			@ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
			String filters)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.delete(GrowthPlatformDim.class, filters);
	}
	
	@DeleteMapping("/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}")
	public ResponseEntity&lt;Integer&gt; deleteGrowthPlatform(
			@PathVariable(name = "growth-platform-cd") 
			@ApiParam(value = "FBS GrowthPlatform Code") 
			String growthPlatformCd)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		GrowthPlatformDim growthPlatform = new GrowthPlatformDim(growthPlatformCd);
		return edsDaoService.delete(GrowthPlatformDim.class, growthPlatform);
	}

	@PostMapping("/eds-ut-jrs-tax/growth-platforms/idl")
	public ResponseEntity&lt;EtlResponse&gt; idlGrowthPlatforms(
	       @RequestParam("new-file") MultipartFile newFile
	     , @RequestParam(name = "key-length", defaultValue = "1") int keyLength
	     , @RequestParam(name = "output-file-name"
	                   , defaultValue = "growth-platforms.csv") 
	     , String outputFileName)
	       throws CsvValidationException, IOException, EtlException
	         , IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(GrowthPlatformDim.class
		                       , null      //old data file
		                       , newFile
		                       , keyLength
		                       , outputFileName);
	}
	
	@PostMapping("/eds-ut-jrs-tax/growth-platforms/etl")
	public ResponseEntity&lt;EtlResponse&gt; etlGrowthPlatforms(
	       @RequestParam("old-file") MultipartFile oldFile
	     , @RequestParam("new-file") MultipartFile newFile
	     , @RequestParam(name = "key-length", defaultValue = "1") int keyLength
	     , @RequestParam(name = "output-file-name"
	                   , defaultValue = "growth-platforms.csv") 
	     , String outputFileName)
	       throws CsvValidationException, IOException, EtlException
	         , IllegalArgumentException, IllegalAccessException, SQLException {
		return edsDaoService.etl(GrowthPlatformDim.class
		                       , oldFile
		                       , newFile
		                       , keyLength
		                       , outputFileName);
	}
 //... Mappings for other tables ...
</pre>

Below is a detailed breakdown of the key components, methods, and their responsibilities:

## **Overview**

This controller defines a RESTful API that interacts with "growth platforms" data stored in a database, organized in a hierarchy involving "brands". It relies on several services to perform database operations, data uploads, and transformations, specifically leveraging the `EdsDaoService` for most of the data handling logic. It includes various HTTP methods (GET, POST, DELETE) and uses annotations to define API endpoints, making it simple to retrieve, insert, and delete data while managing parameters like filters, ordering, and even performing ETL (Extract, Transform, Load) operations.

### Defines the API endpoints and parameters
Spring 4.0 introduced the **`@RestController`** annotation in order to simplify the creation of RESTful web services. It's a convenient annotation that combines `@Controller` and `@ResponseBody`, which eliminates the need to annotate every request handling method of the controller class with the `@ResponseBody` annotation.  We typically use `@Controller` and `@RestController` in combination with a `@RequestMapping` annotation for request handling methods.

`@GetMapping` is a specialized version of `@RequestMapping` annotation that acts as a shortcut for `@RequestMapping(method = RequestMethod.GET)`. `@PostMapping` is also a specialized version of `@RequestMapping` annotation that acts as a shortcut for `@RequestMapping(method = RequestMethod.POST)`.

In the Java example below a `POST` request of `/api/v1/taxonomyEvaluator` would invoke the `taxonomyEvaluator()` method.  The `@RequestParam` annotation  extracts query parameters, form parameters, and even files from the request.  Method parameters annotated with `@RequestParam` are required by default.  This means that if the parameter isn’t present in the request, we'll get an 400 Bad Request error.  We can configure any `@RequestParam` to be optional, though, with the `required=false` attribute.  You can also specify a default value using the `defaultValue` attribute.  Finally, to store the uploaded file we can use the **`MultipartFile`** variable.

### Returns a Response Entity
All controllers within the framework will return a **`ResponseEntity<T>`**. A ResponseEntity is an extension of `HttpEntity` that represents an HTTP response including status, headers and body. ResponseEntity allows you to modify the response with optional headers and status code. For example, for POST/inserts, the framework can return a response with `HttpStatus.CREATED` instead of the more generic `HttpStatus.OK`.

So, if the controller returned a bean (e.g., `FsbFootball`) or a collection of beans (e.g., `List<FbsFootball>`), these can be wrapped in a ResponseEntity, e.g., `ResponseEntity<List<FsbFootball>>`.

## **Class Annotations and Dependency Injection**

<pre name="code" class="java">
@RestController
@RequestMapping("/api/v1")
public class EdsDaoJrsTaxonomyController {
</pre>

- **`@RestController`**: Marks the class as a Spring MVC controller, indicating that it can handle HTTP requests. It is a specialized version of `@Controller` that includes `@ResponseBody`, which automatically serializes responses to JSON/XML format.
- **`@RequestMapping("/api/v1")`**: Sets the base path for all endpoints in this controller to `/api/v1`. All routes defined in this class will be prefixed with `/api/v1`.

<pre name="code" class="java">
@Autowired
private EdsDaoService edsDaoService;

@Autowired
private FileStorageService fileStorageService;

@Autowired
private FileStorageProperties fileStorageProperties;
</pre>

- **`@Autowired`**: Injects dependencies for services used within the controller.
  - **`edsDaoService`**: A service handling CRUD operations for data related to growth platforms.
  - **`fileStorageService`**: Manages file uploads and storage, possibly for ETL processing.
  - **`fileStorageProperties`**: Contains configuration properties related to file storage (e.g., file paths or sizes).

## **GET Methods for Retrieving Data**

### 1. **Retrieving Growth Platforms by Code**

<pre name="code" class="java">
@GetMapping(path = { "/eds-ut-jrs-tax/growth-platforms"
                   , "/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}" }
                   , produces = { "application/json", "application/xml" })
public synchronized &lt;T&gt; ResponseEntity&lt;Object&gt; retrieveAllGrowthPlatformByCode(
		  @PathVariable(name = "growth-platform-cd", required = false) 
		  String growthPlatformCd
		, @RequestParam(defaultValue = "") 
		  @ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
		  String filters
		, @RequestParam(defaultValue = "") 
		  @ApiParam(value="Override the SQL ORDER BY clause (default=natural key).")
		  String orderByCols
		, @RequestParam(required = false, defaultValue = "false") 
		  @ApiParam(value = "Include parent nodes?") 
		  boolean includeParentage
		, @RequestParam(required = false, defaultValue = "false") 
		  @ApiParam(value = "Return results as CSV") 
		  boolean returnCsv
		, @RequestParam(required = false, defaultValue = "false") 
		  @ApiParam(value = "Include Point in Time data?") 
		  boolean includePit
		, @RequestParam(required = false, defaultValue = "") 
		  @ApiParam(value = "Specific Point in Time (format: yyyy-MM-dd-hh.mm.....")
		  String pit
		, @RequestParam(required = false, defaultValue = "false") 
		  @ApiParam(value = "Include only EDS data?") 
		  boolean edsOnly
		, @RequestParam(defaultValue = "All") 
		  @ApiParam(value = "Size of the results, use ‘ALL’ to get all data.") 
		  String resultSetMaxSize
		, HttpServletRequest request) 
		  throws SQLException, ClassNotFoundException, IOException {
</pre>

- **`@GetMapping`**: Handles HTTP GET requests. This method supports two endpoint paths:
  - `/eds-ut-jrs-tax/growth-platforms`: Retrieves all growth platforms.
  - `/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}`: Retrieves a specific growth platform by code.
- **Parameters**:
  - **`@PathVariable` growthPlatformCd**: A path variable that represents the specific code for a growth platform. It is optional, allowing the method to be flexible for both retrieving a list or a single entity.
  - **`@RequestParam` filters**: A SQL-like `WHERE` clause filter for querying the database.
  - **`orderByCols`**: Customizes the SQL `ORDER BY` clause.
  - **`includeParentage`**: A boolean indicating whether to include parent nodes (for hierarchical data).
  - **`returnCsv`**: A flag to return data as a CSV file.
  - **`includePit`** and **`pit`**: Control for "Point in Time" data retrieval, allowing historical or current data selection.
  - **`resultSetMaxSize`**: Limits the number of results, with the default being all records.
- **Return**: It calls `edsDaoService.find()` to retrieve the data, returning a `ResponseEntity` that includes the result in JSON or XML format.

### 2. **Retrieving Growth Platforms by Brand and Code**

<pre name="code" class="java">
@GetMapping(path = { "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms"
       , "/eds-ut-jrs-tax/brands/{brand-cd}/growth-platforms/{growth-platform-cd}"}
       , produces = {"application/json", "application/xml" })
public synchronized &lt;T&gt; ResponseEntity&lt;Object&gt; 
                              retrieveAllGrowthPlatformByBrandGrowthPlatformCode(
		@PathVariable(name = "growth-platform-cd", required = false) 
		String growthPlatformCd
              , @PathVariable(name = "brand-cd") String brandCd
              , ... see above ...) 
              throws SQLException, ClassNotFoundException, IOException {
</pre>

- Similar to the previous method but scoped to a specific brand using the **`brand-cd`** path variable.
- Uses a path variable to filter data for a specific brand and potentially a growth platform within that brand.

## **POST Method for Creating Data**

### Inserting a New Growth Platform

<pre name="code" class="java">
@PostMapping(value = "/eds-ut-jrs-tax/growth-platforms"
           , consumes = "application/json"
           , produces = "application/json")
public ResponseEntity&lt;GrowthPlatformDim&gt; insertGrowthPlatform(
             @RequestBody GrowthPlatformDim fbsGrowthPlatform)
		throws IllegalArgumentException, IllegalAccessException, SQLException {
	return edsDaoService.insert(fbsGrowthPlatform);
}
</pre>

- **`@PostMapping`**: Handles HTTP POST requests for creating new resources.
- **Consumes** and **Produces**: The method consumes and returns `application/json`.
- **`@RequestBody`**: The JSON body of the request is mapped to a `GrowthPlatformDim` object.
- **Return**: Uses `edsDaoService.insert()` to add a new growth platform to the database.

## **DELETE Methods for Deleting Data**

### 1. **Delete All Growth Platforms**

<pre name="code" class="java">
@DeleteMapping("/eds-ut-jrs-tax/growth-platforms")
public ResponseEntity&lt;Integer&gt; deleteAllGrowthPlatforms(
		@RequestParam(defaultValue = "") 
		@ApiParam(value = "Add filter in format of a SQL WHERE clause.") 
		String filters)
		throws IllegalArgumentException, IllegalAccessException, SQLException {
	return edsDaoService.delete(GrowthPlatformDim.class, filters);
}
</pre>

- **`@DeleteMapping`**: Handles HTTP DELETE requests.
- **`filters`**: Optional SQL `WHERE` clause filter to delete specific records.
- **Return**: The number of deleted rows using `edsDaoService.delete()`.

### 2. **Delete a Specific Growth Platform by Code**

<pre name="code" class="java">
@DeleteMapping("/eds-ut-jrs-tax/growth-platforms/{growth-platform-cd}")
public ResponseEntity&lt;Integer&gt; deleteGrowthPlatform(
		@PathVariable(name = "growth-platform-cd") 
		@ApiParam(value = "FBS GrowthPlatform Code") 
		String growthPlatformCd)
		throws IllegalArgumentException, IllegalAccessException, SQLException {
	GrowthPlatformDim growthPlatform = new GrowthPlatformDim(growthPlatformCd);
	return edsDaoService.delete(GrowthPlatformDim.class, growthPlatform);
}
</pre>

- Deletes a specific growth platform by its code.

## **POST Methods for ETL Operations**

### 1. **Initial Data Load (IDL)**

<pre name="code" class="java">
@PostMapping("/eds-ut-jrs-tax/growth-platforms/idl")
public ResponseEntity&lt;EtlResponse&gt; idlGrowthPlatforms(
       @RequestParam("new-file") MultipartFile newFile
     , @RequestParam(name = "key-length", defaultValue = "1") int keyLength
     , @RequestParam(name = "output-file-name"
                   , defaultValue = "growth-platforms.csv") 
     , String outputFileName)
       throws CsvValidationException, IOException, EtlException
         , IllegalArgumentException, IllegalAccessException, SQLException {
	return edsDaoService.etl(GrowthPlatformDim.class
	                       , null      //old data file
	                       , newFile
	                       , keyLength
	                       , outputFileName);
}
</pre>

- **Initial Data Load (IDL)** operation using an uploaded file. The `newFile` parameter contains the file data, and the `keyLength` specifies the key length for processing.

### 2. **ETL Operation (Extract, Transform, Load)**

<pre name="code" class="java">
@PostMapping("/eds-ut-jrs-tax/growth-platforms/etl")
public ResponseEntity&lt;EtlResponse&gt; etlGrowthPlatforms(
       @RequestParam("old-file") MultipartFile oldFile
     , @RequestParam("new-file") MultipartFile newFile
     , @RequestParam(name = "key-length", defaultValue = "1") int keyLength
     , @RequestParam(name = "output-file-name"
                   , defaultValue = "growth-platforms.csv") 
     , String outputFileName)
       throws CsvValidationException, IOException, EtlException
         , IllegalArgumentException, IllegalAccessException, SQLException {
	return edsDaoService.etl(GrowthPlatformDim.class
	                       , oldFile
	                       , newFile
	                       , keyLength
	                       , outputFileName);
}
</pre>

- **ETL operation** that compares the `oldFile` with the `newFile` to identify changes and updates accordingly.

# Services

The Services package handles the business logic for data operations and acts as the bridge between the Controller layer and the database. It contains a key abstract class, `AbstractDaoService`, and specific service classes tailored for each business area. Service classes are organized by business domains (e.g., Product Taxonomy) and extend `AbstractDaoService`. These services inherit all necessary functionality from `AbstractDaoService` for CRUD operations, SQL generation, hierarchical data navigation, and delta processing.


## **Services Overview**
- **`AbstractDaoService`**: 
  - A base class implementing `DaoInterface` with core methods for data operations, including `find`, `findAll`, `insert`, `insertAll`, `delete`, and more.
  - This class generates SQL queries dynamically for CRUD operations, supporting variable substitution for filtering and flexibility.
  - It contains methods for handling complex SQL queries that navigate through table hierarchies, using parent-child relationships defined in the Beans.
  - Implements initial data load (IDL) and delta change processing using a "ladder comparison" algorithm, which determines the necessary inserts, updates, and deletes to bring the target table into alignment with incoming data.
  
- **`DaoInterface`**: 
  - A simple interface defining the `getConnection()` method, which manages database connectivity via JDBC. Each service implementing this interface must provide a connection configuration.
  
## **Object Model**

The object model below shows a single concrete class, `EdsDaoService`, and its relationship with the `AbstractDaoService` and `DaoInterface`. As mentioned, all services inherit their functionality from `AbstractDaoService` and must implement the `getConnection()` method.

<pre class="mermaid svg-zoomable-content" id="services-object-model">
classDiagram
  class AbstractDaoService {
    - Class t
    - String baseTableNm
    - String tableNm
    - String scdTableNm
    - String forceTableNm
    - FileStorageService fileStorageService
    - FileStorageProperties fileStorageProperties
    + List findAll(String, String, int, int)
    + List findAll(String, String, int)
    + List findAll(String, int, int)
    + List findAll(String, int)
    + List findAll(String)
    + List findAll()
    + List findAll(String, String)
    + List findAll(Map, String, boolean, String, int, int, String, boolean)
    + List findAll(String, String, int, int, String)
    + List findAll(String, String, int, boolean)
    + List findAll(String, String, int, int, boolean)
    + List findAll(String, String, int, String)
    + ResponseEntity insert(Object)
    + ResponseEntity find(Class, Map, String, String, boolean, String, boolean, boolean, String, int, String, HttpServletRequest)
    + ResponseEntity find(Class, Map, String, String, boolean, String, boolean, boolean, String, String, HttpServletRequest)
    + ResponseEntity find(Class, Map, String, String, boolean, String, boolean, boolean, String, boolean, String, HttpServletRequest)
    + ResponseEntity find(Class, Map, String, String, boolean, boolean, String, boolean, boolean, String, boolean, int, String, HttpServletRequest)
    + int delete(List)
    + ResponseEntity delete(Class, Object)
    + String getForceTableNm()
    + void setForceTableNm(String)
    + ResponseEntity returnTax2Csv(List, String, HttpServletRequest)
    + ResponseEntity returnTax2CsvObject(List, String, HttpServletRequest)
    + ResponseEntity returnTax3CsvObject(List, String, HttpServletRequest)
    + ResponseEntity returnCsvObject(List, String, HttpServletRequest)
    + ResponseEntity connectToDatasource(String)
    + ResponseEntity runDynamicSql(String, String, HttpServletRequest, boolean)
    + ResponseEntity runDynamicSql(String, String, HttpServletRequest, int, int)
    + ResponseEntity runDynamicSql(String, String, String[], HttpServletRequest, boolean, int, int)
    + ResponseEntity runDynamicSql(String, String, HttpServletRequest)
    + ResponseEntity getListForSql(Class, Connection, String, boolean, HttpServletRequest)
    + ResponseEntity findWithInheritence(Class, Map, String, String, boolean, boolean, String, boolean, String, HttpServletRequest)
    + List findAllWithInheritence(Map, String, boolean, String, int, String, boolean)
    + List getObjectListFromExcel(String, String)
    + String getScdTableNm()
    + void setScdTableNm(String)
    + String getBaseTableNm()
    + void setBaseTableNm(String)
    + String getForSql(DataSourceDim, String, String[])
    + String getForSql(Connection, String)
    + String getForSql(Connection, String, String[])
    + String getForSql(DataSourceDim, String)
    + List findAllTax(Map, String, boolean, String, int, boolean, String)
    + List findAllTax(String, String, int, boolean, String)
    + List findAllTax(String, String, int, String)
    + int insertAll(List)
    + int deleteAll(String)
    + int deleteAll()
    + ResponseEntity etl(Class, MultipartFile, MultipartFile, int, String)
    + EtlResponse etl(String, String, int, String)
    + int updateAll(List)
    + ResponseEntity countAll(Class, Map, String, boolean, String, boolean)
    + ResponseEntity idlExcel(Class, MultipartFile, String)
    + Class getT()
    + void setT(Class)
    + String getTableNm()
    + void setTableNm(String)
    - Object lambda$1(Entry)
    - void lambda$2(Builder, String)
    + ResponseEntity returnCsv(List, String, HttpServletRequest)
  }
  class DaoInterface {
    + Connection getConnection()
  }
  class EdsDaoService {
    - Class t
    - String tableNm
    - String scdTableNm
    - BridgeDatasourceProperties bridgeProp
    + Connection getConnection()
  }
DaoInterface <|.. AbstractDaoService
AbstractDaoService <|-- EdsDaoService
</pre>
<button type="button" class="btn btn-primary" onclick="restoreSvg('services-object-model')">Reset zoom</button>
<button type="button" class="btn btn-primary" onclick="downloadSvg('services-object-model')">Download</button>
<button type="button" class="btn btn-primary" onclick="openSvg('services-object-model')">Open</button>
  

## AbstractDaoService's @GetMapping

<pre class="mermaid svg-zoomable-content" id="sequence-diagram-get-mapping" >
flowchart TD
    classDef decisionBox fill:#0078d4,stroke:#000,stroke-width:2px,color:#fff
    style ads fill:#0078d4,stroke:#000,stroke-width:2px,color:#fff
    classDef box fill:#0078d4,stroke:#000,stroke-width:2px,color:#fff
    

    client[[Client]]
    controller(Controller):::box
    client-- /api/v1/{cntlr}/{object-name}<br>/{path-var(s)}?{request-parm(s)} --->controller
    ads(AbstractDaoService)
    controller-- find() -->ads
    hi{includeParentage?}
    ads-->hi:::decisionBox
    hi-- yes -->sr{selfReferencing?}:::decisionBox
	sr-- no -->findalltax[List&lt;T&gt; listObjects = findAllTax#40;#41;]
	sr-- yes -->top
	subgraph top
		fa1[List&lt;SelfReferenceTaxonomyNodeInterface&gt; nodes= this.findAll#40;filters, pit, size#41;]
		conv[listObjects.add#40;#40;T#40;NaryTree.buildTreeFromSelfReferencingTaxonomy#40;nodes#41;#41;]
		fa1-->conv			 
    end
	top-->checknull
    hi-- no -->findall[List&lt;T&gt; listObjects = findAll#40;#41;]

    checknull{listObjects==null or<br>listObjects.size#40;#41;==0}:::decisionBox
    findalltax-->checknull
    findall-->checknull

    checknull-- yes -->r1[return ResponseEntity.status#40;HttpStatus.SC_NOT_FOUND#41;.body#40;listObjects#41;]
    checknull -- no --> returncsv{returnCsv?}:::decisionBox

    returncsv-- yes -->hi2{includeParentage?}:::decisionBox
    returncsv-- no -->r2[return ResponseEntity.ok#40;#41;.body#40;listObjects#41;]

    r2[return ResponseEntity.ok#40;#41;.body#40;listObjects#41;]

    hi2--yes-->r3[return this.returnTax2CsvObject<br/>#40;listObjects<br/>,csvFileName<br/>,request#41;]
    hi2--no-->r4[return this.returnCsvObject<br/>#40;listObjects<br/>,csvFileName<br/>,request#41;]

    r2-->stop(("`stop`")) 
    r3-->stop
    r4-->stop
    r1-->stop2(("`stop`")) 
</pre>
<button type="button" class="btn btn-primary" onclick="restoreSvg('sequence-diagram-get-mapping')">Reset zoom</button>
<button type="button" class="btn btn-primary" onclick="downloadSvg('sequence-diagram-get-mapping')">Download</button>
<button type="button" class="btn btn-primary" onclick="openSvg('sequence-diagram-get-mapping')">Open</button>

<!-- ####################################################################################################################### -->

# Annotations
This package contains custom Java annotations that facilitate Object-Relational Mapping (ORM) and metadata management for database interactions and Excel-based utilities. These annotations are defined with `@Retention(RetentionPolicy.RUNTIME)`, allowing them to be accessed via reflection during runtime. 

<pre class="mermaid svg-zoomable-content" id="object-model-annotations" >
classDiagram

  class DbTable {
    + String beanName()
    + String tableName()
    + String baseTableName()
    + String parentBeanPackageName()
    + String parentBeanName()
    + String parentBaseTableName()
  }
  class DbColumn {
    + String columnName()
    + boolean isId()
    + int keySeq()
    + boolean isScd()
    + boolean isExtension()
    + int foreignKeySeq()
    + int assocParentKey()
    + int assocChildKey()
  }
  class ExcelSheet {
    + boolean ignore()
    + String columnName()
    + int columnNum()
  }

Annotation <|.. DbTable
Annotation <|.. DbColumn
Annotation <|.. ExcelSheet
</pre>
<button type="button" class="btn btn-primary" onclick="restoreSvg('object-model-annotations')">Reset zoom</button>
<button type="button" class="btn btn-primary" onclick="downloadSvg('object-model-annotations')">Download</button>
<button type="button" class="btn btn-primary" onclick="openSvg('object-model-annotations')">Open</button>

Below are the annotations provided:

## **Annotations Overview**
- **`DbTable`**:  
  This annotation is used to designate a class as an entity representing a database table. It contains metadata about the database table, such as table name, schema, and relationships (like parent-child relations). This is essential for defining how the Java classes map to the backend database tables, supporting ORM.

	**Example Usage**:
	
	<pre name="code" class="java">
	@DbTable(beanName="ServiceLineDim"
	       , baseTableName="REFT.SERVICE_LINE"
	       , parentBeanName="GrowthPlatformDim"
	       , parentBaseTableName="REFT.GROWTH_PLATFORM")
	public class ServiceLineDim extends ...
	</pre>
  
- **`DbColumn`**:  
  This annotation is applied to fields within a POJO to represent a database column. It includes attributes such as column name, data type, nullable constraints, and key designations (primary key, foreign key). This enables precise mapping of Java fields to database columns.

    **Example Usage**:
	<pre name="code" class="java">
	@DbColumn(columnName="SERVICE_LINE_ID",isId=true)
	private int        serviceLineId;
	@DbColumn(columnName="SERVICE_LINE_CD",keySeq=1)
	private String     serviceLineCd;
	@DbColumn(columnName="SERVICE_LINE_NM")
	private String     serviceLineNm;
	@DbColumn(columnName="SERVICE_LINE_DESC")
	private String     serviceLineDesc;
	@DbColumn(columnName="LGCY_SERVICE_LINE_CD",isExtension=true)
	private String     lgcyServiceLineCd;
	@DbColumn(columnName="DCC_CD",isExtension=true)
	private String     dccCd;
	@DbColumn(columnName="GROWTH_PLATFORM_CD",foreignKeySeq=1)
	private String     growthPlatformCd;
      </pre>

- **`ExcelSheet`**:  
  Utilized to designate a class or method that interacts with an Excel Spreadsheet. This annotation is crucial for artifact generation and data loading functionalities. It includes attributes like sheet name and data mapping configurations for Excel-based schema definitions.

    **Example Usage**:
	<pre name="code" class="java">
	@ExcelSheet(columnName="Geography Type",columnNum=0)	
	private String     geographyTypeNm;	
       </pre>


# Beans
The Beans package consists of Plain Old Java Objects (POJOs) that are annotated with the custom annotations from the Annotations package. These POJOs are utilized for ORM and represent rows from database tables. They are primarily generated using a Maven plugin developed for the framework, automating the creation process based on Excel-based schema definitions.


## **Beans Overview**
The Beans package consists of Plain Old Java Objects (POJOs) that are annotated with the custom annotations from the Annotations package. These POJOs are utilized for ORM and represent rows from database tables. They are primarily generated using a Maven plugin developed for the framework, automating the creation process based on Excel-based schema definitions.

# Configurations

The Configurations package is responsible for setting up the application environment, including database connectivity, security, and other necessary configurations. It contains:
- **Database Configurations**: JDBC templates, connection pools, and credential handling using secure storage solutions (e.g., Secrets Manager).
- **API Settings**: Configurations for RESTful API settings, endpoint URLs, and security filters.
- **Annotation Scanning**: Setup for scanning custom annotations like `@DbTable` and `@DbColumn` during runtime to facilitate dynamic ORM mapping.

# Utilities

This package consists of various utility classes that support the core operations of the framework. These utilities include:
- **Artifact Generation**: Code and schema generation based on Excel files.
- **Data Processing**: Helper classes for data transformation, data validation, and template processing.
- **Excel Handling**: Tools for reading, parsing, and manipulating Excel spreadsheets to extract schema definitions.

# Exceptions

This package handles exception management across the application. It includes:
- **Custom Exceptions**: Application-specific exceptions for data access, validation errors, and service layer issues.
- **Global Exception Handler**: A global handler annotated with `@ControllerAdvice` to manage exceptions centrally. It provides a consistent error response structure for the API, improving the developer experience and debugging process.


# Project Object Model

This application uses Maven, so we used Spring Initializr to generate the initial project with the required dependencies (Spring Web), specified in the **Project Object Model** or **POM** file.  The POM is the fundamental unit of work in Maven. It is an XML file that contains information about the project and configuration details used by Maven to build the project. It contains default values for most projects. Examples for this is the build directory, which is target; the source directory, which is src/main/java; the test source directory, which is src/test/java; etc. This is because all POMs are inherit from a super-POM, that contain these default values (see: [https://maven.apache.org/ref/3.6.3/maven-model-builder/super-pom.html](https://maven.apache.org/ref/3.6.3/maven-model-builder/super-pom.html)). When executing a task or goal, Maven looks for the POM in the current directory. It reads the POM, gets the needed configuration information, then executes the goal.

Some of the configuration that can be specified in the POM are the project dependencies, the plugins or goals that can be executed, the build profiles, clean, verify, etc. Other information such as the project version, description, developers, mailing lists and such can also be specified.

The minimum requirement for a POM are the following:
- project root
- modelVersion - should be set to 4.0.0
- groupId - the id of the project's group.
- artifactId - the id of the artifact (project)
- version - the version of the artifact under the specified group


The following listing shows the pom.xml file that was created when you choose Maven:

<pre name="code" class="xml">
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.ibm.wfm.ms</groupId>
	<artifactId>ms-spring-server</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>ms-spring-server</name>
	<description>Spring Boot Centralized Configuration</description>
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.0</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
</pre>

We'll add to this all the dependencies that we'll need as we continue.  The Spring Initializr also creates a simple application class called `Application.java`.  We'll make some changes to this, primarily to enable `Swagger` documents to be generated automatically for us.

To enable the Swagger2 in Spring Boot application, you need to add the following dependencies in the `POM.xml` configuration file.

<pre name="code" class="xml">
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
  </dependency>		
  
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
  </dependency>
</pre>