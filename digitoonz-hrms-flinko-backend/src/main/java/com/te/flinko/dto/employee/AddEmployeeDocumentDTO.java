package com.te.flinko.dto.employee;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AddEmployeeDocumentDTO {
	
	private String documentObjectId;
	
	private List<String> deletedURLs;
	
	private Map<String, String> documents;

}
