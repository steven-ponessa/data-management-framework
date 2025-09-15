package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="FbsTeamDim",baseTableName="TEST.FBS_TEAM",parentBeanName="FbsDivisionDim",parentBaseTableName="TEST.FBS_DIVISION")
public class FbsTeamDim extends NaryTreeNode {
	@DbColumn(columnName="TEAM_ID",isId=true)
	private int        teamId;
	private int        id;
	@DbColumn(columnName="TEAM_CD",keySeq=1)
	private String     teamCd;
	@DbColumn(columnName="NICKNAME")
	private String     nickname;
	@DbColumn(columnName="INSTITUTION_NM")
	private String     institutionNm;
	@DbColumn(columnName="LOCATION_NM")
	private String     locationNm;
	@DbColumn(columnName="FOUNDED_YR")
	private int        foundedYr;
	@DbColumn(columnName="JOINED_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       joinedDt;
	@DbColumn(columnName="ENROLLMENT_CNT")
	private int        enrollmentCnt;
	@DbColumn(columnName="ENDOWMENT_AMT")
	private double     endowmentAmt;
	@DbColumn(columnName="ON_CAMPUS_STADIUM_IND")
	private String     onCampusStadiumInd;
	@DbColumn(columnName="CONF_CD")
	private String     confCd;
	@DbColumn(columnName="DIV_CD",foreignKeySeq=1)
	private String     divCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public FbsTeamDim () {
				this.level = 2;
	}
	
	// Define natural key constructor
	public FbsTeamDim (
      String     teamCd
	) {
		this.teamCd                         = teamCd;
		this.level                          = 2;
	}
	
	// Define base constructor
	public FbsTeamDim (
      String     teamCd
    , String     nickname
	) {
		this.teamCd                         = teamCd;
		this.nickname                       = nickname;
		this.level                          = 2;
	}
    
	// Define full constructor
	public FbsTeamDim (
		  int        teamId
		, String     teamCd
		, String     nickname
		, String     institutionNm
		, String     locationNm
		, int        foundedYr
		, Date       joinedDt
		, int        enrollmentCnt
		, double     endowmentAmt
		, String     onCampusStadiumInd
		, String     confCd
		, String     divCd
	) {
		this.teamId                         = teamId;
		this.id                             = teamId;
		this.teamCd                         = teamCd;
		this.nickname                       = nickname;
		this.institutionNm                  = institutionNm;
		this.locationNm                     = locationNm;
		this.foundedYr                      = foundedYr;
		this.joinedDt                       = joinedDt;
		this.enrollmentCnt                  = enrollmentCnt;
		this.endowmentAmt                   = endowmentAmt;
		this.onCampusStadiumInd				= onCampusStadiumInd;
		this.confCd                         = confCd;
		this.divCd                          = divCd;
		this.level                          = 2;
	}
	
	@Override
	public String getCode() { 
		return this.teamCd
		;
	}
	public String getDescription() { 
		return this.nickname; //manually entered
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		FbsTeamDim other = (FbsTeamDim) obj;
		if (
            this.teamCd.equals(other.getTeamCd())
         && this.nickname.equals(other.getNickname())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.teamCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.nickname))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("TEAM_CD")
        + "," + Helpers.formatCsvField("NICKNAME")
		;
	}
    
	// Define Getters and Setters
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
		this.id = teamId;
	}
	public int getId() {
		return teamId;
	}
	public String getTeamCd() {
		return teamCd;
	}
	public void setTeamCd(String teamCd) {
		this.teamCd = teamCd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getInstitutionNm() {
		return institutionNm;
	}
	public void setInstitutionNm(String institutionNm) {
		this.institutionNm = institutionNm;
	}
	public String getLocationNm() {
		return locationNm;
	}
	public void setLocationNm(String locationNm) {
		this.locationNm = locationNm;
	}
	public int getFoundedYr() {
		return foundedYr;
	}
	public void setFoundedYr(int foundedYr) {
		this.foundedYr = foundedYr;
	}
	public Date getJoinedDt() {
		return joinedDt;
	}
	public void setJoinedDt(Date joinedDt) {
		this.joinedDt = joinedDt;
	}
	public int getEnrollmentCnt() {
		return enrollmentCnt;
	}
	public void setEnrollmentCnt(int enrollmentCnt) {
		this.enrollmentCnt = enrollmentCnt;
	}
	public double getEndowmentAmt() {
		return endowmentAmt;
	}
	public void setEndowmentAmt(double endowmentAmt) {
		this.endowmentAmt = endowmentAmt;
	}
	public String getDivCd() {
		return divCd;
	}
	public void setDivCd(String divCd) {
		this.divCd = divCd;
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

	public String getOnCampusStadiumInd() {
		return onCampusStadiumInd;
	}

	public void setOnCampusStadiumInd(String onCampusStadiumInd) {
		this.onCampusStadiumInd = onCampusStadiumInd;
	}

	public String getConfCd() {
		return confCd;
	}

	public void setConfCd(String confCd) {
		this.confCd = confCd;
	}
}