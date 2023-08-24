package com.te.flinko.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.employee.mongo.EmployeeTaskCommentDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTaskDetailsDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.employee.EmployeeTaskDetailsService;

@RestController
@RequestMapping("/api/v1/employee-task")
@CrossOrigin(origins = "*")
public class TaskAssignedController extends BaseConfigController {

	@Autowired
	private EmployeeTaskDetailsService taskDetailsService;

	@GetMapping("/all")
	public ResponseEntity<SuccessResponse> getAsignedTask(@RequestParam String employeeId, String status) {
		List<EmployeeTaskDetailsDTO> taskDetails = taskDetailsService.getAllTaskDetails(employeeId, status,
				getCompanyId());
		if (!taskDetails.isEmpty()) {
			return new ResponseEntity<>(SuccessResponse.builder().data(taskDetails).error(false).message("").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(taskDetails).error(false).message("No Task Assigned").build(),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/task-comment")
	public ResponseEntity<SuccessResponse> editComment(@RequestBody EmployeeTaskCommentDTO taskDetailsDTO) {
		Boolean editComment = taskDetailsService.editComment(taskDetailsDTO);
		if (Boolean.TRUE.equals(editComment)) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(null).error(false).message("Comment Updated").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(editComment).error(true)
					.message("Unable to update comment!!!").build(), HttpStatus.OK);
		}
	}
}
