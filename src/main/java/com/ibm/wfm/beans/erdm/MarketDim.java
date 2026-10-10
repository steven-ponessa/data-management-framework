package com.ibm.wfm.beans.erdm;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbRelationship;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.JoinType;
import com.ibm.wfm.annotations.RelationshipType;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

//@DbTable(beanName="MarketDim",baseTableName="ERDM.MARKET",parentBeanPackageName="com.ibm.wfm.beans.erdm",parentBeanName="DeliveryOrgMarketAssocDim",parentBaseTableName="ERDM.DELIVERY_ORG_MARKET_ASSOC")
@DbTable(beanName="MarketDim",baseTableName="ERDM.MARKET",parentBeanPackageName="com.ibm.wfm.beans.erdm",parentBeanName="GeographyDim",parentBaseTableName="ERDM.GEOGRAPHY")
@DbRelationship(
    name = "geography",
    relationshipType = RelationshipType.PARENT, 
    targetBeanPackageName = "com.ibm.wfm.beans.erdm",
    targetBeanName = "GeographyDim",
    targetTableName = "ERDM.GEOGRAPHY",
    joinType = JoinType.INNER,
    joinColumns = {
        "ERDM.MARKET.GEOGRAPHY_ID = ERDM.GEOGRAPHY.GEOGRAPHY_ID"
    },
    joinChildKey = "GEOGRAPHY_ID",
    joinParentKey = "GEOGRAPHY_ID",
    defaultPath = true
)
@DbRelationship(
    name = "deliveryOrgMarketAssoc",
    relationshipType = RelationshipType.ASSOCIATION,
    targetBeanPackageName = "com.ibm.wfm.beans.erdm",
    targetBeanName = "DeliveryOrgMarketAssocDim",
    targetTableName = "ERDM.DELIVERY_ORG_MARKET_ASSOC",
    joinType = JoinType.INNER,
    joinColumns = {
        "ERDM.MARKET.MARKET_ID = ERDM.DELIVERY_ORG_MARKET_ASSOC.MARKET_CD"
    },
    joinChildKey = "MARKET_CD",
    joinParentKey = "MARKET_CD"
)
public class MarketDim extends NaryTreeNode {
	@DbColumn(columnName="MARKET_CD",keySeq=1) 
	private String     marketCd;
	@DbColumn(columnName="GEOGRAPHY_CD",foreignKeySeq=1)
	private String     geographyCd;
	@DbColumn(columnName="LEVELNUM")
	private int        levelnum;
	@DbColumn(columnName="LONGDESCRIPTION")
	private String     longdescription;
	@DbColumn(columnName="MEDIUMDESCRIPTION")
	private String     mediumdescription;
	@DbColumn(columnName="SHORTDESCRIPTION")
	private String     shortdescription;
	@DbColumn(columnName="CODE_TYPE")
	private String     codeType;
	@DbColumn(columnName="COMPLIANCE_CD")
	private String     complianceCd;
	@DbColumn(columnName="USAGE_RULE")
	private String     usageRule;
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
	public MarketDim () {
		this.level=30;
	}
	
	// Define natural key constructor
	public MarketDim (
      String     marketCd
	) {
		this.marketCd                       = marketCd;
		this.level=30;
		
	}
    
	// Define full constructor
	public MarketDim (
		  String     marketCd
		, String     geographyCd
		, int        levelnum
		, String     longdescription
		, String     mediumdescription
		, String     shortdescription
		, String     codeType
		, String     complianceCd
		, String     usageRule
	    , Long       orgMask
	    , String     altParentcode			
	) {
		this.marketCd                       = marketCd;
		this.geographyCd                    = geographyCd;
		this.levelnum                       = levelnum;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;		
		this.usageRule                      = usageRule;
		this.orgMask                        = orgMask;
		this.altParentcode                  = altParentcode;
		this.level=30;
		
	}
	
	@Override
	public String getCode() { 
		return this.marketCd
		;
	}
	public String getDescription() { 
		return this.longdescription; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		MarketDim other = (MarketDim) obj;
		if (
            this.marketCd.equals(other.getMarketCd())
         && this.geographyCd.equals(other.getGeographyCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.marketCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geographyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.longdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.codeType))
        + "," + Helpers.formatCsvField(String.valueOf(this.complianceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRule))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMask))
        + "," + Helpers.formatCsvField(String.valueOf(this.altParentcode))        
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("MARKET_CD")
        + "," + Helpers.formatCsvField("GEOGRAPHY_CD")
        + "," + Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("LONGDESCRIPTION")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("CODE_TYPE")
        + "," + Helpers.formatCsvField("COMPLIANCE_CD")
        + "," + Helpers.formatCsvField("USAGE_RULE")
        + "," + Helpers.formatCsvField("ORG_MASK")
        + "," + Helpers.formatCsvField("ALT_PARENTCODE")        
		;
	}
    
	// Define Getters and Setters
	public String getMarketCd() {
		return marketCd;
	}
	public void setMarketCd(String marketCd) {
		this.marketCd = marketCd;
	}
	public String getGeographyCd() {
		return geographyCd;
	}
	public void setGeographyCd(String geographyCd) {
		this.geographyCd = geographyCd;
	}
	public int getLevelnum() {
		return levelnum;
	}
	public void setLevelnum(int levelnum) {
		this.levelnum = levelnum;
	}
	public String getLongdescription() {
		return longdescription;
	}
	public void setLongdescription(String longdescription) {
		this.longdescription = longdescription;
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
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getComplianceCd() {
		return complianceCd;
	}
	public void setComplianceCd(String complianceCd) {
		this.complianceCd = complianceCd;
	}
	public String getUsageRule() {
		return usageRule;
	}
	public void setUsageRule(String usageRule) {
		this.usageRule = usageRule;
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
}