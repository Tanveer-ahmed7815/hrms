package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;



import lombok.Data;


//@author Rakesh Kumar Nayak
@Data
public class CompanyPayrollEarningDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long earningId;
	
	private String salaryComponent;
	
	private String type;
	
	private BigDecimal value;
	
	private Boolean isTaxable;
	
	private Long payrollId;

	public CompanyPayrollEarningDTO(Long earningId, String salaryComponent, String type, BigDecimal value,
			Boolean isTaxable, Long payrollId) {
		
		this.earningId = earningId;
		this.salaryComponent = salaryComponent;
		this.type = type;
		this.value = value;
		this.isTaxable = isTaxable;
		this.payrollId = payrollId;
	}
	
	
	
}
