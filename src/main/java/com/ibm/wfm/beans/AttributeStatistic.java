package com.ibm.wfm.beans;

public class AttributeStatistic {
	
	private String name;
	private int minLength;
	private int maxLength;
	
	public AttributeStatistic() {}
	
	public AttributeStatistic(String name) {
		this(name, -1,-1);
	}
	
	public AttributeStatistic(String name, int maxLength, int minLength) {
		super();
		this.name = name;
		this.maxLength = maxLength;
		this.minLength = minLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	@Override
	public String toString() {
		return "AttributeStatistic [name=" + name + ", maxLength=" + maxLength + ", minLength=" + minLength + "]";
	}
	
	public String toCsv() {
		return name + "," + maxLength + "," + minLength;
	}
	
}
