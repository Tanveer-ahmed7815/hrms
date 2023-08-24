package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyAddressInfo;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyShiftInfo;
import com.te.flinko.entity.admin.CompanyWorkWeekRule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_official_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "officialId")
public class EmployeeOfficialInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eoi_official_id", unique = true, nullable = false, precision = 19)
	private Long officialId;
	@Column(name = "eoi_employee_id", nullable = false, length = 25)
	private String employeeId;
	@Column(name = "eoi_official_email_id", length = 100)
	private String officialEmailId;
	@JsonFormat(shape = Shape.STRING,pattern = "MM-dd-YYYY")
	@Column(name = "eoi_doj")
	private LocalDate doj;
	@Column(name = "eoi_employee_type", length = 25)
	private String employeeType;
	@Column(name = "eoi_department", length = 50)
	private String department;
	@Column(name = "eoi_designation", length = 50)
	private String designation;
	@Column(name = "eoi_employeement_status", length = 25)
	private String employeementStatus;
	@Column(name = "eoi_probation_start_date")
	private LocalDate probationStartDate;
	@Column(name = "eoi_probation_end_date")
	private LocalDate probationEndDate;
	@ManyToOne
	@JoinColumn(name = "eoi_branch_address_id")
	private CompanyAddressInfo companyAddressInfo;
	@ManyToOne
	@JoinColumn(name = "eoi_office_branch_id")
	private CompanyBranchInfo companyBranchInfo;
	@ManyToOne
	@JoinColumn(name = "eoi_shift_id")
	private CompanyShiftInfo companyShiftInfo;
	@ManyToOne
	@JoinColumn(name = "eoi_work_week_rule_id")
	private CompanyWorkWeekRule companyWorkWeekRule;
}
