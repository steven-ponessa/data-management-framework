package com.ibm.wfm.beans.prom;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SubResrcReqTDim",baseTableName="BCSPMPC.SUB_RESRC_REQ_T",isDimension=false,parentBeanPackageName="com.ibm.wfm.beans.prom",parentBeanName="OpnsetTDim",parentBaseTableName="BCSPMPC.OPNSET_T")
public class SubResrcReqTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="SUB_RESRC_REQ_ID",keySeq=1)
	private Long     subResrcReqId;
	@DbColumn(columnName="OPNSET_ID",foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="SUB_REQ_INDX_NUM")
	private Short   subReqIndxNum;
	@DbColumn(columnName="DEL_FLG")
	private String     delFlg;
	@DbColumn(columnName="CSA_REQ_ID")
	private String     csaReqId;
	@DbColumn(columnName="CSA_REQ_URL")
	private String     csaReqUrl;
	@DbColumn(columnName="CSA_CONF_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  csaConfT;
	@DbColumn(columnName="YR_EXP")
	private String     yrExp;
	@DbColumn(columnName="IBM_WRK_LOC_IND")
	private String     ibmWrkLocInd;
	@DbColumn(columnName="FED_CNTRCT_IND")
	private String     fedCntrctInd;
	@DbColumn(columnName="SUPPLR_CMT_TXT")
	private String       supplrCmtTxt;
	@DbColumn(columnName="INIT_USR_ID")
	private Long     initUsrId;
	@DbColumn(columnName="REQ_USR_ID")
	private Long     reqUsrId;
	@DbColumn(columnName="TECH_COORD_USR_ID")
	private Long     techCoordUsrId;
	@DbColumn(columnName="DUE_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       dueDt;
	@DbColumn(columnName="EDU_REQ_TXT")
	private String     eduReqTxt;
	@DbColumn(columnName="WRK_LOC_ST_ADDR")
	private String     wrkLocStAddr;
	@DbColumn(columnName="BND_LVL")
	private String     bndLvl;
	@DbColumn(columnName="SEC_WRK_LOC")
	private String     secWrkLoc;
	@DbColumn(columnName="PASS_THRU_SKL_IND")
	private String     passThruSklInd;
	@DbColumn(columnName="CTZNSHP_VISA_PREF")
	private String     ctznshpVisaPref;
	@DbColumn(columnName="CSA_QTY")
	private Short   csaQty;
	@DbColumn(columnName="REF_CAND_FRST_NM")
	private String     refCandFrstNm;
	@DbColumn(columnName="REF_CAND_MID_NM")
	private String     refCandMidNm;
	@DbColumn(columnName="REF_CAND_LST_NM")
	private String     refCandLstNm;
	@DbColumn(columnName="REF_CAND_PHN_NUM")
	private String     refCandPhnNum;
	@DbColumn(columnName="REF_CAND_EMAL_ADDR")
	private String     refCandEmalAddr;
	@DbColumn(columnName="JOB_ROL_TYP_DESC")
	private String     jobRolTypDesc;
	@DbColumn(columnName="CET_JOB_ROL_CD")
	private String     cetJobRolCd;
	@DbColumn(columnName="SKLST_TYP_DESC")
	private String     sklstTypDesc;
	@DbColumn(columnName="CET_SKLST_CD")
	private String     cetSklstCd;
	@DbColumn(columnName="CAND_PRC_LVL_CD")
	private String     candPrcLvlCd;
	@DbColumn(columnName="CSA_JRSS_OVRRD_RESN_CD")
	private String     csaJrssOvrrdResnCd;
	@DbColumn(columnName="CNTR_REQ_TYP_ID")
	private Long     cntrReqTypId;
	@DbColumn(columnName="SAP_CMPNY_ID")
	private Long     sapCmpnyId;
	@DbColumn(columnName="BUSUNIT_ID")
	private Long     busunitId;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SubResrcReqTDim () {
		
	}
	
	// Define natural key constructor
	public SubResrcReqTDim (
      Long     subResrcReqId
	) {
		this.subResrcReqId                  = subResrcReqId;

	}
	
	// Define base constructor
	public SubResrcReqTDim (
      Long     subResrcReqId
    , Long     opnsetId
    , Short   subReqIndxNum
    , String     delFlg
    , String     csaReqId
    , String     csaReqUrl
    , Timestamp  csaConfT
    , String     yrExp
    , String     ibmWrkLocInd
    , String     fedCntrctInd
    , String       supplrCmtTxt
    , Long     initUsrId
    , Long     reqUsrId
    , Long     techCoordUsrId
    , Date       dueDt
    , String     eduReqTxt
    , String     wrkLocStAddr
    , String     bndLvl
    , String     secWrkLoc
    , String     passThruSklInd
    , String     ctznshpVisaPref
    , Short   csaQty
    , String     refCandFrstNm
    , String     refCandMidNm
    , String     refCandLstNm
    , String     refCandPhnNum
    , String     refCandEmalAddr
    , String     jobRolTypDesc
    , String     cetJobRolCd
    , String     sklstTypDesc
    , String     cetSklstCd
    , String     candPrcLvlCd
    , String     csaJrssOvrrdResnCd
    , Long     cntrReqTypId
    , Long     sapCmpnyId
    , Long     busunitId
	) {
		this.subResrcReqId                  = subResrcReqId;
		this.opnsetId                       = opnsetId;
		this.subReqIndxNum                  = subReqIndxNum;
		this.delFlg                         = delFlg;
		this.csaReqId                       = csaReqId;
		this.csaReqUrl                      = csaReqUrl;
		this.csaConfT                       = csaConfT;
		this.yrExp                          = yrExp;
		this.ibmWrkLocInd                   = ibmWrkLocInd;
		this.fedCntrctInd                   = fedCntrctInd;
		this.supplrCmtTxt                   = supplrCmtTxt;
		this.initUsrId                      = initUsrId;
		this.reqUsrId                       = reqUsrId;
		this.techCoordUsrId                 = techCoordUsrId;
		this.dueDt                          = dueDt;
		this.eduReqTxt                      = eduReqTxt;
		this.wrkLocStAddr                   = wrkLocStAddr;
		this.bndLvl                         = bndLvl;
		this.secWrkLoc                      = secWrkLoc;
		this.passThruSklInd                 = passThruSklInd;
		this.ctznshpVisaPref                = ctznshpVisaPref;
		this.csaQty                         = csaQty;
		this.refCandFrstNm                  = refCandFrstNm;
		this.refCandMidNm                   = refCandMidNm;
		this.refCandLstNm                   = refCandLstNm;
		this.refCandPhnNum                  = refCandPhnNum;
		this.refCandEmalAddr                = refCandEmalAddr;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.candPrcLvlCd                   = candPrcLvlCd;
		this.csaJrssOvrrdResnCd             = csaJrssOvrrdResnCd;
		this.cntrReqTypId                   = cntrReqTypId;
		this.sapCmpnyId                     = sapCmpnyId;
		this.busunitId                      = busunitId;
		
	}
    
	// Define full constructor
	public SubResrcReqTDim (
		  int        recordId
		, Long     subResrcReqId
		, Long     opnsetId
		, Short   subReqIndxNum
		, String     delFlg
		, String     csaReqId
		, String     csaReqUrl
		, Timestamp  csaConfT
		, String     yrExp
		, String     ibmWrkLocInd
		, String     fedCntrctInd
		, String       supplrCmtTxt
		, Long     initUsrId
		, Long     reqUsrId
		, Long     techCoordUsrId
		, Date       dueDt
		, String     eduReqTxt
		, String     wrkLocStAddr
		, String     bndLvl
		, String     secWrkLoc
		, String     passThruSklInd
		, String     ctznshpVisaPref
		, Short   csaQty
		, String     refCandFrstNm
		, String     refCandMidNm
		, String     refCandLstNm
		, String     refCandPhnNum
		, String     refCandEmalAddr
		, String     jobRolTypDesc
		, String     cetJobRolCd
		, String     sklstTypDesc
		, String     cetSklstCd
		, String     candPrcLvlCd
		, String     csaJrssOvrrdResnCd
		, Long     cntrReqTypId
		, Long     sapCmpnyId
		, Long     busunitId
	) {
		this.recordId                       = recordId;
		this.subResrcReqId                  = subResrcReqId;
		this.opnsetId                       = opnsetId;
		this.subReqIndxNum                  = subReqIndxNum;
		this.delFlg                         = delFlg;
		this.csaReqId                       = csaReqId;
		this.csaReqUrl                      = csaReqUrl;
		this.csaConfT                       = csaConfT;
		this.yrExp                          = yrExp;
		this.ibmWrkLocInd                   = ibmWrkLocInd;
		this.fedCntrctInd                   = fedCntrctInd;
		this.supplrCmtTxt                   = supplrCmtTxt;
		this.initUsrId                      = initUsrId;
		this.reqUsrId                       = reqUsrId;
		this.techCoordUsrId                 = techCoordUsrId;
		this.dueDt                          = dueDt;
		this.eduReqTxt                      = eduReqTxt;
		this.wrkLocStAddr                   = wrkLocStAddr;
		this.bndLvl                         = bndLvl;
		this.secWrkLoc                      = secWrkLoc;
		this.passThruSklInd                 = passThruSklInd;
		this.ctznshpVisaPref                = ctznshpVisaPref;
		this.csaQty                         = csaQty;
		this.refCandFrstNm                  = refCandFrstNm;
		this.refCandMidNm                   = refCandMidNm;
		this.refCandLstNm                   = refCandLstNm;
		this.refCandPhnNum                  = refCandPhnNum;
		this.refCandEmalAddr                = refCandEmalAddr;
		this.jobRolTypDesc                  = jobRolTypDesc;
		this.cetJobRolCd                    = cetJobRolCd;
		this.sklstTypDesc                   = sklstTypDesc;
		this.cetSklstCd                     = cetSklstCd;
		this.candPrcLvlCd                   = candPrcLvlCd;
		this.csaJrssOvrrdResnCd             = csaJrssOvrrdResnCd;
		this.cntrReqTypId                   = cntrReqTypId;
		this.sapCmpnyId                     = sapCmpnyId;
		this.busunitId                      = busunitId;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.subResrcReqId)
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
                    
		SubResrcReqTDim other = (SubResrcReqTDim) obj;
		if (
            this.subResrcReqId.equals(other.getSubResrcReqId())
         && this.opnsetId.equals(other.getOpnsetId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.subResrcReqId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.subReqIndxNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.delFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.csaReqId))
        + "," + Helpers.formatCsvField(String.valueOf(this.csaReqUrl))
        + "," + Helpers.formatCsvField(String.valueOf(this.csaConfT))
        + "," + Helpers.formatCsvField(String.valueOf(this.yrExp))
        + "," + Helpers.formatCsvField(String.valueOf(this.ibmWrkLocInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.fedCntrctInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.supplrCmtTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.initUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.reqUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.techCoordUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.dueDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.eduReqTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkLocStAddr))
        + "," + Helpers.formatCsvField(String.valueOf(this.bndLvl))
        + "," + Helpers.formatCsvField(String.valueOf(this.secWrkLoc))
        + "," + Helpers.formatCsvField(String.valueOf(this.passThruSklInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctznshpVisaPref))
        + "," + Helpers.formatCsvField(String.valueOf(this.csaQty))
        + "," + Helpers.formatCsvField(String.valueOf(this.refCandFrstNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.refCandMidNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.refCandLstNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.refCandPhnNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.refCandEmalAddr))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRolTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetJobRolCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sklstTypDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.cetSklstCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.candPrcLvlCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.csaJrssOvrrdResnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntrReqTypId))
        + "," + Helpers.formatCsvField(String.valueOf(this.sapCmpnyId))
        + "," + Helpers.formatCsvField(String.valueOf(this.busunitId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SUB_RESRC_REQ_ID")
        + "," + Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("SUB_REQ_INDX_NUM")
        + "," + Helpers.formatCsvField("DEL_FLG")
        + "," + Helpers.formatCsvField("CSA_REQ_ID")
        + "," + Helpers.formatCsvField("CSA_REQ_URL")
        + "," + Helpers.formatCsvField("CSA_CONF_T")
        + "," + Helpers.formatCsvField("YR_EXP")
        + "," + Helpers.formatCsvField("IBM_WRK_LOC_IND")
        + "," + Helpers.formatCsvField("FED_CNTRCT_IND")
        + "," + Helpers.formatCsvField("SUPPLR_CMT_TXT")
        + "," + Helpers.formatCsvField("INIT_USR_ID")
        + "," + Helpers.formatCsvField("REQ_USR_ID")
        + "," + Helpers.formatCsvField("TECH_COORD_USR_ID")
        + "," + Helpers.formatCsvField("DUE_DT")
        + "," + Helpers.formatCsvField("EDU_REQ_TXT")
        + "," + Helpers.formatCsvField("WRK_LOC_ST_ADDR")
        + "," + Helpers.formatCsvField("BND_LVL")
        + "," + Helpers.formatCsvField("SEC_WRK_LOC")
        + "," + Helpers.formatCsvField("PASS_THRU_SKL_IND")
        + "," + Helpers.formatCsvField("CTZNSHP_VISA_PREF")
        + "," + Helpers.formatCsvField("CSA_QTY")
        + "," + Helpers.formatCsvField("REF_CAND_FRST_NM")
        + "," + Helpers.formatCsvField("REF_CAND_MID_NM")
        + "," + Helpers.formatCsvField("REF_CAND_LST_NM")
        + "," + Helpers.formatCsvField("REF_CAND_PHN_NUM")
        + "," + Helpers.formatCsvField("REF_CAND_EMAL_ADDR")
        + "," + Helpers.formatCsvField("JOB_ROL_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_JOB_ROL_CD")
        + "," + Helpers.formatCsvField("SKLST_TYP_DESC")
        + "," + Helpers.formatCsvField("CET_SKLST_CD")
        + "," + Helpers.formatCsvField("CAND_PRC_LVL_CD")
        + "," + Helpers.formatCsvField("CSA_JRSS_OVRRD_RESN_CD")
        + "," + Helpers.formatCsvField("CNTR_REQ_TYP_ID")
        + "," + Helpers.formatCsvField("SAP_CMPNY_ID")
        + "," + Helpers.formatCsvField("BUSUNIT_ID")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Long getSubResrcReqId() {
		return subResrcReqId;
	}
	public void setSubResrcReqId(Long subResrcReqId) {
		this.subResrcReqId = subResrcReqId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public Short getSubReqIndxNum() {
		return subReqIndxNum;
	}
	public void setSubReqIndxNum(Short subReqIndxNum) {
		this.subReqIndxNum = subReqIndxNum;
	}
	public String getDelFlg() {
		return delFlg;
	}
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}
	public String getCsaReqId() {
		return csaReqId;
	}
	public void setCsaReqId(String csaReqId) {
		this.csaReqId = csaReqId;
	}
	public String getCsaReqUrl() {
		return csaReqUrl;
	}
	public void setCsaReqUrl(String csaReqUrl) {
		this.csaReqUrl = csaReqUrl;
	}
	public Timestamp getCsaConfT() {
		return csaConfT;
	}
	public void setCsaConfT(Timestamp csaConfT) {
		this.csaConfT = csaConfT;
	}
	public String getYrExp() {
		return yrExp;
	}
	public void setYrExp(String yrExp) {
		this.yrExp = yrExp;
	}
	public String getIbmWrkLocInd() {
		return ibmWrkLocInd;
	}
	public void setIbmWrkLocInd(String ibmWrkLocInd) {
		this.ibmWrkLocInd = ibmWrkLocInd;
	}
	public String getFedCntrctInd() {
		return fedCntrctInd;
	}
	public void setFedCntrctInd(String fedCntrctInd) {
		this.fedCntrctInd = fedCntrctInd;
	}
	public String getSupplrCmtTxt() {
		return supplrCmtTxt;
	}
	public void setSupplrCmtTxt(String supplrCmtTxt) {
		this.supplrCmtTxt = supplrCmtTxt;
	}
	public Long getInitUsrId() {
		return initUsrId;
	}
	public void setInitUsrId(Long initUsrId) {
		this.initUsrId = initUsrId;
	}
	public Long getReqUsrId() {
		return reqUsrId;
	}
	public void setReqUsrId(Long reqUsrId) {
		this.reqUsrId = reqUsrId;
	}
	public Long getTechCoordUsrId() {
		return techCoordUsrId;
	}
	public void setTechCoordUsrId(Long techCoordUsrId) {
		this.techCoordUsrId = techCoordUsrId;
	}
	public Date getDueDt() {
		return dueDt;
	}
	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
	}
	public String getEduReqTxt() {
		return eduReqTxt;
	}
	public void setEduReqTxt(String eduReqTxt) {
		this.eduReqTxt = eduReqTxt;
	}
	public String getWrkLocStAddr() {
		return wrkLocStAddr;
	}
	public void setWrkLocStAddr(String wrkLocStAddr) {
		this.wrkLocStAddr = wrkLocStAddr;
	}
	public String getBndLvl() {
		return bndLvl;
	}
	public void setBndLvl(String bndLvl) {
		this.bndLvl = bndLvl;
	}
	public String getSecWrkLoc() {
		return secWrkLoc;
	}
	public void setSecWrkLoc(String secWrkLoc) {
		this.secWrkLoc = secWrkLoc;
	}
	public String getPassThruSklInd() {
		return passThruSklInd;
	}
	public void setPassThruSklInd(String passThruSklInd) {
		this.passThruSklInd = passThruSklInd;
	}
	public String getCtznshpVisaPref() {
		return ctznshpVisaPref;
	}
	public void setCtznshpVisaPref(String ctznshpVisaPref) {
		this.ctznshpVisaPref = ctznshpVisaPref;
	}
	public Short getCsaQty() {
		return csaQty;
	}
	public void setCsaQty(Short csaQty) {
		this.csaQty = csaQty;
	}
	public String getRefCandFrstNm() {
		return refCandFrstNm;
	}
	public void setRefCandFrstNm(String refCandFrstNm) {
		this.refCandFrstNm = refCandFrstNm;
	}
	public String getRefCandMidNm() {
		return refCandMidNm;
	}
	public void setRefCandMidNm(String refCandMidNm) {
		this.refCandMidNm = refCandMidNm;
	}
	public String getRefCandLstNm() {
		return refCandLstNm;
	}
	public void setRefCandLstNm(String refCandLstNm) {
		this.refCandLstNm = refCandLstNm;
	}
	public String getRefCandPhnNum() {
		return refCandPhnNum;
	}
	public void setRefCandPhnNum(String refCandPhnNum) {
		this.refCandPhnNum = refCandPhnNum;
	}
	public String getRefCandEmalAddr() {
		return refCandEmalAddr;
	}
	public void setRefCandEmalAddr(String refCandEmalAddr) {
		this.refCandEmalAddr = refCandEmalAddr;
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
	public String getCandPrcLvlCd() {
		return candPrcLvlCd;
	}
	public void setCandPrcLvlCd(String candPrcLvlCd) {
		this.candPrcLvlCd = candPrcLvlCd;
	}
	public String getCsaJrssOvrrdResnCd() {
		return csaJrssOvrrdResnCd;
	}
	public void setCsaJrssOvrrdResnCd(String csaJrssOvrrdResnCd) {
		this.csaJrssOvrrdResnCd = csaJrssOvrrdResnCd;
	}
	public Long getCntrReqTypId() {
		return cntrReqTypId;
	}
	public void setCntrReqTypId(Long cntrReqTypId) {
		this.cntrReqTypId = cntrReqTypId;
	}
	public Long getSapCmpnyId() {
		return sapCmpnyId;
	}
	public void setSapCmpnyId(Long sapCmpnyId) {
		this.sapCmpnyId = sapCmpnyId;
	}
	public Long getBusunitId() {
		return busunitId;
	}
	public void setBusunitId(Long busunitId) {
		this.busunitId = busunitId;
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