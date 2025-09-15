---
title: Reference Architecture
permalink: en/learning-framework/reference-architecture
abstract: >- 
  The Reference Architecture depicts the target architecture we are looking at for delivering data services throughout IBM WFM.
  In a microservices application architectural style, an application is composed of many discrete, network-connected components, which are termed microservices. 
  The microservices architectural style is an evolution of the SOA (Services Oriented Architecture) architectural style. 
---

![Microservice Reference Architecture]({{ site.baseurl }}/assets/images/docs/microservice-reference-architecture.png)

# API Gateway

In a microservices architecture, each microservice exposes a set of (typically) fine-grained endpoints. For access to these end points, we can either implement a client-to-microservice pattern (which we have today) or Api Gateway pattern. In In the client-to-microservice pattern, a client app can make requests directly the microservices. Here each microservice has a public endpoint, sometimes with a different TCP port for each microservice.

In a multi node environment like OpenShift, the URL would map to the load balancer used in the cluster, which, in turn, distributes the requests across the nodes the microservice is running.
The direct client-to-microservice communication architecture is okay for a small microservice-based application, however, as the number of microservices grow, the approach faces some challenges. First it can make UI’s consuming the services unnecessarily complex and increases the number of round trips across the Internet. But the biggest thing is that you’d have to handle cross-cutting concerns such as authorization, entitlement, and logging in each microservice.

In a microservices architecture, the client apps usually need to consume functionality from more than one microservice. If that consumption is performed directly, the client needs to handle multiple calls to microservice endpoints. When new microservices are introduced and existing microservices are updated, this becomes cumbersome to manage.
The API Gateway pattern is a service that provides a single-entry point for certain groups of microservices. It's similar to the Facade pattern from object-oriented design, but in this case, it's part of a distributed system. The API Gateway pattern is also sometimes known as the "backend for frontend" (BFF) because you build it while thinking about the needs of the client app. It can also provide cross-cutting features such as authentication, entitlement, logging, and cache.

**Ribbon** can also be used to distribute load between multiple instances of microservices.

# Service Registry

Each microservice has a unique name (URL) that's used to resolve its location. In a true, scalable microservice architecture, the microservice needs to be addressable wherever it's running. In the same way that DNS resolves a URL to a particular computer, a microservice needs to have a unique name so that its current location is discoverable. By the microservice having an addressable name it makes it independent from the infrastructure it’s running on. This approach implies that there's an interaction between how the service is deployed and how it's discovered, and therefore there needs to be a service registry. In the same vein, when a computer fails, the registry service must be able to indicate where the service is now running.

The service registry pattern is a key part of service discovery. The registry is a database containing the network locations of service instances. A service registry needs to be highly available and up-to-date. So, a service registry consists of a cluster of servers that use a replication protocol to maintain consistency.

In Open Shift, service discovery is built in with Kubernetes running under the covers, therefore the plan is to have OpenShift handle service instance registration and deregistration. OpenShift also runs a proxy on each cluster host that plays the role of server-side discovery router. However, some of the documentation I have looked at is saying that you should have a Kubernetes service on top of the Eureka pods/deployments which then will provide you a referable IP address and port number.

However we may use the **Eureka** discovery service which is just a spring-boot application with the annotation `@EnableEurekaServer` (adding the appropriate dependency the application's POM file).  Thereafter we can create/update microservices with the annotation `@EnableDiscoveryClient` to the main classes and the defaultZone pointing to your eureka-service in the application.properties (or application.yml) of both, start the eureka-service (discovery service) and the microservices. Microseervices with the `@EnableDiscoveryClient` will then register with the discovery-service.

Idea is to move light blue instances to dark.

# Configuration Server

_add content on what the configuration server provides_

# Distributed Tracking

_add content on what distributed tracking provides_

# Microservices

Microservices architectures can seamlessly execute microservices deployed in different locations running on different technology stacks. Currently WMF has Node.js microservices running in dev, test and production. A test Python microservices running in dev. This one is running in Java.

# Swagger/OpenAPI

_add content on what and why we are using Swagger/OpenAPI_