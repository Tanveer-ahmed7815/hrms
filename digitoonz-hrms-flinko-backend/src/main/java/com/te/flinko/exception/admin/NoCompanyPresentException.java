package com.te.flinko.exception.admin;

//@author Rakesh Kumar Nayak
public class NoCompanyPresentException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public NoCompanyPresentException() {
		
	}



	public NoCompanyPresentException(String message) {
		super(message);
		
	}



}
