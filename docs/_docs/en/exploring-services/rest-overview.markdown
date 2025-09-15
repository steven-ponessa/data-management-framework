---
title: REST overview
permalink: /en/exploring-services/rest-overview
abstract: >- 
  REST is an architecture style for designing networked applications and providing functionality using HTTP requests HTTP verbs to signal the type of action, specifically GET (Read/Select), POST (Create/Insert), PUT (Update/Update), and DELETE (Delete).  Thus, REST uses HTTP for all four CRUD (Create/Read/Update/Delete) operations.
---

# REST overview

REST stands for Representational State Transfer.  It is an architecture style for designing networked applications and providing functionality that is much simpler using complex mechanisms such as CORBA, RPC, or SOAP.   It uses simple HTTP to make calls.  The Internet, also based on HTTP, is a REST-based architecture.   RESTful applications use HTTP requests HTTP verbs to signal the type of action, specifically, using CRUD/SQL vernacular, GET (Read/Select), POST (Create/Insert), PUT (Update/Update), and DELETE (Delete).  Thus, REST uses HTTP for all four CRUD (Create/Read/Update/Delete) operations.

Like Web Services, REST is platform and language independent and Standards-based, running on top of HTTP.   Also, like Web Services, REST offers no built-in security features, encryption, session management, or QoS (Quality of Service) guarantees.  But also as with Web Services, these can be added by building on top of HTTP, for example security can be managed with username/password tokens and encryption can be used since it can run over HTTPS (the secure socket layer).
RESTful Design

REST is an architectural style, not a standard, which makes it important to understand its implene.  Consider a social media application that has users that can post messages that others can comment on, like, or share.  And, say for example it is supported by the following database schema:

![rest-sample-db-schema]({{ site.baseurl }}/assets/images/docs/rest-sample-db-schema.png){: style="width:70%;"}

For RESTful APIs we need to think in the terms of resource based **URIs (Uniform Resource Identifier)**.  To help with designing RESTful URIs think of the data as resources within a static web site.

The diagram below shows how we’d set up the directory structure and example resources (HTML) for use in a static web site.  

![rest-static-site]({{ site.baseurl }}/assets/images/docs/rest-static-site.png){: style="width:30%;"}

Thereafter, we can either ask for a <b>collection</b>, which will return a list of objects, or an <b>individual</b> object.  For example:

| Example requests  | URL      |
|:------------------|:---------|
| List of all users | `<host>/api/v1/users` |
| Individual user (`ponessa@us.ibm.com`) | `<host>/api/v1/users/ponessa@us.ibm.com` |
| List of messages for individual user (`ponessa@us.ibm.com`) | `<host>/api/v1/users/ponessa@us.ibm.com/messages` |
| Individual message (`2`) for individual user (`ponessa@us.ibm.com`) | `<host>/api/v1/users/ponessa@us.ibm.com/messages/2` |
| List of comments for individual message (`2`) for individual user (`ponessa@us.ibm.com`) | `<host>/api/v1/users/ponessa@us.ibm.com/messages/2/comments` |
| Individual comments (`1`) for individual message (`2`) for individual user (`ponessa@us.ibm.com`) | `<host>/api/v1/users/ponessa@us.ibm.com/messages/2/comments/1` |

In the URLs above parameters are passed in the form of **Path Variables**. REST API endpoints can pass data within their requests through 4 types of parameters: **Header**, **Path**, **Query String**, or in the **Request Body**.  

For GET (data access) URLs **Query string** parameters can also be used. Parameters can be passed in the query string of the endpoint, after the  **?** separator.
In the query string, each parameter is listed one right after the other with an ampersand (**&**) separating them. The order of the query string parameters does not matter. Thus, if someone wanted to look for all comments containing the term "REST" on a specific date, they could enter:

<pre name="code" class="js">
https://&lt;host&gt;/api/v1/comments?COMMENT_TXT=*REST*&COMMENT_DT=2021-10-31
</pre>

The framework also uses headers to store authentication (e.g., bearer token) and entitlement information, and the Request Body for PUT and POST requests.

