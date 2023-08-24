package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;

public interface AdminLeaveDetailsService {

	String addEmployeeLeaveDetails(Long companyIdLong, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

	List<EmployeeLeaveInfoDTO> leaveApprovals(Long companyId, String status);

	EmployeeLeaveInfoDTO leaveApproval(Long leaveAppliedId, Long employeeInfoId, Long companyId);

}
