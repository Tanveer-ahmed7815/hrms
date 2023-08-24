package com.te.flinko.exception;

/**
 * 
 * @author Brunda
 *
 */

public class CompanyRulesExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CompanyRulesExistsException(String message) {
		super(message);
	}
}
