package com.te.flinko.controller.hr;

import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INFORMATION_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INFORMATION_SUCCESSFULLY_DELETED;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INTERVIEW_DETAILS_INSERT_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INTERVIEW_FEEDBACK_UPDATE_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_RECORD_FETCH_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_RECORD_NOT_FOUND;
import static com.te.flinko.common.hr.HrConstants.EMPLOYEES_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.REJECTED_CANDIDATE_RECORDS_FETCH_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INFORMATION_UPDATED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CANDIDATE_INTERVIEW_FEEDBACK_DETAILS_FETCHED_SUCCESSFULLY;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
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
import com.te.flinko.dto.hr.SendLinkDTO;
import com.te.flinko.dto.hr.UpdateFeedbackDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.CandidateManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/candidate")
@CrossOrigin(origins = "*")
/**
 * 
 * @author Ravindra
 *
 */
public class CandidateManagementController extends BaseConfigController{

	@Autowired
	private CandidateManagementService service;

	@PostMapping("/newCandidate")
	public ResponseEntity<SuccessResponse> newCandidate(@RequestBody CandidateInfoDTO candidateInfo) {
		CandidateInfoDTO newCandidate = service.newCandidate(candidateInfo);
		log.info("Candidate details add successfully");
		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_INFORMATION_ADDED_SUCCESSFULLY, newCandidate),
				HttpStatus.CREATED);

	}

	@PostMapping("/editCandidateInfo")
	public ResponseEntity<SuccessResponse> editCandidateInfo(@RequestBody CandidateInfoDTO candidateInfo) {

		CandidateInfoDTO editCandidateInfo = service.editCandidateInfo(candidateInfo);
		log.info("Candidate details update successfully");

		return new ResponseEntity<>(
				new SuccessResponse(false, CANDIDATE_INFORMATION_UPDATED_SUCCESSFULLY, editCandidateInfo),
				HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteCandidateInfo")
	public ResponseEntity<SuccessResponse> deleteCandidateInfo(@RequestParam Long candidateId,
			@RequestParam Long companyId) {
		String msg = service.deleteCandidateInfo(candidateId, companyId);
		log.info("Candidate details delete successfully");

		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_INFORMATION_SUCCESSFULLY_DELETED, msg),
				HttpStatus.OK);
	}

	@GetMapping("/candidateDetailsList")
	public ResponseEntity<SuccessResponse> showData(@RequestParam Long companyId) {
		List<CandidateListDTO> candidateDetailsList = service.candidateDetailsList(companyId);

		if (candidateDetailsList.isEmpty()) {
			log.warn(CANDIDATE_RECORD_NOT_FOUND);
			return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_NOT_FOUND, candidateDetailsList),
					HttpStatus.OK);
		} else {
			log.info("Candidate information fetched successfully");
			return new ResponseEntity<>(
					new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, candidateDetailsList),
					HttpStatus.OK);
		}
	}

	@GetMapping("/findCandidateInfoByUsingId")
	public ResponseEntity<SuccessResponse> findCandidateInfoByUsingId(@RequestParam Long candidateId,
			@RequestParam Long companyId) {
		CandidateInfoDTOById findCandidateInfoByUsingId = service.findCandidateInfoByUsingId(candidateId, companyId);
		return new ResponseEntity<>(
				new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, findCandidateInfoByUsingId),
				HttpStatus.OK);
	}

	@PostMapping("/scheduleInterview")
	public ResponseEntity<SuccessResponse> scheduleInterview(@RequestBody CandidateInterviewInfoDTO candidateInfo) {
		CandidateInterviewInfoDTO scheduleInterview = service.scheduleInterview(candidateInfo);
		log.info("candidate interview details save successfully");
		if (scheduleInterview == null) {
			log.warn("candidate interview data has some problem ");
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, CANDIDATE_INTERVIEW_DETAILS_INSERT_SUCCESSFULLY, scheduleInterview),
				HttpStatus.OK);

	}

	@GetMapping("/followUp")
	public ResponseEntity<SuccessResponse> followUp(@RequestParam Long companyId) {
		List<FollowUpDTO> followUp = service.followUp(companyId);
		if (followUp.isEmpty()) {
			log.warn(CANDIDATE_RECORD_NOT_FOUND);
			return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_NOT_FOUND, followUp),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, followUp),
				HttpStatus.OK);

	}

	@GetMapping("/followUpDetails")
	public ResponseEntity<SuccessResponse> followUpDetails(@RequestParam Long candidateId,
			@RequestParam Long companyId) {
		FollowUpDetailsDTO followUpDetails = service.followUpDetails(candidateId, companyId);
		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, followUpDetails),
				HttpStatus.OK);
	}

	@GetMapping("/scheduledCandidates")
	public ResponseEntity<SuccessResponse> scheduledCandidates(@RequestParam Long companyId) {
		List<ScheduledCandidateDTO> scheduledCandidates = service.scheduledCandidates(companyId);
		if (scheduledCandidates.isEmpty()) {
			log.warn("No candidates found in records");
			return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_NOT_FOUND, scheduledCandidates),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, scheduledCandidates), HttpStatus.OK);
	}

	@GetMapping("/rejectedCandidates")
	public ResponseEntity<SuccessResponse> rejectedCandidates(@RequestParam Long companyId) {

		List<FollowUpDTO> rejectedCandidates = service.rejectedCandidates(companyId);
		if (rejectedCandidates.isEmpty()) {
			log.warn("No candidates found in records");
			return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_NOT_FOUND, rejectedCandidates),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, REJECTED_CANDIDATE_RECORDS_FETCH_SUCCESSFULLY, rejectedCandidates),
				HttpStatus.OK);
	}

	@GetMapping("/scheduledCandidateDetails")
	public ResponseEntity<SuccessResponse> scheduledCandidateDetails(@RequestParam Long candidateId,
			@RequestParam Long companyId) {
		ScheduledCandidateDetailsDTO candidateDetails = service.scheduledCandidateDetails(candidateId, companyId);
		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, candidateDetails),
				HttpStatus.OK);
	}

	@GetMapping("/rejectedCandidateDetails")
	public ResponseEntity<SuccessResponse> rejectedCandidateDetails(@RequestParam Long candidateId,
			@RequestParam Long companyId) {
		RejectedCandidatedetailsDTO candidateDetail = service.rejectedCandidateDetails(candidateId, companyId);
		return new ResponseEntity<>(new SuccessResponse(false, CANDIDATE_RECORD_FETCH_SUCCESSFULLY, candidateDetail),
				HttpStatus.OK);
	}

	@PostMapping("/updateFeedback/{interviewId}")
	public ResponseEntity<SuccessResponse> updateFeedback(@PathVariable Long interviewId,
			@RequestBody UpdateFeedbackDTO feedback) {
		UpdateFeedbackDTO updateFeedback = service.updateFeedback(interviewId, feedback);
		return new ResponseEntity<>(
				new SuccessResponse(false, CANDIDATE_INTERVIEW_FEEDBACK_UPDATE_SUCCESSFULLY, updateFeedback),
				HttpStatus.OK);
	}

	@GetMapping("/employee-list")
	public ResponseEntity<SuccessResponse> employeeDropdownList(@RequestParam Long companyId) {
		List<EmployeeDropdownDTO> dropdownDTOs = service.employeeDropdownList(companyId);
		return new ResponseEntity<>(new SuccessResponse(false, EMPLOYEES_FETCHED_SUCCESSFULLY, dropdownDTOs),
				HttpStatus.OK);
	}

	@GetMapping("/interview-Feedback-Info")
	public ResponseEntity<SuccessResponse> interviewFeedbackInfo(@RequestParam Long interviewId,
			@RequestParam Long companyId) {
		InterviewFeedbackInfoDTO interviewFeedbackInfo = service.interviewFeedbackInfo(interviewId, companyId);
		return new ResponseEntity<>(new SuccessResponse(false,CANDIDATE_INTERVIEW_FEEDBACK_DETAILS_FETCHED_SUCCESSFULLY, interviewFeedbackInfo),
				HttpStatus.OK);
	}
	
	@PostMapping("/send-link")
	public ResponseEntity<SuccessResponse> sendLink(@RequestBody SendLinkDTO sendLinkDTO) {
		String message = service.sendLink(sendLinkDTO.getLink(),sendLinkDTO.getCandidateId(),getCompanyId(), getUserId());
		return new ResponseEntity<>(new SuccessResponse(false,message, message),
				HttpStatus.OK);
	}


}
