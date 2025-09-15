---
title: Overview
permalink: en/learning-framework/fw-overview
abstract: >- # this means to ignore newlines until "baseurl:"
  The Workforce Managment Data Management Framework (WFM-DMF) fundamentally transforms how organizations handle and interact with data by allowing direct access to business and reference information at its source, bypassing the need for traditional ETL (Extract, Transform, Load) pipelines. 
---

In conventional data management, ETL  (Extract, Transform, Load) processes involve significant development effort to extract data from various systems, transform it into a unified format, and load it into a centralized database or repository. This can lead to complex workflows, increased costs, and maintenance burdens. The framework's approach eliminates the need for these intermediate steps, providing direct, real-time access to backend data sources through dynamic APIs. 

This real-time access to data ensures that users are always interacting with the most up-to-date information, eliminating data latency issues typically introduced by ETL schedules. By avoiding redundant data transfers and storage in application-specific repositories, the framework reduces the storage and synchronization challenges that often accompany centralized data systems. Instead, data can be queried in its native form, streamlining operations and leading to faster decision-making, improved analytics, and better business outcomes.

The direct interaction with backend systems also minimizes data errors and inconsistencies, as there's no intermediate transformation layer that could introduce inaccuracies. This approach not only enhances data quality but also reduces user frustration, fostering greater trust in the data being utilized for business decisions. Additionally, by avoiding the need for a centralized repository, the framework minimizes storage costs and eliminates the need for managing multiple data copies, contributing to a leaner, more cost-efficient data infrastructure.

Moreover, the ability to access data on-demand without pre-aggregating or moving it into centralized stores enables businesses to remain agile. Changes in data needs, which might require significant reconfiguration in a traditional ETL setup, can be handled dynamically by adjusting the framework's API queries. This flexibility ensures that the organization can swiftly adapt to evolving requirements without the overhead of complex data transformations or lengthy deployment cycles.

The WFM Data Management Framework makes it easy to set up RESTful APIs to a relational database without requiring the API to match the structure of the database. The framework provides data repository management abstractions to significantly reduce the amount of boilerplate code required to implement a data access and management layer for various persistence stores. The framework allows developers to focus on the business logic instead of technical complexity and boilerplate code. It handles most of the complexity of JDBC-based database access and object-relational mappings making the implementation of your persistence layer easier and faster. Data can be defined at the entity (table) level and include relationships that the entity has with other entities. With these definitions, and the definition of access endpoints (URLs), full CRUD (Create, Read, Update, and Delete) operations to the entity is enabled, including retrieving the full lineage (parentage) of the entity (i.e., full taxonomy).

In many APIs, however, the REST-endpoint doesn’t correspond to a particular table but rather an aggregate of tables and columns. In those cases, the framework allows users to configure dynamic SQL with 0 to many substitution variables.

In all cases, the framework enables the results to be returned as JSON, XML, or CSV format and, for hierarchies, return flat or hierarchical structures. The framework enables RESTful API access to multiple backend relational databases that support both basic and complex database schemas (e.g., hierarchies, slow changing dimensions, extended relational model). Along with providing RESTful CRUD operations to data, it also supports full replace and delta change, data quality, and other data management services. All this functionality is attainable while requiring little to no coding. With the processing logic imbedded in abstract classes within the framework, users of the framework simply subclass the abstract framework classes and inherit the required functionality.

## Providing Unified Access to Data

The data managment framework's unified access point enables seamless data interaction between different systems without needing complex ETL (Extract, Transform, Load) pipelines.  By accessing data directly where it resides—without the need to aggregate it into a centralized, application-specific repository—offers several compelling benefits:

### **1. Elimination of ETL Complexity**
   - Traditional ETL (Extract, Transform, Load) processes require significant development effort to extract data from various source systems, transform it to fit a specific schema or format, and load it into a centralized database or data warehouse. This involves meticulous mapping, data validation, and regular maintenance to accommodate schema changes or evolving business needs.
   - Your framework bypasses this complexity by enabling direct access to backend data sources. APIs can query data in real-time from its native location, removing the need for multiple extraction and transformation steps, reducing the setup time, and lowering development and maintenance costs.

### **2. Improved Data Freshness and Timeliness**
   - ETL pipelines often introduce data latency since they rely on scheduled jobs or periodic updates to move and refresh data. This lag can result in data that's outdated or out of sync, leading to inaccurate decision-making.
   - With direct access to data sources, your framework allows for real-time data retrieval. This ensures that users always work with the most current information available, enhancing the reliability of analytics and reporting efforts.

### **3. Reduced Data Duplication and Storage Costs**
   - Centralized data repositories create redundant data copies, increasing storage requirements and associated costs. Managing multiple data copies also introduces synchronization challenges, as updates in source systems must be reflected accurately in the replicated data.
   - By querying data directly from its origin, your solution eliminates the need for multiple data copies, leading to lower storage expenses and fewer synchronization headaches.

