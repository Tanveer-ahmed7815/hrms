package com.te.flinko.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
@Table(name = "fa_sales_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "saleItemId")
public class SalesOrderItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "soi_sale_item_id", unique = true, nullable = false, precision = 19)
	private Long saleItemId;
	@Column(name = "soi_product_name", length = 50)
	private String productName;
	@Column(name = "soi_description", length = 999)
	private String description;
	@Column(name = "soi_quantity", precision = 19)
	private long quantity;
	@Column(name = "soi_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "soi_discount", precision = 10, scale = 2)
	private BigDecimal discount;
	@Column(name = "soi_tax", precision = 10, scale = 2)
	private BigDecimal tax;
	@Column(name = "soi_receivable_amount", precision = 10, scale = 2)
	private BigDecimal receivableAmount;
	@ManyToOne
	@JoinColumn(name = "soi_stock_category_id")
	private CompanyStockCategories companyStockCategories;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "salesOrderItems")
	private List<SalesInvoiceItems> salesInvoiceItemsList;
	@ManyToOne
	@JoinColumn(name = "soi_sales_order_id")
	private CompanySalesOrder companySalesOrder;

}
