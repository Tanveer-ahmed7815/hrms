package com.te.flinko.service.employee;

import com.te.flinko.dto.employee.CompanyEmployeeResignationDetailsDTO;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.repository.employee.EmployeeResignationRequestRepository;

public interface EmployeeResignationRequestService {

	CompanyEmployeeResignationDetailsDTO saveEmployeeResignation(CompanyEmployeeResignationDetailsDTO resignationDetails,Long employeeInfoId,Long companyId);

	CompanyEmployeeResignationDetailsDTO getEmployeeResignation(Long employeeInfoId,Long companyId);
}
