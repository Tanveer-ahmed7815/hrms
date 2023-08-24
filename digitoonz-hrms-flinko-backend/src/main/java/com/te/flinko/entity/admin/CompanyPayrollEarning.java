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
@Table(name = "fa_company_payroll_earning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "earningId")
public class CompanyPayrollEarning extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpe_earning_id", unique = true, nullable = false, precision = 19)
	private Long earningId;
	@Column(name = "cpe_salary_component", length = 50)
	private String salaryComponent;
	@Column(name = "cpe_type", length = 25)
	private String type;
	@Column(name = "cpe_value", precision = 10, scale = 2)
	private BigDecimal value;
	@Column(name = "cpe_is_taxable", precision = 3)
	private Boolean isTaxable;
	@ManyToOne
	@JoinColumn(name = "cpe_payroll_id")
	private CompanyPayrollInfo companyPayrollInfo;

}
