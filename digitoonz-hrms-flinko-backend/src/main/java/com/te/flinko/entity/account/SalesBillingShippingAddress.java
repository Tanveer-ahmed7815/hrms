package com.te.flinko.entity.account;

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

import lombok.*;

@Entity
@Table(name = "fa_sales_billing_shipping_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "salesAddressId")
public class SalesBillingShippingAddress extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sbsa_sales_address_id", unique = true, nullable = false, precision = 19)
	private Long salesAddressId;
	@Column(name = "sbsa_address_type", length = 25)
	private String addressType;
	@Column(name = "sbsa_address_details", length = 255)
	private String addressDetails;
	@Column(name = "sbsa_city", length = 25)
	private String city;
	@Column(name = "sbsa_state", length = 25)
	private String state;
	@Column(name = "sbsa_country", length = 25)
	private String country;
	@Column(name = "sbsa_pin_code", length = 15)
	private String pinCode;
	@ManyToOne
	@JoinColumn(name = "sbsa_sales_order_id")
	private CompanySalesOrder companySalesOrder;

}
