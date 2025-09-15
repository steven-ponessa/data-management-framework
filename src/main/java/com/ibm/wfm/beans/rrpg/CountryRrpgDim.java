package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CountryRrpgDim",baseTableName="RRPG.COUNTRY",parentBeanPackageName="com.ibm.wfm.beans.rrpg",parentBeanName="RegionRrpgDim",parentBaseTableName="RRPG.REGION")
public class CountryRrpgDim extends NaryTreeNode implements Comparable <CountryRrpgDim> {
	@DbColumn(columnName="CTRY_ID",isId=true)
	private int        ctryId;
	@DbColumn(columnName="CTRY_ISO_ID",keySeq=1)
	private String     ctryIsoId;
	@DbColumn(columnName="CTRY_CD")
	private String     ctryCd;
	@DbColumn(columnName="CTRY_ISO_CD")
	private String     ctryIsoCd;
	@DbColumn(columnName="LDGR_CTRY_CD")
	private String     ldgrCtryCd;
	@DbColumn(columnName="CTRY_NM")
	private String     ctryNm;
	@DbColumn(columnName="CTRY_DESC")
	private String     ctryDesc;
	@DbColumn(columnName="FIN_ACCT_STD8_IND")
	private String     finAcctStd8Ind;
	@DbColumn(columnName="CR_CROSS_CMPNY_IND")
	private String     crCrossCmpnyInd;
	@DbColumn(columnName="WW_CTRY_CD")
	private String     wwCtryCd;
	@DbColumn(columnName="CURR_CD")
	private String     currCd;
	@DbColumn(columnName="DATE_FMT_CD")
	private String     dateFmtCd;
	@DbColumn(columnName="DBCS_FLG")
	private String     dbcsFlg;
	@DbColumn(columnName="CIC_DOM_DLVRY_CNTR_IND")
	private String     cicDomDlvryCntrInd;
	@DbColumn(columnName="CIC_GBL_DLVRY_CNTR_IND")
	private String     cicGblDlvryCntrInd;
	@DbColumn(columnName="MRKT_CD")
	private String     mrktCd;
	@DbColumn(columnName="RGN_CD",foreignKeySeq=1)
	private String     rgnCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CountryRrpgDim () {
		
	}
	
	// Define natural key constructor
	public CountryRrpgDim (
      String     ctryIsoId
	) {
		this.ctryIsoId                      = ctryIsoId;
		
	}
	
	// Define base constructor
	public CountryRrpgDim (
      String     ctryIsoId
    , String     ctryCd
    , String     ctryIsoCd
    , String     ldgrCtryCd
    , String     ctryNm
    , String     ctryDesc
    , String     finAcctStd8Ind
    , String     crCrossCmpnyInd
    , String     wwCtryCd
    , String     currCd
    , String     dateFmtCd
    , String     dbcsFlg
    , String     cicDomDlvryCntrInd
    , String     cicGblDlvryCntrInd
    , String     mrktCd
    , String     rgnCd
	) {
		this.ctryIsoId                      = ctryIsoId;
		this.ctryCd                         = ctryCd;
		this.ctryIsoCd                      = ctryIsoCd;
		this.ldgrCtryCd                     = ldgrCtryCd;
		this.ctryNm                         = ctryNm;
		this.ctryDesc                       = ctryDesc;
		this.finAcctStd8Ind                 = finAcctStd8Ind;
		this.crCrossCmpnyInd                = crCrossCmpnyInd;
		this.wwCtryCd                       = wwCtryCd;
		this.currCd                         = currCd;
		this.dateFmtCd                      = dateFmtCd;
		this.dbcsFlg                        = dbcsFlg;
		this.cicDomDlvryCntrInd             = cicDomDlvryCntrInd;
		this.cicGblDlvryCntrInd             = cicGblDlvryCntrInd;
		this.mrktCd                         = mrktCd;
		this.rgnCd                          = rgnCd;
		
	}
    
