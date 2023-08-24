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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_work_week_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "workWeekRuleId")

public class CompanyWorkWeekRule extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cwwr_work_week_rule_id", unique = true, nullable = false, precision = 19)
	private Long workWeekRuleId;
	@Column(name = "cwwr_rule_name", length = 100)
	private String ruleName;
	@Column(name = "cwwr_is_default", precision = 3)
	private Boolean isDefault;
	@ManyToOne
	@JoinColumn(name = "cwwr_company_id")
	private CompanyInfo companyInfo;
//	@JsonManagedReference(value = "workWeekRule")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyWorkWeekRule")
	private List<WorkOffDetails> workOffDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyWorkWeekRule")
	private List<EmployeeOfficialInfo> employeeOfficialInfoList;

}
