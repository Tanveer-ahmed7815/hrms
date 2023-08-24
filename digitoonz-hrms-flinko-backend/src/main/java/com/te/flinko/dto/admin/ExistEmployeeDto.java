package com.te.flinko.dto.admin;



import java.util.List;

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
public class ExistEmployeeDto {

	private List<BranchInfoDto> branchInfoDtos;
	private List<DepartmentInfoDto> departmentInfoDtos;
	private List<DesignationInfoDto> designationInfoDtos;
	private List<WorkWeekInfoDto> weekInfoDtos;
}
