package com.te.flinko.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInformationDTO {
	
	private String employeeId;
	
	private Long employeeInfoId;
	
	private String fullname;

}
