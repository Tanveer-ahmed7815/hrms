package com.te.flinko.dto.hr;

import java.util.List;

import lombok.Data;

@Data
public class CandidateDetailsDTO {
	
	private Long employeeId;
	private String fullName;
	private String emailId;
	private long mobileNumber;
	private String department;
	private String designation;
	private List<EmployeeDependantInfoDetailsDTO> employeeDependentInfoList;
	private List<EmployeeEducationDetailsDTO> employeeEducationInfoList;
	private List<BankInformationDTO> employeeBankInfoList;
	private List<EmployeeEmploymentDTO> employeeEmployment;
	
}
