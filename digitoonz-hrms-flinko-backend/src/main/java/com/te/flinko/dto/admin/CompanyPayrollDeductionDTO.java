package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;



import lombok.Data;

//@author Rakesh Kumar Nayak
@Data
public class CompanyPayrollDeductionDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long deductionId;
	
	private String title;
	
	private String type;
	
	private BigDecimal value;
	
	private Long payrollId;

	public CompanyPayrollDeductionDTO(Long deductionId, String title, String type, BigDecimal value, Long payrollId) {
		
		this.deductionId = deductionId;
		this.title = title;
		this.type = type;
		this.value = value;
		this.payrollId = payrollId;
	}
	
	
	
}
