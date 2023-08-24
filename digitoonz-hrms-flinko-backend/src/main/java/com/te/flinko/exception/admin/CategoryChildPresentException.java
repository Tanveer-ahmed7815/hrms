package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class CategoryChildPresentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CategoryChildPresentException(String message) {
		super(message);
	}
}
