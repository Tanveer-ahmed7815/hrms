package com.te.flinko.repository.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyHolidayDetails;

public interface CompanyHolidayDetailsRepository extends JpaRepository<CompanyHolidayDetails, Long> {

	Optional<List<CompanyHolidayDetails>> findByHolidayDateBetweenAndHolidayNameAndCompanyInfoCompanyId(
			LocalDate yearStartDate, LocalDate yearEndDate, String holidayName, Long companyInfo);

	Optional<List<CompanyHolidayDetails>> findByHolidayDateBetweenAndHolidayNameInAndCompanyInfoCompanyId(
			LocalDate yearStartDate, LocalDate yearEndDate, List<String> holidayNames, Long companyInfo);

	Optional<List<CompanyHolidayDetails>> findByHolidayDateBetweenAndCompanyInfoCompanyId(LocalDate yearStartDate,
			LocalDate yearEndDate, Long companyInfo);

	Optional<CompanyHolidayDetails> findByHolidayIdAndHolidayDateBetweenAndCompanyInfoCompanyId(Long holidayId,
			LocalDate of, LocalDate of2, Long companyId);

	Optional<CompanyHolidayDetails> findByHolidayIdAndCompanyInfoCompanyId(Long holidayId, Long companyId);
}
