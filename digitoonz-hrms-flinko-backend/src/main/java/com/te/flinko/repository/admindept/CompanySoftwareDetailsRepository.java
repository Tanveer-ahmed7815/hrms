package com.te.flinko.repository.admindept;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admindept.CompanySoftwareDetails;
/**
 * @author Manish Kumar
 * */
@Repository
public interface CompanySoftwareDetailsRepository extends JpaRepository<CompanySoftwareDetails, Long> {

	Optional<CompanySoftwareDetails> findBySoftwareIdAndCompanyInfoCompanyId(Long softwareId, Long companyId);
	List<CompanySoftwareDetails> findByCompanyInfoCompanyId(Long companyId);

	Optional<CompanySoftwareDetails> findBySoftwareNameAndCompanyInfoCompanyId(String softwareName, Long companyId);
	List<CompanySoftwareDetails> findBySoftwareName(String softwareName);







}

