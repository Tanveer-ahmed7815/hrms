package com.te.flinko.controller.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.ALL_COMPANY_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.ALL_DESIGNATION_FETCHED_SUCCESSFULLY_FOR_THE_PARTICULAR_COMPANY;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.NewConfirmPasswordDto;
import com.te.flinko.dto.employee.Registration;
import com.te.flinko.dto.employee.VerifyOTPDto;
import com.te.flinko.response.employee.EmployeeLoginResponse;
import com.te.flinko.service.employee.EmployeeRegistrationService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class EmployeeRegistrationController {

	private final EmployeeRegistrationService employeeRegistrationService;

	@GetMapping("companies")
	public ResponseEntity<EmployeeLoginResponse> getAllCompanyName() {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(ALL_COMPANY_FETCHED_SUCCESSFULLY)
				.data(employeeRegistrationService.getAllCompany()).build());
	}

	@GetMapping("designations/{companyId}")
	public ResponseEntity<EmployeeLoginResponse> getAllDesignation(@PathVariable Long companyId) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(ALL_DESIGNATION_FETCHED_SUCCESSFULLY_FOR_THE_PARTICULAR_COMPANY)
				.data(employeeRegistrationService.getAllDesignation(companyId)).build());
	}

	@PostMapping("employee/verify")
	public ResponseEntity<EmployeeLoginResponse> verify(@Valid @RequestBody VerifyOTPDto verifyOTPDto) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeRegistrationService.validateOTP(verifyOTPDto)).build());
	}

	@PostMapping("employee/registration-otp/{companyId}")
	public ResponseEntity<EmployeeLoginResponse> sendOTP(@Valid @RequestBody Registration employeeRegistrationDto,@PathVariable Long companyId) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeRegistrationService.varifyEmployee(employeeRegistrationDto, companyId)).build());
	}

	@PostMapping("employee/registration")
	public ResponseEntity<EmployeeLoginResponse> registration(@Valid @RequestBody NewConfirmPasswordDto newConfirmPasswordDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeRegistrationService.registration(newConfirmPasswordDto)).build());
	}
	
	@PostMapping("employee/registration-resend-otp")
	public ResponseEntity<EmployeeLoginResponse> resendOTP(@Valid @RequestBody EmployeeIdDto employeeIdDto) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeRegistrationService.resendOTP(employeeIdDto)).build());
	}

}
