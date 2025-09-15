package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="RahServiceAreaDim",baseTableName="METRICS.SERVICE_AREA")
public class RahServiceAreaDim extends NaryTreeNode {
	@DbColumn(columnName="SVC_AREA_KEY",isId=true)
	@ExcelSheet(columnName="svcAreaKey",columnNum=0)
	private int        svcAreaKey;
	@DbColumn(columnName="SVC_AREA_CD",keySeq=1)
	@ExcelSheet(columnName="svcAreaCd",columnNum=1)
	private String     svcAreaCd;
	@DbColumn(columnName="SVC_AREA_NM")
	@ExcelSheet(columnName="svcAreaNm",columnNum=2)
	private String     svcAreaNm;
	@DbColumn(columnName="PDT_SVC_AREA_315_ID")
	@ExcelSheet(columnName="pdtSvcArea315Id",columnNum=3)
	private int        pdtSvcArea315Id;
	@DbColumn(columnName="SVC_KEY")
	@ExcelSheet(columnName="svcKey",columnNum=4)
	private int        svcKey;
	@DbColumn(columnName="SVC_CD")
	@ExcelSheet(columnName="svcCd",columnNum=5)
	private String     svcCd;
	@DbColumn(columnName="SVC_NM")
	@ExcelSheet(columnName="svcNm",columnNum=6)
	private String     svcNm;
	@DbColumn(columnName="SVC_SHRT_NM")
	@ExcelSheet(columnName="svcShrtNm",columnNum=7)
	private String     svcShrtNm;
	@DbColumn(columnName="SVC_AREA_GRP_KEY")
	@ExcelSheet(columnName="svcAreaGrpKey",columnNum=8)
	private int        svcAreaGrpKey;
	@DbColumn(columnName="SVC_AREA_GRP_NM")
	@ExcelSheet(columnName="svcAreaGrpNm",columnNum=9)
	private String     svcAreaGrpNm;
	@DbColumn(columnName="ACTV_FLG")
	@ExcelSheet(columnName="actvFlg",columnNum=10)
	private String     actvFlg;
	@DbColumn(columnName="DSHB_SVC_AREA_GRP_SHRT_NM")
	@ExcelSheet(columnName="dshbSvcAreaGrpShrtNm",columnNum=11)
	private String     dshbSvcAreaGrpShrtNm;
	@DbColumn(columnName="PRCTC_KEY")
	@ExcelSheet(columnName="prctcKey",columnNum=12)
	private int        prctcKey;
	@DbColumn(columnName="PRCTC_NM")
	@ExcelSheet(columnName="prctcNm",columnNum=13)
	private String     prctcNm;
	@DbColumn(columnName="INNOVATN_UNIT_FLG")
	@ExcelSheet(columnName="innovatnUnitFlg",columnNum=14)
	private String     innovatnUnitFlg;
	@DbColumn(columnName="ACLRTD_PRCTC_FLG")
	@ExcelSheet(columnName="aclrtdPrctcFlg",columnNum=15)	
	private String     aclrtdPrctcFlg;

	// Define null constructor
	public RahServiceAreaDim () {
		
	}
	
	// Define natural key constructor
	public RahServiceAreaDim (
      String     svcAreaCd
	) {
		this.svcAreaCd                      = svcAreaCd;
		
	}
	
    
	// Define full constructor
	public RahServiceAreaDim (
		  int        svcAreaKey
		, String     svcAreaCd
		, String     svcAreaNm
		, int        pdtSvcArea315Id
		, int        svcKey
		, String     svcCd
		, String     svcNm
		, String     svcShrtNm
		, int        svcAreaGrpKey
		, String     svcAreaGrpNm
		, String     actvFlg
		, String     dshbSvcAreaGrpShrtNm
		, int        prctcKey
		, String     prctcNm
		, String     innovatnUnitFlg
		, String     aclrtdPrctcFlg
	) {
		this.svcAreaKey                     = svcAreaKey;
		this.svcAreaCd                      = svcAreaCd;
		this.svcAreaNm                      = svcAreaNm;
		this.pdtSvcArea315Id                = pdtSvcArea315Id;
		this.svcKey                         = svcKey;
		this.svcCd                          = svcCd;
		this.svcNm                          = svcNm;
		this.svcShrtNm                      = svcShrtNm;
		this.svcAreaGrpKey                  = svcAreaGrpKey;
		this.svcAreaGrpNm                   = svcAreaGrpNm;
		this.actvFlg                        = actvFlg;
		this.dshbSvcAreaGrpShrtNm           = dshbSvcAreaGrpShrtNm;
		this.prctcKey                       = prctcKey;
		this.prctcNm                        = prctcNm;
		this.innovatnUnitFlg                = innovatnUnitFlg;
		this.aclrtdPrctcFlg                 = aclrtdPrctcFlg;
		
	}
	
