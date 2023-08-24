package com.te.flinko.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventManagementNameFetchDTO {

	private Long employeeInfoId;
	
	private String employee;
	
	//private String lastName;
	
	private String employeeId;
}
