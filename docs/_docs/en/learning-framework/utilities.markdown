---
title: Utilities
permalink: en/learning-framework/utilities
abstract: >- # this means to ignore newlines until "baseurl:"
  Utility methods perform common, often re-used functions which are helpful for accomplishing routine programming tasks.  Utility methods for this framwork are gathered into utility classes, usually declared as static type methods, within the `com.ibm.wfm.utils` package.
---


## Object-relational Mapper
The framework contains a object-relational mapper (ORM) Java abstraction that automates the transfer of data stored in relational database tables into a list of objects and vice versa. This abstraction performs data marshaling to read, write, update, and/or delete to and from any relational data source where the source and target are not known until runtime. The mapper understands and handles surrogate and natural keys, views, SQL of arbitrary complexity, slow-changing dimensions, and allows the source and targets to have different sources, entity and/or attribute names, and have different schemas.

## Excel Mapper
The framework contains an excel-object mapper Java abstraction that automates the transfer of data stored in an excel spreadsheet tab into a list of objects. This list can then be used by the ORM to persist the data into relational databases. This abstraction performs data marshaling to read from an excel spreadsheet and tab and populate an object list for subsequent processing.

## Data Marshalling

The framework contains a base set of data marshalling functions using Java annotations and reflection. 

**Java reflection** allows you interrogate the properties of a class, such as the names and types of its instance variables and methods, at run time. It also enables classes to be created from their names, dynamically creating and invoking class constructors at runtime. It allows these base functions to do serialization and deserialization objects in a completely generic manner. This means that there is no need to write code for marshalling functions for each type of object. Any mapping required (e.g., source to target attribute name, attribute offsets) are captured using **Java annotations**.

There are marshalling functions for Excel, CSVs, JSON, XML, and relational databases.

## Result Set Serializer
For most data retrieval, a POJO Bean is created and the **`AbstractDaoService`** methods return a `List` of these beans from the database.  For bean based data retrieval, including beans that are part of a hierarchy within an n-ary tree, the framework uses the **Jackson** libraries to return JSON. Jackson has become the defacto standard for JSON parsering in Java, with a suite of data-processing tools for Java, including matching data-binding library (POJOs to and from JSON) and streaming JSON parser / generator libraries.

However there are times when the result set we need is based on a query of arbitrary complexity .... avoid loading all the cursor in a list and, instead, write each row directly on an output stream or writer.

The **`ResultSetSerializer`** allows the framework to serialize a result set to JSON, acting as a JSON generator that subclasses Jackson's **`JsonSerializer`**, but is specific to `ResultSet`s  and wraps an output stream or writer.  The `ResultSetSerializer` object uses the Jackson Streaming API and instructs Jackson how to serialize (tranform the object to JSON) a `ResultSet`. 

We first need to add Jackson data binding to the Project Object Model (POM).

<pre name="code" class="xml">
&lt;dependency&gt;
    &lt;groupId&gt;com.fasterxml.jackson.core&lt;/groupId&gt;
    &lt;artifactId&gt;jackson-databind&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre>

Thereafter the framework:

1. Instantiates a Jackson `SimpleModule` class that allows the registration of serializers and deserializers
1. Add `ResultSetSerializer` class as a serializer to the Module
1. Create a Jackson `ObjectMapper` that provides functionality for reading and writing JSON as well as performing conversions
1. Then the framework registers the Module to the `ObjectMapper`, extending the functionality provided by the mapper with our custom serializer.

<pre name="code" class="java">
SimpleModule module = new SimpleModule();
module.addSerializer(new ResultSetSerializer());

ObjectMapper objectMapper = new ObjectMapper();
objectMapper.registerModule(module);

[ . . . do the query . . . ]
Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
ResultSet resultSet = stmt.executeQuery(query);


// Use the DataBind Api
ObjectNode objectNode = objectMapper.createObjectNode();

// put the resultset in a containing structure
objectNode.putPOJO("results", resultSet);

StringWriter stringWriter = new StringWriter();

// generate all
objectMapper.writeValue(stringWriter, objectNode);

stmt.close();
conn.close();
</pre>

## Delta Processing
The framework contains a data comparison microservice that can compare two sources and identify inserts, updates, and deletes between the two sources. Ladder comparison algorithm.

## JR/S Mapper
The framework contains a employee JR/S mapper function as a microservice within this framework.

## File Bursting
The framework contains a generic file bursting service. This service allows a single file to be split into an arbitrary number of files, retaining a common set of column headings, based on a configurable set of parameters.

## Taxonomy Evaluator
The framework contains a Taxonomy Evaluator. The taxonomy evaluator will evaluate any data file against any taxonomy based on a map of taxonomy keys within the data file being evaluated. For each data file record, the service identifies all invalid nodes and/or arcs (i.e., valid nodes with invalid parent-child relationships), the lowest valid node within a branch, and the valid leaf node (when it exists) and its full branch.