package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IsicDim",baseTableName="REFT.ISIC",parentBeanName="IndustryClassDim",parentBaseTableName="REFT.INDUSTRY_CLASS")
public class IsicDim extends NaryTreeNode {
	@DbColumn(columnName="ISIC_ID",isId=true)
	private int        isicId;
	@DbColumn(columnName="ISIC_CD",keySeq=1)
	private String     isicCd;
	@DbColumn(columnName="IISIC_DESC")
	private String     iisicDesc;
	@DbColumn(columnName="USAGE_RULE_CD")
	private String     usageRuleCd;
	@DbColumn(columnName="IND_CLSS_CD",foreignKeySeq=1)
	private String     indClssCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IsicDim () {
		
	}
	
	// Define natural key constructor
	public IsicDim (
      String     isicCd
	) {
		this.isicCd                         = isicCd;
		
	}
	
	// Define base constructor
	public IsicDim (
      String     isicCd
    , String     iisicDesc
    , String     usageRuleCd
    , String     indClssCd
	) {
		this.isicCd                         = isicCd;
		this.iisicDesc                      = iisicDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.indClssCd                      = indClssCd;
		
	}
    
	// Define full constructor
	public IsicDim (
		  int        isicId
		, String     isicCd
		, String     iisicDesc
		, String     usageRuleCd
		, String     indClssCd
	) {
		this.isicId                         = isicId;
		this.isicCd                         = isicCd;
		this.iisicDesc                      = iisicDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.indClssCd                      = indClssCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.isicCd
		;
	}
	public String getDescription() { 
		return this.iisicDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IsicDim other = (IsicDim) obj;
		if (
            this.isicCd.equals(other.getIsicCd())
         && this.iisicDesc.equals(other.getIisicDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.isicCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.iisicDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRuleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indClssCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ISIC_CD")
        + "," + Helpers.formatCsvField("IISIC_DESC")
        + "," + Helpers.formatCsvField("USAGE_RULE_CD")
        + "," + Helpers.formatCsvField("IND_CLSS_CD")
		;
	}
    
	// Define Getters and Setters
	public int getIsicId() {
		return isicId;
	}
	public void setIsicId(int isicId) {
		this.isicId = isicId;
	}
	public String getIsicCd() {
		return isicCd;
	}
	public void setIsicCd(String isicCd) {
		this.isicCd = isicCd;
	}
	public String getIisicDesc() {
		return iisicDesc;
	}
	public void setIisicDesc(String iisicDesc) {
		this.iisicDesc = iisicDesc;
	}
	public String getUsageRuleCd() {
		return usageRuleCd;
	}
	public void setUsageRuleCd(String usageRuleCd) {
		this.usageRuleCd = usageRuleCd;
	}
	public String getIndClssCd() {
		return indClssCd;
	}
	public void setIndClssCd(String indClssCd) {
		this.indClssCd = indClssCd;
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