package com.te.flinko.exception.employee;

@SuppressWarnings("serial")
public class InsufficientLeavesException extends RuntimeException {

	public InsufficientLeavesException(String msg) {
		super(msg);
	}
}
