package com.te.flinko.controller.admindept;

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
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admindept.CompanySoftwareDetailsDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admindept.CompanySoftwareDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Manish Kumar
 * 
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/company")
public class CompanySoftwareDetailsController extends BaseConfigController {
	@Autowired
	private CompanySoftwareDetailsService companySoftwareDetailsService;

	/*
	 * api for adding new software
	 */
	@PostMapping("/software")
	public ResponseEntity<SuccessResponse> createSoftware(
			@RequestBody(required = false) CompanySoftwareDetailsDto companySoftwareDetailsDto
			) {
		log.info("controller method of CompanySoftwareDetailsController class, company id is : {}" + getCompanyId());

	
		return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(companySoftwareDetailsService.createSoftware(companySoftwareDetailsDto, getCompanyId())
						.booleanValue() ? AdminConstants.NEW_SOFTWARE_ADDED
								: AdminConstants.FAIL_TO_ADD_NEW_SOFTWARE)
				.build());

	}

	/*
	 * api for find all software
	 */
	@GetMapping("/software")
	public ResponseEntity<SuccessResponse> getAllSoftware() {
		log.info("controller method of CompanySoftwareDetailsController class, company id is : {}" + getCompanyId());

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(AdminConstants.LIST_OF_SOFTWARE_ARE_HERE)
						.data(companySoftwareDetailsService.getAllSoftware(getCompanyId())).build());

	}

	/*
	 * api for updating software details
	 * 
	 */
	@PutMapping("/software/{softwareId}")
	public ResponseEntity<SuccessResponse> updateSoftware(
			@RequestBody(required = false) CompanySoftwareDetailsDto companySoftwareDetailsDto,
			@PathVariable Long softwareId) {
		log.info("controller method of CompanySoftwareDetailsController class, company id is : {}, software id{}"+getCompanyId(),  softwareId);


		
	return	 ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.builder().error(Boolean.FALSE)
					.message(companySoftwareDetailsService.updatesoftware(companySoftwareDetailsDto, getCompanyId(), softwareId)
							.booleanValue() ? AdminConstants.SOFTWARE_DETAILS_UPDATED_SUCCESSFULLY
									: AdminConstants.SOFTWARE_DETAILS_UPDATE_FAILURE)
					.build());

	}

}
