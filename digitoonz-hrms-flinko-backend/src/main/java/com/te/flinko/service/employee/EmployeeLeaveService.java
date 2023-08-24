package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeAllotedLeavesDTO;
import com.te.flinko.dto.employee.EmployeeApplyLeaveDTO;
import com.te.flinko.dto.employee.EmployeeCalanderDetailsDTO;
import com.te.flinko.dto.employee.EmployeeLeaveDTO;

public interface EmployeeLeaveService {

	EmployeeApplyLeaveDTO saveLeaveApplied(EmployeeApplyLeaveDTO employeeLeaveApplied, Long employeeInfoId);

	List<EmployeeApplyLeaveDTO> getLeavesList(String status, Long employeeInfoId, Integer year, List<Integer> months);
	
	List<EmployeeAllotedLeavesDTO> getAllotedLeavesList(Long employeeInfoId);
	
	EmployeeLeaveDTO getLeaveById(Long leaveAppliedId, Long employeeInfoId);

	EmployeeApplyLeaveDTO editLeave(EmployeeApplyLeaveDTO applyLeaveDto, Long leaveAppliedId, Long employeeInfoId);

	Boolean deleteLeave(Long leaveAppliedId, Long employeeInfoId);
	
	EmployeeCalanderDetailsDTO getAllCalenderDetails(Long employeeInfoId, Integer year, List<Integer> months );
	
	List<String> getLeaveTypesDropdown(Long employeeInfoId);

}
