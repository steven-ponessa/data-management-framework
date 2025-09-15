package com.ibm.wfm.beans;

import com.ibm.wfm.utils.Helpers;

public class NaryTreeNodeEvaluation {
	private int foundInd = -1;
	private Object key = null;
	private NaryTreeNode leaf = null;
	private NaryTreeNode lowestValidNode=null;
	private int validNodeInd = -1;
	private int validArcInd = -1;
	private String dataFileRow = null;
	
	public static final int NODE_FOUND_VALID_BRACH=0;
	public static final int NODE_FOUND_INVALID_BRACH = 1;
	public static final int NODE_NOT_FOUND = -1;
	
	public NaryTreeNodeEvaluation(int foundInd, Object key, NaryTreeNode leaf, NaryTreeNode lowestValidNode,
			int validNodeInd, int validArcInd) {
		this(foundInd, key, leaf, lowestValidNode, validNodeInd, validArcInd, null);
	}
	
	public NaryTreeNodeEvaluation(int foundInd, Object key, NaryTreeNode leaf, NaryTreeNode lowestValidNode,
			int validNodeInd, int validArcInd, String dataFileRow) {
		super();
		this.foundInd = foundInd;
		this.key = key;
		this.leaf = leaf;
		this.lowestValidNode = lowestValidNode;
		this.validNodeInd = validNodeInd;
		this.validArcInd = validArcInd;
		this.dataFileRow = dataFileRow;
	}
	
	public int getFoundInd() {
		return foundInd;
	}
	public void setFoundInd(int foundInd) {
		this.foundInd = foundInd;
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public NaryTreeNode getLeaf() {
		return leaf;
	}
	public void setLeaf(NaryTreeNode leaf) {
		this.leaf = leaf;
	}
	public NaryTreeNode getLowestValidNode() {
		return lowestValidNode;
	}
	public void setLowestValidNode(NaryTreeNode lowestValidNode) {
		this.lowestValidNode = lowestValidNode;
	}
	public int getValidNodeInd() {
		return validNodeInd;
	}
	public void setValidNodeInd(int validNodeInd) {
		this.validNodeInd = validNodeInd;
	}
	public int getValidArcInd() {
		return validArcInd;
	}
	public void setValidArcInd(int validArcInd) {
		this.validArcInd = validArcInd;
	}
	
	public boolean isNodeLevelValid(int level) {
		return isLevelValid(level, this.validNodeInd);
	}
	
	public boolean isArcLevelValid(int level) {
		return isLevelValid(level, this.validArcInd);
	}
	
	public boolean isLevelValid(int level, int status) {
		int levelValue = (int)Math.pow(2,level);
		if ((status & levelValue) == levelValue) return false;
		else return true;
	}
	
	public static String toStringHeading() {
		return "\tKey     \tFound\tLeaf key\tLVleaf key\tNode Ind\tArc Ind\tINL   \tIAL";
	}
	
	public static String toCsvStringHeading() {
		return ",Key,\"Found Ind\",\"Leaf key\",\"LVleaf key\",\"Node Ind\",\"NI Bytes\",\"Arc Ind\",\"AI Byte\",\"Invalid Node Levels\",\"Invalid Arc Levels\"";
	}
	
	public static String toCsvStringHeading(String dataFileHeader) {
		return ",Key,\"Found Ind\",\"Leaf key\",\"LVleaf key\",\"Node Ind\",\"NI Bytes\",\"Arc Ind\",\"AI Byte\",\"Invalid Node Levels\",\"Invalid Arc Levels\""+dataFileHeader;
	}
	
	public String toCsvString(int cnt, int numberLevels) {
		String invalidNodes = "";
		String invalidArcs = "";
		for (int i=0;i<numberLevels;i++) {
			if (!isNodeLevelValid(i)) invalidNodes+=(invalidNodes.length()>0?"; ":"")+String.valueOf(i);
			if (!isArcLevelValid(i)) invalidArcs+=(invalidArcs.length()>0?"; ":"")+String.valueOf(i);
		}
		if (invalidNodes.length()==0) invalidNodes="none";
		if (invalidArcs.length()==0) invalidArcs="none";
		return (cnt<10?" ":"")+String.valueOf(cnt)+","+this.key+","+this.foundInd+","+(leaf==null?"null    ":this.leaf.getFullKey())+","
				+ (this.lowestValidNode==null?"null    ":Helpers.pad(this.lowestValidNode.getFullKey(),8))+","
				+ (this.validNodeInd<10?" ":"")+this.validNodeInd+",x"+Helpers.pad(Integer.toString(this.validNodeInd,2),"0",8,Helpers.PAD_LEFT)+","
				+ (this.validArcInd<10?" ":"")+this.validArcInd+",x"+Helpers.pad(Integer.toString(this.validArcInd,2),"0",8,Helpers.PAD_LEFT)+","
				+ "\""+invalidNodes+"\",\""
				+ invalidArcs+"\""
				+ this.dataFileRow
				;
	}
	
	public String toString(int cnt, int numberLevels) {
		String invalidNodes = "";
		String invalidArcs = "";
		for (int i=0;i<numberLevels;i++) {
			if (!isNodeLevelValid(i)) invalidNodes+=(invalidNodes.length()>0?", ":"")+String.valueOf(i);
			if (!isArcLevelValid(i)) invalidArcs+=(invalidArcs.length()>0?", ":"")+String.valueOf(i);
		}
		if (invalidNodes.length()==0) invalidNodes="none";
		if (invalidArcs.length()==0) invalidArcs="none";
		return (cnt<10?" ":"")+String.valueOf(cnt)+".\t"+this.key+"\t"+this.foundInd+"\t"+(leaf==null?"null    ":this.leaf.getFullKey())+"\t"
				+ (this.lowestValidNode==null?"null    ":Helpers.pad(this.lowestValidNode.getFullKey(),8))+"\t"
				+ (this.validNodeInd<10?" ":"")+this.validNodeInd+" ("+Helpers.pad(Integer.toString(this.validNodeInd,2),"0",8,Helpers.PAD_LEFT)+")\t"
				+ (this.validArcInd<10?" ":"")+this.validArcInd+" ("+Helpers.pad(Integer.toString(this.validArcInd,2),"0",8,Helpers.PAD_LEFT)+")\t"
				+ invalidNodes+"\t"
				+ invalidArcs
				;
	}

	public String getDataFileRow() {
		return dataFileRow;
	}

	public void setDataFileRow(String dataFileRow) {
		this.dataFileRow = dataFileRow;
	}

}
