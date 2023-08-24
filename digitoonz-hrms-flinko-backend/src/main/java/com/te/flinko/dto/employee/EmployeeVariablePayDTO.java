package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeVariablePayDTO {
	
	private Long variablePayId;
	private BigDecimal percentage;
	private BigDecimal amount;
	private List<String> effectiveMonth;
	private String description;
	
}
