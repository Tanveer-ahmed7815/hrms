package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateInterviewInfoDTO {
	
	private Long candidateId;
	private Long companyId;
	private String interviewType;
	private String interviewDetails;
	private Integer roundOfInterview;
	private String roundName;
	private Map<String, String> feedback;
	private LocalDate interviewDate;
	private LocalTime startTime;
	private Integer duration;
	private Long employeePersonalInfo;
	private String linkUrlFeedback;
}