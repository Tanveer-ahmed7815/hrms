package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;

public interface HRLeaveApprovalService {

	String updateLeaveStatus(Long companyIdLong, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

	List<EmployeeLeaveInfoDTO> getLeaveDetailsByStatus(Long companyId, String status, Long employeeInfoId);

	EmployeeLeaveInfoDTO getLeaveDetailsById(Long leaveAppliedId, Long employeeInfoId, Long companyId);
	
}
