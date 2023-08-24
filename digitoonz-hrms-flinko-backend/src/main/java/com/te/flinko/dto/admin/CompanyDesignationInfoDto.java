package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.dto.employee.CompanyRegistrationDto;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyDesignationInfoDto implements Serializable {
	private Long designationId;
	private String designationName;
	private String department;
	private Boolean isSubmited;
	@Convert(converter = ListToStringConverter.class)
	private List<String> roles;
	private CompanyRegistrationDto companyInfo;
	private Set<CompanyDesignationInfoDto> childcompanyDesignationInfoDto;
}
