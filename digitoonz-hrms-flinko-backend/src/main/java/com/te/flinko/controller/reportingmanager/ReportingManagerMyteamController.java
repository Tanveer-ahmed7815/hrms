package com.te.flinko.controller.reportingmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.reportingmanager.EmployeePersonalInfoDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTaskDetailsDTO;
import com.te.flinko.dto.reportingmanager.ReportingmanagerMyTeamDTO;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.reportingmanager.ReportingManagerMyteamService;

@RestController
@RequestMapping("/api/v1/reporting-manager")
@CrossOrigin(origins = "*")
public class ReportingManagerMyteamController extends BaseConfigController {

	@Autowired
	private ReportingManagerMyteamService service;
	
	@GetMapping("/all/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getEmployeesReportingInfo(@PathVariable Long employeeInfoId){
		
		List<ReportingmanagerMyTeamDTO> employeeList = service.getEmployeeList(employeeInfoId,getCompanyId());
		
		if(employeeInfoId != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeList).error(false).message("Data Feteched Successfully").build(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeList).error(true).message("No such data").build(),HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/info/{reportingManagerId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getEmployeeInfo(@PathVariable Long reportingManagerId,@PathVariable Long employeeInfoId){
		
		EmployeePersonalInfoDTO employeeInfo = service.getEmployeeInfo(reportingManagerId,employeeInfoId,getCompanyId());
		
		if(employeeInfoId != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeInfo).error(false).message("Data fetched Successfully").build(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeInfo).error(true).message("No such data").build(),HttpStatus.OK);
		}		
	}
	
	@GetMapping("/task/{reportingManagerId}/{employeeInfoId}/{status}")
	public ResponseEntity<SuccessResponse> getEmployeeTaskList(@PathVariable Long reportingManagerId,@PathVariable Long employeeInfoId,@PathVariable String status){
		
		 List<EmployeeTaskDetailsDTO> employeeTaskList = service.getEmployeeTaskList(reportingManagerId,employeeInfoId,getCompanyId(),status);
		
		if(!employeeTaskList.isEmpty()) {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeTaskList).error(false).message("Data fetched Successfully").build(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeTaskList).error(true).message("No such data").build(),HttpStatus.OK);
		}
		
	}
}
