---
title: Calling from Java
permalink: en/using-services/calling-from-java
abstract: >- # this means to ignore newlines until "baseurl:"
  Calling services from Java Code.
---


Put a bunch of the content here on Calling from Java.


## Calling Services with SpringBoot

If you are accessing APIs (services) from a Java Spring Boot application, you can simply use the **Rest Template** class in Spring.  `RestTemplate` is used to create applications that consume RESTful Web Services. It is a synchronous client to perform HTTP requests, exposing a simple, template method API over underlying HTTP client libraries such as the JDK HttpURLConnection, Apache HttpComponents, and others.

The RestTemplate offers templates for common scenarios by HTTP method, in addition to the generalized exchange and execute methods that support of less frequent cases.

**Note:** As of Spring Framework 5 Spring introduced a new HTTP client called `WebClient`, which is a modern alternative HTTP client to RestTemplate. Not only does it provide a traditional synchronous API, but it also supports an efficient nonblocking and asynchronous approach.

To use `RestTemplate` you simply
1. Construct the API's URI
1. Call the `RestTemplate`'s `getForEntity()` method, passing in the URI and the `Class` of the returned objects
1. Extract the returned objects from the `ResponseEntity`.

<pre name="code" class="java">
//Call /ed-jrs-tax/service-lines
String serviceLineUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/")
        .path("/eds-jrs-tax/service-lines")
        .queryParam("filters", filters)
        .queryParam("includeParentage", includeParentage)
        .queryParam("includePit", includePit)
        .queryParam("pit", pit)
        .queryParam("resultSetMaxSize", resultSetMaxSize)
        .toUriString();

ResponseEntity<ServiceLineDim[]> responseEntity = new RestTemplate().getForEntity(serviceLineUri, ServiceLineDim[].class); 
List<ServiceLineDim> serviceLines = Arrays.asList(responseEntity.getBody());
</pre>