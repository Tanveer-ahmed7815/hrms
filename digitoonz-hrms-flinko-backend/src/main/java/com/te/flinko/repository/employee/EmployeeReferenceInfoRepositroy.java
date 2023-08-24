package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeReferenceInfo;
@Repository
public interface EmployeeReferenceInfoRepositroy extends JpaRepository<EmployeeReferenceInfo,Long>{
	List<EmployeeReferenceInfo> findByReferralName(String referralName);
//	List<EmployeeReferenceInfo> FindByCandidateInfocandidateId(CandidateInfo f);
//	void FindByCandidateInfocandidateId(Long candidateId);
	List<EmployeeReferenceInfo> findByReferralNameAndCandidateInfoCandidateId(String referralName,Long candidateId);
	
	List<EmployeeReferenceInfo> findByCandidateInfoCandidateId(Long candidateId);
	
	List<EmployeeReferenceInfo> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);
	

}
