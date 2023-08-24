package com.te.flinko.dto.reportingmanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EligibleEmployeeDetailsDTO {
	
	private String employeeId;
	
	private Long employeeInfoId;
	
	private String fullName;
	
	private String emailId;
	
	private String department;
	
	private String designation;
	
	
	

}
