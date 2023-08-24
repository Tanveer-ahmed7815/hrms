package com.te.flinko.exception;

public class AdvanceSalaryApprovalDeleteException extends RuntimeException {

	String message;
	
public AdvanceSalaryApprovalDeleteException(String message) {
	
	super(message);
	}
}
