package com.te.flinko.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admin.CompanyBranchInfo;
@Repository
public interface CompanyBranchInfoRepository extends JpaRepository<CompanyBranchInfo, Long> {

	List<CompanyBranchInfo> findByCompanyInfoCompanyId(Long CompanyId);

	Optional<CompanyBranchInfo> findByBranchName(String branchName);

}
