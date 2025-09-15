package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SectorDim",baseTableName="REFT.SECTOR",parentBeanName="GtmSegmentDim",parentBaseTableName="REFT.GTM_SEGMENT")
public class SectorDim extends NaryTreeNode {
	@DbColumn(columnName="SCTR_ID",isId=true)
	private int        sctrId;
	@DbColumn(columnName="SCTR_CD",keySeq=1)
	private String     sctrCd;
	@DbColumn(columnName="SCTR_DESC")
	private String     sctrDesc;
	@DbColumn(columnName="SCTR_MED_DESC")
	private String     sctrMedDesc;
	@DbColumn(columnName="SCTR_SHRT_DESC")
	private String     sctrShrtDesc;
	@DbColumn(columnName="SCTR_ALT1_DESC")
	private String     sctrAlt1Desc;
	@DbColumn(columnName="SCTR_ALT2_DESC")
	private String     sctrAlt2Desc;
	@DbColumn(columnName="USAGE_RULE_CD")
	private String     usageRuleCd;
	@DbColumn(columnName="GTM_SGMT_CD",foreignKeySeq=1)
	private String     gtmSgmtCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SectorDim () {
		
	}
	
	// Define natural key constructor
	public SectorDim (
      String     sctrCd
	) {
		this.sctrCd                         = sctrCd;
		
	}
	
	// Define base constructor
	public SectorDim (
      String     sctrCd
    , String     sctrDesc
    , String     sctrMedDesc
    , String     sctrShrtDesc
    , String     sctrAlt1Desc
    , String     sctrAlt2Desc
    , String     usageRuleCd
    , String     gtmSgmtCd
	) {
		this.sctrCd                         = sctrCd;
		this.sctrDesc                       = sctrDesc;
		this.sctrMedDesc                    = sctrMedDesc;
		this.sctrShrtDesc                   = sctrShrtDesc;
		this.sctrAlt1Desc                   = sctrAlt1Desc;
		this.sctrAlt2Desc                   = sctrAlt2Desc;
		this.usageRuleCd                    = usageRuleCd;
		this.gtmSgmtCd                      = gtmSgmtCd;
		
	}
    
	// Define full constructor
	public SectorDim (
		  int        sctrId
		, String     sctrCd
		, String     sctrDesc
		, String     sctrMedDesc
		, String     sctrShrtDesc
		, String     sctrAlt1Desc
		, String     sctrAlt2Desc
		, String     usageRuleCd
		, String     gtmSgmtCd
	) {
		this.sctrId                         = sctrId;
		this.sctrCd                         = sctrCd;
		this.sctrDesc                       = sctrDesc;
		this.sctrMedDesc                    = sctrMedDesc;
		this.sctrShrtDesc                   = sctrShrtDesc;
		this.sctrAlt1Desc                   = sctrAlt1Desc;
		this.sctrAlt2Desc                   = sctrAlt2Desc;
		this.usageRuleCd                    = usageRuleCd;
		this.gtmSgmtCd                      = gtmSgmtCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.sctrCd
		;
	}
	public String getDescription() { 
		return this.getSctrDesc(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SectorDim other = (SectorDim) obj;
		if (
            this.sctrCd.equals(other.getSctrCd())
         && this.sctrDesc.equals(other.getSctrDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.sctrCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrMedDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrShrtDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrAlt1Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrAlt2Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRuleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gtmSgmtCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SCTR_CD")
        + "," + Helpers.formatCsvField("SCTR_DESC")
        + "," + Helpers.formatCsvField("SCTR_MED_DESC")
        + "," + Helpers.formatCsvField("SCTR_SHRT_DESC")
        + "," + Helpers.formatCsvField("SCTR_ALT1_DESC")
        + "," + Helpers.formatCsvField("SCTR_ALT2_DESC")
        + "," + Helpers.formatCsvField("USAGE_RULE_CD")
        + "," + Helpers.formatCsvField("GTM_SGMT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getSctrId() {
		return sctrId;
	}
	public void setSctrId(int sctrId) {
		this.sctrId = sctrId;
	}
	public String getSctrCd() {
		return sctrCd;
	}
	public void setSctrCd(String sctrCd) {
		this.sctrCd = sctrCd;
	}
	public String getSctrDesc() {
		return sctrDesc;
	}
	public void setSctrDesc(String sctrDesc) {
		this.sctrDesc = sctrDesc;
	}
	public String getSctrMedDesc() {
		return sctrMedDesc;
	}
	public void setSctrMedDesc(String sctrMedDesc) {
		this.sctrMedDesc = sctrMedDesc;
	}
	public String getSctrShrtDesc() {
		return sctrShrtDesc;
	}
	public void setSctrShrtDesc(String sctrShrtDesc) {
		this.sctrShrtDesc = sctrShrtDesc;
	}
	public String getSctrAlt1Desc() {
		return sctrAlt1Desc;
	}
	public void setSctrAlt1Desc(String sctrAlt1Desc) {
		this.sctrAlt1Desc = sctrAlt1Desc;
	}
	public String getSctrAlt2Desc() {
		return sctrAlt2Desc;
	}
	public void setSctrAlt2Desc(String sctrAlt2Desc) {
		this.sctrAlt2Desc = sctrAlt2Desc;
	}
	public String getUsageRuleCd() {
		return usageRuleCd;
	}
	public void setUsageRuleCd(String usageRuleCd) {
		this.usageRuleCd = usageRuleCd;
	}
	public String getGtmSgmtCd() {
		return gtmSgmtCd;
	}
	public void setGtmSgmtCd(String gtmSgmtCd) {
		this.gtmSgmtCd = gtmSgmtCd;
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