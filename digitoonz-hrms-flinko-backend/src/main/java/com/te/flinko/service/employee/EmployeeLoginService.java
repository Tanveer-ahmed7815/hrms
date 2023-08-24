/**
 * 
 */
package com.te.flinko.service.employee;

import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.EmployeeLoginDto;
import com.te.flinko.dto.employee.EmployeeLoginResponseDto;
import com.te.flinko.dto.employee.NewConfirmPasswordDto;
import com.te.flinko.dto.employee.ResetPasswordDto;
import com.te.flinko.dto.employee.VerifyOTPDto;

/**
 * @author Sahid
 *
 */
public interface EmployeeLoginService {
	
	public EmployeeLoginResponseDto login(EmployeeLoginDto employeeLoginDto);
	
	public String resendOTP(EmployeeIdDto employeeIdDto);
	
	public String forgotPassword(EmployeeIdDto employeeIdDto);
	
	public String validateOTP(VerifyOTPDto verifyOTPDto );
	
	public String resetPassword(NewConfirmPasswordDto newConfirmPasswordDto);
	
	public String updatePassword(ResetPasswordDto resetPasswordDto ,String logedInUser);
	
}
