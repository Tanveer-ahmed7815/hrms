package com.te.flinko.repository.project.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.project.mongo.ProjectMilestoneDeliverables;

@Repository
public interface MilestoneRepository extends MongoRepository<ProjectMilestoneDeliverables,String>{
		
	public List<ProjectMilestoneDeliverables> findByProjectId(Long projectId);

	
	public Optional<ProjectMilestoneDeliverables> findByMileStoneObjectId(String mileStoneObjectId);
	
	public Optional<ProjectMilestoneDeliverables> findByMileStoneObjectIdAndProjectId(String mileStoneObjectId,Long  projectId);
	
	public Optional<ProjectMilestoneDeliverables> findByMileStoneObjectIdAndMilestoneId(String mileStoneObjectId,Long milestoneId);
}
