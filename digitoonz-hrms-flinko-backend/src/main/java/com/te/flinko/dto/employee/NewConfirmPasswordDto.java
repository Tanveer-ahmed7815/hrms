package com.te.flinko.dto.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.CONFIRM_PASSWORD_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.NEW_PASSWORD_CAN_NOT_BE_NULL_OR_BLANK;

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
public class NewConfirmPasswordDto implements Serializable {
	@NotBlank(message = EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK)
	private String employeeId;
	
	private String emailId;
	
	@NotBlank(message = NEW_PASSWORD_CAN_NOT_BE_NULL_OR_BLANK)
	private String newPassword;
	
	@NotBlank(message = CONFIRM_PASSWORD_CAN_NOT_BE_NULL_OR_BLANK)
	private String confirmPassword;
	
	private Long companyId;
}
