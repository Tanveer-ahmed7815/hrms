package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_employee_resignation_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "resignationId")
public class CompanyEmployeeResignationDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cerd_resignation_id", unique = true, nullable = false, precision = 19)
	private Long resignationId;
	@Column(name = "cerd_resignation_reason", length = 999)
	private String resignationReason;
	@Column(name = "cerd_applied_date")
	private LocalDate appliedDate;
	@Column(name = "cerd_check_list_to_be_cleared", length = 999)
	@Convert(converter = ListToStringConverter.class)
	private List<String> checkListToBeCleared;
	@Convert(converter = ListToStringConverter.class)
	@Column(name = "cerd_accessory_submission", length = 999)
	private List<String> accessorySubmission;
	@Column(name = "cerd_notice_period_start_date")
	private LocalDate noticePeriodStartDate;
	@Column(name = "cerd_notice_period_duration", precision = 2, scale = 2)
	private BigDecimal noticePeriodDuration;
	@Column(name = "cerd_status", length = 25)
	private String status;
	@ManyToOne
	@JoinColumn(name = "cerd_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cerd_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyEmployeeResignationDetails")
	private List<EmployeeResignationDiscussion> employeeResignationDiscussionList;

}
