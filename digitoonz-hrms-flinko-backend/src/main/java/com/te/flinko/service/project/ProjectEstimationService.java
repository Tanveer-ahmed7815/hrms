package com.te.flinko.service.project;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.project.ProjectDetailsBasicDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;

public interface ProjectEstimationService {

	ProjectEstimationDTO estimate(ProjectEstimationDTO projectEstimationDTO, MultipartFile file);

	List<ProjectDetailsBasicDTO> getAllProjectDetailsEstimation(Long companyId);

	ProjectDetailsBasicDTO getProjectDetailsEstimation(Long projectId);
	
	ProjectEstimationDTO getEstimationByProject(Long projectId);
	
	String updateStatus(Long projectId, String status);

}
