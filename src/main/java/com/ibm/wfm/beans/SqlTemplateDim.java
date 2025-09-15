package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SqlTemplateDim",baseTableName="DMF.SQL_TEMPLATE")
public class SqlTemplateDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="NAME",keySeq=1)
	private String     name;
	@DbColumn(columnName="DESCRIPTION")
	private String     description;
	@DbColumn(columnName="TEMPLATE")
	private String     template;

	// Define null constructor
	public SqlTemplateDim () {
		this.level=1;
	}
	
	// Define natural key constructor
	public SqlTemplateDim (
      String     name
	) {
		this.name                           = name;
		this.level = 1;
	}
	
    
	// Define full constructor
	public SqlTemplateDim (
		  int        id
		, String     name
		, String     description
		, String     template
	) {
		this.id                             = id;
		this.name                           = name;
		this.description                    = description;
		this.template                       = template;
		this.level = 1;
	}
	
	@Override
	public String getCode() { return this.name; }
	//public String getDescription() { return this.ctryNm; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SqlTemplateDim other = (SqlTemplateDim) obj;
		if (
            this.name.equals(other.getName())
         && this.description.equals(other.getDescription())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.name))
        + "," + Helpers.formatCsvField(String.valueOf(this.description))
        + "," + Helpers.formatCsvField(String.valueOf(this.template))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("NAME")
        + "," + Helpers.formatCsvField("DESCRIPTION")
        + "," + Helpers.formatCsvField("TEMPLATE")
		;
	}
    
	// Define Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}