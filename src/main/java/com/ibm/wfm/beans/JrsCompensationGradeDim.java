package com.ibm.wfm.beans;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JrsCompensationGradeDim",baseTableName="REFT.JRS_COMPENSATION_GRADE")
public class JrsCompensationGradeDim  {
	@DbColumn(columnName="JRS_CMPNSTN_GRD_ID",isId=true)
	private int        jrsCmpnstnGrdId;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	private String     jrsCd;
	@DbColumn(columnName="CMPNSTN_GRD_CD",keySeq=2)
	private String     cmpnstnGrdCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JrsCompensationGradeDim () {
		
	}
	
	// Define base constructor
	public JrsCompensationGradeDim (
      String     jrsCd
    , String     cmpnstnGrdCd
	) {
		this.jrsCd                          = jrsCd;
		this.cmpnstnGrdCd                   = cmpnstnGrdCd;
		
	}
    
	// Define full constructor
	public JrsCompensationGradeDim (
		  int        jrsCmpnstnGrdId
		, String     jrsCd
		, String     cmpnstnGrdCd
	) {
		this.jrsCmpnstnGrdId                = jrsCmpnstnGrdId;
		this.jrsCd                          = jrsCd;
		this.cmpnstnGrdCd                   = cmpnstnGrdCd;
		
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JrsCompensationGradeDim other = (JrsCompensationGradeDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         && this.cmpnstnGrdCd.equals(other.getCmpnstnGrdCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpnstnGrdCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("CMPNSTN_GRD_CD")
		;
	}
    
	// Define Getters and Setters
	public int getJrsCmpnstnGrdId() {
		return jrsCmpnstnGrdId;
	}
	public void setJrsCmpnstnGrdId(int jrsCmpnstnGrdId) {
		this.jrsCmpnstnGrdId = jrsCmpnstnGrdId;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getCmpnstnGrdCd() {
		return cmpnstnGrdCd;
	}
	public void setCmpnstnGrdCd(String cmpnstnGrdCd) {
		this.cmpnstnGrdCd = cmpnstnGrdCd;
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