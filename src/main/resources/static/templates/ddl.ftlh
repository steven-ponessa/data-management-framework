--Drop existing objects
<#if db2table.scd>
DROP TRIGGER ${db2table.schema}.${db2table.name}_DIM_T1;
DROP TRIGGER ${db2table.schema}.${db2table.name}_DIM_T2;
DROP TRIGGER ${db2table.schema}.${db2table.name}_DIM_T3;
DROP TRIGGER ${db2table.schema}.${db2table.name}_DIM_T4;
	<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
DROP TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T1;
DROP TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T2;
DROP TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T3;
DROP TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T4;

DROP VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_TV;
	</#if>
	
DROP VIEW ${db2table.schema}.${db2table.name}_DIM_TV;
</#if>


DROP VIEW ${db2table.schema}.${db2table.name}_EDS_DIM_V;
DROP VIEW ${db2table.schema}.${db2table.name}_DIM_V;

<#if db2table.scd>
DROP VIEW ${db2table.schema}.${db2table.name}_SCD_V;
DROP VIEW ${db2table.schema}.${db2table.name}_EDS_SCD_V;
</#if>
<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
DROP VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_V;
	<#if db2table.scd>
DROP VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_SCD_V;
	</#if>

DROP TABLE ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM;
</#if>
DROP TABLE ${db2table.schema}.${db2table.name}_DIM;

DROP TABLESPACE <#t>
<#if db2table.name?length gt 15>
TS_${db2table.name?truncate_c(15,"")}<#t>
<#else>
TS_${db2table.name}<#t>
</#if>
;

<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
DROP TABLESPACE <#t>
	<#if db2table.name?length gt 13>
TS_E_${db2table.name?truncate_c(13,"")}<#t>
	<#else>
TS_E_${db2table.name}<#t>
	</#if>
;
</#if>

-- Create Tablespace
CREATE TABLESPACE <#t>
<#if db2table.name?length gt 15>
TS_${db2table.name?truncate_c(15,"")}<#t>
<#else>
TS_${db2table.name}<#t>
</#if>
 MANAGED BY AUTOMATIC STORAGE;
 
<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
CREATE TABLESPACE <#t>
	<#if db2table.name?length gt 13>
TS_E_${db2table.name?truncate_c(13,"")}<#t>
	<#else>
TS_E_${db2table.name}<#t>
	</#if>
 MANAGED BY AUTOMATIC STORAGE;
</#if>

-- Create EDS table
CREATE TABLE ${db2table.schema}.${db2table.name}_DIM (
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.id>
   <#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.name} INTEGER  NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 NO CYCLE CACHE 20 NO ORDER )
	<#else>
   <#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.name?right_pad(35)} ${dbcolumn.dataType}<#if dbcolumn.dataType?trim=="CHAR" || dbcolumn.dataType?trim=="CHARACTER" || dbcolumn.dataType?trim=="VARCHAR">(${dbcolumn.length})</#if> <#if dbcolumn.dataType?trim=="DECIMAL" || dbcolumn.dataType?trim=="FIXED DECIMAL">(${dbcolumn.length},${dbcolumn.scale})</#if><#if !dbcolumn.nullable>NOT NULL</#if>
	</#if>
</#list>
<#if db2table.scd>
   , ROW_STATUS_CD                       CHAR(1) NOT NULL DEFAULT 'I'
   , VERSION_NUM                         INTEGER NOT NULL DEFAULT 1
   , EFF_TMS                             TIMESTAMP NOT NULL
   , EXPIR_TMS                           TIMESTAMP NOT NULL
</#if>
 )
<#if db2table.name?length gt 15>
 IN TS_${db2table.name?truncate_c(15,"")}
<#else>
 IN TS_${db2table.name}
</#if>
 DATA CAPTURE NONE
 COMPRESS NO;
 
