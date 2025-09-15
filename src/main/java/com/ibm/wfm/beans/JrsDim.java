package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JrsDim",baseTableName="REFT.JRS",parentBeanName="ServiceAreaDim",parentBaseTableName="REFT.SERVICE_AREA")
public class JrsDim extends NaryTreeNode implements Comparable<JrsDim> {
	@DbColumn(columnName="JRS_ID",isId=true)
	private int        jrsId;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	private String     jrsCd;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName="JRS_DESC")
	private String     jrsDesc;
	@DbColumn(columnName="JOB_ROLE_CD")
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="SPECIALTY_CD")
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	private String     specialtyNm;
	@DbColumn(columnName="SERVICE_AREA_CD",foreignKeySeq=1)
	private String     serviceAreaCd;
	@DbColumn(columnName="CMPNSTN_GRD_LST")
	private String     cmpnstnGrdLst;
	@DbColumn(columnName="INCENTIVE_FLG")
	private String     incentiveFlg;
	@DbColumn(columnName="RECOVERY_ADDER_CIC_INDIA_NM")
	private String     recoveryAdderCicIndiaNm;
	@DbColumn(columnName="RECOVERY_ADDER_CIC_OTHER_NM")
	private String     recoveryAdderCicOtherNm;
	@DbColumn(columnName="PRI_JRS_CNT")
	private int     priJrsCnt;
	@DbColumn(columnName="SEC_JRS_CNT")
	private int     secJrsCnt;
	@DbColumn(columnName="PRI_JOB_CAT_NM")
	private String     priJobCatNm;
	@DbColumn(columnName="CAMSS_NM")
	private String     camssNm;
	@DbColumn(columnName="SVF_GRP_NM")
	private String     svfGrpNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JrsDim () {
		this.level = 5;
	}
	
	// Define base constructor
	public JrsDim (
      String     jrsCd
	) {
		this.jrsCd                          = jrsCd;
		this.level                          = 5;
	}
	
	// Define base constructor
	public JrsDim (
      String     jrsCd
    , String     jrsNm
    , String     jrsDesc
    , String     serviceAreaCd
	) {
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.jrsDesc                        = jrsDesc;
		this.serviceAreaCd                  = serviceAreaCd;
		this.level                          = 5;
	}
	
	// Define full constructor
	public JrsDim (
		  int        jrsId
		, String     jrsCd
		, String     jrsNm
		, String     jrsDesc
		, String     jobRoleCd
		, String     jobRoleNm
		, String     specialtyCd
		, String     specialtyNm
		, String     serviceAreaCd
	) {
		this.jrsId                          = jrsId;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.jrsDesc                        = jrsDesc;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.level                          = 5;
	}
    
	// Define full constructor
	public JrsDim (
		  int        jrsId
		, String     jrsCd
		, String     jrsNm
		, String     jrsDesc
		, String     jobRoleCd
		, String     jobRoleNm
		, String     specialtyCd
		, String     specialtyNm
		, String     serviceAreaCd
		, String     cmpnstnGrdLst
		, String     incentiveFlg
		, String     recoveryAdderCicIndiaNm
		, String     recoveryAdderCicOtherNm
		, int        priJrsCnt
		, int        secJrsCnt
		, String     priJobCatNm
		, String     camssNm
		, String     svfGrpNm
	) {
		this.jrsId                          = jrsId;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.jrsDesc                        = jrsDesc;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.cmpnstnGrdLst                  = cmpnstnGrdLst;
		this.incentiveFlg                   = incentiveFlg;
		this.level                          = 5;
		this.recoveryAdderCicIndiaNm        = recoveryAdderCicIndiaNm;
		this.recoveryAdderCicOtherNm        = recoveryAdderCicOtherNm;
		this.priJrsCnt                      = priJrsCnt;
		this.secJrsCnt                      = secJrsCnt;
		this.priJobCatNm                    = priJobCatNm;
		this.camssNm                        = camssNm;
		this.svfGrpNm                       = svfGrpNm;
	}
	
    @Override
    public int compareTo(JrsDim o) {
        return this.getJrsCd().compareTo(o.getJrsCd());
    }
	
	@Override
	public String getCode() {
		return this.jrsCd;
	}
	public String getDescription() {
		return this.jrsNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JrsDim other = (JrsDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         //&& this.jrsNm.equals(other.getJrsNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpnstnGrdLst))
        + "," + Helpers.formatCsvField(String.valueOf(this.incentiveFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.recoveryAdderCicIndiaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.recoveryAdderCicOtherNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.priJrsCnt))
        + "," + Helpers.formatCsvField(String.valueOf(this.secJrsCnt))
        + "," + Helpers.formatCsvField(String.valueOf(this.priJobCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.camssNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfGrpNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("JRS_DESC")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("SERVICE_AREA_CD")
        + "," + Helpers.formatCsvField("CMPNSTN_GRD_LST")
        + "," + Helpers.formatCsvField("INCENTIVE_FLG")
        + "," + Helpers.formatCsvField("RECOVERY_ADDER_CIC_INDIA_NM")
        + "," + Helpers.formatCsvField("RECOVERY_ADDER_CIC_OTHER_NM")
        + "," + Helpers.formatCsvField("PRI_JRS_CNT")
        + "," + Helpers.formatCsvField("SEC_JRS_CNT")
        + "," + Helpers.formatCsvField("PRI_JOB_CAT_NM")
        + "," + Helpers.formatCsvField("CAMSS_NM")
        + "," + Helpers.formatCsvField("SVF_GRP_NM")
		;
	}
    
	// Define Getters and Setters
	public int getJrsId() {
		return jrsId;
	}
	public void setJrsId(int jrsId) {
		this.jrsId = jrsId;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
	public String getJrsDesc() {
		return jrsDesc;
	}
	public void setJrsDesc(String jrsDesc) {
		this.jrsDesc = jrsDesc;
	}
	public String getJobRoleCd() {
		return jobRoleCd;
	}
	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
	}
	public String getJobRoleNm() {
		return jobRoleNm;
	}
	public void setJobRoleNm(String jobRoleNm) {
		this.jobRoleNm = jobRoleNm;
	}
	public String getSpecialtyCd() {
		return specialtyCd;
	}
	public void setSpecialtyCd(String specialtyCd) {
		this.specialtyCd = specialtyCd;
	}
	public String getSpecialtyNm() {
		return specialtyNm;
	}
	public void setSpecialtyNm(String specialtyNm) {
		this.specialtyNm = specialtyNm;
	}
	public String getServiceAreaCd() {
		return serviceAreaCd;
	}
	public void setServiceAreaCd(String serviceAreaCd) {
		this.serviceAreaCd = serviceAreaCd;
	}
	public String getCmpnstnGrdLst() {
		return cmpnstnGrdLst;
	}

	public void setCmpnstnGrdLst(String cmpnstnGrdLst) {
		this.cmpnstnGrdLst = cmpnstnGrdLst;
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

	public String getIncentiveFlg() {
		return incentiveFlg;
	}

	public void setIncentiveFlg(String incentiveFlg) {
		this.incentiveFlg = incentiveFlg;
	}

	public int getPriJrsCnt() {
		return priJrsCnt;
	}

	public void setPriJrsCnt(int priJrsCnt) {
		this.priJrsCnt = priJrsCnt;
	}

	public int getSecJrsCnt() {
		return secJrsCnt;
	}

	public void setSecJrsCnt(int secJrsCnt) {
		this.secJrsCnt = secJrsCnt;
	}

	public String getPriJobCatNm() {
		return priJobCatNm;
	}

	public void setPriJobCatNm(String priJobCatNm) {
		this.priJobCatNm = priJobCatNm;
	}

	public String getCamssNm() {
		return camssNm;
	}

	public void setCamssNm(String camssNm) {
		this.camssNm = camssNm;
	}

	public String getSvfGrpNm() {
		return svfGrpNm;
	}

	public void setSvfGrpNm(String svfGrpNm) {
		this.svfGrpNm = svfGrpNm;
	}

	public String getRecoveryAdderCicIndiaNm() {
		return recoveryAdderCicIndiaNm;
	}

	public void setRecoveryAdderCicIndiaNm(String recoveryAdderCicIndiaNm) {
		this.recoveryAdderCicIndiaNm = recoveryAdderCicIndiaNm;
	}

	public String getRecoveryAdderCicOtherNm() {
		return recoveryAdderCicOtherNm;
	}

	public void setRecoveryAdderCicOtherNm(String recoveryAdderCicOtherNm) {
		this.recoveryAdderCicOtherNm = recoveryAdderCicOtherNm;
	}
}