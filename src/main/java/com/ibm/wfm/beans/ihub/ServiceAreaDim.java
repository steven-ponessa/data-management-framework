package com.ibm.wfm.beans.ihub;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="ServiceAreaDim",baseTableName="REFT.SERVICE_AREA",parentBeanName="PracticeDim",parentBaseTableName="REFT.PRACTICE")
public class ServiceAreaDim extends NaryTreeNode implements Comparable<ServiceAreaDim> {
	@DbColumn(columnName="SERVICE_AREA_ID",isId=true)
	private int        serviceAreaId;
	@DbColumn(columnName="SERVICE_AREA_CD",keySeq=1)
	private String     serviceAreaCd;
	@DbColumn(columnName="SERVICE_AREA_NM")
	private String     serviceAreaNm;
	@DbColumn(columnName="SERVICE_AREA_DESC")
	private String     serviceAreaDesc;
	@DbColumn(columnName="PRACTICE_CD",foreignKeySeq=1)
	private String     practiceCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public ServiceAreaDim () {
		this.level = 4;
	}
	
	// Define base constructor
	public ServiceAreaDim (
      String     serviceAreaCd
	) {
		this.serviceAreaCd                  = serviceAreaCd;
		this.level                          = 4;
	}	
	
	// Define base constructor
	public ServiceAreaDim (
      String     serviceAreaCd
    , String     serviceAreaNm
    , String     serviceAreaDesc
    , String     practiceCd
	) {
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.serviceAreaDesc                = serviceAreaDesc;
		this.practiceCd                     = practiceCd;
		this.level                          = 4;
	}
    
	// Define full constructor
	public ServiceAreaDim (
		  int        serviceAreaId
		, String     serviceAreaCd
		, String     serviceAreaNm
		, String     serviceAreaDesc
		, String     practiceCd
	) {
		this.serviceAreaId                  = serviceAreaId;
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.serviceAreaDesc                = serviceAreaDesc;
		this.practiceCd                     = practiceCd;
		this.level                          = 4;
	}
	
    @Override
    public int compareTo(ServiceAreaDim o) {
        return this.getServiceAreaCd().compareTo(o.getServiceAreaCd());
    }
	
	@Override
	public String getCode() {
		return this.serviceAreaCd;
	}
	public String getDescription() {
		return this.serviceAreaNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		ServiceAreaDim other = (ServiceAreaDim) obj;
		if (
            this.serviceAreaCd.equals(other.getServiceAreaCd())
         && this.serviceAreaNm.equals(other.getServiceAreaNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.serviceAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SERVICE_AREA_CD")
        + "," + Helpers.formatCsvField("SERVICE_AREA_NM")
        + "," + Helpers.formatCsvField("SERVICE_AREA_DESC")
        + "," + Helpers.formatCsvField("PRACTICE_CD")
		;
	}
    
	// Define Getters and Setters
	public int getServiceAreaId() {
		return serviceAreaId;
	}
	public void setServiceAreaId(int serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}
	public String getServiceAreaCd() {
		return serviceAreaCd;
	}
	public void setServiceAreaCd(String serviceAreaCd) {
		this.serviceAreaCd = serviceAreaCd;
	}
	public String getServiceAreaNm() {
		return serviceAreaNm;
	}
	public void setServiceAreaNm(String serviceAreaNm) {
		this.serviceAreaNm = serviceAreaNm;
	}
	public String getServiceAreaDesc() {
		return serviceAreaDesc;
	}
	public void setServiceAreaDesc(String serviceAreaDesc) {
		this.serviceAreaDesc = serviceAreaDesc;
	}
	public String getPracticeCd() {
		return practiceCd;
	}
	public void setPracticeCd(String practiceCd) {
		this.practiceCd = practiceCd;
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