--Alter EDS table to add primary key for Surrogate Id
ALTER TABLE ${db2table.schema}.${db2table.name}_DIM ADD CONSTRAINT ${db2table.name}_DIM_PK PRIMARY KEY (<#t>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.id>${dbcolumn.name});</#if><#t>
</#list>

<#assign keyCnt = 0>
--Create EDS UNIQUE INDEX based on the natural key and SCD attributes
CREATE UNIQUE INDEX ${db2table.schema}.${db2table.name}_DIM_UX1 
    ON ${db2table.schema}.${db2table.name}_DIM (
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>    , <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name}${'\n'}</#if><#t>
</#list>
<#if db2table.scd>
    , EFF_TMS
    , EXPIR_TMS
</#if>
    )
    MINPCTUSED 0
    DISALLOW REVERSE SCANS
    PAGE SPLIT SYMMETRIC
    COLLECT SAMPLED DETAILED STATISTICS
    COMPRESS NO;
    
--Add comments to the EDS table
COMMENT ON TABLE ${db2table.schema}.${db2table.name}_DIM IS '${db2table.remarks!""}';

--Add comments to the EDS columns
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.id>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.${dbcolumn.name} IS 'Surrogate key for ${db2table.schema}.${db2table.name}_DIM.'; 
	<#else>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.${dbcolumn.name} IS '${dbcolumn.remarks!""}.'; 
	</#if>
</#list>   
<#if db2table.scd>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.ROW_STATUS_CD IS 'Row Status code. ''I'' - Insert, ''U'' - Update, ''D'' - Delete.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.VERSION_NUM IS 'Version number of the record''s state.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.EFF_TMS IS 'Timestamp indicating when the particular record became effective within the slow changing dimension. Note that each record of a SCD represents a ''state'' of the object/data.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_DIM.EXPIR_TMS IS 'Timestamp indicating when the particular record has expired (is no longer in effect) within the slow changing dimension.. 9999-12-31-23.59.59.999999 represents the current state of the record.';
</#if>   

--Views
<#if db2table.scd>
CREATE VIEW ${db2table.schema}.${db2table.name}_DIM_TV
AS
SELECT * FROM ${db2table.schema}.${db2table.name}_DIM;
</#if>

<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
--if db2table.extensionNm?? 
<#else>
--Come on - #else
CREATE VIEW ${db2table.schema}.${db2table.name}_DIM_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn?is_first>        <#else>     ,  </#if>${dbcolumn.name}
</#list>
FROM   ${db2table.schema}.${db2table.name}_DIM<#if !db2table.scd>;</#if>
<#if db2table.scd>
WHERE  EXPIR_TMS = '9999-12-31-23.59.59.999999';

CREATE VIEW ${db2table.schema}.${db2table.name}_SCD_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn?is_first>    <#else> ,  </#if>${dbcolumn.name}
</#list>
     ,  ROW_STATUS_CD 
     ,  VERSION_NUM
     ,  EFF_TMS 
     ,  EXPIR_TMS
FROM   ${db2table.schema}.${db2table.name}_DIM;
</#if>
</#if>

CREATE VIEW ${db2table.schema}.${db2table.name}_EDS_DIM_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn?is_first>        <#else>     ,  </#if>${dbcolumn.name}
</#list>
FROM   ${db2table.schema}.${db2table.name}_DIM<#if !db2table.scd>;</#if>
<#if db2table.scd>
WHERE  EXPIR_TMS = '9999-12-31-23.59.59.999999';

CREATE VIEW ${db2table.schema}.${db2table.name}_EDS_SCD_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn?is_first>        <#else>     ,  </#if>${dbcolumn.name}
</#list>
   ,  ROW_STATUS_CD
   ,  VERSION_NUM
   ,  EFF_TMS 
   ,  EXPIR_TMS  
FROM   ${db2table.schema}.${db2table.name}_DIM;

--Create Triggers to manage SCD
--T1 Set Effective and Expiration Timestamps on new/inserted records
CREATE TRIGGER ${db2table.schema}.${db2table.name}_DIM_T1
    BEFORE INSERT ON ${db2table.schema}.${db2table.name}_DIM
    REFERENCING  NEW AS NEWVAL
    FOR EACH ROW
SET EFF_TMS       = CURRENT TIMESTAMP
  , EXPIR_TMS     = '9999-12-31-23.59.59.999999'
  , ROW_STATUS_CD = 'I';
  
--T2 Expire previous record for the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
CREATE TRIGGER ${db2table.schema}.${db2table.name}_DIM_T2
    AFTER INSERT ON ${db2table.schema}.${db2table.name}_DIM
    REFERENCING  NEW AS N
    FOR EACH ROW
