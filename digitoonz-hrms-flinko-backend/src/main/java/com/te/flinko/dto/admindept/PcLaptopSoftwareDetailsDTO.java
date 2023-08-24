package com.te.flinko.dto.admindept;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Brunda
 *
 */

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)

public class PcLaptopSoftwareDetailsDTO implements Serializable {

	private Long softwareDetailsId;
	private String softwareDetailsLogoUrl;
	private String softwareName;
	private LocalDate installedDate;
	private String installedBy;
	private LocalDate expirationDate;
	private LocalDate notificationDate;
	private LocalDate uninstalledDate;
	private String uninstalledBy;
}
