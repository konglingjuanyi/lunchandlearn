package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 5/11/2016.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public final class UnauthorizedOperationException extends RuntimeException {
	private static final long serialVersionUID = 3l;

	public UnauthorizedOperationException(String message) {
		super(message);
	}
	public UnauthorizedOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}