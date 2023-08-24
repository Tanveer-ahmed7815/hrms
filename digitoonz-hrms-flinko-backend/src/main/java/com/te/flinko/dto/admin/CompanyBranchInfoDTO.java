package com.te.flinko.dto.admin;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Brunda
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyBranchInfoDTO {

	private Long branchId;
	private String branchName;  //CompanyBranchInfo
	private Long noOfEmp;
	private List<CompanyAddressInfoDTO> companyAddressInfoList;
	
	CompanyAddressInfoDTO companyAddressInfo;

}
