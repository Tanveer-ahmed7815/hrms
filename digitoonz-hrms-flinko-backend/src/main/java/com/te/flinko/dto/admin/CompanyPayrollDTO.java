package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CompanyPayrollDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String payrollName;
	private Long payrollId;

	@JsonFormat(pattern = "MM-dd-yyyy", timezone = "Asia/kolkata")
	private LocalDate salaryApprovalDate;

	@JsonFormat(pattern = "MM-dd-yyyy", timezone = "Asia/kolkata")
	private LocalDate paymentDate;

	@JsonFormat(pattern = "MM-dd-yyyy", timezone = "Asia/kolkata")
	private LocalDate paySlipGenerationDate;

	private Boolean isPfEnabled;

	private Boolean isPtEnabled;

	private Boolean isCsiEnabled;

	private Boolean isAdvanceSalaryEnabled;

	public CompanyPayrollDTO(String payrollName, Long payrollId, LocalDate salaryApprovalDate, LocalDate paymentDate,
			LocalDate paySlipGenerationDate, Boolean isPfEnabled, Boolean isPtEnabled, Boolean isCsiEnabled,
			Boolean isAdvanceSalaryEnabled) {
		
		this.payrollName = payrollName;
		this.payrollId = payrollId;
		this.salaryApprovalDate = salaryApprovalDate;
		this.paymentDate = paymentDate;
		this.paySlipGenerationDate = paySlipGenerationDate;
		this.isPfEnabled = isPfEnabled;
		this.isPtEnabled = isPtEnabled;
		this.isCsiEnabled = isCsiEnabled;
		this.isAdvanceSalaryEnabled = isAdvanceSalaryEnabled;
	}
	
	

}
