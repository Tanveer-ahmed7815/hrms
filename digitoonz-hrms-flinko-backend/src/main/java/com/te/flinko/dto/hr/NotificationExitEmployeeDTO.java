package com.te.flinko.dto.hr;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationExitEmployeeDTO {
	
	private Long resignationId;

	private String employeeId;
	
	private String fullName;
	
	private String officialEmailId;
	
	private String department;
	
	private String designation;
		
	private String reportingManager;
		
	private Long mobileNumber;
	
	private String resignationReason;
	
	private BigDecimal annualSalary;
}
