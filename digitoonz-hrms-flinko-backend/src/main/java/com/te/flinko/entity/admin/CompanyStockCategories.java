package com.te.flinko.entity.admin;

import java.io.Serializable;
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
import com.te.flinko.entity.account.PurchaseOrderItems;
import com.te.flinko.entity.account.SalesOrderItems;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_stock_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "stockCategoryId")
public class CompanyStockCategories extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csc_stock_category_id", unique = true, nullable = false, precision = 19)
	private Long stockCategoryId;
	
	@Column(name = "csc_stock_category_name", length = 50)
	private String stockCategoryName;
	
	@ManyToOne
	@JoinColumn(name = "csc_stock_group_id")
	private CompanyStockGroup companyStockGroup;
	
	@ManyToOne
	@JoinColumn(name = "csc_unit_id")
	private CompanyStockUnits companyStockUnits;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockCategories")
	private List<CompanyStockGroupItems> companyStockGroupItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockCategories")
	private List<PurchaseOrderItems> purchaseOrderItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockCategories")
	private List<SalesOrderItems> salesOrderItemsList;

}
