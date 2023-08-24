package com.te.flinko.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
//	@Modifying
//	@Query("from Department")
//	//List<Department> findByDepartmentId();

	// List<Department> findByDepartmentId(Long departmentId);
	
	Optional<Department> findByDepartmentName(String departmentName);
	
	
	

}
