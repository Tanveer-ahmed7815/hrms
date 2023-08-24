package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class DuplicateLeadNameException extends RuntimeException {

	public DuplicateLeadNameException(String message) {

		super(message);
	}

}
