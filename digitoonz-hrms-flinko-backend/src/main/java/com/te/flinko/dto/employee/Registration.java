package com.te.flinko.dto.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.COMPANY_NAME_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.DESIGNATION_NAME_NAME_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.FIRST_NAME_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.LAST_NAME_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.OFFICIAL_EMAIL_ID_CAN_NOT_BE_NULL_OR_BLANK;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class Registration implements Serializable{

	@NotBlank(message = COMPANY_NAME_CAN_NOT_BE_NULL_OR_BLANK)
	private String  companyName;
	
	@NotBlank(message = EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK)
	private String employeeId;
	
	@NotBlank(message = FIRST_NAME_CAN_NOT_BE_NULL_OR_BLANK)
	private String firstName;
	
	@NotBlank(message = LAST_NAME_CAN_NOT_BE_NULL_OR_BLANK)
	private String lastName;
	
	@NotBlank(message = OFFICIAL_EMAIL_ID_CAN_NOT_BE_NULL_OR_BLANK)
	private String officialEmailId;
	
//	@NotBlank(message = MOBILE_NUMBER_CAN_NOT_BE_NULL_OR_BLANK)
	private Long mobileNumber;
	
	@NotBlank(message = DESIGNATION_NAME_NAME_CAN_NOT_BE_NULL_OR_BLANK)
	private String designationName;
	
	private String password;
	private String confirmPassword;
	private Long otp;
	private Boolean isActive;
}
