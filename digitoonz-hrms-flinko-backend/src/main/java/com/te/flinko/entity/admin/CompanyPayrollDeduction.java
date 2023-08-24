package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_payroll_deduction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "deductionId")
public class CompanyPayrollDeduction extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpd_deduction_id", unique = true, nullable = false, precision = 19)
	private Long deductionId;
	@Column(name = "cpd_title", length = 50)
	private String title;
	@Column(name = "cpd_type", length = 25)
	private String type;
	@Column(name = "cpd_value", precision = 10, scale = 2)
	private BigDecimal value;
	@ManyToOne
	@JoinColumn(name = "cpd_payroll_id")
	private CompanyPayrollInfo companyPayrollInfo;

}
