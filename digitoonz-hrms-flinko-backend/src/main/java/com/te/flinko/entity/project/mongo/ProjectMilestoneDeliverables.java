package com.te.flinko.entity.project.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.dto.project.mongo.SubMilestone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("fa_project_milestone_deliverables")
public class ProjectMilestoneDeliverables implements Serializable {

	@Id
	private String mileStoneObjectId;
	
	@Field("pmd_milestone_id")
	private Long milestoneId;
	
	@Field("pmd_project_id")
	private Long projectId;
	
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
	
	@Field("pmd_sub_milestones")
	private List<SubMilestone> subMilestones;
}
