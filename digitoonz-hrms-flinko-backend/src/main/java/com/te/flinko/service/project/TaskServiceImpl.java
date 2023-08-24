package com.te.flinko.service.project;

import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.ProjectConstants.COMPLETED;
import static com.te.flinko.common.hr.ProjectConstants.IN_PROGRESS;
import static com.te.flinko.common.hr.ProjectConstants.PROJECT_DETAILS_NOT_FOUND;
import static com.te.flinko.common.hr.ProjectConstants.PROJECT_INFORMATION_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.ProjectConstants.TASK_DETAILS_NOT_FOUND;
import static com.te.flinko.common.hr.ProjectConstants.TASK_UPDATED_SUCCEESSFULLY;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_INPROGRESS;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_COMPLETED;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_UNASSIGNED;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_ALL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.project.mongo.AssignTaskDTO;
import com.te.flinko.dto.project.mongo.TaskDetailsDTO;
import com.te.flinko.dto.project.mongo.TaskHistroy;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.project.mongo.ProjectMilestoneDeliverables;
import com.te.flinko.entity.project.mongo.ProjectTaskDetails;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.project.ProjectDetailsRepository;
import com.te.flinko.repository.project.ProjectEstimationDetailsRepository;
import com.te.flinko.repository.project.mongo.MilestoneRepository;
import com.te.flinko.repository.project.mongo.ProjectTaskDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private ProjectTaskDetailsRepository projectTaskDetailsRepo;
	@Autowired
	private CompanyInfoRepository companyInfoRepo;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepo;
	@Autowired
	private MilestoneRepository milestoneRepo;

	@Override
	public TaskDetailsDTO createtask(TaskDetailsDTO taskDetailsDTO) {
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		ProjectDetails projectDetails = projectDetailsRepository.findById(taskDetailsDTO.getProjectId())
				.orElseThrow(() -> new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND));
		TaskDetailsDTO taskDetailDTO = new TaskDetailsDTO();

		ProjectMilestoneDeliverables milestone = milestoneRepo.findById(taskDetailsDTO.getMileStoneId())
				.orElseThrow(() -> new CustomExceptionForHr("Given milestone does not exists!!!"));
		if (taskDetailsDTO.getSubMilestoneId() != null) {
			if (milestone.getSubMilestones() != null) {

				boolean subMilestoneStatus = milestone.getSubMilestones().stream().anyMatch(submilestone -> (Objects
						.equals(submilestone.getMilestoneId(), taskDetailsDTO.getSubMilestoneId())));
				if (!subMilestoneStatus) {
					throw new CustomExceptionForHr("Given Sub-Milestone does not exists!!!");
				}
			} else {
				throw new CustomExceptionForHr("No Sub-Milestones exists for current Milestone");
			}
		}

		if ((projectDetails.getCompanyInfo().getCompanyId().equals(taskDetailsDTO.getCompanyId())
				&& (projectDetails.getProjectId().equals(taskDetailsDTO.getProjectId())))) {
			log.info(PROJECT_INFORMATION_FETCHED_SUCCESSFULLY);
			ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
			BeanUtils.copyProperties(taskDetailsDTO, projectTaskDetails);
			projectTaskDetails.setStatus(STATUS_UNASSIGNED);
			ProjectTaskDetails savedTaskDetails = projectTaskDetailsRepo.save(projectTaskDetails);

			BeanUtils.copyProperties(savedTaskDetails, taskDetailDTO);
			taskDetailDTO.setCompanyId(taskDetailsDTO.getCompanyId());
			taskDetailDTO.setProjectId(projectDetails.getProjectId());
			log.info("Task created Successfully");
		} else {
			throw new CustomExceptionForHr("Project id and company id not match");
		}
		return taskDetailDTO;
	}

	@Override
	public TaskDetailsDTO assignTaskToEmployee(AssignTaskDTO assignTaskDTO) {
		TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
		ProjectDetails projectDetails = projectDetailsRepository.findById(assignTaskDTO.getProjectId())
				.orElseThrow(() -> new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND));
		log.info(PROJECT_INFORMATION_FETCHED_SUCCESSFULLY);
		ProjectMilestoneDeliverables milestone = milestoneRepo.findById(assignTaskDTO.getMileStoneId())
				.orElseThrow(() -> new CustomExceptionForHr("Given milestone does not exists!!!"));
		if (taskDetailsDTO.getSubMilestoneId() != null) {
			if (milestone.getSubMilestones() != null) {

				boolean subMilestoneStatus = milestone.getSubMilestones().stream().anyMatch(submilestone -> (Objects
						.equals(submilestone.getMilestoneId(), taskDetailsDTO.getSubMilestoneId())));
				if (!subMilestoneStatus) {
					throw new CustomExceptionForHr("Given Sub-Milestone does not exists!!!");
				}
			} else {
				throw new CustomExceptionForHr("No Sub-Milestones exists for current Milestone");
			}
		}

		List<EmployeePersonalInfo> personalInfos = employeePersonalInfoRepo
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(assignTaskDTO.getCompanyId(),
						assignTaskDTO.getAssignedEmployee())
				.orElseThrow(() -> new CustomExceptionForHr("Employee details not found"));
		EmployeePersonalInfo personalInfo = personalInfos.get(0);
		if (projectDetails.getCompanyInfo().getCompanyId().equals(assignTaskDTO.getCompanyId())) {
			ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();
			BeanUtils.copyProperties(assignTaskDTO, projectTaskDetails);
			projectTaskDetails.setStatus(STATUS_INPROGRESS);
			projectTaskDetails.setAssignedDate(LocalDate.now());
			ProjectTaskDetails projectTaskDetail = projectTaskDetailsRepo.save(projectTaskDetails);

			BeanUtils.copyProperties(projectTaskDetail, taskDetailsDTO);
			taskDetailsDTO.setCompanyId(assignTaskDTO.getCompanyId());
			taskDetailsDTO.setProjectId(projectDetails.getProjectId());

			taskDetailsDTO.setAssignedEmployeeName(personalInfo.getFirstName() + " " + personalInfo.getLastName());

			log.info("Task assign Successfully");
		} else {
			throw new CustomExceptionForHr("No such project found");
		}
		return taskDetailsDTO;
	}

	@Override
	public TaskDetailsDTO markAsCompleted(Long companyId, String taskId, Long projectId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		List<ProjectDetails> projectinfo = projectDetailsRepository.findByCompanyInfoCompanyIdAndProjectId(companyId,
				projectId);
		if (projectinfo == null || projectinfo.isEmpty()) {
			throw new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND);
		}
		ProjectDetails projectDetails = projectinfo.get(0);
		List<ProjectTaskDetails> findByProjectIdAndId = projectTaskDetailsRepo
				.findByProjectIdAndId(projectDetails.getProjectId(), taskId);
		if (findByProjectIdAndId == null || findByProjectIdAndId.isEmpty()
				|| findByProjectIdAndId.get(0).getAssignedEmployee() == null) {
			throw new CustomExceptionForHr(TASK_DETAILS_NOT_FOUND);
		}
		ProjectTaskDetails projectTaskDetails = findByProjectIdAndId.get(0);
		if (projectTaskDetails.getStatus().equalsIgnoreCase(STATUS_COMPLETED)) {
			throw new CustomExceptionForHr("Task is already completed");
		}
		projectTaskDetails.setStatus(STATUS_COMPLETED);
		projectTaskDetails.setCompletedDate(LocalDate.now());
		ProjectTaskDetails taskinfo = projectTaskDetailsRepo.save(projectTaskDetails);
		TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
		BeanUtils.copyProperties(taskinfo, taskDetailsDTO);
		taskDetailsDTO.setCompanyId(companyInfo.getCompanyId());
		taskDetailsDTO.setId(projectTaskDetails.getId());
		taskDetailsDTO.setProjectId(projectDetails.getProjectId());
		if (projectTaskDetails.getAssignedEmployee() != null) {
			List<EmployeePersonalInfo> employeeName = employeePersonalInfoRepo
					.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId,
							projectTaskDetails.getAssignedEmployee())
					.orElseThrow(() -> new CustomExceptionForHr("Employee details not found"));
			if ((employeeName != null) && (!employeeName.isEmpty())) {
				taskDetailsDTO.setAssignedEmployeeName(
						employeeName.get(0).getFirstName() + " " + employeeName.get(0).getLastName());
			}
		}
		log.info("successfully mark as completed the task");
		return taskDetailsDTO;
	}

	@Override
	public TaskDetailsDTO unAssignTask(Long companyId, String taskId, Long projectId, String reason) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		List<ProjectDetails> projectInfo = projectDetailsRepository.findByCompanyInfoCompanyIdAndProjectId(companyId,
				projectId);
		if (projectInfo == null || projectInfo.isEmpty()) {
			throw new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND);
		}
		List<ProjectTaskDetails> findByProjectIdAndId = projectTaskDetailsRepo.findByProjectIdAndId(projectId, taskId);
		if (findByProjectIdAndId == null || findByProjectIdAndId.isEmpty()) {
			throw new CustomExceptionForHr(TASK_DETAILS_NOT_FOUND);
		}
		ProjectTaskDetails projectTaskDetails = findByProjectIdAndId.get(0);
		if (projectTaskDetails.getAssignedEmployee() == null) {
			throw new CustomExceptionForHr("Given task is not assigned to anyone please assigned first");
		}
		if (projectTaskDetails.getStatus().equalsIgnoreCase(STATUS_COMPLETED)) {
			throw new CustomExceptionForHr("Given task is already completed not able to unassign task");
		}
		TaskHistroy taskHistroy = new TaskHistroy();
		taskHistroy.setEmployeeId(projectTaskDetails.getAssignedEmployee());
		taskHistroy.setUnassigningReason(taskId);
		taskHistroy.setUnassigningDate(LocalDate.now());
		taskHistroy.setUnassigningReason(reason);
		ArrayList<TaskHistroy> taskhistory = new ArrayList<>();
		taskhistory.add(taskHistroy);
		projectTaskDetails.setTaskHistroys(taskhistory);
		projectTaskDetails.setAssignedEmployee(null);
		projectTaskDetails.setStatus("unassigned");
		ProjectTaskDetails save = projectTaskDetailsRepo.save(projectTaskDetails);

		TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
		BeanUtils.copyProperties(save, taskDetailsDTO);
		taskDetailsDTO.setCompanyId(companyInfo.getCompanyId());
		taskDetailsDTO.setProjectId(projectInfo.get(0).getProjectId());
		log.info("Successfullly unassigned the task");
		return taskDetailsDTO;

	}

	@Override
	public List<TaskDetailsDTO> fetchAllTaskBaseOnProjectIdAndStatus(Long projectId, String status, Long companyId,
			String mileStoneId, Long subMilestoneId) {
		List<ProjectDetails> projectInfo = projectDetailsRepository.findByCompanyInfoCompanyIdAndProjectId(companyId,
				projectId);
		if (projectInfo == null || projectInfo.isEmpty()) {
			throw new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND);
		}
		log.info(PROJECT_INFORMATION_FETCHED_SUCCESSFULLY);
		if (mileStoneId == null) {
			throw new CustomExceptionForHr("Please provide a Milestone");
		}

		final List<ProjectTaskDetails> findByProjectIdAndStatus;

		List<String> statusList = new ArrayList<>();

		switch (status.toLowerCase()) {
		case STATUS_INPROGRESS:
			statusList.add(STATUS_INPROGRESS);
			break;
		case STATUS_COMPLETED:
			statusList.add(STATUS_COMPLETED);
			break;
		case STATUS_UNASSIGNED:
			statusList.add(STATUS_UNASSIGNED);
			break;
		case STATUS_ALL:
			statusList.add(STATUS_INPROGRESS);
			statusList.add(STATUS_COMPLETED);
			statusList.add(STATUS_UNASSIGNED);
			break;
		default:
			throw new CustomExceptionForHr("Please provide a valid Status");
		}

		if (subMilestoneId != 0) {
			findByProjectIdAndStatus = projectTaskDetailsRepo.findByProjectIdAndStatusInAndMileStoneIdAndSubMilestoneId(
					projectId, statusList, mileStoneId, subMilestoneId);
		} else {
			findByProjectIdAndStatus = projectTaskDetailsRepo.findByProjectIdAndStatusInAndMileStoneId(projectId,
					statusList, mileStoneId);
		}

		ProjectMilestoneDeliverables milestone = milestoneRepo.findById(mileStoneId)
				.orElseThrow(() -> new CustomExceptionForHr("Milestone does not exist!!!"));

		ArrayList<TaskDetailsDTO> emptyTaskList = new ArrayList<>();
		if (findByProjectIdAndStatus == null || findByProjectIdAndStatus.isEmpty()) {
			return emptyTaskList;
		}
		List<TaskDetailsDTO> taskDetailsDTOList = findByProjectIdAndStatus.stream().map(a -> {
			TaskDetailsDTO details = new TaskDetailsDTO();
			BeanUtils.copyProperties(a, details);
			if (a.getStatus().equalsIgnoreCase(COMPLETED) || (a.getStatus().equalsIgnoreCase(IN_PROGRESS))) {
				List<EmployeePersonalInfo> employeeDetails = employeePersonalInfoRepo
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId, a.getAssignedEmployee())
						.orElseThrow(() -> new CustomExceptionForHr("Employee details not found"));
				if ((employeeDetails != null) && (!employeeDetails.isEmpty())) {
					details.setAssignedEmployeeName(
							employeeDetails.get(0).getFirstName() + " " + employeeDetails.get(0).getLastName());
				}
			}
			details.setMileStoneName(milestone.getMilestoneName());
			if (details.getSubMilestoneId() != 0) {
				details.setSubMilestoneName(milestone.getSubMilestones().stream()
						.filter(e -> Objects.equals(e.getMilestoneId(), details.getSubMilestoneId())).collect(Collectors.toList())
						.get(0).getMilestoneName());
			}
			details.setCompanyId(companyId);
			details.setProjectId(findByProjectIdAndStatus.get(0).getProjectId());
			EmployeePersonalInfo employeeDetails = employeePersonalInfoRepo.findById(a.getCreatedBy()).orElse(null);
			if(employeeDetails!= null) {
			details.setCreatedByName(employeeDetails.getFirstName()+" "+employeeDetails.getLastName());
		}
			return details;
		}).collect(Collectors.toList());
		
		Collections.reverse(taskDetailsDTOList);
		return taskDetailsDTOList;

	}

	@Override
	public TaskDetailsDTO assignTask(Long projectId, String taskId, Long companyId, String assignedEmployee) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND));
		log.info(PROJECT_INFORMATION_FETCHED_SUCCESSFULLY);
		List<ProjectTaskDetails> findByProjectIdAndTaskId = projectTaskDetailsRepo.findByProjectIdAndId(projectId,
				taskId);

		if (findByProjectIdAndTaskId == null || findByProjectIdAndTaskId.isEmpty()) {
			log.info(TASK_DETAILS_NOT_FOUND);
			throw new CustomExceptionForHr("Task details not present");
		}

		ProjectTaskDetails projectTaskDetails = findByProjectIdAndTaskId.get(0);
		if (projectTaskDetails.getStatus().equalsIgnoreCase(COMPLETED)) {
			throw new CustomExceptionForHr("Given task is completed not able to assign task to employee");
		}
		if (projectTaskDetails.getAssignedEmployee() != null) {
			throw new CustomExceptionForHr(
					"Given task is already assign to " + projectTaskDetails.getAssignedEmployee());
		}
		List<EmployeePersonalInfo> personalDetail = employeePersonalInfoRepo
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId, assignedEmployee)
				.orElseThrow(() -> new CustomExceptionForHr("Employee details not found"));
		if (personalDetail == null && (personalDetail.isEmpty())) {
			throw new CustomExceptionForHr("Employee details not found");
		}
		EmployeePersonalInfo personalInfo = personalDetail.get(0);
		TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
		if ((personalInfo.getCompanyInfo().getCompanyId().equals(companyInfo.getCompanyId()))
				&& (projectTaskDetails.getProjectId().equals(projectId))) {
			projectTaskDetails.setAssignedEmployee(assignedEmployee);
			projectTaskDetails.setStatus(IN_PROGRESS);
			projectTaskDetails.setAssignedDate(LocalDate.now());
			ProjectTaskDetails projectTaskDetail = projectTaskDetailsRepo.save(projectTaskDetails);

			BeanUtils.copyProperties(projectTaskDetail, taskDetailsDTO);
			taskDetailsDTO.setCompanyId(companyInfo.getCompanyId());
			taskDetailsDTO.setProjectId(projectDetails.getProjectId());
			taskDetailsDTO.setAssignedEmployeeName(personalInfo.getFirstName() + " " + personalInfo.getLastName());

		}
		log.info("Task assign Successfully");
		return taskDetailsDTO;
	}

	@Override
	public TaskDetailsDTO editTask(TaskDetailsDTO taskDetailsDTO) {
		CompanyInfo companyInfo = companyInfoRepo.findById(taskDetailsDTO.getCompanyId())
				.orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		List<ProjectDetails> projectId = projectDetailsRepository
				.findByCompanyInfoCompanyIdAndProjectId(companyInfo.getCompanyId(), taskDetailsDTO.getProjectId());
		if (projectId == null || projectId.isEmpty()) {
			throw new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND);
		}
		log.info(PROJECT_INFORMATION_FETCHED_SUCCESSFULLY);
		List<ProjectTaskDetails> findByProjectIdAndId = projectTaskDetailsRepo
				.findByProjectIdAndId(taskDetailsDTO.getProjectId(), taskDetailsDTO.getId());
		if (findByProjectIdAndId == null || findByProjectIdAndId.isEmpty()) {
			throw new CustomExceptionForHr(TASK_DETAILS_NOT_FOUND);
		}

		ProjectTaskDetails projectTaskDetails = findByProjectIdAndId.get(0);
		if (projectTaskDetails.getStatus().equalsIgnoreCase(COMPLETED)) {
			log.info("Task is already completed cant update the task");
			throw new CustomExceptionForHr("Given task is already completed");
		}
		projectTaskDetails.setTaskName(taskDetailsDTO.getTaskName());
		projectTaskDetails.setMileStoneId(taskDetailsDTO.getMileStoneId());
		projectTaskDetails.setSubMilestoneId(taskDetailsDTO.getSubMilestoneId());
		projectTaskDetails.setTaskDescription(taskDetailsDTO.getTaskDescription());
		projectTaskDetails.setStartDate(taskDetailsDTO.getStartDate());
		projectTaskDetails.setEndDate(taskDetailsDTO.getEndDate());
		ProjectTaskDetails save = projectTaskDetailsRepo.save(projectTaskDetails);
		TaskDetailsDTO taskDetailsDTO2 = new TaskDetailsDTO();
		if (projectTaskDetails.getAssignedEmployee() != null) {
			taskDetailsDTO2.setAssignedEmployeeName(save.getAssignedEmployee());
		}
		BeanUtils.copyProperties(save, taskDetailsDTO2);
		taskDetailsDTO2.setCompanyId(companyInfo.getCompanyId());
		taskDetailsDTO2.setProjectId(projectId.get(0).getProjectId());
		log.info(TASK_UPDATED_SUCCEESSFULLY);
		return taskDetailsDTO2;
	}

	@Override
	public List<TaskDetailsDTO> fetchTaskBaseOnId(String employeeId, Long companyId, Long projectId, String status) {
		ArrayList<TaskDetailsDTO> arrayList = new ArrayList<>();
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		List<EmployeePersonalInfo> employeedetails = employeePersonalInfoRepo
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyInfo.getCompanyId(), employeeId)
				.orElseThrow(() -> new CustomExceptionForHr("Employee details not found"));
		if (employeedetails.isEmpty()) {
			log.error("Employee Details not found");
			throw new CustomExceptionForHr("Employee details not found for " + employeeId + " id");
		}
		List<ProjectDetails> projectInfo = projectDetailsRepository.findByCompanyInfoCompanyIdAndProjectId(companyId,
				projectId);
		if (projectInfo == null || projectInfo.isEmpty()) {
			log.error(PROJECT_DETAILS_NOT_FOUND);
			throw new CustomExceptionForHr(PROJECT_DETAILS_NOT_FOUND);
		}
		if (status.equalsIgnoreCase("all")) {
			List<ProjectTaskDetails> taskDetail = projectTaskDetailsRepo.findByAssignedEmployeeAndProjectId(employeeId,
					projectId);
			if (taskDetail == null || taskDetail.isEmpty()) {
				return arrayList;
			}
			for (ProjectTaskDetails task : taskDetail) {
				arrayList.add(new TaskDetailsDTO(task.getId(), task.getMileStoneId(), null, task.getSubMilestoneId(),
						null, task.getTaskName(), task.getTaskDescription(), null, null, null, task.getStartDate(), task.getEndDate(),
						task.getAssignedDate(), task.getStatus(), task.getCompletedDate(), companyInfo.getCompanyId(), task.getProjectId(),
						employeedetails.get(0).getFirstName() + " " + employeedetails.get(0).getLastName()));
			}

		}
		List<ProjectTaskDetails> taskDetail = projectTaskDetailsRepo
				.findByAssignedEmployeeAndProjectIdAndStatus(employeeId, projectId, status);

		if (taskDetail == null || taskDetail.isEmpty()) {
			log.info("empty list" + taskDetail);
			return arrayList;
		}
		for (ProjectTaskDetails taskInfo : taskDetail) {
			arrayList.add(new TaskDetailsDTO(taskInfo.getId(), taskInfo.getMileStoneId(), null,
					taskInfo.getSubMilestoneId(), null, taskInfo.getTaskName(), taskInfo.getTaskDescription(), null, null, null,
					taskInfo.getStartDate(), taskInfo.getEndDate(), taskInfo.getAssignedDate(), taskInfo.getStatus(),  taskInfo.getCompletedDate(), 
					companyInfo.getCompanyId(), taskInfo.getProjectId(), taskInfo.getAssignedEmployee()));
		}
		return arrayList;
	}

}
