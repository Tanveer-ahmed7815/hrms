package com.te.flinko.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admin.LevelsOfApproval;

@Repository
public interface LevelsOfApprovalRepository extends JpaRepository<LevelsOfApproval, Long> {
	
	Optional<LevelsOfApproval> findByCompanyInfoCompanyId(Long companyId);

}

