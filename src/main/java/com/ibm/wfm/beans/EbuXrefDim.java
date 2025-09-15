package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.ExcelSheet;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="EbuXrefDim",baseTableName="REFT.EBU_XREF")
public class EbuXrefDim extends NaryTreeNode {
	@DbColumn(columnName="EBU_ID",isId=true)
	private int        ebuId;
	@DbColumn(columnName="EBU_CD",keySeq=1)
	@ExcelSheet(columnName="Ebu Cd",columnNum=0)	
	private String     ebuCd;
	@DbColumn(columnName="STATUS")
	@ExcelSheet(columnName="EBU_STATUS",columnNum=6)	
	private String     status;
	@DbColumn(columnName="EBU_DESC")
	@ExcelSheet(columnName="EBU Desc",columnNum=1)	
	private String     ebuDesc;
	@DbColumn(columnName="ORG_MODEL_CATEGORY")
	@ExcelSheet(columnName="Org Model Category",columnNum=7)	
	private String     orgModelCategory;
	@DbColumn(columnName="GBS_EBU_CATEGORY")
	@ExcelSheet(columnName="GBS EBU Category",columnNum=8)	
	private String     gbsEbuCategory;
	@DbColumn(columnName="BRAND")
	@ExcelSheet(columnName="EMF LoB",columnNum=9)	
	private String     brand;
	@DbColumn(columnName="GBS_GROWTH_PLATFORM")
	@ExcelSheet(columnName="GBS Growth Platform",columnNum=10)	
	private String     gbsGrowthPlatform;
	@DbColumn(columnName="SERVICE_GROUP")
	@ExcelSheet(columnName="Service Group",columnNum=11)	
	private String     serviceGroup;
	@DbColumn(columnName="LABOR_TYPE_NM")
	@ExcelSheet(columnName="Labor Type",columnNum=12)	
	private String     laborTypeNm;
	@DbColumn(columnName="LABOR_TYPE_CD")
	@ExcelSheet(columnName="Labor Type Code",columnNum=13)	
	private String     laborTypeCd;
	@DbColumn(columnName="GD_FLG")
	@ExcelSheet(columnName="Gd Flg",columnNum=14)	
	private String     gdFlg;
	@DbColumn(columnName="RCC_FLG")
	@ExcelSheet(columnName="Rcc Flg",columnNum=15)	
	private String     rccFlg;
	@DbColumn(columnName="GD_LANDD_FLG")
	@ExcelSheet(columnName="Gd Landd Flg",columnNum=16)	
	private String     gdLanddFlg;
	@DbColumn(columnName="COC_FLG")
	@ExcelSheet(columnName="Coc Flg",columnNum=17)	
	private String     cocFlg;
	@DbColumn(columnName="AIS_FLG")
	@ExcelSheet(columnName="Ais Flg",columnNum=18)	
	private String     aisFlg;
	@DbColumn(columnName="BTO_FLG")
	@ExcelSheet(columnName="BTO?",columnNum=19)	
	private String     btoFlg;
	@DbColumn(columnName="CSSD_FLG")
	@ExcelSheet(columnName="CSSD Flg",columnNum=20)	
	private String     cssdFlg;
	@DbColumn(columnName="INDUSTRY_KEY")
	@ExcelSheet(columnName="Industry Key",columnNum=21)	
	private String     industryKey;
	@DbColumn(columnName="INDUSTRY_NAME")
	@ExcelSheet(columnName="Industry Name",columnNum=22)	
	private String     industryName;
	@DbColumn(columnName="EXCEP_SCT_FLG")
	@ExcelSheet(columnName="Excep Sct Flg",columnNum=23)	
	private String     excepSctFlg;
	@DbColumn(columnName="SCTR_KEY")
	@ExcelSheet(columnName="Sctr Key",columnNum=24)	
	private int   sctrKey;
	@DbColumn(columnName="SCTR_NM")
	@ExcelSheet(columnName="Sctr Nm",columnNum=25)	
	private String     sctrNm;
	@DbColumn(columnName="SCTR_SHRT_NM")
	@ExcelSheet(columnName="Sctr Shrt Nm",columnNum=26)	
	private String     sctrShrtNm;
	@DbColumn(columnName="BUS_AREA_KEY")
	@ExcelSheet(columnName="Bus Area Key",columnNum=27)	
	private int   busAreaKey;
	@DbColumn(columnName="BUS_AREA_NM")
	@ExcelSheet(columnName="Bus Area Nm",columnNum=28)	
	private String     busAreaNm;
	@DbColumn(columnName="SVC_KEY_T")
	@ExcelSheet(columnName="Svc Key (T)",columnNum=29)	
	private int   svcKeyT;
	@DbColumn(columnName="SVC_NM")
	@ExcelSheet(columnName="Svc Nm",columnNum=30)	
	private String     svcNm;
	@DbColumn(columnName="SVC_SHRT_NM")
	@ExcelSheet(columnName="Svc Shrt Nm",columnNum=31)	
	private String     svcShrtNm;
	@DbColumn(columnName="COC_KEY")
	@ExcelSheet(columnName="Coc Key",columnNum=32)	
	private int   cocKey;
	@DbColumn(columnName="COC_NM")
	@ExcelSheet(columnName="Coc Nm",columnNum=33)	
	private String     cocNm;
	@DbColumn(columnName="GD_LANDD_CTRY_KEY")
	@ExcelSheet(columnName="Gd Landd Ctry Key",columnNum=34)	
	private int   gdLanddCtryKey;
	@DbColumn(columnName="CTRY_NM")
	@ExcelSheet(columnName="Ctry Nm",columnNum=35)	
	private String     ctryNm;
	@DbColumn(columnName="GD_LANDD_ISO_CTRY_CD")
	@ExcelSheet(columnName="Gd Landd Iso Ctry Cd",columnNum=36)	
	private String     gdLanddIsoCtryCd;
	@DbColumn(columnName="OWNING_BM_DIV")
	@ExcelSheet(columnName="Owning BM Div",columnNum=37)	
	private String     owningBmDiv;
	@DbColumn(columnName="DEFAULT_WW_TOP")
	@ExcelSheet(columnName="Default WW Top",columnNum=38)	
	private String     defaultWwTop;
	@DbColumn(columnName="DEFAULT_IOT_TOP")
	@ExcelSheet(columnName="Default IOT Top",columnNum=39)	
	private String     defaultIotTop;
	@DbColumn(columnName="HQG_FLG")
	@ExcelSheet(columnName="HQG",columnNum=40)	
	private String     hqgFlg;
	@DbColumn(columnName="IOS_FLG")
	@ExcelSheet(columnName="IOS",columnNum=41)	
	private String     iosFlg;
	@DbColumn(columnName="SOL_FLG")
	@ExcelSheet(columnName="SOL",columnNum=42)	
	private String     solFlg;
	@DbColumn(columnName="CTX_FLG")
	@ExcelSheet(columnName="CTX",columnNum=43)	
	private String     ctxFlg;
	@DbColumn(columnName="DTT_FLG")
	@ExcelSheet(columnName="DTT",columnNum=44)	
	private String     dttFlg;
	@DbColumn(columnName="FSC_FLG")
	@ExcelSheet(columnName="FSC",columnNum=45)	
	private String     fscFlg;
	@DbColumn(columnName="ITU_FLG")
	@ExcelSheet(columnName="ITU",columnNum=46)	
	private String     ituFlg;
	@DbColumn(columnName="TTX_FLG")
	@ExcelSheet(columnName="TTX",columnNum=47)	
	private String     ttxFlg;
	@DbColumn(columnName="HCM_FLG")
	@ExcelSheet(columnName="HCM",columnNum=48)	
	private String     hcmFlg;
	@DbColumn(columnName="HCT_FLG")
	@ExcelSheet(columnName="HCT",columnNum=49)	
	private String     hctFlg;
	@DbColumn(columnName="CSS_FLG")
	@ExcelSheet(columnName="CSS",columnNum=50)	
	private String     cssFlg;
	@DbColumn(columnName="LBR_MDL_CD")
	@ExcelSheet(columnName="LABOR_MODEL_CD",columnNum=51)	
	private String     lbrMdlCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public EbuXrefDim () {
		
	}
	
