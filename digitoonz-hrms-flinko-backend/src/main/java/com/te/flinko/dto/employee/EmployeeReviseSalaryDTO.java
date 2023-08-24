package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReviseSalaryDTO {
	private Long reviseSalaryId;
	private String employeeId;
	private Long companyId;
	private String fullName;
	private String designation;
	private String department;
	private BigDecimal amount;
	private LocalDate dueDate;
	private String reason;
	private String status;

	public EmployeeReviseSalaryDTO(Long reviseSalaryId, String employeeId, Long companyId, String fullName,
			String designation, String department, BigDecimal amount, String reason) {
		this.reviseSalaryId = reviseSalaryId;
		this.employeeId = employeeId;
		this.companyId = companyId;
		this.fullName = fullName;
		this.designation = designation;
		this.department = department;
		this.amount = amount;
		this.reason = reason;
	}
	
}
