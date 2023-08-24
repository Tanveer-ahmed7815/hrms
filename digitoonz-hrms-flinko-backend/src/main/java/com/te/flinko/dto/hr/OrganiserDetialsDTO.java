package com.te.flinko.dto.hr;

import java.time.LocalTime;
import java.util.Map;

import lombok.Data;

@Data
public class OrganiserDetialsDTO {
	
	private Long employeeId;
	private String employeeName;
	private LocalTime startTime;
	private Integer duration;
	private String status;
	private Map<String, String> feedback;
	
}
