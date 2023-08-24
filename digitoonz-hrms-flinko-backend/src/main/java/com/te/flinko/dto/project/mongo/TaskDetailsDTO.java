package com.te.flinko.dto.project.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDetailsDTO {
	private String id;
	private String mileStoneId;
	private String mileStoneName;
	private Long subMilestoneId;
	private String subMilestoneName;
	private String taskName;
	private String taskDescription;
	private Long createdBy;
	private String createdByName;
	private LocalDateTime createdDate;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDate assignedDate;
	private String status;
	private LocalDate completedDate;
	private Long companyId;
	private Long projectId;
	private String assignedEmployeeName;
	

}
