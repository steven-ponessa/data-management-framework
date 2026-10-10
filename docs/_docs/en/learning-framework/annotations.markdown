---
title: Annotations
permalink: en/learning-framework/annotations
features:
  - mermaid
  - zoom
abstract: >- # this means to ignore newlines until "baseurl:"
  Java annotations are a form of metadata you can attach to classes, methods, fields, parameters, etc. They don’t directly change program logic, but they provide information to the compiler, runtime, or frameworks about how the code should behave.
---

# Overview

We should move from a single-parent metadata model to a named-relationship model. A bean such as Market does not have one universal parent; it participates in multiple valid structural paths, including a geography hierarchy path and a delivery-organization association path. By explicitly defining those paths in metadata and requiring the SQL builder to choose a named path, we make join generation deterministic, extensible, and aligned with the true business model.

# Table relationships

In the initial deployment of the framework, relations where in the form of a “parent” as a single scalar property of `@DbTable`. This annotation design assumes a table has exactly one structural parent, which works for a pure tree but breaks as soon as one bean can participate in different valid join paths.  To support multiple parents (join paths) new annotations were introduced to model relationships as a set of **named relationship paths**.

In your case, `Market` is not ambiguous in the data model; it is ambiguous only in how SQL should be built. That means the annotation metadata should explicitly describe **multiple relationships**, and `DataMagerTye4.java` should choose among them by a **relationship name / path context**, not by guessing from a single `parentBeanName`.

## DbRelationship design

The new design keeps `@DbTable` focused on the bean/table identity, and move parent/association path definitions into a separate repeatable relationship annotation.

### 1. Simplify `@DbTable`

```java
@DbTable(
    beanName = "MarketDim",
    baseTableName = "ERDM.MARKET"
)
```

### 2. Add repeatable relationship metadata

For example:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DbRelationships.class)
public @interface DbRelationship {
    String name();                   // unique logical path name
    String relationshipType();       // PARENT, ASSOCIATION, HIERARCHY, etc.
    String targetBeanName();         // parent/related bean
    String targetTableName();
    String joinType() default "INNER";
    String[] joinColumns();          // e.g. "MARKET.GEOGRAPHY_ID = GEOGRAPHY.GEOGRAPHY_ID"
    boolean defaultPath() default false;
}
```

Container:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbRelationships {
    DbRelationship[] value();
}
```

Then `MarketDim` could look like:

```java
@DbTable(
    beanName = "MarketDim",
    baseTableName = "ERDM.MARKET"
)
@DbRelationship(
    name = "geography",
    relationshipType = "PARENT",
    targetBeanName = "GeographyDim",
    targetTableName = "ERDM.GEOGRAPHY",
    joinType = "LEFT",
    joinColumns = {
        "ERDM.MARKET.GEOGRAPHY_ID = ERDM.GEOGRAPHY.GEOGRAPHY_ID"
    },
    defaultPath = true
)
@DbRelationship(
    name = "deliveryOrganizationAssoc",
    relationshipType = "ASSOCIATION",
    targetBeanName = "DeliveryOrganizationMarketAssoc",
    targetTableName = "ERDM.DELIVERY_ORGANIZATION_MARKET_ASSOC",
    joinType = "LEFT",
    joinColumns = {
        "ERDM.MARKET.MARKET_ID = ERDM.DELIVERY_ORGANIZATION_MARKET_ASSOC.MARKET_ID"
    }
)
public class MarketDim {
}
```

## Why this is the right direction

This separates two different ideas that are currently conflated:

* **what table/bean this class represents**
* **what valid navigation paths exist from this bean**

That is important because `Market -> Geography` is a classic parent-child hierarchy path, while `Market -> DeliveryOrganizationMarketAssoc` is not really a parent in the same semantic sense. It is an association path. If you force both into `parentBeanName`, the metadata becomes misleading and `DataMagerTye4.java` has to infer intent from incomplete information.

## How `DataMagerTye4.java` should choose the path

It should not try to infer automatically when more than one path exists. Instead, make path choice explicit in the request context.

A good pattern is:

* if only one relationship path exists, use it
* if multiple exist and one is marked `defaultPath=true`, use the default for generic queries
* if the caller asks for fields/filters that require a specific path, resolve that path explicitly
* if multiple paths are possible and none is specified, fail with a clear error

For example, your query-building API could accept something like:

```java
QueryContext ctx = new QueryContext();
ctx.setRelationshipPath("geography");
```

or

```java
ctx.addJoinPath("deliveryOrganizationAssoc");
```

Then `DataMagerTye4.java` reads the bean’s `@DbRelationship`s, finds the named one, and builds SQL accordingly.

## Best practical recommendation

Use **named paths** and make them first-class.

For example:

* `geography`
* `deliveryOrganizationAssoc`

Then SQL generation becomes deterministic:

* for standard hierarchy traversal from Market upward, use `geography`
* for delivery-organization slicing, use `deliveryOrganizationAssoc`, possibly followed by another relationship from the assoc bean to `DeliveryOrganizationDim`

That also scales well when you add more perspectives later, such as sales, service line, or business unit.

## Even better long-term model

If your framework often navigates more than one hop, consider representing relationships as a graph of bean metadata, not just parent pointers. Then `DataMagerTye4.java` can resolve join chains such as:

* `MarketDim -> GeographyDim`
* `MarketDim -> DeliveryOrganizationMarketAssoc -> DeliveryOrganizationDim`

This is much more future-proof than bolting more parent fields onto `@DbTable`.

## Concrete recommendation summary

I would change the framework in these ways:

1. Remove single-parent assumptions from `@DbTable`
2. Introduce repeatable `@DbRelationship` annotations
3. Give every relationship a unique logical `name`
4. Distinguish relationship semantics such as `PARENT` vs `ASSOCIATION`
5. Require `DataMagerTye4.java` to use an explicit path name whenever more than one valid path exists
6. Allow one optional `defaultPath` for backward compatibility


If you want, I can sketch the actual Java annotation definitions plus a pseudocode update to `DataMagerTye4.java` showing how path resolution would work.

