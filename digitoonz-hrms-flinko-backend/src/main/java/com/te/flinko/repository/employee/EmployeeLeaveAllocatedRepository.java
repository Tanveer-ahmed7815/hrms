package com.te.flinko.repository.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeLeaveAllocated;

public interface EmployeeLeaveAllocatedRepository extends JpaRepository<EmployeeLeaveAllocated, Long> {
	
	Optional<EmployeeLeaveAllocated> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
//	List<EmployeeLeaveAllocated> findByEmployeePersonalInfoEmployeeInfoId(Long employeeId);

}
