package com.te.flinko.service.admin.mongo;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.mongo.EmployeeTimeSheetDTO;

public interface AdminTimesheetDetailsService {

	List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status);

	EmployeeTimeSheetDTO getEmployeeTimesheetDetails(String timesheetObjectId, Long companyId);

	String updateEmployeeTimesheetDetails(Long companyId, String timesheetObjectId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);
}
