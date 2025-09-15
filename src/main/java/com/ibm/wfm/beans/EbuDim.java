package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="EbuDim",baseTableName="METRICS.EBU")
public class EbuDim {
	//@DbColumn(columnName="EBU_ID",isId=true)
	//private int        ebuId;
	@DbColumn(columnName="EBU_CD",keySeq=1)
	private String     ebuCd;
	@DbColumn(columnName="EBU_DESC")
	private String     ebuDesc;
	@DbColumn(columnName="GD_FLG")
	private String     gdFlg;
	@DbColumn(columnName="RCC_FLG")
	private String     rccFlg;
	@DbColumn(columnName="ONSITE_FLG")
	private String     onsiteFlg;
	@DbColumn(columnName="COC_FLG")
	private String     cocFlg;
	@DbColumn(columnName="AIS_FLG")
	private String     aisFlg;
	@DbColumn(columnName="SCTR_KEY")
	private int        sctrKey;
	@DbColumn(columnName="SCTR_NM")
	private String     sctrNm;
	@DbColumn(columnName="SCTR_SHRT_NM")
	private String     sctrShrtNm;
	@DbColumn(columnName="PRCTC_KEY")
	private int        prctcKey;
	@DbColumn(columnName="PRCTC_NM")
	private String     prctcNm;
	@DbColumn(columnName="BUS_AREA_KEY")
	private int        busAreaKey;
	@DbColumn(columnName="BUS_AREA_NM")
	private String     busAreaNm;
	@DbColumn(columnName="SVC_KEY")
	private int        svcKey;
	@DbColumn(columnName="SVC_NM")
	private String     svcNm;
	@DbColumn(columnName="SVC_SHRT_NM")
	private String     svcShrtNm;
	@DbColumn(columnName="COC_KEY")
	private int        cocKey;
	@DbColumn(columnName="COC_NM")
	private String     cocNm;
	@DbColumn(columnName="ONSITE_CTRY_KEY")
	private int        onsiteCtryKey;
	@DbColumn(columnName="ONSITE_ISO_CTRY_CD")
	private String     onsiteIsoCtryCd;
	@DbColumn(columnName="VIRT_ORG_LVL_0_FLG")
	private String     virtOrgLvl0Flg;
	@DbColumn(columnName="VIRT_ORG_LVL_0_TXT")
	private String     virtOrgLvl0Txt;
	@DbColumn(columnName="VIRT_ORG_LVL_1_FLG")
	private String     virtOrgLvl1Flg;
	@DbColumn(columnName="VIRT_ORG_LVL_1_TXT")
	private String     virtOrgLvl1Txt;
	@DbColumn(columnName="LOB_CD")
	private String     lobCd;
	@DbColumn(columnName="HCCM_FLG")
	private String     hccmFlg;
	@DbColumn(columnName="HCAM_FLG")
	private String     hcamFlg;
	@DbColumn(columnName="SIH_FLG")
	private String     sihFlg;
	@DbColumn(columnName="WW_HQ_FLG")
	private String     wwHqFlg;
	@DbColumn(columnName="OWNG_BUS_MEAS_DIV_CD")
	private String     owngBusMeasDivCd;
	@DbColumn(columnName="VIRT_ORG_LVL_3_FLG")
	private String     virtOrgLvl3Flg;
	@DbColumn(columnName="VIRT_ORG_LVL_3_TXT")
	private String     virtOrgLvl3Txt;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public EbuDim () {}
	
