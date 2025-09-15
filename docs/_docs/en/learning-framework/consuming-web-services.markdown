---
title: Consuming Web Services
permalink: en/learning-framework/consuming-web-services
abstract: >- # this means to ignore newlines until "baseurl:"
  Consuming RESTful web services often requires a lot of boilerplate code. Within this framework, coarse-grained APIs consume finer-grained to deliver a service. This framework uses Spring Boot REST template to simplify REST services consumption.
---

# Overview

In spring boot microservices architecture, all services are configured as rest services. There is a need for one rest service to be called by another rest service. A rest api service may depend on a number of other services. All spring boot downstream services need to be accessed from the main rest api. Spring boot supports calling one rest api from another rest api.

Granularity is the extent to which a system is broken down into small parts; it is the extent to which a larger entity is subdivided. Within this framework, **coarse-grained** APIs are the results of calling one or more **fine-grained** APIs, aggregating or processing the data, and returning the results.

To call fine-grained APIs within an API, Spring provides a convenient template class called **`RestTemplate`**. `RestTemplate` is a synchronised client side class that is responsible for calling another rest service. `RestTemplate` supports all HTTP methods such as GET, POST, DELET, PUT, HEAD, etc. `RestTemplate` makes interacting with most RESTful services a one-line incantation and it can bind the data to custom model/class.  The bound Java class has properties and matching getter methods, that match the API's return type. The target class can be annotated with `@JsonIgnoreProperties(ignoreUnknown = true)` from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.  To directly bind your data to your custom class, you need to specify the variable name to be exactly the same as the key in the JSON document returned from the API. In case your variable name and key in JSON doc do not match, you can use `@JsonProperty` annotation to specify the exact key of the JSON document.

Using Spring `RestTemplate`, consuming APIs can be performed in 3 steps:
1. Building the URI
1. Executing the fine-grained API
1. Consuming results


# Building the URI

A **Uniform Resource Identifier (URI)** is a unique sequence of characters that identifies a logical or physical resource used by web technologies.  Note, A **Uniform Resource Locator (URL)**,  is a special type of identifier that also tells you how to access it, such as HTTPs, FTP, etc.  If the protocol (https, ftp, etc.) is either present or implied for a domain, it is really a URL, but since the Spring framework refers to it as a URI, the DMF framework does as well. 

Coarse-grained services first builds the URI to the fine-grained API/service using the Spring `ServletUriComponentsBuilder` class. The `ServletUriComponentsBuilder` class extends the `UriComponentsBuilder` that extracts information from the `HttpServletRequest` and provides methods for building a URI. Specifically the framework uses the `fromCurrentContextPath()`, `path()`, and `queryParam()` methods to build the required URI for the fine-grained API.  

- `fromCurrentContextPath()` - Prepares a builder from the host, port, scheme, and context path of the request obtained through `RequestContextHolder`.
- `path()` - Inherited from `UriComponentsBuilder`. Append the given path to the existing path of this builder. The given path may contain URI template variables.
- `queryParam()` - Inherited from `UriComponentsBuilder`. Append the given query parameter to the existing query parameters. The given name or any of the values may contain URI template variables. If no values are given, the resulting URI will contain the query parameter name only..

An example of this is below:

<pre name="code" class="java">
// Build URI for the fine grained API/Service
String fineGrainedApiUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/")
        .path("api-path")
        //if applicable
        .path(pathVariableValue)
        ...
        //if applicable
        .queryParam("parm-name", "parm-value")
        ...
        .toUriString();
</pre>


# Executing the fine-grained API

The Spring Boot `RestTemplate` class is contained in the **spring-web** artifact and provides overloaded methods for different HTTP methods, such as GET, POST, PUT, DELETE etc.
Additionally the class contains generalized `exchange()` and `execute()` methods that support cases where headers need to be passed.

_NOTE:_ As of 5.0 this class is in maintenance mode, with only minor requests for changes and bugs to be accepted going forward. This framework will neeed to start using the `org.springframework.web.reactive.client.WebClient` which has a more modern API and supports synchronous, asynchronous, and streaming scenarios.

<pre name="code" class="java">
// Call the fine grained service using RestTemplate
ResponseEntity&lt;EtlResponse&gt; fineGrainedResponseEntity = 
           new RestTemplate().getForEntity(fineGrainedApiUri, null, EtlResponse.class);
</pre>

