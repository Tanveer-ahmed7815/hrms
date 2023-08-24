package com.te.flinko.exception.admin;

//@author Rakesh Kumar Nayak
public class RuleNameSameException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public RuleNameSameException() {
		
		
	}
	

	public RuleNameSameException(String message) {
		
		super(message);
	}
}