	// Define base constructor
	public EbuDim (
      String     ebuCd
    , String     ebuDesc
    , String     gdFlg
    , String     rccFlg
    , String     onsiteFlg
    , String     cocFlg
    , String     aisFlg
    , int        sctrKey
    , String     sctrNm
    , String     sctrShrtNm
    , int        prctcKey
    , String     prctcNm
    , int        busAreaKey
    , String     busAreaNm
    , int        svcKey
    , String     svcNm
    , String     svcShrtNm
    , int        cocKey
    , String     cocNm
    , int        onsiteCtryKey
    , String     onsiteIsoCtryCd
    , String     virtOrgLvl0Flg
    , String     virtOrgLvl0Txt
    , String     virtOrgLvl1Flg
    , String     virtOrgLvl1Txt
    , String     lobCd
    , String     hccmFlg
    , String     hcamFlg
    , String     sihFlg
    , String     wwHqFlg
    , String     owngBusMeasDivCd
    , String     virtOrgLvl3Flg
    , String     virtOrgLvl3Txt
    , String     actvFlg
	) {
		this.ebuCd                          = ebuCd;
		this.ebuDesc                        = ebuDesc;
		this.gdFlg                          = gdFlg;
		this.rccFlg                         = rccFlg;
		this.onsiteFlg                      = onsiteFlg;
		this.cocFlg                         = cocFlg;
		this.aisFlg                         = aisFlg;
		this.sctrKey                        = sctrKey;
		this.sctrNm                         = sctrNm;
		this.sctrShrtNm                     = sctrShrtNm;
		this.prctcKey                       = prctcKey;
		this.prctcNm                        = prctcNm;
		this.busAreaKey                     = busAreaKey;
		this.busAreaNm                      = busAreaNm;
		this.svcKey                         = svcKey;
		this.svcNm                          = svcNm;
		this.svcShrtNm                      = svcShrtNm;
		this.cocKey                         = cocKey;
		this.cocNm                          = cocNm;
		this.onsiteCtryKey                  = onsiteCtryKey;
		this.onsiteIsoCtryCd                = onsiteIsoCtryCd;
		this.virtOrgLvl0Flg                 = virtOrgLvl0Flg;
		this.virtOrgLvl0Txt                 = virtOrgLvl0Txt;
		this.virtOrgLvl1Flg                 = virtOrgLvl1Flg;
		this.virtOrgLvl1Txt                 = virtOrgLvl1Txt;
		this.lobCd                          = lobCd;
		this.hccmFlg                        = hccmFlg;
		this.hcamFlg                        = hcamFlg;
		this.sihFlg                         = sihFlg;
		this.wwHqFlg                        = wwHqFlg;
		this.owngBusMeasDivCd               = owngBusMeasDivCd;
		this.virtOrgLvl3Flg                 = virtOrgLvl3Flg;
		this.virtOrgLvl3Txt                 = virtOrgLvl3Txt;
		this.actvFlg                        = actvFlg;
	}
    
