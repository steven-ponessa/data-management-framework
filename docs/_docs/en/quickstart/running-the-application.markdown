---
title: Running the Application
permalink: /en/quickstart/running-the-application
abstract: >- # this means to ignore newlines until "baseurl:"
  Running the application locally.
---

# Running the application locally     

To perform a **Maven** build and run, perform the following:
1.	Maven build.  
    1. From the “Package Explorer” panel, select the pom.xml file, right click, and select “Run as…” -> “Maven Build…” from the context menu.  
    1. Under “Goal” specify **`clean verify`**
    1. Click “Run”

    This will perform the build and populate the “**target**” folder, building the ***.jar** file based on `<name>` and `<version>` element values specified in the `pom.xml` file (`ms-spring-server-0.0.1-SNAPSHOT.jar`).

2.	Run the application
    1.	Go to the directory with the jar file and execute:

<pre name="code" class="php">
java -jar ms-spring-server-0.0.1-SNAPSHOT.jar
</pre>

You can test with **CURL**, browser (for GET methods), or **POSTMAN**, e.g.:

```
 curl --location --request GET 'http://localhost:8080/api/v1/limits/' 

```
Which returns `{"minimum":5,"maximum":995}`

or

```
curl --location --request POST 'http://localhost:8080/api/v1/taxonomyEvaluator/' \
--form 'tax=@"/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv"' \
--form 'data=@"/Users/steve/$WFM/wf360/data/rah_people_data.csv"' \
--form 'keyStr="0,2,16,18,20,15"'
```

Which returns: 

<pre name="code" class="js">
{
   "startTime":"2021-04-04T21:30:37.749+00:00",
   "taxUploadStartTime":"2021-04-04T21:30:37.749+00:00",
   "dataUploadStartTime":"2021-04-04T21:30:37.757+00:00",
   "evaluationStartTime":"2021-04-04T21:30:38.136+00:00",
   "statisticsStartTime":"2021-04-04T21:30:45.103+00:00",
   "completionTime":"2021-04-04T21:30:45.550+00:00",
   "taxonomyLevels":6,
   "totalRecords":128063,
   "validRecords":111379,
   "percentValidRecords":86.97203720044041,
   "invalidBrachRecords":11859,
   "percentInvalidBrachRecords":9.260285953007504,
   "invalidNodeRecords":4825,
   "percentInvalidNodeRecords":3.767676846552088,
   "tieOut":true,
   "outputDatasetUrl":"http://localhost:8080/api/v1/downloadFile/taxonomyEvaluationResults.csv"
}
</pre>

