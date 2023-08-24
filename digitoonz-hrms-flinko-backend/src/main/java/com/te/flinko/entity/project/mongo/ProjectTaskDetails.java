package com.te.flinko.entity.project.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.audit.Audit;
import com.te.flinko.dto.project.mongo.TaskHistroy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@Document("fa_project_task_details")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTaskDetails extends Audit implements Serializable{
	
	@Id
	private String id;
	
	@Field("ptd_task_id")
	private Long taskId;
	
	@Field("ptd_milestone_id")
	private String mileStoneId;
	
	@Field("ptd_sub_milestone_id")
	private Long subMilestoneId;
	
	@Field("ptd_task_name")
	private String taskName;
	
	@Field("ptd_task_description")
	private String taskDescription;
	
	@Field("ptd_start_date")
	private LocalDateTime startDate;
	
	@Field("ptd_end_date")
	private LocalDateTime endDate;
	
	@Field("ptd_assigned_employee")
	private String assignedEmployee;
	
	@Field("ptd_assigned_date")
	private LocalDate assignedDate;
	
	@Field("ptd_comment")
	private String comment;
	
	@Field("ptd_status")
	private String status;
	
	@Field("ptd_completed_date")
	private LocalDate completedDate;
	
	@Field("ptd_histroy")
	private List<TaskHistroy> taskHistroys;
	
	@Field("ptd_project_id")
    private Long projectId;
	
	@Field("ptd_company_id")
    private Long companyId;
}
