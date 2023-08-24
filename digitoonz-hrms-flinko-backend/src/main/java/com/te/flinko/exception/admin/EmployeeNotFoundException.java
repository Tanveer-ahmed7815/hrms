package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class EmployeeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public EmployeeNotFoundException(String msg) {
		super(msg);
	}
}
