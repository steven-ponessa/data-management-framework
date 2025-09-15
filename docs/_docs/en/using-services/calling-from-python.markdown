---
title: Calling from Python
permalink: en/using-services/calling-from-python
abstract: >- # this means to ignore newlines until "baseurl:"
  Calling services from Python Code.
---

# Pre-requisites

In order to work with APIs in Python, we need tools that will make those requests. In Python, the most common library for making requests and working with APIs is the [requests library](https://2.python-requests.org/en/master/). The requests library isn’t part of the standard Python library, so you’ll need to install it to get started.

If you use **pip** to manage your Python packages, you can install requests using the following command:

<pre name="code" class="vb">
pip install requests
</pre>

If you use [conda](https://www.anaconda.com/), the command you’ll need is:

<pre name="code" class="vb">
conda install requests
</pre>

Once you’ve installed the library, you can now invoke APIs.

First you'll have to import the `requests` libary. 

# Requests

After that there are many different types of requests. The most commonly used one, a **GET** request, is used to retrieve data, and that is what we'll implement.  When we make a request, the response from the API comes with a **response code** which tells us whether our request was successful. Response codes are important because they immediately tell us if something went wrong.

To make a ‘GET’ request, we’ll use the `requests.get()` function, which requires one argument — the URL we want to make the request to. We'll start by calling and formatting the data for all GBS Growth Platforms ([/api/v1/growth-platforms](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/eds/growth-platforms)).

## Status codes
Status codes are returned with every request that is made to a web server. Status codes indicate information about what happened with a request. Here are some codes that are relevant to GET requests:

- 200: Everything went okay, and the result has been returned (if any).
- 301: The server is redirecting you to a different endpoint. This can happen when a company switches domain names, or an endpoint name is changed.
- 400: The server thinks you made a bad request. This can happen when you don’t send along the right data, among other things.
- 401: The server thinks you’re not authenticated. Many APIs require login ccredentials, so this happens when you don’t send the right credentials to access an API.
- 403: The resource you’re trying to access is forbidden: you don’t have the right permissions to see it.
- 404: The resource you tried to access wasn’t found on the server.
- 503: The server is not ready to handle the request.
You might notice that all of the status codes that begin with a ‘4’ indicate some sort of error. The first number of status codes indicate their categorization. This is useful — you can know that if your status code starts with a ‘2’ it was successful and if it starts with a ‘4’ or ‘5’ there was an error. If you’re interested you can read more about status codes here.

## Working with JSON Data in Python
**JSON (JavaScript Object Notation)** is the language of APIs. JSON is a way to encode data structures that ensures that they are easily readable by machines. JSON is the primary format in which data is passed back and forth to APIs, and most API servers will send their responses in JSON format.

Python has great JSON support with the [json package](https://docs.python.org/3/library/json.html). The json package is part of the standard library, so we don’t have to install anything to use it. We can both convert lists and dictionaries to JSON, and convert strings to lists and dictionaries. In the case of our ISS Pass data, it is a dictionary encoded to a string in JSON format.

The json library has two main functions:

1. `json.dumps()` — Takes in a Python object, and converts (dumps) it to a string.
1. `json.loads()` — Takes a JSON string, and converts (loads) it to a Python object.

We'll use the `dumps()` function so we can print a formatted string which makes it easier to understand the JSON output.

# Sample code

That is pretty much it. The code snippet below shows invoking the Growth Platforms API ([/api/v1/growth-platforms](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/eds/growth-platforms)), checking the status, printing the returned JSON, and then looping through the returned list and printing the growth platform code and name.

<pre name="code" class="python">
import requests
import json
response  = requests.get("https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/eds/growth-platforms")
print('Status code:',response.status_code)

if (response.status_code == 200):
    formattedJson = json.dumps(response.json(), indent = 3)
    print('Formatted JSON:')
    print(formattedJson)
    
    # Loop through returned list and return GP code and name
    growthPlatformList = response.json()
    for growthPlatform in growthPlatformList:
        print(growthPlatform['growthPlatformCd'],growthPlatform['growthPlatformNm'])
else:
    print('API call failed, status code:',response.status_code)

</pre>

**Sample output**
<pre name="code" class="js">
Status code: 200
Formatted JSON:
[
   {
      "code": "15BS    ",
      "fullKey": null,
      "description": "Business Support",
      "level": 1,
      "children": null,
      "growthPlatformId": 3,
      "growthPlatformCd": "15BS    ",
      "growthPlatformNm": "Business Support",
      "growthPlatformDesc": "Business Support",
      "brandCd": "10J00   ",
      "effTms": null,
      "expirTms": null,
      "rowStatusCd": null
   },
   {
      "code": "15CAI   ",
      "fullKey": null,
      "description": "Hybrid Cloud Services",
      "level": 1,
      "children": null,
      "growthPlatformId": 1,
      "growthPlatformCd": "15CAI   ",
      "growthPlatformNm": "Hybrid Cloud Services",
      "growthPlatformDesc": "Hybrid Cloud Services",
      "brandCd": "10J00   ",
      "effTms": null,
      "expirTms": null,
      "rowStatusCd": null
   },
...
 
   {
      "code": "15TSL   ",
      "fullKey": null,
      "description": "GBS Top Service Line (AUO)",
      "level": 1,
      "children": null,
      "growthPlatformId": 4,
      "growthPlatformCd": "15TSL   ",
      "growthPlatformNm": "GBS Top Service Line (AUO)",
      "growthPlatformDesc": "GBS Top Service Line (AUO)",
      "brandCd": "10J00   ",
      "effTms": null,
      "expirTms": null,
      "rowStatusCd": null
   }
]
15BS     Business Support
15CAI    Hybrid Cloud Services
15CPT    Business Transformation Services
15JIA    Promontory
15TSL    GBS Top Service Line (AUO)
</pre>

# Using an API with Query Parameters

Within the framework, a number of API endpoints have required or optional parameters. An example of this is the [https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/eds/practices](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/eds/practices). This endpoint allows us to request that the entire taxonomy included in the results and allows us to apply additional filters.

We can do this by adding an optional keyword argument, `params`, to our PI request. We can make a dictionary with these parameters, and then pass them into the `requests.get()` function. Here’s what our dictionary would look like, specifying to include parent nodes (the taxonomy) and only for the 'Hybrid Cloud Management' (17ADM) Service Line.

<pre name="code" class="python">
parameters = {
    "includeParentage": True,
    "filters": "T1.SERVICE_LINE_CD='17ADM'"
}

response  = requests.get("http://localhost:8080/api/v1/eds/practices", parameters)
print('Status code:',response.status_code)

if (response.status_code == 200):
    formattedJson = json.dumps(response.json(), indent = 3)
    print('Formatted JSON:')
    print(formattedJson)
else:
    print('API call failed, status code:',response.status_code)
</pre>

**Sample output**
<pre name="code" class="js">
Status code: 200
Formatted JSON:
[
   {
      "code": "10J00   ",
      "fullKey": null,
      "description": "Global Business Services",
      "level": 0,
      "children": [
         {
            "code": "15CAI   ",
            "fullKey": null,
            "description": "Hybrid Cloud Services",
            "level": 1,
            "children": [
               {
                  "code": "17ADM   ",
                  "fullKey": null,
                  "description": "Hybrid Cloud Management",
                  "level": 2,
                  "children": [
                     {
                        "code": "GBS025  ",
                        "fullKey": null,
                        "description": "Dev Sec Ops",
                        "level": 3,
                        "children": null,
                        "practiceId": 17,
                        "practiceCd": "GBS025  ",
                        "practiceNm": "Dev Sec Ops",
                        "practiceDesc": "Dev Sec Ops",
                        "serviceLineCd": "17ADM   ",
                        "dccCd": null,
                        "accelPracticeInd": null,
                        "effTms": null,
                        "expirTms": null,
                        "rowStatusCd": null
                     },
...
                     {
                        "code": "GBS032  ",
                        "fullKey": null,
                        "description": "ServiceNow",
                        "level": 3,
                        "children": null,
                        "practiceId": 16,
                        "practiceCd": "GBS032  ",
                        "practiceNm": "ServiceNow",
                        "practiceDesc": "ServiceNow",
                        "serviceLineCd": "17ADM   ",
                        "dccCd": null,
                        "accelPracticeInd": null,
                        "effTms": null,
                        "expirTms": null,
                        "rowStatusCd": null
                     }
                  ],
                  "serviceLineId": 3,
                  "serviceLineCd": "17ADM   ",
                  "serviceLineNm": "Hybrid Cloud Management",
                  "serviceLineDesc": "Hybrid Cloud Management",
                  "growthPlatformCd": "15CAI   ",
                  "effTms": null,
                  "expirTms": null,
                  "rowStatusCd": null
               }
            ],
            "growthPlatformId": 1,
            "growthPlatformCd": "15CAI   ",
            "growthPlatformNm": "Hybrid Cloud Services",
            "growthPlatformDesc": "Hybrid Cloud Services",
            "brandCd": "10J00   ",
            "effTms": null,
            "expirTms": null,
            "rowStatusCd": null
         }
      ],
      "brandId": 1,
      "brandCd": "10J00   ",
      "brandNm": "Global Business Services",
      "brandDesc": "Global Business Services",
      "effTms": null,
      "expirTms": null,
      "rowStatusCd": null
   }
]
</pre>
