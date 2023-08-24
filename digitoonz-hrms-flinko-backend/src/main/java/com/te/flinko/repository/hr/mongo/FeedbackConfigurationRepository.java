package com.te.flinko.repository.hr.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.hr.mongo.FeedbackConfiguration;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface FeedbackConfigurationRepository extends MongoRepository<FeedbackConfiguration, String> {



	List<FeedbackConfiguration> findByCompanyIdAndFeedbackFactor(Long companyId, String feedbackFactor);

	List<FeedbackConfiguration> findByCompanyIdAndFeedbackType(Long companyId, String feedbackType);

	List<FeedbackConfiguration> findByCompanyId(long companyId);


}
