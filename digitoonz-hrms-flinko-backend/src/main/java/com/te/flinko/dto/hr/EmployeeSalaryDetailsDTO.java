package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.util.Map;

import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeSalaryDetailsDTO {
	private Long employeeSalaryId;
	private Map<String, String> earning;
	private Map<String, String> deduction;
	private Map<String, String> additional;
	private BigDecimal netPay;
	private List<Integer> month;
	private Integer year;
	private Boolean isFinalized;
	private Boolean isPaid;
	private Boolean isPayslipGenerated;
	private BigDecimal totalSalary;
	private long employeeId;
	private String employeeName;
	//private EmployeePersonalInfo employeePersonalInfo;
	//private CompanyInfo companyInfo;
	
	private List<String> department;
	private Long companyId;
}
