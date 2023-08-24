package com.te.flinko.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralRewardsListDTO {
	
	
	private String employeeId;
	
	private String fullName;
	
	private String department;
	
	private String designation;
		
	private String referredTo;

}
