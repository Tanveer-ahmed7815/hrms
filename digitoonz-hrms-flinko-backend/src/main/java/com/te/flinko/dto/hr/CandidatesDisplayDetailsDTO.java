package com.te.flinko.dto.hr;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatesDisplayDetailsDTO {
	
	private Long candidateId;
	private String fullName;
	private String emailId;
	private long mobileNumber;
	private String designation;
	private BigDecimal yearOfExperience;
	
}
