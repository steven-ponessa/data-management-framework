package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IndustryDim",baseTableName="REFT.INDUSTRY",parentBeanName="IndustrySolutionUnitDim",parentBaseTableName="REFT.INDUSTRY_SOLUTION_UNIT")
public class IndustryDim extends NaryTreeNode {
	@DbColumn(columnName="IND_ID",isId=true)
	private int        indId;
	@DbColumn(columnName="IND_CD",keySeq=1)
	private String     indCd;
	@DbColumn(columnName="IND_DESC")
	private String     indDesc;
	@DbColumn(columnName="USAGE_RULE_CD")
	private String     usageRuleCd;
	@DbColumn(columnName="ISU_CD",foreignKeySeq=1)
	private String     isuCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IndustryDim () {
		
	}
	
	// Define natural key constructor
	public IndustryDim (
      String     indCd
	) {
		this.indCd                          = indCd;
		
	}
	
	// Define base constructor
	public IndustryDim (
      String     indCd
    , String     indDesc
    , String     usageRuleCd
    , String     isuCd
	) {
		this.indCd                          = indCd;
		this.indDesc                        = indDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.isuCd                          = isuCd;
		
	}
    
	// Define full constructor
	public IndustryDim (
		  int        indId
		, String     indCd
		, String     indDesc
		, String     usageRuleCd
		, String     isuCd
	) {
		this.indId                          = indId;
		this.indCd                          = indCd;
		this.indDesc                        = indDesc;
		this.usageRuleCd                    = usageRuleCd;
		this.isuCd                          = isuCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.indCd
		;
	}
	public String getDescription() { 
		return this.indDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IndustryDim other = (IndustryDim) obj;
		if (
            this.indCd.equals(other.getIndCd())
         && this.indDesc.equals(other.getIndDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.indCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRuleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.isuCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("IND_CD")
        + "," + Helpers.formatCsvField("IND_DESC")
        + "," + Helpers.formatCsvField("USAGE_RULE_CD")
        + "," + Helpers.formatCsvField("ISU_CD")
		;
	}
    
	// Define Getters and Setters
	public int getIndId() {
		return indId;
	}
	public void setIndId(int indId) {
		this.indId = indId;
	}
	public String getIndCd() {
		return indCd;
	}
	public void setIndCd(String indCd) {
		this.indCd = indCd;
	}
	public String getIndDesc() {
		return indDesc;
	}
	public void setIndDesc(String indDesc) {
		this.indDesc = indDesc;
	}
	public String getUsageRuleCd() {
		return usageRuleCd;
	}
	public void setUsageRuleCd(String usageRuleCd) {
		this.usageRuleCd = usageRuleCd;
	}
	public String getIsuCd() {
		return isuCd;
	}
	public void setIsuCd(String isuCd) {
		this.isuCd = isuCd;
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