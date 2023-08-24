package com.te.flinko.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDocumentDTO {
	
	private String documentObjectId;
	
	private String fileType;
	
	private String url;
	
	private String fileName;

}
