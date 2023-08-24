package com.te.flinko.dto.employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;

import lombok.Data;

@Data
public class EmployeeLeaveDTO {
	
	private Long leaveAppliedId;
	
	private String leaveOfType;
	
	private Double leaveDuration;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalTime startTime;

	private LocalTime endTime;

	private String reason;

	private String status;

	private LinkedHashMap<String, String> approvedBy;

	private String rejectedBy;

	private String rejectionReason;
	
	private Long reportingManagerId;
	
	private String reportingManagerName;

}
