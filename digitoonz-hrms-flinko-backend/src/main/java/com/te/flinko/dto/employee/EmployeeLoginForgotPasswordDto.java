package com.te.flinko.dto.employee;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeLoginForgotPasswordDto implements Serializable {

	private String employeeId;

	private String oldPassword;
	
	private String newPassword;
	
	private String confirmPassword;
	
	private Long otp;
}
