package com.te.flinko.controller.hr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.hr.NotificationExitEmployeeDTO;
import com.te.flinko.dto.hr.NotificationExitInterviewDTO;
import com.te.flinko.dto.hr.NotificationExitInterviewDropdownDTO;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.NotificationExitEmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hrnotification")
public class NotificationController extends BaseConfigController {

	@Autowired
	private NotificationExitEmployeeService exitEmployeeService;

	// Controller for scheduling Exit interview
	@PostMapping("/scheduleinterview/{resignationId}")
	ResponseEntity<SuccessResponse> scheduleInterview(@RequestBody NotificationExitInterviewDTO scheduleInterviewdto,
			@PathVariable Long resignationId) {

		NotificationExitInterviewDTO resignationDiscussion = exitEmployeeService.scheduleInterview(scheduleInterviewdto,
				resignationId);

		if (resignationDiscussion != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(resignationDiscussion).error(false).message("Exit Interview Scheduled Successfully").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("NUll").build(),
					HttpStatus.NOT_FOUND);
		}

	}

	// Controller for fetching the employees who have applied for resignation for a
	// company
	@GetMapping("/resignationdetailslist/{companyId}")
	ResponseEntity<SuccessResponse> displayExitEmployeeList(@PathVariable Long companyId) {

		List<NotificationExitEmployeeDTO> resignationDetailsList = exitEmployeeService.resignationDetails(getUserId(),companyId);

		if (!resignationDetailsList.isEmpty()) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(resignationDetailsList).error(false).message("Data fetched").build(),
					HttpStatus.OK);
		} else {
			throw new DataNotFoundException("No employees Applied for Resignation");
		}

	}

//Controller for fetching the details of employees who have applied for resignation based on resignation id
	@GetMapping("/resignationdetails/{resignationId}")
	ResponseEntity<SuccessResponse> displayExitEmployeeDetails(@PathVariable Long resignationId) {
		NotificationExitEmployeeDTO exitEmployeedto = exitEmployeeService.exitEmployeedto(resignationId);
		if (exitEmployeedto != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(exitEmployeedto).error(false).message("Data fetched").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(null).error(true).message("Data Not fetched").build(),
					HttpStatus.NOT_FOUND);
		}
	}

	// Controller for fetching the employees for organizers dropdown
	@GetMapping("/dropdown/{companyId}")
	ResponseEntity<SuccessResponse> organizersDetails(@PathVariable("companyId") Long companyId) {

		List<NotificationExitInterviewDropdownDTO> exitInterviewDropdowndtoList = exitEmployeeService
				.getExitInterviewDropdowndtoList(companyId);
		if (!exitInterviewDropdowndtoList.isEmpty()) {
			return new ResponseEntity<>(SuccessResponse.builder().data(exitInterviewDropdowndtoList).error(false)
					.message("list fetched").build(), HttpStatus.OK);
		} else {
			throw new DataNotFoundException("Employees for the Company Not Found");
		}

	}

}