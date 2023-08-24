package com.te.flinko.exception.admindept;


/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class StockGroupNotFoundException extends RuntimeException {

	public StockGroupNotFoundException(String msg) {
		super(msg);
	}
}
