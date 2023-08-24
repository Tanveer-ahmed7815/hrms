package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class EmployeeAddressInfoDTO {

	private Long addressId;
	private String addressType;
	private String addressDetails;
	private String city;
	private String state;
	private String country;
	private Long pinCode;

}
