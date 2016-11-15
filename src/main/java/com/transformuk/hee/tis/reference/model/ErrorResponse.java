package com.transformuk.hee.tis.reference.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Generic error response format
 */
@JsonInclude(NON_NULL)
public class ErrorResponse {

	private ErrorType errorType;
	private String code;
	private String field;
	private ErrorMessageKey message;
	private Object rejectedValue;

	public ErrorResponse(ErrorType errorType, ErrorMessageKey message) {
		this.errorType = errorType;
		this.message = message;
	}

	public ErrorResponse(ErrorType errorType, String code, String field, ErrorMessageKey message, Object rejectedValue) {
		this.code = code;
		this.errorType = errorType;
		this.field = field;
		this.message = message;
		this.rejectedValue = rejectedValue;
	}

	public String getCode() {
		return code;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public String getField() {
		return field;
	}

	public ErrorMessageKey getMessage() {
		return message;
	}

	public Object getRejectedValue() {
		return rejectedValue;
	}
}
