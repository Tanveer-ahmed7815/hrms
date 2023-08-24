package com.te.flinko.entity.account.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.audit.Audit;
import com.te.flinko.dto.account.mongo.ContactPerson;
import com.te.flinko.dto.account.mongo.VendorAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("fa_company_vendor_info")
public class CompanyVendorInfo extends Audit implements Serializable{

	@Field("cvi_vendor_info_id")
	private Long vendorInfoId;

	@Field("cvi_company_id")
	private Long companyId;
	
	@Field("cvi_vendor_name")
	private String vendorName;
	
	@Field("cvi_contact_persons")
	private List<ContactPerson> contactPersons;
	
	@Field("cvi_vendor_address")
	private List<VendorAddress> vendorAddress;
	
	@Field("cvi_other_details")
	private Map<String, String> otherDetails;

}
