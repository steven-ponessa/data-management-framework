package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="IndustrySolutionUnitDim",baseTableName="REFT.INDUSTRY_SOLUTION_UNIT",parentBeanName="IndustryGroupDim",parentBaseTableName="REFT.INDUSTRY_GROUP")
public class IndustrySolutionUnitDim extends NaryTreeNode {
	@DbColumn(columnName="ISU_ID",isId=true)
	private int        isuId;
	@DbColumn(columnName="ISU_CD",keySeq=1)
	private String     isuCd;
	@DbColumn(columnName="ISU_DESC")
	private String     isuDesc;
	@DbColumn(columnName="IND_GRP_CD",foreignKeySeq=1)
	private String     indGrpCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public IndustrySolutionUnitDim () {
		
	}
	
	// Define natural key constructor
	public IndustrySolutionUnitDim (
      String     isuCd
	) {
		this.isuCd                          = isuCd;
		
	}
	
	// Define base constructor
	public IndustrySolutionUnitDim (
      String     isuCd
    , String     isuDesc
    , String     indGrpCd
	) {
		this.isuCd                          = isuCd;
		this.isuDesc                        = isuDesc;
		this.indGrpCd                       = indGrpCd;
		
	}
    
	// Define full constructor
	public IndustrySolutionUnitDim (
		  int        isuId
		, String     isuCd
		, String     isuDesc
		, String     indGrpCd
	) {
		this.isuId                          = isuId;
		this.isuCd                          = isuCd;
		this.isuDesc                        = isuDesc;
		this.indGrpCd                       = indGrpCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.isuCd
		;
	}
	public String getDescription() { 
		return this.isuDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		IndustrySolutionUnitDim other = (IndustrySolutionUnitDim) obj;
		if (
            this.isuCd.equals(other.getIsuCd())
         && this.isuDesc.equals(other.getIsuDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.isuCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.isuDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.indGrpCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ISU_CD")
        + "," + Helpers.formatCsvField("ISU_DESC")
        + "," + Helpers.formatCsvField("IND_GRP_CD")
		;
	}
    
	// Define Getters and Setters
	public int getIsuId() {
		return isuId;
	}
	public void setIsuId(int isuId) {
		this.isuId = isuId;
	}
	public String getIsuCd() {
		return isuCd;
	}
	public void setIsuCd(String isuCd) {
		this.isuCd = isuCd;
	}
	public String getIsuDesc() {
		return isuDesc;
	}
	public void setIsuDesc(String isuDesc) {
		this.isuDesc = isuDesc;
	}
	public String getIndGrpCd() {
		return indGrpCd;
	}
	public void setIndGrpCd(String indGrpCd) {
		this.indGrpCd = indGrpCd;
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