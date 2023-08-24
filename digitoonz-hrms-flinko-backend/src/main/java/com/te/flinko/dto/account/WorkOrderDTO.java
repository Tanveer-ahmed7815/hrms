package com.te.flinko.dto.account;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class WorkOrderDTO {

	private Long workOrderId;
	private String workOrderOwner;
	private String departmentName;
	private Long requestTo;
	private Long companyClientInfoId;
	private String workTitle;
	private String priority;
	private BigDecimal estimatedCost;
	private Boolean isCostEstimated;
	private String description;
	private Long noOfEmployee;
	private List<WorkOrderResourcesDTO> workOrderResources;
	
}	
