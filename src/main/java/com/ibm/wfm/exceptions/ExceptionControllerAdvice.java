package com.ibm.wfm.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception e) {
		ObjectMapper mapper = JsonMapper.builder().build();
		ErrorInfo errorInfo = new ErrorInfo(e);
		String respJSONstring = "{}";
		try {
			respJSONstring = mapper.writeValueAsString(errorInfo);
		} catch (JacksonException e1) {
			e1.printStackTrace();
		}
		return ResponseEntity.badRequest().body(respJSONstring);
	}
	
	private class ErrorInfo {
	    public final String className;
	    public final String exMessage;

	    public ErrorInfo(Exception ex) {
	    	ex.printStackTrace();
	        this.className = ex.getClass().getName();
	        this.exMessage = ex.getLocalizedMessage();
	    }
	}
}