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
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_bonus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bonusId")
public class EmployeeBonus extends Audit implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eb_bonus_id", unique = true, nullable = false, precision = 19)
	private Long bonusId;
	@Column(name = "eb_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "eb_effective_date")
	private LocalDate effectiveDate;
	@Column(name = "eb_reason", length = 999)
	private String reason;
	@ManyToOne
	@JoinColumn(name = "eb_annual_salary_id")
	private EmployeeAnnualSalary employeeAnnualSalary;

}
