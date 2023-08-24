package com.te.flinko.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Brunda
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyAddressInfoDTO {

	private Long companyAddressId;
	private String addressDetails;//CompanyAddressInfo
	private String state;
	private String country;
	private Long pincode;
	private Double latitude;
	private Double longitude;
	private Double radius;
	private Object geoFencingLocation;
}
