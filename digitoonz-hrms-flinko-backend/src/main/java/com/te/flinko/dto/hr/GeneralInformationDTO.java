package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class GeneralInformationDTO {
	
	private long employeeInfoId;		
	private String firstName;
	private String lastName;
	private String emailId;
	private long mobileNumber;
	private Boolean isActive;
	
}
