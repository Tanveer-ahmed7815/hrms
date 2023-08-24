package com.te.flinko.dto.project;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubMilestoneDetailsDTO {
	
	private String parentMilestoneObjectId;
	
	private String parentMilestoneName;
	
	private Long milestoneId;
	
	private String milestoneName;
	
    private String milestoneDescription;
	
	private LocalDate dueDate;
	
	private Double amountToBeReceived;
	
	private LocalDate deliveredDate;
	
	private Double amountReceived;
	
	private String status;
	
    private String clientFeedback;

}
