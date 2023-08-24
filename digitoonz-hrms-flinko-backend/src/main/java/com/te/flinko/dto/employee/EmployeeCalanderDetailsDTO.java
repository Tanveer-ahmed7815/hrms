package com.te.flinko.dto.employee;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeCalanderDetailsDTO {
	
	private Double allotedLeaves;

	private Double approvedLeaves;
	
	private Double appliedLeaves;
	
	private Double rejectedLeaves;
	
	private Double remainingLeaves;
	
	List<EmployeeApplyLeaveDTO> calenderLeaves;

}
