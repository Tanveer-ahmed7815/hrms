package com.te.flinko.entity.employee;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_reporting_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "reportId")
public class EmployeeReportingInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eri_report_id", unique = true, nullable = false, precision = 19)
	private Long reportId;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeReportingInfo")
	private List<EmployeeLeaveApplied> employeeLeaveApplied;
	@ManyToOne
	@JoinColumn(name = "eri_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "eri_reporting_manager_id")
	private EmployeePersonalInfo reportingManager;
	@ManyToOne
	@JoinColumn(name = "eri_reporting_hr_id")
	private EmployeePersonalInfo reportingHR;

}
