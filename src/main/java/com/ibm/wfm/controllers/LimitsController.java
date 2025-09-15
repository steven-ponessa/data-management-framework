package com.ibm.wfm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wfm.beans.Limits;
import com.ibm.wfm.configurations.Configuration;

@RestController
@RequestMapping("/api/v1")
public class LimitsController {
	
	@Autowired
	private Configuration configuration;
	
	@GetMapping("/limits")
	public Limits retrieveLimits() {
		return new Limits(configuration.getMinimum(),configuration.getMaximum(), configuration.getCacheExpireSecs());
	}

}
