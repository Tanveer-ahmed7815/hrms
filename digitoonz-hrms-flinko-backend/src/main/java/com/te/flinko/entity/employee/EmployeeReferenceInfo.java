package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.hr.CandidateInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_reference_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "referenceId")
public class EmployeeReferenceInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eri_reference_id", unique = true, nullable = false, precision = 19)
	private Long referenceId;
	@Column(name = "eri_reference_code", length = 25)
	private String referenceCode;
	@Column(name = "eri_referral_name", length = 100)
	private String referralName;
	@Column(name = "eri_reward_amount", precision = 10, scale = 2)
	private BigDecimal rewardAmount;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime rewardUpdatedDate;
	@Column(name = "eri_is_included_in_salary")
	private Boolean isIncludedInSalary;
	@OneToOne
	@JoinColumn(name = "eri_candidate_id")
	private CandidateInfo candidateInfo;
	@OneToOne
	@JoinColumn(name = "eri_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "eri_referral_employee_id")
	private EmployeePersonalInfo refferalEmployeePersonalInfo;
}
