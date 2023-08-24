package com.te.flinko.dto.admin;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Brunda
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyShiftInfoDTO {

	private Long shiftId;
	private String shiftName; // Company Shift Info -- CompanyRuleInfo & EmployeeOfficialInfoList
	@JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
	private LocalTime loginTime;
	@JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
	private LocalTime logoutTime;
}
