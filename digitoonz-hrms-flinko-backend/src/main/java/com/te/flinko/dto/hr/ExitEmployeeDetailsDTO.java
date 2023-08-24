package com.te.flinko.dto.hr;

import java.util.List;

import lombok.Data;

@Data
public class ExitEmployeeDetailsDTO {
	
	private Long employeeInfoId;
	private String fullName;
	private String officialId;
	private String department;
	private String designation;
	private String reportingManager;
	private Double annualSalary;
	private Long mobileNumber;
	private String reason;
	private List<OrganiserDetialsDTO> organiser;
	
}
