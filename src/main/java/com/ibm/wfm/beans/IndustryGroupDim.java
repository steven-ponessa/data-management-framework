package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IndustryGroupDim",baseTableName="REFT.INDUSTRY_GROUP",parentBeanName="SectorDim",parentBaseTableName="REFT.SECTOR")
public class IndustryGroupDim extends NaryTreeNode {
	@DbColumn(columnName="IND_GRP_ID",isId=true)
	private int        indGrpId;
	@DbColumn(columnName="IND_GRP_CD",keySeq=1)
	private String     indGrpCd;
	@DbColumn(columnName="IND_GRP_DESC")
	private String     indGrpDesc;
	@DbColumn(columnName="SCTR_CD",foreignKeySeq=1)
	private String     sctrCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IndustryGroupDim () {
		
	}
	
	// Define natural key constructor
	public IndustryGroupDim (
      String     indGrpCd
	) {
		this.indGrpCd                       = indGrpCd;
		
	}
	
	// Define base constructor
	public IndustryGroupDim (
      String     indGrpCd
    , String     indGrpDesc
    , String     sctrCd
	) {
		this.indGrpCd                       = indGrpCd;
		this.indGrpDesc                     = indGrpDesc;
		this.sctrCd                         = sctrCd;
		
	}
    
	// Define full constructor
	public IndustryGroupDim (
		  int        indGrpId
		, String     indGrpCd
		, String     indGrpDesc
		, String     sctrCd
	) {
		this.indGrpId                       = indGrpId;
		this.indGrpCd                       = indGrpCd;
		this.indGrpDesc                     = indGrpDesc;
		this.sctrCd                         = sctrCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.indGrpCd
		;
	}
	public String getDescription() { 
		return this.indGrpDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IndustryGroupDim other = (IndustryGroupDim) obj;
		if (
            this.indGrpCd.equals(other.getIndGrpCd())
         && this.indGrpDesc.equals(other.getIndGrpDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.indGrpCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indGrpDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("IND_GRP_CD")
        + "," + Helpers.formatCsvField("IND_GRP_DESC")
        + "," + Helpers.formatCsvField("SCTR_CD")
		;
	}
    
	// Define Getters and Setters
	public int getIndGrpId() {
		return indGrpId;
	}
	public void setIndGrpId(int indGrpId) {
		this.indGrpId = indGrpId;
	}
	public String getIndGrpCd() {
		return indGrpCd;
	}
	public void setIndGrpCd(String indGrpCd) {
		this.indGrpCd = indGrpCd;
	}
	public String getIndGrpDesc() {
		return indGrpDesc;
	}
	public void setIndGrpDesc(String indGrpDesc) {
		this.indGrpDesc = indGrpDesc;
	}
	public String getSctrCd() {
		return sctrCd;
	}
	public void setSctrCd(String sctrCd) {
		this.sctrCd = sctrCd;
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