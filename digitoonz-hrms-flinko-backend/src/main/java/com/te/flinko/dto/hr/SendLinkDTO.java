package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class SendLinkDTO {
	
	private String link;
	
	private Long candidateId;

}
