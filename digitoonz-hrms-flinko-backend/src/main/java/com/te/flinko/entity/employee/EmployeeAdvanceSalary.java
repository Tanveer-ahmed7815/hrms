package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.util.LinkedHashMapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_advance_salary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "advanceSalaryId")
public class EmployeeAdvanceSalary extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eas_advance_salary_id", unique = true, nullable = false, precision = 19)
	private Long advanceSalaryId;
	@Column(name = "eas_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "eas_emi", precision = 10, scale = 2)
	private BigDecimal emi;
	@Column(name = "eas_reason", length = 999)
	private String reason;
	@Column(name = "eas_status", length = 25)
	private String status;
	@Column(name = "eas_approved_by", length = 255)
	@Convert(converter = LinkedHashMapToStringConverter.class)
	private LinkedHashMap<String, String> approvedBy;
	@Column(name = "eas_is_paid", precision = 3)
	private Boolean isPaid;
	@Column(name = "eas_rejected_by", length = 50)
	private String rejectedBy;
	@Column(name = "eas_rejected_reason", length = 999)
	private String rejectedReason;
	@ManyToOne
	@JoinColumn(name = "eas_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
