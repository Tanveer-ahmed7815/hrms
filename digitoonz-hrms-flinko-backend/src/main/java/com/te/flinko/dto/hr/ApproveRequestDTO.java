package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class ApproveRequestDTO {
	
	private Long personalId;
	private String employeeId;
	private String password;
	
}
