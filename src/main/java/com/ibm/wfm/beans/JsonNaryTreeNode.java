package com.ibm.wfm.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonNaryTreeNode implements NaryTreeNodeInterface<JsonNaryTreeNode> {
	
	private String code = null;
	private String fullKey = null;
	protected int level = -1;
	private HashMap<String, Object> attributes = null;
	private ArrayList<JsonNaryTreeNode> children = null;
	@JsonIgnore
	private JsonNaryTreeNode parentNode = null;
	
	public JsonNaryTreeNode() {}
	
	public JsonNaryTreeNode(String code) {
		this(code, null, -1);
	}
	
	public JsonNaryTreeNode(String code, String fullKey, int level) {
		this(code, fullKey, level, null, null, null);
	}
	
	public JsonNaryTreeNode(String code, String fullKey, int level, HashMap<String, Object> attributes,
			ArrayList<JsonNaryTreeNode> children, JsonNaryTreeNode parentNode) {
		super();
		this.code = code.trim();
		this.fullKey = fullKey;
		this.level = level;
		this.attributes = attributes;
		this.children = children;
		this.parentNode = parentNode;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code.trim();
	}
	public HashMap<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributtes(HashMap<String, Object> attributtes) {
		this.attributes = attributtes;
	}
	public ArrayList<JsonNaryTreeNode> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<JsonNaryTreeNode> children) {
		this.children = children;
	}
	public JsonNaryTreeNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(JsonNaryTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public void addAttribute(String name, Object value) {
		if (attributes==null) attributes = new HashMap<String, Object>();
		if (value instanceof String) value = ((String)value).trim();
		attributes.put(name.trim(), value);
	}
	
	public void addChild(JsonNaryTreeNode jsonNaryTreeNode) {
		if (children == null) children = new ArrayList<JsonNaryTreeNode>();
		children.add(jsonNaryTreeNode);
	}
	
	public Object getAttribute(String name) {
		if (attributes==null) return null;
		return attributes.get(name);
	}
	
	public String getFullKey() {
		return fullKey;
	}

	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "JsonNaryTreeNode [code=" + code + ", fullKey=" + fullKey + ", level=" + level + ", attributes="
				+ attributes + ", children=" + children + ", parentNode=" + parentNode + "]";
	}



}
