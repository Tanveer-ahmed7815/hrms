package com.te.flinko.dto.hr;

import java.util.List;

import lombok.Data;
@Data          
public class AddFeedbackDTO {
	private Long feedbackConfigurationId;
	private String feedbackType;
	private Long companyId;
	private List<String> feedbackFactor;
	private List<String> duplicatedFactors;
	private String oldFeedbackFactor;
	private String newFeedbackFactor;
	
}

