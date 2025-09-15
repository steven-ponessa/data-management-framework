---
title: Object Model
permalink: en/learning-framework/object-model
features:
  - mermaid
  - zoom
abstract: >- # this means to ignore newlines until "baseurl:"
  The object model visualizes the elements in a software application in terms of objects and their relationships to one another.
---

# Abstract DAO Service Model

The diagram below shows the object model for the **Data Access Objects (DOA)**.

{: style="text-align: center;" }
![Abstract DAO Service Model]({{ site.baseurl }}/assets/images/docs/abstract-dao-service-model.png){:class="zoom"}

[![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/abstract-dao-service-model.png){: target="_blank"}


See [https://app.genmymodel.com/editor/edit/_Kdhu8O_4EeuvZZKl65u9TA#](https://app.genmymodel.com/editor/edit/_Kdhu8O_4EeuvZZKl65u9TA#)

The `AbstractDaoService` abstract class contains the common data functions, specifically:
- `findAll()` which will retrieve all records from the source relational table while applying any filters (WHERE clause) passed and/or for a specific **point in time**. Note that the point in time
must be in the format `yyyy-MM-dd hh.mm.ss.tttttt
- `findAllTax()` which will retrieve all records from the source relational table and all parent records while applying any filters (WHERE clause) passed and/or for a specific **point in time**. Note that the point in time
must be in the format `yyyy-MM-dd hh.mm.ss.tttttt
- `insertAll()` which inserts a `List` of objects into the table specified by the **concrete subclass** and driven by the `bean`s `DbTable` annotations.
- `deleteAll()` which deletes all rows from the the table specified by the **concrete subclass**.
- `delete()` which deletes all rows specified by a `List` of objects from the table specified by the **concrete subclass** and driven by the `bean`s `DbTable` annotations.
- `getObjectListFromExcel()` which populates a `List` of objects from an Excel file and tab.
- `etl()` (Extract, Transform, and Load) which uses a ladder processing algorithm to identify insert, update, and delete operations and then applies them to the table specified by the **concrete subclass** and driven by the `bean`s `DbInfo` and `DbTable` annotations. Note that IDL (Initial Data Load) can be performed with the `findAll()` function.
- `returnCsv()`
- `returnTax2Csv()`

The `AbstractDaoService` abstract class implements the `DaoInterface` that contains the single abstract method, `getConnection()`. This method must be implemented in each **concrete subclass** that needs to perform database operations and must return a connection to the specific database.

_put in stuff on generated POJO and DDL such that to get an API up and running to pull or push data from and to a database, the developer only needs to create a new `Service` object that is a **concrete subclass** of the  `AbstractDaoService` abstract class and call that service from a new or existing `Controller`._

# Annotations and Reflection to Populate List from Resultset

![sequence diagram for data acquisition]({{ site.baseurl }}/assets/images/docs/sequence-diagram-data-acquisition.png){:class="zoom"}
[![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/sequence-diagram-data-acquisition.png){: target="_blank"}

<pre class="mermaid svg-zoomable-content" id="sequence-diagram-data-acquisition" >
  sequenceDiagram
  autonumber
    participant app as <br/>@SpringBootApplication<br/>Application
    participant controller as @RestController<br/>@RequestMapping('/api/v1')<br/>XxController
    Note over controller: @GetMapping<br/>('/eds-ut-jrs-tax/growth-platforms')<br/>retrieveAllGrowthPlatformByCode
    participant dao as <br/>@Component<br/>XxDaoService
    participant dsprop as  @Component<br/>@ConfigurationProperties("xx.datasource")<br/>XxDatasourceProperties
    participant bean as <br/>&lt;&lt;POJO&gt;&gt;<br/>Xx
    Note over bean: bean
    participant dbtable as <br/>&lt;&lt;Annotation&gt;&gt;<br/>DbTable
    participant dbcolumn as <br/>&lt;&lt;Annotation&gt;&gt;<br/>DbColumn
    participant dm as <br/><br/>DataManagerType4
    participant db as <br/><br/>Data Base

    app->>+controller: /api/v1/eds-ut-jrs-tax/growth-platforms
    controller->>+dao: findAll()
    dao->>+bean: getAnnotation(DbTable.class)
    bean->>+dbtable: getAnnotation()
    dbtable-->>-bean: dbTable
    bean-->>-dao: dbTable
    dao->>+dsprop: getUrl()
    dsprop-->>-dao: url
    dao->>+dm: getConnection()
    dm-->>-dao: connection
    dao->>+dm: getSelectQuery()
    dm->>+db: sql
    db-->>-dm: resultSet
    activate bean
    dm->>bean: getConstructor()
    bean-->>dm: constructor
    loop Every row
        dm-->>bean: Constructor.newInstance()
        bean-->>dm: bean instance
        dm->>bean: getDeclaredFields()
        bean-->>dm: fields
        loop Every column
            bean->>+dbcolumn: getAnnotation()
            dbcolumn-->>-bean: columns
            dm->>bean: set()
        end
        deactivate bean
    end
    dm-->>-dao: collection
    dao-->>-controller: collection
    controller-->>-app: collection as <br/>JSON, XML, or CSV

</pre>
<button type="button" class="btn btn-primary" onclick="restoreSvg('sequence-diagram-data-acquisition')">Reset zoom</button>
<button type="button" class="btn btn-primary" onclick="downloadSvg('sequence-diagram-data-acquisition')">Download</button>
<button type="button" class="btn btn-primary" onclick="openSvg('sequence-diagram-data-acquisition')">Open</button>

## Overview

In Java, we generally create objects using the `new` keyword or we use some framework e.g., Spring Boot JPA (Java Persistence API) to create an object which internally use **Java Reflection API** to do so. In this post, we are going to look at a way to use Java **annotations** and **reflection** to create a list of objects from a result set, where the SQL used to create the result set and target object are not know until runtime.

Java object serialization uses reflection to find out the class name of the object to be serialized and the names, types and values of its instance variables. The annotations contain the name of the serialization target, specifically the database column name.

For deserialization, the class name in the serialized form is used to create a class. The annotations contain the name of the serialization source, specifically the database column name or excel/csv column name or offset. This is then used to create a new constructor with argument types corresponding to those specified in the serialized form. Finally, the new constructor is used to create a new object with instance variables whose values are read from the serialized source.

The framework constains a base set of data marshalling classes using Java annotations and reflection. Java reflection allows you interrogate the properties of a class, such as the names and types of its instance variables and methods, at run time. It also enables classes to be created from their names, dynamically creating and invoking class constructors at runtime. It allows these base functions to do serialization and deserialization objects in a completely generic manner. This means that there is no need to write code for marshalling functions for each type of object. There are marshalling functions for Excel, CSVs, JSON, XML, and relational databases.

This is exploited by both database and Excel data marshalling classes.

For databases there is an object-relational mapper (ORM) Java abstraction that automates the transfer of data stored in relational database tables into a list of objects and vice versa. This abstraction performs data marshaling to read, write, update, and/or delete to and from any relational data source where the source and target are not known until runtime. The mapper understands and handles surrogate and natural keys, views, SQL of arbitrary complexity, slow-changing dimensions, and allows the source and targets to have different sources, entity and/or attribute names, and have different schemas.

For Excel there is an excel-object mapper Java abstraction that automates the transfer of data stored in an excel spreadsheet tab into a list of objects. This list can then be used by the ORM to persist the data into relational databases. This abstraction performs data marshaling to read from an excel spreadsheet and tab and populate an object list for subsequent processing.

There are also other services like 
- A data comparison microservice that can compare two sources and identify inserts, updates, and deletes between the two sources.
- An employee JR/S mapper 
- A generic file bursting service into this framework. This service allows a single file to be split into an arbitrary number of files, retaining a common set of column headings, based on a configurable set of parameters
- A Taxonomy Evaluator. The taxonomy evaluator will evaluate any data file against any taxonomy based on a map of taxonomy keys within the data file being evaluated. For each data file record, the service identifies 
    - all invalid nodes and/or arcs (i.e., valid nodes with invalid parent-child relationships)
    - the lowest valid node within a branch
    - the valid leaf node (when it exists) and its full branch.
- A service to auto-generate the Db2 DDL and Java Bean code required to hold new objects for ETL or data retrievals, based on a supplied definition.

The solution described in this post will use JDBC to execute SQL, returning the result set, the result set meta data, Java annotations to map object attributes to result set columns, and Java reflection to construct and populate the object.

## Annotation

Annotations are Java types that are preceded by an “@” symbol.  Java has had annotations since the 1.5 release and they have shaped the way applications are designed. The Spring and Hibernate are great examples of frameworks that rely heavily on annotations to enable various behaviors.

Annotation assigns extra metadata to the source code it's bound to. By adding an annotation to a method, interface, class, or field, we can:

- Inform the compiler about warnings and errors
- Manipulate source code at compilation time
- Modify or examine behavior at runtime

For this solution we will create an `DbTable` annotation that will carry a single attribute, `columnName`, which will represent the name of the column in the result set.

<pre name="code" class="java">
package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {
	public String columnName();
	public boolean isId() default false;
	public int keySeq() default -1;
	public boolean isScd() default false;
}
</pre>

`@Retention(RetentionPolicy.RUNTIME)}` means that the annotation can be accessed via reflection at runtime. If this directive was not set, the defalut, `@Retention(RetentionPolicy.CLASS)}`, is be used, which is only avaliable at compile time and the annotation will not be preserved at runtime, and thus not available via reflection.

## The target POJO
Object state is often persisted in Relational Database Management Systems (RDMS) system, however RDMSs represent data in a tabular format whereas object-oriented languages represent it as an interconnected graph of objects. The `DbTable` metadata annotation will be used to **configure** the POJO, specifically mapping the POJO's attribute to the column name within the result set (containing the result of the executed SQL).

To set up the configuration, we simply include the annotation before the attribute definition and specify the result set column name it corresponds to.  For example.

<pre name="code" class="java">
public class FutureSkill {
	
	@DbTable(columnName ="FUTURE_SKILL_ID",isId=true)    
	private int  futureSkillId;
	@DbTable(columnName ="CNUM",keySeq=1) 
	private String cnum;
	@DbTable(columnName ="TARGET_JRS",keySeq=2) 
	private String targetJrs;
	@DbTable(columnName ="TYPE_OF_SKILLING",keySeq=3) 
	private String typeOfSkilling;
	@DbTable(columnName ="CURRENT_BUSINESS_VALUE")
	private String currentBusinessValue;
    ...
	@DbTable(columnName ="EFF_TMS",isScd=true)    
	private Timestamp effTms;
	@DbTable(columnName ="EXPIR_TMS",isScd=true)    
	private Timestamp expirTms;
	@DbTable(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
    ...
</pre>

Here, the result set colunmn `CNUM` would map to the `FutureSkill` class' `cnumId` attribute, `TARGET_JRS` to `targetJrs`, etc. Not every POJO attribute must be mapped to the result set and vice versa. This keeps the SQL and result set decoupled from the object. Therefore, the POJO logic can derives new attributes for inclusion into a target repository as part of a **transformation** that are not required to be coded into a placeholder in the **extract** SQL.

## Getting the Result Set and creating new objects
A database query, using an SQL (Structured Query Language) statements, read data from a RDBMS and return the data in a **result set**.  The `java.sql.ResultSet` interface represents the result set of a database query.

A `ResultSet` object maintains a cursor that points to the current row in the result set. The term "result set" refers to the row and column data contained in a `ResultSet` object.

The methods of the ResultSet interface can be broken down into three categories −

- **Navigational methods** − Used to move the cursor around.
- **Get methods** − Used to view the data in the columns of the current row being pointed by the cursor.
- **Update methods** − Used to update the data in the columns of the current row. The updates can then be updated in the underlying database as well.

The `getSelectQuery()` method below is contained in a Data Managment utility class. The method returns a `List` of some type, `&lt;T&gt;`, and requires the parameters:

- **Class to contain each row of the result set** (java.lang.Class&lt;T&gt;) - Instances of the class `Class` which represent classes and interfaces in a running Java application. An object is an instance of a class. A class itself is a template or blueprint from which objects are created.
<br/><br/>
`Class` has no public constructor. Instead, Class objects are constructed automatically by the Java Virtual Machine as classes are loaded and by calls to the defineClass method in the class loader. Therefore, if the result set contained a collection of object `X`, we'd pass `X.class`. In this example, where the results will be loaded into an instance of the `FutureSkill` object, we'd pass in `FutureSkill.class`.

- **The database connection** (java.sql.Connection) - A connection (session) with a specific database. SQL statements are executed, and results are returned within the context of a connection.

- **The SQL statement** (java.lang.String) - The SQL to use to generate the result set.

The method is below.

<pre name="code" class="java">
public static &lt;T&gt; List&lt;T&gt; getSelectQuery(Class&lt;T&gt; type, Connection conn, String query) throws SQLException {
    List&lt;T&gt; list = new ArrayList&lt;T&gt;(); //Return an empty array, instead of null, if the query has no rows;
    try {
        Statement stmt = conn.createStatement();
        ResultSet rst = stmt.executeQuery(query);
        Constructor&lt;T&gt; constructor = type.getConstructor();
        while (rst.next()) {
          T t = constructor.newInstance();
          loadResultSet2Object(rst, t);
          list.add(t);
        }
        conn.close();
    } 
    catch (InvocationTargetException | InstantiationException | IllegalArgumentException 
            | IllegalAccessException    | NoSuchMethodException e) {
      throw new RuntimeException("Unable to construct "+type.getName()+ " object: " + e.getMessage(), e);
  } 
    return list;
}
</pre>



1. From the `Connection`, create a `java.sql.Statement` object. A Statment object is used for executing a static SQL statement and returning the results it produces.
1. Use the Statement's `executeQuery()` method which will executes the given SQL statement, which returns a single `ResultSet` object.
1. Use the ResultSet's `next()` method to move the cursor to the next row (beginning with the first row). This method returns false if there are no more rows in the result set.
1. Call the Class' `getConstructor()` method that returns a `Constructor` object that reflects the specified public constructor of the class represented by this Class object.
1. Instantiate/create an instance of the `Class` (passed as the first argument of the method) using the Constructor's `newInstance()` method. This creates an instance of the passed in class (`t`). Note that this replaces the deprecated `Class.newInstance()` method, that internally itself use the `Constructor.newInstance()` method to create the object.
1. Call the `loadResultSet2Object()` method (see next section) to load the ResultSet row into each defined attribute of the newly created object.
1. Once the new object has been populated with the ResultSet row values, add the new object to the list.
1. When there are no more rows in the result set, return the list.


## Using reflection to create and populate objects

Next, we'll look at how we use reflection to populate the object, checking for and handling Java primitives (non-objects) and non-mapped columns or attributes.

<pre name="code" class="java">
public static void loadResultSet2Object(ResultSet rst, Object object)
		throws IllegalArgumentException, IllegalAccessException, SQLException {
	Class&lt;?&gt; zclass = object.getClass();
	for (Field field : zclass.getDeclaredFields()) {
		field.setAccessible(true);
		DbTable column = field.getAnnotation(DbTable.class);
		if (column!=null) {
			if (hasColumn(rst,column.columnName())) {
				Object value = rst.getObject(column.columnName());
				Class&lt;?&gt; type = field.getType();
				if (isPrimitive(type)) { // check primitive type
					Class&lt;?&gt; boxed = boxPrimitiveClass(type); // box if primitive
					value = boxed.cast(value);
				}
				field.set(object, value);
			}
		}
	}
}
</pre>

The `loadResultSet2Object()` method:

1. Use thee `getClass()` method from the Object passed (the newly created object) to creates an instance of `java.lang.Class` object. Note that `Class&lt;?&gt;` is used as the definition because the class being modeled is unknow at design time.
1. Using the Class' `getDeclaredFields()` method, iterate through the classes `Field`s. The method returns an array of Field objects reflecting all the fields declared by the class or interface represented by this Class object. This includes public, protected, default (package) access, and private fields, but excludes inherited fields. The elements in the array returned are not sorted and are not in any particular order. This method returns an array of length 0 if the class or interface declares no fields, or if this Class object represents a primitive type, an array class, or void.<br/>
The `Field` object is part of the `java.lang.reflect` packagee that provides classes and interfaces for obtaining reflective information about classes and objects. It provides information about, and dynamic access to, a single field of a class or an interface.
1. For each `Field` we first set the `Accessible` attribute to `true`.  A value of true indicates that the reflected object should suppress Java language access checking when it is used, which allows us to update the field/attribute value without having to go through the "setter" methods (which would require us to both retrieve the method and execute it, using reflection as well).
1. Next it returns this Fields's annotation for the specified type (DbTable.class) if such an annotation is present. Checking for `null` allows us to loosly couple the Java Object and the Result Set, i.e., not every class attribute must be in the Result Set.
1. If the field/attribute was annotated with `DbTable`, check that the `columnName` associated with field is in the result set using the   `hasColumn()` method (below). 
1. If the `columnName` is in the result set, retrive the value. The value is retrieved as an `Object` since we don't know the type of the column yet.
1. Before assigning the value to the object's field, we have to check if the type is one of the Java primatives (non-object) types, e.g., int, float, boolean, small. This is done in the `isPrimitive()` method (below).
1. If so, the `boxPrimitiveClass()` method (below) returns the object that can be used for each of the primitive types.
1. When the object class is returned, we cast value the original value to the object representation of the primitive.
1. Finally, we set the field with the value from the result set.

### Supporting methods

**hasColumn()**

<pre name="code" class="java">
public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columns = rsmd.getColumnCount();
    for (int x = 1; x <= columns; x++) {
        if (columnName.equals(rsmd.getColumnName(x))) {
            return true;
        }
    }
    return false;
}
</pre>

**isPrimitive()**

<pre name="code" class="java">
public static boolean isPrimitive(Class&lt;?&gt; type) {
    return (type == int.class || type == long.class || type == double.class || type == float.class
            || type == boolean.class || type == byte.class || type == char.class || type == short.class);
}
</pre>

**boxPrimitiveClass()**

<pre name="code" class="java">
public static Class&lt;?&gt; boxPrimitiveClass(Class&lt;?&gt; type) {
    if (type == int.class) {
        return Integer.class;
    } else if (type == long.class) {
        return Long.class;
    } else if (type == double.class) {
        return Double.class;
    } else if (type == float.class) {
        return Float.class;
    } else if (type == boolean.class) {
        return Boolean.class;
    } else if (type == byte.class) {
        return Byte.class;
    } else if (type == char.class) {
        return Character.class;
    } else if (type == short.class) {
        return Short.class;
    } else {
        String string = "class '" + type.getName() + "' is not a primitive";
        throw new IllegalArgumentException(string);
    }
}
</pre>