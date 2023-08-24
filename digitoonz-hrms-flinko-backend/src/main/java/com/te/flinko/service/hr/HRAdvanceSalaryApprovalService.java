package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.AdvancedSalaryDTO;

public interface HRAdvanceSalaryApprovalService {
	
	List<AdvancedSalaryDTO> getAdvanceSalaryByStatus(Long companyId, String status, Long employeeInfoId);

	AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeReimbursementId);

	String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
