package com.te.flinko.dto.project;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectEstimationDTO {
	
	private Long projectId;
	
	private Long estimationId; 
	
	private String startDate;

	private String endDate;

	private Integer noOfEmp;

	private BigDecimal totalAmountEstimated;

	private BigDecimal totalAmountToBeReceived;

	private String fileURL;
	
}
