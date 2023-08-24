package com.te.flinko.dto.reportingmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTaskDetailsDTO {

	private String id;

	private Long taskId;
	
	private String mileStoneId;
	
	private Long subMilestoneId;
	
	private Long createdBy;
	
	private String createdByName;
	
	private LocalDateTime createdDate;

	private String taskName;

	private String taskDescription;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private String assignedEmployee;

	private LocalDate assignedDate;

	private String comment;

	private String status;

	private Long projectId;

}
