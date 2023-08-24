package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeDropdownDTO;
import com.te.flinko.dto.hr.CandidateInfoDTO;
import com.te.flinko.dto.hr.CandidateInfoDTOById;
import com.te.flinko.dto.hr.CandidateInterviewInfoDTO;
import com.te.flinko.dto.hr.CandidateListDTO;
import com.te.flinko.dto.hr.FollowUpDTO;
import com.te.flinko.dto.hr.FollowUpDetailsDTO;
import com.te.flinko.dto.hr.InterviewFeedbackInfoDTO;
import com.te.flinko.dto.hr.RejectedCandidatedetailsDTO;
import com.te.flinko.dto.hr.ScheduledCandidateDTO;
import com.te.flinko.dto.hr.ScheduledCandidateDetailsDTO;
import com.te.flinko.dto.hr.UpdateFeedbackDTO;
/**
 * 
 * @author Ravindra
 *
 */
public interface CandidateManagementService {

	 CandidateInfoDTO newCandidate(CandidateInfoDTO candidateInfo);

	List<CandidateListDTO> candidateDetailsList(Long companyId);

	CandidateInterviewInfoDTO scheduleInterview(CandidateInterviewInfoDTO candidateInfo);

	CandidateInfoDTOById findCandidateInfoByUsingId(Long candidateId, Long companyId);
	

	List<FollowUpDTO> followUp(Long companyId);

	FollowUpDetailsDTO followUpDetails(Long candidateId, Long companyId);

	CandidateInfoDTO editCandidateInfo(CandidateInfoDTO candidateInfo);

	List<ScheduledCandidateDTO> scheduledCandidates(Long companyId);

	
	String deleteCandidateInfo(Long candidateId, Long companyId);

	ScheduledCandidateDetailsDTO scheduledCandidateDetails(Long candidateId, Long companyId);

	RejectedCandidatedetailsDTO rejectedCandidateDetails(Long candidateId, Long companyId);

	List<FollowUpDTO> rejectedCandidates(Long companyId);

	UpdateFeedbackDTO updateFeedback(Long interviewId, UpdateFeedbackDTO feedback);

	List<EmployeeDropdownDTO> employeeDropdownList(Long candidateId);

	InterviewFeedbackInfoDTO interviewFeedbackInfo(Long interviewId, Long companyId);
	
	String sendLink(String url, Long candidateId, Long companyId, Long userId);
	

}
