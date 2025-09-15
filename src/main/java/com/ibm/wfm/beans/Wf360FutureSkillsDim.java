package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class Wf360FutureSkillsDim {
	@DbColumn(columnName="FUTURE_SKLS_ID",isId=true)
	private int        futureSklsId;
	@DbColumn(columnName="CNUM_ID",keySeq=1)
	private String     cnumId;
	@DbColumn(columnName="TRGT_JRS_NM",keySeq=2)
	private String     trgtJrsNm;
	@DbColumn(columnName="TYP_OF_SKLG_DESC",keySeq=3)
	private String     typOfSklgDesc;
	@DbColumn(columnName="CURR_BUS_VAL_TXT")
	private String     currBusValTxt;
	@DbColumn(columnName="TRGT_BUS_VAL_TXT")
	private String     trgtBusValTxt;
	@DbColumn(columnName="CURR_IND")
	private int        currInd;
	@DbColumn(columnName="EFF_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       effDt;
	@DbColumn(columnName="EXPIR_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       expirDt;
	@DbColumn(columnName="ETL_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  etlTms;
	@DbColumn(columnName="AUDIT_TMS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  auditTms;
	@DbColumn(columnName="STAT_DESC")
	private String     statDesc;
	@DbColumn(columnName="CMPLTN_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       cmpltnDt;
	@DbColumn(columnName="BATCH_NM")
	private String     batchNm;
	@DbColumn(columnName="BATCH_STAT_DESC")
	private String     batchStatDesc;
	@DbColumn(columnName="CMPLTN_PCT")
	private int        cmpltnPct;
	@DbColumn(columnName="PLN_NM")
	private String     plnNm;
	@DbColumn(columnName="JRS_CHGD_NM")
	private String     jrsChgdNm;
	@DbColumn(columnName="RPTG_YR_NUM")
	private int        rptgYrNum;
	@DbColumn(columnName="LEARNG_ADVISR_DESC")
	private String     learngAdvisrDesc;
	@DbColumn(columnName="ENROLLMT_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       enrollmtDt;
	@DbColumn(columnName="SRC_JRS_NM")
	private String     srcJrsNm;
	@DbColumn(columnName="MGMT_ORG_DESC")
	private String     mgmtOrgDesc;
	@DbColumn(columnName="CHNL_NM")
	private String     chnlNm;
	@DbColumn(columnName="STRT_MO_NM")
	private String     strtMoNm;
	@DbColumn(columnName="ARCHVD_STAT_NM")
	private String     archvdStatNm;

	// Define null constructor
	public Wf360FutureSkillsDim () {}
	
	// Define base constructor
	public Wf360FutureSkillsDim (
      String     cnumId
    , String     trgtJrsNm
    , String     typOfSklgDesc
    , String     currBusValTxt
    , String     trgtBusValTxt
    , int        currInd
    , Date       effDt
    , Date       expirDt
    , Timestamp  etlTms
    , Timestamp  auditTms
    , String     statDesc
    , Date       cmpltnDt
    , String     batchNm
    , String     batchStatDesc
    , int        cmpltnPct
    , String     plnNm
    , String     jrsChgdNm
    , int        rptgYrNum
    , String     learngAdvisrDesc
    , Date       enrollmtDt
    , String     srcJrsNm
    , String     mgmtOrgDesc
    , String     chnlNm
    , String     strtMoNm
    , String     archvdStatNm
	) {
		this.cnumId                         = cnumId;
		this.trgtJrsNm                      = trgtJrsNm;
		this.typOfSklgDesc                  = typOfSklgDesc;
		this.currBusValTxt                  = currBusValTxt;
		this.trgtBusValTxt                  = trgtBusValTxt;
		this.currInd                        = currInd;
		this.effDt                          = effDt;
		this.expirDt                        = expirDt;
		this.etlTms                         = etlTms;
		this.auditTms                       = auditTms;
		this.statDesc                       = statDesc;
		this.cmpltnDt                       = cmpltnDt;
		this.batchNm                        = batchNm;
		this.batchStatDesc                  = batchStatDesc;
		this.cmpltnPct                      = cmpltnPct;
		this.plnNm                          = plnNm;
		this.jrsChgdNm                      = jrsChgdNm;
		this.rptgYrNum                      = rptgYrNum;
		this.learngAdvisrDesc               = learngAdvisrDesc;
		this.enrollmtDt                     = enrollmtDt;
		this.srcJrsNm                       = srcJrsNm;
		this.mgmtOrgDesc                    = mgmtOrgDesc;
		this.chnlNm                         = chnlNm;
		this.strtMoNm                       = strtMoNm;
		this.archvdStatNm                   = archvdStatNm;
	}
    
	// Define full constructor
	public Wf360FutureSkillsDim (
		  int        futureSklsId
		, String     cnumId
		, String     trgtJrsNm
		, String     typOfSklgDesc
		, String     currBusValTxt
		, String     trgtBusValTxt
		, int        currInd
		, Date       effDt
		, Date       expirDt
		, Timestamp  etlTms
		, Timestamp  auditTms
		, String     statDesc
		, Date       cmpltnDt
		, String     batchNm
		, String     batchStatDesc
		, int        cmpltnPct
		, String     plnNm
		, String     jrsChgdNm
		, int        rptgYrNum
		, String     learngAdvisrDesc
		, Date       enrollmtDt
		, String     srcJrsNm
		, String     mgmtOrgDesc
		, String     chnlNm
		, String     strtMoNm
		, String     archvdStatNm
	) {
		this.futureSklsId                   = futureSklsId;
		this.cnumId                         = cnumId;
		this.trgtJrsNm                      = trgtJrsNm;
		this.typOfSklgDesc                  = typOfSklgDesc;
		this.currBusValTxt                  = currBusValTxt;
		this.trgtBusValTxt                  = trgtBusValTxt;
		this.currInd                        = currInd;
		this.effDt                          = effDt;
		this.expirDt                        = expirDt;
		this.etlTms                         = etlTms;
		this.auditTms                       = auditTms;
		this.statDesc                       = statDesc;
		this.cmpltnDt                       = cmpltnDt;
		this.batchNm                        = batchNm;
		this.batchStatDesc                  = batchStatDesc;
		this.cmpltnPct                      = cmpltnPct;
		this.plnNm                          = plnNm;
		this.jrsChgdNm                      = jrsChgdNm;
		this.rptgYrNum                      = rptgYrNum;
		this.learngAdvisrDesc               = learngAdvisrDesc;
		this.enrollmtDt                     = enrollmtDt;
		this.srcJrsNm                       = srcJrsNm;
		this.mgmtOrgDesc                    = mgmtOrgDesc;
		this.chnlNm                         = chnlNm;
		this.strtMoNm                       = strtMoNm;
		this.archvdStatNm                   = archvdStatNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		Wf360FutureSkillsDim other = (Wf360FutureSkillsDim) obj;
		if (
            this.cnumId.equals(other.getCnumId())
         && this.trgtJrsNm.equals(other.getTrgtJrsNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.cnumId))
        + "," + Helpers.formatCsvField(String.valueOf(this.trgtJrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.typOfSklgDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.currBusValTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.trgtBusValTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.currInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.effDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.expirDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.etlTms))
        + "," + Helpers.formatCsvField(String.valueOf(this.auditTms))
        + "," + Helpers.formatCsvField(String.valueOf(this.statDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpltnDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.batchNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.batchStatDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpltnPct))
        + "," + Helpers.formatCsvField(String.valueOf(this.plnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsChgdNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.rptgYrNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.learngAdvisrDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.enrollmtDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcJrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.mgmtOrgDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.chnlNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.strtMoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.archvdStatNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CNUM_ID")
        + "," + Helpers.formatCsvField("TRGT_JRS_NM")
        + "," + Helpers.formatCsvField("TYP_OF_SKLG_DESC")
        + "," + Helpers.formatCsvField("CURR_BUS_VAL_TXT")
        + "," + Helpers.formatCsvField("TRGT_BUS_VAL_TXT")
        + "," + Helpers.formatCsvField("CURR_IND")
        + "," + Helpers.formatCsvField("EFF_DT")
        + "," + Helpers.formatCsvField("EXPIR_DT")
        + "," + Helpers.formatCsvField("ETL_TMS")
        + "," + Helpers.formatCsvField("AUDIT_TMS")
        + "," + Helpers.formatCsvField("STAT_DESC")
        + "," + Helpers.formatCsvField("CMPLTN_DT")
        + "," + Helpers.formatCsvField("BATCH_NM")
        + "," + Helpers.formatCsvField("BATCH_STAT_DESC")
        + "," + Helpers.formatCsvField("CMPLTN_PCT")
        + "," + Helpers.formatCsvField("PLN_NM")
        + "," + Helpers.formatCsvField("JRS_CHGD_NM")
        + "," + Helpers.formatCsvField("RPTG_YR_NUM")
        + "," + Helpers.formatCsvField("LEARNG_ADVISR_DESC")
        + "," + Helpers.formatCsvField("ENROLLMT_DT")
        + "," + Helpers.formatCsvField("SRC_JRS_NM")
        + "," + Helpers.formatCsvField("MGMT_ORG_DESC")
        + "," + Helpers.formatCsvField("CHNL_NM")
        + "," + Helpers.formatCsvField("STRT_MO_NM")
        + "," + Helpers.formatCsvField("ARCHVD_STAT_NM")
		;
	}
    
	// Define Getters and Setters
	public int getFutureSklsId() {
		return futureSklsId;
	}
	public void setFutureSklsId(int futureSklsId) {
		this.futureSklsId = futureSklsId;
	}
	public String getCnumId() {
		return cnumId;
	}
	public void setCnumId(String cnumId) {
		this.cnumId = cnumId;
	}
	public String getTrgtJrsNm() {
		return trgtJrsNm;
	}
	public void setTrgtJrsNm(String trgtJrsNm) {
		this.trgtJrsNm = trgtJrsNm;
	}
	public String getTypOfSklgDesc() {
		return typOfSklgDesc;
	}
	public void setTypOfSklgDesc(String typOfSklgDesc) {
		this.typOfSklgDesc = typOfSklgDesc;
	}
	public String getCurrBusValTxt() {
		return currBusValTxt;
	}
	public void setCurrBusValTxt(String currBusValTxt) {
		this.currBusValTxt = currBusValTxt;
	}
	public String getTrgtBusValTxt() {
		return trgtBusValTxt;
	}
	public void setTrgtBusValTxt(String trgtBusValTxt) {
		this.trgtBusValTxt = trgtBusValTxt;
	}
	public int getCurrInd() {
		return currInd;
	}
	public void setCurrInd(int currInd) {
		this.currInd = currInd;
	}
	public Date getEffDt() {
		return effDt;
	}
	public void setEffDt(Date effDt) {
		this.effDt = effDt;
	}
	public Date getExpirDt() {
		return expirDt;
	}
	public void setExpirDt(Date expirDt) {
		this.expirDt = expirDt;
	}
	public Timestamp getEtlTms() {
		return etlTms;
	}
	public void setEtlTms(Timestamp etlTms) {
		this.etlTms = etlTms;
	}
	public Timestamp getAuditTms() {
		return auditTms;
	}
	public void setAuditTms(Timestamp auditTms) {
		this.auditTms = auditTms;
	}
	public String getStatDesc() {
		return statDesc;
	}
	public void setStatDesc(String statDesc) {
		this.statDesc = statDesc;
	}
	public Date getCmpltnDt() {
		return cmpltnDt;
	}
	public void setCmpltnDt(Date cmpltnDt) {
		this.cmpltnDt = cmpltnDt;
	}
	public String getBatchNm() {
		return batchNm;
	}
	public void setBatchNm(String batchNm) {
		this.batchNm = batchNm;
	}
	public String getBatchStatDesc() {
		return batchStatDesc;
	}
	public void setBatchStatDesc(String batchStatDesc) {
		this.batchStatDesc = batchStatDesc;
	}
	public int getCmpltnPct() {
		return cmpltnPct;
	}
	public void setCmpltnPct(int cmpltnPct) {
		this.cmpltnPct = cmpltnPct;
	}
	public String getPlnNm() {
		return plnNm;
	}
	public void setPlnNm(String plnNm) {
		this.plnNm = plnNm;
	}
	public String getJrsChgdNm() {
		return jrsChgdNm;
	}
	public void setJrsChgdNm(String jrsChgdNm) {
		this.jrsChgdNm = jrsChgdNm;
	}
	public int getRptgYrNum() {
		return rptgYrNum;
	}
	public void setRptgYrNum(int rptgYrNum) {
		this.rptgYrNum = rptgYrNum;
	}
	public String getLearngAdvisrDesc() {
		return learngAdvisrDesc;
	}
	public void setLearngAdvisrDesc(String learngAdvisrDesc) {
		this.learngAdvisrDesc = learngAdvisrDesc;
	}
	public Date getEnrollmtDt() {
		return enrollmtDt;
	}
	public void setEnrollmtDt(Date enrollmtDt) {
		this.enrollmtDt = enrollmtDt;
	}
	public String getSrcJrsNm() {
		return srcJrsNm;
	}
	public void setSrcJrsNm(String srcJrsNm) {
		this.srcJrsNm = srcJrsNm;
	}
	public String getMgmtOrgDesc() {
		return mgmtOrgDesc;
	}
	public void setMgmtOrgDesc(String mgmtOrgDesc) {
		this.mgmtOrgDesc = mgmtOrgDesc;
	}
	public String getChnlNm() {
		return chnlNm;
	}
	public void setChnlNm(String chnlNm) {
		this.chnlNm = chnlNm;
	}
	public String getStrtMoNm() {
		return strtMoNm;
	}
	public void setStrtMoNm(String strtMoNm) {
		this.strtMoNm = strtMoNm;
	}
	public String getArchvdStatNm() {
		return archvdStatNm;
	}
	public void setArchvdStatNm(String archvdStatNm) {
		this.archvdStatNm = archvdStatNm;
	}
}