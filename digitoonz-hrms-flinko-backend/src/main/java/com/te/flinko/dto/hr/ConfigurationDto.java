package com.te.flinko.dto.hr;

import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class ConfigurationDto {
	private Long companyId;
	private Map<Integer, String> rounds;
	private List<String> entryFeedbackFactor;
	private List<String> exitFeedbackFactor;
	private List<String> checklistFactor;
}
