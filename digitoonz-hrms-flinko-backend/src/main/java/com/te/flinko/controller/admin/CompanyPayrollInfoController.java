package com.te.flinko.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyPayrollInfoDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyPayrollInfoService;

//@author Rakesh Kumar Nayak 
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin-company")
public class CompanyPayrollInfoController extends BaseConfigController {

	@Autowired
	CompanyPayrollInfoService companyPayrollInfoService;

	@PostMapping("/payroll")
	public ResponseEntity<SuccessResponse> createPayrollInfo(
			@Valid @RequestBody CompanyPayrollInfoDTO companyPayrollInfoDto) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyPayrollInfoService.createPayrollInfo(companyPayrollInfoDto, getCompanyId())
								.booleanValue() ? AdminConstants.PAYROLL_INFO_CREATED_SUCCESS
										: AdminConstants.PAYROLL_INFO_CREATED_FAILURE)
						.build());
	}

	@GetMapping("/payroll")
	public ResponseEntity<SuccessResponse> getAllPayrollInfo() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(AdminConstants.ALL_PAYROLL_INFO_FETCH_SUCCESS)
						.data(companyPayrollInfoService.getAllCompanyPayrollInfo(getCompanyId())).build());
	}

	@PutMapping("/payroll")
	public ResponseEntity<SuccessResponse> updatepayRoll(
			@Valid @RequestBody CompanyPayrollInfoDTO companyPayrollInfoDto) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(companyPayrollInfoService.updateCompanyPayrollInfo(companyPayrollInfoDto, getCompanyId())
						.booleanValue() ? AdminConstants.PAYROLL_INFO_UPDATE_SUCCESS
								: AdminConstants.PAYROLL_INFO_UPDATE_FAILURE)
				.build());
	}
}
