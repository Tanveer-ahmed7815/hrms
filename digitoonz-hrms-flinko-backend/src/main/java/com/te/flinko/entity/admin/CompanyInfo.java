package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.account.CompanyCostEvaluation;
import com.te.flinko.entity.account.CompanyPurchaseInvoice;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesInvoice;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.account.CompanyWorkOrder;
import com.te.flinko.entity.admindept.CompanySoftwareDetails;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReviseSalary;
import com.te.flinko.entity.employee.EmployeeSalaryDetails;
import com.te.flinko.entity.employee.EmployeeTerminationDetails;
import com.te.flinko.entity.hr.CandidateInfo;
import com.te.flinko.entity.hr.CompanyEventDetails;
import com.te.flinko.entity.it.CompanyHardwareItems;
import com.te.flinko.entity.it.CompanyPcLaptopDetails;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.sales.CompanyClientInfo;
import com.te.flinko.entity.superadmin.CompanyNotification;
import com.te.flinko.entity.superadmin.PaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "companyId")
public class CompanyInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ci_company_id", unique = true, nullable = false, precision = 19)
	private Long companyId;
	@Column(name = "ci_company_logo_url", length = 100)
	private String companyLogoUrl;
	@Column(name = "ci_company_name", nullable = false, length = 50)
	private String companyName;
	@Column(name = "ci_pan", precision = 19)
	private String pan;
	@Column(name = "ci_gstin", precision = 19)
	private String gstin;
	@Column(name = "ci_cin", precision = 19)
	private String cin;
	@Column(name = "ci_no_of_emp", precision = 19)
	private Long noOfEmp;
	@Column(name = "ci_email_id", length = 100)
	private String emailId;
	@Column(name = "ci_mobile_number", precision = 19)
	private Long mobileNumber;
	@Column(name = "ci_telephone_number", precision = 19)
	private Long telephoneNumber;
	@Column(name = "ci_type_of_industry", length = 50)
	private String typeOfIndustry;
	@Column(name = "ci_isActive", length = 50)
	private Boolean isActive;

	@Column(name = "ci_is_submited")
	private Boolean isSubmited;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CandidateInfo> candidateInfoList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyBranchInfo> companyBranchInfoList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyClientInfo> companyClientInfoList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyCostEvaluation> companyCostEvaluationList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyDesignationInfo> companyDesignationInfoList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyEmployeeResignationDetails> companyEmployeeResignationDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyEventDetails> companyEventDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyExpenseCategories> companyExpenseCategoriesList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyHardwareItems> companyHardwareItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyHolidayDetails> companyHolidayDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyLeadCategories> companyLeadCategoriesList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyNotification> companyNotificationList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyPcLaptopDetails> companyPcLaptopDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyPurchaseInvoice> companyPurchaseInvoiceList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyPurchaseOrder> companyPurchaseOrderList;
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private CompanyRuleInfo companyRuleInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanySalesInvoice> companySalesInvoiceList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanySalesOrder> companySalesOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanySoftwareDetails> companySoftwareDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyStockGroup> companyStockGroupList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyStockGroupItems> companyStockGroupItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyStockUnits> companyStockUnitsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyWorkOrder> companyWorkOrderList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyWorkWeekRule> companyWorkWeekRuleList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<CompanyPayrollInfo> companyPayrollInfoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<EmployeePersonalInfo> employeePersonalInfoList;
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private LevelsOfApproval levelsOfApproval;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<PaymentDetails> paymentDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<ProjectDetails> projectDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<EmployeeSalaryDetails> employeeSalaryDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<EmployeeTerminationDetails> employeeTerminationDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyInfo")
	private List<EmployeeReviseSalary> employeeReviseSalaryList;

	@PrePersist
	public void setIsSubmitted() {
		isSubmited = isSubmited == null ? Boolean.FALSE : isSubmited.booleanValue();
	}

}
