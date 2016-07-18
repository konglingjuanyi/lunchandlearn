package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 5/11/2016.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public final class InvalidOperationException extends RuntimeException {
	private static final long serialVersionUID = 4l;

	public InvalidOperationException(String message) {
		super(message);
	}
	public InvalidOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}