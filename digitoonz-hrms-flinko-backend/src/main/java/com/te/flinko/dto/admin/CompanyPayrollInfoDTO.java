package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;

//@author rakesh Kumar Nayak
@Data
public class CompanyPayrollInfoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long payrollId;
	
	private Integer salaryApprovalDate;
	
	private Integer paymentDate;
	
	private Integer paySlipGenerationDate;
	
	private Boolean isPfEnabled;

	private Boolean isPtEnabled;
	
	private Boolean isCsiEnabled;
	
	private Boolean isAdvanceSalaryEnabled;
	
	private String payrollName;
	
	@NotEmpty(message = "Company payroll deduction should not be empty")
	private List<CompanyPayrollDeductionDTO> companyPayrollDeductionList;
	@NotEmpty(message = "company payroll earning should not be empty")
	private List<CompanyPayrollEarningDTO> companyPayrollEarningList;
	
	

}
