package com.ibm.wfm.beans;

import java.util.ArrayList;

public class SelfReferencingNode {
    private int level;
    private String parentCd;
    private String code;
    private String name;
    private ArrayList<SelfReferencingNode> children = null;
    
	public SelfReferencingNode(int level, String parentCd, String code, String name) {
		super();
		this.level = level;
		this.parentCd = parentCd;
		this.code = code;
		this.name = name;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getParentCd() {
		return parentCd;
	}
	public String getParentCode() {
		return parentCd;
	}
	public void setParentCd(String parentCd) {
		this.parentCd = parentCd;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SelfReferencingNode [level=" + level + ", parentCd=" + parentCd + ", code=" + code + ", name=" + name
				+ "]";
	}
	
	public void addChild(SelfReferencingNode srn) {
		if (children==null) children = new ArrayList<SelfReferencingNode>();
		children.add(srn);
		return;
	}

	public ArrayList<SelfReferencingNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<SelfReferencingNode> children) {
		this.children = children;
	}

}
