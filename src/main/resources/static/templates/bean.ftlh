package com.ibm.wfm.beans;

<#assign hasDate=false>
<#assign hasTms=false>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.dataType=="DATE"><#assign hasDate=true></#if>
	<#if dbcolumn.dataType=="TIMESTAMP"><#assign hasTms=true></#if>
</#list>
<#if db2table.scd><#assign hasTms=true></#if>

<#if hasDate>
import java.sql.Date;
</#if>
<#if hasTms>
import java.sql.Timestamp;
</#if>
<#if hasDate || hasTms>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>

import com.ibm.wfm.annotations.DbTable;
<#if db2table.dbColumns??>
import com.ibm.wfm.annotations.DbColumn;
</#if>
<#if db2table.excelColumns??>
import com.ibm.wfm.annotations.ExcelSheet;
</#if>

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="${db2table.nameProperCase}Dim",baseTableName="${db2table.schema}.${db2table.name}"<#if db2table.parentSchema?? && db2table.parentSchema!="" && db2table.parentSchema!="root">,parentBeanName="${db2table.parentNameProperCase}Dim",parentBaseTableName="${db2table.parentSchema}.${db2table.parentTableName}"</#if>)
public class ${db2table.nameProperCase}Dim <#if db2table.parentSchema??>extends NaryTreeNode </#if>{
<#list db2table.dbColumns as dbcolumn>
	@DbColumn(columnName="${dbcolumn.name}"<#if dbcolumn.id>,isId=true</#if><#if dbcolumn.keySeq gt 0>,keySeq=${dbcolumn.keySeq}</#if><#if dbcolumn.foreignKeySeq gt 0>,foreignKeySeq=${dbcolumn.foreignKeySeq}</#if>)
	<#if dbcolumn.dataType=="TIMESTAMP">
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	</#if>
	<#if dbcolumn.dataType=="DATE">
	@JsonFormat(pattern="yyyy-MM-dd")
	</#if>
	<#if db2table.excelColumns??>
		<#list db2table.excelColumns as excelcolumn>
			<#if excelcolumn.dbName == dbcolumn.name>
	@ExcelSheet(columnName="${excelcolumn.name}"<#if excelcolumn.position?has_content>,columnNum=${excelcolumn.position}</#if>)	
			</#if>
		</#list>
	</#if>
	private ${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase};
</#list>
<#if db2table.scd>
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
</#if>

	// Define null constructor
	public ${db2table.nameProperCase}Dim () {
		<#if db2table.parentLevel gt 0>		this.level = ${db2table.parentLevel-1};</#if>
	}
	
	// Define natural key constructor
	public ${db2table.nameProperCase}Dim (
<#assign keySeqCnt = 0>
<#list db2table.dbColumns as dbcolumn>
		<#if dbcolumn.keySeq gt 0><#if keySeqCnt gt 0>    , <#else>      </#if><#assign keySeqCnt = keySeqCnt+1>${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase}${'\n'}</#if><#t>
</#list>
	) {
<#list db2table.dbColumns as dbcolumn>
		<#if dbcolumn.keySeq gt 0>		this.${dbcolumn.nameCamelCase?right_pad(30)} = ${dbcolumn.nameCamelCase};${'\n'}</#if><#t>
</#list>
		<#if db2table.parentLevel gt 0>this.level                          = ${db2table.parentLevel-1};</#if>
	}
	
<#if db2table.etl>
	// Define base constructor
	public ${db2table.nameProperCase}Dim (
<#assign etlCnt = 0>
<#list db2table.dbColumns as dbcolumn>
		<#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>    , <#else>      </#if><#assign etlCnt = etlCnt+1>${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase}${'\n'}</#if><#t>
</#list>
	) {
<#list db2table.dbColumns as dbcolumn>
		<#if dbcolumn.etlSeq gt 0>		this.${dbcolumn.nameCamelCase?right_pad(30)} = ${dbcolumn.nameCamelCase};${'\n'}</#if><#t>
</#list>
		<#if db2table.parentLevel gt 0>this.level                          = ${db2table.parentLevel-1};</#if>
	}
</#if>
    
<#assign keyCnt = 0>
	// Define full constructor
	public ${db2table.nameProperCase}Dim (
<#list db2table.dbColumns as dbcolumn>
		<#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.javaDataType?right_pad(9)}  ${dbcolumn.nameCamelCase}
</#list>
	) {
<#list db2table.dbColumns as dbcolumn>
		this.${dbcolumn.nameCamelCase?right_pad(30)} = ${dbcolumn.nameCamelCase};
</#list>
		<#if db2table.parentLevel gt 0>this.level                          = ${db2table.parentLevel-1};</#if>
	}
	
<#if db2table.parentSchema??>
	@Override
	public String getCode() { 
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>    +":"+ <#else>		return </#if><#assign keyCnt = keyCnt+1>this.${dbcolumn.nameCamelCase}${'\n'}</#if><#t>
</#list>
		;
	}
	public String getDescription() { 
		return null; 
	}
</#if>
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		${db2table.nameProperCase}Dim other = (${db2table.nameProperCase}Dim) obj;
		if (
<#assign etlCnt = 0>
<#list db2table.dbColumns as dbcolumn>
			<#if dbcolumn.etlSeq gt 0 && dbcolumn.etlSeq lt 3><#if etlCnt gt 0>         && <#else>            </#if><#assign etlCnt = etlCnt+1>this.${dbcolumn.nameCamelCase}.equals(other.get${dbcolumn.nameProperCase}())${'\n'}</#if><#t>
</#list>
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
		<#assign etlCnt = 0>
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>        + "," + <#else>                </#if><#assign etlCnt = etlCnt+1>Helpers.formatCsvField(String.valueOf(this.${dbcolumn.nameCamelCase}))${'\n'}</#if><#t>
</#list>
		;
	}
	
	public static String getEtlHeader() {
		return 
		<#assign etlCnt = 0>
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn.etlSeq gt 0><#if etlCnt gt 0>        + "," + <#else>                </#if><#assign etlCnt = etlCnt+1>Helpers.formatCsvField("${dbcolumn.name}")${'\n'}</#if><#t>
</#list>
		;
	}
    
	// Define Getters and Setters
<#list db2table.dbColumns as dbcolumn>
	public ${dbcolumn.javaDataType} get${dbcolumn.nameProperCase}() {
		return ${dbcolumn.nameCamelCase};
	}
	public void set${dbcolumn.nameProperCase}(${dbcolumn.javaDataType} ${dbcolumn.nameCamelCase}) {
		this.${dbcolumn.nameCamelCase} = ${dbcolumn.nameCamelCase};
	}
</#list>
<#if db2table.scd>
	public Timestamp getEffTms() {
		return effTms;
	}
	public void setEffTms(Timestamp effTms) {
		this.effTms = effTms;
	}
	public Timestamp getExpirTms() {
		return expirTms;
	}
	public void setExpirTms(Timestamp expirTms) {
		this.expirTms = expirTms;
	}
	public String getRowStatusCd() {
		return rowStatusCd;
	}
	public void setRowStatusCd(String rowStatusCd) {
		this.rowStatusCd = rowStatusCd;
	}
</#if>
}