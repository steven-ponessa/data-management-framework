package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyTypeDim",baseTableName="SDSS.GEOGRAPHY_TYPE")
public class GeographyTypeDim extends NaryTreeNode {
	@DbColumn(columnName="GEO_TYP_ID",isId=true)
	private int        geoTypId;
	@DbColumn(columnName="GEO_TYP_CD",keySeq=1)
	private String     geoTypCd;
	@DbColumn(columnName="GEO_TYP_NM")
	private String     geoTypNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GeographyTypeDim () {
				this.level = 0;
	}
	
	// Define natural key constructor
	public GeographyTypeDim (
      String     geoTypCd
	) {
		this.geoTypCd                       = geoTypCd;
		this.level                          = 0;
	}
    
	// Define full constructor
	public GeographyTypeDim (
		  int        geoTypId
		, String     geoTypCd
		, String     geoTypNm
	) {
		this.geoTypId                       = geoTypId;
		this.geoTypCd                       = geoTypCd;
		this.geoTypNm                       = geoTypNm;
		this.level                          = 0;
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
                    
		GeographyTypeDim other = (GeographyTypeDim) obj;
		if (
			this.geoTypCd.equals(other.getGeoTypCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoTypId))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_TYP_ID")
        + "," + Helpers.formatCsvField("GEO_TYP_CD")
        + "," + Helpers.formatCsvField("GEO_TYP_NM")
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