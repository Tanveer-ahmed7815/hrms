package com.te.flinko.entity.admin;

import java.io.Serializable;
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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeeAnnualSalary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_payroll_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "payrollId")
public class CompanyPayrollInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "epi_payroll_id", unique = true, nullable = false, precision = 19)
	private Long payrollId;
	@Column(name = "epi_payroll_name", nullable = false, length = 50)
	private String payrollName;
	@Column(name = "epi_salary_approval_date")
//	@JsonFormat(pattern = "MM-dd-yyyy"  , timezone = "Asia/kolkata")
	private Integer salaryApprovalDate;
	@Column(name = "epi_payment_date")
//	@JsonFormat(pattern = "MM-dd-yyyy" , timezone = "Asia/kolkata")
	private Integer paymentDate;
	@Column(name = "epi_pay_slip_generation_date")
//	@JsonFormat(pattern = "MM-dd-yyyy"  , timezone = "Asia/kolkata")
	private Integer paySlipGenerationDate;
	@Column(name = "epi_is_pf_enabled", precision = 3)
	private Boolean isPfEnabled;
	@Column(name = "epi_is_pt_enabled", precision = 3)
	private Boolean isPtEnabled;
	@Column(name = "epi_is_csi_enabled", precision = 3)
	private Boolean isCsiEnabled;
	@Column(name = "epi_is_advance_salary_enabled", precision = 3)
	private Boolean isAdvanceSalaryEnabled;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPayrollInfo")
	private List<CompanyPayrollDeduction> companyPayrollDeductionList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPayrollInfo")
	private List<CompanyPayrollEarning> companyPayrollEarningList;
	
	@ManyToOne
	@JoinColumn(name = "epi_company_id")
	private CompanyInfo companyInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPayrollInfo")
	private List<EmployeeAnnualSalary> employeeAnnualSalaryList;
}

