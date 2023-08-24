package com.te.flinko.dto.reportingmanager;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprisalMeetingDTO {
	
private String employeeId;
	
	private Long employeeInfoId;
	
	private String fullName;
	
	private String OfficialEmailId;
	
	private Long mobileNumber;
	
	private String department;
	
	private String designation;
	
	private LocalDate dueDate;
	
	private List<ProjectDetailsDTO> project;

}
