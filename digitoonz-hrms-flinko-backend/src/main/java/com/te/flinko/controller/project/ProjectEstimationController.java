package com.te.flinko.controller.project;

import static com.te.flinko.common.project.ProjectManagementConstants.PROJECT_ESTIMATION_DETAILS_SUCCESS_MESSAGE;

import org.checkerframework.dataflow.qual.Pure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.project.ProjectDetailsBasicDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.project.ProjectEstimationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/project-estimation")
public class ProjectEstimationController extends BaseConfigController {

	@Autowired
	private ProjectEstimationService projectEstimationService;

	@PostMapping("/")
	public ResponseEntity<SuccessResponse> employeeDocument(@RequestParam String data, @RequestParam MultipartFile file)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

		ProjectEstimationDTO projectEstimationDTO = mapper.readValue(data, ProjectEstimationDTO.class);
		projectEstimationDTO = projectEstimationService.estimate(projectEstimationDTO, file);
		return new ResponseEntity<>(new SuccessResponse(false, "Project Estimated Successfully", projectEstimationDTO),
				HttpStatus.OK);

	}

	@GetMapping(path = "/list-all")
	public ResponseEntity<SuccessResponse> getEstimationList() {
		return new ResponseEntity<>(new SuccessResponse(false, PROJECT_ESTIMATION_DETAILS_SUCCESS_MESSAGE,
				projectEstimationService.getAllProjectDetailsEstimation(getCompanyId())), HttpStatus.OK);
	}

	@GetMapping(path = "")
	public ResponseEntity<SuccessResponse> getProjectEstimation(@RequestParam Long projectId) {

		SuccessResponse successResponse = new SuccessResponse(false, PROJECT_ESTIMATION_DETAILS_SUCCESS_MESSAGE,
				projectEstimationService.getProjectDetailsEstimation(projectId));
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}
	
	@PutMapping(path = "/{projectId}/{status}")
	public ResponseEntity<SuccessResponse> updateStatus(@PathVariable Long projectId, @PathVariable String status) {
		String updatedStatus = projectEstimationService.updateStatus(projectId, status);
		SuccessResponse successResponse = new SuccessResponse(false, updatedStatus ,
				updatedStatus);
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}


	@GetMapping(path = "/by-project/{projectId}")
	public ResponseEntity<SuccessResponse> getEstimationByProject(@PathVariable Long projectId) {
		 ProjectEstimationDTO estimationByProject = projectEstimationService
				.getEstimationByProject(projectId);
		if (estimationByProject != null) {
			SuccessResponse successResponse = new SuccessResponse(false, "Estimation Fetched Successfully",
					estimationByProject);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		}
		SuccessResponse successResponse = new SuccessResponse(false, "Project is Not Estimated",
				estimationByProject);
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}

}
