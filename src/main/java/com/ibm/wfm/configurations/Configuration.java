package com.ibm.wfm.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("limits-service")
public class Configuration {
	
	private int minimum;
	private int maximum;
	private long cacheExpireSecs;
	
	
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
