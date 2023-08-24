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
@Table(name = "fa_purchase_billing_shipping_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "purchaseAddressId")
public class PurchaseBillingShippingAddress extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pbsa_purchase_address_id", unique = true, nullable = false, precision = 19)
	private Long purchaseAddressId;
	@Column(name = "pbsa_address_type", length = 25)
	private String addressType;
	@Column(name = "pbsa_address_details", length = 255)
	private String addressDetails;
	@Column(name = "pbsa_city", length = 25)
	private String city;
	@Column(name = "pbsa_state", length = 25)
	private String state;
	@Column(name = "pbsa_country", length = 25)
	private String country;
	@Column(name = "pbsa_pin_code", length = 15)
	private String pinCode;
	@ManyToOne
	@JoinColumn(name = "pbsa_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;

}
