package com.te.flinko.service.project;

import java.util.ArrayList;
import java.util.List;

import com.te.flinko.dto.employee.EmployeeDropdownDTO;
import com.te.flinko.dto.project.EmployeeDetailsForProjectDTO;
import com.te.flinko.dto.project.MapEmployeeProjectDTO;
import com.te.flinko.dto.project.MilestoneDTO;
import com.te.flinko.dto.project.MilestoneIDNameDTO;
import com.te.flinko.dto.project.ProjectDetailsDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;
import com.te.flinko.dto.project.SubMilestoneDetailsDTO;
import com.te.flinko.dto.project.SubMilestoneIDNameDTO;
import com.te.flinko.dto.project.mongo.ProjectListDTO;

/**
 * 
 * @author Ravindra
 *
 */
public interface ProjectManagementService {

	ArrayList<ProjectListDTO> onGoingProjects(Long companyId);

	public MapEmployeeProjectDTO mapEmployeeWithProject(MapEmployeeProjectDTO mapEmployeeProjectDTO);

	public List<EmployeeDropdownDTO> getListOfEmployeesInProject(Long projectId);

	public MilestoneDTO addMilestone(MilestoneDTO milestone);

	public MilestoneDTO getMilestone(String projectObjectId);

	public ProjectDetailsDTO getProjectDetailsById(Long projectId);

	List<MilestoneIDNameDTO> getProjectMilestonesIDName(Long projectId);

	public MilestoneDTO addSubMilestone(MilestoneDTO milestone);

	ProjectEstimationDTO getProjectEstimationDetailsById(Long projectId);

	List<SubMilestoneIDNameDTO> getSubMilestonesIDName(String mileStoneObjectId);

	SubMilestoneDetailsDTO getSubMilestoneById(String projectObjectId, Long milestoneId);

	List<MilestoneDTO> getAllMilestone(Long projectId);
	
	List<EmployeeDetailsForProjectDTO> getEmployeesInProject(Long projectId);
}
