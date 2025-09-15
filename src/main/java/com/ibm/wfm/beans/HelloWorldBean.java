package com.ibm.wfm.beans;

import java.util.Date;

public class HelloWorldBean {
	
	private String message;
	private Date creationTms;
	private String echo;

	public HelloWorldBean(String message) {
		this(message,null);
	}
	public HelloWorldBean(String message, String echo) {
		super();
		this.message = message;
		this.echo = echo;
		this.creationTms = new Date();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationTms() {
		return creationTms;
	}

	public void setCreationTms(Date creationTms) {
		this.creationTms = creationTms;
	}
	
	@Override
	public String toString() {
		return String.format("HelloWorldBeean [message=%s]", this.message);
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

}
