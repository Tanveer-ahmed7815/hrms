package com.te.flinko.repository.employee.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.flinko.entity.employee.mongo.EmployeeDocumentDetails;

public interface EmployeeDocumentDetailsRepository extends MongoRepository<EmployeeDocumentDetails, String>{
	
	List<EmployeeDocumentDetails> findByEmployeeIdAndCompanyId(String employeeId, Long companyId);

}
