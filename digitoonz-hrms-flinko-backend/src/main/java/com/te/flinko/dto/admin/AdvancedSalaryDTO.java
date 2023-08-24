package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class AdvancedSalaryDTO implements Serializable {
	private String employeeId;
	private Long employeeInfoId;
	private Long advanceSalaryId;
	private String employeeName;
	private String branch;
	private String department;
	private String designation;
	private LocalDateTime requestedOn;
	private String reimbursementType;
	private BigDecimal amount;
	private String status;
	private String reason;
	private BigDecimal emi;
	private Boolean isActionRequired;
	private String pendingAt;
	private String rejectedBy;
	private String rejectionReason;
	private String reportingManager;
}
