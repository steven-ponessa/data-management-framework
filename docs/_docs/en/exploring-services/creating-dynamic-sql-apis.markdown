---
title: Creating Dynamic SQL APIs
permalink: en/exploring-services/creating-dynamic-sql-apis
abstract: >- # this means to ignore newlines until "baseurl:"
  The ability to support dynamic SQL is one of the most powerful functions in the framework. It enables RESTful API to be created and run against any defined backend data source/relational databases dynamically, with no coding required. It extends the capabilities of the framework from beyond access to pre-defined entities or hierarchies to sources that require varying attributes, row aggregation, consolidation, and joining across multiple tables.  Users choose a data source, dynamically add an SQL template, and specify zero to many substitution variables with or without default values (passed as parameters in the URL), and the dynamic API is instantly available; without writing a single line of code or re-deploying the application.
---

# Variable Substitution

In many APIs, however, the REST-endpoint doesn't correspond to a particular table but rather an aggregate of tables and columns. In those cases, the framework allows users to configure dynamic SQL with 0 to many substitution variables and conditional logic.

The framework allows you to create SQL templates with substitution variables. These variables are enclosed in curly brackets `{}`, allowing dynamic substitution of values at runtime. The parameters for these variables must be defined with specific attributes to ensure proper handling and substitution.

## Substitution Variables Syntax
In your SQL template, you can define substitution variables are enclosed in curly brackets `{}` like this:
<pre name="code" class="sql">
SELECT * 
FROM users 
WHERE user_id = {userId} 
  AND created_at > {startDate};
</pre>

## Parameter Attributes
Each substitution variable must have a corresponding parameter definition with the following attributes:

- **name:** The name of the parameter. This must exactly match the substitution variable name enclosed in curly brackets in the SQL template.
- **description:** A brief description of what the parameter represents.
- **dataType:** The data type of the parameter. Supported data types include:
  - **String**
  - **Number**
  - **Date**
  - **Timestamp**
  - **List**

  The framework will handle formatting for `Date` and `Timestamp` data types and will enclose `String` values in quotes automatically.

- **length:** The maximum length of the parameter value (if applicable).
- **requiredFlg:** Indicates whether the parameter is required (`true`) or optional (`false`).
- **defaultValue:** The default value to use if the parameter is omitted by the user.

## Example Parameter Definition
Here's an example of how you might define parameters for the above SQL template:

<pre name="code" class="js">
{
  "parameters": [
    {
      "name": "userId",
      "description": "The unique identifier of the user",
      "dataType": "String",
      "length": 10,
      "requiredFlg": true,
      "defaultValue": null
    },
    {
      "name": "startDate",
      "description": "The start date for filtering results",
      "dataType": "Date",
      "length": null,
      "requiredFlg": false,
      "defaultValue": "2022-01-01"
    }
  ]
}
</pre>

## Framework Operation
1. **Parameter Matching:** The framework pulls and checks the parameters against the request’s variables.
2. **Validation:** It ensures that all required parameters are present and that they conform to the specified data types and lengths.
3. **Substitution:** The framework substitutes the variables in the SQL template with the provided parameter values.
4. **SQL Generation:** The resulting SQL statement is generated and ready for execution.

So given the SQL template
<pre name="code" class="sql">
SELECT * 
FROM users 
WHERE user_id = {userId} 
  AND created_at > {startDate};
</pre>

With the parameters above of `userId` and `startDate` (assuming that the **data source** is `bmsiw` and the dynamic SQL name is `bms-user-by-start-date` )

```
http://<host>/api/v1/dyn/sql/bmsiw/bms-charge-groups?userId=someuser@company.com&startDate=6/12/2000
```

The framework would generate and execute the following SQL:
So given the SQL template
<pre name="code" class="sql">
SELECT * 
FROM users 
WHERE user_id = 'someuser@company.com'
  AND created_at > '2000-06-12';
</pre>


This mechanism ensures flexibility and safety when creating dynamic SQL queries. By defining clear parameters with appropriate attributes, you can leverage the full power of the framework’s SQL template functionality.

---

This should help end users understand how to use and define substitution variables within SQL templates effectively. If you need any further details or examples, please let me know!


# Conditional Logic

The `<#if>` statement in Apache FreeMarker templates is used to conditionally include content in the output based on whether a given condition is true or false. The syntax is similar to `if` statements in many programming languages. Here's a basic explanation of how it works:


## Syntax
<pre name="code" class="xml">
<#if condition>
  <!-- Content to display if the condition is true -->
</#if>
</pre>

You can also use `<#else>` and `<#elseif>` to handle additional cases:

<pre name="code" class="xml">
<#if condition>
  <!-- Content to display if the condition is true -->