UPDATE ${db2table.schema}.${db2table.name}_DIM
  SET EXPIR_TMS     = N.EFF_TMS - 1 MICROSECOND 
    , ROW_STATUS_CD = 'H'
WHERE 
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name?right_pad(35)}= N.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
--  AND VERSION_NUM                      = N.VERSION_NUM-1 
  AND EXPIR_TMS                          = '9999-12-31-23.59.59.999999'
  AND EFF_TMS                            < N.EFF_TMS;

--T3 Change DELETE transaction to expire previous record for the natural key.
CREATE TRIGGER ${db2table.schema}.${db2table.name}_DIM_T3
    INSTEAD OF DELETE ON ${db2table.schema}.${db2table.name}_DIM_TV
    REFERENCING  OLD AS O
    FOR EACH ROW
UPDATE ${db2table.schema}.${db2table.name}_DIM
  SET EXPIR_TMS     = CURRENT TIMESTAMP
    , ROW_STATUS_CD = 'D'
WHERE 
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name?right_pad(35)}= O.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
  AND EXPIR_TMS                          = '9999-12-31-23.59.59.999999';
  
--T4 Change UPDATE transaction to insert a new record.
-- The insert will invoke T1 to set the effective and expiration timestamp and
-- T2 to expire previous record for the natural key.
CREATE TRIGGER ${db2table.schema}.${db2table.name}_DIM_T4
    INSTEAD OF UPDATE ON ${db2table.schema}.${db2table.name}_DIM_TV
    REFERENCING  NEW AS N
    FOR EACH ROW
INSERT INTO ${db2table.schema}.${db2table.name}_DIM
 (
<#assign columnCnt = 0>
<#list db2table.dbColumns as dbcolumn>
<#if !dbcolumn.id>
<#if columnCnt == 0>        <#else>     ,  </#if><#assign columnCnt = columnCnt+1>${dbcolumn.name}
</#if>
</#list> 
 )
VALUES
 (
<#assign columnCnt = 0>
<#list db2table.dbColumns as dbcolumn>
<#if !dbcolumn.id>
<#if columnCnt == 0>        <#else>     ,  </#if><#assign columnCnt = columnCnt+1>N.${dbcolumn.name}
</#if>
</#list> 
 );  
 </#if>
 
<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
 -- Create Extension table
CREATE TABLE ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM (
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.id>
   <#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.name} INTEGER  NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 10000 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 NO CYCLE CACHE 20 NO ORDER )
	<#else>
   <#if dbcolumn?is_first>  <#else>, </#if>${dbcolumn.name?right_pad(35)} ${dbcolumn.dataType}<#if dbcolumn.dataType?trim=="CHAR" || dbcolumn.dataType?trim=="VARCHAR">(${dbcolumn.length})</#if> <#if dbcolumn.dataType?trim=="DECIMAL" || dbcolumn.dataType?trim=="FIXED DECIMAL">(${dbcolumn.length},${dbcolumn.scale})</#if><#if !dbcolumn.nullable>NOT NULL</#if>
	</#if>
</#list>
<#if db2table.scd>
     , ROW_STATUS_CD                       CHAR(1) NOT NULL DEFAULT 'I'
     , VERSION_NUM                         INTEGER NOT NULL DEFAULT 1
     , EFF_TMS                             TIMESTAMP NOT NULL
     , EXPIR_TMS                           TIMESTAMP NOT NULL
</#if>
 )
<#if db2table.name?length gt 13>
 IN TS_E_${db2table.name?truncate_c(13,"")}
<#else>
 IN TS_E_${db2table.name}
</#if>
 DATA CAPTURE NONE
 COMPRESS NO;
 
 --Alter Extension table to add primary key for Surrogate Id
ALTER TABLE ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM ADD CONSTRAINT ${db2table.name}_${db2table.extensionNm}_DIM_PK PRIMARY KEY (<#t>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.id>${dbcolumn.name});</#if><#t>
</#list>

<#assign keyCnt = 0>
--Create Extension UNIQUE INDEX based on the natural key and SCD attributes
CREATE UNIQUE INDEX ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_UX1 
    ON ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM (
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>    , <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name}${'\n'}</#if><#t>
</#list>
<#if db2table.scd>
    , EFF_TMS
    , EXPIR_TMS
</#if>
    )
    MINPCTUSED 0
    DISALLOW REVERSE SCANS
    PAGE SPLIT SYMMETRIC
    COLLECT SAMPLED DETAILED STATISTICS
    COMPRESS NO;
    
