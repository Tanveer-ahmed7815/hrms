package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;
@Data
public class AddEmployeeTerminationDetailsDTO {
	private String reason;
	private LocalDate terminationDate;
	private String terminationType;
	private Long companyId;
	private Long employeeInfoId;

}
