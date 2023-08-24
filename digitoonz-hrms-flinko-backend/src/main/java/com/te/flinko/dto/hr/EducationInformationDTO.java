package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EducationInformationDTO {

	private Long educationId;
	private String degree;
	private String course;
	private String institudeName;
	private String averageGrade;
	private String description;
	private LocalDate courseStartDate;
	private LocalDate courseEndDate;
	private String yearOfPassing;
	
}
