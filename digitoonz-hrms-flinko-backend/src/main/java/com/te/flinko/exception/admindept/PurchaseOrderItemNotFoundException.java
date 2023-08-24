package com.te.flinko.exception.admindept;

/**
 * 
 * @author Brunda
 *
 */
public class PurchaseOrderItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PurchaseOrderItemNotFoundException(String message) {
		super(message);
	}
}
