package com.te.flinko.exception.admindept;


/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class QuantityNotMatchedException extends RuntimeException {

	public QuantityNotMatchedException(String msg) {
		super(msg);
	}
}
