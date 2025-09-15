---
title: Data Marshalling
permalink: en/learning-framework/data-marshalling
abstract: >- # this means to ignore newlines until "baseurl:"
   Data Marshalling is the process of transforming the memory representation of an object into a data format suitable for storage or transmission and vice versa. Within the scope of this framework, it also referes to transforming between different memory representations of an object. The framework's Data Marshalling classes have methods that can marshal data from a relational table, Excel worksheet, or CVS file to a Java Object and visa versa WITHOUT knowing the source or target until runtime. 
---

# Marshalling to and from RDBMSs

Data marshalling refers to the process of transforming data from one representation to another in order to facilitate its transmission or storage. It is typically used when data needs to be transferred between different systems or components that may have different data formats, structures, or protocols.

The purpose of data marshalling is to ensure that data can be effectively communicated or stored in a format that is compatible with the receiving system or component. This involves converting data from its native representation, such as a data structure or object, into a format suitable for transmission or storage, such as binary data, XML, JSON, or another specific protocol.

Data marshalling often involves two main operations:

1. **Serialization**: This operation involves converting data into a linear format that can be easily transmitted or stored. Serialization takes complex data structures or objects and transforms them into a sequence of bytes or a string representation. This serialized data can then be transmitted over a network or written to a file.

2. **Deserialization**: This operation is the reverse of serialization and involves reconstructing the original data from the serialized format. Deserialization takes the linear representation of data and reconstructs it into its original form, such as a data structure or object, so that it can be processed or used by the receiving system or component.

Data marshalling is essential in scenarios where systems or components need to communicate or exchange data, especially when they are built using different programming languages, frameworks, or platforms. By standardizing the format of the data during marshalling, interoperability can be achieved, allowing different systems to work together seamlessly.

The data management framework, uses the **Jackson** frameworks to provide built-in functionality for data marshalling, handling the serialization and deserialization processes automatically. The Jackson libraries abstract the complexities of data marshalling, making it easier to exchange data between different systems or components.  Jackson is a high-performance **JSON** (**JavaScript Object Notation**) library for Java. It provides easy-to-use APIs for serializing Java objects to JSON and deserializing JSON back to Java objects. 

# Converting JSON to CSV

When you create a `@RestController` in a Spring Boot application to define API endpoints, Jackson JSON ObjectMapper is the default HTTP Converter which does two things:

1. Convert the incoming JSON Request Body to Java Object of your method `@RequestBody` argument. Generally used in POST HTTP methods.
1. Convert the returned Java Object to JSON Response. Generally used in GET HTTP methods.

Its good to know that the process of converting:

1. Java Object to JSON is known as **Marshalling**, or **Serialization**, and
1. JSON to Java Object is called **Unmarshalling**, or **Deserialization**

The framework relies heavily on **Jackson** packages for marshalling and unmarshalling, specifically  a combination of `ObjectMapper` and `CSVMapper` to convert between JSON and CSV and vica versa.

## Dependencies
Let's first add the following dependencies to the pom.xml:

<pre name="code" class="xml">
&lt;dependency&gt;
    &lt;groupId&gt;com.fasterxml.jackson.core&lt;/groupId&gt;
    &lt;artifactId&gt;jackson-databind&lt;/artifactId&gt;
    &lt;version&gt;2.11.1&lt;/version&gt;
&lt;/dependency&gt;
</pre>

This dependency will also transitively add the following libraries to the classpath:

1. jackson-annotations
1. jackson-core

