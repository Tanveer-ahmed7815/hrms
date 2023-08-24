package com.te.flinko.dto.project;

import java.util.List;

import lombok.Data;

@Data
public class MapEmployeeProjectDTO {
	
	private Long projectId;
	private List<Long> 	listOfEmployeePersonalInfoId;
	private String projectName;
	private List<String> members;	
}
