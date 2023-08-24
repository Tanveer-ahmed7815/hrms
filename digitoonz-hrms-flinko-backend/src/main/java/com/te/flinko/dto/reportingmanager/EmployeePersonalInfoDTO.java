package com.te.flinko.dto.reportingmanager;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePersonalInfoDTO {

	private String employeeId;
	
	private String fullName;
	
	private String officialEmailId;
	
	private long mobileNumber;
	
	private String designation;
	
	private String department;
	
	private String branchName;
	
	private List<String> projectName;
}
