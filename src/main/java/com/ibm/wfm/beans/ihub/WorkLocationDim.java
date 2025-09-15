package com.ibm.wfm.beans.ihub;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="WorkLocationDim",baseTableName="REFT.WORK_LOCATION")
public class WorkLocationDim extends NaryTreeNode {
	@DbColumn(columnName="WRK_LOC_KEY",isId=true)
	private int        wrkLocKey;
	@DbColumn(columnName="WRK_LOC_CD",keySeq=1)
	private String     wrkLocCd;
	@DbColumn(columnName="WRK_LOC_NM")
	private String     wrkLocNm;
	@DbColumn(columnName="ISO_CTRY_CD")
	private String     isoCtryCd;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;
	@DbColumn(columnName="CMPS_ID")
	private String     cmpsId;
	@DbColumn(columnName="CMPS_NM")
	private String     cmpsNm;
	@DbColumn(columnName="PHYSICAL_GEO_CD")
	private String     physicalGeoCd;
	@DbColumn(columnName="STATE_PROV_CD")
	private String     stateProvCd;
	@DbColumn(columnName="LATITUDE_DAT")
	private double     latitudeDat;
	@DbColumn(columnName="LONGITUDE_DAT")
	private double     longitudeDat;
	@DbColumn(columnName="TM_ZONE_DAT")
	private String     tmZoneDat;
	@DbColumn(columnName="ADDR_LINE_TXT")
	private String     addrLineTxt;
	@DbColumn(columnName="ADDR_LINE_2_TXT")
	private String     addrLine2Txt;
	@DbColumn(columnName="CNTY_CD")
	private String     cntyCd;
	@DbColumn(columnName="STD_GEO_CD")
	private String     stdGeoCd;
	@DbColumn(columnName="STD_GEO_DESC")
	private String     stdGeoDesc;
	@DbColumn(columnName="STD_REG_CD")
	private String     stdRegCd;
	@DbColumn(columnName="STD_REG_DESC")
	private String     stdRegDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public WorkLocationDim () {
		
	}
	
	// Define natural key constructor
	public WorkLocationDim (
      String     wrkLocCd
	) {
		this.wrkLocCd                       = wrkLocCd;
		
	}
	
	// Define base constructor
	public WorkLocationDim (
      String     wrkLocCd
    , String     wrkLocNm
    , String     isoCtryCd
    , String     actvFlg
    , String     cmpsId
    , String     cmpsNm
    , String     physicalGeoCd
    , String     stateProvCd
    , double     latitudeDat
    , double     longitudeDat
    , String     tmZoneDat
    , String     addrLineTxt
    , String     addrLine2Txt
    , String     cntyCd
    , String     stdGeoCd
    , String     stdGeoDesc
    , String     stdRegCd
    , String     stdRegDesc
	) {
		this.wrkLocCd                       = wrkLocCd;
		this.wrkLocNm                       = wrkLocNm;
		this.isoCtryCd                      = isoCtryCd;
		this.actvFlg                        = actvFlg;
		this.cmpsId                         = cmpsId;
		this.cmpsNm                         = cmpsNm;
		this.physicalGeoCd                  = physicalGeoCd;
		this.stateProvCd                    = stateProvCd;
		this.latitudeDat                    = latitudeDat;
		this.longitudeDat                   = longitudeDat;
		this.tmZoneDat                      = tmZoneDat;
		this.addrLineTxt                    = addrLineTxt;
		this.addrLine2Txt                   = addrLine2Txt;
		this.cntyCd                         = cntyCd;
		this.stdGeoCd                       = stdGeoCd;
		this.stdGeoDesc                     = stdGeoDesc;
		this.stdRegCd                       = stdRegCd;
		this.stdRegDesc                     = stdRegDesc;
		
	}
    
	// Define full constructor
	public WorkLocationDim (
		  int        wrkLocKey
		, String     wrkLocCd
		, String     wrkLocNm
		, String     isoCtryCd
		, String     actvFlg
		, String     cmpsId
		, String     cmpsNm
		, String     physicalGeoCd
		, String     stateProvCd
		, double     latitudeDat
		, double     longitudeDat
		, String     tmZoneDat
		, String     addrLineTxt
		, String     addrLine2Txt
		, String     cntyCd
		, String     stdGeoCd
		, String     stdGeoDesc
		, String     stdRegCd
		, String     stdRegDesc
	) {
		this.wrkLocKey                      = wrkLocKey;
		this.wrkLocCd                       = wrkLocCd;
		this.wrkLocNm                       = wrkLocNm;
		this.isoCtryCd                      = isoCtryCd;
		this.actvFlg                        = actvFlg;
		this.cmpsId                         = cmpsId;
		this.cmpsNm                         = cmpsNm;
		this.physicalGeoCd                  = physicalGeoCd;
		this.stateProvCd                    = stateProvCd;
		this.latitudeDat                    = latitudeDat;
		this.longitudeDat                   = longitudeDat;
		this.tmZoneDat                      = tmZoneDat;
		this.addrLineTxt                    = addrLineTxt;
		this.addrLine2Txt                   = addrLine2Txt;
		this.cntyCd                         = cntyCd;
		this.stdGeoCd                       = stdGeoCd;
		this.stdGeoDesc                     = stdGeoDesc;
		this.stdRegCd                       = stdRegCd;
		this.stdRegDesc                     = stdRegDesc;
		
	}
	