### **4. Faster Access to Integrated Data**
   - In complex organizations, data is often dispersed across multiple systems and platforms. An ETL-based approach typically consolidates this data in a single location, which can lead to long wait times for the integration and availability of new data.
   - Your framework allows for seamless integration across disparate systems via dynamic APIs, providing immediate access to an integrated view of data without waiting for it to be pulled together into a central repository. This agility enhances the responsiveness of applications and the ability to make data-driven decisions quickly.

### **5. Reduction in Errors and Frustrations**
   - ETL processes are prone to errors, particularly when handling complex transformations or dealing with inconsistent data formats across sources. Data inconsistencies, incorrect mappings, or timing issues during ETL can lead to inaccuracies that frustrate users and hinder trust in the data.
   - Directly querying and accessing data in its native form reduces these risks, as there's no intermediate processing that might introduce errors. Users can be confident that they are seeing the actual state of data in the source systems.

### **6. Flexibility for Multiple Applications**
   - In environments where multiple applications rely on the same data sources, an ETL approach may require each application to have its own ETL pipeline or access to a shared centralized repository, complicating the architecture.
   - Your framework's API-driven approach allows multiple applications to directly access data without requiring separate ETL processes or data warehouses, providing a single source of truth without architectural complexity. This improves consistency across applications and ensures that all systems are working with identical data.

### **7. Greater Agility in Handling Business Changes**
   - Changes in data requirements often necessitate modifications to ETL pipelines, including altering extraction queries, transformation logic, and data load schedules. This can be time-consuming and requires dedicated resources.
   - Your framework’s capability to adjust queries and attributes dynamically enables quick adaptation to changing business requirements without modifying a centralized ETL workflow or data repository. This flexibility allows businesses to respond faster to new demands.

By enabling applications and users to interact directly with live data in its original repositories, your framework not only simplifies data architecture but also enhances agility, reduces costs, and improves data accuracy. This is particularly valuable in environments with fast-paced data needs, where real-time access is crucial to maintaining a competitive edge.

## Functional Overview

Below is a functional overview highlighting how the data managment framework benefits IBM Consulting and IT teams:

### 1. **Streamlined API Management**
   - **No-Code API Creation**: The data managment framework eliminates the need for custom coding to create APIs. Business users or developers can instantly generate APIs for CRUD (Create, Read, Update, Delete) and data loading operations without any programming, reducing development time and costs.
   - **Dynamic Data Access**: Users can dynamically define data sources, set SQL templates, and specify variables, making it easy to generate custom queries without altering the backend system. This adaptability allows businesses to quickly respond to changing data requirements without technical intervention.
   - **Hierarchical Data Navigation**: The built-in support for managing hierarchical data—whether through collections of related tables or self-referencing entities—facilitates complex data structures, such as organizational hierarchies or product categorizations, ensuring that information is represented accurately and intuitively.

### 2. **Increased Efficiency for Data Handling**
   - **Dynamic SQL Templates**: By allowing SQL templates with placeholders for substitution variables, the framework supports the execution of complex queries, aggregations, and joins on-the-fly. This reduces the need for manually crafting complex SQL logic for each new report or data analysis, streamlining operations.
   - **Flexible Data Integration**: The data managment framework is capable of integrating diverse backend data sources into a single API layer. This unified access point enables seamless data interaction between different departments or systems without needing complex ETL (Extract, Transform, Load) pipelines.

