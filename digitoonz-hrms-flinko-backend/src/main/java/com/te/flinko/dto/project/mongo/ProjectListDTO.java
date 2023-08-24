package com.te.flinko.dto.project.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectListDTO {
	private Long projectId;
	private String projectName;
	private Long companyId;
}
