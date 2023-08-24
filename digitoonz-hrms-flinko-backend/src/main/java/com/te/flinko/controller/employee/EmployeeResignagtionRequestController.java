package com.te.flinko.controller.employee;

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
import com.te.flinko.dto.employee.CompanyEmployeeResignationDetailsDTO;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.employee.EmployeeResignationRequestService;

@RestController
@RequestMapping("/api/v1/resignation-request")
@CrossOrigin(origins = "*")
public class EmployeeResignagtionRequestController extends BaseConfigController{

	@Autowired
	EmployeeResignationRequestService service;

	@PostMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> saveEmployeeResignatioRequest(
			@RequestBody CompanyEmployeeResignationDetailsDTO resignationDetailsDTO,@PathVariable Long employeeInfoId) {

		CompanyEmployeeResignationDetailsDTO resignationDetailsDTO2 = service.saveEmployeeResignation(resignationDetailsDTO,employeeInfoId,getCompanyId());

		if (resignationDetailsDTO2 != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(resignationDetailsDTO2)
					.message("Details Saved Successfully").error(false).build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(resignationDetailsDTO2)
					.message("Details Not Saved").error(true).build(), HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getEmployeeResignation(@PathVariable Long employeeInfoId) {

		CompanyEmployeeResignationDetailsDTO resignationEmployeeDetails = service
				.getEmployeeResignation(employeeInfoId,getCompanyId());
		if (resignationEmployeeDetails != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(resignationEmployeeDetails)
					.message("Details Fetched Successfully").error(false).build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(resignationEmployeeDetails)
					.message("Data Not Fetched").error(false).build(), HttpStatus.BAD_REQUEST);
	}

}