	@Override
	public String getCode() { 
		return this.wrkLocCd
		;
	}
	public String getDescription() { 
		return this.wrkLocNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		WorkLocationDim other = (WorkLocationDim) obj;
		if (
            this.wrkLocCd.equals(other.getWrkLocCd())
         && this.wrkLocNm.equals(other.getWrkLocNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.wrkLocCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkLocNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.isoCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpsId))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.physicalGeoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.stateProvCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.latitudeDat))
        + "," + Helpers.formatCsvField(String.valueOf(this.longitudeDat))
        + "," + Helpers.formatCsvField(String.valueOf(this.tmZoneDat))
        + "," + Helpers.formatCsvField(String.valueOf(this.addrLineTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.addrLine2Txt))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.stdGeoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.stdGeoDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.stdRegCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.stdRegDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("WRK_LOC_CD")
        + "," + Helpers.formatCsvField("WRK_LOC_NM")
        + "," + Helpers.formatCsvField("ISO_CTRY_CD")
        + "," + Helpers.formatCsvField("ACTV_FLG")
        + "," + Helpers.formatCsvField("CMPS_ID")
        + "," + Helpers.formatCsvField("CMPS_NM")
        + "," + Helpers.formatCsvField("PHYSICAL_GEO_CD")
        + "," + Helpers.formatCsvField("STATE_PROV_CD")
        + "," + Helpers.formatCsvField("LATITUDE_DAT")
        + "," + Helpers.formatCsvField("LONGITUDE_DAT")
        + "," + Helpers.formatCsvField("TM_ZONE_DAT")
        + "," + Helpers.formatCsvField("ADDR_LINE_TXT")
        + "," + Helpers.formatCsvField("ADDR_LINE_2_TXT")
        + "," + Helpers.formatCsvField("CNTY_CD")
        + "," + Helpers.formatCsvField("STD_GEO_CD")
        + "," + Helpers.formatCsvField("STD_GEO_DESC")
        + "," + Helpers.formatCsvField("STD_REG_CD")
        + "," + Helpers.formatCsvField("STD_REG_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getWrkLocKey() {
		return wrkLocKey;
	}
	public void setWrkLocKey(int wrkLocKey) {
		this.wrkLocKey = wrkLocKey;
	}
	public String getWrkLocCd() {
		return wrkLocCd;
	}
	public void setWrkLocCd(String wrkLocCd) {
		this.wrkLocCd = wrkLocCd;
	}
	public String getWrkLocNm() {
		return wrkLocNm;
	}
	public void setWrkLocNm(String wrkLocNm) {
		this.wrkLocNm = wrkLocNm;
	}
	public String getIsoCtryCd() {
		return isoCtryCd;
	}
	public void setIsoCtryCd(String isoCtryCd) {
		this.isoCtryCd = isoCtryCd;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
	}
	public String getCmpsId() {
		return cmpsId;
	}
	public void setCmpsId(String cmpsId) {
		this.cmpsId = cmpsId;
	}
	public String getCmpsNm() {
		return cmpsNm;
	}
	public void setCmpsNm(String cmpsNm) {
		this.cmpsNm = cmpsNm;
	}
	public String getPhysicalGeoCd() {
		return physicalGeoCd;
	}
	public void setPhysicalGeoCd(String physicalGeoCd) {
		this.physicalGeoCd = physicalGeoCd;
	}
	public String getStateProvCd() {
		return stateProvCd;
	}
	public void setStateProvCd(String stateProvCd) {
		this.stateProvCd = stateProvCd;
	}
	public double getLatitudeDat() {
		return latitudeDat;
	}
	public void setLatitudeDat(double latitudeDat) {
		this.latitudeDat = latitudeDat;
	}
	public double getLongitudeDat() {
		return longitudeDat;
	}
	public void setLongitudeDat(double longitudeDat) {
		this.longitudeDat = longitudeDat;
	}
	public String getTmZoneDat() {
		return tmZoneDat;
	}
	public void setTmZoneDat(String tmZoneDat) {
		this.tmZoneDat = tmZoneDat;
	}
	public String getAddrLineTxt() {
		return addrLineTxt;
	}
	public void setAddrLineTxt(String addrLineTxt) {
		this.addrLineTxt = addrLineTxt;
	}
	public String getAddrLine2Txt() {
		return addrLine2Txt;
	}
	public void setAddrLine2Txt(String addrLine2Txt) {
		this.addrLine2Txt = addrLine2Txt;
	}
	public String getCntyCd() {
		return cntyCd;
	}
	public void setCntyCd(String cntyCd) {
		this.cntyCd = cntyCd;
	}
	public String getStdGeoCd() {
		return stdGeoCd;
	}
	public void setStdGeoCd(String stdGeoCd) {
		this.stdGeoCd = stdGeoCd;
	}
	public String getStdGeoDesc() {
		return stdGeoDesc;
	}
	public void setStdGeoDesc(String stdGeoDesc) {
		this.stdGeoDesc = stdGeoDesc;
	}
	public String getStdRegCd() {
		return stdRegCd;
	}
	public void setStdRegCd(String stdRegCd) {
		this.stdRegCd = stdRegCd;
	}
	public String getStdRegDesc() {
		return stdRegDesc;
	}
	public void setStdRegDesc(String stdRegDesc) {
		this.stdRegDesc = stdRegDesc;
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