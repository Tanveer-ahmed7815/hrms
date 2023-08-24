package com.te.flinko.repository.project.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.project.mongo.ProjectTaskDetails;

@Repository
public interface ProjectTaskDetailsRepository extends MongoRepository<ProjectTaskDetails, String> {

	// List<ProjectTaskDetails> findByProjectIdIn(Long projectId);

	List<ProjectTaskDetails> findByProjectId(Long projectId);

	List<ProjectTaskDetails> findByAssignedEmployeeAndCompanyId(String employeeId, Long companyId);

	List<ProjectTaskDetails> findByProjectIdAndStatus(Long projectId, String status);

	List<ProjectTaskDetails> findByProjectIdAndId(Long projectId, String taskId);

	List<ProjectTaskDetails> findByAssignedEmployeeAndProjectIdAndStatus(String employeeId, Long projectId,
			String status);

	List<ProjectTaskDetails> findByAssignedEmployeeAndProjectId(String employeeId, Long projectId);

	ProjectTaskDetails findByTaskId(Long taskId);

	List<ProjectTaskDetails> findByStatus(String status);

	List<ProjectTaskDetails> findByProjectIdAndStatusInAndMileStoneIdAndSubMilestoneId(Long projectId,
			List<String> status, String mileStoneId, Long subMilestoneId);

	List<ProjectTaskDetails> findByProjectIdAndStatusInAndMileStoneId(Long projectId, List<String> status,
			String mileStoneId);

	List<ProjectTaskDetails> findByAssignedEmployeeAndStatusAndCompanyId(String employeeId, String status,
			Long companyId);

}
