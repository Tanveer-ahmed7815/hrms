package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class StockGroupNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public StockGroupNotFound(String message) {
		super(message);
	}
}
