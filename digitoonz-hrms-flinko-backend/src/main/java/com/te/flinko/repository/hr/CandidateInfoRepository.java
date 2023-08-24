package com.te.flinko.repository.hr;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.hr.CandidateInfo;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface CandidateInfoRepository extends JpaRepository<CandidateInfo, Long> {

	Optional<CandidateInfo> findByCandidateIdAndCompanyInfoCompanyId(long candidateId, Long companyId);

	List<CandidateInfo> findByCandidateIdNotInAndCompanyInfoCompanyId(Set<Long> candidateId,Long companyId);

	List<CandidateInfo> findByCompanyInfoCompanyId(Long companyId);

	List<CandidateInfo> findByCandidateIdInAndCompanyInfoCompanyId(Set<Long> candidateSet, Long companyId);

	List<CandidateInfo> findByCandidateIdInAndCompanyInfoCompanyId(List<Long> getCandidateId, Long companyId);
	
	List<CandidateInfo> findByCompanyInfoCompanyIdAndIsSelected(Long companyId, boolean isSeleted);
	
	List<CandidateInfo> findByCandidateIdNotInAndCompanyInfoCompanyIdAndIsSelected(Set<Long> candidateSet, Long companyId, boolean isSelected);



	

}
