---
title: Generating Artifacts
permalink: /en/exploring-services/generating-artifacts
abstract: >- 
  The data marshalling utilities make it simple to retrieve, move, transform, and load data, however they do require a POJO with annotations and DDL to build a source or target relational database source. The framework includes a service and utility to generate a POJO and/or DDL for your data source.
---


The service accepts a spreadsheet that contains a (database) schema.  From this, the artifact generator service produces the POJO Java code necessary, including all required annotations, to access each field in each table or spreadsheet through the framework's persistence framework. 

# Template Excel File

The user supplies a Excel template file that contains at a minimum two tabs:

1. Named **Tables** contains a list of every table that the template spreadsheet contains. The columns for this tab are:
   1. **Schema** - The schema name for the database table where thee object will be persisted.  
   1. **Name** - The table name for the database table where thee object will be persisted.  
   1. **Tablespace Name** - [Optional, default is "TS_"+up to 15 characters of table name (tablespace names can have a maximum size of 18 characters)]  
   1. **Extension Name** - [Optional, default is none] The framework supports multiple design patterns including the "extended relational model".  <br/><br/>The model allowed business units to extend a relational model with business unit specific schema or data. It enables a relational model to behave like an object-oriented model with respect to inheritance, covering both the breath of the dimension (columns) and depth of the dimensions (rows) and enables overriding parent attribute values. By specifying a value, the service will generate the DDL for both the standard schema and one for the Extended Model.  
   1. **Is SCD** - [Optional, default is `false`] The framework supports multiple design patterns including the "slow changing dimensions".  <br/><br/>The design pattern tracks and stores all state changes to a dimension over time and allows users to easily access the state of the dimension at any time in the past. The pattern also includes triggers to manage the attributes that manage the time varient nature of the data. If this column is set to `true`, the service will generate the DDL that includes the additional attributes required for slow changing dimensions (e.g. effective and expiration timestamps), the triggers used to manage them, and the additional end user views. 
   1. **Remarks**  - [Optional] Remarks to be used in the DDL for the generated table.
   
1. One tab, that correcesponds to a **Name** in the **Tables** tab. This will be used to generate the Java objects representing each table, using annotations for use with the framework. The columns for this tab are:
   1. **Excel Pos**
   1. **Excel Column**
   1. **Excel Heading**
   1. **Name**
   1. **Data Type**
   1. **Length**
   1. **Scale**
   1. **Nullable**
   1. **Default Value**
   1. **Key Seq**
   1. **Is ID**
   1. **Is SCD**
   1. **Remarks**

<p>Sample template file: <img src="{{ site.baseurl }}/assets/images/octicons/excel-file.svg" height="16px" width="16px" fill="green"> <a href="{{ site.baseurl }}/assets/data/dao-template.xlsx">dao-template.xlsx</a> </p>

# Invoking the function

See [https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/dao-artifact-generator-controller](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/dao-artifact-generator-controller)



