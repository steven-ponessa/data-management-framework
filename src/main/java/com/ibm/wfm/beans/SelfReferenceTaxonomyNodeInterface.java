package com.ibm.wfm.beans;

public interface SelfReferenceTaxonomyNodeInterface {

	
	public String getParentCd();
	public void setParentCd(String parentCd);
	public String getCode();
	public void setCode(String code);
	public int getLevelNum();
	public void setLevelNum(int levelNum);
	public String getDescription() ;
	public void setDescription(String description);
	public String getName() ;
	public void setName(String name);
	
}