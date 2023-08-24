package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class EmployeeDisplayDetailsDTO {

	private Long employeeId;
	private String fullName;
	private String officialEmailId;
	private String designation;
	private String department;
	private Boolean isActive;
	
}
