package com.te.flinko.entity.sales;

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
@Table(name = "fa_company_client_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "clientAddressId")
public class CompanyClientAddress extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cca_client_address_id", unique = true, nullable = false, precision = 19)
	private Long clientAddressId;
	@Column(name = "cca_address_details", length = 255)
	private String addressDetails;
	@Column(name = "cca_city", nullable = false, length = 25)
	private String city;
	@Column(name = "cca_state", nullable = false, length = 25)
	private String state;
	@Column(name = "cca_country", nullable = false, length = 25)
	private String country;
	@Column(name = "cca_pin_code", nullable = false, length = 15)
	private String pinCode;
	@Column(name = "cca_address_type", length = 25)
	private String addressType;
	@ManyToOne
	@JoinColumn(name = "cca_client_id")
	private CompanyClientInfo companyClientInfo;

}
