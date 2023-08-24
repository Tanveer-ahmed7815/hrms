package com.te.flinko.dto.project.mongo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class AssignTaskDTO {
	private String mileStoneId;
	private String mileStoneName;
	private Long subMilestoneId;
	private String subMilestoneName;
	private String taskName;
	private String taskDescription;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String status;
	private Long companyId;
	private Long projectId;
	private String assignedEmployee;
	
}
