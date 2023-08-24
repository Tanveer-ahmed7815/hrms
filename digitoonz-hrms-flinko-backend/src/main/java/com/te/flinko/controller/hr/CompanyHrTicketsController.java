package com.te.flinko.controller.hr;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.dto.hr.mongo.CompanyHrTicketsDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.CompanyHrTicketsService;

@RestController
@RequestMapping("api/v1/hrTickets")
@CrossOrigin(origins = "*")
public class CompanyHrTicketsController {
	@Autowired
	CompanyHrTicketsService companyHrTicketsService;
	
	
	//API For Getting All the tickets raised in a Company
	
	@GetMapping("/getticketinfolist/{companyId}")
	ResponseEntity<SuccessResponse> getHrTicketInfoList(@PathVariable Long companyId) {

		List<CompanyHrTicketsDTO> hrTicketsInfoList = companyHrTicketsService.hrTicketsInfoList(companyId);
		if (hrTicketsInfoList != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(hrTicketsInfoList).error(false).message("Ticket List Fetched").build(),
					HttpStatus.OK);
		}else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(hrTicketsInfoList).error(true).message("Ticket List Not Fetched").build(),
					HttpStatus.OK);
		}
	}
	
	//API For Getting the details of the tickets based on ticketId
	
	
	@GetMapping("/getticketinfo/{hrTicketObjectId}")
	ResponseEntity<SuccessResponse> getHrTicketInfo(@PathVariable String hrTicketObjectId) {

		CompanyHrTicketsDTO hrTicketsInfo = companyHrTicketsService.hrTicketsDTOInfo(hrTicketObjectId);
		if (hrTicketsInfo != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(hrTicketsInfo).error(false).message("Ticket Info Fetched").build(),
					HttpStatus.OK);
		}else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(null).error(true).message("Ticket Info Not Fetched").build(),
					HttpStatus.OK);
		}
	}
	
	
	//API for Actions in tickets
	
	
	@PutMapping("/updateHistory/{status}/{param1}/{param2}")
	public ResponseEntity<?> updateTicketHistory(@PathVariable String status,@PathVariable("param1") Long employeeInfoId,@PathVariable("param2") String hrTicketObjectId){
		
		CompanyHrTicketsDTO hrTicketDTO = companyHrTicketsService.updateTicketHistory(status,employeeInfoId,hrTicketObjectId);
		
		if(hrTicketDTO != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(hrTicketDTO).error(false)
					.message("data updated Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(hrTicketDTO).error(false)
					.message("data not available").build(), HttpStatus.OK);	
	}
}
