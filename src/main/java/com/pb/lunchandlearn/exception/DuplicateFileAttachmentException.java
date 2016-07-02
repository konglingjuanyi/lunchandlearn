package com.pb.lunchandlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DE007RA on 6/26/2016.
 */
@ResponseStatus(HttpStatus.FOUND)
public final class DuplicateFileAttachmentException extends RuntimeException {
	private static final long serialVersionUID = 2l;

	public DuplicateFileAttachmentException(String message) {
		super(message);
	}
	public DuplicateFileAttachmentException(String message, Throwable cause) {
		super(message, cause);
	}
}
