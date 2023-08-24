package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

//@author Rakesh Kumar Nayak
@Data
public class CompanyPayrollInfoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String payrollName;
	private Long payrollId;

	private Integer salaryApprovalDate;

	private Integer paymentDate;

	private Integer paySlipGenerationDate;

	private Boolean isPfEnabled;

	private Boolean isPtEnabled;

	private Boolean isCsiEnabled;
	
	private Boolean isSubmited;

	private Boolean isAdvanceSalaryEnabled;

	private List<CompanyPayrollDeductionDTO> companyPayrollDeductionList;

	private List<CompanyPayrollEarningDTO> companyPayrollEarningList;

	public CompanyPayrollInfoResponseDTO(Long payrollId,String payrollName, Integer salaryApprovalDate,
			Integer paymentDate, Integer paySlipGenerationDate, Boolean isPfEnabled, Boolean isPtEnabled,
			Boolean isCsiEnabled, Boolean isAdvanceSalaryEnabled) {
		
		
		this.payrollId = payrollId;
		this.payrollName = payrollName;
		this.salaryApprovalDate = salaryApprovalDate;
		this.paymentDate = paymentDate;
		this.paySlipGenerationDate = paySlipGenerationDate;
		this.isPfEnabled = isPfEnabled;
		this.isPtEnabled = isPtEnabled;
		this.isCsiEnabled = isCsiEnabled;
		this.isAdvanceSalaryEnabled = isAdvanceSalaryEnabled;
	}

	
	
}
