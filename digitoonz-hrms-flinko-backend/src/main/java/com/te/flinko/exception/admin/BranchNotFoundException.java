package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class BranchNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public BranchNotFoundException(String msg){
		super(msg);
	}
}
