package com.te.flinko.dto.account.mongo;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class VendorAddress implements Serializable{
	
	@Field("address_details")
	private String addressDetails;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private Long pincode;
}