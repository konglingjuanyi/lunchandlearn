package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 5/11/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1l;

	public ResourceNotFoundException(String message) {
		super(message);
	}
	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}