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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.entity.admin.CompanyInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_termination_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "terminationId")
public class EmployeeTerminationDetails implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "etd_terminationId", unique = true, nullable = false, precision = 19)
	private Long terminationId;
	@Column(name = "etd_reason", length = 999)
	private String reason;
	@Column(name = "etd_termination_date")
	private LocalDate terminationDate;
	@Column(name = "etd_termination_type", length = 25)
	private String terminationType;
	@ManyToOne
	@JoinColumn(name = "etd_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "etd_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
