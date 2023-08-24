package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.employee.GetReportingManagerDTO;

public interface EmployeeHierarchyService {

	//Map<String,String> getHierarchy(Long employeeInfoId);
	GetReportingManagerDTO getEmployeeHierarchy(Long employeeInfoId, Long companyId);


}
