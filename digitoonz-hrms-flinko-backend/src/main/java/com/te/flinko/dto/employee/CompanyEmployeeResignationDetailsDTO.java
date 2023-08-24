package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.te.flinko.entity.employee.EmployeeResignationDiscussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEmployeeResignationDetailsDTO {

	private LocalDate appliedDate;

	private String resignationReason;

	private List<EmployeeResignationDiscussionDTO> discussion;

	private List<LocalDate> discussionDate;

	private Map<String, String> feedback;

	private LocalDate noticePeriodStartDate;

	private BigDecimal noticePeriodDuration;

	private List<String> accessorySubmission;

	private List<String> checkListToBeCleared;
	
	private String status;
}
