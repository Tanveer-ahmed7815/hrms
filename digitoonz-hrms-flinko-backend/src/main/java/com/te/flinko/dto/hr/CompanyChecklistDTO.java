package com.te.flinko.dto.hr;

import java.util.List;

import lombok.Data;
@Data
public class CompanyChecklistDTO {
	private Long checklistId;
	private List<String> checklistFactor;
	private Long companyId;
	private List<String> duplicateChecklistFactors;
	private String newFactor;
	private String oldFactor;
	
}
