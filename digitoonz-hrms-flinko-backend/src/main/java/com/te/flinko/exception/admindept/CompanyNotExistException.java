package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class CompanyNotExistException extends RuntimeException{

	public CompanyNotExistException (String message) {
		
		super(message);
	}
}
