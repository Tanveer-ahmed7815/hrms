package com.te.flinko.entity.account;

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
import com.te.flinko.entity.admin.CompanyInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_cost_evaluation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "costEvaluationId")
public class CompanyCostEvaluation extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cce_cost_evaluation_id", unique = true, nullable = false, precision = 19)
	private Long costEvaluationId;
	@Column(name = "cce_category", length = 50)
	private String category;
	@Column(name = "cce_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "cce_duration", length = 10)
	private String duration;
	@Column(name = "cce_additional", precision = 10, scale = 2)
	private BigDecimal additional;
	@Column(name = "cce_deduction", precision = 10, scale = 2)
	private BigDecimal deduction;
	@Column(name = "cce_reason", length = 255)
	private String reason;
	@ManyToOne
	@JoinColumn(name = "cce_company_id")
	private CompanyInfo companyInfo;

}
