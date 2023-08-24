package com.te.flinko.entity.project;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
import lombok.ToString;

@Entity
@Table(name = "fa_project_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "projectId")
public class ProjectDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pd_project_id", unique = true, nullable = false, precision = 19)
	private Long projectId;
	@Column(name = "pd_project_name", length = 50)
	private String projectName;
	@Column(name = "pd_project_description", length = 999)
	private String projectDescription;
	@Column(name = "pd_start_date")
	private LocalDate startDate;
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "allocatedProjectList")
	private List<EmployeePersonalInfo> employeePersonalInfoList;
	@ManyToOne
	@JoinColumn(name = "pd_client_id")
	private CompanyClientInfo companyClientInfo;
	@ManyToOne
	@JoinColumn(name = "pd_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "pd_project_manager_id")
	private EmployeePersonalInfo projectManager;
	@ManyToOne
	@JoinColumn(name = "pd_reporting_manager_id")
	private EmployeePersonalInfo reportingManager;
	@OneToOne
	@JoinColumn(name = "pd_project_estimation_id")
	private ProjectEstimationDetails projectEstimationDetails;

}