**or**
<pre name="code" class="java">
// Call the fine grained service using RestTemplate
ResponseEntity&lt;EtlResponse&gt; fineGrainedResponseEntity = 
           new RestTemplate().postForEntity(fineGrainedApiUri, httpEntity, EtlResponse.class);
</pre>

The `getForEntity()` method retrieve an entity by doing a GET on the specified URL. The response is converted and stored in an `ResponseEntity`.

The `postForEntity()` method retrieve an entity by doing a POST on the specified URL. The response is converted and stored in an `ResponseEntity`.

The `RestTemplate()` call returns a `ResponseEntity`, which extents the `HttpEntity` adding a `HttpStatus` status code object with a body type of `<T>`.

# Consuming results

The `getBody()` method of the `ResponseEntity` class is inherited from `HttpEntity` and returns the body of this entity.

<pre name="code" class="java">
List&lt;T&gt; fineGrainedResponseList = fineGrainedResponseEntity.getBody();
</pre>


# Building and executing fine-grained APIs with MultipartFile
A sepcial case is when the API is a POST that uses a `MultipartFile` as one of the required parameters. Consider an ETL service that requires a new and old data file as a `MultipartFile`
in the body of the of the POST request.  

<pre name="code" class="java">
@PostMapping("/eds-ut-jrs-tax/brands/etl")
public ResponseEntity&lt;EtlResponse&gt; etlBrands(
    @RequestParam("old-file") MultipartFile oldFile
  , @RequestParam("new-file") MultipartFile newFile
  , @RequestParam(name="key-length",defaultValue="1") int keyLength
  , @RequestParam(name="output-file-name",defaultValue="brands.csv") String outputFileName) 
  throws CsvValidationException, IOException, EtlException, IllegalArgumentException
  , IllegalAccessException, SQLException {
  
	return edsDaoService.etl(BrandDim.class, oldFile, newFile, keyLength, outputFileName);
	
}
</pre>

A HTTP multipart request is a HTTP request that HTTP clients construct to send files and data over to a HTTP Server. It is commonly used by browsers and HTTP clients to upload files to the server.  In Spring Boot `MultipartFile` extends `InputStreamSource` and is a representation of an uploaded file received in a multipart request.

1. Setup files from the disk as `FileSystemResource`s. `FileSystemResource` is a resource implementation for `java.io.File` and `java.nio.file.Path` handles with a file system target. 
1. Add/set the context type for the `HttpHeaders`.  Specficially `MediaType.MULTIPART_FORM_DATA`. When this header is set, RestTemplate automatically marshals the file data along with some metadata.
1. Add the request paramters to a map which maps request parameter names, as the keys, and tthe values `FileSystemResource`.
1. Create an `HttpEntity` with the headers and parameter map.
1. Call the fine grained service using `RestTemplate`, `HttpEntity`, and target/result class.
1. Consume the result

<pre name="code" class="java">
String brandEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/")
        .path("eds-ut-jrs-tax/brands/etl")
        .toUriString();

// Set tye URI Parameter values
String oldFileName = fileStorageProperties.getUploadDir()+"/brandDim-old.csv";
String newFileName = fileStorageProperties.getUploadDir()+"/brandDim-new.csv";
String outputFileName = "/brandDim-etl.csv";

// (1) getting the file from disk
FileSystemResource oldFileResource = new FileSystemResource(new File(oldFileName));
FileSystemResource newFileResource = new FileSystemResource(new File(newFileName));

// (2) adding headers to the api
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.MULTIPART_FORM_DATA);
headers.set("x-key", API_KEY);

// (3) Add request parameters to map
MultiValueMap&lt;String, Object&gt; body = new LinkedMultiValueMap&lt;>();
body.add("old-file", oldFileResource);
body.add("new-file", newFileResource);
body.add("output-file-name", outputFileName);

// (4) Create the Http Request Entity
HttpEntity&lt;MultiValueMap&lt;String, Object&gt;&gt; requestEntity= new HttpEntity&lt;&gt;(body, headers);

// (5) Call the fine grained service using RestTemplate
ResponseEntity&lt;EtlResponse&gt; brandEtlResponseEntity = new RestTemplate().postForEntity(brandEtlUri, requestEntity, EtlResponse.class);

// (6) get the returned EtlResponse object 
EtlResponse etlResponses = brandEtlResponseEntity.getBody();

