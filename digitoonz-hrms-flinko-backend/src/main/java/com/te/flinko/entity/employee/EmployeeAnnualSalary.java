package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyPayrollInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_annual_salary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "annualSalaryId")
public class EmployeeAnnualSalary extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eans_annual_salary_id", unique = true, nullable = false, precision = 19)
	private Long annualSalaryId;
	@Column(name = "eans_effective_date")
	private LocalDate effectiveDate;
	@Column(name = "eans_annual_salary", precision = 10, scale = 2)
	private BigDecimal annualSalary;
	@Column(name = "eans_reason", length = 999)
	private String reason;
	@Column(name = "eans_frequency", length = 25)
	private String frequency;
	@Column(name = "eans_pay_method", length = 50)
	private String payMethod;
	@Column(name = "eans_is_pf_enabled", precision = 3)
	private Boolean isPfEnabled;
	@Column(name = "eans_is_pt_enabled", precision = 3)
	private Boolean isPtEnabled;
	@Column(name = "eans_is_esi_enabled", precision = 3)
	private Boolean isEsiEnabled;
	@Column(name = "eans_is_banus_enabled", precision = 3)
	private Boolean isBonusEnabled;
	@Column(name = "eans_is_variable_pay_enabled", precision = 3)
	private Boolean isPayEnabled;
	@Column(name = "eans_is_salary_stopped", precision = 3)
	private Boolean isSalaryStopped;
	@Column(name = "eans_salary_stopped_from")
	private LocalDate salaryStoppedFrom;
	@Column(name = "eans_state", length = 50)
	private String state;
	@Column(name = "eans_uan", length = 15)
	private String uan;
	@Column(name = "eans_pf_number", length = 25)
	private String pfNumber;
	@Column(name = "eans_esi_number", length = 20)
	private String esiNumber;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "eans_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeAnnualSalary", orphanRemoval=true)
	private List<EmployeeBonus> employeeBonusList;
	
	@OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
	@JoinColumn(name = "eans_employee_variable_pay_id")
	private EmployeeVariablePay employeeVariablePay;

	@ManyToOne
	@JoinColumn(name = "eans_payroll_id")
	private CompanyPayrollInfo companyPayrollInfo;

}
