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
import com.te.flinko.entity.sales.CompanyClientInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_lead_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "leadCategoryId")
public class CompanyLeadCategories extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clc_lead_category_id", unique = true, nullable = false, precision = 19)
	private Long leadCategoryId;
	@Column(name = "clc_lead_category_name", length = 50)
	private String leadCategoryName;
	@Column(name = "clc_color", length = 25)
	private String color;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyLeadCategories")
	private List<CompanyClientInfo> companyClientInfoList;
	@ManyToOne
	@JoinColumn(name = "clc_company_id")
	private CompanyInfo companyInfo;

}
