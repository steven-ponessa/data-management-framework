package com.ibm.wfm.beans.ihub;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="ServiceLineDim",baseTableName="REFT.SERVICE_LINE",parentBeanName="GrowthPlatformDim",parentBaseTableName="REFT.GROWTH_PLATFORM")
public class ServiceLineDim  extends NaryTreeNode implements Comparable<ServiceLineDim>{
	@DbColumn(columnName="SERVICE_LINE_ID",isId=true)
	private int        serviceLineId;
	@DbColumn(columnName="SERVICE_LINE_CD",keySeq=1)
	private String     serviceLineCd;
	@DbColumn(columnName="SERVICE_LINE_NM")
	private String     serviceLineNm;
	@DbColumn(columnName="SERVICE_LINE_DESC")
	private String     serviceLineDesc;
	@DbColumn(columnName="LGCY_SERVICE_LINE_CD",isExtension=true)
	private String     lgcyServiceLineCd;
	@DbColumn(columnName="DCC_CD",isExtension=true)
	private String     dccCd;
	@DbColumn(columnName="GROWTH_PLATFORM_CD",foreignKeySeq=1)
	private String     growthPlatformCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public ServiceLineDim () {
		this.level = 2;
	}
	
	// Define base constructor
	public ServiceLineDim (
      String     serviceLineCd
	) {
		this.setLevel(2);
		this.serviceLineCd                  = serviceLineCd;
	}	
	
	public ServiceLineDim (
		      String     serviceLineCd
		    , String     serviceLineNm
		    , String     serviceLineDesc
		    , String     growthPlatformCd
			) {
				this(serviceLineCd,serviceLineNm,serviceLineDesc,null,null,growthPlatformCd);
			}
	
	// Define base constructor
	public ServiceLineDim (
      String     serviceLineCd
    , String     serviceLineNm
    , String     serviceLineDesc
    , String     lgcyServiceLineCd
    , String     dccCd
    , String     growthPlatformCd
	) {
		super(serviceLineCd, serviceLineNm);
		this.setLevel(2);
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.serviceLineDesc                = serviceLineDesc;
		this.lgcyServiceLineCd              = lgcyServiceLineCd;
		this.dccCd                          = dccCd;
		this.growthPlatformCd               = growthPlatformCd;
	}
    
	// Define full constructor
	public ServiceLineDim (
		  int        serviceLineId
		, String     serviceLineCd
		, String     serviceLineNm
		, String     serviceLineDesc
	    , String     lgcyServiceLineCd
	    , String     dccCd
		, String     growthPlatformCd
	) {
		super(serviceLineCd, serviceLineNm);
		this.setLevel(2);
		this.serviceLineId                  = serviceLineId;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.serviceLineDesc                = serviceLineDesc;
		this.lgcyServiceLineCd              = lgcyServiceLineCd;
		this.dccCd                          = dccCd;
		this.growthPlatformCd               = growthPlatformCd;
	}
	
	@Override
	public String getCode() { return this.serviceLineCd; }
	public String getDescription() { return this.serviceLineNm; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		ServiceLineDim other = (ServiceLineDim) obj;
		if (
            this.serviceLineCd.equals(other.getServiceLineCd())
         && this.serviceLineNm.equals(other.getServiceLineNm())
		) return true;
		return false;
	}	
	
	@Override
    public int compareTo(ServiceLineDim o) {
        return this.getServiceLineCd().compareTo(o.getServiceLineCd());
    }
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.serviceLineCd)
        + "," + Helpers.formatCsvField(this.serviceLineNm)
        + "," + Helpers.formatCsvField(this.serviceLineDesc)
        + "," + Helpers.formatCsvField(this.growthPlatformCd)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SERVICE_LINE_CD")
        + "," + Helpers.formatCsvField("SERVICE_LINE_NM")
        + "," + Helpers.formatCsvField("SERVICE_LINE_DESC")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_CD")
		;
	}
    
	// Define Getters and Setters
	public int getServiceLineId() {
		return serviceLineId;
	}
	public void setServiceLineId(int serviceLineId) {
		this.serviceLineId = serviceLineId;
	}
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getServiceLineDesc() {
		return serviceLineDesc;
	}
	public void setServiceLineDesc(String serviceLineDesc) {
		this.serviceLineDesc = serviceLineDesc;
	}
	public String getGrowthPlatformCd() {
		return growthPlatformCd;
	}
	public void setGrowthPlatformCd(String growthPlatformCd) {
		this.growthPlatformCd = growthPlatformCd;
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

	public String getLgcyServiceLineCd() {
		return lgcyServiceLineCd;
	}

	public void setLgcyServiceLineCd(String lgcyServiceLineCd) {
		this.lgcyServiceLineCd = lgcyServiceLineCd;
	}

	public String getDccCd() {
		return dccCd;
	}

	public void setDccCd(String dccCd) {
		this.dccCd = dccCd;
	}
}