Always use the latest versions from the Maven central repository for [jackson-databind](https://search.maven.org/classic/#search%7Cgav%7C1%7Cg%3A%22com.fasterxml.jackson.core%22%20AND%20a%3A%22jackson-databind%22).

## Data Structure
Before we reformat a JSON document to CSV, we need to consider how well our data model will map between the two formats.

So first, let's consider what data the different formats support:

- We use JSON to represent a variety of object structures, including ones that contain arrays and nested objects
- We use CSV to represent data from a list of objects, with each object from the list appearing on a new line

This means that if our JSON document has an array of objects, we can reformat each object into a new line of our CSV file. So, as an example, let's use a JSON document containing the following list of items from an order:

<pre name="code" class="javascript">
[ {
  "item" : "No. 9 Sprockets",
  "quantity" : 12,
  "unitPrice" : 1.23
}, {
  "item" : "Widget (10mm)",
  "quantity" : 4,
  "unitPrice" : 3.45
} ]
</pre>

## Java Object to JSON
Let's see a first example of serializing a Java object into JSON using the writeValue method of the ObjectMapper class:

<pre name="code" class="java">
ObjectMapper objectMapper = new ObjectMapper();
Order order = new Order("No. 9 Sprockets", 12, 1.23);
objectMapper.writeValue(new File("target/order.json"), order);
</pre>

The output of the above in the file will be:

<pre name="code" class="javascript">
{"item" : "No. 9 Sprockets", "quantity" : 12, "unitPrice" : 1.23}
</pre>

The methods `writeValueAsString` and `writeValueAsBytes` of `ObjectMapper` class generate a JSON from a Java object and return the generated JSON as a string or as a byte array:

<pre name="code" class="java">
String orderJsonString = objectMapper.writeValueAsString(order);
</pre>

## JSON to Java Object
Below is a simple example of converting a JSON String to a Java object using the `ObjectMapper` class using the `orderJsonString` from above:

<pre name="code" class="java">
Order order = objectMapper.readValue(orderJsonString, Order.class);	
</pre>

The `readValue()` method also accepts other forms of input, such as a file containing JSON string or a URL:

<pre name="code" class="java">
Order order1 = objectMapper.readValue(new File("src/test/resources/json_order.json"), Order.class);

Order order2 = objectMapper.readValue(new URL("https://server/path/resource"), Order.class);
</pre>

## Creating a Java List From a JSON Array String
We can parse a JSON in the form of an array into a Java object list using a `TypeReference`:

<pre name="code" class="java">
List&lt;Order&gt; orders = objectMapper.readValue(jsonOrdersString, new TypeReference&lt;List&lt;Order&gt;&gt;(){});
</pre>



## Read JSON and Write CSV

We'll use the field names from the JSON document as column headers, and reformat it to the following CSV file:

<pre name="code" class="javascript">
item,quantity,unitPrice
"No. 9 Sprockets",12,1.23
"Widget (10mm)",4,3.45
</pre>

First, we use Jackson's `ObjectMapper` to read our example JSON document into a tree of JsonNode objects:

<pre name="code" class="java">
JsonNode jsonTree = new ObjectMapper().readTree(new File("src/main/resources/orderLines.json"));
</pre>

Next, let's create a `CsvSchema`. This determines the column headers, types, and sequence of columns in the CSV file. To do this, we create a `CsvSchema` Builder and set the column headers to match the JSON field names:

<pre name="code" class="java">
Builder csvSchemaBuilder = CsvSchema.builder();
JsonNode firstObject = jsonTree.elements().next();
firstObject.fieldNames().forEachRemaining(fieldName -&gt; 
							{csvSchemaBuilder.addColumn(fieldName);} );
CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
</pre>

Then, we create a `CsvMapper` with our `CsvSchema`, and finally, we write the `jsonTree` to our CSV file:

<pre name="code" class="java">
CsvMapper csvMapper = new CsvMapper();
csvMapper.writerFor(JsonNode.class)
  .with(csvSchema)
  .writeValue(new File("src/main/resources/orderLines.csv"), jsonTree);
</pre>

## Read CSV and Write JSON
Now, let's use Jackson's `CsvMapper` to read our CSV file into a List of OrderLine objects. To do this, we first create the OrderLine class as a simple POJO:

<pre name="code" class="java">
public class OrderLine {
    private String item;
    private int quantity;
    private BigDecimal unitPrice;
 
    // Constructors, Getters, Setters and toString
}
</pre>

We'll use the column headers in the CSV file to define our `CsvSchema`. Then, we use the `CsvMapper` to read the data from the CSV into a `MappingIterator` of OrderLine objects:

<pre name="code" class="java">
CsvSchema orderLineSchema = CsvSchema.emptySchema().withHeader();
CsvMapper csvMapper = new CsvMapper();
MappingIterator&lt;OrderLine&gt; orderLines = csvMapper.readerFor(OrderLine.class)
  .with(orderLineSchema)
  .readValues(new File("src/main/resources/orderLines.csv"));
</pre>

Next, we'll use the MappingIterator to get a List of OrderLine objects. Then, we use Jackson's ObjectMapper to write the list out as a JSON document:

<pre name="code" class="java">
new ObjectMapper()
  .configure(SerializationFeature.INDENT_OUTPUT, true)
  .writeValue(new File("src/main/resources/orderLinesFromCsv.json"), orderLines.readAll());
</pre>