## HTTP Methods & Idempotent

Thee table below

| HTTP Method | CRUD | Action | Comment |
|:------------|:----:|:-------|:--------|
| GET         | R    | SELECT/Read |    |	 
| POST        | C    | INSERT | To a “collection” URI with detail in the body of the POST |
| PUT         | U    | UPDATE | To a “instance” URI with detail in the body of the POST |
| DELETE      | D    | DELETE |         |

Why do we use a different method for INSERT and UPDATE?  The answer is **Idempotent**. 

By definition Idempotent is the property of certain operations in mathematics and computer science that can be applied multiple times without changing the result beyond the initial application.  From a RESTful service standpoint, for an operation (or service call) to be idempotent, clients must be able to make that same call repeatedly while producing the same result. 
 
Obviously GET/Read is idempotent.  A read only operation may be executed multiple times without changing the values.  If we make a call for the same resource multiple times nothing changes.  

A DELETE, on the surface, would not appear to be idempotent, however, when you take a closer look, you see that it is. Consider a DELETE on `/messages/10`.  The first time it is executed the message with `MESSAGE_ID=10` is deleted.  If this method is re-submitted there is no affect on the messages table.  Subsequent executions should return a response code `404 – Not Found` – however, the message data will not change.  

Similarly, a PUT/Update executed repeatedly will only change the value once.  If you submit a PUT on `/messages/10` with a change to the MSG_TXT in the POST’s body, the API will change the text of the message.  However subsequent PUTs will only re-update the value to the same value the prior update performed, thus only the initial application of the method results in any change. 

Finally a POST/Insert is Non-idempotent.  Every duplicate call creates a new instance of a resource on the server or in the database.  By associating the POST method to the Insert operation allows for some client safeguards.  All browsers have a refresh button that re-submits the last request the browser submitted.  Since the browser understands that POST methods are non-idempotent, it asks the user if they want to re-POST the request and gives them the opportunity to decline.  

## REST/HTTP Responses

It used to be that **XML (eXtensible Markup Language)** was the only option for returning data from web services however **JSON (JavaScript Object Notation)** has been gaining popularity in that it is more compact and is easily consumable by web based JavaScript client widgets.  

XML is a markup language used to describe the content and structure of data in a document. It is a simplified version of Standard Generalized Markup Language (SGML). XML is an industry standard for delivering content on the Internet. Because it provides a facility to define new tags, XML is also extensible.

Like HTML, XML uses tags to describe content. However, rather than focusing on the presentation of content, the tags in XML describe the meaning and hierarchical structure of data. This functionality allows for the sophisticated data types that are required for efficient data interchange between different programs and systems. Further, because XML enables separation of content and presentation, the content, or data, is portable across heterogeneous systems.

The XML syntax uses matching start and end tags (such as `<name>` and `</name>`) to mark up information. Information delimited by tags is called an element. Every XML document has a single root element, which is the top-level element that contains all the other elements. Elements that are contained by other elements are often referred to as sub-elements. An element can optionally have attributes, structured as name-value pairs that are part of the element and are used to further define it.

JSON is a lightweight, language independent data-interchange format. It is easy for humans to read and write and easy for machines to parse and generate. In JSON an object is an unordered set of name/value pairs that begins with { (left brace) and ends with } (right brace). JSON is built on two structures:

1. A collection of name/value pairs. Each name is enclosed in double quotes and is followed by : (colon). A value can be a string in double quotes, number, true, false, null, an object, or an array. The name/value pairs are separated by , (comma) and the structures can be nested.

1. An ordered list of values (array).

Looking at the format themselves, consider a “Message” class that has a syntax of

<pre name="code" class="java">
public class Message {
	private long messageId = -1;
	private String userId = null;
	private Date msgDt = null;
	private String msgText = null;
…
} 
</pre>

The JSON you’d return could be something like:
<pre name="code" class="js">
{	  "messageId": 1
	, "userId": "ponessa@us.ibm.com"	
	, "msgDt": "2015-11-04"
	, "msgText": "Lorem ipsum onsectetuer adipiscing"
}
</pre>

