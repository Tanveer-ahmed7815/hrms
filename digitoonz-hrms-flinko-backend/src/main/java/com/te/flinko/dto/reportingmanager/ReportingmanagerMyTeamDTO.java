package com.te.flinko.dto.reportingmanager;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportingmanagerMyTeamDTO {

	private String employeeId;
	
	private String fullName;
	
	private String officialEmailId;
	
	private String designation;
	
	private List<String> projectName;
}
