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
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.sales.CompanyClientInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_work_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "workOrderId")
public class CompanyWorkOrder extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cwo_work_order_id", unique = true, nullable = false, precision = 19)
	private Long workOrderId;
	@Column(name = "cwo_department_name", length = 50)
	private String departmentName;
	@Column(name = "cwo_work_title", length = 100)
	private String workTitle;
	@Column(name = "cwo_priority", length = 50)
	private String priority;
	@Column(name = "cwo_is_cost_estimated", precision = 3)
	private Boolean isCostEstimated;
	@Column(name = "cwo_estimated_cost", precision = 10, scale = 2)
	private BigDecimal estimatedCost;
	@Column(name = "cwo_description", length = 999)
	private String description;
	@Column(name = "cwo_status", length = 25)
	private String status;
	@Column(name = "cwo_rejection_reason", length = 999)
	private String rejectionReason;
	@Column(name = "cwo_no_of_employee", precision = 19)
	private Long noOfEmployee;
	@ManyToOne
	@JoinColumn(name = "cwo_client_id")
	private CompanyClientInfo companyClientInfo;
	@ManyToOne
	@JoinColumn(name = "cwo_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cwo_request_to")
	private EmployeePersonalInfo employeePersonalInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyWorkOrder")
	private List<WorkOrderResources> workOrderResourcesList;

}
