package com.te.flinko.exception.admindept;

/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class PurchaseOrderNotFoundException extends RuntimeException {
	public PurchaseOrderNotFoundException(String msg) {
		super(msg);
	}
}