--Add comments to the EDS table
COMMENT ON TABLE ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM IS '${db2table.remarks!""}';

--Add comments to the EDS columns
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn.id>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.${dbcolumn.name} IS 'Surrogate key for ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.'; 
<#else>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.${dbcolumn.name} IS '${dbcolumn.remarks!""}.'; 
</#if>
</#list>   
<#if db2table.scd>
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.ROW_STATUS_CD IS 'Row Status code. ''I'' - Insert, ''U'' - Update, ''D'' - Delete.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.VERSION_NUM IS 'Version number of the record''s state.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.EFF_TMS IS 'Timestamp indicating when the particular record became effective within the slow changing dimension. Note that each record of a SCD represents a ''state'' of the object/data.';
COMMENT ON COLUMN ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM.EXPIR_TMS IS 'Timestamp indicating when the particular record has expired (is no longer in effect) within the slow changing dimension.. 9999-12-31-23.59.59.999999 represents the current state of the record.';
</#if>   

<#if db2table.extensionNm?? && db2table.extensionNm?length gt 0>
CREATE VIEW ${db2table.schema}.${db2table.name}_DIM_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn?is_first>    <#else> ,  </#if>COALESCE(D.${dbcolumn.name},P.${dbcolumn.name}) AS ${dbcolumn.name}
</#list>
--     ,  CASE WHEN (D.BRAND_CD IS NULL) THEN 'B' ELSE 'E' END AS RECORD_TYP_CD
FROM   ${db2table.schema}.${db2table.name}_DIM P
FULL OUTER JOIN
       ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM D
  ON  
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>P.${dbcolumn.name?right_pad(35)} = D.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
<#if !db2table.scd>;</#if>
<#if db2table.scd>
WHERE  (P.EXPIR_TMS = '9999-12-31-23.59.59.999999' OR P.EXPIR_TMS IS NULL)
  AND  (D.EXPIR_TMS = '9999-12-31-23.59.59.999999' OR D.EXPIR_TMS IS NULL); 

CREATE VIEW ${db2table.schema}.${db2table.name}_SCD_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn?is_first>    <#else> ,  </#if>COALESCE(D.${dbcolumn.name},P.${dbcolumn.name}) AS ${dbcolumn.name}
</#list>
     ,  COALESCE(D.ROW_STATUS_CD, P.ROW_STATUS_CD) AS ROW_STATUS_CD
     ,  COALESCE(D.VERSION_NUM, P.VERSION_NUM)     AS VERSION_NUM
     ,  COALESCE(D.EFF_TMS, P.EFF_TMS)             AS EFF_TMS
     ,  COALESCE(D.EXPIR_TMS, P.EXPIR_TMS)         AS EXPIR_TMS  
--     ,  CASE WHEN (D.BRAND_CD IS NULL) THEN 'B' ELSE 'E' END AS RECORD_TYP_CD
FROM   ${db2table.schema}.${db2table.name}_DIM P
FULL OUTER JOIN
       ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM D
  ON  
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>P.${dbcolumn.name?right_pad(35)} = D.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
;
</#if>
<#else>
--Views
CREATE VIEW ${db2table.schema}.${db2table.name}_DIM_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn?is_first>    <#else> ,  </#if>${dbcolumn.name}
</#list>
FROM   ${db2table.schema}.${db2table.name}_DIM<#if !db2table.scd>;</#if>
<#if db2table.scd>
WHERE  EXPIR_TMS = '9999-12-31-23.59.59.999999';

CREATE VIEW ${db2table.schema}.${db2table.name}_SCD_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
    <#if dbcolumn?is_first>    <#else> ,  </#if>${dbcolumn.name}
</#list>
     ,  ROW_STATUS_CD 
     ,  VERSION_NUM
     ,  EFF_TMS 
     ,  EXPIR_TMS