	@Override
	public String getCode() { 
		return this.svcAreaCd
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
                    
		RahServiceAreaDim other = (RahServiceAreaDim) obj;
		if (
            this.svcAreaCd.equals(other.getSvcAreaCd())
         && this.svcAreaNm.equals(other.getSvcAreaNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.svcAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.pdtSvcArea315Id))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcAreaGrpKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcAreaGrpNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.dshbSvcAreaGrpShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.prctcKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.prctcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.innovatnUnitFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.aclrtdPrctcFlg))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SVC_AREA_CD")
        + "," + Helpers.formatCsvField("SVC_AREA_NM")
        + "," + Helpers.formatCsvField("PDT_SVC_AREA_315_ID")
        + "," + Helpers.formatCsvField("SVC_KEY")
        + "," + Helpers.formatCsvField("SVC_CD")
        + "," + Helpers.formatCsvField("SVC_NM")
        + "," + Helpers.formatCsvField("SVC_SHRT_NM")
        + "," + Helpers.formatCsvField("SVC_AREA_GRP_KEY")
        + "," + Helpers.formatCsvField("SVC_AREA_GRP_NM")
        + "," + Helpers.formatCsvField("ACTV_FLG")
        + "," + Helpers.formatCsvField("DSHB_SVC_AREA_GRP_SHRT_NM")
        + "," + Helpers.formatCsvField("PRCTC_KEY")
        + "," + Helpers.formatCsvField("PRCTC_NM")
        + "," + Helpers.formatCsvField("INNOVATN_UNIT_FLG")
        + "," + Helpers.formatCsvField("ACLRTD_PRCTC_FLG")
		;
	}
    
	// Define Getters and Setters
	public int getSvcAreaKey() {
		return svcAreaKey;
	}
	public void setSvcAreaKey(int svcAreaKey) {
		this.svcAreaKey = svcAreaKey;
	}
	public String getSvcAreaCd() {
		return svcAreaCd;
	}
	public void setSvcAreaCd(String svcAreaCd) {
		this.svcAreaCd = svcAreaCd;
	}
	public String getSvcAreaNm() {
		return svcAreaNm;
	}
	public void setSvcAreaNm(String svcAreaNm) {
		this.svcAreaNm = svcAreaNm;
	}
	public int getPdtSvcArea315Id() {
		return pdtSvcArea315Id;
	}
	public void setPdtSvcArea315Id(int pdtSvcArea315Id) {
		this.pdtSvcArea315Id = pdtSvcArea315Id;
	}
	public int getSvcKey() {
		return svcKey;
	}
	public void setSvcKey(int svcKey) {
		this.svcKey = svcKey;
	}
	public String getSvcCd() {
		return svcCd;
	}
	public void setSvcCd(String svcCd) {
		this.svcCd = svcCd;
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
	public int getSvcAreaGrpKey() {
		return svcAreaGrpKey;
	}
	public void setSvcAreaGrpKey(int svcAreaGrpKey) {
		this.svcAreaGrpKey = svcAreaGrpKey;
	}
	public String getSvcAreaGrpNm() {
		return svcAreaGrpNm;
	}
	public void setSvcAreaGrpNm(String svcAreaGrpNm) {
		this.svcAreaGrpNm = svcAreaGrpNm;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
	}
	public String getDshbSvcAreaGrpShrtNm() {
		return dshbSvcAreaGrpShrtNm;
	}
	public void setDshbSvcAreaGrpShrtNm(String dshbSvcAreaGrpShrtNm) {
		this.dshbSvcAreaGrpShrtNm = dshbSvcAreaGrpShrtNm;
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
	public String getInnovatnUnitFlg() {
		return innovatnUnitFlg;
	}
	public void setInnovatnUnitFlg(String innovatnUnitFlg) {
		this.innovatnUnitFlg = innovatnUnitFlg;
	}
	public String getAclrtdPrctcFlg() {
		return aclrtdPrctcFlg;
	}
	public void setAclrtdPrctcFlg(String aclrtdPrctcFlg) {
		this.aclrtdPrctcFlg = aclrtdPrctcFlg;
	}
}