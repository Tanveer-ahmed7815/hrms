package com.te.flinko.dto.reportingmanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDetailsDTO {
	
	private Long projectId;
	
	private String projectName;

}
