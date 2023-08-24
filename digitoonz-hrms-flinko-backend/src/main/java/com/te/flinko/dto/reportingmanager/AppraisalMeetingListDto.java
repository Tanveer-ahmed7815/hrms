package com.te.flinko.dto.reportingmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraisalMeetingListDto {
	private String employeeId;
	private String officialEmailId;
	private String designation;
	private String meetingType;
	private Long meetingId;
	private Boolean isFeedbackGiven; 

}
