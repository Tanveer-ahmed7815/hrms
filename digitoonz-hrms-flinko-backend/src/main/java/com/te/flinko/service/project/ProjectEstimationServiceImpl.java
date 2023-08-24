package com.te.flinko.service.project;

import static com.te.flinko.common.project.ProjectManagementConstants.STATUS_NOT_ESTIMATED;
import static com.te.flinko.common.project.ProjectManagementConstants.APPROVED;
import static com.te.flinko.common.project.ProjectManagementConstants.REJECTED;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.project.ProjectDetailsBasicDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.project.ProjectEstimationDetails;
import com.te.flinko.entity.sales.CompanyClientInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.project.ProjectDetailsRepository;
import com.te.flinko.repository.project.ProjectEstimationDetailsRepository;
import com.te.flinko.util.S3UploadFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectEstimationServiceImpl implements ProjectEstimationService {

	@Autowired
	private S3UploadFile s3UploadFile;

	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;

	@Autowired
	private ProjectEstimationDetailsRepository projectEstimationDetailsRepository;

	@Override
	@Transactional
	public ProjectEstimationDTO estimate(ProjectEstimationDTO projectEstimationDTO, MultipartFile file) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectEstimationDTO.getProjectId())
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		Optional<ProjectEstimationDetails> findByProjectDetailsProjectId = projectEstimationDetailsRepository
				.findByProjectDetailsProjectId(projectEstimationDTO.getProjectId());
		if (findByProjectDetailsProjectId.isPresent()) {
			throw new DataNotFoundException("Project is Already Estimated");
		}
		if (!file.isEmpty()) {
			projectEstimationDTO.setFileURL(s3UploadFile.uploadFile(file));
		}
		ProjectEstimationDetails projectEstimationDetails = new ProjectEstimationDetails();
		BeanUtils.copyProperties(projectEstimationDTO, projectEstimationDetails);
		projectEstimationDetails.setStartDate(LocalDate.parse(projectEstimationDTO.getStartDate()));
		projectEstimationDetails.setEndDate(LocalDate.parse(projectEstimationDTO.getEndDate()));
		projectEstimationDetails.setStatus("In-Progress");
		projectEstimationDetails.setProjectDetails(projectDetails);
		projectDetails.setProjectEstimationDetails(projectEstimationDetails);
		ProjectEstimationDetails save = projectEstimationDetailsRepository.save(projectEstimationDetails);
		projectEstimationDTO.setEstimationId(save.getEstimationId());
		return projectEstimationDTO;
	}

	@Override
	public ProjectEstimationDTO getEstimationByProject(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
		if (projectEstimationDetails == null) {
			return null;
		}
		ProjectEstimationDTO projectEstimationDTO = new ProjectEstimationDTO();
		BeanUtils.copyProperties(projectEstimationDetails, projectEstimationDTO);
		projectEstimationDTO.setProjectId(projectId);
		projectEstimationDTO.setStartDate(projectEstimationDetails.getStartDate().toString());
		projectEstimationDTO.setEndDate(projectEstimationDetails.getEndDate().toString());
		return projectEstimationDTO;
	}

	@Override
	public List<ProjectDetailsBasicDTO> getAllProjectDetailsEstimation(Long companyId) {

		log.info("getAllProjectDetailsEstimation method, execution start");
		List<ProjectDetailsBasicDTO> projectDetailsBasicDto = new ArrayList<>();
		List<ProjectDetails> projectDeatilsList = projectDetailsRepository.findByCompanyInfoCompanyId(companyId);
		projectDeatilsList.stream().forEach(projectDetails -> {
			ProjectDetailsBasicDTO projectDetailsDTO = new ProjectDetailsBasicDTO();
			BeanUtils.copyProperties(projectDetails, projectDetailsDTO);
			EmployeePersonalInfo ownerPersonalInfo = projectDetails.getCompanyInfo().getEmployeePersonalInfoList()
					.stream().filter(e -> Objects.equals(e.getEmployeeInfoId(), projectDetails.getCreatedBy()))
					.findFirst().orElse(null);

			if (ownerPersonalInfo != null) {
				projectDetailsDTO
						.setProjectOwner(ownerPersonalInfo.getFirstName() + " " + ownerPersonalInfo.getLastName());
			}
			CompanyClientInfo companyClientInfo = projectDetails.getCompanyClientInfo();
			if (companyClientInfo != null) {
				projectDetailsDTO.setClientName(companyClientInfo.getClientName());
			}
			EmployeePersonalInfo projectManager = projectDetails.getProjectManager();
			if (projectManager != null) {
				projectDetailsDTO.setProjectManager(projectManager.getFirstName() + " " + projectManager.getLastName());
			}
			projectDetailsBasicDto.add(projectDetailsDTO);
			ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
			if (projectEstimationDetails == null) {
				projectDetailsDTO.setStatus(STATUS_NOT_ESTIMATED);
			} else {
				projectDetailsDTO.setStatus(projectEstimationDetails.getStatus());
			}

		});
		log.info("getAllProjectDetailsEstimation method, execution finished");
		Collections.reverse(projectDetailsBasicDto);
		return projectDetailsBasicDto;
	}

	@Override
	public ProjectDetailsBasicDTO getProjectDetailsEstimation(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId).orElse(null);

		if (projectDetails == null) {
			return null;
		}

		ProjectDetailsBasicDTO projectDetailsDTO = new ProjectDetailsBasicDTO();
		BeanUtils.copyProperties(projectDetails, projectDetailsDTO);
		EmployeePersonalInfo ownerPersonalInfo = projectDetails.getCompanyInfo().getEmployeePersonalInfoList().stream()
				.filter(e -> Objects.equals(e.getEmployeeInfoId(), projectDetails.getCreatedBy())).findFirst()
				.orElse(null);
		if (ownerPersonalInfo != null) {
			projectDetailsDTO.setProjectOwner(ownerPersonalInfo.getFirstName() + " " + ownerPersonalInfo.getLastName());
		}
		CompanyClientInfo companyClientInfo = projectDetails.getCompanyClientInfo();
		if (companyClientInfo != null) {
			projectDetailsDTO.setClientName(companyClientInfo.getClientName());
		}
		EmployeePersonalInfo projectManager = projectDetails.getProjectManager();
		if (projectManager != null) {
			projectDetailsDTO.setProjectManager(projectManager.getFirstName() + " " + projectManager.getLastName());
		}
		EmployeePersonalInfo reportingManager = projectDetails.getReportingManager();
		if (reportingManager != null) {
			projectDetailsDTO
					.setReportingManager(reportingManager.getFirstName() + " " + reportingManager.getLastName());
		}
		if (projectDetails.getProjectEstimationDetails() == null) {
			projectDetailsDTO.setStatus(STATUS_NOT_ESTIMATED);
		} else {
			projectDetailsDTO.setStatus(projectDetails.getProjectEstimationDetails().getStatus());
		}
		return projectDetailsDTO;
	}

	@Override
	@Transactional
	public String updateStatus(Long projectId, String status) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
		if (projectEstimationDetails == null) {
			throw new DataNotFoundException("Estimation Not Found");
		}
		if (!status.equalsIgnoreCase(APPROVED) && !status.equalsIgnoreCase(REJECTED)) {
			throw new DataNotFoundException("Estimation can only be Approved or Rejected");
		}
		if (APPROVED.equalsIgnoreCase(projectEstimationDetails.getStatus())
				|| REJECTED.equalsIgnoreCase(projectEstimationDetails.getStatus())) {
			throw new DataNotFoundException("Estimation Already " + projectEstimationDetails.getStatus());
		}
		projectEstimationDetails.setStatus(status);
		projectEstimationDetailsRepository.save(projectEstimationDetails);
		return "Estimation " + status + " Successfully";
	}

}
