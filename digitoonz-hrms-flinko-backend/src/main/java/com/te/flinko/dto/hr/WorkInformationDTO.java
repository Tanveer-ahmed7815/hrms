package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WorkInformationDTO {
	
	private long employeeInfoId;
	private Long officialId;
	private String employeeId;
	private LocalDate doj;
	private String employeeType;
	private Long departmentId;
	private String department;
	private Long designationId;
	private String designation;
	private String employeementStatus;
	private Long branchId;
	private String officialEmailId;
	private String branchName;
	
}
