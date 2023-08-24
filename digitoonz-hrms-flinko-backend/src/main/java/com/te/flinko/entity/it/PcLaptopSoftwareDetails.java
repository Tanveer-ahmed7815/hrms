package com.te.flinko.entity.it;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_pc_laptop_software_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "softwareDetailsId")
public class PcLaptopSoftwareDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plsd_software_details_id", unique = true, nullable = false, precision = 19)
	private Long softwareDetailsId;
	@Column(name = "plsd_software_logo_url", length = 255)
	private String softwareDetailsLogoUrl;
	@Column(name = "plsd_software_name", length = 50)
	private String softwareName;
	@Column(name = "plsd_installed_date")
	private LocalDate installedDate;
	@Column(name = "plsd_installed_by", length = 50)
	private String installedBy;
	@Column(name = "plsd_expiration_date")
	private LocalDate expirationDate;
	@Column(name = "plsd_notification_date")
	private LocalDate notificationDate;
	@Column(name = "plsd_uninstalled_date")
	private LocalDate uninstalledDate;
	@Column(name = "plsd_uninstalled_by", length = 50)
	private String uninstalledBy;
	@Column(name = "plsd_is_renewed", precision = 3)
	private Boolean isRenewed;
	@ManyToOne
	@JoinColumn(name = "plsd_serial_number")
	private CompanyPcLaptopDetails companyPcLaptopDetails;

}
