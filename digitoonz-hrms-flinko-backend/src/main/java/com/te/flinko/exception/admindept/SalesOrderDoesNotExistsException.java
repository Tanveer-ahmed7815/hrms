package com.te.flinko.exception.admindept;

public class SalesOrderDoesNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SalesOrderDoesNotExistsException(String message) {
		super(message);
	}

}
