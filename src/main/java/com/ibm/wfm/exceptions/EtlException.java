package com.ibm.wfm.exceptions;

public class EtlException extends RuntimeException {
	
	public EtlException(String message) {
		super(message);
	}

	public EtlException(String message, Throwable cause) {
		super(message, cause);
	}

}
