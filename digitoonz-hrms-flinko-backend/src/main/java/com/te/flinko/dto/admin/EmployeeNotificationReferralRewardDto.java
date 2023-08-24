package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeNotificationReferralRewardDto implements Serializable{
	
	@NotBlank(message = "Reward Amount Can Not Be Null or Blank")
	private BigDecimal rewardAmount;
	
	@NotBlank(message = "Include In Salary Can Not Be Null or Blank")
	private Boolean includeInSalary;
	
	@NotBlank(message = "Reference Id Can Not Be Null or Blank")
	private Long referenceId;
	
	private String employeeName;
	
	private String employeeId;
	
	private String designation;
	
	private String department;
	
	private String referredTo;

}
