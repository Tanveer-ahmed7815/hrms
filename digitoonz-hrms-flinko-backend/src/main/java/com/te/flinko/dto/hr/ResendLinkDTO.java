package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class ResendLinkDTO {
	
	private Long personalInfoId;
	private String link;
	private String resentBy;
	private String comment;
	
}
