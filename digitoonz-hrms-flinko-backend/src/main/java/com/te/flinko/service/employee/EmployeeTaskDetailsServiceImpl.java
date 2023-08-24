package com.te.flinko.service.employee;

import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_ALL;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_COMPLETED;
import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_INPROGRESS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.mongo.EmployeeTaskCommentDTO;
import com.te.flinko.dto.reportingmanager.EmployeeTaskDetailsDTO;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.project.mongo.ProjectTaskDetails;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.project.mongo.ProjectTaskDetailsRepository;

@Service
public class EmployeeTaskDetailsServiceImpl implements EmployeeTaskDetailsService {

	@Autowired
	private ProjectTaskDetailsRepository taskDetailsRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Override
	public List<EmployeeTaskDetailsDTO> getAllTaskDetails(String employeeId, String status, Long companyId) {
		List<EmployeeTaskDetailsDTO> taskList = new ArrayList<>();
		String taskSatus = status.toLowerCase();
		List<ProjectTaskDetails> taskDetails = null;
		if (taskSatus.equals(STATUS_ALL)) {
			taskDetails = taskDetailsRepository.findByAssignedEmployeeAndCompanyId(employeeId, companyId);
		} else if (taskSatus.equals(STATUS_INPROGRESS)) {
			taskDetails = taskDetailsRepository.findByAssignedEmployeeAndStatusAndCompanyId(employeeId,
					STATUS_INPROGRESS, companyId);
		} else if (taskSatus.equals(STATUS_COMPLETED)) {
			taskDetails = taskDetailsRepository.findByAssignedEmployeeAndStatusAndCompanyId(employeeId,
					STATUS_COMPLETED, companyId);
		} else {
			throw new DataNotFoundException("Please provide a valid status");
		}
		HashMap<Long, String> creatorHashMap = new HashMap<>();
		for (ProjectTaskDetails projectTaskDetails : taskDetails) {
			EmployeeTaskDetailsDTO employeeTaskDetailsDTO = new EmployeeTaskDetailsDTO();
			BeanUtils.copyProperties(projectTaskDetails, employeeTaskDetailsDTO);
			if (projectTaskDetails.getCreatedBy() != null) {
				creatorHashMap.put(projectTaskDetails.getCreatedBy(), "");
			}
			taskList.add(employeeTaskDetailsDTO);
		}
		// To fetch createdByName name from MySql
		List<EmployeePersonalInfo> employeePersonalInfos = employeePersonalInfoRepository
				.findByEmployeeInfoIdIn(creatorHashMap.keySet());

		employeePersonalInfos.forEach(
				info -> creatorHashMap.put(info.getEmployeeInfoId(), info.getFirstName() + " " + info.getLastName()));

		List<EmployeeTaskDetailsDTO> taskDeatialsDtoList = taskList.stream().map(task -> {
			if (task.getCreatedBy() != null) {
				task.setCreatedByName(creatorHashMap.get(task.getCreatedBy()));
			}
			return task;
		}).collect(Collectors.toList());
		
		Collections.reverse(taskDeatialsDtoList);
		return taskDeatialsDtoList;
	}

	@Override
	public Boolean editComment(EmployeeTaskCommentDTO detailsDTO) {
		ProjectTaskDetails findByStatus = taskDetailsRepository.findById(detailsDTO.getId())
				.orElseThrow(() -> new DataNotFoundException("Task does not exists!!!"));
		findByStatus.setComment(detailsDTO.getComment());
		taskDetailsRepository.save(findByStatus);
		return Boolean.TRUE;
	}

}
