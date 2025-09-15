package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyDim",baseTableName="REFT.GEOGRAPHY")
public class BmsGeographyDim extends NaryTreeNode {
	@DbColumn(columnName="GEO_ID",isId=true)
	private int        geoId;
	@DbColumn(columnName="GEO_CD",keySeq=1)
	private String     geoCd;
	@DbColumn(columnName="GEO_DESC")
	private String     geoDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BmsGeographyDim () {
		this.level=0;
	}
	
	// Define base constructor
	public BmsGeographyDim (
      String     geoCd
	) {
		this.geoCd                          = geoCd;
		this.level                          = 0;
	}
	
	// Define base constructor
	public BmsGeographyDim (
      String     geoCd
    , String     geoDesc
	) {
		this.geoCd                          = geoCd;
		this.geoDesc                        = geoDesc;
		this.level                          = 0;
	}
    
	// Define full constructor
	public BmsGeographyDim (
		  int        geoId
		, String     geoCd
		, String     geoDesc
	) {
		this.geoId                          = geoId;
		this.geoCd                          = geoCd;
		this.geoDesc                        = geoDesc;
		this.level                          = 0;
	}
	
	@Override
	public String getCode() { return this.geoCd; }
	public String getDescription() { return this.geoDesc; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BmsGeographyDim other = (BmsGeographyDim) obj;
		if (
            this.geoCd.equals(other.getGeoCd())
         && this.geoDesc.equals(other.getGeoDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_CD")
        + "," + Helpers.formatCsvField("GEO_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getGeoId() {
		return geoId;
	}
	public void setGeoId(int geoId) {
		this.geoId = geoId;
	}
	public String getGeoCd() {
		return geoCd;
	}
	public void setGeoCd(String geoCd) {
		this.geoCd = geoCd;
	}
	public String getGeoDesc() {
		return geoDesc;
	}
	public void setGeoDesc(String geoDesc) {
		this.geoDesc = geoDesc;
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