	// Define natural key constructor
	public EbuXrefDim (
      String     ebuCd
	) {
		this.ebuCd                          = ebuCd;
		
	}
	
	// Define base constructor
	public EbuXrefDim (
      String     ebuCd
    , String     status
    , String     ebuDesc
    , String     orgModelCategory
    , String     gbsEbuCategory
    , String     brand
    , String     gbsGrowthPlatform
    , String     serviceGroup
    , String     laborTypeNm
    , String     laborTypeCd
    , String     gdFlg
    , String     rccFlg
    , String     gdLanddFlg
    , String     cocFlg
    , String     aisFlg
    , String     btoFlg
    , String     cssdFlg
    , String     industryKey
    , String     industryName
    , String     excepSctFlg
    , int   sctrKey
    , String     sctrNm
    , String     sctrShrtNm
    , int   busAreaKey
    , String     busAreaNm
    , int   svcKeyT
    , String     svcNm
    , String     svcShrtNm
    , int   cocKey
    , String     cocNm
    , int   gdLanddCtryKey
    , String     ctryNm
    , String     gdLanddIsoCtryCd
    , String     owningBmDiv
    , String     defaultWwTop
    , String     defaultIotTop
    , String     hqgFlg
    , String     iosFlg
    , String     solFlg
    , String     ctxFlg
    , String     dttFlg
    , String     fscFlg
    , String     ituFlg
    , String     ttxFlg
    , String     hcmFlg
    , String     hctFlg
    , String     cssFlg
    , String     lbrMdlCd
	) {
		this.ebuCd                          = ebuCd;
		this.status                         = status;
		this.ebuDesc                        = ebuDesc;
		this.orgModelCategory               = orgModelCategory;
		this.gbsEbuCategory                 = gbsEbuCategory;
		this.brand                          = brand;
		this.gbsGrowthPlatform              = gbsGrowthPlatform;
		this.serviceGroup                   = serviceGroup;
		this.laborTypeNm                    = laborTypeNm;
		this.laborTypeCd                    = laborTypeCd;
		this.gdFlg                          = gdFlg;
		this.rccFlg                         = rccFlg;
		this.gdLanddFlg                     = gdLanddFlg;
		this.cocFlg                         = cocFlg;
		this.aisFlg                         = aisFlg;
		this.btoFlg                         = btoFlg;
		this.cssdFlg                        = cssdFlg;
		this.industryKey                    = industryKey;
		this.industryName                   = industryName;
		this.excepSctFlg                    = excepSctFlg;
		this.sctrKey                        = sctrKey;
		this.sctrNm                         = sctrNm;
		this.sctrShrtNm                     = sctrShrtNm;
		this.busAreaKey                     = busAreaKey;
		this.busAreaNm                      = busAreaNm;
		this.svcKeyT                        = svcKeyT;
		this.svcNm                          = svcNm;
		this.svcShrtNm                      = svcShrtNm;
		this.cocKey                         = cocKey;
		this.cocNm                          = cocNm;
		this.gdLanddCtryKey                 = gdLanddCtryKey;
		this.ctryNm                         = ctryNm;
		this.gdLanddIsoCtryCd               = gdLanddIsoCtryCd;
		this.owningBmDiv                    = owningBmDiv;
		this.defaultWwTop                   = defaultWwTop;
		this.defaultIotTop                  = defaultIotTop;
		this.hqgFlg                         = hqgFlg;
		this.iosFlg                         = iosFlg;
		this.solFlg                         = solFlg;
		this.ctxFlg                         = ctxFlg;
		this.dttFlg                         = dttFlg;
		this.fscFlg                         = fscFlg;
		this.ituFlg                         = ituFlg;
		this.ttxFlg                         = ttxFlg;
		this.hcmFlg                         = hcmFlg;
		this.hctFlg                         = hctFlg;
		this.cssFlg                         = cssFlg;
		this.lbrMdlCd                       = lbrMdlCd;
		
	}
    
