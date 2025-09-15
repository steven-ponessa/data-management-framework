package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OfferingComponentDim",baseTableName="REFT.OFFERING_COMPONENT",parentBeanName="OfferingDim",parentBaseTableName="REFT.OFFERING")
public class OfferingComponentDim extends NaryTreeNode implements Comparable<OfferingComponentDim> {
	@DbColumn(columnName="OFFERING_COMPONENT_ID",isId=true)
	private int        offeringComponentId;
	@DbColumn(columnName="OFFERING_COMPONENT_CD",keySeq=1)
	private String     offeringComponentCd;
	@DbColumn(columnName="OFFERING_COMPONENT_NM")
	private String     offeringComponentNm;
	@DbColumn(columnName="UT_CD")
	private String     utCd;
	@DbColumn(columnName="UT_LEVEL10_CD")
	private String     utLevel10Cd;
	@DbColumn(columnName="UT_LEVEL10_DESC")
	private String     utLevel10Desc;
	@DbColumn(columnName="UT_LEVEL15_CD")
	private String     utLevel15Cd;
	@DbColumn(columnName="UT_LEVEL15_DESC")
	private String     utLevel15Desc;
	@DbColumn(columnName="UT_LEVEL17_CD")
	private String     utLevel17Cd;
	@DbColumn(columnName="UT_LEVEL17_DESC")
	private String     utLevel17Desc;
	@DbColumn(columnName="UT_LEVEL20_CD")
	private String     utLevel20Cd;
	@DbColumn(columnName="UT_LEVEL20_DESC")
	private String     utLevel20Desc;
	@DbColumn(columnName="UT_LEVEL30_CD")
	private String     utLevel30Cd;
	@DbColumn(columnName="UT_LEVEL30_DESC")
	private String     utLevel30Desc;
	@DbColumn(columnName="UT_LEVEL35_CD")
	private String     utLevel35Cd;
	@DbColumn(columnName="UT_LEVEL35_DESC")
	private String     utLevel35Desc;
	@DbColumn(columnName="GBS_OFFERING_ATTRIBUTE_NM")
	private String     gbsOfferingAttributeNm;
	@DbColumn(columnName="GBS_PRACTICE_CD")
	private String     gbsPracticeCd;
	@DbColumn(columnName="GBS_PRACTICE_DESC")
	private String     gbsPracticeDesc;
	@DbColumn(columnName="ACCOUNT_ASSIGNMENT_GROUP")
	private String     accountAssignmentGroup;
	@DbColumn(columnName="ANNOUNCEMENT_TYPE")
	private String     announcementType;
	@DbColumn(columnName="AVAILABILITY_STATUS")
	private String     availabilityStatus;
	@DbColumn(columnName="AVAILABILITY_TYPE")
	private String     availabilityType;
	@DbColumn(columnName="BH_ACCOUNT_ASSIGNMENT_GROUP_CD")
	private String     bhAccountAssignmentGroupCd;
	@DbColumn(columnName="COMMON_NM")
	private String     commonNm;
	@DbColumn(columnName="DIGITAL_SALES_IND")
	private String     digitalSalesInd;
	@DbColumn(columnName="EXT_PROFILE_KEY_IND")
	private String     extProfileKeyInd;
	@DbColumn(columnName="FCR_ELIGIBLE_IND")
	private String     fcrEligibleInd;
	@DbColumn(columnName="GBT_BRAND_CD")
	private String     gbtBrandCd;
	@DbColumn(columnName="GBT_LEVEL10_CD")
	private String     gbtLevel10Cd;
	@DbColumn(columnName="GBT_LEVEL10_DESC")
	private String     gbtLevel10Desc;
	@DbColumn(columnName="GBT_LEVEL17_CD")
	private String     gbtLevel17Cd;
	@DbColumn(columnName="GBT_LEVEL20_CD")
	private String     gbtLevel20Cd;
	@DbColumn(columnName="GBT_LEVEL20_DESC")
	private String     gbtLevel20Desc;
	@DbColumn(columnName="GBT_LEVEL_30_CD")
	private String     gbtLevel30Cd;
	@DbColumn(columnName="GBT_LEVEL30_DESC")
	private String     gbtLevel30Desc;
	@DbColumn(columnName="GENERAL_AREA_SELECTION")
	private String     generalAreaSelection;
	@DbColumn(columnName="ID_TYPE_CD")
	private String     idTypeCd;
	@DbColumn(columnName="IERP_PRODUCT_HIERARCHY_CD")
	private String     ierpProductHierarchyCd;
	@DbColumn(columnName="IERP_PROFIT_CENTER_CD")
	private String     ierpProfitCenterCd;
	@DbColumn(columnName="LAST_UPDATED_TMS")
	private String     lastUpdatedTms;
	@DbColumn(columnName="LEAD_PRACTICE_NM")
	private String     leadPracticeNm;
	@DbColumn(columnName="LEADS")
	private String     leads;
	@DbColumn(columnName="MACHINE_MODEL_CD")
	private String     machineModelCd;
	@DbColumn(columnName="MACHINE_TYPE_CD")
	private String     machineTypeCd;
	@DbColumn(columnName="MARKETING_NM")
	private String     marketingNm;
	@DbColumn(columnName="MARKETING_REPORTING_CD")
	private String     marketingReportingCd;
	@DbColumn(columnName="MODEL_CATEGORY_NM")
	private String     modelCategoryNm;
	@DbColumn(columnName="MODEL_SUBCATEGORY_NM")
	private String     modelSubcategoryNm;
	@DbColumn(columnName="MODEL_SUBGROUP_NM")
	private String     modelSubgroupNm;
	@DbColumn(columnName="OCC_NAME")
	private String     occName;
	@DbColumn(columnName="OC_STATUS_CD")
	private String     ocStatusCd;
	@DbColumn(columnName="OC_TYPE_CD")
	private String     ocTypeCd;
	@DbColumn(columnName="OFFERING_CATEGORY_CD")
	private String     offeringCategoryCd;
	@DbColumn(columnName="OFFERING_CATEGORY_DESC")
	private String     offeringCategoryDesc;
	@DbColumn(columnName="OFFERING_DESC")
	private String     offeringDesc;
	@DbColumn(columnName="OFFERING_CONFIGURATION_TYPE")
	private String     offeringConfigurationType;
	@DbColumn(columnName="OFFERING_INDICATOR")
	private String     offeringIndicator;
	@DbColumn(columnName="OFFERING_INVESTMENT_STATUS")
	private String     offeringInvestmentStatus;
	@DbColumn(columnName="OM_BRAND_CD")
	private String     omBrandCd;
	@DbColumn(columnName="OM_PRODUCT_FAMILY_CD")
	private String     omProductFamilyCd;
	@DbColumn(columnName="POC_FLG")
	private String     pocFlg;
	@DbColumn(columnName="PID_DESC")
	private String     pidDesc;
	@DbColumn(columnName="PID_NM")
	private String     pidNm;
	@DbColumn(columnName="POC_IND")
	private String     pocInd;
	@DbColumn(columnName="PRICE_FILE_NM")
	private String     priceFileNm;
	@DbColumn(columnName="PRODUCT_ID")
	private String     productId;
	@DbColumn(columnName="PRODUCT_TYPE")
	private String     productType;
	@DbColumn(columnName="PRODUCT_TYPE_CD")
	private String     productTypeCd;
	@DbColumn(columnName="PUBLIC_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       publicDt;
	@DbColumn(columnName="REVENUE_DIVISION")
	private String     revenueDivision;
	@DbColumn(columnName="REVENUE_DIVISION_CD")
	private String     revenueDivisionCd;
	@DbColumn(columnName="SHORT_NM")
	private String     shortNm;
	@DbColumn(columnName="SLT_LEVEL10_NM")
	private String     sltLevel10Nm;
	@DbColumn(columnName="SLT_LEVEL15_CD")
	private String     sltLevel15Cd;
	@DbColumn(columnName="SLT_LEVEL17_CD")
	private String     sltLevel17Cd;
	@DbColumn(columnName="SLT_LEVEL20_CD")
	private String     sltLevel20Cd;
	@DbColumn(columnName="SLT_LEVEL30_CD")
	private String     sltLevel30Cd;
	@DbColumn(columnName="SOP_RELEVANT_IND")
	private String     sopRelevantInd;
	@DbColumn(columnName="SOP_TASK_TYPECD")
	private String     sopTaskTypecd;
	@DbColumn(columnName="SVC_LINE_AREA_CD")
	private String     svcLineAreaCd;
	@DbColumn(columnName="SVC_LINE_AREA_DESC")
	private String     svcLineAreaDesc;
	@DbColumn(columnName="TAX_CLASSIFICATION")
	private String     taxClassification;
	@DbColumn(columnName="TAX_CD")
	private String     taxCd;
	@DbColumn(columnName="TYPE_NM")
	private String     typeNm;
	@DbColumn(columnName="UOM_CD")
	private String     uomCd;
	@DbColumn(columnName="OFFERING_CD",foreignKeySeq=1)
	private String     offeringCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OfferingComponentDim () {
				this.level = 5;
	}
	
	// Define natural key constructor
	public OfferingComponentDim (
      String     offeringComponentCd
	) {
		this.offeringComponentCd            = offeringComponentCd;
		this.level                          = 5;
	}
	
	// Define base constructor
	public OfferingComponentDim (
      String     offeringComponentCd
    , String     offeringComponentNm
    , String     utCd
    , String     utLevel10Cd
    , String     utLevel10Desc
    , String     utLevel15Cd
    , String     utLevel15Desc
    , String     utLevel17Cd
    , String     utLevel17Desc
    , String     utLevel20Cd
    , String     utLevel20Desc
    , String     utLevel30Cd
    , String     utLevel30Desc
    , String     utLevel35Cd
    , String     utLevel35Desc
    , String     gbsOfferingAttributeNm
    , String     gbsPracticeCd
    , String     gbsPracticeDesc
    , String     accountAssignmentGroup
    , String     announcementType
    , String     availabilityStatus
    , String     availabilityType
    , String     bhAccountAssignmentGroupCd
    , String     commonNm
    , String     digitalSalesInd
    , String     extProfileKeyInd
    , String     fcrEligibleInd
    , String     gbtBrandCd
    , String     gbtLevel10Cd
    , String     gbtLevel10Desc
    , String     gbtLevel17Cd
    , String     gbtLevel20Cd
    , String     gbtLevel20Desc
    , String     gbtLevel30Cd
    , String     gbtLevel30Desc
    , String     generalAreaSelection
    , String     idTypeCd
    , String     ierpProductHierarchyCd
    , String     ierpProfitCenterCd
    , String     lastUpdatedTms
    , String     leadPracticeNm
    , String     leads
    , String     machineModelCd
    , String     machineTypeCd
    , String     marketingNm
    , String     marketingReportingCd
    , String     modelCategoryNm
    , String     modelSubcategoryNm
    , String     modelSubgroupNm
    , String     occName
    , String     ocStatusCd
    , String     ocTypeCd
    , String     offeringCategoryCd
    , String     offeringCategoryDesc
    , String     offeringDesc
    , String     offeringConfigurationType
    , String     offeringIndicator
    , String     offeringInvestmentStatus
    , String     omBrandCd
    , String     omProductFamilyCd
    , String     pocFlg
    , String     pidDesc
    , String     pidNm
    , String     pocInd
    , String     priceFileNm
    , String     productId
    , String     productType
    , String     productTypeCd
    , Date       publicDt
    , String     revenueDivision
    , String     revenueDivisionCd
    , String     shortNm
    , String     sltLevel10Nm
    , String     sltLevel15Cd
    , String     sltLevel17Cd
    , String     sltLevel20Cd
    , String     sltLevel30Cd
    , String     sopRelevantInd
    , String     sopTaskTypecd
    , String     svcLineAreaCd
    , String     svcLineAreaDesc
    , String     taxClassification
    , String     taxCd
    , String     typeNm
    , String     uomCd
    , String     offeringCd
	) {
		this.offeringComponentCd            = offeringComponentCd;
		this.offeringComponentNm            = offeringComponentNm;
		this.utCd                           = utCd;
		this.utLevel10Cd                    = utLevel10Cd;
		this.utLevel10Desc                  = utLevel10Desc;
		this.utLevel15Cd                    = utLevel15Cd;
		this.utLevel15Desc                  = utLevel15Desc;
		this.utLevel17Cd                    = utLevel17Cd;
		this.utLevel17Desc                  = utLevel17Desc;
		this.utLevel20Cd                    = utLevel20Cd;
		this.utLevel20Desc                  = utLevel20Desc;
		this.utLevel30Cd                    = utLevel30Cd;
		this.utLevel30Desc                  = utLevel30Desc;
		this.utLevel35Cd                    = utLevel35Cd;
		this.utLevel35Desc                  = utLevel35Desc;
		this.gbsOfferingAttributeNm         = gbsOfferingAttributeNm;
		this.gbsPracticeCd                  = gbsPracticeCd;
		this.gbsPracticeDesc                = gbsPracticeDesc;
		this.accountAssignmentGroup         = accountAssignmentGroup;
		this.announcementType               = announcementType;
		this.availabilityStatus             = availabilityStatus;
		this.availabilityType               = availabilityType;
		this.bhAccountAssignmentGroupCd     = bhAccountAssignmentGroupCd;
		this.commonNm                       = commonNm;
		this.digitalSalesInd                = digitalSalesInd;
		this.extProfileKeyInd               = extProfileKeyInd;
		this.fcrEligibleInd                 = fcrEligibleInd;
		this.gbtBrandCd                     = gbtBrandCd;
		this.gbtLevel10Cd                   = gbtLevel10Cd;
		this.gbtLevel10Desc                 = gbtLevel10Desc;
		this.gbtLevel17Cd                   = gbtLevel17Cd;
		this.gbtLevel20Cd                   = gbtLevel20Cd;
		this.gbtLevel20Desc                 = gbtLevel20Desc;
		this.gbtLevel30Cd                   = gbtLevel30Cd;
		this.gbtLevel30Desc                 = gbtLevel30Desc;
		this.generalAreaSelection           = generalAreaSelection;
		this.idTypeCd                       = idTypeCd;
		this.ierpProductHierarchyCd         = ierpProductHierarchyCd;
		this.ierpProfitCenterCd             = ierpProfitCenterCd;
		this.lastUpdatedTms                 = lastUpdatedTms;
		this.leadPracticeNm                 = leadPracticeNm;
		this.leads                          = leads;
		this.machineModelCd                 = machineModelCd;
		this.machineTypeCd                  = machineTypeCd;
		this.marketingNm                    = marketingNm;
		this.marketingReportingCd           = marketingReportingCd;
		this.modelCategoryNm                = modelCategoryNm;
		this.modelSubcategoryNm             = modelSubcategoryNm;
		this.modelSubgroupNm                = modelSubgroupNm;
		this.occName                        = occName;
		this.ocStatusCd                     = ocStatusCd;
		this.ocTypeCd                       = ocTypeCd;
		this.offeringCategoryCd             = offeringCategoryCd;
		this.offeringCategoryDesc           = offeringCategoryDesc;
		this.offeringDesc                   = offeringDesc;
		this.offeringConfigurationType      = offeringConfigurationType;
		this.offeringIndicator              = offeringIndicator;
		this.offeringInvestmentStatus       = offeringInvestmentStatus;
		this.omBrandCd                      = omBrandCd;
		this.omProductFamilyCd              = omProductFamilyCd;
		this.pocFlg                         = pocFlg;
		this.pidDesc                        = pidDesc;
		this.pidNm                          = pidNm;
		this.pocInd                         = pocInd;
		this.priceFileNm                    = priceFileNm;
		this.productId                      = productId;
		this.productType                    = productType;
		this.productTypeCd                  = productTypeCd;
		this.publicDt                       = publicDt;
		this.revenueDivision                = revenueDivision;
		this.revenueDivisionCd              = revenueDivisionCd;
		this.shortNm                        = shortNm;
		this.sltLevel10Nm                   = sltLevel10Nm;
		this.sltLevel15Cd                   = sltLevel15Cd;
		this.sltLevel17Cd                   = sltLevel17Cd;
		this.sltLevel20Cd                   = sltLevel20Cd;
		this.sltLevel30Cd                   = sltLevel30Cd;
		this.sopRelevantInd                 = sopRelevantInd;
		this.sopTaskTypecd                  = sopTaskTypecd;
		this.svcLineAreaCd                  = svcLineAreaCd;
		this.svcLineAreaDesc                = svcLineAreaDesc;
		this.taxClassification              = taxClassification;
		this.taxCd                          = taxCd;
		this.typeNm                         = typeNm;
		this.uomCd                          = uomCd;
		this.offeringCd                     = offeringCd;
		this.level                          = 5;
	}
    
	// Define full constructor
	public OfferingComponentDim (
		  int        offeringComponentId
		, String     offeringComponentCd
		, String     offeringComponentNm
		, String     utCd
		, String     utLevel10Cd
		, String     utLevel10Desc
		, String     utLevel15Cd
		, String     utLevel15Desc
		, String     utLevel17Cd
		, String     utLevel17Desc
		, String     utLevel20Cd
		, String     utLevel20Desc
		, String     utLevel30Cd
		, String     utLevel30Desc
		, String     utLevel35Cd
		, String     utLevel35Desc
		, String     gbsOfferingAttributeNm
		, String     gbsPracticeCd
		, String     gbsPracticeDesc
		, String     accountAssignmentGroup
		, String     announcementType
		, String     availabilityStatus
		, String     availabilityType
		, String     bhAccountAssignmentGroupCd
		, String     commonNm
		, String     digitalSalesInd
		, String     extProfileKeyInd
		, String     fcrEligibleInd
		, String     gbtBrandCd
		, String     gbtLevel10Cd
		, String     gbtLevel10Desc
		, String     gbtLevel17Cd
		, String     gbtLevel20Cd
		, String     gbtLevel20Desc
		, String     gbtLevel30Cd
		, String     gbtLevel30Desc
		, String     generalAreaSelection
		, String     idTypeCd
		, String     ierpProductHierarchyCd
		, String     ierpProfitCenterCd
		, String     lastUpdatedTms
		, String     leadPracticeNm
		, String     leads
		, String     machineModelCd
		, String     machineTypeCd
		, String     marketingNm
		, String     marketingReportingCd
		, String     modelCategoryNm
		, String     modelSubcategoryNm
		, String     modelSubgroupNm
		, String     occName
		, String     ocStatusCd
		, String     ocTypeCd
		, String     offeringCategoryCd
		, String     offeringCategoryDesc
		, String     offeringDesc
		, String     offeringConfigurationType
		, String     offeringIndicator
		, String     offeringInvestmentStatus
		, String     omBrandCd
		, String     omProductFamilyCd
		, String     pocFlg
		, String     pidDesc
		, String     pidNm
		, String     pocInd
		, String     priceFileNm
		, String     productId
		, String     productType
		, String     productTypeCd
		, Date       publicDt
		, String     revenueDivision
		, String     revenueDivisionCd
		, String     shortNm
		, String     sltLevel10Nm
		, String     sltLevel15Cd
		, String     sltLevel17Cd
		, String     sltLevel20Cd
		, String     sltLevel30Cd
		, String     sopRelevantInd
		, String     sopTaskTypecd
		, String     svcLineAreaCd
		, String     svcLineAreaDesc
		, String     taxClassification
		, String     taxCd
		, String     typeNm
		, String     uomCd
		, String     offeringCd
	) {
		this.offeringComponentId            = offeringComponentId;
		this.offeringComponentCd            = offeringComponentCd;
		this.offeringComponentNm            = offeringComponentNm;
		this.utCd                           = utCd;
		this.utLevel10Cd                    = utLevel10Cd;
		this.utLevel10Desc                  = utLevel10Desc;
		this.utLevel15Cd                    = utLevel15Cd;
		this.utLevel15Desc                  = utLevel15Desc;
		this.utLevel17Cd                    = utLevel17Cd;
		this.utLevel17Desc                  = utLevel17Desc;
		this.utLevel20Cd                    = utLevel20Cd;
		this.utLevel20Desc                  = utLevel20Desc;
		this.utLevel30Cd                    = utLevel30Cd;
		this.utLevel30Desc                  = utLevel30Desc;
		this.utLevel35Cd                    = utLevel35Cd;
		this.utLevel35Desc                  = utLevel35Desc;
		this.gbsOfferingAttributeNm         = gbsOfferingAttributeNm;
		this.gbsPracticeCd                  = gbsPracticeCd;
		this.gbsPracticeDesc                = gbsPracticeDesc;
		this.accountAssignmentGroup         = accountAssignmentGroup;
		this.announcementType               = announcementType;
		this.availabilityStatus             = availabilityStatus;
		this.availabilityType               = availabilityType;
		this.bhAccountAssignmentGroupCd     = bhAccountAssignmentGroupCd;
		this.commonNm                       = commonNm;
		this.digitalSalesInd                = digitalSalesInd;
		this.extProfileKeyInd               = extProfileKeyInd;
		this.fcrEligibleInd                 = fcrEligibleInd;
		this.gbtBrandCd                     = gbtBrandCd;
		this.gbtLevel10Cd                   = gbtLevel10Cd;
		this.gbtLevel10Desc                 = gbtLevel10Desc;
		this.gbtLevel17Cd                   = gbtLevel17Cd;
		this.gbtLevel20Cd                   = gbtLevel20Cd;
		this.gbtLevel20Desc                 = gbtLevel20Desc;
		this.gbtLevel30Cd                   = gbtLevel30Cd;
		this.gbtLevel30Desc                 = gbtLevel30Desc;
		this.generalAreaSelection           = generalAreaSelection;
		this.idTypeCd                       = idTypeCd;
		this.ierpProductHierarchyCd         = ierpProductHierarchyCd;
		this.ierpProfitCenterCd             = ierpProfitCenterCd;
		this.lastUpdatedTms                 = lastUpdatedTms;
		this.leadPracticeNm                 = leadPracticeNm;
		this.leads                          = leads;
		this.machineModelCd                 = machineModelCd;
		this.machineTypeCd                  = machineTypeCd;
		this.marketingNm                    = marketingNm;
		this.marketingReportingCd           = marketingReportingCd;
		this.modelCategoryNm                = modelCategoryNm;
		this.modelSubcategoryNm             = modelSubcategoryNm;
		this.modelSubgroupNm                = modelSubgroupNm;
		this.occName                        = occName;
		this.ocStatusCd                     = ocStatusCd;
		this.ocTypeCd                       = ocTypeCd;
		this.offeringCategoryCd             = offeringCategoryCd;
		this.offeringCategoryDesc           = offeringCategoryDesc;
		this.offeringDesc                   = offeringDesc;
		this.offeringConfigurationType      = offeringConfigurationType;
		this.offeringIndicator              = offeringIndicator;
		this.offeringInvestmentStatus       = offeringInvestmentStatus;
		this.omBrandCd                      = omBrandCd;
		this.omProductFamilyCd              = omProductFamilyCd;
		this.pocFlg                         = pocFlg;
		this.pidDesc                        = pidDesc;
		this.pidNm                          = pidNm;
		this.pocInd                         = pocInd;
		this.priceFileNm                    = priceFileNm;
		this.productId                      = productId;
		this.productType                    = productType;
		this.productTypeCd                  = productTypeCd;
		this.publicDt                       = publicDt;
		this.revenueDivision                = revenueDivision;
		this.revenueDivisionCd              = revenueDivisionCd;
		this.shortNm                        = shortNm;
		this.sltLevel10Nm                   = sltLevel10Nm;
		this.sltLevel15Cd                   = sltLevel15Cd;
		this.sltLevel17Cd                   = sltLevel17Cd;
		this.sltLevel20Cd                   = sltLevel20Cd;
		this.sltLevel30Cd                   = sltLevel30Cd;
		this.sopRelevantInd                 = sopRelevantInd;
		this.sopTaskTypecd                  = sopTaskTypecd;
		this.svcLineAreaCd                  = svcLineAreaCd;
		this.svcLineAreaDesc                = svcLineAreaDesc;
		this.taxClassification              = taxClassification;
		this.taxCd                          = taxCd;
		this.typeNm                         = typeNm;
		this.uomCd                          = uomCd;
		this.offeringCd                     = offeringCd;
		this.level                          = 5;
	}
	
	@Override
	public String getCode() { 
		return this.offeringComponentCd
		;
	}
	public String getDescription() { 
		return this.offeringComponentNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OfferingComponentDim other = (OfferingComponentDim) obj;
		if (
            this.offeringComponentCd.equals(other.getOfferingComponentCd())
         && this.offeringComponentNm.equals(other.getOfferingComponentNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.offeringComponentCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringComponentNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.utCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel10Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel10Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel15Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel15Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel17Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel17Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel20Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel20Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel30Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel30Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel35Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.utLevel35Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsOfferingAttributeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsPracticeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsPracticeDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.accountAssignmentGroup))
        + "," + Helpers.formatCsvField(String.valueOf(this.announcementType))
        + "," + Helpers.formatCsvField(String.valueOf(this.availabilityStatus))
        + "," + Helpers.formatCsvField(String.valueOf(this.availabilityType))
        + "," + Helpers.formatCsvField(String.valueOf(this.bhAccountAssignmentGroupCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.commonNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.digitalSalesInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.extProfileKeyInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.fcrEligibleInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtBrandCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel10Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel10Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel17Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel20Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel20Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel30Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbtLevel30Desc))
        + "," + Helpers.formatCsvField(String.valueOf(this.generalAreaSelection))
        + "," + Helpers.formatCsvField(String.valueOf(this.idTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ierpProductHierarchyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ierpProfitCenterCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.lastUpdatedTms))
        + "," + Helpers.formatCsvField(String.valueOf(this.leadPracticeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.leads))
        + "," + Helpers.formatCsvField(String.valueOf(this.machineModelCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.machineTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketingNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketingReportingCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.modelCategoryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.modelSubcategoryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.modelSubgroupNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.occName))
        + "," + Helpers.formatCsvField(String.valueOf(this.ocStatusCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ocTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCategoryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCategoryDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringConfigurationType))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringIndicator))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringInvestmentStatus))
        + "," + Helpers.formatCsvField(String.valueOf(this.omBrandCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.omProductFamilyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.pocFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.pidDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.pidNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.pocInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.priceFileNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.productId))
        + "," + Helpers.formatCsvField(String.valueOf(this.productType))
        + "," + Helpers.formatCsvField(String.valueOf(this.productTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.publicDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.revenueDivision))
        + "," + Helpers.formatCsvField(String.valueOf(this.revenueDivisionCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sltLevel10Nm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sltLevel15Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sltLevel17Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sltLevel20Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sltLevel30Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sopRelevantInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sopTaskTypecd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcLineAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcLineAreaDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.taxClassification))
        + "," + Helpers.formatCsvField(String.valueOf(this.taxCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.typeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.uomCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_COMPONENT_CD")
        + "," + Helpers.formatCsvField("OFFERING_COMPONENT_NM")
        + "," + Helpers.formatCsvField("UT_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL10_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL10_DESC")
        + "," + Helpers.formatCsvField("UT_LEVEL15_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL15_DESC")
        + "," + Helpers.formatCsvField("UT_LEVEL17_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL17_DESC")
        + "," + Helpers.formatCsvField("UT_LEVEL20_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL20_DESC")
        + "," + Helpers.formatCsvField("UT_LEVEL30_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL30_DESC")
        + "," + Helpers.formatCsvField("UT_LEVEL35_CD")
        + "," + Helpers.formatCsvField("UT_LEVEL35_DESC")
        + "," + Helpers.formatCsvField("GBS_OFFERING_ATTRIBUTE_NM")
        + "," + Helpers.formatCsvField("GBS_PRACTICE_CD")
        + "," + Helpers.formatCsvField("GBS_PRACTICE_DESC")
        + "," + Helpers.formatCsvField("ACCOUNT_ASSIGNMENT_GROUP")
        + "," + Helpers.formatCsvField("ANNOUNCEMENT_TYPE")
        + "," + Helpers.formatCsvField("AVAILABILITY_STATUS")
        + "," + Helpers.formatCsvField("AVAILABILITY_TYPE")
        + "," + Helpers.formatCsvField("BH_ACCOUNT_ASSIGNMENT_GROUP_CD")
        + "," + Helpers.formatCsvField("COMMON_NM")
        + "," + Helpers.formatCsvField("DIGITAL_SALES_IND")
        + "," + Helpers.formatCsvField("EXT_PROFILE_KEY_IND")
        + "," + Helpers.formatCsvField("FCR_ELIGIBLE_IND")
        + "," + Helpers.formatCsvField("GBT_BRAND_CD")
        + "," + Helpers.formatCsvField("GBT_LEVEL10_CD")
        + "," + Helpers.formatCsvField("GBT_LEVEL10_DESC")
        + "," + Helpers.formatCsvField("GBT_LEVEL17_CD")
        + "," + Helpers.formatCsvField("GBT_LEVEL20_CD")
        + "," + Helpers.formatCsvField("GBT_LEVEL20_DESC")
        + "," + Helpers.formatCsvField("GBT_LEVEL_30_CD")
        + "," + Helpers.formatCsvField("GBT_LEVEL30_DESC")
        + "," + Helpers.formatCsvField("GENERAL_AREA_SELECTION")
        + "," + Helpers.formatCsvField("ID_TYPE_CD")
        + "," + Helpers.formatCsvField("IERP_PRODUCT_HIERARCHY_CD")
        + "," + Helpers.formatCsvField("IERP_PROFIT_CENTER_CD")
        + "," + Helpers.formatCsvField("LAST_UPDATED_TMS")
        + "," + Helpers.formatCsvField("LEAD_PRACTICE_NM")
        + "," + Helpers.formatCsvField("LEADS")
        + "," + Helpers.formatCsvField("MACHINE_MODEL_CD")
        + "," + Helpers.formatCsvField("MACHINE_TYPE_CD")
        + "," + Helpers.formatCsvField("MARKETING_NM")
        + "," + Helpers.formatCsvField("MARKETING_REPORTING_CD")
        + "," + Helpers.formatCsvField("MODEL_CATEGORY_NM")
        + "," + Helpers.formatCsvField("MODEL_SUBCATEGORY_NM")
        + "," + Helpers.formatCsvField("MODEL_SUBGROUP_NM")
        + "," + Helpers.formatCsvField("OCC_NAME")
        + "," + Helpers.formatCsvField("OC_STATUS_CD")
        + "," + Helpers.formatCsvField("OC_TYPE_CD")
        + "," + Helpers.formatCsvField("OFFERING_CATEGORY_CD")
        + "," + Helpers.formatCsvField("OFFERING_CATEGORY_DESC")
        + "," + Helpers.formatCsvField("OFFERING_DESC")
        + "," + Helpers.formatCsvField("OFFERING_CONFIGURATION_TYPE")
        + "," + Helpers.formatCsvField("OFFERING_INDICATOR")
        + "," + Helpers.formatCsvField("OFFERING_INVESTMENT_STATUS")
        + "," + Helpers.formatCsvField("OM_BRAND_CD")
        + "," + Helpers.formatCsvField("OM_PRODUCT_FAMILY_CD")
        + "," + Helpers.formatCsvField("POC_FLG")
        + "," + Helpers.formatCsvField("PID_DESC")
        + "," + Helpers.formatCsvField("PID_NM")
        + "," + Helpers.formatCsvField("POC_IND")
        + "," + Helpers.formatCsvField("PRICE_FILE_NM")
        + "," + Helpers.formatCsvField("PRODUCT_ID")
        + "," + Helpers.formatCsvField("PRODUCT_TYPE")
        + "," + Helpers.formatCsvField("PRODUCT_TYPE_CD")
        + "," + Helpers.formatCsvField("PUBLIC_DT")
        + "," + Helpers.formatCsvField("REVENUE_DIVISION")
        + "," + Helpers.formatCsvField("REVENUE_DIVISION_CD")
        + "," + Helpers.formatCsvField("SHORT_NM")
        + "," + Helpers.formatCsvField("SLT_LEVEL10_NM")
        + "," + Helpers.formatCsvField("SLT_LEVEL15_CD")
        + "," + Helpers.formatCsvField("SLT_LEVEL17_CD")
        + "," + Helpers.formatCsvField("SLT_LEVEL20_CD")
        + "," + Helpers.formatCsvField("SLT_LEVEL30_CD")
        + "," + Helpers.formatCsvField("SOP_RELEVANT_IND")
        + "," + Helpers.formatCsvField("SOP_TASK_TYPECD")
        + "," + Helpers.formatCsvField("SVC_LINE_AREA_CD")
        + "," + Helpers.formatCsvField("SVC_LINE_AREA_DESC")
        + "," + Helpers.formatCsvField("TAX_CLASSIFICATION")
        + "," + Helpers.formatCsvField("TAX_CD")
        + "," + Helpers.formatCsvField("TYPE_NM")
        + "," + Helpers.formatCsvField("UOM_CD")
        + "," + Helpers.formatCsvField("OFFERING_CD")
		;
	}
    
	// Define Getters and Setters
	public int getOfferingComponentId() {
		return offeringComponentId;
	}
	public void setOfferingComponentId(int offeringComponentId) {
		this.offeringComponentId = offeringComponentId;
	}
	public String getOfferingComponentCd() {
		return offeringComponentCd;
	}
	public void setOfferingComponentCd(String offeringComponentCd) {
		this.offeringComponentCd = offeringComponentCd;
	}
	public String getOfferingComponentNm() {
		return offeringComponentNm;
	}
	public void setOfferingComponentNm(String offeringComponentNm) {
		this.offeringComponentNm = offeringComponentNm;
	}
	public String getUtCd() {
		return utCd;
	}
	public void setUtCd(String utCd) {
		this.utCd = utCd;
	}
	public String getUtLevel10Cd() {
		return utLevel10Cd;
	}
	public void setUtLevel10Cd(String utLevel10Cd) {
		this.utLevel10Cd = utLevel10Cd;
	}
	public String getUtLevel10Desc() {
		return utLevel10Desc;
	}
	public void setUtLevel10Desc(String utLevel10Desc) {
		this.utLevel10Desc = utLevel10Desc;
	}
	public String getUtLevel15Cd() {
		return utLevel15Cd;
	}
	public void setUtLevel15Cd(String utLevel15Cd) {
		this.utLevel15Cd = utLevel15Cd;
	}
	public String getUtLevel15Desc() {
		return utLevel15Desc;
	}
	public void setUtLevel15Desc(String utLevel15Desc) {
		this.utLevel15Desc = utLevel15Desc;
	}
	public String getUtLevel17Cd() {
		return utLevel17Cd;
	}
	public void setUtLevel17Cd(String utLevel17Cd) {
		this.utLevel17Cd = utLevel17Cd;
	}
	public String getUtLevel17Desc() {
		return utLevel17Desc;
	}
	public void setUtLevel17Desc(String utLevel17Desc) {
		this.utLevel17Desc = utLevel17Desc;
	}
	public String getUtLevel20Cd() {
		return utLevel20Cd;
	}
	public void setUtLevel20Cd(String utLevel20Cd) {
		this.utLevel20Cd = utLevel20Cd;
	}
	public String getUtLevel20Desc() {
		return utLevel20Desc;
	}
	public void setUtLevel20Desc(String utLevel20Desc) {
		this.utLevel20Desc = utLevel20Desc;
	}
	public String getUtLevel30Cd() {
		return utLevel30Cd;
	}
	public void setUtLevel30Cd(String utLevel30Cd) {
		this.utLevel30Cd = utLevel30Cd;
	}
	public String getUtLevel30Desc() {
		return utLevel30Desc;
	}
	public void setUtLevel30Desc(String utLevel30Desc) {
		this.utLevel30Desc = utLevel30Desc;
	}
	public String getUtLevel35Cd() {
		return utLevel35Cd;
	}
	public void setUtLevel35Cd(String utLevel35Cd) {
		this.utLevel35Cd = utLevel35Cd;
	}
	public String getUtLevel35Desc() {
		return utLevel35Desc;
	}
	public void setUtLevel35Desc(String utLevel35Desc) {
		this.utLevel35Desc = utLevel35Desc;
	}
	public String getGbsOfferingAttributeNm() {
		return gbsOfferingAttributeNm;
	}
	public void setGbsOfferingAttributeNm(String gbsOfferingAttributeNm) {
		this.gbsOfferingAttributeNm = gbsOfferingAttributeNm;
	}
	public String getGbsPracticeCd() {
		return gbsPracticeCd;
	}
	public void setGbsPracticeCd(String gbsPracticeCd) {
		this.gbsPracticeCd = gbsPracticeCd;
	}
	public String getGbsPracticeDesc() {
		return gbsPracticeDesc;
	}
	public void setGbsPracticeDesc(String gbsPracticeDesc) {
		this.gbsPracticeDesc = gbsPracticeDesc;
	}
	public String getAccountAssignmentGroup() {
		return accountAssignmentGroup;
	}
	public void setAccountAssignmentGroup(String accountAssignmentGroup) {
		this.accountAssignmentGroup = accountAssignmentGroup;
	}
	public String getAnnouncementType() {
		return announcementType;
	}
	public void setAnnouncementType(String announcementType) {
		this.announcementType = announcementType;
	}
	public String getAvailabilityStatus() {
		return availabilityStatus;
	}
	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}
	public String getAvailabilityType() {
		return availabilityType;
	}
	public void setAvailabilityType(String availabilityType) {
		this.availabilityType = availabilityType;
	}
	public String getBhAccountAssignmentGroupCd() {
		return bhAccountAssignmentGroupCd;
	}
	public void setBhAccountAssignmentGroupCd(String bhAccountAssignmentGroupCd) {
		this.bhAccountAssignmentGroupCd = bhAccountAssignmentGroupCd;
	}
	public String getCommonNm() {
		return commonNm;
	}
	public void setCommonNm(String commonNm) {
		this.commonNm = commonNm;
	}
	public String getDigitalSalesInd() {
		return digitalSalesInd;
	}
	public void setDigitalSalesInd(String digitalSalesInd) {
		this.digitalSalesInd = digitalSalesInd;
	}
	public String getExtProfileKeyInd() {
		return extProfileKeyInd;
	}
	public void setExtProfileKeyInd(String extProfileKeyInd) {
		this.extProfileKeyInd = extProfileKeyInd;
	}
	public String getFcrEligibleInd() {
		return fcrEligibleInd;
	}
	public void setFcrEligibleInd(String fcrEligibleInd) {
		this.fcrEligibleInd = fcrEligibleInd;
	}
	public String getGbtBrandCd() {
		return gbtBrandCd;
	}
	public void setGbtBrandCd(String gbtBrandCd) {
		this.gbtBrandCd = gbtBrandCd;
	}
	public String getGbtLevel10Cd() {
		return gbtLevel10Cd;
	}
	public void setGbtLevel10Cd(String gbtLevel10Cd) {
		this.gbtLevel10Cd = gbtLevel10Cd;
	}
	public String getGbtLevel10Desc() {
		return gbtLevel10Desc;
	}
	public void setGbtLevel10Desc(String gbtLevel10Desc) {
		this.gbtLevel10Desc = gbtLevel10Desc;
	}
	public String getGbtLevel17Cd() {
		return gbtLevel17Cd;
	}
	public void setGbtLevel17Cd(String gbtLevel17Cd) {
		this.gbtLevel17Cd = gbtLevel17Cd;
	}
	public String getGbtLevel20Cd() {
		return gbtLevel20Cd;
	}
	public void setGbtLevel20Cd(String gbtLevel20Cd) {
		this.gbtLevel20Cd = gbtLevel20Cd;
	}
	public String getGbtLevel20Desc() {
		return gbtLevel20Desc;
	}
	public void setGbtLevel20Desc(String gbtLevel20Desc) {
		this.gbtLevel20Desc = gbtLevel20Desc;
	}
	public String getGbtLevel30Cd() {
		return gbtLevel30Cd;
	}
	public void setGbtLevel30Cd(String gbtLevel30Cd) {
		this.gbtLevel30Cd = gbtLevel30Cd;
	}
	public String getGbtLevel30Desc() {
		return gbtLevel30Desc;
	}
	public void setGbtLevel30Desc(String gbtLevel30Desc) {
		this.gbtLevel30Desc = gbtLevel30Desc;
	}
	public String getGeneralAreaSelection() {
		return generalAreaSelection;
	}
	public void setGeneralAreaSelection(String generalAreaSelection) {
		this.generalAreaSelection = generalAreaSelection;
	}
	public String getIdTypeCd() {
		return idTypeCd;
	}
	public void setIdTypeCd(String idTypeCd) {
		this.idTypeCd = idTypeCd;
	}
	public String getIerpProductHierarchyCd() {
		return ierpProductHierarchyCd;
	}
	public void setIerpProductHierarchyCd(String ierpProductHierarchyCd) {
		this.ierpProductHierarchyCd = ierpProductHierarchyCd;
	}
	public String getIerpProfitCenterCd() {
		return ierpProfitCenterCd;
	}
	public void setIerpProfitCenterCd(String ierpProfitCenterCd) {
		this.ierpProfitCenterCd = ierpProfitCenterCd;
	}
	public String getLastUpdatedTms() {
		return lastUpdatedTms;
	}
	public void setLastUpdatedTms(String lastUpdatedTms) {
		this.lastUpdatedTms = lastUpdatedTms;
	}
	public String getLeadPracticeNm() {
		return leadPracticeNm;
	}
	public void setLeadPracticeNm(String leadPracticeNm) {
		this.leadPracticeNm = leadPracticeNm;
	}
	public String getLeads() {
		return leads;
	}
	public void setLeads(String leads) {
		this.leads = leads;
	}
	public String getMachineModelCd() {
		return machineModelCd;
	}
	public void setMachineModelCd(String machineModelCd) {
		this.machineModelCd = machineModelCd;
	}
	public String getMachineTypeCd() {
		return machineTypeCd;
	}
	public void setMachineTypeCd(String machineTypeCd) {
		this.machineTypeCd = machineTypeCd;
	}
	public String getMarketingNm() {
		return marketingNm;
	}
	public void setMarketingNm(String marketingNm) {
		this.marketingNm = marketingNm;
	}
	public String getMarketingReportingCd() {
		return marketingReportingCd;
	}
	public void setMarketingReportingCd(String marketingReportingCd) {
		this.marketingReportingCd = marketingReportingCd;
	}
	public String getModelCategoryNm() {
		return modelCategoryNm;
	}
	public void setModelCategoryNm(String modelCategoryNm) {
		this.modelCategoryNm = modelCategoryNm;
	}
	public String getModelSubcategoryNm() {
		return modelSubcategoryNm;
	}
	public void setModelSubcategoryNm(String modelSubcategoryNm) {
		this.modelSubcategoryNm = modelSubcategoryNm;
	}
	public String getModelSubgroupNm() {
		return modelSubgroupNm;
	}
	public void setModelSubgroupNm(String modelSubgroupNm) {
		this.modelSubgroupNm = modelSubgroupNm;
	}
	public String getOccName() {
		return occName;
	}
	public void setOccName(String occName) {
		this.occName = occName;
	}
	public String getOcStatusCd() {
		return ocStatusCd;
	}
	public void setOcStatusCd(String ocStatusCd) {
		this.ocStatusCd = ocStatusCd;
	}
	public String getOcTypeCd() {
		return ocTypeCd;
	}
	public void setOcTypeCd(String ocTypeCd) {
		this.ocTypeCd = ocTypeCd;
	}
	public String getOfferingCategoryCd() {
		return offeringCategoryCd;
	}
	public void setOfferingCategoryCd(String offeringCategoryCd) {
		this.offeringCategoryCd = offeringCategoryCd;
	}
	public String getOfferingCategoryDesc() {
		return offeringCategoryDesc;
	}
	public void setOfferingCategoryDesc(String offeringCategoryDesc) {
		this.offeringCategoryDesc = offeringCategoryDesc;
	}
	public String getOfferingDesc() {
		return offeringDesc;
	}
	public void setOfferingDesc(String offeringDesc) {
		this.offeringDesc = offeringDesc;
	}
	public String getOfferingConfigurationType() {
		return offeringConfigurationType;
	}
	public void setOfferingConfigurationType(String offeringConfigurationType) {
		this.offeringConfigurationType = offeringConfigurationType;
	}
	public String getOfferingIndicator() {
		return offeringIndicator;
	}
	public void setOfferingIndicator(String offeringIndicator) {
		this.offeringIndicator = offeringIndicator;
	}
	public String getOfferingInvestmentStatus() {
		return offeringInvestmentStatus;
	}
	public void setOfferingInvestmentStatus(String offeringInvestmentStatus) {
		this.offeringInvestmentStatus = offeringInvestmentStatus;
	}
	public String getOmBrandCd() {
		return omBrandCd;
	}
	public void setOmBrandCd(String omBrandCd) {
		this.omBrandCd = omBrandCd;
	}
	public String getOmProductFamilyCd() {
		return omProductFamilyCd;
	}
	public void setOmProductFamilyCd(String omProductFamilyCd) {
		this.omProductFamilyCd = omProductFamilyCd;
	}
	public String getPocFlg() {
		return pocFlg;
	}
	public void setPocFlg(String pocFlg) {
		this.pocFlg = pocFlg;
	}
	public String getPidDesc() {
		return pidDesc;
	}
	public void setPidDesc(String pidDesc) {
		this.pidDesc = pidDesc;
	}
	public String getPidNm() {
		return pidNm;
	}
	public void setPidNm(String pidNm) {
		this.pidNm = pidNm;
	}
	public String getPocInd() {
		return pocInd;
	}
	public void setPocInd(String pocInd) {
		this.pocInd = pocInd;
	}
	public String getPriceFileNm() {
		return priceFileNm;
	}
	public void setPriceFileNm(String priceFileNm) {
		this.priceFileNm = priceFileNm;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductTypeCd() {
		return productTypeCd;
	}
	public void setProductTypeCd(String productTypeCd) {
		this.productTypeCd = productTypeCd;
	}
	public Date getPublicDt() {
		return publicDt;
	}
	public void setPublicDt(Date publicDt) {
		this.publicDt = publicDt;
	}
	public String getRevenueDivision() {
		return revenueDivision;
	}
	public void setRevenueDivision(String revenueDivision) {
		this.revenueDivision = revenueDivision;
	}
	public String getRevenueDivisionCd() {
		return revenueDivisionCd;
	}
	public void setRevenueDivisionCd(String revenueDivisionCd) {
		this.revenueDivisionCd = revenueDivisionCd;
	}
	public String getShortNm() {
		return shortNm;
	}
	public void setShortNm(String shortNm) {
		this.shortNm = shortNm;
	}
	public String getSltLevel10Nm() {
		return sltLevel10Nm;
	}
	public void setSltLevel10Nm(String sltLevel10Nm) {
		this.sltLevel10Nm = sltLevel10Nm;
	}
	public String getSltLevel15Cd() {
		return sltLevel15Cd;
	}
	public void setSltLevel15Cd(String sltLevel15Cd) {
		this.sltLevel15Cd = sltLevel15Cd;
	}
	public String getSltLevel17Cd() {
		return sltLevel17Cd;
	}
	public void setSltLevel17Cd(String sltLevel17Cd) {
		this.sltLevel17Cd = sltLevel17Cd;
	}
	public String getSltLevel20Cd() {
		return sltLevel20Cd;
	}
	public void setSltLevel20Cd(String sltLevel20Cd) {
		this.sltLevel20Cd = sltLevel20Cd;
	}
	public String getSltLevel30Cd() {
		return sltLevel30Cd;
	}
	public void setSltLevel30Cd(String sltLevel30Cd) {
		this.sltLevel30Cd = sltLevel30Cd;
	}
	public String getSopRelevantInd() {
		return sopRelevantInd;
	}
	public void setSopRelevantInd(String sopRelevantInd) {
		this.sopRelevantInd = sopRelevantInd;
	}
	public String getSopTaskTypecd() {
		return sopTaskTypecd;
	}
	public void setSopTaskTypecd(String sopTaskTypecd) {
		this.sopTaskTypecd = sopTaskTypecd;
	}
	public String getSvcLineAreaCd() {
		return svcLineAreaCd;
	}
	public void setSvcLineAreaCd(String svcLineAreaCd) {
		this.svcLineAreaCd = svcLineAreaCd;
	}
	public String getSvcLineAreaDesc() {
		return svcLineAreaDesc;
	}
	public void setSvcLineAreaDesc(String svcLineAreaDesc) {
		this.svcLineAreaDesc = svcLineAreaDesc;
	}
	public String getTaxClassification() {
		return taxClassification;
	}
	public void setTaxClassification(String taxClassification) {
		this.taxClassification = taxClassification;
	}
	public String getTaxCd() {
		return taxCd;
	}
	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
	}
	public String getTypeNm() {
		return typeNm;
	}
	public void setTypeNm(String typeNm) {
		this.typeNm = typeNm;
	}
	public String getUomCd() {
		return uomCd;
	}
	public void setUomCd(String uomCd) {
		this.uomCd = uomCd;
	}
	public String getOfferingCd() {
		return offeringCd;
	}
	public void setOfferingCd(String offeringCd) {
		this.offeringCd = offeringCd;
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
	
	@Override
    public int compareTo(OfferingComponentDim o) {
        return this.getOfferingComponentCd().compareTo(o.getOfferingComponentCd());
    }
}