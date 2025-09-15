package com.ibm.wfm.beans;

public class Limits {
	private int minimum;
	private int maximum;
	private long cacheExpireSecs;
	
	public Limits() {}
	
	public Limits(int minimum, int maximum, long cacheExpireSecs) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
		this.cacheExpireSecs = cacheExpireSecs;
	}
	
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public long getCacheExpireSecs() {
		return cacheExpireSecs;
	}

	public void setCacheExpireSecs(long cacheExpireSecs) {
		this.cacheExpireSecs = cacheExpireSecs;
	}

}
