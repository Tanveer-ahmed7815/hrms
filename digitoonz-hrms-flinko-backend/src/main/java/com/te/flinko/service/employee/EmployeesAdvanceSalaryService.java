package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeAdvanceSalaryDTO;
import com.te.flinko.entity.employee.EmployeeAdvanceSalary;

public interface EmployeesAdvanceSalaryService {

	EmployeeAdvanceSalaryDTO saveAdvanceSalaryRequest(Long companyId,EmployeeAdvanceSalaryDTO advanceSalaryDTO,Long employeeInfoId);
	
	EmployeeAdvanceSalaryDTO getAdvanceSalary(Long advanceSalaryId,Long companyId);
	
	List<EmployeeAdvanceSalaryDTO>  getAdvanceSalaryDTOList(Long employeeInfoId,Long companyId);

	void deleteAdvanceSalaryRequest(Long advanceSalaryId,Long companyId);
	
	EmployeeAdvanceSalaryDTO editAdvanceSalaryRequest(EmployeeAdvanceSalaryDTO advanceSalaryDTO,Long advanceSalaryId,Long companyId);
}
