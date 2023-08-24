package com.te.flinko.repository.employee.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.flinko.entity.employee.mongo.EmployeeTimesheetDetails;

public interface EmployeeTimesheetDetailsRepository extends MongoRepository<EmployeeTimesheetDetails, String> {

	Optional<List<EmployeeTimesheetDetails>> findByCompanyId(Long companyId);
	
	EmployeeTimesheetDetails findByEmployeeId(String employeeId);
	
	EmployeeTimesheetDetails findByEmployeeIdAndCompanyId(String employeeId,Long companyId);
	
	Optional<List<EmployeeTimesheetDetails>> findByCompanyIdAndEmployeeId(Long companyId, String employeeIdList);

	Optional<List<EmployeeTimesheetDetails>> findByCompanyIdAndIsApproved(Long companyId, Boolean isApproved);

	Optional<EmployeeTimesheetDetails> findByTimesheetObjectIdAndCompanyIdAndIsApproved(String timesheetObjectId, Long companyId, Boolean isApproved);
	
	Optional<EmployeeTimesheetDetails> findByTimesheetObjectIdAndCompanyId(String timesheetObjectId, Long companyId);

	Optional<List<EmployeeTimesheetDetails>> findByCompanyIdAndEmployeeIdIn(Long companyId,
			List<String> employeeIdList);

//	Optional<String> findByTimesheetObjectIdAndCompanyId(String timesheetObjectId, Long companyId);

//	Optional<EmployeeTimesheetDetails> findByIdAndCompanyId(String id,Long companyId);
	
	Optional<EmployeeTimesheetDetails> findByTimesheetIdAndCompanyId(Long timesheetId, Long companyId);
	
	List<EmployeeTimesheetDetails> findByIsApprovedAndCompanyIdAndEmployeeIdIn(Boolean isApproved,Long companyId,List<String> employeeIdList);
}
