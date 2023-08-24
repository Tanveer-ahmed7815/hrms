package com.te.flinko.exception.admin;
/**
 * @author Tapas
 *
 */
public class WorkWeekRuleNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public WorkWeekRuleNotFoundException(String msg) {
		super(msg);
	}
}
