package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeNoticePeriodDTO {
	
	private Long resignationId;
	private Double noticePeriodDuration;
	private LocalDate noticePeriodStartDate;
	private LocalDate noticePeriodEndDate;

}
