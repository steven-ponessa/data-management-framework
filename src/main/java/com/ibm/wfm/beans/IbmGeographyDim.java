package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IbmGeographyDim",baseTableName="ERDM.IBM_GEOGRAPHY")
public class IbmGeographyDim {
	@DbColumn(columnName="IBM_GEOGRAPHY_ID",isId=true)
	private int        ibmGeographyId;
	
	@DbColumn(columnName="CODE",keySeq=1)
	@ExcelSheet(columnName="code",columnNum=3)	
	private String     code;
	
	@DbColumn(columnName="LEVELNUM")
	@ExcelSheet(columnName="levelNum",columnNum=1)	
	private int        levelnum;
	
	@DbColumn(columnName="PARENTCODE")
	@ExcelSheet(columnName="parentCode",columnNum=2)
	private String     parentcode;
	
	@DbColumn(columnName="LONGDESCRIPTION")
	@ExcelSheet(columnName="longDescription",columnNum=4)
	private String     longdescription;
	
	@DbColumn(columnName="MEDIUMDESCRIPTION")
	@ExcelSheet(columnName="mediumDescription",columnNum=5)
	private String     mediumdescription;
	
	@DbColumn(columnName="SHORTDESCRIPTION")
	@ExcelSheet(columnName="shortDescription",columnNum=6)
	private String     shortdescription;
	
	@DbColumn(columnName="COMMENTS")
	@ExcelSheet(columnName="comments",columnNum=7)
	private String     comments;
	
	@DbColumn(columnName="RECORDSTATUS")
	@ExcelSheet(columnName="recordStatus",columnNum=8)
	private String     recordstatus;
	
	@DbColumn(columnName="ROW_CREATE_TS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  rowCreateTs;
	
	@DbColumn(columnName="ROW_UPDATE_TS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  rowUpdateTs;
	
	@DbColumn(columnName="CODE_TYPE")
	@ExcelSheet(columnName="CODE_TYPE",columnNum=11)
	private String     codeType;
	
	@DbColumn(columnName="COMPLIANCE_CD")
	@ExcelSheet(columnName="COMPLIANCE_CD",columnNum=12)
	private String     complianceCd;
	
	@DbColumn(columnName="USAGE_RULE")
	@ExcelSheet(columnName="USAGE_RULE",columnNum=13)
	private String     usageRule;
	
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IbmGeographyDim () {
		
	}
	
	// Define natural key constructor
	public IbmGeographyDim (
      String     code
	) {
		this.code                           = code;
		
	}
	
	// Define base constructor
	public IbmGeographyDim (
      String     code
    , int        levelnum
    , String     parentcode
    , String     longdescription
    , String     mediumdescription
    , String     shortdescription
    , String     comments
    , String     recordstatus
    , Timestamp  rowCreateTs
    , Timestamp  rowUpdateTs
    , String     codeType
    , String     complianceCd
    , String     usageRule
	) {
		this.code                           = code;
		this.levelnum                       = levelnum;
		this.parentcode                     = parentcode;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.comments                       = comments;
		this.recordstatus                   = recordstatus;
		this.rowCreateTs                    = rowCreateTs;
		this.rowUpdateTs                    = rowUpdateTs;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		
	}
    
	// Define full constructor
	public IbmGeographyDim (
		  int        ibmGeographyId
		, String     code
		, int        levelnum
		, String     parentcode
		, String     longdescription
		, String     mediumdescription
		, String     shortdescription
		, String     comments
		, String     recordstatus
		, Timestamp  rowCreateTs
		, Timestamp  rowUpdateTs
		, String     codeType
		, String     complianceCd
		, String     usageRule
	) {
		this.ibmGeographyId                 = ibmGeographyId;
		this.code                           = code;
		this.levelnum                       = levelnum;
		this.parentcode                     = parentcode;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.comments                       = comments;
		this.recordstatus                   = recordstatus;
		this.rowCreateTs                    = rowCreateTs;
		this.rowUpdateTs                    = rowUpdateTs;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IbmGeographyDim other = (IbmGeographyDim) obj;
		if (
            this.code.equals(other.getCode())
         && this.levelnum == other.getLevelnum()
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.code))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentcode))
        + "," + Helpers.formatCsvField(String.valueOf(this.longdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.comments))
        + "," + Helpers.formatCsvField(String.valueOf(this.recordstatus))
        + "," + Helpers.formatCsvField(String.valueOf(this.rowCreateTs))
        + "," + Helpers.formatCsvField(String.valueOf(this.rowUpdateTs))
        + "," + Helpers.formatCsvField(String.valueOf(this.codeType))
        + "," + Helpers.formatCsvField(String.valueOf(this.complianceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRule))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CODE")
        + "," + Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("PARENTCODE")
        + "," + Helpers.formatCsvField("LONGDESCRIPTION")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("COMMENTS")
        + "," + Helpers.formatCsvField("RECORDSTATUS")
        + "," + Helpers.formatCsvField("ROW_CREATE_TS")
        + "," + Helpers.formatCsvField("ROW_UPDATE_TS")
        + "," + Helpers.formatCsvField("CODE_TYPE")
        + "," + Helpers.formatCsvField("COMPLIANCE_CD")
        + "," + Helpers.formatCsvField("USAGE_RULE")
		;
	}
    
	// Define Getters and Setters
	public int getIbmGeographyId() {
		return ibmGeographyId;
	}
	public void setIbmGeographyId(int ibmGeographyId) {
		this.ibmGeographyId = ibmGeographyId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getLevelnum() {
		return levelnum;
	}
	public void setLevelnum(int levelnum) {
		this.levelnum = levelnum;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public String getLongdescription() {
		return longdescription;
	}
	public void setLongdescription(String longdescription) {
		this.longdescription = longdescription;
	}
	public String getMediumdescription() {
		return mediumdescription;
	}
	public void setMediumdescription(String mediumdescription) {
		this.mediumdescription = mediumdescription;
	}
	public String getShortdescription() {
		return shortdescription;
	}
	public void setShortdescription(String shortdescription) {
		this.shortdescription = shortdescription;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRecordstatus() {
		return recordstatus;
	}
	public void setRecordstatus(String recordstatus) {
		this.recordstatus = recordstatus;
	}
	public Timestamp getRowCreateTs() {
		return rowCreateTs;
	}
	public void setRowCreateTs(Timestamp rowCreateTs) {
		this.rowCreateTs = rowCreateTs;
	}
	public Timestamp getRowUpdateTs() {
		return rowUpdateTs;
	}
	public void setRowUpdateTs(Timestamp rowUpdateTs) {
		this.rowUpdateTs = rowUpdateTs;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getComplianceCd() {
		return complianceCd;
	}
	public void setComplianceCd(String complianceCd) {
		this.complianceCd = complianceCd;
	}
	public String getUsageRule() {
		return usageRule;
	}
	public void setUsageRule(String usageRule) {
		this.usageRule = usageRule;
	}
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
}