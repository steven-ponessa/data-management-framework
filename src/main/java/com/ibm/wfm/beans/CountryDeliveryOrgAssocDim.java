package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CountryDeliveryOrgAssocDim",baseTableName="REFT.COUNTRY_DELIVERY_ORG_ASSOC",parentBeanName="BmsCountryDim",parentBaseTableName="REFT.COUNTRY")
public class CountryDeliveryOrgAssocDim extends NaryTreeNode {
	@DbColumn(columnName="CTRY_DLVRY_ORG_ASSOC_ID",isId=true)
	private int        ctryDlvryOrgAssocId;
	@DbColumn(columnName="CTRY_CD",keySeq=1,foreignKeySeq=1,assocParentKey=1)
	private String     ctryCd;
	@DbColumn(columnName="DLVRY_ORG_CD",keySeq=2,assocChildKey=1)
	private String     dlvryOrgCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CountryDeliveryOrgAssocDim () {
		this.level=5;
	}
	
	// Define base constructor
	public CountryDeliveryOrgAssocDim (
      String     ctryCd
    , String     dlvryOrgCd
	) {
		this.ctryCd                         = ctryCd;
		this.dlvryOrgCd                     = dlvryOrgCd;
		this.level                          = 5;
	}
    
	// Define full constructor
	public CountryDeliveryOrgAssocDim (
		  int        ctryDlvryOrgAssocId
		, String     ctryCd
		, String     dlvryOrgCd
	) {
		this.ctryDlvryOrgAssocId            = ctryDlvryOrgAssocId;
		this.ctryCd                         = ctryCd;
		this.dlvryOrgCd                     = dlvryOrgCd;
		this.level                          = 5;
	}
	
	@Override
	public String getCode() { return this.ctryCd+this.dlvryOrgCd; }
	public String getDescription() { return this.ctryCd+this.dlvryOrgCd; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CountryDeliveryOrgAssocDim other = (CountryDeliveryOrgAssocDim) obj;
		if (
            this.ctryCd.equals(other.getCtryCd())
         && this.dlvryOrgCd.equals(other.getDlvryOrgCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ctryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryOrgCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CTRY_CD")
        + "," + Helpers.formatCsvField("DLVRY_ORG_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCtryDlvryOrgAssocId() {
		return ctryDlvryOrgAssocId;
	}
	public void setCtryDlvryOrgAssocId(int ctryDlvryOrgAssocId) {
		this.ctryDlvryOrgAssocId = ctryDlvryOrgAssocId;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getDlvryOrgCd() {
		return dlvryOrgCd;
	}
	public void setDlvryOrgCd(String dlvryOrgCd) {
		this.dlvryOrgCd = dlvryOrgCd;
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