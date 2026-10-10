package com.ibm.wfm.beans.erdm;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IbmGeographyDim",baseTableName="ERDM.IBM_GEOGRAPHY")
public class IbmGeographyDim extends NaryTreeNode {
	@DbColumn(columnName="IBM_GEOGRAPHY_ID",isId=true)
	private int        ibmGeographyId;
	@DbColumn(columnName="LEVELNUM")
	private int        levelnum;
	@DbColumn(columnName="PARENTCODE")
	private String     parentcode;
	@DbColumn(columnName="CODE",keySeq=1)
	private String     code;
	@DbColumn(columnName="LONGDESCRIPTION")
	private String     longdescription;
	@DbColumn(columnName="MEDIUMDESCRIPTION")
	private String     mediumdescription;
	@DbColumn(columnName="SHORTDESCRIPTION")
	private String     shortdescription;
	@DbColumn(columnName="COMMENTS")
	private String     comments;
	@DbColumn(columnName="STATUS")
	private String     status;
	@DbColumn(columnName="CODE_TYPE")
	private String     codeType;
	@DbColumn(columnName="COMPLIANCE_CD")
	private String     complianceCd;
	@DbColumn(columnName="USAGE_RULE")
	private String     usageRule;
	@DbColumn(columnName="DISPLAY_SEQ")
	private int        displaySeq;
	@DbColumn(columnName="ORG_MASK")
	private Long        orgMask;
	@DbColumn(columnName="ALT_PARENTCODE")
	private String     altParentcode;
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
      int        levelnum
    , String     parentcode
    , String     code
    , String     longdescription
    , String     mediumdescription
    , String     shortdescription
    , String     comments
    , String     status
    , String     codeType
    , String     complianceCd
    , String     usageRule
    , int        displaySeq
    , Long       orgMask
    , String     altParentcode
	) {
		this.levelnum                       = levelnum;
		this.parentcode                     = parentcode;
		this.code                           = code;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.comments                       = comments;
		this.status                         = status;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		this.displaySeq                     = displaySeq;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		
	}
    
	// Define full constructor
	public IbmGeographyDim (
		  int        ibmGeographyId
		, int        levelnum
		, String     parentcode
		, String     code
		, String     longdescription
		, String     mediumdescription
		, String     shortdescription
		, String     comments
		, String     status
		, String     codeType
		, String     complianceCd
		, String     usageRule
		, int        displaySeq
		, Long       orgMask
		, String     altParentcode
	) {
		this.ibmGeographyId                 = ibmGeographyId;
		this.levelnum                       = levelnum;
		this.parentcode                     = parentcode;
		this.code                           = code;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.comments                       = comments;
		this.status                         = status;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		this.displaySeq                     = displaySeq;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		
	}
	
	@Override
	public String getCode() { 
		return this.code
		;
	}
	public String getDescription() { 
		return this.longdescription; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IbmGeographyDim other = (IbmGeographyDim) obj;
		if (
            this.code.equals(other.getCode())
         && this.parentcode.equals(other.getParentcode())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentcode))
        + "," + Helpers.formatCsvField(String.valueOf(this.code))
        + "," + Helpers.formatCsvField(String.valueOf(this.longdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.comments))
        + "," + Helpers.formatCsvField(String.valueOf(this.status))
        + "," + Helpers.formatCsvField(String.valueOf(this.codeType))
        + "," + Helpers.formatCsvField(String.valueOf(this.complianceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRule))
        + "," + Helpers.formatCsvField(String.valueOf(this.displaySeq))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMask))
        + "," + Helpers.formatCsvField(String.valueOf(this.altParentcode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("PARENTCODE")
        + "," + Helpers.formatCsvField("CODE")
        + "," + Helpers.formatCsvField("LONGDESCRIPTION")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("COMMENTS")
        + "," + Helpers.formatCsvField("STATUS")
        + "," + Helpers.formatCsvField("CODE_TYPE")
        + "," + Helpers.formatCsvField("COMPLIANCE_CD")
        + "," + Helpers.formatCsvField("USAGE_RULE")
        + "," + Helpers.formatCsvField("DISPLAY_SEQ")
        + "," + Helpers.formatCsvField("ORG_MASK")
        + "," + Helpers.formatCsvField("ALT_PARENTCODE")
		;
	}
    
	// Define Getters and Setters
	public int getIbmGeographyId() {
		return ibmGeographyId;
	}
	public void setIbmGeographyId(int ibmGeographyId) {
		this.ibmGeographyId = ibmGeographyId;
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
	//public String getCode() {
	//	return code;
	//}
	public void setCode(String code) {
		this.code = code;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public int getDisplaySeq() {
		return displaySeq;
	}
	public void setDisplaySeq(int displaySeq) {
		this.displaySeq = displaySeq;
	}
	public Long getOrgMask() {
		return orgMask;
	}
	public void setOrgMask(Long orgMask) {
		this.orgMask = orgMask;
	}
	public String getAltParentcode() {
		return altParentcode;
	}
	public void setAltParentcode(String altParentcode) {
		this.altParentcode = altParentcode;
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