FROM   ${db2table.schema}.${db2table.name}_DIM;
</#if>

</#if>

--Extension Views
<#if db2table.scd>
CREATE VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_TV
AS
SELECT * FROM ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM;
</#if>

CREATE VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn?is_first>        <#else>     ,  </#if>${dbcolumn.name}
</#list>
FROM   ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM<#if !db2table.scd>;</#if>
<#if db2table.scd>
WHERE  EXPIR_TMS = '9999-12-31-23.59.59.999999';

CREATE VIEW ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_SCD_V
AS    
SELECT
<#list db2table.dbColumns as dbcolumn>
<#if dbcolumn?is_first>        <#else>     ,  </#if>${dbcolumn.name}
</#list>
     , ROW_STATUS_CD
     , VERSION_NUM
     , EFF_TMS 
     , EXPIR_TMS  
FROM   ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM;
</#if> 

<#if db2table.scd>
--Create Triggers to manage SCD
--T1 Set Effective and Expiration Timestamps on new/inserted records
CREATE TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T1
    BEFORE INSERT ON ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM
    REFERENCING  NEW AS NEWVAL
    FOR EACH ROW
SET EFF_TMS       = CURRENT TIMESTAMP
  , EXPIR_TMS     = '9999-12-31-23.59.59.999999'
  , ROW_STATUS_CD = 'I';
  
--T2 Expire previous record for the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
CREATE TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T2
    AFTER INSERT ON ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM
    REFERENCING  NEW AS N
    FOR EACH ROW
UPDATE ${db2table.schema}.${db2table.name}_DIM
  SET EXPIR_TMS     = N.EFF_TMS - 1 MICROSECOND 
    , ROW_STATUS_CD = 'H'
WHERE 
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name?right_pad(35)}= N.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
--  AND VERSION_NUM                      = N.VERSION_NUM-1 
  AND EXPIR_TMS                          = '9999-12-31-23.59.59.999999'
  AND EFF_TMS                            < N.EFF_TMS;

--T3 Change DELETE transaction to expire previous record for the natural key.
CREATE TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T3
    INSTEAD OF DELETE ON ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_TV
    REFERENCING  OLD AS O
    FOR EACH ROW
UPDATE ${db2table.schema}.${db2table.name}_DIM
  SET EXPIR_TMS     = CURRENT TIMESTAMP
    , ROW_STATUS_CD = 'D'
WHERE 
<#assign keyCnt = 0>
<#list db2table.dbColumns as dbcolumn>
	<#if dbcolumn.keySeq gt 0><#if keyCnt gt 0>  AND <#else>      </#if><#assign keyCnt = keyCnt+1>${dbcolumn.name?right_pad(35)}= O.${dbcolumn.name}${'\n'}</#if><#t>
</#list>
  AND EXPIR_TMS                          = '9999-12-31-23.59.59.999999';
  
--T4 Change UPDATE transaction to insert a new record.
-- The insert will invoke T1 to set the effective and expiration timestamp and
-- T2 to expire previous record for the natural key.
CREATE TRIGGER ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_T4
    INSTEAD OF UPDATE ON ${db2table.schema}.${db2table.name}_${db2table.extensionNm}_DIM_TV
    REFERENCING  NEW AS N
    FOR EACH ROW
INSERT INTO ${db2table.schema}.${db2table.name}_DIM
 (
<#assign columnCnt = 0>
<#list db2table.dbColumns as dbcolumn>
<#if !dbcolumn.id>
<#if columnCnt == 0>        <#else>     ,  </#if><#assign columnCnt = columnCnt+1>${dbcolumn.name}
</#if>
</#list> 
 )
VALUES
 (
<#assign columnCnt = 0>
<#list db2table.dbColumns as dbcolumn>
<#if !dbcolumn.id>
<#if columnCnt == 0>        <#else>     ,  </#if><#assign columnCnt = columnCnt+1>N.${dbcolumn.name}
</#if>
</#list> 
 );

</#if>
</#if> <#-- end - <#if db2table.extensionNm?? && db2table.extensionNm?length gt 0> line 238 (extension) -->
    
--template: Users/steve/eclipse-workspace/ms-spring-server/src/main/resources/static/templates/ddl.ftlh