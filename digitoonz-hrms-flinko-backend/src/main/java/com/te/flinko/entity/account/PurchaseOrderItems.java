package com.te.flinko.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyStockCategories;

import lombok.*;

@Entity
@Table(name = "fa_purchase_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "purchaseItemId")
public class PurchaseOrderItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poi_purchase_item_id", unique = true, nullable = false, precision = 19)
	private Long purchaseItemId;
	@Column(name = "poi_product_name", length = 50)
	private String productName;
	@Column(name = "poi_description", length = 999)
	private String description;
	@Column(name = "poi_quantity", precision = 19)
	private long quantity;
	@Column(name = "poi_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "poi_discount", precision = 10, scale = 2)
	private BigDecimal discount;
	@Column(name = "poi_tax", precision = 10, scale = 2)
	private BigDecimal tax;
	@Column(name = "poi_payable_amount", precision = 10, scale = 2)
	private BigDecimal payableAmount;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderItems")
	private Set<PurchaseInvoiceItems> purchaseInvoiceItems;
	@ManyToOne
	@JoinColumn(name = "poi_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;
	@ManyToOne
	@JoinColumn(name = "poi_stock_category_id")
	private CompanyStockCategories companyStockCategories;
}
