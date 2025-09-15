package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SqlParameterDim",baseTableName="DMF.SQL_PARAMETER",parentBeanName="SqlTemplateDim",parentBaseTableName="DMF.SQL_TEMPLATE")
public class SqlParameterDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="SQL_TEMPLATE_NM",keySeq=1, foreignKeySeq=1)
	private String     sqlTemplateNm;
	@DbColumn(columnName="NAME",keySeq=2)
	private String     name;
	@DbColumn(columnName="DATA_TYPE")
	private int        dataType;
	@DbColumn(columnName="PARM_LTH")
	private int        length;
	@DbColumn(columnName="REQD_IND")
	private Boolean    reqdInd;
	@DbColumn(columnName="IMPLY_OPERATOR_IND")
	private Boolean    implyOperatorInd;
	@DbColumn(columnName="DEFAULT_VALUE")
	private String     defaultValue;
	@DbColumn(columnName="DESCRIPTION")
	private String     description;

	// Define null constructor
	public SqlParameterDim () {
		this.level = 2;
	}
	
	// Define natural key constructor
	public SqlParameterDim (
      String     sqlTemplateNm
    , String     name
	) {
		this.sqlTemplateNm                  = sqlTemplateNm;
		this.name                           = name;
		this.level = 2;
	}
	
    
	// Define full constructor
	public SqlParameterDim (
		  int        id
		, String     sqlTemplateNm
		, String     name
		, int        dataType
		, int        length
		, Boolean    reqdInd
		, Boolean	 implyOperatorInd
		, String     defaultValue
		, String     description
	) {
		this.id                             = id;
		this.sqlTemplateNm                  = sqlTemplateNm;
		this.name                           = name;
		this.dataType                       = dataType;
		this.length                         = length;
		this.reqdInd                        = reqdInd;
		this.implyOperatorInd				= implyOperatorInd;
		this.defaultValue                   = defaultValue;
		this.description                    = description;
		this.level = 2;
	}
	
	@Override
	public String getCode() { return this.sqlTemplateNm+":"+this.name; }
	//public String getDescription() { return this.ctryNm; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SqlParameterDim other = (SqlParameterDim) obj;
		if (
            this.sqlTemplateNm.equals(other.getSqlTemplateNm())
         && this.name.equals(other.getName())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.sqlTemplateNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.name))
        + "," + Helpers.formatCsvField(String.valueOf(this.dataType))
        + "," + Helpers.formatCsvField(String.valueOf(this.length))
        + "," + Helpers.formatCsvField(String.valueOf(this.reqdInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.defaultValue))
        + "," + Helpers.formatCsvField(String.valueOf(this.description))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SQL_TEMPLATE_NM")
        + "," + Helpers.formatCsvField("NAME")
        + "," + Helpers.formatCsvField("DATA_TYPE")
        + "," + Helpers.formatCsvField("LENGTH")
        + "," + Helpers.formatCsvField("REQD_IND")
        + "," + Helpers.formatCsvField("DEFAULT_VALUE")
        + "," + Helpers.formatCsvField("DESCRIPTION")
		;
	}
    
	// Define Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSqlTemplateNm() {
		return sqlTemplateNm;
	}
	public void setSqlTemplateNm(String sqlTemplateNm) {
		this.sqlTemplateNm = sqlTemplateNm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Boolean getReqdInd() {
		return reqdInd;
	}
	public void setReqdInd(Boolean reqdInd) {
		this.reqdInd = reqdInd;
	}
	public Boolean getImplyOperatorInd() {
		return implyOperatorInd;
	}
	public void setImplyOperatorInd(Boolean implyOperatorInd) {
		this.implyOperatorInd = implyOperatorInd;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}