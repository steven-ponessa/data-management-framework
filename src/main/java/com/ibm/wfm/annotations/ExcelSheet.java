package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
	public String columnName();
	public boolean ignore() default false;
	public int columnNum() default -1;
}
