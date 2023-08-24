package com.te.flinko.dto.hr;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSalaryDetailsListDTO {
	
	private Long employeeSalaryId;
	private String employeeId;
	private String fullname;
	private BigDecimal grossPay;
	private BigDecimal netPay;
	private String lop;
	private String status;
	

}
