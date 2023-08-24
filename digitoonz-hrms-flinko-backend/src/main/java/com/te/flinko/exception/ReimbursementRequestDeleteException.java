package com.te.flinko.exception;

public class ReimbursementRequestDeleteException extends RuntimeException{

	String message;
	
	public ReimbursementRequestDeleteException(String message) {
	
		super(message);
	}
}
