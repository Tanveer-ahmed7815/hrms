package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyRuleInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_performance_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "performanceId")
public class EmployeePerformanceRule extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "epr_performance_id", unique = true, nullable = false, precision = 19)
	private Long performanceId;
	@Column(name = "epr_no_of_leave_in_month", precision = 2, scale = 2)
	private BigDecimal noOfLeaveInMonth;
	@Column(name = "epr_unapproved_leave", precision = 2, scale = 2)
	private BigDecimal unapprovedLeave;
	@Column(name = "epr_login_before_time", precision = 2, scale = 2)
	private BigDecimal loginBeforeTime;
	@Column(name = "epr_late_login", precision = 2, scale = 2)
	private BigDecimal lateLogin;
	@Column(name = "epr_target_achived", precision = 2, scale = 2)
	private BigDecimal targetAchived;
	@Column(name = "epr_achieved_more_then_50_per", precision = 2, scale = 2)
	private BigDecimal achievedMoreThen50Per;
	@Column(name = "epr_achieved_more_then_100_per", precision = 2, scale = 2)
	private BigDecimal achievedMoreThen100Per;
	@Column(name = "epr_indisplinary_star", precision = 2, scale = 2)
	private BigDecimal indisplinaryStar;
	@Column(name = "epr_late_login_duration", precision = 10)
	private Integer lateLoginDuration;	
	@OneToOne
	@JoinColumn(name = "epr_rule_id")
	private CompanyRuleInfo companyRuleInfo;
}
