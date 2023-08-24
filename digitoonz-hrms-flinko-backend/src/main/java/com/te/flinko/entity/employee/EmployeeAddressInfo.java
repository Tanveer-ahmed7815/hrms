package com.te.flinko.entity.employee;

import java.io.Serializable;

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
@Table(name = "fa_employee_address_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "addressId")
public class EmployeeAddressInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eai_address_id", unique = true, nullable = false, precision = 19)
	private Long addressId;
	@Column(name = "eai_address_type", length = 25)
	private String addressType;
	@Column(name = "eai_address_details", length = 999)
	private String addressDetails;
	@Column(name = "eai_city", length = 25)
	private String city;
	@Column(name = "eai_state", length = 25)
	private String state;
	@Column(name = "eai_country", length = 25)
	private String country;
	@Column(name = "eai_pin_code", precision = 19)
	private Long pinCode;
	@ManyToOne
	@JoinColumn(name = "eai_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
