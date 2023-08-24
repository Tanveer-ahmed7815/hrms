package com.te.flinko.controller.reportingmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.reportingmanager.ApprisalMeetingDTO;
import com.te.flinko.dto.reportingmanager.EditInterviewDTO;
import com.te.flinko.dto.reportingmanager.EligibleEmployeeDetailsDTO;
import com.te.flinko.dto.reportingmanager.ScheduleInterviewDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.reportingmanager.NotificationListService;

@RestController
@RequestMapping("/api/v1/apprisal")
@CrossOrigin(origins = "*")
public class NotificationListController extends BaseConfigController{
	@Autowired
	NotificationListService notificationListService;

	@GetMapping("/getlist/{employeeInfoId}/{companyId}")
	ResponseEntity<SuccessResponse> getAll(@PathVariable Long employeeInfoId, @PathVariable Long companyId) {

		List<EligibleEmployeeDetailsDTO> departmentInfo = notificationListService.getEmployeeList(employeeInfoId,
				companyId);
		if (!departmentInfo.isEmpty()) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(departmentInfo).error(false).message("List fetched").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(departmentInfo).error(false)
					.message("No Apprisal Meeting To Be Scheduled").build(), HttpStatus.OK);

		}

	}

	@GetMapping("/get/{employeeInfoId}/{companyId}")
	ResponseEntity<SuccessResponse> getEmployee(@PathVariable Long employeeInfoId, @PathVariable Long companyId) {

		// List<EligibleEmployeeDetailsDTO> departmentInfo =
		// notificationListService.(employeeInfoId,companyId);
		ApprisalMeetingDTO detailsDTO = notificationListService.getEmployee(employeeInfoId, companyId);
		if (detailsDTO != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("List fetched").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("Apprisal Not Scheduled").build(),
					HttpStatus.OK);

		}

	}

	@PostMapping("/addInterview/{employeeInfoId}/{companyId}")
	ResponseEntity<SuccessResponse> addInterview(@RequestBody ScheduleInterviewDTO interviewDTO,
			@PathVariable Long employeeInfoId, @PathVariable Long companyId) {

		ScheduleInterviewDTO detailsDTO = notificationListService.addInterview(interviewDTO, employeeInfoId, companyId,getUserId());
		if (detailsDTO != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("Apprisal Scheduled").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("Apprisal Not Scheduled").build(),
					HttpStatus.OK);

		}

	}

	
	@PutMapping("/editInterview/{meetingId}/{employeeInfoId}/{companyId}")
	ResponseEntity<SuccessResponse> editInterview(@RequestBody EditInterviewDTO editinterviewDTO,@PathVariable Long employeeInfoId,@PathVariable Long meetingId,
			 @PathVariable Long companyId) {

		EditInterviewDTO detailsDTO = notificationListService.editInterview(editinterviewDTO,employeeInfoId,meetingId,companyId,getUserId());
		if (detailsDTO != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("Apprisal Scheduled").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(detailsDTO).error(false).message("Apprisal Not Scheduled").build(),
					HttpStatus.OK);

		}

	}

}
