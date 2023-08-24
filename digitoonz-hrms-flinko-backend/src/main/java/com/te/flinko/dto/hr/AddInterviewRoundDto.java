package com.te.flinko.dto.hr;

import java.util.Map;

import lombok.Data;
@Data
public class AddInterviewRoundDto {
	private Long interviewRoundId;
	private Long companyId;
	private Map<Integer, String> rounds;
	

	

}
