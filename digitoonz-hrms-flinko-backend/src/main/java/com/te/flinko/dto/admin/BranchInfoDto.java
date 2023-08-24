package com.te.flinko.dto.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@JsonInclude(value = Include.NON_DEFAULT)
public class BranchInfoDto {

	private Long branchId;     //CompanyBranchInfo (M-1) CompanyInfo
	private String branchName; 
	
}
