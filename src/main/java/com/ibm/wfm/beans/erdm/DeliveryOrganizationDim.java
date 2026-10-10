package com.ibm.wfm.beans.erdm;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryOrganizationDim",baseTableName="ERDM.DELIVERY_ORGANIZATION",parentBeanPackageName="com.ibm.wfm.beans.erdm",parentBeanName="DeliveryGroupDim",parentBaseTableName="ERDM.DELIVERY_GROUP")
public class DeliveryOrganizationDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="LEVELNUM")
	private int        levelnum;
	@DbColumn(columnName="DELIVERY_ORG_CD",keySeq=1)
	private String     deliveryOrgCd;
	@DbColumn(columnName="DELIVERY_GROUP_CD",foreignKeySeq=1)
	private String     deliveryGroupCd;
	@DbColumn(columnName="DELIVERY_ORG_DESC")
	private String     deliveryOrgDesc;
	@DbColumn(columnName="MEDIUMDESCRIPTION")
	private String     mediumdescription;
	@DbColumn(columnName="SHORTDESCRIPTION")
	private String     shortdescription;
	@DbColumn(columnName="ORG_MASK")
	private Long       orgMask;
	@DbColumn(columnName="ALT_PARENTCODE")
	private String     altParentcode;
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
		
	}
	
	// Define natural key constructor
	public DeliveryOrganizationDim (
      String     deliveryOrgCd
	) {
		this.deliveryOrgCd                  = deliveryOrgCd;
		
	}
	
	// Define base constructor
	public DeliveryOrganizationDim (
      int        id
    , int        levelnum
    , String     deliveryOrgCd
    , String     deliveryGroupCd
    , String     deliveryOrgDesc

	) {
		this.id                             = id;
		this.levelnum                       = levelnum;
		this.deliveryOrgCd                  = deliveryOrgCd;
		this.deliveryGroupCd                = deliveryGroupCd;
		this.deliveryOrgDesc                = deliveryOrgDesc;
	}
    
	// Define full constructor
	public DeliveryOrganizationDim (
		  int        id
		, int        levelnum
		, String     deliveryOrgCd
		, String     deliveryGroupCd
		, String     deliveryOrgDesc
		, String     mediumdescription
		, String     shortdescription
		, Long       orgMask
		, String     altParentcode
	) {
		this.id                             = id;
		this.levelnum                       = levelnum;
		this.deliveryOrgCd                  = deliveryOrgCd;
		this.deliveryGroupCd                = deliveryGroupCd;
		this.deliveryOrgDesc                = deliveryOrgDesc;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		
	}
	
	@Override
	public String getCode() { 
		return this.deliveryOrgCd
		;
	}
	public String getDescription() { 
		return this.getDeliveryOrgDesc(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryOrganizationDim other = (DeliveryOrganizationDim) obj;
		if (
			this.deliveryOrgCd.equals(other.getDeliveryOrgCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.id))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryOrgCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryGroupCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryOrgDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMask))
        + "," + Helpers.formatCsvField(String.valueOf(this.altParentcode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ID")
        + "," + Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("DELIVERY_ORG_CD")
        + "," + Helpers.formatCsvField("DELIVERY_GROUP_CD")
        + "," + Helpers.formatCsvField("DELIVERY_ORG_DESC")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("ORG_MASK")
        + "," + Helpers.formatCsvField("ALT_PARENTCODE")
		;
	}
    
	// Define Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevelnum() {
		return levelnum;
	}
	public void setLevelnum(int levelnum) {
		this.levelnum = levelnum;
	}
	public String getDeliveryOrgCd() {
		return deliveryOrgCd;
	}
	public void setDeliveryOrgCd(String deliveryOrgCd) {
		this.deliveryOrgCd = deliveryOrgCd;
	}
	public String getDeliveryGroupCd() {
		return deliveryGroupCd;
	}
	public void setDeliveryGroupCd(String deliveryGroupCd) {
		this.deliveryGroupCd = deliveryGroupCd;
	}
	public String getDeliveryOrgDesc() {
		return deliveryOrgDesc;
	}
	public void setDeliveryOrgDesc(String deliveryOrgDesc) {
		this.deliveryOrgDesc = deliveryOrgDesc;
	}
	public String getMediumdescription() {
		return mediumdescription;
	}
	public void setMediumdescription(String mediumdescription) {
		this.mediumdescription = mediumdescription;
	}
	public String getShortdescription() {
		return shortdescription;
	}
	public void setShortdescription(String shortdescription) {
		this.shortdescription = shortdescription;
	}
	public Long getOrgMask() {
		return orgMask;
	}
	public void setOrgMask(Long orgMask) {
		this.orgMask = orgMask;
	}
	public String getAltParentcode() {
		return altParentcode;
	}
	public void setAltParentcode(String altParentcode) {
		this.altParentcode = altParentcode;
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