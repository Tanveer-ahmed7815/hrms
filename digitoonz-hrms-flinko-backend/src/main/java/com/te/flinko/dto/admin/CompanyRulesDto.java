package com.te.flinko.dto.admin;


import java.math.BigDecimal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Brunda
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyRulesDto {

	private Long ruleId;
	private String fiscalYear; // CompanyRuleInfo -- CompanyInfo & CompanyShiftInfoList
	private String dateFormate;
	private BigDecimal probationPeriod;
	private BigDecimal noOfClPerMonth;
	private String clActivationMonth;
	private String reservedClActivation;
	private String apprisalCycle;
	private String timeSheetApproval;
	private Boolean isChatBoxEnabled;
	private Boolean isCasualLeaveEarnedLeave;
	private BigDecimal noOfLeaveInMonth; // EmployeePerformanceRule -- CompanyRuleInfo
	private BigDecimal loginBeforeTime;
	private BigDecimal targetAchived;
	private BigDecimal achievedMoreThen50Per;
	private BigDecimal achievedMoreThen100Per;
	private BigDecimal unapprovedLeave;
	private BigDecimal lateLogin;
	private BigDecimal indisplinaryStar;
	private Integer lateLoginDuration;
	private Boolean isSubmited;
	private List<CompanyShiftInfoDTO> companyShiftInfoList;

//	private String leaveTitle;   //CompanyLeaveInfo --CompanyRuleInfo  && CompanyInfo
	private List<CompanyLeaveInfoDto> companyLeaveInfoList;
}
