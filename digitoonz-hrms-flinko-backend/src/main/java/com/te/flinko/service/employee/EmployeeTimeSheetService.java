package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeProjectListDTO;
import com.te.flinko.dto.employee.TimesheetDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTaskListDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTimesheetDetailsDTO;
import com.te.flinko.dto.employee.mongo.Timesheet;
import com.te.flinko.entity.project.mongo.ProjectTaskDetails;

public interface EmployeeTimeSheetService {
	
	List<EmployeeProjectListDTO> getProjectList(Long employeeInfoId,Long companyId);
	
	ProjectTaskDetails saveProjectTaskDetails(ProjectTaskDetails projectTaskDetails);
	
	List<EmployeeTaskListDTO> getTaskList(Long employeeInfoId, List<Long> projectIdList,Long companyId);
	
	EmployeeTimesheetDetailsDTO saveEmployeeTimesheetDetails(EmployeeTimesheetDetailsDTO employeeTimesheetDetailsDTO,Long employeeInfoId,Long companyId);
	
	List<EmployeeTimesheetDetailsDTO> getTimeSheet(List<String> projectList,Long CompanyId,Long employeeInfoId, int year, int month);
	
	List<Timesheet> editEmployeeTimeSheet(Timesheet timesheet,Long employeeInfoId,String timesheetObjectId,Long companyId);
	
	void deleteEmployeeTimeSheet(Long employeeInfoId,String timesheetObjectId,String id,Long companyId);
}
