package com.te.flinko.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.te.flinko.entity.admin.CompanyDesignationInfo;

public interface CompanyDesignationRepository extends JpaRepository<CompanyDesignationInfo, Long> {

	Optional<CompanyDesignationInfo> findByDepartmentAndDesignationNameAndCompanyInfoCompanyId(String department,
			String designationName, long companyId);

	List<CompanyDesignationInfo>  findByParentDesignationInfoDesignationId(long parentDesignationId);

	Optional<CompanyDesignationInfo> findByCompanyInfoCompanyIdAndDesignationId(long companyId, Long designationId);

	List<CompanyDesignationInfo> findByCompanyInfoCompanyIdAndDepartment(String departmentName, long companyId);

	List<CompanyDesignationInfo> findByCompanyInfoCompanyIdAndDepartment(long companyId, String departmentName);

	Optional<List<CompanyDesignationInfo>> findByCompanyInfoCompanyIdAndParentDesignationInfoDesignationId(Long companyId,
			Long designationId);

	List<CompanyDesignationInfo> findByCompanyInfoCompanyIdAndDepartmentAndParentDesignationInfoDesignationId(
			long companyId, String department, long parentDesignationId);

}
