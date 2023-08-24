package com.te.flinko.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyBranchInfo;

public interface CompanyBranchInfoRepo extends JpaRepository<CompanyBranchInfo, Long>{
	
	public Optional<CompanyBranchInfo> findByBranchName(String branchName);
	
}
