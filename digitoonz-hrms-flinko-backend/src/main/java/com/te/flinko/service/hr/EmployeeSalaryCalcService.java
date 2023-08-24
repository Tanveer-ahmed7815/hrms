package com.te.flinko.service.hr;

import com.te.flinko.dto.hr.EmployeeSalaryAllDetailsDTO;

public interface EmployeeSalaryCalcService {
	
	Boolean calculateAllEmployeeSalary ();
	
	EmployeeSalaryAllDetailsDTO editEmployeeSalary (EmployeeSalaryAllDetailsDTO employeeSalaryAllDetailsDTO);
	
	Boolean calculateReviseSalary ();
	
}
