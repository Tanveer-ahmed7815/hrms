package com.te.flinko.service.project;

import static com.te.flinko.common.project.ProjectManagementConstants.PROJECT_SAVED;
import static com.te.flinko.common.project.ProjectManagementConstants.PROJECT_UPDATED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.te.flinko.dto.project.ClientDropDownDTO;
import com.te.flinko.dto.project.CreateProjectDto;
import com.te.flinko.dto.project.ProjectDetailsBasicDTO;
import com.te.flinko.dto.project.UpdateProjectDTO;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.sales.CompanyClientInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.project.ProjectDetailsRepository;
import com.te.flinko.repository.sales.CompanyClientInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectDetailsServiceImpl implements ProjectDetailsService {

	private final ProjectDetailsRepository projectDetailsRepository;
	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;
	private final CompanyClientInfoRepository companyClientInfoRepository;
	private final CompanyInfoRepository companyInfoRepository;

	@Override
	public List<ProjectDetailsBasicDTO> getAllProjectDetailsBasicByCompanyId(Long companyId) {
		log.info("getAllProjectDetailsBasicByCompanyId method, execution start");
		List<ProjectDetailsBasicDTO> projectDetailsBasicDtos = Lists.newArrayList();
		projectDetailsRepository.findByCompanyInfoCompanyId(companyId).forEach(p -> {
			log.debug("getAllProjectDetailsBasicByCompanyId method, iteration on all the objects to create dto list");
			String projectManager = "", projectOwner = "", clientName = "";
			if (Optional.ofNullable(p.getProjectManager()).isPresent()
					&& Optional.ofNullable(p.getProjectManager().getFirstName()).isPresent()
					&& Optional.ofNullable(p.getProjectManager().getLastName()).isPresent()) {
				projectManager = String.format("%s %s", p.getProjectManager().getFirstName(),
						p.getProjectManager().getLastName());
			} else
				projectManager = null;
			log.debug("getAllProjectDetailsBasicByCompanyId method, fetching project owner");
			Optional<EmployeePersonalInfo> opEpi = employeePersonalInfoRepository.findById(p.getCreatedBy());
			if (opEpi.isPresent()) {
				EmployeePersonalInfo employeePersonalInfo = opEpi.get();
				if (Optional.ofNullable(employeePersonalInfo.getFirstName()).isPresent()
						&& Optional.ofNullable(employeePersonalInfo.getLastName()).isPresent()) {
					projectOwner = String.format("%s %s", employeePersonalInfo.getFirstName(),
							employeePersonalInfo.getLastName());
				}
			} else
				projectOwner = null;
			if (Optional.ofNullable(p.getCompanyClientInfo()).isPresent()) {
				clientName = p.getCompanyClientInfo().getClientName();
			} else
				clientName = null;
			projectDetailsBasicDtos.add(ProjectDetailsBasicDTO.builder().projectId(p.getProjectId())
					.projectName(p.getProjectName()).projectManager(projectManager).projectOwner(projectOwner)
					.clientName(clientName).startDate(p.getStartDate()).build());
		});
		log.info("getAllProjectDetailsBasicByCompanyId method, returning with data");
		Collections.reverse(projectDetailsBasicDtos);
		return projectDetailsBasicDtos;
	}

	@Transactional
	@Override
	public String createProject(CreateProjectDto createProjectDto, Long companyId) {
		createProjectDto.setProjectName(StringUtils.normalizeSpace(createProjectDto.getProjectName()));
		log.info("createProject method, execution start");
		ProjectDetails projectDetails = new ProjectDetails();
		List<ProjectDetails> existingProjects = projectDetailsRepository
				.findByProjectName(createProjectDto.getProjectName());
		if (!existingProjects.isEmpty()) {
			throw new DataNotFoundException("Project name already exists");
		}
		log.info("createProject method, checking if the company with the given company ID exist and then setting it");
		projectDetails.setCompanyInfo(companyInfoRepository.findByCompanyId(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company data based on company ID could not be found")));
		log.debug("createProject method, fetching project manager");
		employeePersonalInfoRepository.findById(createProjectDto.getProjectManagerEpiId())
				.ifPresentOrElse(projectDetails::setProjectManager, () -> {
					throw new DataNotFoundException("Project manager details not found");
				});
		log.debug("createProject method, fetching client name");
		companyClientInfoRepository.findById(createProjectDto.getClientNameCciId())
				.ifPresentOrElse(projectDetails::setCompanyClientInfo, () -> {
					throw new DataNotFoundException("Client details not found");
				});
		employeePersonalInfoRepository.findById(createProjectDto.getReportingManagerEpiId())
				.ifPresentOrElse(projectDetails::setReportingManager, () -> {
					throw new DataNotFoundException("Reporting manager details not found");
				});
		projectDetails.setProjectDescription(createProjectDto.getProjectDescription());
		projectDetails.setProjectName(createProjectDto.getProjectName());
		projectDetails.setStartDate(createProjectDto.getStartDate());
		log.info("createProject method, saving project...");
		projectDetailsRepository.save(projectDetails);
		log.info("createProject method, project saved and returning");
		return PROJECT_SAVED;
	}

	@Override
	public String updateProject(UpdateProjectDTO updateProjectDto) {
		log.info("updateProject method, execution start");
		Optional<ProjectDetails> opPD = projectDetailsRepository.findById(updateProjectDto.getProjactId());
		log.info("updateProject method, project with the given project id '{}' is present in the database",
				updateProjectDto.getProjactId());
		if (opPD.isPresent()) {
			ProjectDetails projectDetails = opPD.get();
			log.debug("updateProject method, fetching project manager");
			employeePersonalInfoRepository.findById(updateProjectDto.getProjectManagerEpiId())
					.ifPresentOrElse(projectDetails::setProjectManager, () -> {
						throw new DataNotFoundException("Project manager details not found");
					});
			log.debug("updateProject method, fetching project manager");
			companyClientInfoRepository.findById(updateProjectDto.getClientNameCciId())
					.ifPresentOrElse(projectDetails::setCompanyClientInfo, () -> {
						throw new DataNotFoundException("Client details not found");
					});
			employeePersonalInfoRepository.findById(updateProjectDto.getReportingManagerEpiId())
					.ifPresentOrElse(projectDetails::setReportingManager, () -> {
						throw new DataNotFoundException("Reporting manager details not found");
					});
			projectDetails.setProjectDescription(updateProjectDto.getProjectDescription());
			projectDetails.setProjectName(updateProjectDto.getProjectName());
			projectDetails.setStartDate(updateProjectDto.getStartDate());
			log.info("updateProject method, updating project...");
			projectDetailsRepository.save(projectDetails);
			log.info("updateProject method, project updated and returning");
		}
		return PROJECT_UPDATED;
	}

	@Override
	public List<ClientDropDownDTO> getCompanyClients(Long companyId) {
		List<ClientDropDownDTO> clientDropDownDTOList = new ArrayList<>();
		List<CompanyClientInfo> companyClientDetails = companyClientInfoRepository
				.findByCompanyInfoCompanyId(companyId);
		for (CompanyClientInfo companyClientInfo : companyClientDetails) {
			clientDropDownDTOList
					.add(new ClientDropDownDTO(companyClientInfo.getClientId(), companyClientInfo.getClientName()));
		}
		log.info("Clients Fetched");
		return clientDropDownDTOList;
	}

}