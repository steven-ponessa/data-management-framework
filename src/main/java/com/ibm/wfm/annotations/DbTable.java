package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {code @Retention(RetentionPolicy.RUNTIME)} means that the annotation can be accessed via reflection at runtime. If you do not set 
 * this directive, the defalut, {code @Retention(RetentionPolicy.CLASS)}, will be used, which is only avaliable at compile time and
 * the annotation will not be preserved at runtime, and thus not available via reflection.
 * @author Steve Ponessa
 *
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {
	public String baseTableName() default "";
	public String beanName() default "";
	public String parentBeanPackageName() default "com.ibm.wfm.beans";
	public String parentBeanName() default "";
	public String parentBaseTableName() default "";
	public String tableName() default "";
	public boolean isDimension() default true;
	public boolean useTable() default false;
}