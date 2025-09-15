package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetTDim",baseTableName="BCSPMPC.OPNSET_T",isDimension=false)
public class OpnsetTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_ID",keySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="SET_STAT_ID")
	private Long     setStatId;
	@DbColumn(columnName="SET_TYP_CD")
	private String     setTypCd;
	@DbColumn(columnName="CRE_USR_ID")
	private Long     creUsrId;
	@DbColumn(columnName="OWNR_USR_ID")
	private Long     ownrUsrId;
	@DbColumn(columnName="DELG_USR_ID")
	private Long     delgUsrId;
	@DbColumn(columnName="OPNSET_TITL")
	private String     opnsetTitl;
	@DbColumn(columnName="PROJ_DESC_TXT")
	private String     projDescTxt;
	@DbColumn(columnName="POS_DESC_TXT")
	private String     posDescTxt;
	@DbColumn(columnName="STRT_DT")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  strtDt;
	@DbColumn(columnName="END_DT")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  endDt;
	@DbColumn(columnName="NUM_SET_REQ")
	private Short   numSetReq;
	@DbColumn(columnName="NUM_SET_FILD")
	private Short   numSetFild;
	@DbColumn(columnName="BND_LOW")
	private String     bndLow;
	@DbColumn(columnName="BND_HIGH")
	private String     bndHigh;
	@DbColumn(columnName="REQ_SKL_TXT")
	private String     reqSklTxt;
	@DbColumn(columnName="OPT_SKL_TXT")
	private String     optSklTxt;
	@DbColumn(columnName="WRK_CTY_NM")
	private String     wrkCtyNm;
	@DbColumn(columnName="WRK_ZIP_PSTL_CD")
	private String     wrkZipPstlCd;
	@DbColumn(columnName="WRK_RMT_IND")
	private String     wrkRmtInd;
	@DbColumn(columnName="WKLY_HR")
	private Double     wklyHr;
	@DbColumn(columnName="PAY_TRVL_IND")
	private String     payTrvlInd;
	@DbColumn(columnName="CAS_CMPLNT_IND")
	private String     casCmplntInd;
	@DbColumn(columnName="CTZNSHP_REQ_IND")
	private String     ctznshpReqInd;
	@DbColumn(columnName="CNTRY_SEC_CLRNCE_ID")
	private Long     cntrySecClrnceId;
	@DbColumn(columnName="CNTRCT_OWNG_ORG_ID")
	private Long     cntrctOwngOrgId;
	@DbColumn(columnName="CNTRCT_STAT_ID")
	private Long     cntrctStatId;
	@DbColumn(columnName="CNTRCT_TYP_ID")
	private Long     cntrctTypId;
	@DbColumn(columnName="VNDR_ID")
	private Long     vndrId;
	@DbColumn(columnName="CAND_TRK_TYP_ID")
	private Long     candTrkTypId;
	@DbColumn(columnName="ADDL_CMT_TXT")
	private String       addlCmtTxt;
	@DbColumn(columnName="OPPOR_OWNR_NOTES_ID")
	private String     opporOwnrNotesId;
	@DbColumn(columnName="HIR_REQ_NUM")
	private String     hirReqNum;
	@DbColumn(columnName="REQ_CMPNY_NM")
	private String     reqCmpnyNm;
	@DbColumn(columnName="CRE_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  creT;
	@DbColumn(columnName="LST_UPDT_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  lstUpdtT;
	@DbColumn(columnName="SRC_APP_NM")
	private String     srcAppNm;
	@DbColumn(columnName="OPPOR_ID")
	private Long     opporId;
	@DbColumn(columnName="WRK_CNTRY_CD")
	private String     wrkCntryCd;
	@DbColumn(columnName="WRK_STTE_PROV_ID")
	private Long     wrkStteProvId;
	@DbColumn(columnName="CLNT_CONF_IND")
	private String     clntConfInd;
	@DbColumn(columnName="CLM_ACCT_ID")
	private String     clmAcctId;
	@DbColumn(columnName="OPN_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  opnT;
	@DbColumn(columnName="WTHDRW_CLOS_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  wthdrwClosT;
	@DbColumn(columnName="STAF_PLAN_ID")
	private Long     stafPlanId;
	@DbColumn(columnName="LABR_CATGRY_CD")
	private String     labrCatgryCd;
	@DbColumn(columnName="CATGRY_TEAM_NM")
	private String     catgryTeamNm;
	@DbColumn(columnName="EST_CSTRTE_AMT_USD")
	private Double     estCstrteAmtUsd;
	@DbColumn(columnName="EXT_REF_MAT_LNK")
	private String     extRefMatLnk;
	@DbColumn(columnName="LST_UPDT_USR_ID")
	private Long     lstUpdtUsrId;
	@DbColumn(columnName="OPN_USR_ID")
	private Long     opnUsrId;
	@DbColumn(columnName="FIN_INTLCK_ID")
	private Long     finIntlckId;
	@DbColumn(columnName="APPROV_BY_ID")
	private Long     approvById;
	@DbColumn(columnName="LST_MOD_SYS_EVNT_ID")
	private Long     lstModSysEvntId;
	@DbColumn(columnName="PRIRTY_RNK_NUM")
	private Short   prirtyRnkNum;
	@DbColumn(columnName="APPROV_BY_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  approvByT;
	@DbColumn(columnName="PROJ_CONT_ID")
	private Long     projContId;
	@DbColumn(columnName="BCKFIL_RESN_CD")
	private String     bckfilResnCd;
	@DbColumn(columnName="BCKFIL_TYP_CD")
	private String     bckfilTypCd;
	@DbColumn(columnName="FULFILL_RISK_ID")
	private Short   fulfillRiskId;
	@DbColumn(columnName="ORI_SBMTD_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  oriSbmtdT;
	@DbColumn(columnName="RB_SET_ID")
	private Integer        rbSetId;
	@DbColumn(columnName="REPL_SET_ID")
	private Integer        replSetId;
	@DbColumn(columnName="LOC_USE")
	private String     locUse;
	@DbColumn(columnName="CLNT_TYP")
	private String     clntTyp;
	@DbColumn(columnName="TRAM_REQ_ID")
	private String     tramReqId;
	@DbColumn(columnName="KEY_ROLE_FIELD")
	private String     keyRoleField;
	@DbColumn(columnName="OPPOR_CALL_STATUS_ID")
	private Integer        opporCallStatusId;
	@DbColumn(columnName="PCW_FLAG")
	private String     pcwFlag;
	@DbColumn(columnName="BCKFIL_NM")
	private String     bckfilNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetTDim () {
		
	}
	
	// Define natural key constructor
	public OpnsetTDim (
      Long     opnsetId
	) {
		this.opnsetId                       = opnsetId;
		
	}
	
	// Define base constructor
	public OpnsetTDim (
      Long     opnsetId
    , Long     setStatId
    , String     setTypCd
    , Long     creUsrId
    , Long     ownrUsrId
    , Long     delgUsrId
    , String     opnsetTitl
    , String     projDescTxt
    , String     posDescTxt
    , Timestamp  strtDt
    , Timestamp  endDt
    , Short   numSetReq
    , Short   numSetFild
    , String     bndLow
    , String     bndHigh
    , String     reqSklTxt
    , String     optSklTxt
    , String     wrkCtyNm
    , String     wrkZipPstlCd
    , String     wrkRmtInd
    , Double     wklyHr
    , String     payTrvlInd
    , String     casCmplntInd
    , String     ctznshpReqInd
    , Long     cntrySecClrnceId
    , Long     cntrctOwngOrgId
    , Long     cntrctStatId
    , Long     cntrctTypId
    , Long     vndrId
    , Long     candTrkTypId
    , String       addlCmtTxt
    , String     opporOwnrNotesId
    , String     hirReqNum
    , String     reqCmpnyNm
    , Timestamp  creT
    , Timestamp  lstUpdtT
    , String     srcAppNm
    , Long     opporId
    , String     wrkCntryCd
    , Long     wrkStteProvId
    , String     clntConfInd
    , String     clmAcctId
    , Timestamp  opnT
    , Timestamp  wthdrwClosT
    , Long     stafPlanId
    , String     labrCatgryCd
    , String     catgryTeamNm
    , Double     estCstrteAmtUsd
    , String     extRefMatLnk
    , Long     lstUpdtUsrId
    , Long     opnUsrId
    , Long     finIntlckId
    , Long     approvById
    , Long     lstModSysEvntId
    , Short   prirtyRnkNum
    , Timestamp  approvByT
    , Long     projContId
    , String     bckfilResnCd
    , String     bckfilTypCd
    , Short   fulfillRiskId
    , Timestamp  oriSbmtdT
    , Integer        rbSetId
    , Integer        replSetId
    , String     locUse
    , String     clntTyp
    , String     tramReqId
    , String     keyRoleField
    , Integer        opporCallStatusId
    , String     pcwFlag
    , String     bckfilNm
	) {
		this.opnsetId                       = opnsetId;
		this.setStatId                      = setStatId;
		this.setTypCd                       = setTypCd;
		this.creUsrId                       = creUsrId;
		this.ownrUsrId                      = ownrUsrId;
		this.delgUsrId                      = delgUsrId;
		this.opnsetTitl                     = opnsetTitl;
		this.projDescTxt                    = projDescTxt;
		this.posDescTxt                     = posDescTxt;
		this.strtDt                         = strtDt;
		this.endDt                          = endDt;
		this.numSetReq                      = numSetReq;
		this.numSetFild                     = numSetFild;
		this.bndLow                         = bndLow;
		this.bndHigh                        = bndHigh;
		this.reqSklTxt                      = reqSklTxt;
		this.optSklTxt                      = optSklTxt;
		this.wrkCtyNm                       = wrkCtyNm;
		this.wrkZipPstlCd                   = wrkZipPstlCd;
		this.wrkRmtInd                      = wrkRmtInd;
		this.wklyHr                         = wklyHr;
		this.payTrvlInd                     = payTrvlInd;
		this.casCmplntInd                   = casCmplntInd;
		this.ctznshpReqInd                  = ctznshpReqInd;
		this.cntrySecClrnceId               = cntrySecClrnceId;
		this.cntrctOwngOrgId                = cntrctOwngOrgId;
		this.cntrctStatId                   = cntrctStatId;
		this.cntrctTypId                    = cntrctTypId;
		this.vndrId                         = vndrId;
		this.candTrkTypId                   = candTrkTypId;
		this.addlCmtTxt                     = addlCmtTxt;
		this.opporOwnrNotesId               = opporOwnrNotesId;
		this.hirReqNum                      = hirReqNum;
		this.reqCmpnyNm                     = reqCmpnyNm;
		this.creT                           = creT;
		this.lstUpdtT                       = lstUpdtT;
		this.srcAppNm                       = srcAppNm;
		this.opporId                        = opporId;
		this.wrkCntryCd                     = wrkCntryCd;
		this.wrkStteProvId                  = wrkStteProvId;
		this.clntConfInd                    = clntConfInd;
		this.clmAcctId                      = clmAcctId;
		this.opnT                           = opnT;
		this.wthdrwClosT                    = wthdrwClosT;
		this.stafPlanId                     = stafPlanId;
		this.labrCatgryCd                   = labrCatgryCd;
		this.catgryTeamNm                   = catgryTeamNm;
		this.estCstrteAmtUsd                = estCstrteAmtUsd;
		this.extRefMatLnk                   = extRefMatLnk;
		this.lstUpdtUsrId                   = lstUpdtUsrId;
		this.opnUsrId                       = opnUsrId;
		this.finIntlckId                    = finIntlckId;
		this.approvById                     = approvById;
		this.lstModSysEvntId                = lstModSysEvntId;
		this.prirtyRnkNum                   = prirtyRnkNum;
		this.approvByT                      = approvByT;
		this.projContId                     = projContId;
		this.bckfilResnCd                   = bckfilResnCd;
		this.bckfilTypCd                    = bckfilTypCd;
		this.fulfillRiskId                  = fulfillRiskId;
		this.oriSbmtdT                      = oriSbmtdT;
		this.rbSetId                        = rbSetId;
		this.replSetId                      = replSetId;
		this.locUse                         = locUse;
		this.clntTyp                        = clntTyp;
		this.tramReqId                      = tramReqId;
		this.keyRoleField                   = keyRoleField;
		this.opporCallStatusId              = opporCallStatusId;
		this.pcwFlag                        = pcwFlag;
		this.bckfilNm                       = bckfilNm;
		
	}
    
	// Define full constructor
	public OpnsetTDim (
		  int        recordId
		, Long     opnsetId
		, Long     setStatId
		, String     setTypCd
		, Long     creUsrId
		, Long     ownrUsrId
		, Long     delgUsrId
		, String     opnsetTitl
		, String     projDescTxt
		, String     posDescTxt
		, Timestamp  strtDt
		, Timestamp  endDt
		, Short   numSetReq
		, Short   numSetFild
		, String     bndLow
		, String     bndHigh
		, String     reqSklTxt
		, String     optSklTxt
		, String     wrkCtyNm
		, String     wrkZipPstlCd
		, String     wrkRmtInd
		, Double     wklyHr
		, String     payTrvlInd
		, String     casCmplntInd
		, String     ctznshpReqInd
		, Long     cntrySecClrnceId
		, Long     cntrctOwngOrgId
		, Long     cntrctStatId
		, Long     cntrctTypId
		, Long     vndrId
		, Long     candTrkTypId
		, String       addlCmtTxt
		, String     opporOwnrNotesId
		, String     hirReqNum
		, String     reqCmpnyNm
		, Timestamp  creT
		, Timestamp  lstUpdtT
		, String     srcAppNm
		, Long     opporId
		, String     wrkCntryCd
		, Long     wrkStteProvId
		, String     clntConfInd
		, String     clmAcctId
		, Timestamp  opnT
		, Timestamp  wthdrwClosT
		, Long     stafPlanId
		, String     labrCatgryCd
		, String     catgryTeamNm
		, Double     estCstrteAmtUsd
		, String     extRefMatLnk
		, Long     lstUpdtUsrId
		, Long     opnUsrId
		, Long     finIntlckId
		, Long     approvById
		, Long     lstModSysEvntId
		, Short   prirtyRnkNum
		, Timestamp  approvByT
		, Long     projContId
		, String     bckfilResnCd
		, String     bckfilTypCd
		, Short   fulfillRiskId
		, Timestamp  oriSbmtdT
		, Integer        rbSetId
		, Integer        replSetId
		, String     locUse
		, String     clntTyp
		, String     tramReqId
		, String     keyRoleField
		, Integer        opporCallStatusId
		, String     pcwFlag
		, String     bckfilNm
	) {
		this.recordId                       = recordId;
		this.opnsetId                       = opnsetId;
		this.setStatId                      = setStatId;
		this.setTypCd                       = setTypCd;
		this.creUsrId                       = creUsrId;
		this.ownrUsrId                      = ownrUsrId;
		this.delgUsrId                      = delgUsrId;
		this.opnsetTitl                     = opnsetTitl;
		this.projDescTxt                    = projDescTxt;
		this.posDescTxt                     = posDescTxt;
		this.strtDt                         = strtDt;
		this.endDt                          = endDt;
		this.numSetReq                      = numSetReq;
		this.numSetFild                     = numSetFild;
		this.bndLow                         = bndLow;
		this.bndHigh                        = bndHigh;
		this.reqSklTxt                      = reqSklTxt;
		this.optSklTxt                      = optSklTxt;
		this.wrkCtyNm                       = wrkCtyNm;
		this.wrkZipPstlCd                   = wrkZipPstlCd;
		this.wrkRmtInd                      = wrkRmtInd;
		this.wklyHr                         = wklyHr;
		this.payTrvlInd                     = payTrvlInd;
		this.casCmplntInd                   = casCmplntInd;
		this.ctznshpReqInd                  = ctznshpReqInd;
		this.cntrySecClrnceId               = cntrySecClrnceId;
		this.cntrctOwngOrgId                = cntrctOwngOrgId;
		this.cntrctStatId                   = cntrctStatId;
		this.cntrctTypId                    = cntrctTypId;
		this.vndrId                         = vndrId;
		this.candTrkTypId                   = candTrkTypId;
		this.addlCmtTxt                     = addlCmtTxt;
		this.opporOwnrNotesId               = opporOwnrNotesId;
		this.hirReqNum                      = hirReqNum;
		this.reqCmpnyNm                     = reqCmpnyNm;
		this.creT                           = creT;
		this.lstUpdtT                       = lstUpdtT;
		this.srcAppNm                       = srcAppNm;
		this.opporId                        = opporId;
		this.wrkCntryCd                     = wrkCntryCd;
		this.wrkStteProvId                  = wrkStteProvId;
		this.clntConfInd                    = clntConfInd;
		this.clmAcctId                      = clmAcctId;
		this.opnT                           = opnT;
		this.wthdrwClosT                    = wthdrwClosT;
		this.stafPlanId                     = stafPlanId;
		this.labrCatgryCd                   = labrCatgryCd;
		this.catgryTeamNm                   = catgryTeamNm;
		this.estCstrteAmtUsd                = estCstrteAmtUsd;
		this.extRefMatLnk                   = extRefMatLnk;
		this.lstUpdtUsrId                   = lstUpdtUsrId;
		this.opnUsrId                       = opnUsrId;
		this.finIntlckId                    = finIntlckId;
		this.approvById                     = approvById;
		this.lstModSysEvntId                = lstModSysEvntId;
		this.prirtyRnkNum                   = prirtyRnkNum;
		this.approvByT                      = approvByT;
		this.projContId                     = projContId;
		this.bckfilResnCd                   = bckfilResnCd;
		this.bckfilTypCd                    = bckfilTypCd;
		this.fulfillRiskId                  = fulfillRiskId;
		this.oriSbmtdT                      = oriSbmtdT;
		this.rbSetId                        = rbSetId;
		this.replSetId                      = replSetId;
		this.locUse                         = locUse;
		this.clntTyp                        = clntTyp;
		this.tramReqId                      = tramReqId;
		this.keyRoleField                   = keyRoleField;
		this.opporCallStatusId              = opporCallStatusId;
		this.pcwFlag                        = pcwFlag;
		this.bckfilNm                       = bckfilNm;
		
	}
	
	@Override
	public String getCode() { 
		return (String.valueOf(this.opnsetId))
		;
	}
	public String getDescription() { 
		return this.opnsetTitl; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OpnsetTDim other = (OpnsetTDim) obj;
		if (
            this.opnsetId.equals(other.getOpnsetId())
         && this.setStatId.equals(other.getSetStatId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.setStatId))
        + "," + Helpers.formatCsvField(String.valueOf(this.setTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.creUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.ownrUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.delgUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetTitl))
        + "," + Helpers.formatCsvField(String.valueOf(this.projDescTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.posDescTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.strtDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.endDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.numSetReq))
        + "," + Helpers.formatCsvField(String.valueOf(this.numSetFild))
        + "," + Helpers.formatCsvField(String.valueOf(this.bndLow))
        + "," + Helpers.formatCsvField(String.valueOf(this.bndHigh))
        + "," + Helpers.formatCsvField(String.valueOf(this.reqSklTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.optSklTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkCtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkZipPstlCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkRmtInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wklyHr))
        + "," + Helpers.formatCsvField(String.valueOf(this.payTrvlInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.casCmplntInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctznshpReqInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntrySecClrnceId))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntrctOwngOrgId))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntrctStatId))
        + "," + Helpers.formatCsvField(String.valueOf(this.cntrctTypId))
        + "," + Helpers.formatCsvField(String.valueOf(this.vndrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.candTrkTypId))
        + "," + Helpers.formatCsvField(String.valueOf(this.addlCmtTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.opporOwnrNotesId))
        + "," + Helpers.formatCsvField(String.valueOf(this.hirReqNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.reqCmpnyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.creT))
        + "," + Helpers.formatCsvField(String.valueOf(this.lstUpdtT))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcAppNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.opporId))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkCntryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wrkStteProvId))
        + "," + Helpers.formatCsvField(String.valueOf(this.clntConfInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.clmAcctId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnT))
        + "," + Helpers.formatCsvField(String.valueOf(this.wthdrwClosT))
        + "," + Helpers.formatCsvField(String.valueOf(this.stafPlanId))
        + "," + Helpers.formatCsvField(String.valueOf(this.labrCatgryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.catgryTeamNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.estCstrteAmtUsd))
        + "," + Helpers.formatCsvField(String.valueOf(this.extRefMatLnk))
        + "," + Helpers.formatCsvField(String.valueOf(this.lstUpdtUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.finIntlckId))
        + "," + Helpers.formatCsvField(String.valueOf(this.approvById))
        + "," + Helpers.formatCsvField(String.valueOf(this.lstModSysEvntId))
        + "," + Helpers.formatCsvField(String.valueOf(this.prirtyRnkNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.approvByT))
        + "," + Helpers.formatCsvField(String.valueOf(this.projContId))
        + "," + Helpers.formatCsvField(String.valueOf(this.bckfilResnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.bckfilTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.fulfillRiskId))
        + "," + Helpers.formatCsvField(String.valueOf(this.oriSbmtdT))
        + "," + Helpers.formatCsvField(String.valueOf(this.rbSetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.replSetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.locUse))
        + "," + Helpers.formatCsvField(String.valueOf(this.clntTyp))
        + "," + Helpers.formatCsvField(String.valueOf(this.tramReqId))
        + "," + Helpers.formatCsvField(String.valueOf(this.keyRoleField))
        + "," + Helpers.formatCsvField(String.valueOf(this.opporCallStatusId))
        + "," + Helpers.formatCsvField(String.valueOf(this.pcwFlag))
        + "," + Helpers.formatCsvField(String.valueOf(this.bckfilNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("SET_STAT_ID")
        + "," + Helpers.formatCsvField("SET_TYP_CD")
        + "," + Helpers.formatCsvField("CRE_USR_ID")
        + "," + Helpers.formatCsvField("OWNR_USR_ID")
        + "," + Helpers.formatCsvField("DELG_USR_ID")
        + "," + Helpers.formatCsvField("OPNSET_TITL")
        + "," + Helpers.formatCsvField("PROJ_DESC_TXT")
        + "," + Helpers.formatCsvField("POS_DESC_TXT")
        + "," + Helpers.formatCsvField("STRT_DT")
        + "," + Helpers.formatCsvField("END_DT")
        + "," + Helpers.formatCsvField("NUM_SET_REQ")
        + "," + Helpers.formatCsvField("NUM_SET_FILD")
        + "," + Helpers.formatCsvField("BND_LOW")
        + "," + Helpers.formatCsvField("BND_HIGH")
        + "," + Helpers.formatCsvField("REQ_SKL_TXT")
        + "," + Helpers.formatCsvField("OPT_SKL_TXT")
        + "," + Helpers.formatCsvField("WRK_CTY_NM")
        + "," + Helpers.formatCsvField("WRK_ZIP_PSTL_CD")
        + "," + Helpers.formatCsvField("WRK_RMT_IND")
        + "," + Helpers.formatCsvField("WKLY_HR")
        + "," + Helpers.formatCsvField("PAY_TRVL_IND")
        + "," + Helpers.formatCsvField("CAS_CMPLNT_IND")
        + "," + Helpers.formatCsvField("CTZNSHP_REQ_IND")
        + "," + Helpers.formatCsvField("CNTRY_SEC_CLRNCE_ID")
        + "," + Helpers.formatCsvField("CNTRCT_OWNG_ORG_ID")
        + "," + Helpers.formatCsvField("CNTRCT_STAT_ID")
        + "," + Helpers.formatCsvField("CNTRCT_TYP_ID")
        + "," + Helpers.formatCsvField("VNDR_ID")
        + "," + Helpers.formatCsvField("CAND_TRK_TYP_ID")
        + "," + Helpers.formatCsvField("ADDL_CMT_TXT")
        + "," + Helpers.formatCsvField("OPPOR_OWNR_NOTES_ID")
        + "," + Helpers.formatCsvField("HIR_REQ_NUM")
        + "," + Helpers.formatCsvField("REQ_CMPNY_NM")
        + "," + Helpers.formatCsvField("CRE_T")
        + "," + Helpers.formatCsvField("LST_UPDT_T")
        + "," + Helpers.formatCsvField("SRC_APP_NM")
        + "," + Helpers.formatCsvField("OPPOR_ID")
        + "," + Helpers.formatCsvField("WRK_CNTRY_CD")
        + "," + Helpers.formatCsvField("WRK_STTE_PROV_ID")
        + "," + Helpers.formatCsvField("CLNT_CONF_IND")
        + "," + Helpers.formatCsvField("CLM_ACCT_ID")
        + "," + Helpers.formatCsvField("OPN_T")
        + "," + Helpers.formatCsvField("WTHDRW_CLOS_T")
        + "," + Helpers.formatCsvField("STAF_PLAN_ID")
        + "," + Helpers.formatCsvField("LABR_CATGRY_CD")
        + "," + Helpers.formatCsvField("CATGRY_TEAM_NM")
        + "," + Helpers.formatCsvField("EST_CSTRTE_AMT_USD")
        + "," + Helpers.formatCsvField("EXT_REF_MAT_LNK")
        + "," + Helpers.formatCsvField("LST_UPDT_USR_ID")
        + "," + Helpers.formatCsvField("OPN_USR_ID")
        + "," + Helpers.formatCsvField("FIN_INTLCK_ID")
        + "," + Helpers.formatCsvField("APPROV_BY_ID")
        + "," + Helpers.formatCsvField("LST_MOD_SYS_EVNT_ID")
        + "," + Helpers.formatCsvField("PRIRTY_RNK_NUM")
        + "," + Helpers.formatCsvField("APPROV_BY_T")
        + "," + Helpers.formatCsvField("PROJ_CONT_ID")
        + "," + Helpers.formatCsvField("BCKFIL_RESN_CD")
        + "," + Helpers.formatCsvField("BCKFIL_TYP_CD")
        + "," + Helpers.formatCsvField("FULFILL_RISK_ID")
        + "," + Helpers.formatCsvField("ORI_SBMTD_T")
        + "," + Helpers.formatCsvField("RB_SET_ID")
        + "," + Helpers.formatCsvField("REPL_SET_ID")
        + "," + Helpers.formatCsvField("LOC_USE")
        + "," + Helpers.formatCsvField("CLNT_TYP")
        + "," + Helpers.formatCsvField("TRAM_REQ_ID")
        + "," + Helpers.formatCsvField("KEY_ROLE_FIELD")
        + "," + Helpers.formatCsvField("OPPOR_CALL_STATUS_ID")
        + "," + Helpers.formatCsvField("PCW_FLAG")
        + "," + Helpers.formatCsvField("BCKFIL_NM")
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
	public Long getSetStatId() {
		return setStatId;
	}
	public void setSetStatId(Long setStatId) {
		this.setStatId = setStatId;
	}
	public String getSetTypCd() {
		return setTypCd;
	}
	public void setSetTypCd(String setTypCd) {
		this.setTypCd = setTypCd;
	}
	public Long getCreUsrId() {
		return creUsrId;
	}
	public void setCreUsrId(Long creUsrId) {
		this.creUsrId = creUsrId;
	}
	public Long getOwnrUsrId() {
		return ownrUsrId;
	}
	public void setOwnrUsrId(Long ownrUsrId) {
		this.ownrUsrId = ownrUsrId;
	}
	public Long getDelgUsrId() {
		return delgUsrId;
	}
	public void setDelgUsrId(Long delgUsrId) {
		this.delgUsrId = delgUsrId;
	}
	public String getOpnsetTitl() {
		return opnsetTitl;
	}
	public void setOpnsetTitl(String opnsetTitl) {
		this.opnsetTitl = opnsetTitl;
	}
	public String getProjDescTxt() {
		return projDescTxt;
	}
	public void setProjDescTxt(String projDescTxt) {
		this.projDescTxt = projDescTxt;
	}
	public String getPosDescTxt() {
		return posDescTxt;
	}
	public void setPosDescTxt(String posDescTxt) {
		this.posDescTxt = posDescTxt;
	}
	public Timestamp getStrtDt() {
		return strtDt;
	}
	public void setStrtDt(Timestamp strtDt) {
		this.strtDt = strtDt;
	}
	public Timestamp getEndDt() {
		return endDt;
	}
	public void setEndDt(Timestamp endDt) {
		this.endDt = endDt;
	}
	public Short getNumSetReq() {
		return numSetReq;
	}
	public void setNumSetReq(Short numSetReq) {
		this.numSetReq = numSetReq;
	}
	public Short getNumSetFild() {
		return numSetFild;
	}
	public void setNumSetFild(Short numSetFild) {
		this.numSetFild = numSetFild;
	}
	public String getBndLow() {
		return bndLow;
	}
	public void setBndLow(String bndLow) {
		this.bndLow = bndLow;
	}
	public String getBndHigh() {
		return bndHigh;
	}
	public void setBndHigh(String bndHigh) {
		this.bndHigh = bndHigh;
	}
	public String getReqSklTxt() {
		return reqSklTxt;
	}
	public void setReqSklTxt(String reqSklTxt) {
		this.reqSklTxt = reqSklTxt;
	}
	public String getOptSklTxt() {
		return optSklTxt;
	}
	public void setOptSklTxt(String optSklTxt) {
		this.optSklTxt = optSklTxt;
	}
	public String getWrkCtyNm() {
		return wrkCtyNm;
	}
	public void setWrkCtyNm(String wrkCtyNm) {
		this.wrkCtyNm = wrkCtyNm;
	}
	public String getWrkZipPstlCd() {
		return wrkZipPstlCd;
	}
	public void setWrkZipPstlCd(String wrkZipPstlCd) {
		this.wrkZipPstlCd = wrkZipPstlCd;
	}
	public String getWrkRmtInd() {
		return wrkRmtInd;
	}
	public void setWrkRmtInd(String wrkRmtInd) {
		this.wrkRmtInd = wrkRmtInd;
	}
	public Double getWklyHr() {
		return wklyHr;
	}
	public void setWklyHr(Double wklyHr) {
		this.wklyHr = wklyHr;
	}
	public String getPayTrvlInd() {
		return payTrvlInd;
	}
	public void setPayTrvlInd(String payTrvlInd) {
		this.payTrvlInd = payTrvlInd;
	}
	public String getCasCmplntInd() {
		return casCmplntInd;
	}
	public void setCasCmplntInd(String casCmplntInd) {
		this.casCmplntInd = casCmplntInd;
	}
	public String getCtznshpReqInd() {
		return ctznshpReqInd;
	}
	public void setCtznshpReqInd(String ctznshpReqInd) {
		this.ctznshpReqInd = ctznshpReqInd;
	}
	public Long getCntrySecClrnceId() {
		return cntrySecClrnceId;
	}
	public void setCntrySecClrnceId(Long cntrySecClrnceId) {
		this.cntrySecClrnceId = cntrySecClrnceId;
	}
	public Long getCntrctOwngOrgId() {
		return cntrctOwngOrgId;
	}
	public void setCntrctOwngOrgId(Long cntrctOwngOrgId) {
		this.cntrctOwngOrgId = cntrctOwngOrgId;
	}
	public Long getCntrctStatId() {
		return cntrctStatId;
	}
	public void setCntrctStatId(Long cntrctStatId) {
		this.cntrctStatId = cntrctStatId;
	}
	public Long getCntrctTypId() {
		return cntrctTypId;
	}
	public void setCntrctTypId(Long cntrctTypId) {
		this.cntrctTypId = cntrctTypId;
	}
	public Long getVndrId() {
		return vndrId;
	}
	public void setVndrId(Long vndrId) {
		this.vndrId = vndrId;
	}
	public Long getCandTrkTypId() {
		return candTrkTypId;
	}
	public void setCandTrkTypId(Long candTrkTypId) {
		this.candTrkTypId = candTrkTypId;
	}
	public String getAddlCmtTxt() {
		return addlCmtTxt;
	}
	public void setAddlCmtTxt(String addlCmtTxt) {
		this.addlCmtTxt = addlCmtTxt;
	}
	public String getOpporOwnrNotesId() {
		return opporOwnrNotesId;
	}
	public void setOpporOwnrNotesId(String opporOwnrNotesId) {
		this.opporOwnrNotesId = opporOwnrNotesId;
	}
	public String getHirReqNum() {
		return hirReqNum;
	}
	public void setHirReqNum(String hirReqNum) {
		this.hirReqNum = hirReqNum;
	}
	public String getReqCmpnyNm() {
		return reqCmpnyNm;
	}
	public void setReqCmpnyNm(String reqCmpnyNm) {
		this.reqCmpnyNm = reqCmpnyNm;
	}
	public Timestamp getCreT() {
		return creT;
	}
	public void setCreT(Timestamp creT) {
		this.creT = creT;
	}
	public Timestamp getLstUpdtT() {
		return lstUpdtT;
	}
	public void setLstUpdtT(Timestamp lstUpdtT) {
		this.lstUpdtT = lstUpdtT;
	}
	public String getSrcAppNm() {
		return srcAppNm;
	}
	public void setSrcAppNm(String srcAppNm) {
		this.srcAppNm = srcAppNm;
	}
	public Long getOpporId() {
		return opporId;
	}
	public void setOpporId(Long opporId) {
		this.opporId = opporId;
	}
	public String getWrkCntryCd() {
		return wrkCntryCd;
	}
	public void setWrkCntryCd(String wrkCntryCd) {
		this.wrkCntryCd = wrkCntryCd;
	}
	public Long getWrkStteProvId() {
		return wrkStteProvId;
	}
	public void setWrkStteProvId(Long wrkStteProvId) {
		this.wrkStteProvId = wrkStteProvId;
	}
	public String getClntConfInd() {
		return clntConfInd;
	}
	public void setClntConfInd(String clntConfInd) {
		this.clntConfInd = clntConfInd;
	}
	public String getClmAcctId() {
		return clmAcctId;
	}
	public void setClmAcctId(String clmAcctId) {
		this.clmAcctId = clmAcctId;
	}
	public Timestamp getOpnT() {
		return opnT;
	}
	public void setOpnT(Timestamp opnT) {
		this.opnT = opnT;
	}
	public Timestamp getWthdrwClosT() {
		return wthdrwClosT;
	}
	public void setWthdrwClosT(Timestamp wthdrwClosT) {
		this.wthdrwClosT = wthdrwClosT;
	}
	public Long getStafPlanId() {
		return stafPlanId;
	}
	public void setStafPlanId(Long stafPlanId) {
		this.stafPlanId = stafPlanId;
	}
	public String getLabrCatgryCd() {
		return labrCatgryCd;
	}
	public void setLabrCatgryCd(String labrCatgryCd) {
		this.labrCatgryCd = labrCatgryCd;
	}
	public String getCatgryTeamNm() {
		return catgryTeamNm;
	}
	public void setCatgryTeamNm(String catgryTeamNm) {
		this.catgryTeamNm = catgryTeamNm;
	}
	public Double getEstCstrteAmtUsd() {
		return estCstrteAmtUsd;
	}
	public void setEstCstrteAmtUsd(Double estCstrteAmtUsd) {
		this.estCstrteAmtUsd = estCstrteAmtUsd;
	}
	public String getExtRefMatLnk() {
		return extRefMatLnk;
	}
	public void setExtRefMatLnk(String extRefMatLnk) {
		this.extRefMatLnk = extRefMatLnk;
	}
	public Long getLstUpdtUsrId() {
		return lstUpdtUsrId;
	}
	public void setLstUpdtUsrId(Long lstUpdtUsrId) {
		this.lstUpdtUsrId = lstUpdtUsrId;
	}
	public Long getOpnUsrId() {
		return opnUsrId;
	}
	public void setOpnUsrId(Long opnUsrId) {
		this.opnUsrId = opnUsrId;
	}
	public Long getFinIntlckId() {
		return finIntlckId;
	}
	public void setFinIntlckId(Long finIntlckId) {
		this.finIntlckId = finIntlckId;
	}
	public Long getApprovById() {
		return approvById;
	}
	public void setApprovById(Long approvById) {
		this.approvById = approvById;
	}
	public Long getLstModSysEvntId() {
		return lstModSysEvntId;
	}
	public void setLstModSysEvntId(Long lstModSysEvntId) {
		this.lstModSysEvntId = lstModSysEvntId;
	}
	public Short getPrirtyRnkNum() {
		return prirtyRnkNum;
	}
	public void setPrirtyRnkNum(Short prirtyRnkNum) {
		this.prirtyRnkNum = prirtyRnkNum;
	}
	public Timestamp getApprovByT() {
		return approvByT;
	}
	public void setApprovByT(Timestamp approvByT) {
		this.approvByT = approvByT;
	}
	public Long getProjContId() {
		return projContId;
	}
	public void setProjContId(Long projContId) {
		this.projContId = projContId;
	}
	public String getBckfilResnCd() {
		return bckfilResnCd;
	}
	public void setBckfilResnCd(String bckfilResnCd) {
		this.bckfilResnCd = bckfilResnCd;
	}
	public String getBckfilTypCd() {
		return bckfilTypCd;
	}
	public void setBckfilTypCd(String bckfilTypCd) {
		this.bckfilTypCd = bckfilTypCd;
	}
	public Short getFulfillRiskId() {
		return fulfillRiskId;
	}
	public void setFulfillRiskId(Short fulfillRiskId) {
		this.fulfillRiskId = fulfillRiskId;
	}
	public Timestamp getOriSbmtdT() {
		return oriSbmtdT;
	}
	public void setOriSbmtdT(Timestamp oriSbmtdT) {
		this.oriSbmtdT = oriSbmtdT;
	}
	public Integer getRbSetId() {
		return rbSetId;
	}
	public void setRbSetId(Integer rbSetId) {
		this.rbSetId = rbSetId;
	}
	public Integer getReplSetId() {
		return replSetId;
	}
	public void setReplSetId(Integer replSetId) {
		this.replSetId = replSetId;
	}
	public String getLocUse() {
		return locUse;
	}
	public void setLocUse(String locUse) {
		this.locUse = locUse;
	}
	public String getClntTyp() {
		return clntTyp;
	}
	public void setClntTyp(String clntTyp) {
		this.clntTyp = clntTyp;
	}
	public String getTramReqId() {
		return tramReqId;
	}
	public void setTramReqId(String tramReqId) {
		this.tramReqId = tramReqId;
	}
	public String getKeyRoleField() {
		return keyRoleField;
	}
	public void setKeyRoleField(String keyRoleField) {
		this.keyRoleField = keyRoleField;
	}
	public Integer getOpporCallStatusId() {
		return opporCallStatusId;
	}
	public void setOpporCallStatusId(Integer opporCallStatusId) {
		this.opporCallStatusId = opporCallStatusId;
	}
	public String getPcwFlag() {
		return pcwFlag;
	}
	public void setPcwFlag(String pcwFlag) {
		this.pcwFlag = pcwFlag;
	}
	public String getBckfilNm() {
		return bckfilNm;
	}
	public void setBckfilNm(String bckfilNm) {
		this.bckfilNm = bckfilNm;
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