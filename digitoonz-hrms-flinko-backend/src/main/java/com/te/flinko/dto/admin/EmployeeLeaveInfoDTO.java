package com.te.flinko.dto.admin;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeLeaveInfoDTO {
	private Long leaveAppliedId;
	private Long employeeInfoId;
	private String employeeId;
	private String name;
	private String branch;
	private String department;
	private String reportingManager;
	private String designation;
	private String leaveOfType;
	private Long days;
	private Long pendingLeaves;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private String reason;
	private String rejectedReason;
	private Boolean isActionRequired;
	private String pendingAt;
	private String rejectedBy;
	private String rejectionReason;
}
