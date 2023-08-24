package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.dto.hr.EmployeeInformationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeAdvanceSalaryDTO  { 
	
	private Long advanceSalaryId;
	
	private BigDecimal amount;
	
	private BigDecimal emi;
	
	private String reason;
	
	private String status;
	
	private LocalDateTime requestedOn;
	
	private String rejectedBy;
	
	private String rejectedReason;
		
	private Map<String, String> approvedBy;
	
}
