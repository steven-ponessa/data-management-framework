package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetDtlTDim",baseTableName="BCSPMPC.OPNSET_DTL_T",isDimension=false,parentBeanPackageName="com.ibm.wfm.beans.prom",parentBeanName="OpnsetTDim",parentBaseTableName="BCSPMPC.OPNSET_T")
public class OpnsetDtlTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_ID",keySeq=1,foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="OWNG_CNTRY_CD")
	private String     owngCntryCd;
	@DbColumn(columnName="JOB_ROL_TYP_DESC")
	private String     jobRolTypDesc;
	@DbColumn(columnName="CET_JOB_ROL_CD")
	private String     cetJobRolCd;
	@DbColumn(columnName="SKLST_TYP_DESC")
	private String     sklstTypDesc;
	@DbColumn(columnName="CET_SKLST_CD")
	private String     cetSklstCd;
	@DbColumn(columnName="SVC_TYP_DESC")
	private String     svcTypDesc;
	@DbColumn(columnName="SVC_AREA_TYP_DESC")
	private String     svcAreaTypDesc;
	@DbColumn(columnName="CLNT_NM")
	private String     clntNm;
	@DbColumn(columnName="NEED_SUB_IND")
	private String     needSubInd;
	@DbColumn(columnName="SCTR_ID")
	private Long     sctrId;
	@DbColumn(columnName="INDSTR_ID")
	private Long     indstrId;
	@DbColumn(columnName="PROJ_NM")
	private String     projNm;
	@DbColumn(columnName="FULFLMNT_ORG_CD")
	private String     fulflmntOrgCd;
	@DbColumn(columnName="URG_PRIRTY_IND")
	private String     urgPrirtyInd;
	@DbColumn(columnName="URG_PRIRTY_RESN_TXT")
	private String     urgPrirtyResnTxt;
	@DbColumn(columnName="PREF_FULFLMNT_CHNL_CD")
	private String     prefFulflmntChnlCd;
	@DbColumn(columnName="PREF_FULFLMNT_CNTRY_CD")
	private String     prefFulflmntCntryCd;
	@DbColumn(columnName="SVC_COMP_TYP_DESC")
	private String     svcCompTypDesc;
	@DbColumn(columnName="GBS_ACCT_CLSS")
	private String     gbsAcctClss;
	@DbColumn(columnName="GLBL_CLNT_NM")
	private String     glblClntNm;
	@DbColumn(columnName="ACCNT_CLSTR_NM")
	private String     accntClstrNm;
	@DbColumn(columnName="CURR_COVRG_TYP_CD")
	private String     currCovrgTypCd;
	@DbColumn(columnName="SVF_GRP_CD")
	private String     svfGrpCd;
	@DbColumn(columnName="SVF_SUBGRP_CD")
	private String     svfSubgrpCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetDtlTDim () {
		
	}
	
	// Define natural key constructor
	public OpnsetDtlTDim (
      Long     opnsetId
	) {
		this.opnsetId                       = opnsetId;		
	}
	
	// Define base constructor
	public OpnsetDtlTDim (
      Long     opnsetId
    , String     owngCntryCd
    , String     jobRolTypDesc
    , String     cetJobRolCd
    , String     sklstTypDesc
    , String     cetSklstCd
    , String     svcTypDesc
    , String     svcAreaTypDesc
    , String     clntNm
    , String     needSubInd
    , Long     sctrId
    , Long     indstrId
    , String     projNm
    , String     fulflmntOrgCd
    , String     urgPrirtyInd
    , String     urgPrirtyResnTxt
    , String     prefFulflmntChnlCd
    , String     prefFulflmntCntryCd
    , String     svcCompTypDesc
    , String     gbsAcctClss
    , String     glblClntNm
    , String     accntClstrNm
    , String     currCovrgTypCd
    , String     svfGrpCd
    , String     svfSubgrpCd
	) {
		this.opnsetId                       = opnsetId;
		this.owngCntryCd                    = owngCntryCd;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.svcTypDesc                     = svcTypDesc;
		this.svcAreaTypDesc                 = svcAreaTypDesc;
		this.clntNm                         = clntNm;
		this.needSubInd                     = needSubInd;
		this.sctrId                         = sctrId;
		this.indstrId                       = indstrId;
		this.projNm                         = projNm;
		this.fulflmntOrgCd                  = fulflmntOrgCd;
		this.urgPrirtyInd                   = urgPrirtyInd;
		this.urgPrirtyResnTxt               = urgPrirtyResnTxt;
		this.prefFulflmntChnlCd             = prefFulflmntChnlCd;
		this.prefFulflmntCntryCd            = prefFulflmntCntryCd;
		this.svcCompTypDesc                 = svcCompTypDesc;
		this.gbsAcctClss                    = gbsAcctClss;
		this.glblClntNm                     = glblClntNm;
		this.accntClstrNm                   = accntClstrNm;
		this.currCovrgTypCd                 = currCovrgTypCd;
		this.svfGrpCd                       = svfGrpCd;
		this.svfSubgrpCd                    = svfSubgrpCd;
		
	}
    
	// Define full constructor
	public OpnsetDtlTDim (
		  int        recordId
		, Long     opnsetId
		, String     owngCntryCd
		, String     jobRolTypDesc
		, String     cetJobRolCd
		, String     sklstTypDesc
		, String     cetSklstCd
		, String     svcTypDesc
		, String     svcAreaTypDesc
		, String     clntNm
		, String     needSubInd
		, Long     sctrId
		, Long     indstrId
		, String     projNm
		, String     fulflmntOrgCd
		, String     urgPrirtyInd
		, String     urgPrirtyResnTxt
		, String     prefFulflmntChnlCd
		, String     prefFulflmntCntryCd
		, String     svcCompTypDesc
		, String     gbsAcctClss
		, String     glblClntNm
		, String     accntClstrNm
		, String     currCovrgTypCd
		, String     svfGrpCd
		, String     svfSubgrpCd
	) {
		this.recordId                       = recordId;
		this.opnsetId                       = opnsetId;
		this.owngCntryCd                    = owngCntryCd;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.svcTypDesc                     = svcTypDesc;
		this.svcAreaTypDesc                 = svcAreaTypDesc;
		this.clntNm                         = clntNm;
		this.needSubInd                     = needSubInd;
		this.sctrId                         = sctrId;
		this.indstrId                       = indstrId;
		this.projNm                         = projNm;
		this.fulflmntOrgCd                  = fulflmntOrgCd;
		this.urgPrirtyInd                   = urgPrirtyInd;
		this.urgPrirtyResnTxt               = urgPrirtyResnTxt;
		this.prefFulflmntChnlCd             = prefFulflmntChnlCd;
		this.prefFulflmntCntryCd            = prefFulflmntCntryCd;
		this.svcCompTypDesc                 = svcCompTypDesc;
		this.gbsAcctClss                    = gbsAcctClss;
		this.glblClntNm                     = glblClntNm;
		this.accntClstrNm                   = accntClstrNm;
		this.currCovrgTypCd                 = currCovrgTypCd;
		this.svfGrpCd                       = svfGrpCd;
		this.svfSubgrpCd                    = svfSubgrpCd;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.opnsetId)
		;
	}
	public String getDescription() { 
		return this.jobRolTypDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OpnsetDtlTDim other = (OpnsetDtlTDim) obj;
		if (
            this.opnsetId.equals(other.getOpnsetId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.owngCntryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRolTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetJobRolCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sklstTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetSklstCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcAreaTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.clntNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.needSubInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sctrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.indstrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.projNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.fulflmntOrgCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.urgPrirtyInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.urgPrirtyResnTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.prefFulflmntChnlCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.prefFulflmntCntryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcCompTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.gbsAcctClss))
        + "," + Helpers.formatCsvField(String.valueOf(this.glblClntNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.accntClstrNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.currCovrgTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfGrpCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfSubgrpCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("OWNG_CNTRY_CD")
        + "," + Helpers.formatCsvField("JOB_ROL_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_JOB_ROL_CD")
        + "," + Helpers.formatCsvField("SKLST_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_SKLST_CD")
        + "," + Helpers.formatCsvField("SVC_TYP_DESC")
        + "," + Helpers.formatCsvField("SVC_AREA_TYP_DESC")
        + "," + Helpers.formatCsvField("CLNT_NM")
        + "," + Helpers.formatCsvField("NEED_SUB_IND")
        + "," + Helpers.formatCsvField("SCTR_ID")
        + "," + Helpers.formatCsvField("INDSTR_ID")
        + "," + Helpers.formatCsvField("PROJ_NM")
        + "," + Helpers.formatCsvField("FULFLMNT_ORG_CD")
        + "," + Helpers.formatCsvField("URG_PRIRTY_IND")
        + "," + Helpers.formatCsvField("URG_PRIRTY_RESN_TXT")
        + "," + Helpers.formatCsvField("PREF_FULFLMNT_CHNL_CD")
        + "," + Helpers.formatCsvField("PREF_FULFLMNT_CNTRY_CD")
        + "," + Helpers.formatCsvField("SVC_COMP_TYP_DESC")
        + "," + Helpers.formatCsvField("GBS_ACCT_CLSS")
        + "," + Helpers.formatCsvField("GLBL_CLNT_NM")
        + "," + Helpers.formatCsvField("ACCNT_CLSTR_NM")
        + "," + Helpers.formatCsvField("CURR_COVRG_TYP_CD")
        + "," + Helpers.formatCsvField("SVF_GRP_CD")
        + "," + Helpers.formatCsvField("SVF_SUBGRP_CD")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public String getOwngCntryCd() {
		return owngCntryCd;
	}
	public void setOwngCntryCd(String owngCntryCd) {
		this.owngCntryCd = owngCntryCd;
	}
	public String getJobRolTypDesc() {
		return jobRolTypDesc;
	}
	public void setJobRolTypDesc(String jobRolTypDesc) {
		this.jobRolTypDesc = jobRolTypDesc;
	}
	public String getCetJobRolCd() {
		return cetJobRolCd;
	}
	public void setCetJobRolCd(String cetJobRolCd) {
		this.cetJobRolCd = cetJobRolCd;
	}
	public String getSklstTypDesc() {
		return sklstTypDesc;
	}
	public void setSklstTypDesc(String sklstTypDesc) {
		this.sklstTypDesc = sklstTypDesc;
	}
	public String getCetSklstCd() {
		return cetSklstCd;
	}
	public void setCetSklstCd(String cetSklstCd) {
		this.cetSklstCd = cetSklstCd;
	}
	public String getSvcTypDesc() {
		return svcTypDesc;
	}
	public void setSvcTypDesc(String svcTypDesc) {
		this.svcTypDesc = svcTypDesc;
	}
	public String getSvcAreaTypDesc() {
		return svcAreaTypDesc;
	}
	public void setSvcAreaTypDesc(String svcAreaTypDesc) {
		this.svcAreaTypDesc = svcAreaTypDesc;
	}
	public String getClntNm() {
		return clntNm;
	}
	public void setClntNm(String clntNm) {
		this.clntNm = clntNm;
	}
	public String getNeedSubInd() {
		return needSubInd;
	}
	public void setNeedSubInd(String needSubInd) {
		this.needSubInd = needSubInd;
	}
	public Long getSctrId() {
		return sctrId;
	}
	public void setSctrId(Long sctrId) {
		this.sctrId = sctrId;
	}
	public Long getIndstrId() {
		return indstrId;
	}
	public void setIndstrId(Long indstrId) {
		this.indstrId = indstrId;
	}
	public String getProjNm() {
		return projNm;
	}
	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	public String getFulflmntOrgCd() {
		return fulflmntOrgCd;
	}
	public void setFulflmntOrgCd(String fulflmntOrgCd) {
		this.fulflmntOrgCd = fulflmntOrgCd;
	}
	public String getUrgPrirtyInd() {
		return urgPrirtyInd;
	}
	public void setUrgPrirtyInd(String urgPrirtyInd) {
		this.urgPrirtyInd = urgPrirtyInd;
	}
	public String getUrgPrirtyResnTxt() {
		return urgPrirtyResnTxt;
	}
	public void setUrgPrirtyResnTxt(String urgPrirtyResnTxt) {
		this.urgPrirtyResnTxt = urgPrirtyResnTxt;
	}
	public String getPrefFulflmntChnlCd() {
		return prefFulflmntChnlCd;
	}
	public void setPrefFulflmntChnlCd(String prefFulflmntChnlCd) {
		this.prefFulflmntChnlCd = prefFulflmntChnlCd;
	}
	public String getPrefFulflmntCntryCd() {
		return prefFulflmntCntryCd;
	}
	public void setPrefFulflmntCntryCd(String prefFulflmntCntryCd) {
		this.prefFulflmntCntryCd = prefFulflmntCntryCd;
	}
	public String getSvcCompTypDesc() {
		return svcCompTypDesc;
	}
	public void setSvcCompTypDesc(String svcCompTypDesc) {
		this.svcCompTypDesc = svcCompTypDesc;
	}
	public String getGbsAcctClss() {
		return gbsAcctClss;
	}
	public void setGbsAcctClss(String gbsAcctClss) {
		this.gbsAcctClss = gbsAcctClss;
	}
	public String getGlblClntNm() {
		return glblClntNm;
	}
	public void setGlblClntNm(String glblClntNm) {
		this.glblClntNm = glblClntNm;
	}
	public String getAccntClstrNm() {
		return accntClstrNm;
	}
	public void setAccntClstrNm(String accntClstrNm) {
		this.accntClstrNm = accntClstrNm;
	}
	public String getCurrCovrgTypCd() {
		return currCovrgTypCd;
	}
	public void setCurrCovrgTypCd(String currCovrgTypCd) {
		this.currCovrgTypCd = currCovrgTypCd;
	}
	public String getSvfGrpCd() {
		return svfGrpCd;
	}
	public void setSvfGrpCd(String svfGrpCd) {
		this.svfGrpCd = svfGrpCd;
	}
	public String getSvfSubgrpCd() {
		return svfSubgrpCd;
	}
	public void setSvfSubgrpCd(String svfSubgrpCd) {
		this.svfSubgrpCd = svfSubgrpCd;
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