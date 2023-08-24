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
public class EmployeeLetterInfoDto {

	private Long employeeInfoId;
	private String name;
	private String branchName;
	private String designation;
	private String department;
	private String leaveOfLetter;
	private String issuedBy;
	private LocalDate issuedDate;
	private String letter;
	private String status; 
	private String rejectionReason;
	
	
}
