package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PersonalInformationDTO {

	private Long employeeInfoId;
	private String pan;
	private String gender;
	private LocalDate dob;
	private String bloodGroup;	
	private String guardiansName;
	private String maritalStatus;
	private List<String> language;
	private String emailId;
	private String linkedinId;
	private List<EmployeeAddressInfoDTO> employeeAddressDTO;
	
}
