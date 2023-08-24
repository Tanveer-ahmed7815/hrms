package com.te.flinko.dto.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.PASSWORD_CAN_NOT_BE_NULL_OR_BLANK;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeLoginDto implements Serializable {

	private Long loginId;
	
	@NotBlank(message = EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK)
	private String employeeId;

	@NotBlank(message = PASSWORD_CAN_NOT_BE_NULL_OR_BLANK)
	private String password;
	
	private String oldPassword;
	
	private String currentPassword;
	
	@Convert(converter = ListToStringConverter.class)
	private List<String> roles;
	
	private EmployeePersonalInfoDto employeePersonalInfo;

	@Override
	public String toString() {
		return "EmployeeLoginDto [loginId=" + loginId + ", employeeId=" + employeeId + ", password=" + password
				+ ", oldPassword=" + oldPassword + ", currentPassword=" + currentPassword + ", roles=" + roles + "]";
	}
	
}
