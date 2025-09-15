package com.ibm.wfm.beans.prom;

import java.sql.Timestamp;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GrProjTDim",baseTableName="BCSPMPC.GR_PROJ_T",isDimension=false,useTable=true)
public class GrProjTDim extends NaryTreeNode {
	@DbColumn(columnName="GR_PROJ_ID",keySeq=1)
	private Long       grProjId;
	@DbColumn(columnName="PROJ_NM")
	private String     projNm;
	@DbColumn(columnName="GRDS_CLNT_CD")
	private int        grdsClntCd;
	@DbColumn(columnName="PROJ_MGR_USR_ID")
	private Long       projMgrUsrId;
	@DbColumn(columnName="PROJ_DEPT_CD")
	private String     projDeptCd;
	@DbColumn(columnName="GRDS_PROJ_ID")
	private int        grdsProjId;
	@DbColumn(columnName="CRE_USR_ID")
	private Long       creUsrId;
	@DbColumn(columnName="CRE_T")
	private Timestamp   creT;
	@DbColumn(columnName="LST_UPDT_USR_ID")
	private Long       lstUpdtUsrId;
	@DbColumn(columnName="LST_UPDT_T")
	private Timestamp   lstUpdtT;
	@DbColumn(columnName="DEL_FLG")
	private String     delFlg;
	@DbColumn(columnName="CSEG_ENBL_FLG")
	private String     csegEnblFlg;
	@DbColumn(columnName="CSEG_CNTRY_CD")
	private String     csegCntryCd;
	@DbColumn(columnName="CSEG_CAPT_BY_USR_ID")
	private Long       csegCaptByUsrId;
	@DbColumn(columnName="CSEG_CAPT_T")
	private Timestamp   csegCaptT;
	@DbColumn(columnName="CSEG_OWNR_CONT_ID")
	private Long       csegOwnrContId;
	@DbColumn(columnName="CSEG_DEL_CNTR_TYP_ID")
	private Short      csegDelCntrTypId;
	@DbColumn(columnName="PROJ_NM_SRCH")
	private String     projNmSrch;
	@DbColumn(columnName="CSEG_RSTCTN")
	private String     csegRstctn;
	@DbColumn(columnName="CSEG_RSTCTN_COMMENT")
	private String     csegRstctnComment;

	// Define null constructor
	public GrProjTDim () {
		
	}
	
