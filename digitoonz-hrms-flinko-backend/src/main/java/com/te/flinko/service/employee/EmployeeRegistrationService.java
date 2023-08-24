package com.te.flinko.service.employee;

import java.util.List;

import com.te.flinko.dto.admin.CompanyDesignationNamesDto;
import com.te.flinko.dto.admin.CompanyInfoNamesDto;
import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.NewConfirmPasswordDto;
import com.te.flinko.dto.employee.Registration;
import com.te.flinko.dto.employee.VerifyOTPDto;

public interface EmployeeRegistrationService {
	
	List<CompanyInfoNamesDto> getAllCompany();
	
	List<CompanyDesignationNamesDto> getAllDesignation(Long companyId);

	String varifyEmployee(Registration employeeRegistrationDto,Long companyId);
	 
	String validateOTP(VerifyOTPDto verifyOTPDto);
	
	String registration(NewConfirmPasswordDto newConfirmPasswordDto);
	
	String resendOTP(EmployeeIdDto employeeIdDto);
}
