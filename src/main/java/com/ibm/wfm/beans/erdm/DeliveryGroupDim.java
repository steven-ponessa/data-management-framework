package com.ibm.wfm.beans.erdm;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryGroupDim",baseTableName="ERDM.DELIVERY_GROUP",parentBeanPackageName="com.ibm.wfm.beans.erdm",parentBeanName="GlobalDim",parentBaseTableName="ERDM.GLOBAL")
public class DeliveryGroupDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="LEVELNUM")
	private int        levelnum;
	@DbColumn(columnName="DELIVERY_GROUP_CD",keySeq=1)
	private String     deliveryGroupCd;
	@DbColumn(columnName="GLOBAL_CD",foreignKeySeq=1)
	private String     globalCd;
	@DbColumn(columnName="DELIVERY_GROUP_DESC")
	private String     deliveryGroupDesc;
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
	public DeliveryGroupDim () {
		
	}
	
	// Define natural key constructor
	public DeliveryGroupDim (
      String     deliveryGroupCd
	) {
		this.deliveryGroupCd                = deliveryGroupCd;
		
	}
	
	// Define base constructor
	public DeliveryGroupDim (
      int        id
    , int        levelnum
    , String     deliveryGroupCd
    , String     globalCd
    , String     deliveryGroupDesc
	) {
		this.id                             = id;
		this.levelnum                       = levelnum;
		this.deliveryGroupCd                = deliveryGroupCd;
		this.globalCd                       = globalCd;
		this.deliveryGroupDesc              = deliveryGroupDesc;
		
	}
    
	// Define full constructor
	public DeliveryGroupDim (
		  int        id
		, int        levelnum
		, String     deliveryGroupCd
		, String     globalCd
		, String     deliveryGroupDesc
		, String     mediumdescription
		, String     shortdescription
		, Long       orgMask
		, String     altParentcode
	) {
		this.id                             = id;
		this.levelnum                       = levelnum;
		this.deliveryGroupCd                = deliveryGroupCd;
		this.globalCd                       = globalCd;
		this.deliveryGroupDesc              = deliveryGroupDesc;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		
	}
	
	@Override
	public String getCode() { 
		return this.deliveryGroupCd
		;
	}
	public String getDescription() { 
		return this.getDeliveryGroupDesc(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryGroupDim other = (DeliveryGroupDim) obj;
		if (
            this.deliveryGroupCd.equals(other.getDeliveryGroupCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.id))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryGroupCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.globalCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryGroupDesc))
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
        + "," + Helpers.formatCsvField("DELIVERY_GROUP_CD")
        + "," + Helpers.formatCsvField("GLOBAL_CD")
        + "," + Helpers.formatCsvField("DELIVERY_GROUP_DESC")
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
	public String getDeliveryGroupCd() {
		return deliveryGroupCd;
	}
	public void setDeliveryGroupCd(String deliveryGroupCd) {
		this.deliveryGroupCd = deliveryGroupCd;
	}
	public String getGlobalCd() {
		return globalCd;
	}
	public void setGlobalCd(String globalCd) {
		this.globalCd = globalCd;
	}
	public String getDeliveryGroupDesc() {
		return deliveryGroupDesc;
	}
	public void setDeliveryGroupDesc(String deliveryGroupDesc) {
		this.deliveryGroupDesc = deliveryGroupDesc;
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