---
title: Exception Handling
permalink: en/learning-framework/exception-handling
abstract: >- # this means to ignore newlines until "baseurl:"
  Handling errors correctly in APIs while providing meaningful error messages is an important feature, as it can help the API client properly respond to issues. The default behavior tends to be returning stack traces that are hard to understand and ultimately useless for the API client. Partitioning the error information into fields enables the API client to parse it and provide better error messages to the user. In this article, we will cover how the framework handles exceptions.
---

`RestController` is the base annotation for classes that handle REST operations.

`ExceptionHandler` is a Spring annotation that provides a mechanism to treat exceptions that are thrown during execution of handlers (Controller operations). This annotation, if used on methods of controller classes, will serve as the entry point for handling exceptions thrown within this controller only. Altogether, the most common way is to use `@ExceptionHandler` on methods of `@ControllerAdvice` classes so that the exception handling will be applied globally or to a subset of controllers.

`ControllerAdvice` is an annotation introduced in Spring 3.2, and is “Advice” for multiple controllers. It is used to enable a single `ExceptionHandler` to be applied to multiple controllers. This way we can, in just one place, define how to treat such an exception and this handler will be called when the exception is thrown from classes that are covered by the `ControllerAdvice`. The subset of controllers affected can defined by using the following selectors on `@ControllerAdvice`: `annotations()`, `basePackageClasses()`, and `basePackages()`. If no selectors are provided, then the ControllerAdvice is applied globally to all controllers.

So by using `@ExceptionHandler` and `@ControllerAdvice`, the framework defines a central point for treating exceptions and wrapping them up in an `ApiError` object with better organization than the default Spring Boot error handling mechanism.

# Handling Exceptions

![Exception Handling]({{ site.baseurl }}/assets/images/docs/exception-handling.png)

_Articulate the eexception handling methods used._