package com.te.flinko.service.reportingmanager.mongo;

import java.util.List;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.mongo.EmployeeTimeSheetDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTimesheetDetailsApprovalDTO;

public interface ReportingManagerTimesheetApprovalService {
	
	List<EmployeeTimeSheetDTO> getEmployeeTimesheetList( Long companyId,String status,Long employeeInfoId);
	
	EmployeeTimesheetDetailsApprovalDTO getEmployeetimesheet(String timesheetObjectId, Long companyId);
	
	String addResponseToTimesheet(String timesheetObjectId,Long companyId,String employeeId , AdminApprovedRejectDto adminApprovedRejectDto);

}
