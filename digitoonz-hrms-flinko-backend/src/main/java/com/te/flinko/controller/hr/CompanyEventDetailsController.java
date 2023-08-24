package com.te.flinko.controller.hr;

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

import com.te.flinko.dto.hr.BranchNameFetchDTO;
import com.te.flinko.dto.hr.EventManagementAddEventDTO;
import com.te.flinko.dto.hr.EventManagementAddWinnerDTO;
import com.te.flinko.dto.hr.EventManagementDepartmentNameDTO;
import com.te.flinko.dto.hr.EventManagementDisplayEventDTO;
import com.te.flinko.dto.hr.EventManagementEditEventDTO;
import com.te.flinko.dto.hr.EventManagementNameFetchDTO;
	import com.te.flinko.dto.hr.EventManagementParticipantslistDTO;
import com.te.flinko.entity.hr.CompanyEventDetails;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.CompanyEventDetailsService;

@RestController
@RequestMapping("/api/v1/event")
@CrossOrigin(origins = "*")
public class CompanyEventDetailsController {

	private static final String DATA_INSERTED = "Deatails Saved Successfully!!";
	private static final String DATA_UPDATED = "Changes Updated Successfully!!";
	@Autowired
	private CompanyEventDetailsService companyEventDetailsService;

// Controller for fetching department names for department dropdown ..
	@GetMapping("/getdept")
	ResponseEntity<SuccessResponse> fetchDepartment() {

		List<EventManagementDepartmentNameDTO> departmentInfo = companyEventDetailsService.fetchDepartmentNames();
		if (departmentInfo != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(departmentInfo).error(false).message("List fetched").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(departmentInfo).error(true).message("List Not fetched").build(),
					HttpStatus.OK);

		}

	}

	// Controller for fetching the employee name
	@GetMapping("/getfirstname/{companyId}")
	ResponseEntity<SuccessResponse> fetchEmployeeName(@PathVariable("companyId") Long companyId) {

		List<EventManagementNameFetchDTO> employeeNmae = companyEventDetailsService.fetchEmployeeNames(companyId);

		if (!employeeNmae.isEmpty()) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeNmae).error(false).message("Employee names fetched").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeNmae).error(true)
					.message("Employee names Not fetched").build(), HttpStatus.OK);
		}
	}

	// Controller for fetching department dependent Employees
	@PostMapping("/getemployeelist/{companyId}")
	ResponseEntity<SuccessResponse> fetchEmployeeName(@RequestBody List<String> department,
			@PathVariable("companyId") Long companyId) {

		List<EventManagementNameFetchDTO> employeeName = companyEventDetailsService
				.fetchMultipleDepartmentEmployees(companyId, department);

		if (!employeeName.isEmpty()) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeName).error(false).message("Employee names fetched").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeName).error(true)
					.message("Employee names Not fetched").build(), HttpStatus.OK);
		}
	}

	// controller for displaying the event list based on eventId
	@GetMapping("/eventlist/{eventId}")
	ResponseEntity<SuccessResponse> getEvent(@PathVariable("eventId") Long eventId) {
		EventManagementDisplayEventDTO eventList = companyEventDetailsService.displayEventDetails(eventId);
		if (eventList != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(eventList).error(false).message("eventlist dispalyed").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(null).error(true).message("EventId Not Available").build(),
					HttpStatus.OK);
		}

	}

	// Controller for displaying the event list based on companyId and month and
	// year
	@GetMapping("/displayeventslist/{companyId}/{year}/{month}")
	ResponseEntity<SuccessResponse> getEvents(@PathVariable("companyId") Long companyId, @PathVariable("year") int year,
			@PathVariable("month") int month) {
		List<EventManagementDisplayEventDTO> eventList = companyEventDetailsService.displayEventList(companyId, year,
				month);
		if (!eventList.isEmpty()) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(eventList).error(false).message("eventlist dispalyed").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(eventList).error(true).message("No Event Present").build(),
					HttpStatus.OK);
		}

	}

// Controller for adding the New Event
	@PostMapping("/add/{companyId}")
	ResponseEntity<SuccessResponse> addEvent(@RequestBody EventManagementAddEventDTO addEventdto,
			@PathVariable Long companyId) {

		EventManagementAddEventDTO eventDetails = companyEventDetailsService.adddetails(addEventdto, companyId);
		if (eventDetails != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(eventDetails).error(false).message("New Event Created Successfully").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("Null").build(),
					HttpStatus.NOT_FOUND);

		}
	}

	// Controller for uploading the winner
	@PutMapping("/addWinner/{eventId}")
	ResponseEntity<SuccessResponse> addWinner(@RequestBody EventManagementAddWinnerDTO addWinnerdto,
			@PathVariable("eventId") Long eventId) {

		EventManagementAddWinnerDTO winnerDetails = companyEventDetailsService.addWinners(addWinnerdto, eventId);

		if (winnerDetails != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(winnerDetails).error(false).message("Winner Details Updated Successfully").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("Null").build(),
					HttpStatus.NOT_FOUND);

		}

	}

	// Controller for deleting the Event
	@DeleteMapping("deleteEvent/{eventId}")
	ResponseEntity<SuccessResponse> deleteEvent(@PathVariable("eventId") Long eventId) {

		companyEventDetailsService.deleteEvent(eventId);

		return new ResponseEntity<>(SuccessResponse.builder().data(null).error(false).message("Data deleted").build(),
				HttpStatus.OK);

	}

	// Controller for editing the event

	@PutMapping("/editEvent/{eventId}")
	ResponseEntity<SuccessResponse> addWinner(@RequestBody EventManagementEditEventDTO editEventdto,
			@PathVariable("eventId") Long eventId) {

		EventManagementEditEventDTO winnerDetails = companyEventDetailsService.editEvent(editEventdto, eventId);

		if (winnerDetails != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(winnerDetails).error(false).message("Event Updated Successfully").build(),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("Null").build(),
					HttpStatus.NOT_FOUND);

		}

	}

	// Controller for Fetching participants list for selecting Winners Api..

	@GetMapping("/participentsList/{eventId}")
	ResponseEntity<SuccessResponse> participantslist(@PathVariable("eventId") Long eventId) {

		List<EventManagementParticipantslistDTO> participantsList = companyEventDetailsService.getParticipantsList(eventId);
		if (participantsList != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(participantsList).error(false).message("Data Fetched").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("Null").build(),
					HttpStatus.NOT_FOUND);

		}

	}

	// Controller for fetching the Branch name..
	@GetMapping("/branchnames/{companyId}")
	ResponseEntity<SuccessResponse> branchNames(@PathVariable Long companyId) {

		List<BranchNameFetchDTO> branchNames = companyEventDetailsService.fetchBranchNames(companyId);
		if (branchNames != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(branchNames).error(false).message("Data Fetched").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(null).error(true).message("Null").build(),
					HttpStatus.NOT_FOUND);
		}
	}

}
