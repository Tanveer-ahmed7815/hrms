package com.te.flinko.repository.employee;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.te.flinko.entity.employee.mongo.EmployeeTimesheetDetails;

@Repository
public interface EmployeeTimeSheetRepository extends MongoRepository<EmployeeTimesheetDetails, Long> {

}
