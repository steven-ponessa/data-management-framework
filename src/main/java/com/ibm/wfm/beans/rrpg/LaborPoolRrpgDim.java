package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborPoolRrpgDim",baseTableName="RRPG.LABOR_POOL",parentBeanPackageName="com.ibm.wfm.beans.rrpg",parentBeanName="GeographyTypeRrpgDim",parentBaseTableName="RRPG.GEOGRAPHY_TYPE")
public class LaborPoolRrpgDim extends NaryTreeNode implements Comparable<LaborPoolRrpgDim> {
	@DbColumn(columnName="LABOR_POOL_ID",isId=true)
	private int        laborPoolId;
	@DbColumn(columnName="LABOR_POOL_CD",keySeq=1)
	private String     laborPoolCd;
	@DbColumn(columnName="LABOR_POOL_NM")
	private String     laborPoolNm;
	@DbColumn(columnName="LABOR_POOL_DESC")
	private String     laborPoolDesc;
	@DbColumn(columnName="GEO_TYP_CD",foreignKeySeq=1)
	private String     geoTypCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public LaborPoolRrpgDim () {
		
	}
	
	// Define natural key constructor
	public LaborPoolRrpgDim (
      String     laborPoolCd
	) {
		this.laborPoolCd                    = laborPoolCd;
		
	}
	
	// Define base constructor
	public LaborPoolRrpgDim (
      String     laborPoolCd
    , String     laborPoolNm
    , String     laborPoolDesc
    , String     geoTypCd
	) {
		this.laborPoolCd                    = laborPoolCd;
		this.laborPoolNm                    = laborPoolNm;
		this.laborPoolDesc                  = laborPoolDesc;
		this.geoTypCd                       = geoTypCd;
		
	}
    
	// Define full constructor
	public LaborPoolRrpgDim (
		  int        laborPoolId
		, String     laborPoolCd
		, String     laborPoolNm
		, String     laborPoolDesc
		, String     geoTypCd
	) {
		this.laborPoolId                    = laborPoolId;
		this.laborPoolCd                    = laborPoolCd;
		this.laborPoolNm                    = laborPoolNm;
		this.laborPoolDesc                  = laborPoolDesc;
		this.geoTypCd                       = geoTypCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.laborPoolCd
		;
	}
	public String getDescription() { 
		return this.laborPoolNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborPoolRrpgDim other = (LaborPoolRrpgDim) obj;
		if (
            this.laborPoolCd.equals(other.getLaborPoolCd())
         && this.laborPoolNm.equals(other.getLaborPoolNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.laborPoolCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborPoolNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborPoolDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LABOR_POOL_CD")
        + "," + Helpers.formatCsvField("LABOR_POOL_NM")
        + "," + Helpers.formatCsvField("LABOR_POOL_DESC")
        + "," + Helpers.formatCsvField("GEO_TYP_CD")
		;
	}
    
	// Define Getters and Setters
	public int getLaborPoolId() {
		return laborPoolId;
	}
	public void setLaborPoolId(int laborPoolId) {
		this.laborPoolId = laborPoolId;
	}
	public String getLaborPoolCd() {
		return laborPoolCd;
	}
	public void setLaborPoolCd(String laborPoolCd) {
		this.laborPoolCd = laborPoolCd;
	}
	public String getLaborPoolNm() {
		return laborPoolNm;
	}
	public void setLaborPoolNm(String laborPoolNm) {
		this.laborPoolNm = laborPoolNm;
	}
	public String getLaborPoolDesc() {
		return laborPoolDesc;
	}
	public void setLaborPoolDesc(String laborPoolDesc) {
		this.laborPoolDesc = laborPoolDesc;
	}
	public String getGeoTypCd() {
		return geoTypCd;
	}
	public void setGeoTypCd(String geoTypCd) {
		this.geoTypCd = geoTypCd;
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
	public int compareTo(LaborPoolRrpgDim o) {
		return this.getLaborPoolCd().compareTo(o.getLaborPoolCd());
	}
}