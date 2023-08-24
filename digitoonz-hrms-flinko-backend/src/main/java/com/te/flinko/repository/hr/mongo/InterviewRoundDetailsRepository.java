package com.te.flinko.repository.hr.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.hr.mongo.InterviewRoundDetails;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface InterviewRoundDetailsRepository extends MongoRepository<InterviewRoundDetails, String> {

	List<InterviewRoundDetails> findByCompanyId(Long companyId);

}
