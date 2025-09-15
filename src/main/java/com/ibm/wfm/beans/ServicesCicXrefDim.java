package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="ServicesCicXrefDim",baseTableName="REFT.SERVICES_CIC_XREF")
public class ServicesCicXrefDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="CODE",keySeq=1)
	private String     code;
	@DbColumn(columnName="DESCRIPTION")
	private String     description;
	@DbColumn(columnName="FINANCIAL_COUNTRY_CODE")
	private String     financialCountryCode;
	@DbColumn(columnName="LABOR_MODEL_CODE")
	private String     laborModelCode;
	@DbColumn(columnName="CIC_CODE")
	private String     cicCode;
	@DbColumn(columnName="CIC_CENTER_NAME")
	private String     cicCenterName;
	@DbColumn(columnName="CIC_CENTER_GROUP",foreignKeySeq=1)
	private String     cicCenterGroup;
	@DbColumn(columnName="ISO_COUNTRY_CODE")
	private String     isoCountryCode;
	@DbColumn(columnName="LEGACY_COMPANY_CODE")
	private String     legacyCompanyCode;
	@DbColumn(columnName="DIVISION_CODE")
	private String     divisionCode;
	@DbColumn(columnName="SERVICE_LINE_CONDITION_CODE")
	private String     serviceLineConditionCode;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public ServicesCicXrefDim () {
		
	}
	
	// Define natural key constructor
	public ServicesCicXrefDim (
      String     code
	) {
		this.code                           = code;
		
	}
	
	// Define base constructor
	public ServicesCicXrefDim (
      String     code
    , String     description
    , String     financialCountryCode
    , String     laborModelCode
    , String     cicCode
    , String     cicCenterName
    , String     cicCenterGroup
	) {
		this.code                           = code;
		this.description                    = description;
		this.financialCountryCode           = financialCountryCode;
		this.laborModelCode                 = laborModelCode;
		this.cicCode                        = cicCode;
		this.cicCenterName                  = cicCenterName;
		this.cicCenterGroup                 = cicCenterGroup;
		
	}
    
	// Define full constructor
	public ServicesCicXrefDim (
		  int        id
		, String     code
		, String     description
		, String     financialCountryCode
		, String     laborModelCode
		, String     cicCode
		, String     cicCenterName
		, String     cicCenterGroup
		, String     isoCountryCode
		, String     legacyCompanyCode
		, String     divisionCode
		, String     serviceLineConditionCode
	) {
		this.id                             = id;
		this.code                           = code;
		this.description                    = description;
		this.financialCountryCode           = financialCountryCode;
		this.laborModelCode                 = laborModelCode;
		this.cicCode                        = cicCode;
		this.cicCenterName                  = cicCenterName;
		this.cicCenterGroup                 = cicCenterGroup;
		this.isoCountryCode                 = isoCountryCode;
		this.legacyCompanyCode              = legacyCompanyCode;
		this.divisionCode                   = divisionCode;
		this.serviceLineConditionCode       = serviceLineConditionCode;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		ServicesCicXrefDim other = (ServicesCicXrefDim) obj;
		if (
            this.code.equals(other.getCode())
         && this.description.equals(other.getDescription())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.code))
        + "," + Helpers.formatCsvField(String.valueOf(this.description))
        + "," + Helpers.formatCsvField(String.valueOf(this.financialCountryCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborModelCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterName))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterGroup))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CODE")
        + "," + Helpers.formatCsvField("DESCRIPTION")
        + "," + Helpers.formatCsvField("FINANCIAL_COUNTRY_CODE")
        + "," + Helpers.formatCsvField("LABOR_MODEL_CODE")
        + "," + Helpers.formatCsvField("CIC_CODE")
        + "," + Helpers.formatCsvField("CIC_CENTER_NAME")
        + "," + Helpers.formatCsvField("CIC_CENTER_GROUP")
		;
	}
    
	// Define Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFinancialCountryCode() {
		return financialCountryCode;
	}
	public void setFinancialCountryCode(String financialCountryCode) {
		this.financialCountryCode = financialCountryCode;
	}
	public String getLaborModelCode() {
		return laborModelCode;
	}
	public void setLaborModelCode(String laborModelCode) {
		this.laborModelCode = laborModelCode;
	}
	public String getCicCode() {
		return cicCode;
	}
	public void setCicCode(String cicCode) {
		this.cicCode = cicCode;
	}
	public String getCicCenterName() {
		return cicCenterName;
	}
	public void setCicCenterName(String cicCenterName) {
		this.cicCenterName = cicCenterName;
	}
	public String getCicCenterGroup() {
		return cicCenterGroup;
	}
	public void setCicCenterGroup(String cicCenterGroup) {
		this.cicCenterGroup = cicCenterGroup;
	}
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	public String getLegacyCompanyCode() {
		return legacyCompanyCode;
	}
	public void setLegacyCompanyCode(String legacyCompanyCode) {
		this.legacyCompanyCode = legacyCompanyCode;
	}
	public String getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	public String getServiceLineConditionCode() {
		return serviceLineConditionCode;
	}
	public void setServiceLineConditionCode(String serviceLineConditionCode) {
		this.serviceLineConditionCode = serviceLineConditionCode;
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