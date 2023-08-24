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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_dependent_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "dependentId")
public class EmployeeDependentInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "edi_dependent_id", unique = true, nullable = false, precision = 19)
	private Long dependentId;
	
	@Column(name = "edi_full_name", length = 50)
	private String fullName;
	
	@Column(name = "edi_relation_ship", length = 50)
	private String relationShip;
	
	@Column(name = "edi_gender", length = 20)
	private String gender;
	
	@Column(name = "edi_dob")
	private LocalDate dob;
	
	@Column(name = "edi_mobile_number", unique = true, nullable = false, precision = 19)
	private Long mobileNumber;
	
	@ManyToOne
	@JoinColumn(name = "edi_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
