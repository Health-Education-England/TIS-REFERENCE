package com.transformuk.hee.tis.reference.model;

/**
 * Different types of error message keys to be used by calling client to interpret the message in i18n
 */
public enum ErrorMessageKey {
	MUST_BE_NOT_NULL("MUST_BE_NOT_NULL");

	private String value;

	private ErrorMessageKey(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
