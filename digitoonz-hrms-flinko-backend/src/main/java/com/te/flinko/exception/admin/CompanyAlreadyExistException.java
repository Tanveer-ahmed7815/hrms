package com.te.flinko.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class CompanyAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CompanyAlreadyExistException(String message){
		super(message);
	}
	
}