Or in XML

<pre name="code" class="xml">
<message>
	<messageId>1</messageId>
	<userId>ponessa@us.ibm.com</userId>
	<msgDt>2015-11-04</msgDt>
	<msgText>Lorem ipsum onsectetuer adipiscing</msgText>
</message>
</pre>

## HTTP Response Header

The response type is placed in the HTTP Response Header.  The `Content Type` attribute of the HTTP Response (`HttpServletResponse.setContentType`) identifies the format of the response.  Examples are `application/json`, `tex`.

## HTTP Response Codes

The HTTP protocol requires the very first line in the HTTP response be a “status code” consisting of a numeric code and a short phrase.  There are 5 classes of status codes and the list below is a summary of these.

- 100s – Information Codes
- 200s – Success Codes
    - 200 – OK (GET)
    - 201 – Created (POST)
    - 204 – No Content (i.e., no response content, PUT and DELETE)
- 300s – Redirect Codes
- 400s – Client Errors
    - 400 – Bad Request
    - 401 – Unauthorized
    - 403 – Forbidden
    - 404 – Not found
    - 415 – Unsupported media type
- 500s – Server Error

## HATEOAS

HATEOAS stands for **Hypermedia As The Engine Of Application State** (possibly the worst acronym ever).  To understand what HATEOAS is, think of a static web site.  You don’t need a user’s guide to use and navigate the site; there are links that lead you to different content and resources.  What if we apply the same concept to API calls?  HATEOAS is where additional links are added into the results of the RESTful call.  For example, consider a RESTful API for message id=20, we could add a link for the collection of comments, likes, and shares in this response.

<pre name="code" class="js">
{	  "messageId": 20
	, "userId": "ponessa@us.ibm.com"	
	, "msgDt": "2021-11-04"
	, "msgText": "Lorem ipsum onsectetuer adipiscing"
	, "commentsUri": "api/messages/20/comments"
, "likesUri": "api/v1/messages/20/likes"
, "sharesUri": "api/v1/messages/20/shares"
, "href": "api/v1/messages/20"
}
</pre>

This is similar to URL links in static web pages.

Notice the last link `href`.  This is the link the API itself or the resource URI of itself.  Now if we extend that to all the possible linkages we can get rid of the `commentsUri`, `likesUri`, and `sharesUr` and replace it with `href`, however we’d have to add something to differentiate the different types.  If we think of `<link>`s in HTML, the `href` attribute and differentiate the types via the `rel` or `relationship` attribute.  For example:

<pre name="code" class="html">
<link rel="stylesheet" href="css/fonts.css"/>
</pre>
This tells the HTML page that the link is a stylesheet.  We could thereafter embed all the links in the response and use the “rel” property to make it clear what we are adding the link for.  The response would now look like:

<pre name="code" class="js">
{ "messageId": 20
, "userId": "ponessa@us.ibm.com"	
, "msgDt": "2021-11-04"
, "msgText": "Lorem ipsum onsectetuer adipiscing"
, "links": [
     { "href": "api/messages/20/comments", "rel": "comments" }
   , { "href": "api/messages/20/likes", "rel": "likes" }
   , { "href": "api/messages/20/shares", "rel": "shares" }
   , { "href": "api/messages/20", "rel": "self" }
  ]
}
</pre>

## Richardson Maturity Model
The Richardson Maturity Model is a way of REST APIs to be classified and the “RESTfulnes” of an API determined.

- Level 0 – Non-RESTful.  An example would be SOAP Web Service.  There is a single end point (URI) that receives messages.  All details of the operation are in the POST body.  “Swamp of POX (Plain Old XML)”.  No HTTP concepts are used in Level 0 to communicate between client and server.
- Level 1 – Integrate resource URIs
- Level 2 – Introduce standard HTTP Methods.  The URI specifies the resource to be acted upon and the HTTP method dictates the action.  There also needs to be use of the HTTP status codes for idempotent and non- idempotent.
- Level 3 – Introduce HATEOAS.


