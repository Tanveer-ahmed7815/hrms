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
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_stock_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "stockGroupId")
public class CompanyStockGroup extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csg_stock_group_id", unique = true, nullable = false, precision = 19)
	private Long stockGroupId;
	
	@Column(name = "csg_stock_group_name", length = 50)
	private String stockGroupName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockGroup")
	private List<CompanyPurchaseOrder> companyPurchaseOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockGroup")
	private List<CompanySalesOrder> companySalesOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockGroup")
	private List<CompanyStockCategories> companyStockCategoriesList;
	@ManyToOne
	@JoinColumn(name = "csg_company_id")
	private CompanyInfo companyInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockGroup")
	private List<CompanyStockGroupItems> companyStockGroupItemsList;

}
