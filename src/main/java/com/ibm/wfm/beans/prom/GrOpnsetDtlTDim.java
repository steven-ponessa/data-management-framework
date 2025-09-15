package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.beans.NaryTreeNode;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GrOpnsetDtlTDim",baseTableName="BCSPMPC.GR_OPNSET_DTL_T",isDimension=false,parentBeanName="OpnsetTDim",parentBaseTableName="PROMDMC.OPNSET_T")
public class GrOpnsetDtlTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_ID",keySeq=1,foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="EXP_Q")
	private String     expQ;
	@DbColumn(columnName="GRFM_USR_ID")
	private Long     grfmUsrId;
	@DbColumn(columnName="ICA_NUM")
	private String     icaNum;
	@DbColumn(columnName="PROJ_TYP_ID")
	private Long     projTypId;
	@DbColumn(columnName="DEL_EXEC")
	private String     delExec;
	@DbColumn(columnName="DEL_ORG_CD")
	private String     delOrgCd;
	@DbColumn(columnName="GR_PRVDR_CNTRY_CD")
	private String     grPrvdrCntryCd;
	@DbColumn(columnName="GRDS_REQ_ID")
	private Integer        grdsReqId;
	@DbColumn(columnName="ACPT_SUB_IND")
	private String     acptSubInd;
	@DbColumn(columnName="GBL_DEL_CTR_ID")
	private Long     gblDelCtrId;
	@DbColumn(columnName="GR_PROJ_ID")
	private Long     grProjId;
	@DbColumn(columnName="RMX_SO_ID")
	private String     rmxSoId;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GrOpnsetDtlTDim () {
		
	}
	
	// Define natural key constructor
	public GrOpnsetDtlTDim (
      Long     opnsetId
	) {
		this.opnsetId                       = opnsetId;
		
	}
	
	// Define base constructor
	public GrOpnsetDtlTDim (
      Long     opnsetId
    , String     expQ
    , Long     grfmUsrId
    , String     icaNum
    , Long     projTypId
    , String     delExec
    , String     delOrgCd
    , String     grPrvdrCntryCd
    , Integer        grdsReqId
    , String     acptSubInd
    , Long     gblDelCtrId
    , Long     grProjId
    , String     rmxSoId
	) {
		this.opnsetId                       = opnsetId;
		this.expQ                           = expQ;
		this.grfmUsrId                      = grfmUsrId;
		this.icaNum                         = icaNum;
		this.projTypId                      = projTypId;
		this.delExec                        = delExec;
		this.delOrgCd                       = delOrgCd;
		this.grPrvdrCntryCd                 = grPrvdrCntryCd;
		this.grdsReqId                      = grdsReqId;
		this.acptSubInd                     = acptSubInd;
		this.gblDelCtrId                    = gblDelCtrId;
		this.grProjId                       = grProjId;
		this.rmxSoId                        = rmxSoId;
		
	}
    
	// Define full constructor
	public GrOpnsetDtlTDim (
		  int        recordId
		, Long     opnsetId
		, String     expQ
		, Long     grfmUsrId
		, String     icaNum
		, Long     projTypId
		, String     delExec
		, String     delOrgCd
		, String     grPrvdrCntryCd
		, Integer        grdsReqId
		, String     acptSubInd
		, Long     gblDelCtrId
		, Long     grProjId
		, String     rmxSoId
	) {
		this.recordId                       = recordId;
		this.opnsetId                       = opnsetId;
		this.expQ                           = expQ;
		this.grfmUsrId                      = grfmUsrId;
		this.icaNum                         = icaNum;
		this.projTypId                      = projTypId;
		this.delExec                        = delExec;
		this.delOrgCd                       = delOrgCd;
		this.grPrvdrCntryCd                 = grPrvdrCntryCd;
		this.grdsReqId                      = grdsReqId;
		this.acptSubInd                     = acptSubInd;
		this.gblDelCtrId                    = gblDelCtrId;
		this.grProjId                       = grProjId;
		this.rmxSoId                        = rmxSoId;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.opnsetId);
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GrOpnsetDtlTDim other = (GrOpnsetDtlTDim) obj;
		if (
            this.opnsetId.equals(other.getOpnsetId())
         && this.expQ.equals(other.getExpQ())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.expQ))
        + "," + Helpers.formatCsvField(String.valueOf(this.grfmUsrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.icaNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.projTypId))
        + "," + Helpers.formatCsvField(String.valueOf(this.delExec))
        + "," + Helpers.formatCsvField(String.valueOf(this.delOrgCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.grPrvdrCntryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.grdsReqId))
        + "," + Helpers.formatCsvField(String.valueOf(this.acptSubInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gblDelCtrId))
        + "," + Helpers.formatCsvField(String.valueOf(this.grProjId))
        + "," + Helpers.formatCsvField(String.valueOf(this.rmxSoId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("EXP_Q")
        + "," + Helpers.formatCsvField("GRFM_USR_ID")
        + "," + Helpers.formatCsvField("ICA_NUM")
        + "," + Helpers.formatCsvField("PROJ_TYP_ID")
        + "," + Helpers.formatCsvField("DEL_EXEC")
        + "," + Helpers.formatCsvField("DEL_ORG_CD")
        + "," + Helpers.formatCsvField("GR_PRVDR_CNTRY_CD")
        + "," + Helpers.formatCsvField("GRDS_REQ_ID")
        + "," + Helpers.formatCsvField("ACPT_SUB_IND")
        + "," + Helpers.formatCsvField("GBL_DEL_CTR_ID")
        + "," + Helpers.formatCsvField("GR_PROJ_ID")
        + "," + Helpers.formatCsvField("RMX_SO_ID")
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
	public String getExpQ() {
		return expQ;
	}
	public void setExpQ(String expQ) {
		this.expQ = expQ;
	}
	public Long getGrfmUsrId() {
		return grfmUsrId;
	}
	public void setGrfmUsrId(Long grfmUsrId) {
		this.grfmUsrId = grfmUsrId;
	}
	public String getIcaNum() {
		return icaNum;
	}
	public void setIcaNum(String icaNum) {
		this.icaNum = icaNum;
	}
	public Long getProjTypId() {
		return projTypId;
	}
	public void setProjTypId(Long projTypId) {
		this.projTypId = projTypId;
	}
	public String getDelExec() {
		return delExec;
	}
	public void setDelExec(String delExec) {
		this.delExec = delExec;
	}
	public String getDelOrgCd() {
		return delOrgCd;
	}
	public void setDelOrgCd(String delOrgCd) {
		this.delOrgCd = delOrgCd;
	}
	public String getGrPrvdrCntryCd() {
		return grPrvdrCntryCd;
	}
	public void setGrPrvdrCntryCd(String grPrvdrCntryCd) {
		this.grPrvdrCntryCd = grPrvdrCntryCd;
	}
	public Integer getGrdsReqId() {
		return grdsReqId;
	}
	public void setGrdsReqId(Integer grdsReqId) {
		this.grdsReqId = grdsReqId;
	}
	public String getAcptSubInd() {
		return acptSubInd;
	}
	public void setAcptSubInd(String acptSubInd) {
		this.acptSubInd = acptSubInd;
	}
	public Long getGblDelCtrId() {
		return gblDelCtrId;
	}
	public void setGblDelCtrId(Long gblDelCtrId) {
		this.gblDelCtrId = gblDelCtrId;
	}
	public Long getGrProjId() {
		return grProjId;
	}
	public void setGrProjId(Long grProjId) {
		this.grProjId = grProjId;
	}
	public String getRmxSoId() {
		return rmxSoId;
	}
	public void setRmxSoId(String rmxSoId) {
		this.rmxSoId = rmxSoId;
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