package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.utils.Helpers;

public class Db2Column {
	@ExcelSheet(columnName = "Name", columnNum = 3)
	private String name;
	@ExcelSheet(columnName = "Data Type", columnNum = 4)
	private String dataType;
	@ExcelSheet(columnName = "Length", columnNum = 5)
	private int length;
	@ExcelSheet(columnName = "Scale", columnNum = 6)
	private int scale;
	@ExcelSheet(columnName = "Nullable", columnNum = 7)
	private boolean nullable;
	@ExcelSheet(columnName = "Default Value", columnNum = 8)
	private String defaultValue;
	@ExcelSheet(columnName = "Key Seq", columnNum = 9)
	private int keySeq;
	@ExcelSheet(columnName = "Is ID", columnNum = 10)
	private boolean id;
	@ExcelSheet(columnName = "Is SCD", columnNum = 11)
	private boolean scd;
	@ExcelSheet(columnName = "ETL Seq", columnNum = 12)
	private int etlSeq;
	@ExcelSheet(columnName = "Foreign Key Seq", columnNum = 13)
	private int foreignKeySeq;
	@ExcelSheet(columnName = "Remarks", columnNum = 14)
	private String remarks;
	
	public Db2Column() {}
	
	public Db2Column(String name, String dataType, int length, String remarks) {
		this(name, dataType, length, 0, true, null, -1, false, false, remarks);
	}
	
	public Db2Column(String name, String dataType, int length, int scale, boolean nullable, String defaultValue
			, String remarks) {
		this(name, dataType, length, scale, nullable, defaultValue, -1, false, false, remarks);
	}
	
	public Db2Column(String name, String dataType, int length, int scale, boolean nullable, String defaultValue,
			int keySeq, boolean id, boolean scd, String remarks) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.length = length;
		this.scale = scale;
		this.nullable = nullable;
		this.defaultValue = defaultValue;
		this.keySeq = keySeq;
		this.id = id;
		this.scd = scd;
		this.remarks = remarks;
	}
	
	public String getNameProperCase() {
		return Helpers.toProperCase(name);
	}
	
	public String getNameCamelCase() {
		return Helpers.toCamelCase(name);
	}
	
	public String getNameVarCase() {
		return Helpers.toVariableCase(name);
	}
	
	public String getNameParmCase() {
		return name.toLowerCase().replace("_", "-");
	}
	
	public String getJavaDataType() {
		return Helpers.relational2JavaDataType(dataType);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public int getKeySeq() {
		return keySeq;
	}
	public void setKeySeq(int keySeq) {
		this.keySeq = keySeq;
	}
	public boolean isId() {
		return id;
	}
	public void setId(boolean id) {
		this.id = id;
	}
	public boolean isScd() {
		return scd;
	}
	public void setScd(boolean scd) {
		this.scd = scd;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getEtlSeq() {
		return etlSeq;
	}

	public void setEtlSeq(int etlSeq) {
		this.etlSeq = etlSeq;
	}

	@Override
	public String toString() {
		return "DbColumn [name=" + name + ", dataType=" + dataType + ", length=" + length + ", scale=" + scale
				+ ", nullable=" + nullable + ", defaultValue=" + defaultValue + ", keySeq=" + keySeq + ", id=" + id
				+ ", scd=" + scd + ", etlSeq=" + etlSeq + ", foreignKeySeq=" + foreignKeySeq + ", remarks=" + remarks
				+ ", getNameProperCase()=" + getNameProperCase() + ", getNameCamelCase()=" + getNameCamelCase() + "]";
	}

	public int getForeignKeySeq() {
		return foreignKeySeq;
	}

	public void setForeignKeySeq(int foreignKeySeq) {
		this.foreignKeySeq = foreignKeySeq;
	}

}
