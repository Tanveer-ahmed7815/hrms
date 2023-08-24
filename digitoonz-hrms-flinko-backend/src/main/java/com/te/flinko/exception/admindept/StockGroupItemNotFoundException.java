package com.te.flinko.exception.admindept;

/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class StockGroupItemNotFoundException extends RuntimeException {

	public StockGroupItemNotFoundException(String msg) {
		super(msg);
	}
}
