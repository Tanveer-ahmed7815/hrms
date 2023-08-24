package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.te.flinko.entity.admin.CompanyInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_revise_salary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reviseSalaryId")
public class EmployeeReviseSalary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ers_advance_salary_id", unique = true, nullable = false, precision = 19)
	private Long reviseSalaryId;
	@Column(name = "ers_apprisal_date")
	private LocalDate apprisalDate;
	@Column(name = "ers_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "ers_reason", length = 999)
	private String reason;
	@Column(name = "ers_revised_date")
	private LocalDate revisedDate;
	@ManyToOne
	@JoinColumn(name = "ers_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "ers_company_id")
	private CompanyInfo companyInfo;

}
