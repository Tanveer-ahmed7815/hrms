package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class StockCategoryNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public StockCategoryNotFoundException(String message) {
		super(message);
	}
}
