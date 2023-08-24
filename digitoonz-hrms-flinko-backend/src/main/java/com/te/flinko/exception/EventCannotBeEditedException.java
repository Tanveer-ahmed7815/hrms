package com.te.flinko.exception;

public class EventCannotBeEditedException extends RuntimeException {

	String message;
	
	public EventCannotBeEditedException(String message) {
		super(message);
	}
}
