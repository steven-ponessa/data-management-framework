package com.ibm.wfm.beans.prom;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.beans.NaryTreeNode;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GrOpnsetPosTDim",baseTableName="BCSPMPC.GR_OPNSET_POS_T",isDimension=false,parentBeanName="OpnsetTDim",parentBaseTableName="PROMDMC.OPNSET_T")
public class GrOpnsetPosTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="GR_OPNSET_POS_ID",keySeq=1)
	private Long     grOpnsetPosId;
	@DbColumn(columnName="OPNSET_ID",foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="POS_INDX_NUM")
	private Integer   posIndxNum;
	@DbColumn(columnName="GRDS_POS_ID")
	private Integer        grdsPosId;
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
	@DbColumn(columnName="ONSIT_STRT_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       onsitStrtDt;
	@DbColumn(columnName="ONSIT_END_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       onsitEndDt;
	@DbColumn(columnName="NEED_CLNT_STE_IND")
	private String     needClntSteInd;
	@DbColumn(columnName="EST_STRT_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       estStrtDt;
	@DbColumn(columnName="OPNSET_VAL_STAT_CD")
	private String     opnsetValStatCd;
	@DbColumn(columnName="POS_STAT_CD")
	private String     posStatCd;
	@DbColumn(columnName="STAT_RESN_CD")
	private String     statResnCd;
	@DbColumn(columnName="WTHDRW_FIL_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  wthdrwFilT;
	@DbColumn(columnName="RMX_SO_LNE")
	private Integer        rmxSoLne;
	@DbColumn(columnName="FULFLMNT_SRC_ID")
	private Long     fulflmntSrcId;
	@DbColumn(columnName="FULFLMNT_DTL_TXT")
	private String     fulflmntDtlTxt;
	@DbColumn(columnName="SVC_COMP_TYP_DESC")
	private String     svcCompTypDesc;
	@DbColumn(columnName="WTHDRW_FIL_USR_ID")
	private Long     wthdrwFilUsrId;
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
	public GrOpnsetPosTDim () {
		
	}
	
	public GrOpnsetPosTDim (Long opnsetId) {
		this.opnsetId  = opnsetId;
	}
	
	// Define natural key constructor
	public GrOpnsetPosTDim (
      Long     grOpnsetPosId
    , Long     opnsetId
	) {
		this.grOpnsetPosId                  = grOpnsetPosId;
		this.opnsetId                       = opnsetId;
		
	}
	
	// Define base constructor
	public GrOpnsetPosTDim (
      Long     grOpnsetPosId
    , Long     opnsetId
    , Integer   posIndxNum
    , Integer        grdsPosId
    , String     jobRolTypDesc
    , String     cetJobRolCd
    , String     sklstTypDesc
    , String     cetSklstCd
    , String     svcTypDesc
    , String     svcAreaTypDesc
    , Date       onsitStrtDt
    , Date       onsitEndDt
    , String     needClntSteInd
    , Date       estStrtDt
    , String     opnsetValStatCd
    , String     posStatCd
    , String     statResnCd
    , Timestamp  wthdrwFilT
    , Integer        rmxSoLne
    , Long     fulflmntSrcId
    , String     fulflmntDtlTxt
    , String     svcCompTypDesc
    , Long     wthdrwFilUsrId
    , String     svfGrpCd
    , String     svfSubgrpCd
	) {
		this.grOpnsetPosId                  = grOpnsetPosId;
		this.opnsetId                       = opnsetId;
		this.posIndxNum                     = posIndxNum;
		this.grdsPosId                      = grdsPosId;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.svcTypDesc                     = svcTypDesc;
		this.svcAreaTypDesc                 = svcAreaTypDesc;
		this.onsitStrtDt                    = onsitStrtDt;
		this.onsitEndDt                     = onsitEndDt;
		this.needClntSteInd                 = needClntSteInd;
		this.estStrtDt                      = estStrtDt;
		this.opnsetValStatCd                = opnsetValStatCd;
		this.posStatCd                      = posStatCd;
		this.statResnCd                     = statResnCd;
		this.wthdrwFilT                     = wthdrwFilT;
		this.rmxSoLne                       = rmxSoLne;
		this.fulflmntSrcId                  = fulflmntSrcId;
		this.fulflmntDtlTxt                 = fulflmntDtlTxt;
		this.svcCompTypDesc                 = svcCompTypDesc;
		this.wthdrwFilUsrId                 = wthdrwFilUsrId;
		this.svfGrpCd                       = svfGrpCd;
		this.svfSubgrpCd                    = svfSubgrpCd;
		
	}
    
	// Define full constructor
	public GrOpnsetPosTDim (
		  int        recordId
		, Long     grOpnsetPosId
		, Long     opnsetId
		, Integer   posIndxNum
		, Integer        grdsPosId
		, String     jobRolTypDesc
		, String     cetJobRolCd
		, String     sklstTypDesc
		, String     cetSklstCd
		, String     svcTypDesc
		, String     svcAreaTypDesc
		, Date       onsitStrtDt
		, Date       onsitEndDt
		, String     needClntSteInd
		, Date       estStrtDt
		, String     opnsetValStatCd
		, String     posStatCd
		, String     statResnCd
		, Timestamp  wthdrwFilT
		, Integer        rmxSoLne
		, Long     fulflmntSrcId
		, String     fulflmntDtlTxt
		, String     svcCompTypDesc
		, Long     wthdrwFilUsrId
		, String     svfGrpCd
		, String     svfSubgrpCd
	) {
		this.recordId                       = recordId;
		this.grOpnsetPosId                  = grOpnsetPosId;
		this.opnsetId                       = opnsetId;
		this.posIndxNum                     = posIndxNum;
		this.grdsPosId                      = grdsPosId;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.svcTypDesc                     = svcTypDesc;
		this.svcAreaTypDesc                 = svcAreaTypDesc;
		this.onsitStrtDt                    = onsitStrtDt;
		this.onsitEndDt                     = onsitEndDt;
		this.needClntSteInd                 = needClntSteInd;
		this.estStrtDt                      = estStrtDt;
		this.opnsetValStatCd                = opnsetValStatCd;
		this.posStatCd                      = posStatCd;
		this.statResnCd                     = statResnCd;
		this.wthdrwFilT                     = wthdrwFilT;
		this.rmxSoLne                       = rmxSoLne;
		this.fulflmntSrcId                  = fulflmntSrcId;
		this.fulflmntDtlTxt                 = fulflmntDtlTxt;
		this.svcCompTypDesc                 = svcCompTypDesc;
		this.wthdrwFilUsrId                 = wthdrwFilUsrId;
		this.svfGrpCd                       = svfGrpCd;
		this.svfSubgrpCd                    = svfSubgrpCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.grOpnsetPosId
    +":"+ this.opnsetId
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
                    
		GrOpnsetPosTDim other = (GrOpnsetPosTDim) obj;
		if (
            this.grOpnsetPosId.equals(other.getGrOpnsetPosId())
         && this.opnsetId.equals(other.getOpnsetId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.grOpnsetPosId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.posIndxNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.grdsPosId))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRolTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetJobRolCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sklstTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetSklstCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcAreaTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsitStrtDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsitEndDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.needClntSteInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.estStrtDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetValStatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.posStatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.statResnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wthdrwFilT))
        + "," + Helpers.formatCsvField(String.valueOf(this.rmxSoLne))
        + "," + Helpers.formatCsvField(String.valueOf(this.fulflmntSrcId))
        + "," + Helpers.formatCsvField(String.valueOf(this.fulflmntDtlTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.svcCompTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.wthdrwFilUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfGrpCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfSubgrpCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GR_OPNSET_POS_ID")
        + "," + Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("POS_INDX_NUM")
        + "," + Helpers.formatCsvField("GRDS_POS_ID")
        + "," + Helpers.formatCsvField("JOB_ROL_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_JOB_ROL_CD")
        + "," + Helpers.formatCsvField("SKLST_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_SKLST_CD")
        + "," + Helpers.formatCsvField("SVC_TYP_DESC")
        + "," + Helpers.formatCsvField("SVC_AREA_TYP_DESC")
        + "," + Helpers.formatCsvField("ONSIT_STRT_DT")
        + "," + Helpers.formatCsvField("ONSIT_END_DT")
        + "," + Helpers.formatCsvField("NEED_CLNT_STE_IND")
        + "," + Helpers.formatCsvField("EST_STRT_DT")
        + "," + Helpers.formatCsvField("OPNSET_VAL_STAT_CD")
        + "," + Helpers.formatCsvField("POS_STAT_CD")
        + "," + Helpers.formatCsvField("STAT_RESN_CD")
        + "," + Helpers.formatCsvField("WTHDRW_FIL_T")
        + "," + Helpers.formatCsvField("RMX_SO_LNE")
        + "," + Helpers.formatCsvField("FULFLMNT_SRC_ID")
        + "," + Helpers.formatCsvField("FULFLMNT_DTL_TXT")
        + "," + Helpers.formatCsvField("SVC_COMP_TYP_DESC")
        + "," + Helpers.formatCsvField("WTHDRW_FIL_USR_ID")
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
	public Long getGrOpnsetPosId() {
		return grOpnsetPosId;
	}
	public void setGrOpnsetPosId(Long grOpnsetPosId) {
		this.grOpnsetPosId = grOpnsetPosId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public Integer getPosIndxNum() {
		return posIndxNum;
	}
	public void setPosIndxNum(Integer posIndxNum) {
		this.posIndxNum = posIndxNum;
	}
	public Integer getGrdsPosId() {
		return grdsPosId;
	}
	public void setGrdsPosId(Integer grdsPosId) {
		this.grdsPosId = grdsPosId;
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
	public Date getOnsitStrtDt() {
		return onsitStrtDt;
	}
	public void setOnsitStrtDt(Date onsitStrtDt) {
		this.onsitStrtDt = onsitStrtDt;
	}
	public Date getOnsitEndDt() {
		return onsitEndDt;
	}
	public void setOnsitEndDt(Date onsitEndDt) {
		this.onsitEndDt = onsitEndDt;
	}
	public String getNeedClntSteInd() {
		return needClntSteInd;
	}
	public void setNeedClntSteInd(String needClntSteInd) {
		this.needClntSteInd = needClntSteInd;
	}
	public Date getEstStrtDt() {
		return estStrtDt;
	}
	public void setEstStrtDt(Date estStrtDt) {
		this.estStrtDt = estStrtDt;
	}
	public String getOpnsetValStatCd() {
		return opnsetValStatCd;
	}
	public void setOpnsetValStatCd(String opnsetValStatCd) {
		this.opnsetValStatCd = opnsetValStatCd;
	}
	public String getPosStatCd() {
		return posStatCd;
	}
	public void setPosStatCd(String posStatCd) {
		this.posStatCd = posStatCd;
	}
	public String getStatResnCd() {
		return statResnCd;
	}
	public void setStatResnCd(String statResnCd) {
		this.statResnCd = statResnCd;
	}
	public Timestamp getWthdrwFilT() {
		return wthdrwFilT;
	}
	public void setWthdrwFilT(Timestamp wthdrwFilT) {
		this.wthdrwFilT = wthdrwFilT;
	}
	public Integer getRmxSoLne() {
		return rmxSoLne;
	}
	public void setRmxSoLne(Integer rmxSoLne) {
		this.rmxSoLne = rmxSoLne;
	}
	public Long getFulflmntSrcId() {
		return fulflmntSrcId;
	}
	public void setFulflmntSrcId(Long fulflmntSrcId) {
		this.fulflmntSrcId = fulflmntSrcId;
	}
	public String getFulflmntDtlTxt() {
		return fulflmntDtlTxt;
	}
	public void setFulflmntDtlTxt(String fulflmntDtlTxt) {
		this.fulflmntDtlTxt = fulflmntDtlTxt;
	}
	public String getSvcCompTypDesc() {
		return svcCompTypDesc;
	}
	public void setSvcCompTypDesc(String svcCompTypDesc) {
		this.svcCompTypDesc = svcCompTypDesc;
	}
	public Long getWthdrwFilUsrId() {
		return wthdrwFilUsrId;
	}
	public void setWthdrwFilUsrId(Long wthdrwFilUsrId) {
		this.wthdrwFilUsrId = wthdrwFilUsrId;
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