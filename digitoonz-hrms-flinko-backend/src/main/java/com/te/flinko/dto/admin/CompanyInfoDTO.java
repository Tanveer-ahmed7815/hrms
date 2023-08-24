package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyInfoDTO implements Serializable {
	private Long companyId;
	private String companyLogoUrl;
	private String companyName;
	private String pan;
	private String gstin;
	private String cin;
	private Long noOfEmp;
	private String emailId;
	private Long mobileNumber;
	private Long telephoneNumber;
	private String typeOfIndustry;
	private Boolean isSubmited;
	private List<CompanyBranchInfoDTO> companyBranchInfoList;
}
