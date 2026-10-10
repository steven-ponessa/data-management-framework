package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;

@DbTable(beanName="SdssChangeRequestPendingDim",baseTableName="REFT.SDSS_CHANGE_REQUEST_PENDING",isDimension=false)
//@DbTable(beanName="SdssChangeRequestPendingDim",baseTableName="SDSS.SDSS_CHANGE_REQUEST_PENDING",isDimension=false)
public class SdssChangeRequestPendingDim  {
	@DbColumn(columnName="CHANGE_ID",isId=true,keySeq=1)
	@ExcelSheet(columnName="Change Id",columnNum=0)
	private int        changeId;
	
	@DbColumn(columnName="CYCLE_ID")
	@ExcelSheet(columnName="Cycle Id",columnNum=1)
	private Integer       cycleId;
	
	@DbColumn(columnName="REQUEST_STATUS_CD")
	@ExcelSheet(columnName="Approval Status",columnNum=2)
	private String        requestStatusCd;
	
	@DbColumn(columnName="APRVR_ID")
	private String        aprvrId;
	
	@DbColumn(columnName="APRVL_TMS")
	private Timestamp     aprvlTms;
	
	@DbColumn(columnName="CHANGE_TYPE_CD")
	@ExcelSheet(columnName="Change Type",columnNum=7)
	private String     changeTypeCd;
	
	@DbColumn(columnName="BASE_ROW_ID")
	@ExcelSheet(columnName="Base Row Id",columnNum=4)
	private Integer       baseRowId;
	
	@DbColumn(columnName="PROPOSED_ROW_ID")
	@ExcelSheet(columnName="Proposed Row Id",columnNum=5)
	private Integer       proposedRowId;
	
	@DbColumn(columnName="ESVF_ID")
	@ExcelSheet(columnName="ESVF Id",columnNum=6)
	private String     esvfId;
	
	@DbColumn(columnName="EDITOR_INTRANET_ID")
	@ExcelSheet(columnName="Submitted By",columnNum=8)
	private String     editorIntranetId;
	
	@DbColumn(columnName="CHANGE_TMS")
	@ExcelSheet(columnName="Submitted",columnNum=9)
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  changeTms;
	
	@DbColumn(columnName="PRIMARY_APRVL_STATUS")
	private String     primaryAprvlStatus;
	
	@DbColumn(columnName="PRIMARY_APRVR_ID")
	private String     primaryAprvrId;
	
