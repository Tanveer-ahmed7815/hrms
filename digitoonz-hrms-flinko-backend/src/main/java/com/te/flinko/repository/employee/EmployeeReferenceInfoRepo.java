package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeReferenceInfo;

@Repository
public interface EmployeeReferenceInfoRepo extends JpaRepository<EmployeeReferenceInfo,Long> {

	List<EmployeeReferenceInfo> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);
//public interface EmployeeReferenceInfoRepo extends JpaRepository<EmployeeReferenceInfo, Long>{
	
}
