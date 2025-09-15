package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyRrpgDim",baseTableName="RRPG.GEOGRAPHY")
public class GeographyRrpgDim extends NaryTreeNode implements Comparable<GeographyRrpgDim> {
	@DbColumn(columnName="GEO_ID",isId=true)
	private int        geoId;
	@DbColumn(columnName="GEO_CD",keySeq=1)
	private String     geoCd;
	@DbColumn(columnName="GEO_NM")
	private String     geoNm;
	@DbColumn(columnName="GEO_DESC")
	private String     geoDesc;
	@DbColumn(columnName="EDS_GEO_CD")
	private String     edsGeoCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GeographyRrpgDim () {
		
	}
	
	// Define natural key constructor
	public GeographyRrpgDim (
      String     geoCd
	) {
		this.geoCd                          = geoCd;
		
	}
	
	// Define base constructor
	public GeographyRrpgDim (
      String     geoCd
    , String     geoNm
    , String     geoDesc
    , String     edsGeoCd
	) {
		this.geoCd                          = geoCd;
		this.geoNm                          = geoNm;
		this.geoDesc                        = geoDesc;
		this.edsGeoCd                       = edsGeoCd;
		
	}
    
	// Define full constructor
	public GeographyRrpgDim (
		  int        geoId
		, String     geoCd
		, String     geoNm
		, String     geoDesc
		, String     edsGeoCd
	) {
		this.geoId                          = geoId;
		this.geoCd                          = geoCd;
		this.geoNm                          = geoNm;
		this.geoDesc                        = geoDesc;
		this.edsGeoCd                       = edsGeoCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.geoCd
		;
	}
	public String getDescription() { 
		return this.geoDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GeographyRrpgDim other = (GeographyRrpgDim) obj;
		if (
            this.geoCd.equals(other.getGeoCd())
         && this.geoNm.equals(other.getGeoNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.edsGeoCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_CD")
        + "," + Helpers.formatCsvField("GEO_NM")
        + "," + Helpers.formatCsvField("GEO_DESC")
        + "," + Helpers.formatCsvField("EDS_GEO_CD")
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
	public String getGeoNm() {
		return geoNm;
	}
	public void setGeoNm(String geoNm) {
		this.geoNm = geoNm;
	}
	public String getGeoDesc() {
		return geoDesc;
	}
	public void setGeoDesc(String geoDesc) {
		this.geoDesc = geoDesc;
	}
	public String getEdsGeoCd() {
		return edsGeoCd;
	}
	public void setEdsGeoCd(String edsGeoCd) {
		this.edsGeoCd = edsGeoCd;
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
	public int compareTo(GeographyRrpgDim o) {
		return this.getGeoCd().compareTo(o.getGeoCd());
	}
}