package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateEmployeeIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateEmployeeIdException(String msg) {
		super(msg);
	}
}
