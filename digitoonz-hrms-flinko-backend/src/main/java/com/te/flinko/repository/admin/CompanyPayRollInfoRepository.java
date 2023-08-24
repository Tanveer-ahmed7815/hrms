package com.te.flinko.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.te.flinko.dto.admin.CompanyPayrollInfoResponseDTO;
import com.te.flinko.entity.admin.CompanyPayrollInfo;

//@author rakesh Kumar Nayak
public interface CompanyPayRollInfoRepository extends JpaRepository<CompanyPayrollInfo, Long> {

	public Optional<CompanyPayrollInfo> findByPayrollIdAndCompanyPayrollDeductionListTitleAndCompanyInfoCompanyId(
			Long payrollId, String title, Long companyId);

	public Optional<CompanyPayrollInfo> findByPayrollIdAndCompanyInfoCompanyId(Long payrollId, Long companyId);

	@Query(value = "select new com.te.flinko.dto.admin.CompanyPayrollInfoResponseDTO(cpi.payrollId,"
			+ "cpi.payrollName," + "cpi.salaryApprovalDate," + "cpi.paymentDate," + "cpi.paySlipGenerationDate,"
			+ "cpi.isPfEnabled," + "cpi.isPtEnabled," + "cpi.isCsiEnabled,"
			+ "cpi.isAdvanceSalaryEnabled)from CompanyPayrollInfo cpi" + " where cpi.companyInfo.companyId=:companyId")
	public Optional<List<CompanyPayrollInfoResponseDTO>> findByCompanyInfoCompanyId(@Param("companyId") Long companyId);

}
