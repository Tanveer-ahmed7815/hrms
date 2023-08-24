package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_holiday_details")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "holidayId")
public class CompanyHolidayDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chd_holiday_id", unique = true, nullable = false, precision = 19)
	private Long holidayId;
	@Column(name = "chd_holiday_name", length = 50)
	private String holidayName;
	@Column(name = "chd_holiday_date")
	private LocalDate holidayDate;
	@Column(name = "chd_is_optional", precision = 3)
	private Boolean isOptional;
	@ManyToOne
	@JoinColumn(name = "chd_company_id")
	private CompanyInfo companyInfo;

}
