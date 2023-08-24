package com.te.flinko.exception;

public class EventDetailsNotFoundException extends RuntimeException {

	String message;
	
	public EventDetailsNotFoundException(String message) {
		super(message);
	}
}
