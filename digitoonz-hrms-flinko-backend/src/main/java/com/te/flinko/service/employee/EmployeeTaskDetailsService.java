package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.employee.mongo.EmployeeTaskCommentDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTaskDetailsDTO;

public interface EmployeeTaskDetailsService {

	List<EmployeeTaskDetailsDTO> getAllTaskDetails(String employeeId, String status, Long companyId);

	Boolean editComment(EmployeeTaskCommentDTO detailsDTO);

}
