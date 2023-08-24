package com.te.flinko.service.project;

import com.te.flinko.dto.project.UpdateProjectDTO;
import com.te.flinko.dto.project.ClientDropDownDTO;
import com.te.flinko.dto.project.CreateProjectDto;
import com.te.flinko.dto.project.ProjectDetailsBasicDTO;

import java.util.List;

public interface ProjectDetailsService {

    List<ProjectDetailsBasicDTO> getAllProjectDetailsBasicByCompanyId(Long companyId);

    String createProject(CreateProjectDto createProjectDto, Long companyId);

    String updateProject(UpdateProjectDTO updateProjectDto);
    
    List<ClientDropDownDTO> getCompanyClients(Long companyId);
}
