package com.ibm.wfm.beans.sdss;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CountryDim",baseTableName="SDSS.COUNTRY",parentBeanPackageName="com.ibm.wfm.beans.sdss",parentBeanName="RegionDim",parentBaseTableName="SDSS.REGION")
public class CountryDim extends NaryTreeNode {
	@DbColumn(columnName="CTRY_ID",isId=true)
	private int        ctryId;
	@DbColumn(columnName="CTRY_CD",keySeq=1)
	private String     ctryCd;
	@DbColumn(columnName="CTRY_NM")
	private String     ctryNm;
	@DbColumn(columnName="ISO_CTRY_CD")
	private String     isoCtryCd;
	@DbColumn(columnName="ISO_FIN_CD")
	private String     isoFinCd;
	@DbColumn(columnName="RGN_CD",foreignKeySeq=1)
	private String     rgnCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CountryDim () {
				this.level = 4;
	}
	
	// Define natural key constructor
	public CountryDim (
      String     ctryCd
	) {
		this.ctryCd                         = ctryCd;
		this.level                          = 4;
	}
    
	// Define full constructor
	public CountryDim (
		  int        ctryId
		, String     ctryCd
		, String     ctryNm
		, String     isoCtryCd
		, String     isoFinCd
		, String     rgnCd
	) {
		this.ctryId                         = ctryId;
		this.ctryCd                         = ctryCd;
		this.ctryNm                         = ctryNm;
		this.isoCtryCd                      = isoCtryCd;
		this.isoFinCd                       = isoFinCd;
		this.rgnCd                          = rgnCd;
		this.level                          = 4;
	}
	
	@Override
	public String getCode() { 
		return this.ctryCd
		;
	}
	public String getDescription() { 
		return this.ctryNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CountryDim other = (CountryDim) obj;
		if (
            this.ctryCd.equals(other.getCtryCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ctryId))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.isoCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.isoFinCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CTRY_ID")
        + "," + Helpers.formatCsvField("CTRY_CD")
        + "," + Helpers.formatCsvField("CTRY_NM")
        + "," + Helpers.formatCsvField("ISO_CTRY_CD")
        + "," + Helpers.formatCsvField("ISO_FIN_CD")
        + "," + Helpers.formatCsvField("RGN_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCtryId() {
		return ctryId;
	}
	public void setCtryId(int ctryId) {
		this.ctryId = ctryId;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
	}
	public String getIsoCtryCd() {
		return isoCtryCd;
	}
	public void setIsoCtryCd(String isoCtryCd) {
		this.isoCtryCd = isoCtryCd;
	}
	public String getIsoFinCd() {
		return isoFinCd;
	}
	public void setIsoFinCd(String isoFinCd) {
		this.isoFinCd = isoFinCd;
	}
	public String getRgnCd() {
		return rgnCd;
	}
	public void setRgnCd(String rgnCd) {
		this.rgnCd = rgnCd;
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