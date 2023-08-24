package com.te.flinko.dto.employee;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class EmployeeApplyLeaveDTO {

	private Long leaveAppliedId;

	private String leaveOfType;
	
	private String leaveType;
	
	private Double leaveDuration;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalTime startTime;

	private LocalTime endTime;

	private String reason;
	
	private String status;

	private String reportingManager;

}
