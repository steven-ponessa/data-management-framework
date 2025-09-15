--Drop existing objects
DROP TRIGGER ${daotemplate.schema}.${daotemplate.name}_DIM_T1;
DROP TRIGGER ${daotemplate.schema}.${daotemplate.name}_DIM_T2;
DROP TRIGGER ${daotemplate.schema}.${daotemplate.name}_DIM_T3;
DROP TRIGGER ${daotemplate.schema}.${daotemplate.name}_DIM_T4;

DROP VIEW ${daotemplate.schema}.${daotemplate.name}_DIM_V;
<#if daotemplate.scd>
DROP VIEW ${daotemplate.schema}.${daotemplate.name}_SCD_V;
</#if>
<#if daotemplate.extensionNm?? && daotemplate.extensionNm?length gt 0>
DROP VIEW ${daotemplate.schema}.${daotemplate.name}_${daotemplate.extensionNm}_DIM_V;
<#if daotemplate.scd>
DROP VIEW ${daotemplate.schema}.${daotemplate.name}_${daotemplate.extensionNm}_SCD_V;
</#if>
DROP VIEW ${daotemplate.schema}.${daotemplate.name}_${daotemplate.extensionNm}_DIM;
</#if>

DROP TABLE ${daotemplate.schema}.${daotemplate.name}_DIM;

DROP TABLESPACE <#t>
<#if daotemplate.name?length gt 15>
TS_${daotemplate.name?truncate_c(15,"")}<#t>
<#else>
TS_${daotemplate.name}<#t>
</#if>
;

-- Create Tablespace
CREATE TABLESPACE <#t>
<#if daotemplate.name?length gt 15>
TS_${daotemplate.name?truncate_c(15,"")}<#t>
<#else>
TS_${daotemplate.name}<#t>
</#if>
MANAGED BY AUTOMATIC STORAGE;

-- Create EDS table
CREATE TABLE ${daotemplate.schema}.${daotemplate.name}_DIM (
<#list daotemplate.dbColumns as dbcolumn>
	<#if dbcolumn.id>
   ${dbcolumn.name} INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 ... <#if dbcolumn?has_next>,</#if>
	<#else>
   ${dbcolumn.name?right_pad(35-dbcolumn.name?length)} ${dbcolumn.dataType}<#if dbcolumn.dataType=="CHAR" || dbcolumn.dataType=="VARCHAR">(${dbcolumn.length})</#if> <#if dbcolumn.dataType=="DECIMAL" || dbcolumn.dataType=="FIXED DECIMAL">(${dbcolumn.length},${dbcolumn.scale})</#if><#if !dbcolumn.nullable>NOT NULL</#if> <#sep>,
	</#if>
</#list>

 )
<#if daotemplate.name?length gt 15>
 IN TS_${daotemplate.name?truncate_c(15,"")}
<#else>
 IN TS_${daotemplate.name}
</#if>
 DATA CAPTURE NONE
 COMPRESS NO;

in src/main/java/com/ibm/wfm/util

