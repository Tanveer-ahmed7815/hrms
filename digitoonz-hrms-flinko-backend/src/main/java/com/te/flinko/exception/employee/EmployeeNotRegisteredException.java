package com.te.flinko.exception.employee;

@SuppressWarnings("serial")
public class EmployeeNotRegisteredException extends RuntimeException{
	public EmployeeNotRegisteredException(String msg) {
		super(msg);
	}
}
