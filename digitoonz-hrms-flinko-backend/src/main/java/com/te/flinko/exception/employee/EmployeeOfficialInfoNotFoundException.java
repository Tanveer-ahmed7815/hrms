package com.te.flinko.exception.employee;


@SuppressWarnings("serial")
public class EmployeeOfficialInfoNotFoundException extends RuntimeException{
	public EmployeeOfficialInfoNotFoundException(String msg) {
		super(msg);
	}
}