	// Define full constructor
	public CountryRrpgDim (
		  int        ctryId
		, String     ctryIsoId
		, String     ctryCd
		, String     ctryIsoCd
		, String     ldgrCtryCd
		, String     ctryNm
		, String     ctryDesc
		, String     finAcctStd8Ind
		, String     crCrossCmpnyInd
		, String     wwCtryCd
		, String     currCd
		, String     dateFmtCd
		, String     dbcsFlg
		, String     cicDomDlvryCntrInd
		, String     cicGblDlvryCntrInd
		, String     mrktCd
		, String     rgnCd
	) {
		this.ctryId                         = ctryId;
		this.ctryIsoId                      = ctryIsoId;
		this.ctryCd                         = ctryCd;
		this.ctryIsoCd                      = ctryIsoCd;
		this.ldgrCtryCd                     = ldgrCtryCd;
		this.ctryNm                         = ctryNm;
		this.ctryDesc                       = ctryDesc;
		this.finAcctStd8Ind                 = finAcctStd8Ind;
		this.crCrossCmpnyInd                = crCrossCmpnyInd;
		this.wwCtryCd                       = wwCtryCd;
		this.currCd                         = currCd;
		this.dateFmtCd                      = dateFmtCd;
		this.dbcsFlg                        = dbcsFlg;
		this.cicDomDlvryCntrInd             = cicDomDlvryCntrInd;
		this.cicGblDlvryCntrInd             = cicGblDlvryCntrInd;
		this.mrktCd                         = mrktCd;
		this.rgnCd                          = rgnCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.ctryIsoId
		;
	}
	public String getDescription() { 
		return this.ctryDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CountryRrpgDim other = (CountryRrpgDim) obj;
		if (
            this.ctryIsoId.equals(other.getCtryIsoId())
         && this.ctryCd.equals(other.getCtryCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ctryIsoId))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryIsoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ldgrCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.finAcctStd8Ind))
        + "," + Helpers.formatCsvField(String.valueOf(this.crCrossCmpnyInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wwCtryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.currCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dateFmtCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dbcsFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicDomDlvryCntrInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicGblDlvryCntrInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CTRY_ISO_ID")
        + "," + Helpers.formatCsvField("CTRY_CD")
        + "," + Helpers.formatCsvField("CTRY_ISO_CD")
        + "," + Helpers.formatCsvField("LDGR_CTRY_CD")
        + "," + Helpers.formatCsvField("CTRY_NM")
        + "," + Helpers.formatCsvField("CTRY_DESC")
        + "," + Helpers.formatCsvField("FIN_ACCT_STD8_IND")
        + "," + Helpers.formatCsvField("CR_CROSS_CMPNY_IND")
        + "," + Helpers.formatCsvField("WW_CTRY_CD")
        + "," + Helpers.formatCsvField("CURR_CD")
        + "," + Helpers.formatCsvField("DATE_FMT_CD")
        + "," + Helpers.formatCsvField("DBCS_FLG")
        + "," + Helpers.formatCsvField("CIC_DOM_DLVRY_CNTR_IND")
        + "," + Helpers.formatCsvField("CIC_GBL_DLVRY_CNTR_IND")
        + "," + Helpers.formatCsvField("MRKT_CD")
        + "," + Helpers.formatCsvField("RGN_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCtryId() {
		return ctryId;
	}
	public void setCtryId(int ctryId) {
		this.ctryId = ctryId;
	}
	public String getCtryIsoId() {
		return ctryIsoId;
	}
	public void setCtryIsoId(String ctryIsoId) {
		this.ctryIsoId = ctryIsoId;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getLdgrCtryCd() {
		return ldgrCtryCd;
	}
	public void setLdgrCtryCd(String ldgrCtryCd) {
		this.ldgrCtryCd = ldgrCtryCd;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
	}
	public String getCtryDesc() {
		return ctryDesc;
	}
	public void setCtryDesc(String ctryDesc) {
		this.ctryDesc = ctryDesc;
	}
	public String getCtryIsoCd() {
		return ctryIsoCd;
	}
	public void setCtryIsoCd(String ctryIsoCd) {
		this.ctryIsoCd = ctryIsoCd;
	}
	public String getFinAcctStd8Ind() {
		return finAcctStd8Ind;
	}
	public void setFinAcctStd8Ind(String finAcctStd8Ind) {
		this.finAcctStd8Ind = finAcctStd8Ind;
	}
	public String getCrCrossCmpnyInd() {
		return crCrossCmpnyInd;
	}
	public void setCrCrossCmpnyInd(String crCrossCmpnyInd) {
		this.crCrossCmpnyInd = crCrossCmpnyInd;
	}
	public String getWwCtryCd() {
		return wwCtryCd;
	}
	public void setWwCtryCd(String wwCtryCd) {
		this.wwCtryCd = wwCtryCd;
	}
	public String getCurrCd() {
		return currCd;
	}
	public void setCurrCd(String currCd) {
		this.currCd = currCd;
	}
	public String getDateFmtCd() {
		return dateFmtCd;
	}
	public void setDateFmtCd(String dateFmtCd) {
		this.dateFmtCd = dateFmtCd;
	}
	public String getDbcsFlg() {
		return dbcsFlg;
	}
	public void setDbcsFlg(String dbcsFlg) {
		this.dbcsFlg = dbcsFlg;
	}
	public String getCicDomDlvryCntrInd() {
		return cicDomDlvryCntrInd;
	}
	public void setCicDomDlvryCntrInd(String cicDomDlvryCntrInd) {
		this.cicDomDlvryCntrInd = cicDomDlvryCntrInd;
	}
	public String getCicGblDlvryCntrInd() {
		return cicGblDlvryCntrInd;
	}
	public void setCicGblDlvryCntrInd(String cicGblDlvryCntrInd) {
		this.cicGblDlvryCntrInd = cicGblDlvryCntrInd;
	}
	public String getMrktCd() {
		return mrktCd;
	}
	public void setMrktCd(String mrktCd) {
		this.mrktCd = mrktCd;
	}
	public String getRgnCd() {
		return rgnCd;
	}
	public void setRgnCd(String rgnCd) {
		this.rgnCd = rgnCd;
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

	public int compareTo(CountryRrpgDim o) {
		return this.getCtryIsoId().compareTo(o.getCtryIsoId());
	}
}