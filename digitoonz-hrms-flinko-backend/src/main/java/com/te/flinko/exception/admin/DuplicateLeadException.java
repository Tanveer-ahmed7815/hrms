package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class DuplicateLeadException extends RuntimeException {

	public DuplicateLeadException (String message) {
		super(message);
	}
}
