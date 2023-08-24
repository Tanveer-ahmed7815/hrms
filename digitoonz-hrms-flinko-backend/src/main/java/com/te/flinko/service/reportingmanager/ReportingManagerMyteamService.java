package com.te.flinko.service.reportingmanager;

import java.util.List;

import com.te.flinko.dto.reportingmanager.EmployeePersonalInfoDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTaskDetailsDTO;
import com.te.flinko.dto.reportingmanager.ReportingmanagerMyTeamDTO;

public interface ReportingManagerMyteamService {

	List<ReportingmanagerMyTeamDTO> getEmployeeList(Long employeeInfoId,Long companyId);
	
	EmployeePersonalInfoDTO getEmployeeInfo(Long reportingManagerId,Long employeeInfoId,Long companyId);
		
	List<EmployeeTaskDetailsDTO> getEmployeeTaskList(Long reportingManagerId,Long employeeInfoId,Long companyId,String status);
}