	// Define full constructor
	public EbuXrefDim (
		  int        ebuId
		, String     ebuCd
		, String     status
		, String     ebuDesc
		, String     orgModelCategory
		, String     gbsEbuCategory
		, String     brand
		, String     gbsGrowthPlatform
		, String     serviceGroup
		, String     laborTypeNm
		, String     laborTypeCd
		, String     gdFlg
		, String     rccFlg
		, String     gdLanddFlg
		, String     cocFlg
		, String     aisFlg
		, String     btoFlg
		, String     cssdFlg
		, String     industryKey
		, String     industryName
		, String     excepSctFlg
		, int   sctrKey
		, String     sctrNm
		, String     sctrShrtNm
		, int   busAreaKey
		, String     busAreaNm
		, int   svcKeyT
		, String     svcNm
		, String     svcShrtNm
		, int   cocKey
		, String     cocNm
		, int   gdLanddCtryKey
		, String     ctryNm
		, String     gdLanddIsoCtryCd
		, String     owningBmDiv
		, String     defaultWwTop
		, String     defaultIotTop
		, String     hqgFlg
		, String     iosFlg
		, String     solFlg
		, String     ctxFlg
		, String     dttFlg
		, String     fscFlg
		, String     ituFlg
		, String     ttxFlg
		, String     hcmFlg
		, String     hctFlg
		, String     cssFlg
		, String     lbrMdlCd
	) {
		this.ebuId                          = ebuId;
		this.ebuCd                          = ebuCd;
		this.status                         = status;
		this.ebuDesc                        = ebuDesc;
		this.orgModelCategory               = orgModelCategory;
		this.gbsEbuCategory                 = gbsEbuCategory;
		this.brand                          = brand;
		this.gbsGrowthPlatform              = gbsGrowthPlatform;
		this.serviceGroup                   = serviceGroup;
		this.laborTypeNm                    = laborTypeNm;
		this.laborTypeCd                    = laborTypeCd;
		this.gdFlg                          = gdFlg;
		this.rccFlg                         = rccFlg;
		this.gdLanddFlg                     = gdLanddFlg;
		this.cocFlg                         = cocFlg;
		this.aisFlg                         = aisFlg;
		this.btoFlg                         = btoFlg;
		this.cssdFlg                        = cssdFlg;
		this.industryKey                    = industryKey;
		this.industryName                   = industryName;
		this.excepSctFlg                    = excepSctFlg;
		this.sctrKey                        = sctrKey;
		this.sctrNm                         = sctrNm;
		this.sctrShrtNm                     = sctrShrtNm;
		this.busAreaKey                     = busAreaKey;
		this.busAreaNm                      = busAreaNm;
		this.svcKeyT                        = svcKeyT;
		this.svcNm                          = svcNm;
		this.svcShrtNm                      = svcShrtNm;
		this.cocKey                         = cocKey;
		this.cocNm                          = cocNm;
		this.gdLanddCtryKey                 = gdLanddCtryKey;
		this.ctryNm                         = ctryNm;
		this.gdLanddIsoCtryCd               = gdLanddIsoCtryCd;
		this.owningBmDiv                    = owningBmDiv;
		this.defaultWwTop                   = defaultWwTop;
		this.defaultIotTop                  = defaultIotTop;
		this.hqgFlg                         = hqgFlg;
		this.iosFlg                         = iosFlg;
		this.solFlg                         = solFlg;
		this.ctxFlg                         = ctxFlg;
		this.dttFlg                         = dttFlg;
		this.fscFlg                         = fscFlg;
		this.ituFlg                         = ituFlg;
		this.ttxFlg                         = ttxFlg;
		this.hcmFlg                         = hcmFlg;
		this.hctFlg                         = hctFlg;
		this.cssFlg                         = cssFlg;
		this.lbrMdlCd                       = lbrMdlCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.ebuCd
		;
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		EbuXrefDim other = (EbuXrefDim) obj;
		if (
            this.ebuCd.equals(other.getEbuCd())
         && this.status.equals(other.getStatus())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ebuCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.status))
        + "," + Helpers.formatCsvField(String.valueOf(this.ebuDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgModelCategory))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsEbuCategory))
        + "," + Helpers.formatCsvField(String.valueOf(this.brand))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsGrowthPlatform))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceGroup))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborTypeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.laborTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gdFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.rccFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.gdLanddFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.aisFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.btoFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cssdFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.industryKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.industryName))
        + "," + Helpers.formatCsvField(String.valueOf(this.excepSctFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.busAreaKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.busAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcKeyT))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.gdLanddCtryKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.gdLanddIsoCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.owningBmDiv))
        + "," + Helpers.formatCsvField(String.valueOf(this.defaultWwTop))
        + "," + Helpers.formatCsvField(String.valueOf(this.defaultIotTop))
        + "," + Helpers.formatCsvField(String.valueOf(this.hqgFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.iosFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.solFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctxFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.dttFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.fscFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.ituFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.ttxFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.hcmFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.hctFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cssFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrMdlCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("EBU_CD")
        + "," + Helpers.formatCsvField("STATUS")
        + "," + Helpers.formatCsvField("EBU_DESC")
        + "," + Helpers.formatCsvField("ORG_MODEL_CATEGORY")
        + "," + Helpers.formatCsvField("GBS_EBU_CATEGORY")
        + "," + Helpers.formatCsvField("BRAND")
        + "," + Helpers.formatCsvField("GBS_GROWTH_PLATFORM")
        + "," + Helpers.formatCsvField("SERVICE_GROUP")
        + "," + Helpers.formatCsvField("LABOR_TYPE_NM")
        + "," + Helpers.formatCsvField("LABOR_TYPE_CD")
        + "," + Helpers.formatCsvField("GD_FLG")
        + "," + Helpers.formatCsvField("RCC_FLG")
        + "," + Helpers.formatCsvField("GD_LANDD_FLG")
        + "," + Helpers.formatCsvField("COC_FLG")
        + "," + Helpers.formatCsvField("AIS_FLG")
        + "," + Helpers.formatCsvField("BTO_FLG")
        + "," + Helpers.formatCsvField("CSSD_FLG")
        + "," + Helpers.formatCsvField("INDUSTRY_KEY")
        + "," + Helpers.formatCsvField("INDUSTRY_NAME")
        + "," + Helpers.formatCsvField("EXCEP_SCT_FLG")
        + "," + Helpers.formatCsvField("SCTR_KEY")
        + "," + Helpers.formatCsvField("SCTR_NM")
        + "," + Helpers.formatCsvField("SCTR_SHRT_NM")
        + "," + Helpers.formatCsvField("BUS_AREA_KEY")
        + "," + Helpers.formatCsvField("BUS_AREA_NM")
        + "," + Helpers.formatCsvField("SVC_KEY_T")
        + "," + Helpers.formatCsvField("SVC_NM")
        + "," + Helpers.formatCsvField("SVC_SHRT_NM")
        + "," + Helpers.formatCsvField("COC_KEY")
        + "," + Helpers.formatCsvField("COC_NM")
        + "," + Helpers.formatCsvField("GD_LANDD_CTRY_KEY")
        + "," + Helpers.formatCsvField("CTRY_NM")
        + "," + Helpers.formatCsvField("GD_LANDD_ISO_CTRY_CD")
        + "," + Helpers.formatCsvField("OWNING_BM_DIV")
        + "," + Helpers.formatCsvField("DEFAULT_WW_TOP")
        + "," + Helpers.formatCsvField("DEFAULT_IOT_TOP")
        + "," + Helpers.formatCsvField("HQG_FLG")
        + "," + Helpers.formatCsvField("IOS_FLG")
        + "," + Helpers.formatCsvField("SOL_FLG")
        + "," + Helpers.formatCsvField("CTX_FLG")
        + "," + Helpers.formatCsvField("DTT_FLG")
        + "," + Helpers.formatCsvField("FSC_FLG")
        + "," + Helpers.formatCsvField("ITU_FLG")
        + "," + Helpers.formatCsvField("TTX_FLG")
        + "," + Helpers.formatCsvField("HCM_FLG")
        + "," + Helpers.formatCsvField("HCT_FLG")
        + "," + Helpers.formatCsvField("CSS_FLG")
        + "," + Helpers.formatCsvField("LBR_MDL_CD")
		;
	}
    
	// Define Getters and Setters
	public int getEbuId() {
		return ebuId;
	}
	public void setEbuId(int ebuId) {
		this.ebuId = ebuId;
	}
	public String getEbuCd() {
		return ebuCd;
	}
	public void setEbuCd(String ebuCd) {
		this.ebuCd = ebuCd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEbuDesc() {
		return ebuDesc;
	}
	public void setEbuDesc(String ebuDesc) {
		this.ebuDesc = ebuDesc;
	}
	public String getOrgModelCategory() {
		return orgModelCategory;
	}
	public void setOrgModelCategory(String orgModelCategory) {
		this.orgModelCategory = orgModelCategory;
	}
	public String getGbsEbuCategory() {
		return gbsEbuCategory;
	}
	public void setGbsEbuCategory(String gbsEbuCategory) {
		this.gbsEbuCategory = gbsEbuCategory;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getGbsGrowthPlatform() {
		return gbsGrowthPlatform;
	}
	public void setGbsGrowthPlatform(String gbsGrowthPlatform) {
		this.gbsGrowthPlatform = gbsGrowthPlatform;
	}
	public String getServiceGroup() {
		return serviceGroup;
	}
	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}
	public String getLaborTypeNm() {
		return laborTypeNm;
	}
	public void setLaborTypeNm(String laborTypeNm) {
		this.laborTypeNm = laborTypeNm;
	}
	public String getLaborTypeCd() {
		return laborTypeCd;
	}
	public void setLaborTypeCd(String laborTypeCd) {
		this.laborTypeCd = laborTypeCd;
	}
	public String getGdFlg() {
		return gdFlg;
	}
	public void setGdFlg(String gdFlg) {
		this.gdFlg = gdFlg;
	}
	public String getRccFlg() {
		return rccFlg;
	}
	public void setRccFlg(String rccFlg) {
		this.rccFlg = rccFlg;
	}
	public String getGdLanddFlg() {
		return gdLanddFlg;
	}
	public void setGdLanddFlg(String gdLanddFlg) {
		this.gdLanddFlg = gdLanddFlg;
	}
	public String getCocFlg() {
		return cocFlg;
	}
	public void setCocFlg(String cocFlg) {
		this.cocFlg = cocFlg;
	}
	public String getAisFlg() {
		return aisFlg;
	}
	public void setAisFlg(String aisFlg) {
		this.aisFlg = aisFlg;
	}
	public String getBtoFlg() {
		return btoFlg;
	}
	public void setBtoFlg(String btoFlg) {
		this.btoFlg = btoFlg;
	}
	public String getCssdFlg() {
		return cssdFlg;
	}
	public void setCssdFlg(String cssdFlg) {
		this.cssdFlg = cssdFlg;
	}
	public String getIndustryKey() {
		return industryKey;
	}
	public void setIndustryKey(String industryKey) {
		this.industryKey = industryKey;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getExcepSctFlg() {
		return excepSctFlg;
	}
	public void setExcepSctFlg(String excepSctFlg) {
		this.excepSctFlg = excepSctFlg;
	}
	public int getSctrKey() {
		return sctrKey;
	}
	public void setSctrKey(int sctrKey) {
		this.sctrKey = sctrKey;
	}
	public String getSctrNm() {
		return sctrNm;
	}
	public void setSctrNm(String sctrNm) {
		this.sctrNm = sctrNm;
	}
	public String getSctrShrtNm() {
		return sctrShrtNm;
	}
	public void setSctrShrtNm(String sctrShrtNm) {
		this.sctrShrtNm = sctrShrtNm;
	}
	public int getBusAreaKey() {
		return busAreaKey;
	}
	public void setBusAreaKey(int busAreaKey) {
		this.busAreaKey = busAreaKey;
	}
	public String getBusAreaNm() {
		return busAreaNm;
	}
	public void setBusAreaNm(String busAreaNm) {
		this.busAreaNm = busAreaNm;
	}
	public int getSvcKeyT() {
		return svcKeyT;
	}
	public void setSvcKeyT(int svcKeyT) {
		this.svcKeyT = svcKeyT;
	}
	public String getSvcNm() {
		return svcNm;
	}
	public void setSvcNm(String svcNm) {
		this.svcNm = svcNm;
	}
	public String getSvcShrtNm() {
		return svcShrtNm;
	}
	public void setSvcShrtNm(String svcShrtNm) {
		this.svcShrtNm = svcShrtNm;
	}
	public int getCocKey() {
		return cocKey;
	}
	public void setCocKey(int cocKey) {
		this.cocKey = cocKey;
	}
	public String getCocNm() {
		return cocNm;
	}
	public void setCocNm(String cocNm) {
		this.cocNm = cocNm;
	}
	public int getGdLanddCtryKey() {
		return gdLanddCtryKey;
	}
	public void setGdLanddCtryKey(int gdLanddCtryKey) {
		this.gdLanddCtryKey = gdLanddCtryKey;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
	}
	public String getGdLanddIsoCtryCd() {
		return gdLanddIsoCtryCd;
	}
	public void setGdLanddIsoCtryCd(String gdLanddIsoCtryCd) {
		this.gdLanddIsoCtryCd = gdLanddIsoCtryCd;
	}
	public String getOwningBmDiv() {
		return owningBmDiv;
	}
	public void setOwningBmDiv(String owningBmDiv) {
		this.owningBmDiv = owningBmDiv;
	}
	public String getDefaultWwTop() {
		return defaultWwTop;
	}
	public void setDefaultWwTop(String defaultWwTop) {
		this.defaultWwTop = defaultWwTop;
	}
	public String getDefaultIotTop() {
		return defaultIotTop;
	}
	public void setDefaultIotTop(String defaultIotTop) {
		this.defaultIotTop = defaultIotTop;
	}
	public String getHqgFlg() {
		return hqgFlg;
	}
	public void setHqgFlg(String hqgFlg) {
		this.hqgFlg = hqgFlg;
	}
	public String getIosFlg() {
		return iosFlg;
	}
	public void setIosFlg(String iosFlg) {
		this.iosFlg = iosFlg;
	}
	public String getSolFlg() {
		return solFlg;
	}
	public void setSolFlg(String solFlg) {
		this.solFlg = solFlg;
	}
	public String getCtxFlg() {
		return ctxFlg;
	}
	public void setCtxFlg(String ctxFlg) {
		this.ctxFlg = ctxFlg;
	}
	public String getDttFlg() {
		return dttFlg;
	}
	public void setDttFlg(String dttFlg) {
		this.dttFlg = dttFlg;
	}
	public String getFscFlg() {
		return fscFlg;
	}
	public void setFscFlg(String fscFlg) {
		this.fscFlg = fscFlg;
	}
	public String getItuFlg() {
		return ituFlg;
	}
	public void setItuFlg(String ituFlg) {
		this.ituFlg = ituFlg;
	}
	public String getTtxFlg() {
		return ttxFlg;
	}
	public void setTtxFlg(String ttxFlg) {
		this.ttxFlg = ttxFlg;
	}
	public String getHcmFlg() {
		return hcmFlg;
	}
	public void setHcmFlg(String hcmFlg) {
		this.hcmFlg = hcmFlg;
	}
	public String getHctFlg() {
		return hctFlg;
	}
	public void setHctFlg(String hctFlg) {
		this.hctFlg = hctFlg;
	}
	public String getCssFlg() {
		return cssFlg;
	}
	public void setCssFlg(String cssFlg) {
		this.cssFlg = cssFlg;
	}
	public String getLbrMdlCd() {
		return lbrMdlCd;
	}
	public void setLbrMdlCd(String lbrMdlCd) {
		this.lbrMdlCd = lbrMdlCd;
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