package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_salary_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeSalaryId")
public class EmployeeSalaryDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "esd_employee_salary_id", unique = true, nullable = false, precision = 19)
	private Long employeeSalaryId;
	@Convert(converter = MapToStringConverter.class)
	@Column(name = "esd_earning", length = 255)
	private Map<String, String> earning;
	@Convert(converter = MapToStringConverter.class)
	@Column(name = "esd_deduction", length = 255)
	private Map<String, String> deduction;
	@Convert(converter = MapToStringConverter.class)
	@Column(name = "esd_additional", length = 255)
	private Map<String, String> additional;
	@Column(name = "esd_net_pay", precision = 10, scale = 2)
	private BigDecimal netPay;
	@Column(name = "esd_month")
	private Integer month;
	@Column(name = "esd_year")
	private Integer year;
	@Column(name = "esd_is_finalized", precision = 3)
	private Boolean isFinalized;
	@Column(name = "esd_is_paid", precision = 3)
	private Boolean isPaid;
	@Column(name = "esd_is_payslip_generated", precision = 3)
	private Boolean isPayslipGenerated;
	@Column(name = "esd_total_salary", precision = 10, scale = 2)
	private BigDecimal totalSalary;
	@ManyToOne
	@JoinColumn(name = "esd_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "esd_company_id")
	private CompanyInfo companyInfo;
	@Column(name = "esd_remark", length = 255)
	private String remarks;

}
