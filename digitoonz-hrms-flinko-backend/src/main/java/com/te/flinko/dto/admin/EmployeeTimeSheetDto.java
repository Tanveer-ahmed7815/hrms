package com.te.flinko.dto.admin;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeTimeSheetDto {
	private String employeeId;
	private Long employeeInfoId;
	private String employeeName;
	private String branch;
	private String department;
	private String designation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String timeSheetName;
	private String status;
}
