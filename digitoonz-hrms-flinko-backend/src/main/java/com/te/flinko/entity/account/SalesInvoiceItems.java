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
@Table(name = "fa_sales_invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "invoiceItemId")
public class SalesInvoiceItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sii_sale_invoice_item_id", unique = true, nullable = false, precision = 19)
	private Long invoiceItemId;
	@Column(name = "sii_description", length = 999)
	private String description;
	@Column(name = "sii_quantity", precision = 10)
	private int quantity;
	@Column(name = "sii_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "sii_discount", precision = 3, scale = 2)
	private BigDecimal discount;
	@Column(name = "sii_tax", precision = 10, scale = 2)
	private BigDecimal tax;
	@Column(name = "sii_receivable_amount", precision = 10, scale = 2)
	private BigDecimal receivableAmount;
	@ManyToOne
	@JoinColumn(name = "sii_sales_invoice_id")
	private CompanySalesInvoice companySalesInvoice;
	@ManyToOne
	@JoinColumn(name = "sii_sale_item_id")
	private SalesOrderItems salesOrderItems;

}
