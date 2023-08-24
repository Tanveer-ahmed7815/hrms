package com.te.flinko.controller.helpandsupport.mongo;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.helpandsupport.mongo.CompanyadminDeptTicketsDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.helpandsupport.mongo.CompanyAdminDeptTicketsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin-department")
public class CompanyAdminDeptTicketsController extends BaseConfigController {

	@Autowired
	CompanyAdminDeptTicketsService companyAdminDeptTicketsService;

	@GetMapping("/admin-department-tickets")
	public ResponseEntity<SuccessResponse> allTickets() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(AdminConstants.ALL_TICKET_FETCH_SUCCESS)
						.data(companyAdminDeptTicketsService.getAllTickets(getCompanyId())).build());
	}

	@GetMapping("/admin-department-tickets/status/{status}")
	public ResponseEntity<SuccessResponse> allTicketsAccordingStatus(@PathVariable(value = "status") String status) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(AdminConstants.ALL_TICKET_FETCH_SUCCESS_ACCORDING_STATUS)
				.data(companyAdminDeptTicketsService.getAllTicketsAccordingStatus(getCompanyId(), status)).build());
	}

	@GetMapping(path = { "/admin-department-tickets/{TicketObjectId}",
			"/admin-department-tickets/{TicketObjectId}/{status}" })
	public ResponseEntity<SuccessResponse> getTicketById(@PathVariable(value = "TicketObjectId") String objectTicketId,
			@PathVariable(required = false) String status) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(AdminConstants.ALL_TICKET_FETCH_SUCCESS)
						.data(companyAdminDeptTicketsService.getTicketById(objectTicketId,status)).build());
	}

	@PutMapping("/admin-department-tickets")
	public ResponseEntity<SuccessResponse> updateTicket(
			@RequestBody CompanyadminDeptTicketsDto companyadminDeptTicketsDto) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(companyAdminDeptTicketsService.updateTickets(getEmployeeInfoId(), companyadminDeptTicketsDto)
						.booleanValue() ? "Ticket "+companyadminDeptTicketsDto.getStatus()+" Successfully"
								: "Fail To "+companyadminDeptTicketsDto.getStatus()+" Ticket")
				.build());
	}

	@GetMapping("/admin-department-tickets/category/{category}")
	public ResponseEntity<SuccessResponse> getTicketsAccordingcatagory(
			@PathVariable(value = "category") String category) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(AdminConstants.ALL_TICKET_FETCH_SUCCESS)
						.data(companyAdminDeptTicketsService.getAllTicketsAccordingCategory(getCompanyId(), category))
						.build());

	}
}