	@DbColumn(columnName="PRIMARY_APRVL_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  primaryAprvlTms;
	
	@DbColumn(columnName="SECONDARY_APRVL_STATUS")
	private String     secondaryAprvlStatus;
	
	@DbColumn(columnName="SECONDARY_APRVR_ID")
	private String     secondaryAprvrId;
	
	@DbColumn(columnName="SECONDARY_APRVL_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  secondaryAprvlTms;
	
	@DbColumn(columnName="REJECTION_REASON")
	@ExcelSheet(columnName="Rejection Reason",columnNum=3)	
	private String     rejectionReason;
	
	@DbColumn(columnName="RECORD_ID")
	@ExcelSheet(columnName="Record Id",columnNum=12)
	private int        recordId;
	
	@DbColumn(columnName="APRVL_CD")
	private String     aprvlCd;
	
	@DbColumn(columnName="GEOGRAPHY_TYPE_NM")
	@ExcelSheet(columnName="Geography Type",columnNum=13)
	private String     geographyTypeNm;
	
	@DbColumn(columnName="GEOGRAPHY_NM")
	@ExcelSheet(columnName="Geography",columnNum=14)
	private String     geographyNm;
	
	@DbColumn(columnName="MARKET_NM")
	@ExcelSheet(columnName="Market",columnNum=15)
	private String     marketNm;
	
	@DbColumn(columnName="MARKET_REGION_NM")
	@ExcelSheet(columnName="Market Region",columnNum=16)
	private String     marketRegionNm;
	
	@DbColumn(columnName="COUNTRY_NM")
	@ExcelSheet(columnName="Country",columnNum=17)
	private String     countryNm;
	
	@DbColumn(columnName="GROWTH_PLATFORM_NM")
	@ExcelSheet(columnName="Growth Platform",columnNum=18)
	private String     growthPlatformNm;
	
	@DbColumn(columnName="SERVICE_LINE_NM")
	@ExcelSheet(columnName="Service Line",columnNum=19)
	private String     serviceLineNm;
	
	@DbColumn(columnName="PRACTICE_NM")
	@ExcelSheet(columnName="Practice",columnNum=20)
	private String     practiceNm;
	
	@DbColumn(columnName="JRS_NM")
	@ExcelSheet(columnName="JRS Name",columnNum=21)
	private String     jrsNm;
	
	@DbColumn(columnName="JRS_ID")
	@ExcelSheet(columnName="JRS",columnNum=22)
	private String     jrsId;
	
	@DbColumn(columnName="BAND_CAT_NM")
	@ExcelSheet(columnName="Band Category",columnNum=23)
	private String     bandCatNm;
	
	@DbColumn(columnName="MSI_NM")
	@ExcelSheet(columnName="Scarcity Index",columnNum=26)
	private String     msiNm;
	
	@DbColumn(columnName="ADS_NM")
	@ExcelSheet(columnName="Anticipated Demand for Skills",columnNum=27)
	private String     adsNm;
	
	@DbColumn(columnName="SOURCING_STRATEGY_NM")
	@ExcelSheet(columnName="Sourcing Strategy",columnNum=24)
	private String     sourcingStrategyNm;
	
	@DbColumn(columnName="ALT_SOURCING_STRATEGY_NM")
	@ExcelSheet(columnName="Market Sourcing Strategy Addendum",columnNum=25)
	private String     altSourcingStrategyNm;
	
	@DbColumn(columnName="EXCEPTION_CD")
	@ExcelSheet(columnName="Exception Code",columnNum=28)
	private String     exceptionCd;
	
	@DbColumn(columnName="EXCEPTION_REASON_TXT")
	@ExcelSheet(columnName="Exception Reason Text",columnNum=29)
	private String     exceptionReasonTxt;
	
	@DbColumn(columnName="EXCEPT_CD")
	private int     exceptCd;
	
	@DbColumn(columnName="EFF_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  effTms;
	
	@DbColumn(columnName="EXPIR_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  expirTms;
	
	private String intranetId;
	
	private String userType;

	// Define null constructor
	public SdssChangeRequestPendingDim () {
		
	}
	
	// Define natural key constructor
	public SdssChangeRequestPendingDim (
      int        changeId
	) {
		this.changeId                       = changeId;
		
	}
	
    
	// Define full constructor
	public SdssChangeRequestPendingDim (
		  int        changeId
		, Integer    cycleId
		, String     requestStatusCd
		, String     aprvrId
		, Timestamp  aprvlTms
		, String     changeTypeCd
		, Integer    baseRowId
		, Integer    proposedRowId
		, String     esvfId
		, String     editorIntranetId
		, Timestamp  changeTms
		, String     primaryAprvlStatus
		, String     primaryAprvrId
		, Timestamp  primaryAprvlTms
		, String     secondaryAprvlStatus
		, String     secondaryAprvrId
		, Timestamp  secondaryAprvlTms
		, String     rejectionReason
		, int        recordId
		, String     aprvlCd
		, String     geographyTypeNm
		, String     geographyNm
		, String     marketNm
		, String     marketRegionNm
		, String     countryNm
		, String     growthPlatformNm
		, String     serviceLineNm
		, String     practiceNm
		, String     jrsNm
		, String     jrsId
		, String     bandCatNm
		, String     msiNm
		, String     adsNm
		, String     sourcingStrategyNm
		, String     altSourcingStrategyNm
		, String     exceptionCd
		, String     exceptionReasonTxt
		, int        exceptCd
		, Timestamp  effTms
		, Timestamp  expirTms
		, String     intranetId
		, String     userType
	) {
		this.changeId                       = changeId;
		this.cycleId                        = cycleId;
		this.requestStatusCd                = requestStatusCd;
		this.aprvrId                        = aprvrId;
		this.aprvlTms                       = aprvlTms;
		this.changeTypeCd                   = changeTypeCd;
		this.baseRowId                      = baseRowId;
		this.proposedRowId                  = proposedRowId;
		this.esvfId                         = esvfId;
		this.editorIntranetId               = editorIntranetId;
		this.changeTms                      = changeTms;
		this.primaryAprvlStatus             = primaryAprvlStatus;
		this.primaryAprvrId                 = primaryAprvrId;
		this.primaryAprvlTms                = primaryAprvlTms;
		this.secondaryAprvlStatus           = secondaryAprvlStatus;
		this.secondaryAprvrId               = secondaryAprvrId;
		this.secondaryAprvlTms              = secondaryAprvlTms;
		this.rejectionReason                = rejectionReason;
		this.recordId                       = recordId;
		this.aprvlCd                        = aprvlCd;
		this.geographyTypeNm                = geographyTypeNm;
		this.geographyNm                    = geographyNm;
		this.marketNm                       = marketNm;
		this.marketRegionNm                 = marketRegionNm;
		this.countryNm                      = countryNm;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceNm                     = practiceNm;
		this.jrsNm                          = jrsNm;
		this.jrsId                          = jrsId;
		this.bandCatNm                      = bandCatNm;
		this.msiNm                          = msiNm;
		this.adsNm                          = adsNm;
		this.sourcingStrategyNm             = sourcingStrategyNm;
		this.altSourcingStrategyNm          = altSourcingStrategyNm;
		this.exceptionCd                    = exceptionCd;
		this.exceptionReasonTxt             = exceptionReasonTxt;
		this.exceptCd                       = exceptCd;
		this.effTms                         = effTms;
		this.expirTms                       = expirTms;
		this.intranetId						= intranetId;
		this.userType						= userType;
	}
    
	// Define Getters and Setters
	public int getChangeId() {
		return changeId;
	}
	public int getId() {
		return changeId;
	}
	public void setChangeId(int changeId) {
		this.changeId = changeId;
	}
	public Integer getCycleId() {
		return cycleId;
	}
	public void setCycleId(Integer cycleId) {
		this.cycleId = cycleId;
	}
	public String getChangeTypeCd() {
		return changeTypeCd;
	}
	public void setChangeTypeCd(String changeTypeCd) {
		this.changeTypeCd = changeTypeCd;
	}
	public Integer getBaseRowId() {
		return baseRowId;
	}
	public void setBaseRowId(Integer baseRowId) {
		this.baseRowId = baseRowId;
	}
	public Integer getProposedRowId() {
		return proposedRowId;
	}
	public void setProposedRowId(Integer proposedRowId) {
		this.proposedRowId = proposedRowId;
	}
	public String getEsvfId() {
		return esvfId;
	}
	public void setEsvfId(String esvfId) {
		this.esvfId = esvfId;
	}
	public String getEditorIntranetId() {
		return editorIntranetId;
	}
	public void setEditorIntranetId(String editorIntranetId) {
		this.editorIntranetId = editorIntranetId;
	}
	public Timestamp getChangeTms() {
		return changeTms;
	}
	public void setChangeTms(Timestamp changeTms) {
		this.changeTms = changeTms;
	}
	public String getPrimaryAprvlStatus() {
		return primaryAprvlStatus;
	}
	public void setPrimaryAprvlStatus(String primaryAprvlStatus) {
		this.primaryAprvlStatus = primaryAprvlStatus;
	}
	public String getPrimaryAprvrId() {
		return primaryAprvrId;
	}
	public void setPrimaryAprvrId(String primaryAprvrId) {
		this.primaryAprvrId = primaryAprvrId;
	}
	public Timestamp getPrimaryAprvlTms() {
		return primaryAprvlTms;
	}
	public void setPrimaryAprvlTms(Timestamp primaryAprvlTms) {
		this.primaryAprvlTms = primaryAprvlTms;
	}
	public String getSecondaryAprvlStatus() {
		return secondaryAprvlStatus;
	}
	public void setSecondaryAprvlStatus(String secondaryAprvlStatus) {
		this.secondaryAprvlStatus = secondaryAprvlStatus;
	}
	public String getSecondaryAprvrId() {
		return secondaryAprvrId;
	}
	public void setSecondaryAprvrId(String secondaryAprvrId) {
		this.secondaryAprvrId = secondaryAprvrId;
	}
	public Timestamp getSecondaryAprvlTms() {
		return secondaryAprvlTms;
	}
	public void setSecondaryAprvlTms(Timestamp secondaryAprvlTms) {
		this.secondaryAprvlTms = secondaryAprvlTms;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getAprvlCd() {
		return aprvlCd;
	}
	public void setAprvlCd(String aprvlCd) {
		this.aprvlCd = aprvlCd;
	}
	public String getGeographyTypeNm() {
		return geographyTypeNm;
	}
	public void setGeographyTypeNm(String geographyTypeNm) {
		this.geographyTypeNm = geographyTypeNm;
	}
	public String getGeographyNm() {
		return geographyNm;
	}
	public void setGeographyNm(String geographyNm) {
		this.geographyNm = geographyNm;
	}
	public String getMarketNm() {
		return marketNm;
	}
	public void setMarketNm(String marketNm) {
		this.marketNm = marketNm;
	}
	public String getMarketRegionNm() {
		return marketRegionNm;
	}
	public void setMarketRegionNm(String marketRegionNm) {
		this.marketRegionNm = marketRegionNm;
	}
	public String getCountryNm() {
		return countryNm;
	}
	public void setCountryNm(String countryNm) {
		this.countryNm = countryNm;
	}
	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}
	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getPracticeNm() {
		return practiceNm;
	}
	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
	public String getJrsId() {
		return jrsId;
	}
	public void setJrsId(String jrsId) {
		this.jrsId = jrsId;
	}
	public String getBandCatNm() {
		return bandCatNm;
	}
	public void setBandCatNm(String bandCatNm) {
		this.bandCatNm = bandCatNm;
	}
	public String getMsiNm() {
		return msiNm;
	}
	public void setMsiNm(String msiNm) {
		this.msiNm = msiNm;
	}
	public String getAdsNm() {
		return adsNm;
	}
	public void setAdsNm(String adsNm) {
		this.adsNm = adsNm;
	}
	public String getSourcingStrategyNm() {
		return sourcingStrategyNm;
	}
	public void setSourcingStrategyNm(String sourcingStrategyNm) {
		this.sourcingStrategyNm = sourcingStrategyNm;
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

	public String getRequestStatusCd() {
		return requestStatusCd;
	}

	public void setRequestStatusCd(String requestStatusCd) {
		this.requestStatusCd = requestStatusCd;
	}

	public String getIntranetId() {
		return intranetId;
	}

	public void setIntranetId(String intranetId) {
		this.intranetId = intranetId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getExceptCd() {
		return exceptCd;
	}

	public void setExceptCd(int exceptCd) {
		this.exceptCd = exceptCd;
	}

	public String getAprvrId() {
		return aprvrId;
	}

	public void setAprvrId(String aprvrId) {
		this.aprvrId = aprvrId;
	}

	public Timestamp getAprvlTms() {
		return aprvlTms;
	}

	public void setAprvlTms(Timestamp aprvlTms) {
		this.aprvlTms = aprvlTms;
	}

	public String getAltSourcingStrategyNm() {
		return altSourcingStrategyNm;
	}

	public void setAltSourcingStrategyNm(String altSourcingStrategyNm) {
		this.altSourcingStrategyNm = altSourcingStrategyNm;
	}

	public String getExceptionCd() {
		return exceptionCd;
	}

	public void setExceptionCd(String exceptionCd) {
		this.exceptionCd = exceptionCd;
	}

	public String getExceptionReasonTxt() {
		return exceptionReasonTxt;
	}

	public void setExceptionReasonTxt(String exceptionReasonTxt) {
		this.exceptionReasonTxt = exceptionReasonTxt;
	}
}