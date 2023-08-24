package com.te.flinko.entity.admindept;

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
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyStockCategories;
import com.te.flinko.entity.admin.CompanyStockGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_stock_group_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "stockGroupItemId")
public class CompanyStockGroupItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csgi_stock_group_item_id", unique = true, nullable = false, precision = 19)
	private Long stockGroupItemId;
	@Column(name = "csgi_in_out", length = 50)
	private String inOut;
	@Column(name = "csgi_quantity", precision = 10)
	private int quantity;
	@Column(name = "csgi_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "csgi_free", precision = 10)
	private int free;
	@Column(name = "csgi_product_name")
	private String productName;
	@Column(name = "csgi_in_use", precision = 10)
	private int inUse;
	@Column(name = "csgi_working", precision = 10)
	private int working;
	@Column(name = "csgi_in_count", precision = 10)
	private int inCount;
	@Column(name = "csgi_out_count", precision = 10)
	private int outCount;
	@ManyToOne
	@JoinColumn(name = "csgi_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "csgi_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;
	@ManyToOne
	@JoinColumn(name = "csgi_sales_order_id")
	private CompanySalesOrder companySalesOrder;
	@ManyToOne
	@JoinColumn(name = "csgi_stock_category_id")
	private CompanyStockCategories companyStockCategories;
	@ManyToOne
	@JoinColumn(name = "csgi_stock_group_id")
	private CompanyStockGroup companyStockGroup;

}
