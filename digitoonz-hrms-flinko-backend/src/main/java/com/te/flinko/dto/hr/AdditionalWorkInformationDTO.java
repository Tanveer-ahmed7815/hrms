package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AdditionalWorkInformationDTO {

	private Long employeeId;
	private Long shiftId;
	private String shiftName;
	private LocalDate probationEndDate;
	private LocalDate probationStartDate;

}
