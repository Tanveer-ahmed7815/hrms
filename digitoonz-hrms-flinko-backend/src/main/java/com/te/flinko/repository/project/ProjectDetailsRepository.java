package com.te.flinko.repository.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.project.ProjectDetails;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetails, Long> {
	
	List<ProjectDetails> findByProjectName(String projectName);

	List<ProjectDetails> findByCompanyInfoCompanyId(Long companyId);

	List<ProjectDetails> findByCompanyInfoCompanyIdAndProjectId(Long companyId, Long projectId);

}