package com.ibm.wfm.beans;



import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryAreaMarketDim",baseTableName="REFT.DELIVERY_AREA_MARKET",parentBeanName="DeliveryAreaGeographyAssocDim",parentBaseTableName="REFT.DELIVERY_AREA_GEOGRAPHY_ASSOC")
public class DeliveryAreaMarketDim extends NaryTreeNode {
	@DbColumn(columnName="DLVRY_AREA_MRKT_ID",isId=true)
	private int        dlvryAreaMrktId;
	@DbColumn(columnName="DLVRY_AREA_MRKT_CD",keySeq=1)
	private String     dlvryAreaMrktCd;
	@DbColumn(columnName="DLVRY_AREA_MRKT_NM")
	private String     dlvryAreaMrktNm;
	@DbColumn(columnName="DLVRY_AREA_MRKT_DESC")
	private String     dlvryAreaMrktDesc;
	@DbColumn(columnName="TYPE_CD")
	private String     typeCd;
	@DbColumn(columnName="SOURCE_TBL_NM")
	private String     sourceTblNm;
	@DbColumn(columnName="DLVRY_AREA_GEO_CD",foreignKeySeq=1)
	private String     dlvryAreaGeoCd;

	// Define null constructor
	public DeliveryAreaMarketDim () {
		
	}
	
	// Define natural key constructor
	public DeliveryAreaMarketDim (
      String     dlvryAreaMrktCd
	) {
		this.dlvryAreaMrktCd                = dlvryAreaMrktCd;
		
	}
	
    
	// Define full constructor
	public DeliveryAreaMarketDim (
		  int        dlvryAreaMrktId
		, String     dlvryAreaMrktCd
		, String     dlvryAreaMrktNm
		, String     dlvryAreaMrktDesc
		, String     typeCd
		, String     sourceTblNm
		, String     dlvryAreaGeoCd
	) {
		this.dlvryAreaMrktId                = dlvryAreaMrktId;
		this.dlvryAreaMrktCd                = dlvryAreaMrktCd;
		this.dlvryAreaMrktNm                = dlvryAreaMrktNm;
		this.dlvryAreaMrktDesc              = dlvryAreaMrktDesc;
		this.typeCd                         = typeCd;
		this.sourceTblNm                    = sourceTblNm;
		this.dlvryAreaGeoCd                 = dlvryAreaGeoCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.dlvryAreaMrktCd
		;
	}
	public String getDescription() { 
		return dlvryAreaMrktDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryAreaMarketDim other = (DeliveryAreaMarketDim) obj;
		if (
            this.dlvryAreaMrktCd.equals(other.getDlvryAreaMrktCd())
         && this.dlvryAreaMrktNm.equals(other.getDlvryAreaMrktNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.dlvryAreaMrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaMrktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaMrktDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.typeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sourceTblNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaGeoCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("DLVRY_AREA_MRKT_CD")
        + "," + Helpers.formatCsvField("DLVRY_AREA_MRKT_NM")
        + "," + Helpers.formatCsvField("DLVRY_AREA_MRKT_DESC")
        + "," + Helpers.formatCsvField("TYPE_CD")
        + "," + Helpers.formatCsvField("SOURCE_TBL_NM")
        + "," + Helpers.formatCsvField("DLVRY_AREA_GEO_CD")
		;
	}
    
	// Define Getters and Setters
	public int getDlvryAreaMrktId() {
		return dlvryAreaMrktId;
	}
	public void setDlvryAreaMrktId(int dlvryAreaMrktId) {
		this.dlvryAreaMrktId = dlvryAreaMrktId;
	}
	public String getDlvryAreaMrktCd() {
		return dlvryAreaMrktCd;
	}
	public void setDlvryAreaMrktCd(String dlvryAreaMrktCd) {
		this.dlvryAreaMrktCd = dlvryAreaMrktCd;
	}
	public String getDlvryAreaMrktNm() {
		return dlvryAreaMrktNm;
	}
	public void setDlvryAreaMrktNm(String dlvryAreaMrktNm) {
		this.dlvryAreaMrktNm = dlvryAreaMrktNm;
	}
	public String getDlvryAreaMrktDesc() {
		return dlvryAreaMrktDesc;
	}
	public void setDlvryAreaMrktDesc(String dlvryAreaMrktDesc) {
		this.dlvryAreaMrktDesc = dlvryAreaMrktDesc;
	}
	public String getTypeCd() {
		return typeCd;
	}
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
	public String getSourceTblNm() {
		return sourceTblNm;
	}
	public void setSourceTblNm(String sourceTblNm) {
		this.sourceTblNm = sourceTblNm;
	}
	public String getDlvryAreaGeoCd() {
		return dlvryAreaGeoCd;
	}
	public void setDlvryAreaGeoCd(String dlvryAreaGeoCd) {
		this.dlvryAreaGeoCd = dlvryAreaGeoCd;
	}
}