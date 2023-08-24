package com.te.flinko.dto.admin.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(value = Include.NON_DEFAULT)
public class VendorFormConfigurationDto implements Serializable{

	private String formConfigurationObjectId;
	
	private Long formConfigurationId;

	private Long companyId;
	
	private Boolean isSubmited;

	private List<Attribute> attributeList;
	
	private Map<String, String> attributes;
}
