package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;
@Data
public class EmployeeSalaryAllDetailsDTO {
	private Long employeeSalaryId;
	private String employeeId;
	private String employeeName;
	private BigDecimal netPay;
	private Map<String, String> earning;
	private Map<String, String> additional;
	private Map<String, String> deduction;
	private Map<String, String> reimbursements;
    private String lop;
    private BigDecimal grossPay;
    private String remarks;
    private Boolean isFinalized;
}
