package com.ibm.wfm.beans;

import java.util.Date;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(value= {"creationDate","birthDate"})
public class User {
	private Integer id;
	
	@Size(min=2, max=25, message="Name must be between 2 and 25 characters.")
	private String name;
	
	@Past
	private Date birthDate;
	
	//Better to use @JsonIgnore in the event a property name changes. Works for both Json and XML
	// Note: This is "static" filtering. Can use "dynamic" filtering (see UDEMY microservices-with-spring-boot-and-spring-cloud chapter 39, step 25)
	// https://ibm-learning.udemy.com/course/microservices-with-spring-boot-and-spring-cloud/learn/lecture/8005676#overview
	@JsonIgnore
	private Date creationDate;
	
	public User(int id, String name, Date birthDate) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.creationDate = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