<#else>
  <!-- Content to display if the condition is false -->
</#if>
</pre>

<pre name="code" class="xml">
<#if condition1>
  <!-- Content to display if condition1 is true -->
<#elseif condition2>
  <!-- Content to display if condition2 is true -->
<#else>
  <!-- Content to display if none of the conditions are true -->
</#if>
</pre>

## Examples

1. **Simple if statement:**
   <pre name="code" class="xml">
   <#if user.age >= 18>
     <p>User is an adult.</p>
   </#if>
   </pre>

2. **If-else statement:**
   <pre name="code" class="xml">
   <#if user.age >= 18>
     <p>User is an adult.</p>
   <#else>
     <p>User is a minor.</p>
   </#if>
   </pre>

3. **If-elseif-else statement:**
   <pre name="code" class="xml">
   <#if user.age >= 18>
     <p>User is an adult.</p>
   <#elseif user.age >= 13>
     <p>User is a teenager.</p>
   <#else>
     <p>User is a child.</p>
   </#if>
   </pre>

## Conditions
The conditions used in `<#if>` statements can be any valid expression, including comparisons, logical operators, and checks for null values.

- **Comparisons:** `==`, `!=`, `<`, `>`, `<=`, `>=`
- **Logical operators:** `&&` (and), `||` (or), `!` (not)
- **Null checks:** `?has_content`, `?is_null`, `?exists` or `??`

## Example with Logical Operators and Null Checks
<pre name="code" class="xml">
<#if user.isActive && user.age?exists>
  <p>User is active and age is specified.</p>
<#else>
  <p>User is not active or age is not specified.</p>
</#if>
</pre>

## Real world example

In the example below the dynamic SQL named `bms-service-resource-type`, The data source is `bmsiw`, and the parameters are:

| **Name** | **Description** | **Data Type** | **Length** | **Requird?** | **Default Value** |
|----------|-----------------|---------------|------------|--------------|-------------------|
| country  | Originating Country Code<br/>(`*` can be used to retrieve all country codes) | String | 3 | True | 897 |
| company  | Originating Company Code<br/>(`*` can be used to retrieve all company codes)  | String | 15 | True | IBM |
| status   | Record Status Code | String | 1 | True | O |
| year     | Year (CHRGPLAN_CD) | String | 4 | True | 2024 |

For this API, we'd prefer NOT to return all countries, companies, status, and years, however we do want users to have the ability to request all countries and companies.  

Thus, if the `country` and/or `company` parameter has not been specified, we want the framework to use the default value (i.e., keep `AND orig_country_cd = {country}` and/or `AND orig_company_cd = {company}` in the `WHERE` clause). Also, if `country` and/or `company` parameter has been specified but is equal to the wildcard value (`*`) we want the framework to retrieve all values regardless of  `country` and/or `company` (i.e., exclude country and companny from the `WHERE` clause).

To handle this situation you can use the built-in functions `?exists` and `?has_content`, combined with string comparison.

Here’s how we structured the conditional logic in the SQL template:

<pre name="code" class="sql">
SELECT
	orig_country_cd,
	orig_company_cd,
	SERVICE_RESTYP_CD ,
	count(*) AS cnt
FROM
	bmsiw.base_labor_rate_v
WHERE
	chrgplan_cd = {year}
	<#IF !(country?exists) || !(country?has_content) || country!='*'>
		AND orig_country_cd = {country}
	</#IF>
	<#IF !(company?exists) || !(company?has_content) || company!='*'>
		AND orig_company_cd = {company}
	</#IF>
	AND status = {status}
GROUP BY
	orig_country_cd,
	orig_company_cd,
	SERVICE_RESTYP_CD
</pre>

The logic ensures that the country and/or company filter is added to the `WHERE` if a country and/or company is not specified (so the default value will be used) AND is NOT added if a country and/or company is specified with a "wildcard" (`*`).

- The `country` key does not exist in the model (`!(country?exists`)).
- The `country` key exists but is null or empty (`!(country?has_content)`).
- The `country` key exists and its value is not `*` (`country!="*"`).

## Summary
- `<#if>` is used to include content conditionally.
- `<#else>` is used for alternative content if the condition is false.
- `<#elseif>` is used for multiple conditions.
- Conditions can involve comparisons, logical operators, and null checks.

These basic structures and examples should help you understand how to use `<#if>` statements in Apache FreeMarker templates. If you have any specific scenarios or further questions, feel free to ask!


# Hierarchical Result Set Conversion

The framework supports the conversion of flat result sets into hierarchical structures by using the `keys` parameter. This allows users to define a hierarchy of levels based on specified key attributes, facilitating the transformation of flat data into a nested JSON hierarchy.

