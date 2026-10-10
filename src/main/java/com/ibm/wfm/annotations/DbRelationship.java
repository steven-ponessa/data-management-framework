package com.ibm.wfm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a relationship (join path) from one DbTable bean to another.
 * Supports multiple relationships per bean via @Repeatable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DbRelationships.class)
public @interface DbRelationship {

    /**
     * Unique logical name of the relationship.
     * Used by DataManagerType4 to select the join path.
     */
    String name();

    /**
     * Type of relationship.
     * Examples: PARENT, ASSOCIATION, HIERARCHY
     */
    RelationshipType relationshipType() default RelationshipType.PARENT;
    
    /**
     * Target bean package name (matches @DbTable.beanPackageName)
     */
    String targetBeanPackageName() default "";

    /**
     * Target bean name (matches @DbTable.beanName)
     */
    String targetBeanName();

    /**
     * Target table name (fully qualified Db2 table)
     */
    String targetTableName();

    /**
     * SQL join type (INNER, LEFT, RIGHT, etc.)
     */
    JoinType joinType() default JoinType.INNER;

    /**
     * Join condition(s).
     * Example:
     * "m.MARKET_ID = d.MARKET_ID"
     */
    String[] joinColumns();
    
    /**
     * Parent key to be used in join.  TO-DO: change to keys
     */
    String joinParentKey();
    
    /**
     * Child key to be used in join.  TO-DO: change to keys
     */
    String joinChildKey();

    /**
     * Indicates if this is the default relationship when multiple exist.
     */
    boolean defaultPath() default false;

    /**
     * Optional alias for the target table (useful for SQL generation).
     */
    String targetAlias() default "";

    /**
     * Optional description (useful for documentation/debugging).
     */
    String description() default "";
}