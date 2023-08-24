package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/


@SuppressWarnings("serial")
public class NoSubjectPresentException extends RuntimeException {

	public NoSubjectPresentException (String message) {
		super(message);
	}
}