## Defining the Hierarchy with Keys

To convert a flat result set into a hierarchy, you need to pass the `keys` parameter with a comma-separated list of attributes that represent the key for each level of the hierarchy.

### Example Hierarchy

Consider a geographic hierarchy where:
- **Level 1**: Geography (`GEO_CD`)
- **Level 2**: Markets (`MRKT_CD`)
- **Level 3**: Regions (`RGN_CD`)
- **Level 4**: Countries (`CTRY_CD`)

To structure this hierarchy, you would pass the parameter:

<pre name="code" class="js">
?keys=GEO_CD,MRKT_CD,RGN_CD,CTRY_CD
</pre>

### Example Data

Given a flat result set as follows:
<pre name="code" class="js">
{
  "results": [
    {
      "GEO_CD": "AM",
      "GEO_NM": "Americas",
      "MRKT_CD": "4N",
      "MRKT_NM": "Canadian Market",
      "RGN_CD": "4C",
      "RGN_NM": "Canada Region",
      "CTRY_CD": "649",
      "CTRY_NM": "Canada",
      "ISO_CTRY_CD": "CA"
    },
    {
      "GEO_CD": "AM",
      "GEO_NM": "Americas",
      "MRKT_CD": "4N",
      "MRKT_NM": "Canadian Market",
      "RGN_CD": "4G",
      "RGN_NM": "Caribbean Region",
      "CTRY_CD": "619",
      "CTRY_NM": "Bahamas",
      "ISO_CTRY_CD": "BS"
    }
    ...
  ]
}
</pre>

E.g., The geographic taxonomy from the Services Information Warehouse (a.k.a., BMSIW).  The dynamic SQL API is:
[https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/dyn/sql/bmsiw/geography-taxonomy](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/dyn/sql/bmsiw/geography-taxonomy){:target='_blank'}

### Resulting Hierarchical JSON
By adding the `keys` parameter, the flat result set is converted into a hierarchical JSON structure:
<pre name="code" class="js">
{
  "results": [
    {
      "code": "WW Top",
      "fullKey": "WW Top",
      "attributes": null,
      "children": [
        {
          "code": "AM",
          "fullKey": "AM",
          "attributes": {
            "GEO_NM": "Americas"
          },
          "children": [
            {
              "code": "4N",
              "fullKey": "AM:4N",
              "attributes": {
                "MRKT_NM": "Canadian Market"
              },
              "children": [
                {
                  "code": "4C",
                  "fullKey": "AM:4N:4C",
                  "attributes": {
                    "RGN_NM": "Canada Region"
                  },
                  "children": [
                    {
                      "code": "649",
                      "fullKey": "AM:4N:4C:649",
                      "attributes": {
                        "CTRY_NM": "Canada",
                        "ISO_CTRY_CD": "CA"
                      },
                      "children": null
                    }
                  ]
                },
                {
                  "code": "4G",
                  "fullKey": "AM:4N:4G",
                  "attributes": {
                    "RGN_NM": "Caribbean Region"
                  },
                  "children": [
                    {
                      "code": "619",
                      "fullKey": "AM:4N:4G:619",
                      "attributes": {
                        "CTRY_NM": "Bahamas",
                        "ISO_CTRY_CD": "BS"
                      },
                      "children": null
                    }
                  ]
                }
                ...
              ]
            }
          ]
          ...
        }
      ]
      ...
    }
  ]
  ...
}
</pre>

E.g., The geographic taxonomy from the Services Information Warehouse (a.k.a., BMSIW) adding the request variable `keys=GEO_CD,MRKT_CD,RGN_CD,CTRY_CD`.  The dynamic SQL API is:
[https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/dyn/sql/bmsiw/geography-taxonomy?keys=GEO_CD,MRKT_CD,RGN_CD,CTRY_CD](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/dyn/sql/bmsiw/geography-taxonomy?keys=GEO_CD,MRKT_CD,RGN_CD,CTRY_CD){:target='_blank'}

## How It Works

1. **Parameter Passing:** Include the `keys` parameter with a list of key attributes in your request.
2. **Flat to Hierarchy Conversion:** The framework will transform the flat result set into a nested hierarchical structure based on the specified key attributes.
3. **Structured Output:** The output will be a JSON object with nested levels corresponding to the hierarchy defined by the keys.

This feature is especially useful for creating structured taxonomies, enabling easy representation of complex hierarchical data in a clear and organized manner.

---

This should provide a clear understanding for end users on how to utilize the `keys` parameter to convert flat result sets into hierarchical JSON structures. If you need any further details or examples, please let me know!