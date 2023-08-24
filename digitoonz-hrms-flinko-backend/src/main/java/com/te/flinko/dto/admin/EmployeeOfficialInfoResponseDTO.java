package com.te.flinko.dto.admin;

import java.io.Serializable;

import lombok.Data;
//@author Rakesh Kumar Nayak
@Data
public class EmployeeOfficialInfoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long employeeInfoId;
	private String employeeId;
	private Long officialId;
	private String employeeName;
	private Long branchId;
	private String branchName;
	private String department;
	private String designation;
	private Long workWeekRuleId;
	private String ruleName;
	
	
}
