package com.te.flinko.repository.employee.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.te.flinko.dto.employee.mongo.EmployeeDocument;

public interface EmployeeDocumentRepository extends MongoRepository<EmployeeDocument, String> {

}
