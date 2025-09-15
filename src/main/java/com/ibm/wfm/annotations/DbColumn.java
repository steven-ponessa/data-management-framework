package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbColumn {
	public String columnName();
	public boolean isId() default false;
	public int keySeq() default -1;
	public boolean isScd() default false;
	public boolean isExtension() default false;
	public int foreignKeySeq() default -1;
	public int assocParentKey() default -1;
	public int assocChildKey() default -1;
}
