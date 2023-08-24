package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeEducationDetailsDTO {
	
	private Long educationId;
	private String degree;
	private String course;
	private String institudeName;
	private String averageGrade;
	private LocalDate courseStartDate;
	private LocalDate courseEndDate;
	private String yearOfPassing;
	private String description;

}
