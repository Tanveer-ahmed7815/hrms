package com.te.flinko.exception.admin;

/**
 * @author Tapas
 *
 */

public class IsSubmittedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IsSubmittedException(String message) {
		super(message);
	}
}
