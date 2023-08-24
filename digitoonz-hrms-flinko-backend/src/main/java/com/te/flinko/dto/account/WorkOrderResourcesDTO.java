package com.te.flinko.dto.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class WorkOrderResourcesDTO {
	
	private Long resourceId;
	private String resourceType;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal amount;
	private Integer quantity;
	
}
