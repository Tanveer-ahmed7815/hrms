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
@Table(name = "fa_employee_visa_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "visaId")
public class EmployeeVisaInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evi_visa_id", unique = true, nullable = false, precision = 19)
	private Long visaId;
	@Column(name = "evi_country_of_issue", nullable = false, length = 50)
	private String countryOfIssue;
	@Column(name = "evi_visa_number", nullable = false, length = 15)
	private String visaNumber;
	@Column(name = "evi_visa_expiry_date", nullable = false)
	private LocalDate visaExpiryDate;
	@ManyToOne
	@JoinColumn(name = "evi_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
