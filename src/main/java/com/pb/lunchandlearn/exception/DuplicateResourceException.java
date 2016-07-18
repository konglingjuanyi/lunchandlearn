package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 6/26/2016.
 */
@ResponseStatus(HttpStatus.FOUND)
public final class DuplicateResourceException extends RuntimeException {
	private static final long serialVersionUID = 2l;

	public DuplicateResourceException(String message) {
		super(message);
	}
	public DuplicateResourceException(String message, Throwable cause) {
		super(message, cause);
	}
}
