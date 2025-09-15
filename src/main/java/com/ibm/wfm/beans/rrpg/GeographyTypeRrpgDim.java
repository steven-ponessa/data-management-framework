package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyTypeRrpgDim",baseTableName="RRPG.GEOGRAPHY_TYPE")
public class GeographyTypeRrpgDim extends NaryTreeNode {
	@DbColumn(columnName="GEO_TYP_ID",isId=true)
	private int        geoTypId;
	@DbColumn(columnName="GEO_TYP_CD",keySeq=1)
	private String     geoTypCd;
	@DbColumn(columnName="GEO_TYP_NM")
	private String     geoTypNm;
	@DbColumn(columnName="GEO_TYP_DESC")
	private String     geoTypDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GeographyTypeRrpgDim () {
		
	}
	
	// Define natural key constructor
	public GeographyTypeRrpgDim (
      String     geoTypCd
	) {
		this.geoTypCd                       = geoTypCd;
		
	}
	
	// Define base constructor
	public GeographyTypeRrpgDim (
      String     geoTypCd
    , String     geoTypNm
    , String     geoTypDesc
	) {
		this.geoTypCd                       = geoTypCd;
		this.geoTypNm                       = geoTypNm;
		this.geoTypDesc                     = geoTypDesc;
		
	}
    
	// Define full constructor
	public GeographyTypeRrpgDim (
		  int        geoTypId
		, String     geoTypCd
		, String     geoTypNm
		, String     geoTypDesc
	) {
		this.geoTypId                       = geoTypId;
		this.geoTypCd                       = geoTypCd;
		this.geoTypNm                       = geoTypNm;
		this.geoTypDesc                     = geoTypDesc;
		
	}
	
	@Override
	public String getCode() { 
		return this.geoTypCd
		;
	}
	public String getDescription() { 
		return this.geoTypNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GeographyTypeRrpgDim other = (GeographyTypeRrpgDim) obj;
		if (
            this.geoTypCd.equals(other.getGeoTypCd())
         && this.geoTypNm.equals(other.getGeoTypNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_TYP_CD")
        + "," + Helpers.formatCsvField("GEO_TYP_NM")
        + "," + Helpers.formatCsvField("GEO_TYP_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getGeoTypId() {
		return geoTypId;
	}
	public void setGeoTypId(int geoTypId) {
		this.geoTypId = geoTypId;
	}
	public String getGeoTypCd() {
		return geoTypCd;
	}
	public void setGeoTypCd(String geoTypCd) {
		this.geoTypCd = geoTypCd;
	}
	public String getGeoTypNm() {
		return geoTypNm;
	}
	public void setGeoTypNm(String geoTypNm) {
		this.geoTypNm = geoTypNm;
	}
	public String getGeoTypDesc() {
		return geoTypDesc;
	}
	public void setGeoTypDesc(String geoTypDesc) {
		this.geoTypDesc = geoTypDesc;
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