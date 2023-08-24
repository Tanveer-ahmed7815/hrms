package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeReimbursementInfoDTO;

public interface HRReimbursementApprovalService {

	public List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId, String status, Long employeeInfoId);

	EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId);

	String updateReimbursementStatus(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
