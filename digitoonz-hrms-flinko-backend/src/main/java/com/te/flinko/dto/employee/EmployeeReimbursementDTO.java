package com.te.flinko.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeReimbursementDTO {
	
	private Long reimbursementId;
	
	private Long expenseCategoryId;

	private String expenseCategoryName;
	
	private LocalDate expenseDate;
	
	private String description;
	
	private BigDecimal amount;
	
	private String attachmentUrl;
	
	private String status;
	
	private Map<String, String> approvedBy;
	
	private String rejectedBy;
	
	private String rejectedReason;
	
}
