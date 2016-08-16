package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 8/15/2016.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public final class ServiceUnavailableException extends Exception{
	private static final long serialVersionUID = 1l;
	private static final String defaultMsg = "Requested resource not found";
	public ServiceUnavailableException(String message) {
		super(message);
	}

	public ServiceUnavailableException() {
		super(defaultMsg);
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

}
