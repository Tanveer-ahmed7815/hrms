package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_branch_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "branchId")
public class CompanyBranchInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cbi_branch_id", unique = true, nullable = false, precision = 19)
	private Long branchId;
	@Column(name = "cbi_branch_name", nullable = false, length = 50)
	private String branchName;
	@Column(name = "cbi_no_of_emp", precision = 19)
	private Long noOfEmp;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyBranchInfo")
	private List<CompanyAddressInfo> companyAddressInfoList;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cbi_company_id")
	private CompanyInfo companyInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyBranchInfo")
	private List<EmployeeOfficialInfo> employeeOfficialInfoList;

	@PreRemove
	public void remove() {
		companyInfo = null;
		employeeOfficialInfoList = employeeOfficialInfoList.stream().map(x -> {
			x.setCompanyBranchInfo(null);
			return x;
		}).collect(Collectors.toList());
	}

}
