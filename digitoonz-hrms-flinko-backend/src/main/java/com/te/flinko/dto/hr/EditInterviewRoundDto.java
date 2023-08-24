package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class EditInterviewRoundDto {
	private Integer oldInterviewRoundId;
	private String oldInterviewRoundName;
	private Integer newInterviewRoundId;
	private String newInterviewRoundName;
	private Long companyId;
}
