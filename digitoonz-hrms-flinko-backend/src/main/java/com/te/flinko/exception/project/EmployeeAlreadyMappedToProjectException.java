package com.te.flinko.exception.project;

public class EmployeeAlreadyMappedToProjectException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeAlreadyMappedToProjectException(String message) {
		super(message);
	}
	
}
