package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReferenceInfo;

@Repository
public interface EmployeeReferenceInfoRepository extends JpaRepository<EmployeeReferenceInfo,Long> {

	List<EmployeeReferenceInfo> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);
	List<EmployeeReferenceInfo> findByReferralName(String referralName);
	List<EmployeeReferenceInfo> findByReferralNameAndCandidateInfoCandidateId(String referralName,Long candidateId);
	List<EmployeeReferenceInfo> findByCandidateInfoCandidateId(Long candidateId);
	List<EmployeeReferenceInfo> findByEmployeePersonalInfo(EmployeePersonalInfo employeePersonalInfo);
	Optional<EmployeeReferenceInfo> findByEmployeePersonalInfoEmployeeInfoId(Long employeeId);
}
