package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborPoolDim",baseTableName="REFT.LABOR_POOL")
public class LaborPoolDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	@DbColumn(columnName="LABOR_POOL_ID",isId=true)
	private int        laborPoolId;
	@DbColumn(columnName="LABOR_POOL_CODE",keySeq=1)
	private String     laborPoolCode;
	@DbColumn(columnName="LABOR_POOL_NM")
	private String     laborPoolNm;
	@DbColumn(columnName="PARENT_LABOR_POOL_CODE")
	private String     parentLaborPoolCode;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	//@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	@JsonIgnore
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	//@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	@JsonIgnore
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)   
	@JsonIgnore
	private String rowStatusCd;

	// Define null constructor
	public LaborPoolDim () {
		
	}
	
	// Define natural key constructor
	public LaborPoolDim (
      String     laborPoolCode
	) {
		this.laborPoolCode                  = laborPoolCode;
		
	}
	
	// Define base constructor
	public LaborPoolDim (
      String     laborPoolCode
    , String     laborPoolNm
    , String     parentLaborPoolCode
	) {
		this.laborPoolCode                  = laborPoolCode;
		this.laborPoolNm                    = laborPoolNm;
		this.parentLaborPoolCode            = parentLaborPoolCode;
		
	}
    
	// Define full constructor
	public LaborPoolDim (
		  int        laborPoolId
		, String     laborPoolCode
		, String     laborPoolNm
		, String     parentLaborPoolCode
	) {
		this.laborPoolId                    = laborPoolId;
		this.laborPoolCode                  = laborPoolCode;
		this.laborPoolNm                    = laborPoolNm;
		this.parentLaborPoolCode            = parentLaborPoolCode;
		
	}
	
	public String getCode() { 
		return this.laborPoolCode
		;
	}
	public void setCode(String code) { 
		this.laborPoolCode = code;
	}
	public String getDescription() { 
		return this.laborPoolNm ; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborPoolDim other = (LaborPoolDim) obj;
		if (
            this.laborPoolCode.equals(other.getLaborPoolCode())
         && this.laborPoolNm.equals(other.getLaborPoolNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.laborPoolCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborPoolNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentLaborPoolCode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LABOR_POOL_CODE")
        + "," + Helpers.formatCsvField("LABOR_POOL_NM")
        + "," + Helpers.formatCsvField("PARENT_LABOR_POOL_CODE")
		;
	}
    
	// Define Getters and Setters
	public int getLaborPoolId() {
		return laborPoolId;
	}
	public void setLaborPoolId(int laborPoolId) {
		this.laborPoolId = laborPoolId;
	}
	public String getLaborPoolCode() {
		return laborPoolCode;
	}
	public void setLaborPoolCode(String laborPoolCode) {
		this.laborPoolCode = laborPoolCode;
	}
	public String getLaborPoolNm() {
		return laborPoolNm;
	}
	public void setLaborPoolNm(String laborPoolNm) {
		this.laborPoolNm = laborPoolNm;
	}
	public String getParentLaborPoolCode() {
		return parentLaborPoolCode;
	}
	public void setParentLaborPoolCode(String parentLaborPoolCode) {
		this.parentLaborPoolCode = parentLaborPoolCode;
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

	@Override
	public String getParentCd() {
		return this.parentLaborPoolCode;
	}

	public void setParentCd(String parentCd) {
		this.parentLaborPoolCode = parentCd;		
	}

	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}

	@Override
	public String getName() {
		return this.laborPoolNm;
	}

	@Override
	public void setName(String name) {
		this.laborPoolNm = name;
	}
}