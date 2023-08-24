package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class StatusNotFound extends RuntimeException {

	public StatusNotFound(String message) {
		super(message);
	}

}
