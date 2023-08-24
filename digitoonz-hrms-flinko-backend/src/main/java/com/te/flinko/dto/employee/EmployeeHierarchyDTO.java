package com.te.flinko.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHierarchyDTO {

	private Long employeeInfoId;
	
	private String firstName;
	
	private String designation;
}
