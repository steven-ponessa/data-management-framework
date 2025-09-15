package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IndustryClassDim",baseTableName="REFT.INDUSTRY_CLASS",parentBeanName="IndustryDim",parentBaseTableName="REFT.INDUSTRY")
public class IndustryClassDim extends NaryTreeNode {
	@DbColumn(columnName="IND_CLSS_ID",isId=true)
	private int        indClssId;
	@DbColumn(columnName="IND_CLSS_CD",keySeq=1)
	private String     indClssCd;
	@DbColumn(columnName="IND_CLSS_DESC")
	private String     indClssDesc;
	@DbColumn(columnName="USAGE_RULE_CD")
	private String     usageRuleCd;
	@DbColumn(columnName="IND_CD",foreignKeySeq=1)
	private String     indCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IndustryClassDim () {
		
	}
	
	// Define natural key constructor
	public IndustryClassDim (
      String     indClssCd
	) {
		this.indClssCd                      = indClssCd;
		
	}
	
	// Define base constructor
	public IndustryClassDim (
      String     indClssCd
    , String     indClssDesc
    , String     usageRuleCd
    , String     indCd
	) {
		this.indClssCd                      = indClssCd;
		this.indClssDesc                    = indClssDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.indCd                          = indCd;
		
	}
    
	// Define full constructor
	public IndustryClassDim (
		  int        indClssId
		, String     indClssCd
		, String     indClssDesc
		, String     usageRuleCd
		, String     indCd
	) {
		this.indClssId                      = indClssId;
		this.indClssCd                      = indClssCd;
		this.indClssDesc                    = indClssDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.indCd                          = indCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.indClssCd
		;
	}
	public String getDescription() { 
		return this.indClssDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IndustryClassDim other = (IndustryClassDim) obj;
		if (
            this.indClssCd.equals(other.getIndClssCd())
         && this.indClssDesc.equals(other.getIndClssDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.indClssCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indClssDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRuleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("IND_CLSS_CD")
        + "," + Helpers.formatCsvField("IND_CLSS_DESC")
        + "," + Helpers.formatCsvField("USAGE_RULE_CD")
        + "," + Helpers.formatCsvField("IND_CD")
		;
	}
    
	// Define Getters and Setters
	public int getIndClssId() {
		return indClssId;
	}
	public void setIndClssId(int indClssId) {
		this.indClssId = indClssId;
	}
	public String getIndClssCd() {
		return indClssCd;
	}
	public void setIndClssCd(String indClssCd) {
		this.indClssCd = indClssCd;
	}
	public String getIndClssDesc() {
		return indClssDesc;
	}
	public void setIndClssDesc(String indClssDesc) {
		this.indClssDesc = indClssDesc;
	}
	public String getUsageRuleCd() {
		return usageRuleCd;
	}
	public void setUsageRuleCd(String usageRuleCd) {
		this.usageRuleCd = usageRuleCd;
	}
	public String getIndCd() {
		return indCd;
	}
	public void setIndCd(String indCd) {
		this.indCd = indCd;
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