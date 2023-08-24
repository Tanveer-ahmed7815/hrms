package com.te.flinko.controller.project;

import static com.te.flinko.common.hr.ProjectConstants.PROJECT_INFORMATION_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.project.ProjectManagementConstants.PROJECT_SUBMILESTONES_PROVIDED;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.employee.EmployeeDropdownDTO;
import com.te.flinko.dto.project.EmployeeDetailsForProjectDTO;
import com.te.flinko.dto.project.MapEmployeeProjectDTO;
import com.te.flinko.dto.project.MilestoneDTO;
import com.te.flinko.dto.project.ProjectDetailsDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;
import com.te.flinko.dto.project.SubMilestoneDetailsDTO;
import com.te.flinko.dto.project.mongo.ProjectListDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.project.ProjectManagementService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Ravindra
 *
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/project")
public class ProjectManagementController extends BaseConfigController {
	@Autowired
	ProjectManagementService projectManagerService;

	@GetMapping(path = "/milestones/{projectId}")
	public ResponseEntity<SuccessResponse> getProjectMilestonesIDName(@PathVariable Long projectId) {
		log.info("getProjectMilestonesIDName method execution start");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Milestone Fethced successsfully")
						.data(projectManagerService.getProjectMilestonesIDName(projectId)).build());
	}

	@GetMapping(path = "/milestone/submilestones/{mileStoneObjectId}")
	public ResponseEntity<SuccessResponse> getSubMilestonesIDName(@PathVariable String mileStoneObjectId) {
		log.info("getSubMilestonesIDName method execution start");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(PROJECT_SUBMILESTONES_PROVIDED)
						.data(projectManagerService.getSubMilestonesIDName(mileStoneObjectId)).build());
	}

	@GetMapping("/onGoingProjects")
	public ResponseEntity<SuccessResponse> onGoingProjects() {
		ArrayList<ProjectListDTO> onGoingProjects = projectManagerService.onGoingProjects(getCompanyId());
		return new ResponseEntity<>(
				new SuccessResponse(false, PROJECT_INFORMATION_FETCHED_SUCCESSFULLY, onGoingProjects), HttpStatus.OK);
	}

	@PostMapping("/employees")
	public ResponseEntity<SuccessResponse> addEmmployeeToProject(
			@RequestBody MapEmployeeProjectDTO employeeProjectDto) {
		MapEmployeeProjectDTO mapEmployeeWithProject = projectManagerService.mapEmployeeWithProject(employeeProjectDto);
		if (mapEmployeeWithProject == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).data(null).message("Data not found").build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).data(mapEmployeeWithProject)
				.message("Details updated successfully").build());
	}
	
	@GetMapping("/employees/{projectId}")
	public ResponseEntity<SuccessResponse> getEmmployeeInProject(@PathVariable Long projectId) {
		List<EmployeeDetailsForProjectDTO> employeeList = projectManagerService.getEmployeesInProject(projectId);
		if (employeeList == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).data(null).message("No Employee is Mapped to Project").build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).data(employeeList)
				.message("Details fetched successfully").build());
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<SuccessResponse> getListOfEmployeesInProject(@PathVariable Long projectId) {
		List<EmployeeDropdownDTO> listOfEmployeesInProject = projectManagerService
				.getListOfEmployeesInProject(projectId);
		if (listOfEmployeesInProject == null) {
			return ResponseEntity.ok(SuccessResponse.builder().error(true).message("Data not Found")
					.data(listOfEmployeesInProject).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).data(listOfEmployeesInProject)
				.message("Project Details found Successfully").build());
	}

	@PostMapping("/milestone")
	public ResponseEntity<SuccessResponse> addMilestone(@RequestBody MilestoneDTO milestone) {
		MilestoneDTO addMilestone = projectManagerService.addMilestone(milestone);
		if (addMilestone == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Add Delevriables successfully.")
				.data(addMilestone).build());
	}

	@PostMapping("/submilestone")
	public ResponseEntity<SuccessResponse> addSubMilestone(@RequestBody MilestoneDTO milestone) {
		MilestoneDTO addMilestone = projectManagerService.addSubMilestone(milestone);
		if (addMilestone == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Add Delevriables successfully.")
				.data(addMilestone).build());
	}

	@GetMapping("/milestone/{projectObjectId}")
	public ResponseEntity<SuccessResponse> getMilestone(@PathVariable String projectObjectId) {
		MilestoneDTO addMilestone = projectManagerService.getMilestone(projectObjectId);
		if (addMilestone == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Fetched Delevriables successfully.")
				.data(addMilestone).build());
	}

	@GetMapping("/submilestone/{projectObjectId}/{milestoneId}")
	public ResponseEntity<SuccessResponse> getSubMilestone(@PathVariable String projectObjectId,@PathVariable Long milestoneId) {
		 SubMilestoneDetailsDTO subMilestoneDetailsDTO = projectManagerService.getSubMilestoneById(projectObjectId,milestoneId);
		if (subMilestoneDetailsDTO == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Fetched Delevriables successfully.")
				.data(subMilestoneDetailsDTO).build());
	}

	@GetMapping("/detials/{projectId}")
	public ResponseEntity<SuccessResponse> getProjectDetailsById(@PathVariable Long projectId) {
		ProjectDetailsDTO projectDetails = projectManagerService.getProjectDetailsById(projectId);
		if (projectDetails == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Fetched Delevriables successfully.")
				.data(projectDetails).build());
	}

	@GetMapping("/estimationdetails/{projectId}")
	public ResponseEntity<SuccessResponse> getProjectEstimationDetails(@PathVariable Long projectId) {
		ProjectEstimationDTO projectEstimationDetailsById = projectManagerService
				.getProjectEstimationDetailsById(projectId);
		if (projectEstimationDetailsById == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(true).message("Data not found").data(null).build());
		}
		return ResponseEntity
				.ok(SuccessResponse.builder().error(false).message("Fetched Project Estimation Details successfully.")
						.data(projectEstimationDetailsById).build());
	}
	
	@GetMapping("/milestone/by-project-id/{projectId}")
	public ResponseEntity<SuccessResponse> getAllMilestone(@PathVariable Long projectId){
		List<MilestoneDTO> allMilestone = projectManagerService.getAllMilestone(projectId);
		if(allMilestone == null)
			return ResponseEntity.ok(SuccessResponse.builder().error(true).message("Data Not Found").data(null).build());
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Details fetched successfully").data(allMilestone).build());
	}

}
