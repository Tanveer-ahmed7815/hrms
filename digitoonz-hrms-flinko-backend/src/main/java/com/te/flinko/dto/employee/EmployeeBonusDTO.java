package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeBonusDTO {
	
	private Long bonusId;
	private BigDecimal amount;
	private LocalDate effectiveDate;
	private String reason;
}
