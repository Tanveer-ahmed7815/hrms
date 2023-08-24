package com.te.flinko.dto.employee;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeIdDto implements Serializable {
	private Long employeeInfoId;
	private String employeeId;
	private String firstname;
	private String lastName;
	private String emailId;

	public EmployeeIdDto(Long employeeInfoId, String firstname, String lastName) {
		this.employeeInfoId = employeeInfoId;
		this.firstname = firstname;
		this.lastName = lastName;
	}
}
