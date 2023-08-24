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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_stock_units")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "unitId")
public class CompanyStockUnits extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csu_unit_id", unique = true, nullable = false, precision = 19)
	private Long unitId;
	
	@Column(name = "csu_symbol", length = 50)
	private String symbol;
	
	@Column(name = "csu_uqc", nullable = false, length = 50)
	private String uqc;
	
	@Column(name = "csu_decimal_place", precision = 10)
	private Integer decimalPlace;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStockUnits")
	private List<CompanyStockCategories> companyStockCategoriesList;
	@ManyToOne
	@JoinColumn(name = "csu_company_id")
	private CompanyInfo companyInfo;

}
