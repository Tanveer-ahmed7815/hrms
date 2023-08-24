package com.te.flinko.dto.reportingmanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditInterviewDTO {

	private String meetingType;

	private LocalDate meetingDate;

	private LocalTime startTime;

	private Integer duration;

	private String meetingDetails;
	
	private String reason;

	private List<Long> employeeInfoIdList;

	private String status;
}
