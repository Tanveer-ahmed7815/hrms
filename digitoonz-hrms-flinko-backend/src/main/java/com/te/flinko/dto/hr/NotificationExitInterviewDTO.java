package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeePersonalInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationExitInterviewDTO {

	private Long duscussionId;
	
	private String discussionType;
	
	private LocalTime startTime;
	
	private Integer duration;
	
	private String discussionDetails;
	
	private String status;
	
	private List<Long> employeeInfoIdList; 
	
	private LocalDate discussionDate;
	
	//private CompanyEmployeeResignationDetails companyEmployeeResignationDetails;
	
}