	// Define natural key constructor
	public GrProjTDim (
      Long       grProjId
	) {
		this.grProjId                       = grProjId;
		
	}
	
    
	// Define full constructor
	public GrProjTDim (
		  Long       grProjId
		, String     projNm
		, int        grdsClntCd
		, Long       projMgrUsrId
		, String     projDeptCd
		, int        grdsProjId
		, Long       creUsrId
		, Timestamp   creT
		, Long       lstUpdtUsrId
		, Timestamp   lstUpdtT
		, String     delFlg
		, String     csegEnblFlg
		, String     csegCntryCd
		, Long       csegCaptByUsrId
		, Timestamp   csegCaptT
		, Long       csegOwnrContId
		, Short      csegDelCntrTypId
		, String     projNmSrch
		, String     csegRstctn
		, String     csegRstctnComment
	) {
		this.grProjId                       = grProjId;
		this.projNm                         = projNm;
		this.grdsClntCd                     = grdsClntCd;
		this.projMgrUsrId                   = projMgrUsrId;
		this.projDeptCd                     = projDeptCd;
		this.grdsProjId                     = grdsProjId;
		this.creUsrId                       = creUsrId;
		this.creT                           = creT;
		this.lstUpdtUsrId                   = lstUpdtUsrId;
		this.lstUpdtT                       = lstUpdtT;
		this.delFlg                         = delFlg;
		this.csegEnblFlg                    = csegEnblFlg;
		this.csegCntryCd                    = csegCntryCd;
		this.csegCaptByUsrId                = csegCaptByUsrId;
		this.csegCaptT                      = csegCaptT;
		this.csegOwnrContId                 = csegOwnrContId;
		this.csegDelCntrTypId               = csegDelCntrTypId;
		this.projNmSrch                     = projNmSrch;
		this.csegRstctn                     = csegRstctn;
		this.csegRstctnComment              = csegRstctnComment;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.grProjId);
	}
	public String getDescription() { 
		return this.projNm; 
	}
	
    
	// Define Getters and Setters
	public Long getGrProjId() {
		return grProjId;
	}
	public void setGrProjId(Long grProjId) {
		this.grProjId = grProjId;
	}
	public String getProjNm() {
		return projNm;
	}
	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	public int getGrdsClntCd() {
		return grdsClntCd;
	}
	public void setGrdsClntCd(int grdsClntCd) {
		this.grdsClntCd = grdsClntCd;
	}
	public Long getProjMgrUsrId() {
		return projMgrUsrId;
	}
	public void setProjMgrUsrId(Long projMgrUsrId) {
		this.projMgrUsrId = projMgrUsrId;
	}
	public String getProjDeptCd() {
		return projDeptCd;
	}
	public void setProjDeptCd(String projDeptCd) {
		this.projDeptCd = projDeptCd;
	}
	public int getGrdsProjId() {
		return grdsProjId;
	}
	public void setGrdsProjId(int grdsProjId) {
		this.grdsProjId = grdsProjId;
	}
	public Long getCreUsrId() {
		return creUsrId;
	}
	public void setCreUsrId(Long creUsrId) {
		this.creUsrId = creUsrId;
	}
	public Timestamp getCreT() {
		return creT;
	}
	public void setCreT(Timestamp creT) {
		this.creT = creT;
	}
	public Long getLstUpdtUsrId() {
		return lstUpdtUsrId;
	}
	public void setLstUpdtUsrId(Long lstUpdtUsrId) {
		this.lstUpdtUsrId = lstUpdtUsrId;
	}
	public Timestamp getLstUpdtT() {
		return lstUpdtT;
	}
	public void setLstUpdtT(Timestamp lstUpdtT) {
		this.lstUpdtT = lstUpdtT;
	}
	public String getDelFlg() {
		return delFlg;
	}
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}
	public String getCsegEnblFlg() {
		return csegEnblFlg;
	}
	public void setCsegEnblFlg(String csegEnblFlg) {
		this.csegEnblFlg = csegEnblFlg;
	}
	public String getCsegCntryCd() {
		return csegCntryCd;
	}
	public void setCsegCntryCd(String csegCntryCd) {
		this.csegCntryCd = csegCntryCd;
	}
	public Long getCsegCaptByUsrId() {
		return csegCaptByUsrId;
	}
	public void setCsegCaptByUsrId(Long csegCaptByUsrId) {
		this.csegCaptByUsrId = csegCaptByUsrId;
	}
	public Timestamp getCsegCaptT() {
		return csegCaptT;
	}
	public void setCsegCaptT(Timestamp csegCaptT) {
		this.csegCaptT = csegCaptT;
	}
	public Long getCsegOwnrContId() {
		return csegOwnrContId;
	}
	public void setCsegOwnrContId(Long csegOwnrContId) {
		this.csegOwnrContId = csegOwnrContId;
	}
	public Short getCsegDelCntrTypId() {
		return csegDelCntrTypId;
	}
	public void setCsegDelCntrTypId(Short csegDelCntrTypId) {
		this.csegDelCntrTypId = csegDelCntrTypId;
	}
	public String getProjNmSrch() {
		return projNmSrch;
	}
	public void setProjNmSrch(String projNmSrch) {
		this.projNmSrch = projNmSrch;
	}
	public String getCsegRstctn() {
		return csegRstctn;
	}
	public void setCsegRstctn(String csegRstctn) {
		this.csegRstctn = csegRstctn;
	}
	public String getCsegRstctnComment() {
		return csegRstctnComment;
	}
	public void setCsegRstctnComment(String csegRstctnComment) {
		this.csegRstctnComment = csegRstctnComment;
	}
}