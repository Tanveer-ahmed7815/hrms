package com.te.flinko.dto.project;

import lombok.Data;

@Data
public class EmployeeDetailsForProjectDTO {
	
	private Long employeeInfoId;
	private String employeeName;
	private String officialEmailId;
	private Integer assignedTask;
	private Double ratings;
	
}
