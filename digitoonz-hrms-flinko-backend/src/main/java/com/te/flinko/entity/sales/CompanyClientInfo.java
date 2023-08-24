package com.te.flinko.entity.sales;

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
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.account.CompanyWorkOrder;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyLeadCategories;
import com.te.flinko.entity.project.ProjectDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_client_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "clientId")
public class CompanyClientInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cci_client_id", unique = true, nullable = false, precision = 19)
	private Long clientId;
	@Column(name = "cci_client_name", nullable = false, length = 50)
	private String clientName;
	@Column(name = "cci_email_id", nullable = false, length = 100)
	private String emailId;
	@Column(name = "cci_mobile_number", nullable = false, length = 15)
	private String mobileNumber;
	@Column(name = "cci_telephone_number", length = 15)
	private String telephoneNumber;
	@Column(name = "cci_fax", precision = 19)
	private Long fax;
	@Column(name = "cci_website_url", length = 50)
	private String websiteUrl;
	@Column(name = "cci_type_of_industry", length = 25)
	private String typeOfIndustry;
	@Column(name = "cci_no_of_emp", precision = 10)
	private Integer noOfEmp;
	@Column(name = "cci_annual_revenue", precision = 10, scale = 2)
	private BigDecimal annualRevenue;
	@Column(name = "cci_secondary_email_id", length = 100)
	private String secondaryEmailId;
	@Column(name = "cci_twitter", length = 25)
	private String twitter;
	@Column(name = "cci_skype_id", length = 25)
	private String skypeId;
	@Column(name = "cci_lead_source", length = 50)
	private String leadSource;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyClientInfo")
	private List<ClientContactPersonDetails> clientContactPersonDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyClientInfo")
	private List<CompanyClientAddress> companyClientAddressList;
	@ManyToOne
	@JoinColumn(name = "cci_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cci_lead_category_id")
	private CompanyLeadCategories companyLeadCategories;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyClientInfo")
	private List<CompanySalesOrder> companySalesOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyClientInfo")
	private List<CompanyWorkOrder> companyWorkOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyClientInfo")
	private List<ProjectDetails> projectDetailsList;

}
