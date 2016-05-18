package com.pb.lunchandlearn.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by de007ra on 5/2/2016.
 */
@ControllerAdvice
@PropertySource("classpath:/errors.properties")
public class GlobalExceptionAdvisor extends ResponseEntityExceptionHandler {

	@Autowired
	private Environment env;

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	protected ResponseEntity<?> handleInvalidRequest(DuplicateKeyException e,
														  ServletWebRequest request) {
		RestErrorMessage error = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), env.getProperty("modal.id.exist"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(e, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	class RestErrorMessage {
		private Integer status;
		private String msg;
		private boolean error;

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public boolean isError() {
			return error;
		}

		public void setError(boolean error) {
			this.error = error;
		}

		public RestErrorMessage(Integer status, String msg) {
			this.status = status;
			this.msg = msg;
			this.error = true;
		}
	}
}