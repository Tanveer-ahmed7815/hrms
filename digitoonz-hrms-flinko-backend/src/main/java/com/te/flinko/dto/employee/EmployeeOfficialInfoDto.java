package com.te.flinko.dto.employee;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeOfficialInfoDto extends Audit implements Serializable {

	private Long officialId;
	private String employeeId;
	private String officialEmailId;
	private LocalDate doj;
	private String employeeType;
	private String designationName;
	private String department;
	private String employeementStatus;
	private LocalDate probationStartDate;
	private LocalDate probationEndDate;
}
