package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeAnnualSalaryDTO {
	private Long annualSalaryId;
	private LocalDate effectiveDate;
	private BigDecimal annualSalary;
	private String reason;
	private String frequency;
	private String payMethod;
	private Boolean isPfEnabled;
	private Boolean isPtEnabled;
	private Boolean isEsiEnabled;
	private Boolean isBonusEnabled;
	private Boolean isPayEnabled;
	private Boolean isSalaryStopped;
	private LocalDate salaryStoppedFrom;
	private String state;
	private String uan;
	private String pfNumber;
	private String esiNumber;
	
	private List<EmployeeBonusDTO> employeeBonusDTOList;
	private EmployeeVariablePayDTO employeeVariablePayDTO;
	
	private Long employeePersonalId;
	private Long companyInfoId;
	private Long companyPayrollInfoId;
	private String companyPayrollName;
	
}
