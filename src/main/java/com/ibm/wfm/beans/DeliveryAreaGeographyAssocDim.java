package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryAreaGeographyAssocDim",baseTableName="REFT.DELIVERY_AREA_GEOGRAPHY_ASSOC",parentBeanName="DeliveryAreaDim",parentBaseTableName="REFT.DELIVERY_AREA")
public class DeliveryAreaGeographyAssocDim extends NaryTreeNode {
	@DbColumn(columnName="DLVRY_AREA_GEO_ID",isId=true)
	private int        dlvryAreaGeoId;
	@DbColumn(columnName="DLVRY_AREA_GEO_CD",keySeq=1)
	private String     dlvryAreaGeoCd;
	@DbColumn(columnName="DLVRY_AREA_GEO_NM")
	private String     dlvryAreaGeoNm;
	@DbColumn(columnName="DLVRY_AREA_CD",foreignKeySeq=1)
	private String     dlvryAreaCd;
	@DbColumn(columnName="GEO_CD")
	private String     geoCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public DeliveryAreaGeographyAssocDim () {
				this.level = 1;
	}
	
	// Define natural key constructor
	public DeliveryAreaGeographyAssocDim (
      String     dlvryAreaGeoCd
	) {
		this.dlvryAreaGeoCd                 = dlvryAreaGeoCd;
		this.level                          = 1;
	}
	
	// Define base constructor
	public DeliveryAreaGeographyAssocDim (
      String     dlvryAreaGeoCd
    , String     dlvryAreaGeoNm
    , String     dlvryAreaCd
    , String     geoCd
	) {
		this.dlvryAreaGeoCd                 = dlvryAreaGeoCd;
		this.dlvryAreaGeoNm                 = dlvryAreaGeoNm;
		this.dlvryAreaCd                    = dlvryAreaCd;
		this.geoCd                          = geoCd;
		this.level                          = 1;
	}
    
	// Define full constructor
	public DeliveryAreaGeographyAssocDim (
		  int        dlvryAreaGeoId
		, String     dlvryAreaGeoCd
		, String     dlvryAreaGeoNm
		, String     dlvryAreaCd
		, String     geoCd
	) {
		this.dlvryAreaGeoId                 = dlvryAreaGeoId;
		this.dlvryAreaGeoCd                 = dlvryAreaGeoCd;
		this.dlvryAreaGeoNm                 = dlvryAreaGeoNm;
		this.dlvryAreaCd                    = dlvryAreaCd;
		this.geoCd                          = geoCd;
		this.level                          = 1;
	}
	
	@Override
	public String getCode() { 
		return this.dlvryAreaGeoCd
		;
	}
	public String getDescription() { 
		return dlvryAreaGeoNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryAreaGeographyAssocDim other = (DeliveryAreaGeographyAssocDim) obj;
		if (
            this.dlvryAreaGeoCd.equals(other.getDlvryAreaGeoCd())
         && this.dlvryAreaGeoNm.equals(other.getDlvryAreaGeoNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.dlvryAreaGeoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaGeoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("DLVRY_AREA_GEO_CD")
        + "," + Helpers.formatCsvField("DLVRY_AREA_GEO_NM")
        + "," + Helpers.formatCsvField("DLVRY_AREA_CD")
        + "," + Helpers.formatCsvField("GEO_CD")
		;
	}
    
	// Define Getters and Setters
	public int getDlvryAreaGeoId() {
		return dlvryAreaGeoId;
	}
	public void setDlvryAreaGeoId(int dlvryAreaGeoId) {
		this.dlvryAreaGeoId = dlvryAreaGeoId;
	}
	public String getDlvryAreaGeoCd() {
		return dlvryAreaGeoCd;
	}
	public void setDlvryAreaGeoCd(String dlvryAreaGeoCd) {
		this.dlvryAreaGeoCd = dlvryAreaGeoCd;
	}
	public String getDlvryAreaGeoNm() {
		return dlvryAreaGeoNm;
	}
	public void setDlvryAreaGeoNm(String dlvryAreaGeoNm) {
		this.dlvryAreaGeoNm = dlvryAreaGeoNm;
	}
	public String getDlvryAreaCd() {
		return dlvryAreaCd;
	}
	public void setDlvryAreaCd(String dlvryAreaCd) {
		this.dlvryAreaCd = dlvryAreaCd;
	}
	public String getGeoCd() {
		return geoCd;
	}
	public void setGeoCd(String geoCd) {
		this.geoCd = geoCd;
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