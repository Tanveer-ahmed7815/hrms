package com.te.flinko.service.hr.mongo;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.mongo.EmployeeTimeSheetDTO;

public interface HRTimesheetApprovalService {
	
	List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status, Long employeeInfoId);
	
	EmployeeTimeSheetDTO getEmployeeTimesheetDetails(String timesheetObjectId, Long companyId);

	String updateEmployeeTimesheetDetails(Long companyId, String timesheetObjectId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

}
