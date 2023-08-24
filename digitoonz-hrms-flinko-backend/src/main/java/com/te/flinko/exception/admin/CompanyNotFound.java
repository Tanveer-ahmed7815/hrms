package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class CompanyNotFound extends RuntimeException {

	public CompanyNotFound(String message) {
		super(message);
	}

}
