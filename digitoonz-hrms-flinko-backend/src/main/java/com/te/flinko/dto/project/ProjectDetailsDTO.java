package com.te.flinko.dto.project;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProjectDetailsDTO {
	
	private Long projectId;
	private String projectName;
	private String projectDescription;
	private Long clientId;
	private String clientName;
	private Long projectManagerId;
	private String projectManager;
	private Long reportingManagerId;
	private String reportingManager;
	private Long holidays;
	private Integer workingDays;
	private Double amountReceived;
	private Double amountPending;
	private String status;
	private LocalDate startDate;
	
}
