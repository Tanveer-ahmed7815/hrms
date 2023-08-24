package com.te.flinko.dto.project.mongo;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubMilestone implements Serializable{
	
	@Field("pmd_milestone_id")
	private Long milestoneId;
	
	@Field("pmd_milestone_name")
	private String milestoneName;
	
	@Field("pmd_milestone_description")
    public String milestoneDescription;
	
	@Field("pmd_due_date")
	private LocalDate dueDate;
	
	@Field("pmd_amount_to_be_received")
	private Double amountToBeReceived;
	
	@Field("pmd_delivered_date")
	private LocalDate deliveredDate;
	
	@Field("pmd_amount_received")
	private Double amountReceived;
	
	@Field("pmd_status")
	private String status;
	
	@Field("pmd_client_feedback")
    private String clientFeedback;
	
}
