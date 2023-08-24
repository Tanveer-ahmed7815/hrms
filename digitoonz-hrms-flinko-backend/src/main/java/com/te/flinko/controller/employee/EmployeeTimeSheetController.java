package com.te.flinko.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.employee.EmployeeProjectListDTO;
import com.te.flinko.dto.employee.TimesheetDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTaskListDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTimesheetDetailsDTO;
import com.te.flinko.dto.employee.mongo.Timesheet;
import com.te.flinko.entity.project.mongo.ProjectTaskDetails;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.employee.EmployeeTimeSheetService;

@RestController
@RequestMapping("/api/v1/timesheet-details")
@CrossOrigin(origins = "*")
public class EmployeeTimeSheetController extends BaseConfigController{

	@Autowired
	EmployeeTimeSheetService service;

	@GetMapping("/project-list/{employeeInfoId}")
	public ResponseEntity<?> getProjectList(@PathVariable Long employeeInfoId) {

		List<EmployeeProjectListDTO> projectList = service.getProjectList(employeeInfoId,getCompanyId());
		if (!projectList.isEmpty())
			return new ResponseEntity<>(SuccessResponse.builder().data(projectList).error(false)
					.message("Data Fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(projectList).error(false)
					.message("No Projects Assigned").build(), HttpStatus.OK);
	}

	@PostMapping("/task")
	public ProjectTaskDetails saveProjectTaskDetails(@RequestBody ProjectTaskDetails projectTaskDetails) {
			
		return service.saveProjectTaskDetails(projectTaskDetails);	
	}	
	
	@PostMapping("/all-task/{employeeInfoId}")
	public ResponseEntity<?> getProjectTaskList(
			@RequestBody List<Long> projectIdList,@PathVariable Long employeeInfoId) {

		List<EmployeeTaskListDTO> taskList = service.getTaskList(employeeInfoId, projectIdList,getCompanyId());
		if (taskList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(taskList).error(false)
					.message("Data Fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(taskList).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}

	@PostMapping("/{employeeInfoId}")
	public ResponseEntity<?> saveTimeSheetDetails(@RequestBody EmployeeTimesheetDetailsDTO employeeTimesheetDetailsDTO,@PathVariable Long employeeInfoId) {

	EmployeeTimesheetDetailsDTO timeSheetDTO = service.saveEmployeeTimesheetDetails(employeeTimesheetDetailsDTO, employeeInfoId,getCompanyId());

		if (timeSheetDTO != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(timeSheetDTO).error(false)
					.message("Data Saved Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(timeSheetDTO).error(false)
					.message("Data not saved").build(), HttpStatus.OK);
	}
	

	@PostMapping("/{employeeInfoId}/{year}/{month}")
	public ResponseEntity<?> getTimeSheetList(@RequestBody List<String> projectList,@PathVariable Long employeeInfoId,@PathVariable int year,@PathVariable int month) {

		List<EmployeeTimesheetDetailsDTO> timeSheet = service.getTimeSheet(projectList,getCompanyId(),employeeInfoId, year, month);

		if (timeSheet  != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(timeSheet ).error(false)
					.message("Data Fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(timeSheet ).error(false)
					.message("Data Not Availble").build(), HttpStatus.OK);
	}

	@PutMapping("/{employeeInfoId}/{timesheetObjectId}")
	public ResponseEntity<?> editTimeSheet(@RequestBody Timesheet timesheet,@PathVariable Long employeeInfoId,@PathVariable String timesheetObjectId) {

		List<Timesheet> employeeTimeSheetList = service.editEmployeeTimeSheet(timesheet, employeeInfoId,timesheetObjectId,getCompanyId());

		if (employeeTimeSheetList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeTimeSheetList).error(false)
					.message("Data edited Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeTimeSheetList).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}
		
	@DeleteMapping("/{employeeInfoId}/{timesheetObjectId}/{id}")
	public ResponseEntity<?> deleteTimeSheet(@PathVariable Long employeeInfoId,@PathVariable String timesheetObjectId,@PathVariable String id) {

		service.deleteEmployeeTimeSheet(employeeInfoId, timesheetObjectId,id,getCompanyId());

	return new ResponseEntity<>(SuccessResponse.builder().data(null).error(false)
					.message("Data deleted Successfully").build(), HttpStatus.OK);		
	}
		
}
