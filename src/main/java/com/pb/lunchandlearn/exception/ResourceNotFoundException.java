package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Created by DE007RA on 5/11/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1l;
	private static final String defaultMsg = "Requested resource not found";
	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException() {
		super(defaultMsg);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}