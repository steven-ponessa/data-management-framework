package com.ibm.wfm.beans.erdm;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryOrgMarketAssocDim",baseTableName="ERDM.DELIVERY_ORG_MARKET_ASSOC",parentBeanPackageName="com.ibm.wfm.beans.erdm",parentBeanName="DeliveryOrganizationDim",parentBaseTableName="ERDM.DELIVERY_ORGANIZATION",type=DbTable.Type.ASSOCIATION)
public class DeliveryOrgMarketAssocDim extends NaryTreeNode {
	@DbColumn(columnName="ID",isId=true)
	private int        id;
	@DbColumn(columnName="DELIVERY_ORG_CD",keySeq=1,foreignKeySeq=1,assocParentKey=1)
	@ExcelSheet(columnName="",columnNum=2)	
	private String     deliveryOrgCd;
	@DbColumn(columnName="MARKET_CD",keySeq=2,assocChildKey=1)
	private String     marketCd;
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
	public DeliveryOrgMarketAssocDim () {
		
	}
	
	// Define natural key constructor
	public DeliveryOrgMarketAssocDim (
      String     deliveryOrgCd
    , String     marketCd
	) {
		this.deliveryOrgCd                  = deliveryOrgCd;
		this.marketCd                       = marketCd;
		
	}
	
	// Define base constructor
	public DeliveryOrgMarketAssocDim (
      int        id
    , String     deliveryOrgCd
    , String     marketCd
	) {
		this.id                             = id;
		this.deliveryOrgCd                  = deliveryOrgCd;
		this.marketCd                       = marketCd;
	}
    
	// Define full constructor
	public DeliveryOrgMarketAssocDim (
		  int        id
		, String     deliveryOrgCd
		, String     marketCd
		, Long       orgMask
		, String     altParentcode
	) {
		this.id                             = id;
		this.deliveryOrgCd                  = deliveryOrgCd;
		this.marketCd                       = marketCd;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		
	}
	
	@Override
	public String getCode() { 
		return this.deliveryOrgCd
    +":"+ this.marketCd
		;
	}
	public String getDescription() { 
		return this.deliveryOrgCd
    +":"+ this.marketCd; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryOrgMarketAssocDim other = (DeliveryOrgMarketAssocDim) obj;
		if (
            this.deliveryOrgCd.equals(other.getDeliveryOrgCd())
         && this.marketCd.equals(other.getMarketCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.id))
        + "," + Helpers.formatCsvField(String.valueOf(this.deliveryOrgCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMask))
        + "," + Helpers.formatCsvField(String.valueOf(this.altParentcode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ID")
        + "," + Helpers.formatCsvField("DELIVERY_ORG_CD")
        + "," + Helpers.formatCsvField("MARKET_CD")
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
	public String getDeliveryOrgCd() {
		return deliveryOrgCd;
	}
	public void setDeliveryOrgCd(String deliveryOrgCd) {
		this.deliveryOrgCd = deliveryOrgCd;
	}
	public String getMarketCd() {
		return marketCd;
	}
	public void setMarketCd(String marketCd) {
		this.marketCd = marketCd;
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