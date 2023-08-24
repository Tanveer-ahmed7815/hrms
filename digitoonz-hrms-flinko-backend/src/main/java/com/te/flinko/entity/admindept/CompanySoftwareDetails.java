package com.te.flinko.entity.admindept;

import java.io.Serializable;
import java.math.BigDecimal;
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
import com.te.flinko.entity.admin.CompanyInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_software_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "softwareId")
public class CompanySoftwareDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csd_software_id", unique = true, nullable = false, precision = 19)
	private Long softwareId;
	@Column(name = "csd_software_name", length = 100)
	private String softwareName;
	@Column(name = "csd_expiration_date")
	private LocalDate expirationDate;
	@Column(name = "csd_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@ManyToOne
	@JoinColumn(name = "csd_company_id")
	private CompanyInfo companyInfo;

}