	// Define full constructor
	/*
	public EbuDim (
		  int        ebuId
		, String     ebuCd
		, String     ebuDesc
		, String     gdFlg
		, String     rccFlg
		, String     onsiteFlg
		, String     cocFlg
		, String     aisFlg
		, int        sctrKey
		, String     sctrNm
		, String     sctrShrtNm
		, int        prctcKey
		, String     prctcNm
		, int        busAreaKey
		, String     busAreaNm
		, int        svcKey
		, String     svcNm
		, String     svcShrtNm
		, int        cocKey
		, String     cocNm
		, int        onsiteCtryKey
		, String     onsiteIsoCtryCd
		, String     virtOrgLvl0Flg
		, String     virtOrgLvl0Txt
		, String     virtOrgLvl1Flg
		, String     virtOrgLvl1Txt
		, String     lobCd
		, String     hccmFlg
		, String     hcamFlg
		, String     sihFlg
		, String     wwHqFlg
		, String     owngBusMeasDivCd
		, String     virtOrgLvl3Flg
		, String     virtOrgLvl3Txt
		, String     actvFlg
	) {
		//this.ebuId                          = ebuId;
		this.ebuCd                          = ebuCd;
		this.ebuDesc                        = ebuDesc;
		this.gdFlg                          = gdFlg;
		this.rccFlg                         = rccFlg;
		this.onsiteFlg                      = onsiteFlg;
		this.cocFlg                         = cocFlg;
		this.aisFlg                         = aisFlg;
		this.sctrKey                        = sctrKey;
		this.sctrNm                         = sctrNm;
		this.sctrShrtNm                     = sctrShrtNm;
		this.prctcKey                       = prctcKey;
		this.prctcNm                        = prctcNm;
		this.busAreaKey                     = busAreaKey;
		this.busAreaNm                      = busAreaNm;
		this.svcKey                         = svcKey;
		this.svcNm                          = svcNm;
		this.svcShrtNm                      = svcShrtNm;
		this.cocKey                         = cocKey;
		this.cocNm                          = cocNm;
		this.onsiteCtryKey                  = onsiteCtryKey;
		this.onsiteIsoCtryCd                = onsiteIsoCtryCd;
		this.virtOrgLvl0Flg                 = virtOrgLvl0Flg;
		this.virtOrgLvl0Txt                 = virtOrgLvl0Txt;
		this.virtOrgLvl1Flg                 = virtOrgLvl1Flg;
		this.virtOrgLvl1Txt                 = virtOrgLvl1Txt;
		this.lobCd                          = lobCd;
		this.hccmFlg                        = hccmFlg;
		this.hcamFlg                        = hcamFlg;
		this.sihFlg                         = sihFlg;
		this.wwHqFlg                        = wwHqFlg;
		this.owngBusMeasDivCd               = owngBusMeasDivCd;
		this.virtOrgLvl3Flg                 = virtOrgLvl3Flg;
		this.virtOrgLvl3Txt                 = virtOrgLvl3Txt;
		this.actvFlg                        = actvFlg;
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		EbuDim other = (EbuDim) obj;
		if (
            this.ebuCd.equals(other.getEbuCd())
         && this.ebuDesc.equals(other.getEbuDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ebuCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ebuDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.gdFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.rccFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsiteFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.aisFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.prctcKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.prctcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.busAreaKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.busAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.cocNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsiteCtryKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsiteIsoCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl0Flg))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl0Txt))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl1Flg))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl1Txt))
        + "," + Helpers.formatCsvField(String.valueOf(this.lobCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.hccmFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.hcamFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.sihFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.wwHqFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.owngBusMeasDivCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl3Flg))
        + "," + Helpers.formatCsvField(String.valueOf(this.virtOrgLvl3Txt))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("EBU_CD")
        + "," + Helpers.formatCsvField("EBU_DESC")
        + "," + Helpers.formatCsvField("GD_FLG")
        + "," + Helpers.formatCsvField("RCC_FLG")
        + "," + Helpers.formatCsvField("ONSITE_FLG")
        + "," + Helpers.formatCsvField("COC_FLG")
        + "," + Helpers.formatCsvField("AIS_FLG")
        + "," + Helpers.formatCsvField("SCTR_KEY")
        + "," + Helpers.formatCsvField("SCTR_NM")
        + "," + Helpers.formatCsvField("SCTR_SHRT_NM")
        + "," + Helpers.formatCsvField("PRCTC_KEY")
        + "," + Helpers.formatCsvField("PRCTC_NM")
        + "," + Helpers.formatCsvField("BUS_AREA_KEY")
        + "," + Helpers.formatCsvField("BUS_AREA_NM")
        + "," + Helpers.formatCsvField("SVC_KEY")
        + "," + Helpers.formatCsvField("SVC_NM")
        + "," + Helpers.formatCsvField("SVC_SHRT_NM")
        + "," + Helpers.formatCsvField("COC_KEY")
        + "," + Helpers.formatCsvField("COC_NM")
        + "," + Helpers.formatCsvField("ONSITE_CTRY_KEY")
        + "," + Helpers.formatCsvField("ONSITE_ISO_CTRY_CD")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_0_FLG")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_0_TXT")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_1_FLG")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_1_TXT")
        + "," + Helpers.formatCsvField("LOB_CD")
        + "," + Helpers.formatCsvField("HCCM_FLG")
        + "," + Helpers.formatCsvField("HCAM_FLG")
        + "," + Helpers.formatCsvField("SIH_FLG")
        + "," + Helpers.formatCsvField("WW_HQ_FLG")
        + "," + Helpers.formatCsvField("OWNG_BUS_MEAS_DIV_CD")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_3_FLG")
        + "," + Helpers.formatCsvField("VIRT_ORG_LVL_3_TXT")
        + "," + Helpers.formatCsvField("ACTV_FLG")
		;
	}
    
	// Define Getters and Setters
	//public int getEbuId() {
	//	return ebuId;
	//}
	//public void setEbuId(int ebuId) {
	//	this.ebuId = ebuId;
	//}
	public String getEbuCd() {
		return ebuCd;
	}
	public void setEbuCd(String ebuCd) {
		this.ebuCd = ebuCd;
	}
	public String getEbuDesc() {
		return ebuDesc;
	}
	public void setEbuDesc(String ebuDesc) {
		this.ebuDesc = ebuDesc;
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
	public String getOnsiteFlg() {
		return onsiteFlg;
	}
	public void setOnsiteFlg(String onsiteFlg) {
		this.onsiteFlg = onsiteFlg;
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
	public int getPrctcKey() {
		return prctcKey;
	}
	public void setPrctcKey(int prctcKey) {
		this.prctcKey = prctcKey;
	}
	public String getPrctcNm() {
		return prctcNm;
	}
	public void setPrctcNm(String prctcNm) {
		this.prctcNm = prctcNm;
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
	public int getSvcKey() {
		return svcKey;
	}
	public void setSvcKey(int svcKey) {
		this.svcKey = svcKey;
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
	public int getOnsiteCtryKey() {
		return onsiteCtryKey;
	}
	public void setOnsiteCtryKey(int onsiteCtryKey) {
		this.onsiteCtryKey = onsiteCtryKey;
	}
	public String getOnsiteIsoCtryCd() {
		return onsiteIsoCtryCd;
	}
	public void setOnsiteIsoCtryCd(String onsiteIsoCtryCd) {
		this.onsiteIsoCtryCd = onsiteIsoCtryCd;
	}
	public String getVirtOrgLvl0Flg() {
		return virtOrgLvl0Flg;
	}
	public void setVirtOrgLvl0Flg(String virtOrgLvl0Flg) {
		this.virtOrgLvl0Flg = virtOrgLvl0Flg;
	}
	public String getVirtOrgLvl0Txt() {
		return virtOrgLvl0Txt;
	}
	public void setVirtOrgLvl0Txt(String virtOrgLvl0Txt) {
		this.virtOrgLvl0Txt = virtOrgLvl0Txt;
	}
	public String getVirtOrgLvl1Flg() {
		return virtOrgLvl1Flg;
	}
	public void setVirtOrgLvl1Flg(String virtOrgLvl1Flg) {
		this.virtOrgLvl1Flg = virtOrgLvl1Flg;
	}
	public String getVirtOrgLvl1Txt() {
		return virtOrgLvl1Txt;
	}
	public void setVirtOrgLvl1Txt(String virtOrgLvl1Txt) {
		this.virtOrgLvl1Txt = virtOrgLvl1Txt;
	}
	public String getLobCd() {
		return lobCd;
	}
	public void setLobCd(String lobCd) {
		this.lobCd = lobCd;
	}
	public String getHccmFlg() {
		return hccmFlg;
	}
	public void setHccmFlg(String hccmFlg) {
		this.hccmFlg = hccmFlg;
	}
	public String getHcamFlg() {
		return hcamFlg;
	}
	public void setHcamFlg(String hcamFlg) {
		this.hcamFlg = hcamFlg;
	}
	public String getSihFlg() {
		return sihFlg;
	}
	public void setSihFlg(String sihFlg) {
		this.sihFlg = sihFlg;
	}
	public String getWwHqFlg() {
		return wwHqFlg;
	}
	public void setWwHqFlg(String wwHqFlg) {
		this.wwHqFlg = wwHqFlg;
	}
	public String getOwngBusMeasDivCd() {
		return owngBusMeasDivCd;
	}
	public void setOwngBusMeasDivCd(String owngBusMeasDivCd) {
		this.owngBusMeasDivCd = owngBusMeasDivCd;
	}
	public String getVirtOrgLvl3Flg() {
		return virtOrgLvl3Flg;
	}
	public void setVirtOrgLvl3Flg(String virtOrgLvl3Flg) {
		this.virtOrgLvl3Flg = virtOrgLvl3Flg;
	}
	public String getVirtOrgLvl3Txt() {
		return virtOrgLvl3Txt;
	}
	public void setVirtOrgLvl3Txt(String virtOrgLvl3Txt) {
		this.virtOrgLvl3Txt = virtOrgLvl3Txt;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
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