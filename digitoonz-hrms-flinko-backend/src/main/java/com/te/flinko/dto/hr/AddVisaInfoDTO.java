package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AddVisaInfoDTO {
	
	private Long visaId;
	private String visaNumber;
	private LocalDate visaExpiryDate;
	private String countryOfIssue;
	
}
