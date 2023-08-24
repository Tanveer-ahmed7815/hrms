package com.te.flinko.dto.admin;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompanyPayrollDropdownInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long payrollId;
	
	private String payrollName;
}
