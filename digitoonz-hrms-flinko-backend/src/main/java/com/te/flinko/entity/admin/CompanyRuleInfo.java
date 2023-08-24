package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeePerformanceRule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_rule_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.USE_DEFAULTS)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "ruleId")
public class CompanyRuleInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cri_rule_id", unique = true, nullable = false, precision = 19)
	private Long ruleId;
	@Column(name = "cri_fiscal_year", length = 4)
	private String fiscalYear;
	@Column(name = "cri_date_formate", length = 20)
	private String dateFormate;
	@Column(name = "cri_probation_period", precision = 2, scale = 2)
	private BigDecimal probationPeriod;
	@Column(name = "cri_no_of_cl_per_month", precision = 2, scale = 2)
	private BigDecimal noOfClPerMonth;
	@Column(name = "cri_cl_activation_month", length = 25)
	private String clActivationMonth;
	@Column(name = "cri_reserved_cl_activation", length = 25)
	private String reservedClActivation;
	@Column(name = "cri_time_sheet_approval", length = 25)
	private String timeSheetApproval;
	@Column(name = "cri_apprisal_cycle", length = 25)
	private String apprisalCycle;
	
	@Column(name = "cri_is_casual_leave_earned_leave", precision = 3)
	private Boolean isCasualLeaveEarnedLeave;
	
	@Column(name = "cri_is_chat_box_enabled", precision = 3)
	private Boolean isChatBoxEnabled;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyRuleInfo")
	private List<CompanyLeaveInfo> companyLeaveInfoList;
	@ManyToOne
	@JoinColumn(name = "cri_company_id")
	private CompanyInfo companyInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyRuleInfo")
	private List<CompanyShiftInfo> companyShiftInfoList;
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "companyRuleInfo")
	private EmployeePerformanceRule employeePerformanceRule;
	

}
