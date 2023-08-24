package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
@Data
public class ReviseSalaryByIdDTO {
	private Long reviseSalaryId;
	private Long companyId;
	private String employeeId;
	private String fullName;
	private String department;
	private String designation;
	private String branch;
	private LocalDate dueDate;
	private LocalDate revisedDate;
	private BigDecimal revisedSalary;
	private String reason;
	private String status;
}
