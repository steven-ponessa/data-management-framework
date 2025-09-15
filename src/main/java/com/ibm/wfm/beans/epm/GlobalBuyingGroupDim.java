package com.ibm.wfm.beans.epm;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.beans.NaryTreeNode;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GlobalBuyingGroupDim",baseTableName="EPM.GLOBAL_BUYING_GROUP")
public class GlobalBuyingGroupDim extends NaryTreeNode {
	@DbColumn(columnName="SK_GLOBAL_BUYING_GROUP",isId=true)
	private int        skGlobalBuyingGroup;
	@DbColumn(columnName="GLOBAL_BUYING_GROUP_ID",keySeq=1)
	private String     globalBuyingGroupId;
	@DbColumn(columnName="GLOBAL_BUYING_GROUP_NAME")
	private String     globalBuyingGroupName;

	// Define null constructor
	public GlobalBuyingGroupDim () {
				this.level = 0;
	}
	
	// Define natural key constructor
	public GlobalBuyingGroupDim (
      String     globalBuyingGroupId
	) {
		this.globalBuyingGroupId            = globalBuyingGroupId;
		this.level                          = 0;
	}
	
    
	// Define full constructor
	public GlobalBuyingGroupDim (
		  int        skGlobalBuyingGroup
		, String     globalBuyingGroupId
		, String     globalBuyingGroupName
	) {
		this.skGlobalBuyingGroup            = skGlobalBuyingGroup;
		this.globalBuyingGroupId            = globalBuyingGroupId;
		this.globalBuyingGroupName          = globalBuyingGroupName;
		this.level                          = 0;
	}
	
	@Override
	public String getCode() { 
		return this.globalBuyingGroupId
		;
	}
	public String getDescription() { 
		return this.globalBuyingGroupName; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GlobalBuyingGroupDim other = (GlobalBuyingGroupDim) obj;
		if (
            this.globalBuyingGroupId.equals(other.getGlobalBuyingGroupId())
         && this.globalBuyingGroupName.equals(other.getGlobalBuyingGroupName())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.globalBuyingGroupId))
        + "," + Helpers.formatCsvField(String.valueOf(this.globalBuyingGroupName))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GLOBAL_BUYING_GROUP_ID")
        + "," + Helpers.formatCsvField("GLOBAL_BUYING_GROUP_NAME")
		;
	}
    
	// Define Getters and Setters
	public int getSkGlobalBuyingGroup() {
		return skGlobalBuyingGroup;
	}
	public void setSkGlobalBuyingGroup(int skGlobalBuyingGroup) {
		this.skGlobalBuyingGroup = skGlobalBuyingGroup;
	}
	public String getGlobalBuyingGroupId() {
		return globalBuyingGroupId;
	}
	public void setGlobalBuyingGroupId(String globalBuyingGroupId) {
		this.globalBuyingGroupId = globalBuyingGroupId;
	}
	public String getGlobalBuyingGroupName() {
		return globalBuyingGroupName;
	}
	public void setGlobalBuyingGroupName(String globalBuyingGroupName) {
		this.globalBuyingGroupName = globalBuyingGroupName;
	}
}