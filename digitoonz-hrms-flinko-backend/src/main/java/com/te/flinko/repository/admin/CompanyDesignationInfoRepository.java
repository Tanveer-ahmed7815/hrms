package com.te.flinko.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyDesignationInfo;

public interface CompanyDesignationInfoRepository extends JpaRepository<CompanyDesignationInfo, Long> {

	List<CompanyDesignationInfo> findByDesignationNameAndCompanyInfoCompanyId(String designationName, Long companyId);

	List<CompanyDesignationInfo> findByCompanyInfoCompanyId(Long companyId);

	Optional<CompanyDesignationInfo> findByDesignationName(String designation);
	
	List<CompanyDesignationInfo> findByDepartmentAndCompanyInfoCompanyId(String department,Long companyId);

}
