package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;

@Repository
public interface CompanyEmployeeResignationDetailsRepository
		extends JpaRepository<CompanyEmployeeResignationDetails, Long> {

	public List<CompanyEmployeeResignationDetails> findByCompanyInfoCompanyId(Long companyId);

	public List<CompanyEmployeeResignationDetails> findByEmployeeResignationDiscussionListDuscussionId(
			Long duscussionId);

//	public List<CompanyEmployeeResignationDetails> findByEmployeePersonalInfoEmployeeReportingInfoAsHRListReportingHRAndCompanyInfoCompanyId(
//			Long employeeInfoId, Long companyId);
	
	List<CompanyEmployeeResignationDetails> findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(Long employeeInfoId,Long companyId);
	
	Optional<CompanyEmployeeResignationDetails> findByResignationIdAndCompanyInfoCompanyId(Long resignationId,Long companyId);
	
	List<CompanyEmployeeResignationDetails> findByStatusAndCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(String status,Long companyId,List<Long> employeeInfoIdList);

	List<CompanyEmployeeResignationDetails> findByCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(
			Long companyId, List<Long> employeeInfoIdList);
}
