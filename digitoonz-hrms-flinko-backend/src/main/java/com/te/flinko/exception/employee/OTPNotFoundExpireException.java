package com.te.flinko.exception.employee;

@SuppressWarnings("serial")
public class OTPNotFoundExpireException extends RuntimeException {

	public OTPNotFoundExpireException(String msg) {
		super(msg);
	}

}
