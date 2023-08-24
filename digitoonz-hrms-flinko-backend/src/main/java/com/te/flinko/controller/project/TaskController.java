package com.te.flinko.controller.project;

import static com.te.flinko.common.hr.ProjectConstants.EMPLOYEE_TASK_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.ProjectConstants.FETCHED_TASKS_SUCCESSFULLY;
import static com.te.flinko.common.hr.ProjectConstants.SUCCESSFULLY_MARK_AS_COMPLETED;
import static com.te.flinko.common.hr.ProjectConstants.SUCCESSFULLY_UNASSIGN_THE_TASK;
import static com.te.flinko.common.hr.ProjectConstants.TASK_CREATED_SUCCEESSFULLY;
import static com.te.flinko.common.hr.ProjectConstants.TASK_SUCCESSFULLY_ASSIGN_TO_EMPLOYEE;
import static com.te.flinko.common.hr.ProjectConstants.TASK_UPDATED_SUCCEESSFULLY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.project.mongo.AssignTaskDTO;
import com.te.flinko.dto.project.mongo.TaskDetailsDTO;
import com.te.flinko.dto.project.mongo.UnAssignTaskDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.project.TaskService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/task")
public class TaskController extends BaseConfigController {
	@Autowired
	TaskService taskService;

	@PostMapping("/")
	public ResponseEntity<SuccessResponse> createtask(@RequestBody TaskDetailsDTO taskDetailsDTO) {
		TaskDetailsDTO createtask = taskService.createtask(taskDetailsDTO);
		return new ResponseEntity<>(new SuccessResponse(false, TASK_CREATED_SUCCEESSFULLY, createtask),
				HttpStatus.CREATED);
	}

	@PostMapping("/assign")
	public ResponseEntity<SuccessResponse> assignTaskToEmployee(@RequestBody AssignTaskDTO assignTaskDTO) {
		TaskDetailsDTO assignTaskToEmployee = taskService.assignTaskToEmployee(assignTaskDTO);
		return new ResponseEntity<>(
				new SuccessResponse(false, TASK_SUCCESSFULLY_ASSIGN_TO_EMPLOYEE, assignTaskToEmployee), HttpStatus.OK);
	}

	@GetMapping("/by-empid")
	public ResponseEntity<SuccessResponse> fetchTaskBaseOnId(@RequestParam String employeeId,
			@RequestParam Long projectId, @RequestParam String status) {
		List<TaskDetailsDTO> fetchTaskBaseOnId = taskService.fetchTaskBaseOnId(employeeId, getCompanyId(),
				projectId, status);

		return new ResponseEntity<>(new SuccessResponse(false, EMPLOYEE_TASK_FETCHED_SUCCESSFULLY, fetchTaskBaseOnId),
				HttpStatus.OK);
	}

	@PostMapping("/mark-completed")
	public ResponseEntity<SuccessResponse> markAsCompleted(@RequestParam String taskId, @RequestParam Long projectId) {
		TaskDetailsDTO markAsCompleted = taskService.markAsCompleted(getCompanyId(), taskId, projectId);
		return new ResponseEntity<>(new SuccessResponse(false, SUCCESSFULLY_MARK_AS_COMPLETED, markAsCompleted),
				HttpStatus.OK);

	}

	@PostMapping("/unassign")
	public ResponseEntity<SuccessResponse> unAssignTask(@RequestBody UnAssignTaskDTO unAssignTaskDTO) {
		TaskDetailsDTO unAssignTask = taskService.unAssignTask(getCompanyId(), unAssignTaskDTO.getTaskId(), unAssignTaskDTO.getProjectId(), unAssignTaskDTO.getReason());
		return new ResponseEntity<>(new SuccessResponse(false, SUCCESSFULLY_UNASSIGN_THE_TASK, unAssignTask),
				HttpStatus.OK);
	}

	@GetMapping("/all-by-project-id-status")
	public ResponseEntity<SuccessResponse> fetchAllTaskBaseOnProjectIdAndStatus(@RequestParam Long projectId,
			@RequestParam String status, @RequestParam String mileStoneId, @RequestParam Long subMilestoneId) {
		List<TaskDetailsDTO> tasklist = taskService.fetchAllTaskBaseOnProjectIdAndStatus(projectId, status,
				getCompanyId(), mileStoneId, subMilestoneId);
		return new ResponseEntity<>(new SuccessResponse(false, FETCHED_TASKS_SUCCESSFULLY, tasklist), HttpStatus.OK);
	}

	@PostMapping("/assign-by-taskid")
	public ResponseEntity<SuccessResponse> assignTask(@RequestParam Long projectId, @RequestParam String taskId,
			@RequestParam String assignedEmployee) {
		TaskDetailsDTO assignTask = taskService.assignTask(projectId, taskId, getCompanyId(),
				assignedEmployee);

		return new ResponseEntity<>(new SuccessResponse(false, TASK_SUCCESSFULLY_ASSIGN_TO_EMPLOYEE, assignTask),
				HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<SuccessResponse> editTask(@RequestBody TaskDetailsDTO taskDetailsDTO) {
		TaskDetailsDTO editTask = taskService.editTask(taskDetailsDTO);
		return new ResponseEntity<>(new SuccessResponse(false, TASK_UPDATED_SUCCEESSFULLY, editTask), HttpStatus.OK);
	}
}
