package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class RejectCandidateRequestDTO {
	private Long personalId;
	private String rejectedBy;
	private String reason;
}
