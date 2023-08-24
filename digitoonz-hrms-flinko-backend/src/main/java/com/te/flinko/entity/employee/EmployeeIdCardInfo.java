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
@Table(name = "fa_employee_id_card_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "cardId")
public class EmployeeIdCardInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eici_card_id", unique = true, nullable = false, precision = 19)
	private Long cardId;
	@Column(name = "eici_applied_date")
	private LocalDate appliedDate;
	@Column(name = "eici_issued_by", length = 50)
	private String issuedBy;
	@Column(name = "eici_issued_date")
	private LocalDate issuedDate;
	@ManyToOne
	@JoinColumn(name = "eici_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
