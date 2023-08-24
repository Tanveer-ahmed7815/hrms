package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateStockUnitSymbolException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public DuplicateStockUnitSymbolException(String msg) {
		super(msg);
	}
}
