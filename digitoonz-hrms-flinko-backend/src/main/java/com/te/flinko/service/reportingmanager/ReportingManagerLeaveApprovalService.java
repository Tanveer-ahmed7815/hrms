package com.te.flinko.service.reportingmanager;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;

public interface ReportingManagerLeaveApprovalService {

	List<EmployeeLeaveInfoDTO> getLeaveDetailsByStatus(Long companyId, String status, Long employeeInfoId);

	EmployeeLeaveInfoDTO getLeaveDetailsById(Long leaveAppliedId, Long employeeInfoId, Long companyId);
	
	String updateLeaveStatus(Long companyIdLong, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);
}
