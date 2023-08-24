package com.te.flinko.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "fa_purchase_invoice_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "purchaseInvoiceItemId")
public class PurchaseInvoiceItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pii_purchase_invoice_item_id", unique = true, nullable = false, precision = 19)
	private Long purchaseInvoiceItemId;
	@Column(name = "pii_description", length = 999)
	private String description;
	@Column(name = "pii_quantity", precision = 10)
	private Integer quantity;
	@Column(name = "pii_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "pii_discount", precision = 3, scale = 2)
	private BigDecimal discount;
	@Column(name = "pii_tax", precision = 10, scale = 2)
	private BigDecimal tax;
	@Column(name = "pii_payable_amount", precision = 10, scale = 2)
	private BigDecimal payableAmount;
	@ManyToOne
	@JoinColumn(name = "pii_purchase_invoice_id")
	private CompanyPurchaseInvoice companyPurchaseInvoice;
	@ManyToOne
	@JoinColumn(name = "pii_purchase_item_id")
	private PurchaseOrderItems purchaseOrderItems;

}
