package com.te.flinko.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDropdownDTO {
	private Long employeeInfoId;
	private String employeeId;
	private String employeeFullName;

	public EmployeeDropdownDTO(Long employeeInfoId, String firstName, String lastName,
			String employeeId) {
		this.employeeInfoId = employeeInfoId;
		this.employeeId = employeeId;
		this.employeeFullName = firstName + " " + lastName;
	}
	
	
}
