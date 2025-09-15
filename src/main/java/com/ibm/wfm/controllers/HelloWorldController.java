package com.ibm.wfm.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wfm.beans.HelloWorldBean;

//Controller
@RestController
@RequestMapping("/api/v1")
public class HelloWorldController {
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("hello-world")
	public String helloWorld() {
		return "Hello world";
	}
	
	@GetMapping("hello-world-i18n")
	public String helloWorldi18n(
			//not ideal because you have to pass Locale
			//@RequestHeader(name="Accept-Language",required=false) Locale locale
			) {

		//To test-Postman and set (Request) Header key = Accept-Language and value = en | nl | es | fr
		//return messageSource.getMessage("messages.hello.world", null, "Default Hello World", locale);		
		return messageSource.getMessage("messages.hello.world", null, "Default Hello World", LocaleContextHolder.getLocale());
	}
	
	@GetMapping("hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello world bean");
	}
	
	@GetMapping("hello-world-bean-echo")
	public HelloWorldBean helloWorldBean(@RequestParam(required = false) String echo) {
		return new HelloWorldBean("Hello world bean echo", echo);
	}
	
	@GetMapping("hello-world/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello world %s", name));
	}
	

}
