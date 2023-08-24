package com.te.flinko.repository.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.sales.CompanyClientInfo;

@Repository
public interface CompanyClientInfoRepository extends JpaRepository<CompanyClientInfo, Long> {
	
	Optional<CompanyClientInfo> findByClientIdAndCompanyInfoCompanyId(Long clientId,Long companyId);
	
	List<CompanyClientInfo> findByCompanyInfoCompanyId(Long companyId);
}
