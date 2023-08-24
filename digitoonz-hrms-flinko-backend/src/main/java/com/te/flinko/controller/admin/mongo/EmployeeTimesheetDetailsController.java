package com.te.flinko.controller.admin.mongo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.mongo.AdminTimesheetDetailsService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin")
@RestController
@RequiredArgsConstructor
public class EmployeeTimesheetDetailsController extends BaseConfigController {

	private final AdminTimesheetDetailsService employeeTimesheetDetailsService;

	@GetMapping("timesheets/{status}")
	public ResponseEntity<SuccessResponse> getAllTimesheetDetails(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Successfully Retrieve All " + status.substring(0, 1).toUpperCase()
						+ status.substring(1).toLowerCase() + " Timesheet Details")
				.data(employeeTimesheetDetailsService.getAllEmployeeTimesheetDetails(getCompanyId(), status)).build());
	}

	@GetMapping("timesheet/{timesheetObjectId}")
	public ResponseEntity<SuccessResponse> getTimesheetDetails(@PathVariable String timesheetObjectId) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Fetch Timesheet Detalis Successfully")
				.data(employeeTimesheetDetailsService.getEmployeeTimesheetDetails(timesheetObjectId, getCompanyId()))
				.build());
	}

	@PutMapping("timesheet/{timesheetObjectId}")
	public ResponseEntity<SuccessResponse> updateTimesheetDetails(
			@RequestBody AdminApprovedRejectDto adminApprovedRejectDto, @PathVariable String timesheetObjectId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).data("Update Timesheet Successfully")
						.message(employeeTimesheetDetailsService.updateEmployeeTimesheetDetails(getCompanyId(),
								timesheetObjectId, getEmployeeId(), adminApprovedRejectDto))
						.build());
	}

}
