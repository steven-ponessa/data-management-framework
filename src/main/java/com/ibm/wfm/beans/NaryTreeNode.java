package com.ibm.wfm.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.utils.FileHelpers;

public class NaryTreeNode implements NaryTreeNodeInterface<NaryTreeNode> {
	private String code = null;
	private String fullKey = null;
	private String description = null;
	protected int level = -1;
	private ArrayList<NaryTreeNode> children = null;
	@JsonIgnore
	private NaryTreeNode parentNode = null;
	
	public static void main (String[] args) {
		/*
	
		String apiUrl = "http://localhost:8080/api/v1/eds-ut-jrs-tax/jrss";
		 // Build URI with query parameter
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("includeParentage", "true");

        System.out.println("uriBuilder.toUriString()="+uriBuilder.toUriString());
		
		// Make GET request and receive response as NaryTreeNode
        ResponseEntity<BrandDim[]> responseEntity = new RestTemplate().getForEntity(uriBuilder.toUriString(), BrandDim[].class);
        
        // Get the BrandDim object from the ResponseEntity
        BrandDim[] rootNode = responseEntity.getBody();
		
		ArrayList<SelfReferencingNode> selfRefNodeArray = rootNode[0].generateSelfReferencingStructure();
		for (SelfReferencingNode selfReferencingNode : selfRefNodeArray) {
			System.out.println(selfReferencingNode.toString());
		}
		*/
        //List<SelfReferencingNode> nodes = readCsv("/Users/steve/Downloads/Consulting Business Unit and Sub Business Unit_BASIC_20240211.csv");
        //SelfReferencingNode root = buildHierarchy(nodes);
        //String json = convertToJson(root);
        //System.out.println(json);
		
	}

	public NaryTreeNode() {
	}

	public NaryTreeNode(String code) {
		this(code, null, null, -1, null);
	}

	public NaryTreeNode(String code, String description) {
		this(code, description, null, -1, null);
	}

	public NaryTreeNode(String code, String description, String fullKey) {
		this(code, description, fullKey, -1, null);
	}

	public NaryTreeNode(String code, String description, String fullKey, int level) {
		this(code, description, fullKey, level, null);
	}

	public NaryTreeNode(NaryTreeNode n) {
		this(n.getCode(), n.getDescription(), n.getFullKey(), n.getLevel(), n.getParentNode());
	}

	public NaryTreeNode(String code, String description, String fullKey, int level, NaryTreeNode parentNode) {
		this.code = code;
		this.description = description;
		this.fullKey = fullKey;
		this.level = level;
		this.parentNode = parentNode;
	}

	public void println() {
		System.out.println(this.code + " - " + this.description);
	}

	public void addChild(NaryTreeNode n) {
		if (children == null)
			children = new ArrayList<NaryTreeNode>();
		n.setParentNode(this);
		children.add(n);
	}

	@Override
	public List<NaryTreeNode> getChildren() {
		return children;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setChildren(ArrayList<NaryTreeNode> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFullKey() {
		return fullKey;
	}

	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}

	public NaryTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(NaryTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	// public String toString() {
	// return "Code: \t"+code+System.lineSeparator()+"Description:
	// \t"+description+System.lineSeparator()+"fullKey:
	// \t"+fullKey+System.lineSeparator()+"level: \t"+level;
	// }



}
