package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyDim",baseTableName="SDSS.GEOGRAPHY",parentBeanPackageName="com.ibm.wfm.beans.sdss",parentBeanName="GeographyTypeDim",parentBaseTableName="SDSS.GEOGRAPHY_TYPE")
public class GeographyDim extends NaryTreeNode {
	@DbColumn(columnName="GEO_ID",isId=true)
	private int        geoId;
	@DbColumn(columnName="GEO_CD",keySeq=1)
	private String     geoCd;
	@DbColumn(columnName="GEO_NM")
	private String     geoNm;
	@DbColumn(columnName="LGCY_GEO_CD")
	private String     lgcyGeoCd;
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
	public GeographyDim () {
				this.level = 1;
	}
	
	// Define natural key constructor
	public GeographyDim (
      String     geoCd
	) {
		this.geoCd                          = geoCd;
		this.level                          = 1;
	}
    
	// Define full constructor
	public GeographyDim (
		  int        geoId
		, String     geoCd
		, String     geoNm
		, String     lgcyGeoCd
		, String     geoTypCd
	) {
		this.geoId                          = geoId;
		this.geoCd                          = geoCd;
		this.geoNm                          = geoNm;
		this.lgcyGeoCd                      = lgcyGeoCd;
		this.geoTypCd                       = geoTypCd;
		this.level                          = 1;
	}
	
	@Override
	public String getCode() { 
		return this.geoCd
		;
	}
	public String getDescription() { 
		return this.geoNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GeographyDim other = (GeographyDim) obj;
		if (
            this.geoCd.equals(other.getGeoCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoId))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lgcyGeoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_ID")
        + "," + Helpers.formatCsvField("GEO_CD")
        + "," + Helpers.formatCsvField("GEO_NM")
        + "," + Helpers.formatCsvField("LGCY_GEO_CD")
        + "," + Helpers.formatCsvField("GEO_TYP_CD")
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
	public String getLgcyGeoCd() {
		return lgcyGeoCd;
	}
	public void setLgcyGeoCd(String lgcyGeoCd) {
		this.lgcyGeoCd = lgcyGeoCd;
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
}