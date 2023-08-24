package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_variable_pay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "variablePayId")
public class EmployeeVariablePay extends Audit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evp_variable_pay_id", unique = true, nullable = false, precision = 19)
	private Long variablePayId;
	@Column(name = "evp_percentage", precision = 10, scale = 2)
	private BigDecimal percentage;
	@Column(name = "evp_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Convert(converter = ListToStringConverter.class)
	@Column(name = "evp_effective_month", length = 999)
	private List<String> effectiveMonth;
	@Column(name = "evp_description", length = 999)
	private String description;

}
