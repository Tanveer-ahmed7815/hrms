package com.te.flinko.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeePersonalInfo;

public interface UserDetailsRepository extends JpaRepository<EmployeePersonalInfo, Long> {

	
	
	

}
