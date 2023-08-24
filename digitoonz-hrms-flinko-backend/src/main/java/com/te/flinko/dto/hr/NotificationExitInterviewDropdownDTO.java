package com.te.flinko.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationExitInterviewDropdownDTO {

	private String employeeId;
	
	private String employeeName;
	
	private Long employeeInfoId;
}
