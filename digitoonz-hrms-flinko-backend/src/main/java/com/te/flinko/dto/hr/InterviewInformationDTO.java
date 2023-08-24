package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.util.Map;

import lombok.Data;

@Data
public class InterviewInformationDTO {

	private Long interviewId;
	private Long interviewerId;
	private String interviewerName;
	private LocalDate interviewDate;
	private Integer roundOfInterview;
	private String roundName;
	private Map<String, String> feedback;
	private String overallFeedback;
	private String detailedFeedback;
	
}
