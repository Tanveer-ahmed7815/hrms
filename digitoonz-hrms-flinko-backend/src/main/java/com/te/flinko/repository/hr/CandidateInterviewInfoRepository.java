package com.te.flinko.repository.hr;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.hr.CandidateInterviewInfo;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface CandidateInterviewInfoRepository extends JpaRepository<CandidateInterviewInfo, Long> {

	public Optional<CandidateInterviewInfo> findByCandidateInfoCandidateId(Long candidateId);

	public List<CandidateInterviewInfo> findByCandidateInfoCandidateIdIn(List<Long> list);

	public List<CandidateInterviewInfo> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);

	public List<CandidateInterviewInfo> findByCandidateInfoCandidateIdNotIn(List<Long> list);

	public List<CandidateInterviewInfo> findByInterviewIdAndCandidateInfoCandidateId(Long interviewId,
			Long candidateId);

	public List<CandidateInterviewInfo> findByCandidateInfoCandidateIdAndCandidateInfoCompanyInfoCompanyId(
			Long candidateId, Long companyId);

	List<CandidateInterviewInfo> findByCandidateInfoCompanyInfoCompanyId(Long companyId);

	public List<CandidateInterviewInfo> findByCandidateInfoCandidateIdInAndCandidateInfoCompanyInfoCompanyIdOrderByCandidateInfoCandidateIdDesc(
			List<Long> listOfInfo, Long companyId);

	public List<CandidateInterviewInfo> findByInterviewIdAndCandidateInfoCompanyInfoCompanyId(Long interviewId, Long companyId);

}
