package com.te.flinko.dto.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.COMPANY_NAME_CAN_NOT_BE_NULL_OR_BLANK;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.dto.admin.CompanyDesignationInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyRegistrationDto implements Serializable {

	@NotBlank(message = COMPANY_NAME_CAN_NOT_BE_NULL_OR_BLANK)
	private String companyName;
	
	private List<EmployeePersonalInfoDto> employeePersonalInfoList;

	private List<CompanyDesignationInfoDto> companyDesignationInfoList;
}
