package com.te.flinko.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admin.CompanyShiftInfo;

@Repository
public interface CompanyShiftInfoRepository extends JpaRepository<CompanyShiftInfo, Long> {

	List<CompanyShiftInfo> findByCompanyRuleInfoCompanyInfoCompanyId(Long companyId);
}
