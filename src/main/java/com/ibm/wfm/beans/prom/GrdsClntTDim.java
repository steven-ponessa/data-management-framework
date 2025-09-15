package com.ibm.wfm.beans.prom;

import java.sql.Timestamp;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GrdsClntTDim",baseTableName="BCSPMPC.GRDS_CLNT_T",isDimension=false,useTable=true)
public class GrdsClntTDim extends NaryTreeNode {
	@DbColumn(columnName="GRDS_CLNT_CD",keySeq=1)
	private int        grdsClntCd;
	@DbColumn(columnName="CLNT_NM")
	private String     clntNm;
	@DbColumn(columnName="CRE_T")
	private Timestamp  creT;
	@DbColumn(columnName="LST_UPDT_T")
	private Timestamp  lstUpdtT;
	@DbColumn(columnName="APPROV_T")
	private Timestamp  approvT;
	@DbColumn(columnName="CRE_USR_ID")
	private Long       creUsrId;
	@DbColumn(columnName="SCTR_ID")
	private Long       sctrId;
	@DbColumn(columnName="INDSTR_ID")
	private Long       indstrId;
	@DbColumn(columnName="CNTRY_CD")
	private String     cntryCd;
	@DbColumn(columnName="LST_UPDT_USR_ID")
	private Long       lstUpdtUsrId;
	@DbColumn(columnName="APPROV_USR_ID")
	private Long       approvUsrId;
	@DbColumn(columnName="APPROV_IND")
	private String     approvInd;
	@DbColumn(columnName="PGM_MGR_USR_ID")
	private Long       pgmMgrUsrId;
	@DbColumn(columnName="CLNT_NM_SRCH")
	private String     clntNmSrch;
	@DbColumn(columnName="GBS_ACCT_CLSS")
	private String     gbsAcctClss;
	@DbColumn(columnName="GLBL_CLNT_NM")
	private String     glblClntNm;
	@DbColumn(columnName="ACCNT_CLSTR_NM")
	private String     accntClstrNm;
	@DbColumn(columnName="CLNT_TYPE_CD")
	private String     clntTypeCd;
	@DbColumn(columnName="DEL_FLG")
	private String     delFlg;
	@DbColumn(columnName="CURR_COVRG_TYP_CD")
	private String     currCovrgTypCd;

	// Define null constructor
	public GrdsClntTDim () {
		
	}
	
	// Define natural key constructor
	public GrdsClntTDim (
      int        grdsClntCd
	) {
		this.grdsClntCd                     = grdsClntCd;
		
	}
    
	// Define full constructor
	public GrdsClntTDim (
		  int        grdsClntCd
		, String     clntNm
		, Timestamp  creT
		, Timestamp  lstUpdtT
		, Timestamp  approvT
		, Long       creUsrId
		, Long       sctrId
		, Long       indstrId
		, String     cntryCd
		, Long       lstUpdtUsrId
		, Long       approvUsrId
		, String     approvInd
		, Long       pgmMgrUsrId
		, String     clntNmSrch
		, String     gbsAcctClss
		, String     glblClntNm
		, String     accntClstrNm
		, String     clntTypeCd
		, String     delFlg
		, String     currCovrgTypCd
	) {
		this.grdsClntCd                     = grdsClntCd;
		this.clntNm                         = clntNm;
		this.creT                           = creT;
		this.lstUpdtT                       = lstUpdtT;
		this.approvT                        = approvT;
		this.creUsrId                       = creUsrId;
		this.sctrId                         = sctrId;
		this.indstrId                       = indstrId;
		this.cntryCd                        = cntryCd;
		this.lstUpdtUsrId                   = lstUpdtUsrId;
		this.approvUsrId                    = approvUsrId;
		this.approvInd                      = approvInd;
		this.pgmMgrUsrId                    = pgmMgrUsrId;
		this.clntNmSrch                     = clntNmSrch;
		this.gbsAcctClss                    = gbsAcctClss;
		this.glblClntNm                     = glblClntNm;
		this.accntClstrNm                   = accntClstrNm;
		this.clntTypeCd                     = clntTypeCd;
		this.delFlg                         = delFlg;
		this.currCovrgTypCd                 = currCovrgTypCd;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.grdsClntCd);
	}
	public String getDescription() { 
		return this.getClntNm(); 
	}
    
	// Define Getters and Setters
	public int getGrdsClntCd() {
		return grdsClntCd;
	}
	public void setGrdsClntCd(int grdsClntCd) {
		this.grdsClntCd = grdsClntCd;
	}
	public String getClntNm() {
		return clntNm;
	}
	public void setClntNm(String clntNm) {
		this.clntNm = clntNm;
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
	public Timestamp getApprovT() {
		return approvT;
	}
	public void setApprovT(Timestamp approvT) {
		this.approvT = approvT;
	}
	public Long getCreUsrId() {
		return creUsrId;
	}
	public void setCreUsrId(Long creUsrId) {
		this.creUsrId = creUsrId;
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
	public String getCntryCd() {
		return cntryCd;
	}
	public void setCntryCd(String cntryCd) {
		this.cntryCd = cntryCd;
	}
	public Long getLstUpdtUsrId() {
		return lstUpdtUsrId;
	}
	public void setLstUpdtUsrId(Long lstUpdtUsrId) {
		this.lstUpdtUsrId = lstUpdtUsrId;
	}
	public Long getApprovUsrId() {
		return approvUsrId;
	}
	public void setApprovUsrId(Long approvUsrId) {
		this.approvUsrId = approvUsrId;
	}
	public String getApprovInd() {
		return approvInd;
	}
	public void setApprovInd(String approvInd) {
		this.approvInd = approvInd;
	}
	public Long getPgmMgrUsrId() {
		return pgmMgrUsrId;
	}
	public void setPgmMgrUsrId(Long pgmMgrUsrId) {
		this.pgmMgrUsrId = pgmMgrUsrId;
	}
	public String getClntNmSrch() {
		return clntNmSrch;
	}
	public void setClntNmSrch(String clntNmSrch) {
		this.clntNmSrch = clntNmSrch;
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
	public String getClntTypeCd() {
		return clntTypeCd;
	}
	public void setClntTypeCd(String clntTypeCd) {
		this.clntTypeCd = clntTypeCd;
	}
	public String getDelFlg() {
		return delFlg;
	}
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}
	public String getCurrCovrgTypCd() {
		return currCovrgTypCd;
	}
	public void setCurrCovrgTypCd(String currCovrgTypCd) {
		this.currCovrgTypCd = currCovrgTypCd;
	}
}