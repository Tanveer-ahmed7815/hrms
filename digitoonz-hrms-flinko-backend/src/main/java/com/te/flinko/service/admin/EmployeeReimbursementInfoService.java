package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeReimbursementInfoDTO;

public interface EmployeeReimbursementInfoService {

	List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId);

	EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId);

	String addEmployeeReimbursement(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
