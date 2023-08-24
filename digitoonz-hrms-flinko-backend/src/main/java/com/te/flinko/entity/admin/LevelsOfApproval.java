package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_levels_of_approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "levelId")
public class LevelsOfApproval extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loa_level_id", unique = true, nullable = false, precision = 19)
	private Long levelId;
	@Column(name = "loa_leave", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> leave;
	@Column(name = "loa_time_sheet", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> timeSheet;
	@Column(name = "loa_advance_salary", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> advanceSalary;
	@Column(name = "loa_reimbursement", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> reimbursement;
	@ManyToOne
	@JoinColumn(name = "loa_company_id")
	private CompanyInfo companyInfo;

}
