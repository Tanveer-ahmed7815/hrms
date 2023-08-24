package com.te.flinko.service.project;

import java.util.List;

import com.te.flinko.dto.project.mongo.AssignTaskDTO;
import com.te.flinko.dto.project.mongo.TaskDetailsDTO;

public interface TaskService {
	TaskDetailsDTO createtask(TaskDetailsDTO taskDetailsDTO);

	TaskDetailsDTO assignTaskToEmployee(AssignTaskDTO assignTaskDTO);

	List<TaskDetailsDTO> fetchTaskBaseOnId(String employeeId, Long companyId, Long projectId, String status);

	TaskDetailsDTO markAsCompleted(Long companyId, String taskId, Long projectId);

	TaskDetailsDTO unAssignTask(Long companyId, String taskId, Long projectId, String reason);

	List<TaskDetailsDTO> fetchAllTaskBaseOnProjectIdAndStatus(Long projectId, String status, Long companyId, String mileStoneId,Long subMilestoneId);

	TaskDetailsDTO assignTask(Long projectId, String taskId, Long companyId, String assignedEmployee);


	TaskDetailsDTO editTask(TaskDetailsDTO taskDetailsDTO);


}
