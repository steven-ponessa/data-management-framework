---
title: Try-it Sample Applications
permalink: /en/exploring-services/try-it-sample-app
abstract: >- 
  To demonstrate using the services and APIs and to make some sample code available, several sample applications have been created. 
---

To access the sample applications, go to the [Try-it]({{ site.url }}/#try-it) menu item on the landing's page navigation menu. Here you'll see several sample applications. 

![Sourcing Strategy]({{ site.baseurl }}/assets/images/docs/try-it.png)



# Sourcing Strategy

This sample application was put together to test the sourcing strategy APIs. For all sample applications, the bottom of the panel contains an **API Execution List** that keeps a running list of all APIs that are executed within this sample application. All these APIs are hyperlinked and can be re-executed directly from the execution list.

![Sourcing Strategy]({{ site.baseurl }}/assets/images/docs/sourcing-strategy.png)

The Sourcing Strategy API returns the sourcing strategy based on the contract owning country and market codes, and the requested job role and specialty. 

On the initial load, the application calls an API to populate the dropdown of available countries using the countries API, sorting the list by the country name ([/api/v1/eds/countries?orderByCols=CTRY_NM]({{ site.url }}/api/v1/eds/countries?orderByCols=CTRY_NM){: target="_blank" }) and the job roles dropdown using the job roles API to populate the dropdown of available job roles ([/api/v1/eds/job-roles]({{ site.url }}/api/v1/eds/job-roles){: target="_blank" }).

When the user selects the contract owning country the countries API is called again, this time with the selected country code included in the URL as a path variable and passing the `includeParentage` parameter with the value of `true` (e.g., for Brazil (country code 631) selected, [/api/v1/eds/countries/631?includeParentage=true]({{ site.url }}/api/v1/eds/countries/631?includeParentage=true ){: target="_blank" }). When the API returns, the country's geography and market are displayed under the contract owning country dropdown box. Similarly, when the user selects the job role the specialties API is called with the job role included in the URL as a path variable (e.g., for &quot;Job Role&quot; of &quot;Application Architect&quot; (job role code 040661) selected, [/api/v1/eds/job-roles/040661/specialties]({{ site.url }}/api/v1/eds/job-roles/040661/specialties){: target="_blank" }). The API's results are used to populate the Specialties drowdown list box with the valid specialties for the selected job role.


Once the specialty has been selected, we have all the components needed to determine the sourcing strategy. The user can click the **Get sourcing strategy** button which invokes the sourcing strategy API with the market, country, and JR/S as URL path variables (e.g., [/api/v1/eds/sourcing-strategy-countries/8L/631/040661-S8974]({{ site.url }}/api/v1/eds/sourcing-strategy-countries/8L/631/040661-S8974){: target="_blank" }). This will either return a sourcing strategy or an error message that there is no sourcing strategy defined for the market, country, and JR/S combination.

![Sourcing Strategy Results]({{ site.baseurl }}/assets/images/docs/sourcing-strategy-results.png)

# Sourcing Strategy Validation

Once a sourcing strategy has been selected it has to be validated. At the time this article was written, there were 19 sourcing strategies, each with their own set of validation rules. The framework includes an **expression evaluator** which allows both the validation rules to be maintained outside of the Java code, thus allowing dynamic rule changes, and reporting back the specific rules that passed or failed. Note however that when a validation rule has multiple multi-step evaluations that are **OR**ed together, each expression of each evaluation is executed, until one of the OR conditions is satisfied. If none pass, the API returns errors from each evaluation.

![Sourcing Strategy validation]({{ site.baseurl }}/assets/images/docs/sourcing-strategy-validation.png){: class="img-responsive zoom" }

The top left section of the sample application contains a carousel control where the user can reference each of the 19 rules. 

Thereafter, the user can select which Sourcing Strategy they want to test. The **Parameters** section of the form dynamically changes based on the sourcing strategy selected to include the parameters that are used in the validation of the selected strategy. The sourcing strategy  validation API only requires that the sourcing strategy code is included in the URL as a path variable, all other parameters are optional, however different parameters are required based on the sourcing strategy being validated. 

**Parameters**

|   | **Parameter Name**                                  | **Parameter URL Name**         | 
|:-:|-----------------------------------------------------|--------------------------------|
| 1 | Role Seat Type Code                                 | role-seat-type-cd              | 
| 2 | Prefered Resource Channel                           | pref-resource-channel          | 
| 3 | GIC Provider Country Code                           | gic-provider-country           | 
| 4 | Contract Owning Country Code                        | contract-owning-country        | 
| 5 | Contract Owning Market Code                         | contract-owning-market         | 
| 6 | Request Contractor / Accept Global Contractor (GIC) | req-cntrct-accept-gbl-delivery | 
| 7 | Requested Band High                                 | band-high                      | 
| 8 | Requested Band Low                                  | band-low                       | 

When the user specifies the parameters they can click the **Submit** button to invoke the API (e.g., [/api/v1/eds/sourcing-strategy/SS02/validate?role-seat-type-cd=gic&gic-provider-country=846&contract-owning-country=806]({{ site.url }}/api/v1/eds/sourcing-strategy/SS02/validate?role-seat-type-cd=gic&gic-provider-country=846&contract-owning-country=806){: target="_blank" }).  Thereafter, the API URL will be captured in the **API Executed List**, the application will note the parameters required for the API and the API URL executed, and the returned results will be displayed.

![Sourcing Strategy validation results]({{ site.baseurl }}/assets/images/docs/sourcing-strategy-validation-results.png)

Finally, the Sourcing Strategy Validation service was created using the **Singleton** creation pattern. The singleton pattern ensures a class can only be instantiated once, and is a global point of access to that instance is provided.

We do this since the evaluation requires the retrieval of the full list of [countries]({{ site.url }}/api/v1//eds/countries){: target="_blank" } and Lists of [CIC Domestic]({{ site.url }}/api/v1//eds/countries/?CIC_DOM_DLVRY_CNTR_IND='Y'){: target="_blank" } and [Global Delivery Centers]({{ site.url }}/api/v1//eds/countries/?CIC_GBL_DLVRY_CNTR_IND='Y'){: target="_blank" }.  These lists are gathered using APIs; however we don't want to re-run these APIs for each validation. These lists are gathered the first time a validation is called (_lazy loading_) and cached for a configurable time interval (86400 second = 24 hours).  Therefore, the first validation call may take 2 or 3 seconds to return but subsequent calls will return near instantaneously.


# Sourcing Strategy API definition

The definition of the sourcing strategy APIs (both population and execution) are contained within the API Swagger/OpenAPI documentation at: [/swagger-ui.html#/sourcing-strategy-controller]({{ site.url }}/swagger-ui.html#/sourcing-strategy-controller){: target="_blank" }.