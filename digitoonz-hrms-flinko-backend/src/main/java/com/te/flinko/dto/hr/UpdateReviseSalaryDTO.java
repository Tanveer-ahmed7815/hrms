package com.te.flinko.dto.hr;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class UpdateReviseSalaryDTO {
	private Long reviseSalaryId;
	private Long companyId;
	private BigDecimal revisedSalary;
	private String reason;

}
