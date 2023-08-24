package com.te.flinko.dto.reportingmanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data

public class EmployeeDetailsDTO {
	private String employeeId;
	private String fullName;
	private String officialEmailId;
	private Long mobileNumber;
	private String department;
	private String designation;
	private List<String> projectList;
	
	private String meetingType;
	private String meetingDetails;
	private LocalDate meetingDate;
	private LocalTime startTime;
	private Integer duration;
	private Boolean isFeedbackGiven;
}
