package com.te.flinko.service.reportingmanager;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeReimbursementInfoDTO;

public interface ReportingManagerReimbursementApprovalService {

	List<EmployeeReimbursementInfoDTO> getReimbursementByStatus(Long companyId, String status, Long employeeInfoId);
	
	EmployeeReimbursementInfoDTO getReimbursementDetails(Long companyId,Long reimbursementId);
	
	String addEmployeeReimbursementInfo(Long companyId, Long reimbursementId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);
}
