package com.te.flinko.exception.employee;

@SuppressWarnings("serial")
public class EmployeeLoginException extends RuntimeException {

	public EmployeeLoginException(String msg) {
		super(msg);
	}
}
