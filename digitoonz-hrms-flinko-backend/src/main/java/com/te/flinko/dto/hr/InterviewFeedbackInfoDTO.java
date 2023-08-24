package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
@Data
public class InterviewFeedbackInfoDTO {
	private Long companyId;
	private Long interviewId;
	private String candidateFullname;
	private LocalDate interviewDate;
	private String roundName;
	private Integer roundOfInterview;
	private Long interviewerId;
	private String interviewerFullName;
	private List<String> feedbackFactor;

}
