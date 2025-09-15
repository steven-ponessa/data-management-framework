package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborTypeDim",baseTableName="REFT.LABOR_TYPE")
public class LaborTypeDim  {
	@DbColumn(columnName="LABOR_TYPE_ID",isId=true)
	private int        laborTypeId;
	@DbColumn(columnName="LABOR_TYPE_CD",keySeq=1)
	private String     laborTypeCd;
	@DbColumn(columnName="LABOR_TYPE_DESC")
	private String     laborTypeDesc;
	@DbColumn(columnName="RECOVERY_MINOR_CD")
	private String     recoveryMinorCd;
	@DbColumn(columnName="COST_MINOR_CD")
	private String     costMinorCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public LaborTypeDim () {
		
	}
	
	// Define natural key constructor
	public LaborTypeDim (
      String     laborTypeCd
	) {
		this.laborTypeCd                    = laborTypeCd;
		
	}
	
	// Define base constructor
	public LaborTypeDim (
      String     laborTypeCd
    , String     laborTypeDesc
    , String     recoveryMinorCd
    , String     costMinorCd
	) {
		this.laborTypeCd                    = laborTypeCd;
		this.laborTypeDesc                  = laborTypeDesc;
		this.recoveryMinorCd                = recoveryMinorCd;
		this.costMinorCd                    = costMinorCd;
		
	}
    
	// Define full constructor
	public LaborTypeDim (
		  int        laborTypeId
		, String     laborTypeCd
		, String     laborTypeDesc
		, String     recoveryMinorCd
		, String     costMinorCd
	) {
		this.laborTypeId                    = laborTypeId;
		this.laborTypeCd                    = laborTypeCd;
		this.laborTypeDesc                  = laborTypeDesc;
		this.recoveryMinorCd                = recoveryMinorCd;
		this.costMinorCd                    = costMinorCd;
		
	}
	

	public String getCode() { 
		return this.laborTypeCd
		;
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborTypeDim other = (LaborTypeDim) obj;
		if (
            this.laborTypeCd.equals(other.getLaborTypeCd())
         && this.laborTypeDesc.equals(other.getLaborTypeDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.laborTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborTypeDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.recoveryMinorCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.costMinorCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LABOR_TYPE_CD")
        + "," + Helpers.formatCsvField("LABOR_TYPE_DESC")
        + "," + Helpers.formatCsvField("RECOVERY_MINOR_CD")
        + "," + Helpers.formatCsvField("COST_MINOR_CD")
		;
	}
    
	// Define Getters and Setters
	public int getLaborTypeId() {
		return laborTypeId;
	}
	public void setLaborTypeId(int laborTypeId) {
		this.laborTypeId = laborTypeId;
	}
	public String getLaborTypeCd() {
		return laborTypeCd;
	}
	public void setLaborTypeCd(String laborTypeCd) {
		this.laborTypeCd = laborTypeCd;
	}
	public String getLaborTypeDesc() {
		return laborTypeDesc;
	}
	public void setLaborTypeDesc(String laborTypeDesc) {
		this.laborTypeDesc = laborTypeDesc;
	}
	public String getRecoveryMinorCd() {
		return recoveryMinorCd;
	}
	public void setRecoveryMinorCd(String recoveryMinorCd) {
		this.recoveryMinorCd = recoveryMinorCd;
	}
	public String getCostMinorCd() {
		return costMinorCd;
	}
	public void setCostMinorCd(String costMinorCd) {
		this.costMinorCd = costMinorCd;
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