package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeReimbursementInfoDTO implements Serializable{

	private String employeeId;
	private Long employeeInfoId;
	private Long employeeReimbursementId;
	private String employeeName;
	private String branch;
	private String department;
	private String designation;
	private LocalDate expenseDate;
	private String reimbursementType;
	private BigDecimal amount;
	private String status;
	private String description;
	private String link;
	private Boolean isActionRequired;
	private String pendingAt;
	private String rejectedBy;
	private String reason;
	private String reportingManager;
}
