package com.te.flinko.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class LeadExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeadExistsException(String message) {
		super(message);
	}
}
