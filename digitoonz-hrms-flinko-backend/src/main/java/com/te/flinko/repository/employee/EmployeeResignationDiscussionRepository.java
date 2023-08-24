package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.te.flinko.entity.employee.EmployeeResignationDiscussion;
@Repository
public interface EmployeeResignationDiscussionRepository extends JpaRepository<EmployeeResignationDiscussion, Long>{

	
	List<EmployeeResignationDiscussion> findByCompanyEmployeeResignationDetailsResignationId(Long resignationId);
}
