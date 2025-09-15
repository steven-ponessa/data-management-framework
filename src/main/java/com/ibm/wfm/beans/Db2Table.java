package com.ibm.wfm.beans;

import java.util.ArrayList;
import java.util.List;

import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.utils.Helpers;

public class Db2Table {
	
	public static final int ARTIFACT_NONE=0;
	public static final int ARTIFACT_DDL = 1;
	public static final int ARTIFACT_BEAN = 2;
	public static final int ARTIFACT_CONTROLLER = 4;
	public static final int ARTIFACT_DATA_SOURCE = 8;
	
	@ExcelSheet(columnName = "Schema", columnNum = 0)
	private String schema;
	@ExcelSheet(columnName = "Name", columnNum = 1)
	private String name;
	@ExcelSheet(columnName = "Tablespace Name", columnNum = 2)
	private String tablespaceNm;                  
	@ExcelSheet(columnName = "Remarks", columnNum = 10)
	private String remarks;
	@ExcelSheet(columnName = "Extension Name", columnNum = 3)
	private String extensionNm;
	@ExcelSheet(columnName = "Is SCD", columnNum = 4)
	private boolean scd;
	@ExcelSheet(columnName = "ETL Seq", columnNum = 5)
	private boolean etl;
	@ExcelSheet(columnName = "Parent Level", columnNum = 6)
	private int parentLevel;
	@ExcelSheet(columnName = "Parent Schema", columnNum = 7)
	private String parentSchema;
	@ExcelSheet(columnName = "Parent Table Name", columnNum = 8)
	private String parentTableName;
	@ExcelSheet(columnName = "Group", columnNum = 9)
	private String groupNm;
	private List<Db2Column> dbColumns;
	private List<ExcelColumn> excelColumns;

	public Db2Table() {
	}
	
	public String getNameProperCase() {
		return Helpers.toProperCase(name);
	}
	
	public String getNameCamelCase() {
		return Helpers.toCamelCase(name);
	}
	
	public String getNameVariableCase() {
		return Helpers.toVariableCase(name);
	}
	
	public String getGroupNmVariableCase() {
		return Helpers.toVariableCase(groupNm);
	}
	
	public String getNameParmCase() {
		return name.toLowerCase().replace("_", "-");
	}
	
	public String getNameParmCasePlural() {
		String plural = name.toLowerCase().replace("_", "-");
		if (plural.toLowerCase().endsWith("y")) plural = plural.substring(0, plural.length()-1)+"ies";
		else plural+="s";
		return plural;
	}
	
	public String getParentNameProperCase() {
		return Helpers.toProperCase(parentTableName);
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTablespaceNm() {
		return tablespaceNm;
	}

	public void setTablespaceNm(String tablespaceNm) {
		this.tablespaceNm = tablespaceNm;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getExtensionNm() {
		return extensionNm;
	}

	public void setExtensionNm(String extensionNm) {
		this.extensionNm = extensionNm;
	}

	public List<Db2Column> getDbColumns() {
		return dbColumns;
	}

	public void setDbColumns(List<Db2Column> dbColumns) {
		this.dbColumns = dbColumns;
	}

	public List<ExcelColumn> getExcelColumns() {
		return excelColumns;
	}

	public void setExcelColumns(List<ExcelColumn> excelColumns) {
		this.excelColumns = excelColumns;
	}
	
	public boolean isScd() {
		return scd;
	}

	public void setScd(boolean scd) {
		this.scd = scd;
	}

	public void addExcelColumn(ExcelColumn excelColumn) {
		if (excelColumns == null)
			excelColumns = new ArrayList<ExcelColumn>();
		excelColumns.add(excelColumn);
	}
	
	public void addDbColumn(Db2Column dbColumn) {
		if (dbColumns == null)
			dbColumns = new ArrayList<Db2Column>();
		dbColumns.add(dbColumn);
	}
	
	public static boolean isDdlRequested(int requestValue) {
		return ((requestValue & ARTIFACT_DDL) == ARTIFACT_DDL);
	}
	
	public static boolean isBeanRequested(int requestValue) {
		return ((requestValue & ARTIFACT_BEAN) == ARTIFACT_BEAN);
	}
	
	public static boolean isControllerRequested(int requestValue) {
		return ((requestValue & ARTIFACT_CONTROLLER) == ARTIFACT_CONTROLLER);
	}
	
	public static boolean isDataSourceRequested(int requestValue) {
		return ((requestValue & ARTIFACT_DATA_SOURCE) == ARTIFACT_DATA_SOURCE);
	}

	public boolean isEtl() {
		return etl;
	}

	public void setEtl(boolean etl) {
		this.etl = etl;
	}

	public String getParentSchema() {
		return parentSchema;
	}

	public void setParentSchema(String parentSchema) {
		this.parentSchema = parentSchema;
	}

	public String getParentTableName() {
		return parentTableName;
	}

	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}

	public int getParentLevel() {
		return parentLevel;
	}

	public void setParentLevel(int parentLevel) {
		this.parentLevel = parentLevel;
	}

	public String getGroupNm() {
		return groupNm;
	}

	public void setGroupNm(String groupNm) {
		this.groupNm = groupNm;
	}

	@Override
	public String toString() {
		return "Db2Table [schema=" + schema + ", name=" + name + ", tablespaceNm=" + tablespaceNm + ", remarks="
				+ remarks + ", extensionNm=" + extensionNm + ", scd=" + scd + ", etl=" + etl + ", parentLevel="
				+ parentLevel + ", parentSchema=" + parentSchema + ", parentTableName=" + parentTableName + ", groupNm="
				+ groupNm + ", dbColumns=" + dbColumns + ", excelColumns=" + excelColumns + "]";
	}

}
