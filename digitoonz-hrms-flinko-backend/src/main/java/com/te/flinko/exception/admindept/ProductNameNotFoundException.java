package com.te.flinko.exception.admindept;

/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class ProductNameNotFoundException extends RuntimeException {
	public ProductNameNotFoundException(String msg) {
		super(msg);
	}
}
