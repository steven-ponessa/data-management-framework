---
title: Calling from JavaScript
permalink: en/using-services/calling-from-javascript
abstract: >- # this means to ignore newlines until "baseurl:"
  Calling services from JavaScript Code.
---

JavaScript provides some built-in browser objects and some external open source libraries to interact with the APIs.
Here are the possible ways to make an API call:
1. XMLHttpRequest
1. jQuery
1. fetch
1. Axios

# XMLHttpRequest
Before ES 6 comes out, the only way to make an HTTP request in JavaScript was `XMLHttpRequest`. It is a built-in browser object that allows us to make HTTP requests in JavaScript.

By default we receive the response in the string format, we need to parse into JSON.

`XMLHttpRequest` was deprecated in ES 6 by the introduction of `fetch`. But still, we are using `XMLHttpRequest` when we need to work with old browsers and don’t want polyfills.

- All modern browsers support the `XMLHttpRequest` object to request data from a server.
- It works on the oldest browsers as well as on new ones.
- It was deprecated in ES6 but is still widely used.

**Sample code**
<pre name="code" class="js">
function callApi() {
	const url = window.location.protocol+"//"+window.location.host+'/api/v1/eds-country-tax/countries?orderByCols=CTRY_NM';

	const request = new XMLHttpRequest();
	request.open('GET', url, true);

	//Define successful onload function
	request.onload = function() {
		if (request.status === 200) {
			const data = JSON.parse(request.responseText);
		} else {
			// Reached the server, but it returned an error
			console.error('An error occured ${request.status} - ${request.statusText}');	
	  }   
	}

	request.onerror = function() {
	  console.error('An error occurred fetching the JSON from ' + url);
	};

	request.send();
}
</pre>

# Fetch
Fetch allows you to make an HTTP request in a similar manner as `XMLHttpRequest` but with a straightforward interface by using promises. It’s not supported by old browsers (can be polyfilled), but very well supported among the modern ones. 

- The Fetch API provides an interface for fetching resources (including across the network) in an asynchronous manner.
- It returns a `Promise`
- It is an object which contains a single value either a Response or an Error that occurred.
- `then()` tells the program what to do once Promise is completed.

**Sample code**
<pre name="code" class="js">
const url = window.location.protocol+"//"+window.location.host+'/api/v1/eds-country-tax/countries?orderByCols=CTRY_NM';

fetch(url)
	.then(response =>{
		return response.json();
	}).then(data =>{
		console.log(data);
	}
)
</pre>

# jQuery
jQuery has many methods to handle asynchronous HTTP requests. In order to use jQuery, we need to include the source file of jQuery, and `$.ajax()` method is used to make the HTTP request.  The `$.ajax()` method takes many parameters, some of which are required and others optional. It contains two callback functions `success` and `error` to handle the response received.

- It performs asynchronous HTTP requests.
- Uses $.ajax() method to make the requests. 

You need to include the jQuery javascript libraries, e.g., 
<pre name="code" class="js">
&lt;script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"&gt;&lt;/script&gt;
</pre>

Thereafter you can call the APIs using jQuery's `$.ajax()` method.

**Sample code**
<pre name="code" class="js"
const url = window.location.protocol+"//"+window.location.host+'/api/v1/eds-country-tax/countries?orderByCols=CTRY_NM';

$(document).ready(function(){
    $.ajax({
        url: url,
        type: "GET",
        success: function(result){
            console.log(result);
        }
    })
})

</pre>

# Axios
Axios is an open-source library for making HTTP requests and provides many great features, and it works both in browsers and Node.js. It is a promise-based HTTP client that can be used in plain JavaScript and advanced frameworks like React, Angular, and Vue.js.

- It is an open-source library for making HTTP requests.
- It works on both Browsers and Node js
- It can be included in an HTML file by using an external CDN
- It also returns promises like fetch API

You need to include the Axios javascript libraries, e.g., 
<pre name="code" class="js">
&lt;script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js">&gt;&lt;/script&gt;
</pre>

Thereafter you can call the APIs using jQuery's `get()` method.

**Sample code**
<pre name="code" class="js">
const url = window.location.protocol+"//"+window.location.host+'/api/v1/eds-country-tax/countries?orderByCols=CTRY_NM';

axios.get(url)
  .then(response =>{
	console.log(response.data)
})

</pre>