### 3. **Enhanced Developer Productivity**
   - **Excel-Driven Code Generation**: The Maven plugin leverages Excel spreadsheets to generate code. By reading the table schema from Excel and using Apache Freemaker templates, it automates the creation of POJOs and basic CRUD APIs. This drastically speeds up the initial setup process, ensuring quick deployment and reducing errors.
   - **Automatic API Documentation**: Using [OpenAPI](https://www.openapis.org/){:target="_blank"}, the framework automatically documents all APIs, which facilitates communication between developers and stakeholders, improves API usability, and enhances integration efforts with third-party tools.
   - **Reduced Manual Coding**: Because the framework handles backend interactions, API creation, and database mapping, development teams can focus on higher-level tasks rather than boilerplate code, enhancing overall productivity.

### 4. **Scalability and Maintainability**
   - **Microservices Architecture**: The microservices design aligns with modern enterprise architectures, providing modularity, scalability, and ease of maintenance. Each service can be independently developed, deployed, and scaled, minimizing disruptions during upgrades or changes.
   - **Centralized and Consistent Data Access**: The framework acts as a standardized interface for data operations across various backend systems, ensuring consistent access, security, and governance. This mitigates the risk of errors and inconsistency when accessing data from multiple sources.

### 5. **Faster Time to Market**
   - **Rapid Deployment**: By leveraging dynamic APIs, businesses can deploy new features faster. When new data needs to be accessed or operations added, the framework supports immediate deployment without requiring a new development cycle or application re-deployment.
   - **Adaptability**: The system's capability to handle dynamic attributes and complex queries allows it to be adaptable to various business scenarios, from simple data retrieval to intricate reporting or analytics needs. This adaptability can be a competitive advantage, helping organizations address changing market demands efficiently.

### 6. **Cost Reduction and Lower IT Overhead**
   - **Reduced Development Costs**: The no-code and low-code capabilities significantly lower the development and maintenance costs associated with backend systems. With this framework, fewer resources are needed to build, maintain, and extend APIs, resulting in direct cost savings.
   - **Minimized Technical Debt**: By standardizing data access and reducing the need for custom code, the framework helps minimize technical debt. This leads to lower long-term costs and less complexity when introducing new team members or upgrading the technology stack.

### 7. **Improved Data Security and Compliance**
   - **Controlled API Access**: With a single, standardized platform for creating APIs, data access can be more tightly controlled. This enables better implementation of security protocols and compliance with data privacy regulations.
   - **Centralized API Governance**: Centralizing API creation and documentation through the framework ensures consistent policies are applied to data handling and API usage. This contributes to safer data operations and easier auditing.

### 8. **Facilitates Business Analytics and Intelligence**
   - **Data Aggregation and Consolidation**: By supporting aggregation, consolidation, and cross-table joins, your framework is particularly well-suited for businesses needing advanced analytics. This makes it easier to generate reports, perform business analysis, and derive insights without manually writing complex SQL.
   - **API-Driven Data Reporting**: Businesses can use the API layer to expose data to reporting tools or dashboards, enabling real-time access to critical information and driving data-driven decision-making.

### 9. **Future-Proof Infrastructure**
   - **Technology Stack Alignment**: Leveraging Java SpringBoot and OpenAPI standards aligns with widely adopted industry technologies. This choice ensures that the framework can easily integrate with other enterprise solutions and take advantage of the ongoing evolution of these technologies.
   - **Built for Evolution**: With its microservices architecture and focus on dynamic configuration, the framework is prepared to evolve as new business requirements arise. This ensures the investment is future-proof and can accommodate growing business needs.

By combining the flexibility of dynamic API creation with the robustness of a standardized framework, your solution positions itself as a strategic asset for companies aiming to streamline backend operations, improve data accessibility, and empower business users—all while controlling costs and enhancing agility.

## Hierarchical Navigation in RESTful APIs

Hierarchical navigation in RESTful APIs using nested path variables to represent a tree-like structure of resources. This is a common design pattern for representing hierarchies in RESTful APIs because it mimics the natural hierarchy in data relationships and provides an intuitive, predictable way for clients to navigate through related entities.


### Concept Breakdown
In a RESTful API, the **path** (or URL) typically represents the resource being requested. When your data is hierarchical (like in your NFL example), you can use nested path variables to represent this hierarchy, allowing users to "drill down" into more specific subsets of data.

Here's a step-by-step explanation of this concept:

1. **Top-Level Resource**:
   - **Request**: `GET /api/nfl/conferences`
   - **Description**: This is a top-level resource request, which asks for all the conferences in the NFL.
   - **Response**:
     ```json
     [
       {"afc": "American Football Conference"},
       {"nfc": "National Football Conference"}
     ]
     ```

2. **Navigating to a Child Resource**:
   - **Request**: `GET /api/nfl/conferences/afc`
   - **Description**: By specifying the conference key (`afc`), the API request now targets a specific child resource under `conferences`. This could return detailed information about the "American Football Conference".
   - **Response**:
     ```json
     {
       "key": "afc",
       "name": "American Football Conference",
       "foundedYear": 1970
     }
     ```

3. **Navigating to a Grandchild Resource**:
   - **Request**: `GET /api/nfl/conferences/afc/divisions`
   - **Description**: Adding `divisions` as a path segment drills down further to list all the divisions within the "American Football Conference".
   - **Response**:
     ```json
     [
       {"east": "East"},
       {"central": "Central"},
       {"west": "West"}
     ]
     ```

4. **Deep Navigation with More Path Variables**:
   - **Request**: `GET /api/nfl/conferences/afc/divisions/east`
   - **Description**: Specifying both the conference (`afc`) and the division (`east`) allows the API to return detailed information about the "East" division within the AFC.
   - **Response**:
     ```json
     {
       "key": "east",
       "name": "AFC East",
       "teamsCount": 4
     }
     ```

5. **Retrieving a List of Leaf Resources**:
   - **Request**: `GET /api/nfl/conferences/afc/divisions/east/teams`
   - **Description**: By further specifying `teams`, the request drills down to list all the teams in the "East" division of the "AFC".
   - **Response**:
     ```json
     [
       {"ne": "New England Patriots"},
       {"nyj": "New York Jets"},
       {"mia": "Miami Dolphins"},
       {"buf": "Buffalo Bills"}
     ]
     ```

6. **Navigating to a Specific Leaf Resource**:
   - **Request**: `GET /api/nfl/conferences/afc/divisions/east/mia`
   - **Description**: Here, the request targets a specific team (`mia` for the "Miami Dolphins") within the "East" division of the "AFC".
   - **Response**:
     ```json
     {
       "key": "mia",
       "name": "Miami Dolphins",
       "foundedYear": 1966,
       "stadium": "Hard Rock Stadium"
     }
     ```

### Benefits of Hierarchical Path Navigation
1. **Clarity**: The URL clearly indicates the structure and relationship between resources. For example, `/api/nfl/conferences/afc/divisions/east/teams` indicates a request for teams in the "East" division of the "AFC".
2. **Predictability**: Users can intuitively guess the endpoints based on the hierarchy, making the API easier to use without extensive documentation.
3. **Consistency**: This structure enforces a consistent pattern across the API, simplifying client-side development and reducing errors.
4. **Scalability**: As more data or levels are added (e.g., players within teams), the structure can easily accommodate new paths without breaking existing ones.

### Limitations to Consider
1. **Deep Hierarchies**: If the hierarchy becomes too deep (e.g., many levels beyond divisions and teams), URLs can get long and hard to manage. Consider balancing depth and usability.
2. **Over-Specificity**: Each path specifies the full hierarchy, which can be redundant if the client already has the context of the parent resources (like knowing the division when requesting a specific team). Using query parameters or other strategies might sometimes be preferred.
3. **Error Handling**: Clients need clear error messages when a specific part of the path does not exist (e.g., a nonexistent conference key).

### Summary
Using RESTful paths to navigate a hierarchy like the NFL structure offers a simple and human-readable way to access nested data resources. The pattern you described is a great fit when:
- The data naturally forms a tree structure.
- The depth of the hierarchy is reasonable.
- The order of resources matters (as the path segments represent the "parent-child" relationship).

In this example, each step down the hierarchy filters the data, narrowing the scope of the request, making it efficient and logically organized for end users.

## Evolution of Data as a Service (DaaS)
Looking at the data access and managment domain from a historical perspective, it used to be that data existed on a box and when one system needed data from another, the data was physically copied or accessed through some form of distributed processing or federation. However, the replication of application functionality and data using point to point connections was not a sustainable paradigm. As the number of enterprise applications proliferated, there needed to be more direct communications between applications, which introduced another layer of interface, which was eventually labeled **Enterprise Application Integration**, or **EAI**. The idea was that if these systems could be integrated, existing functionality could be reused instead of replicated across multiple systems. The reduced functional redundancy meant that functionality only needed to be maintained and enhanced within a single system saving time and money in development, testing, and deployments.

Application integration began with the **application programming interface (API)** to expose system functionality to external processes. Where this worked okay it still required application to be written in the same language and running on the same platform. This led to language and platform specific adaptors, implementing the mediator pattern, to integrate systems. Unfortunately, these adaptors were difficult to maintain and therefore not widely adopted.

The next phase of the integration evolution attempted to expose system function and data through platform independent means. Over time, the EAI industry started widely promoting the use of a **Services Oriented Architecture (SOA)** using the **SOAP (Simple Object Access Protocol)** specification, which used existing EAI methodology of integrating applications through the use of messaging. Web Services, which facilitated a SOA, exposed the system’s functions in a language and platform independent manner.

For years SOAP was the predominant communication protocol within SOAs however **REST (Representational State Transfer)** quickly took over the number one spot and, in 2021, represents well over 80% of all public APIs. SOAP exposes components of application logic as services rather than data. REST is NOT a standard, rather it is an architectural style for APIs that is lighter and faster than SOAP and relies on the HTTP protocol while providing a consistent interface to access named resources.

This framework, which is centered around data management, provides the architectural guidance and low coding overhead of a framework, while delivering the benefits of a SOA and accessing data “where it lives”, and the intuitive, easy to use nature of REST and RESTful APIs.

An API, in essence, allows two systems to communicate with one another. An API quintessentially provides the language and contract for how two systems interact. Each API has documentation and specifications which determine how information can be transferred.

Just like a webpage is rendered, APIs can use HTTP requests to get information from a web application or web server.  

API are accessed through endpoint which is one end of a communication channel. Each endpoint is the location from which APIs can access the resources they need to carry out their function.

APIs work using ‘requests’ and ‘responses.’ When an API requests information from a web application or web server, it will receive a response. The place that APIs send requests and where the resource lives, is called an endpoint.