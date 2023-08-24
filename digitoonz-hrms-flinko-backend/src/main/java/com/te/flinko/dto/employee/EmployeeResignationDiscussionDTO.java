package com.te.flinko.dto.employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.te.flinko.dto.hr.NotificationExitInterviewDropdownDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResignationDiscussionDTO {

	private String discussionType;
	
	private String discussionDetails;
	
	private LocalDate discussionDate;
	
	private List<NotificationExitInterviewDropdownDTO> organizerDetails;
	
	private LocalTime startTime;
	
	private Integer duration;
	
	private String status;
}
