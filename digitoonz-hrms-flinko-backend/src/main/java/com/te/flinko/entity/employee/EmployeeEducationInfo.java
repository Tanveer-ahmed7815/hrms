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
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_education_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "educationId")
public class EmployeeEducationInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eei_education_id", unique = true, nullable = false, precision = 19)
	private Long educationId;
	@Column(name = "eei_degree", length = 25)
	private String degree;
	@Column(name = "eei_course", length = 25)
	private String course;
	@Column(name = "eei_institude_name", length = 100)
	private String institudeName;
	@Column(name = "eei_average_grade", length = 10)
	private String averageGrade;
	@Column(name = "eei_course_start_date")
	private LocalDate courseStartDate;
	@Column(name = "eei_course_end_date")
	private LocalDate courseEndDate;
	@Column(name = "eei_year_of_passing", length = 4)
	private String yearOfPassing;
	@Column(name = "eei_description", length = 999)
	private String description;
	@ManyToOne
	@JoinColumn(name = "eei_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
