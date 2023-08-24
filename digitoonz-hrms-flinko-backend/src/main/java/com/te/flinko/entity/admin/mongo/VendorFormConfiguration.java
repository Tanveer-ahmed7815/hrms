package com.te.flinko.entity.admin.mongo;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.audit.Audit;

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
@Document("fa_vendor_form_configuration")
public class VendorFormConfiguration extends Audit implements Serializable{

	@Id
	private String formConfigurationObjectId;
	
	@Field("vfc_form_configuration_id")
	private Long formConfigurationId;

	@Field("vfc_company_id")
	private Long companyId;

	@Field("vfc_attributes")
	private Map<String, String> attributes;
}
