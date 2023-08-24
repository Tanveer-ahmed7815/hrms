package com.te.flinko.common.employee;

import com.google.common.util.concurrent.AtomicDouble;

public class EmployeeLeaveConstants {
	
	// service constants start
	public static final String LEAVE_STATUS_ALL="ALL";
	
	public static final String LEAVE_STATUS_PENDING="PENDING";
	
	public static final String LEAVE_STATUS_APPROVED="APPROVED";
	
	public static final String LEAVE_STATUS_REJECTED="REJECTED";
	
	public static final String LEAVE_STATUS_APPLIED="APPLIED";
	
	public static final String LEAVE_STATUS_INVALID="Invalid Leave Status";
	
	public static final String LEAVE_DURATION_HALF="HALF";
	
	public static final String LEAVE_DURATION_FULL="FULL";

	public static final String LEAVE_DURATION_OTHER="OTHER";
	
	public static final AtomicDouble ATOMIC_DOUBLE_ZERO = new AtomicDouble(0.0);

}
