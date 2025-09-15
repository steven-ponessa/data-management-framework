package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryOrganizationDim",baseTableName="REFT.DELIVERY_ORGANIZATION",parentBeanName="CountryDeliveryOrgAssocDim",parentBaseTableName="REFT.COUNTRY_DELIVERY_ORG_ASSOC")
public class DeliveryOrganizationDim extends NaryTreeNode {
	@DbColumn(columnName="DLVRY_ORG_ID",isId=true)
	private int        dlvryOrgId;
	@DbColumn(columnName="DLVRY_ORG_CD",keySeq=1,foreignKeySeq=1)
	private String     dlvryOrgCd;
	@DbColumn(columnName="DLVRY_ORG_NM")
	private String     dlvryOrgNm;
	@DbColumn(columnName="DLVRY_ORG_DESC")
	private String     dlvryOrgDesc;
	@DbColumn(columnName="TYP_CD")
	private String     typCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public DeliveryOrganizationDim () {
		this.level=6;
	}
	
	// Define base constructor
	public DeliveryOrganizationDim (
      String     dlvryOrgCd
	) {
		this.dlvryOrgCd                     = dlvryOrgCd;
		this.level                          = 6;
	}
	
	// Define base constructor
	public DeliveryOrganizationDim (
      String     dlvryOrgCd
    , String     dlvryOrgNm
    , String     dlvryOrgDesc
    , String     typCd
	) {
		this.dlvryOrgCd                     = dlvryOrgCd;
		this.dlvryOrgNm                     = dlvryOrgNm;
		this.dlvryOrgDesc                   = dlvryOrgDesc;
		this.typCd                          = typCd;
		this.level                          = 6;
	}
    
	// Define full constructor
	public DeliveryOrganizationDim (
		  int        dlvryOrgId
		, String     dlvryOrgCd
		, String     dlvryOrgNm
		, String     dlvryOrgDesc
		, String     typCd
	) {
		this.dlvryOrgId                     = dlvryOrgId;
		this.dlvryOrgCd                     = dlvryOrgCd;
		this.dlvryOrgNm                     = dlvryOrgNm;
		this.dlvryOrgDesc                   = dlvryOrgDesc;
		this.typCd                          = typCd;
		this.level                          = 6;
	}
	
	@Override
	public String getCode() { return this.dlvryOrgCd; }
	public String getDescription() { return this.dlvryOrgNm; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryOrganizationDim other = (DeliveryOrganizationDim) obj;
		if (
            this.dlvryOrgCd.equals(other.getDlvryOrgCd())
         && this.dlvryOrgNm.equals(other.getDlvryOrgNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.dlvryOrgCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryOrgNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryOrgDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.typCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("DLVRY_ORG_CD")
        + "," + Helpers.formatCsvField("DLVRY_ORG_NM")
        + "," + Helpers.formatCsvField("DLVRY_ORG_DESC")
        + "," + Helpers.formatCsvField("TYP_CD")
		;
	}
    
	// Define Getters and Setters
	public int getDlvryOrgId() {
		return dlvryOrgId;
	}
	public void setDlvryOrgId(int dlvryOrgId) {
		this.dlvryOrgId = dlvryOrgId;
	}
	public String getDlvryOrgCd() {
		return dlvryOrgCd;
	}
	public void setDlvryOrgCd(String dlvryOrgCd) {
		this.dlvryOrgCd = dlvryOrgCd;
	}
	public String getDlvryOrgNm() {
		return dlvryOrgNm;
	}
	public void setDlvryOrgNm(String dlvryOrgNm) {
		this.dlvryOrgNm = dlvryOrgNm;
	}
	public String getDlvryOrgDesc() {
		return dlvryOrgDesc;
	}
	public void setDlvryOrgDesc(String dlvryOrgDesc) {
		this.dlvryOrgDesc = dlvryOrgDesc;
	}
	public String getTypCd() {
		return typCd;
	}
	public void setTypCd(String typCd) {
		this.typCd = typCd;
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