package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;

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
import com.te.flinko.util.LinkedHashMapToStringConverter;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_employment_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "employmentId")
public class EmployeeEmploymentInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eei_employment_id", unique = true, nullable = false, precision = 19)
	private Long employmentId;
	@Column(name = "eei_company_name", length = 50)
	private String companyName;
	@Column(name = "eei_start_date")
	private LocalDate startDate;
	@Column(name = "eei_end_date")
	private LocalDate endDate;
	@Column(name = "eei_designation", length = 50)
	private String designation;
	@Column(name = "eei_job_description", length = 999)
	private String jobDescription;
	@Column(name = "eei_salary", precision = 10, scale = 2)
	private BigDecimal salary;
	@Column(name = "eei_reference_person_details", length = 250)
	@Convert(converter = LinkedHashMapToStringConverter.class)
	private LinkedHashMap<String, String> referencePersonDetails;
	@ManyToOne
	@JoinColumn(name = "eei_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
