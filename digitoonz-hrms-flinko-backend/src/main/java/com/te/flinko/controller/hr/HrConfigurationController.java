package com.te.flinko.controller.hr;

import static com.te.flinko.common.hr.HrConstants.CHECKLISTFACTOR_FACTOR_ADDED_EXCEPT_DUPLICATES;
import static com.te.flinko.common.hr.HrConstants.CHECKLIST_FACTOR_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CHECKLIST_FACTOR_DELETE_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.FEEDBACK_FACTOR_ADDED_EXCEPT_DUPLICATES;
import static com.te.flinko.common.hr.HrConstants.FEEDBACK_FACTOR_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES;
import static com.te.flinko.common.hr.HrConstants.INTERVIEW_DETAILS_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.INTERVIEW_DETAILS_UPDATED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.INTERVIEW_ROUND_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.INTERVIEW_ROUND_DELETE_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.FEEDBACK_FACTOR_DELETED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.hr.AddFeedbackDTO;
import com.te.flinko.dto.hr.AddInterviewRoundDto;
import com.te.flinko.dto.hr.CompanyChecklistDTO;
import com.te.flinko.dto.hr.ConfigurationDto;
import com.te.flinko.dto.hr.DeleteConfigurationDTO;
import com.te.flinko.dto.hr.EditInterviewRoundDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.HrConfigurationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/configuration")
@CrossOrigin(origins = "*")
/**
 * 
 * @author Ravindra
 *
 */
public class HrConfigurationController extends BaseConfigController {

	@Autowired
	HrConfigurationService service;

	@PostMapping("/interview-rounds")
	public ResponseEntity<SuccessResponse> addInterviewRounds(@RequestBody AddInterviewRoundDto interviewdetails) {
		AddInterviewRoundDto addInterviewRounds = service.addInterviewRounds(interviewdetails);
		log.info(INTERVIEW_ROUND_ADDED_SUCCESSFULLY);
		return new ResponseEntity<>(
				new SuccessResponse(false, INTERVIEW_DETAILS_ADDED_SUCCESSFULLY, addInterviewRounds), HttpStatus.OK);
	}

	@PostMapping("/feedback")
	public ResponseEntity<SuccessResponse> addFeedback(@RequestBody AddFeedbackDTO feedback) {
		AddFeedbackDTO addFeedback = service.addFeedback(feedback);
		if (addFeedback.getDuplicatedFactors() == null || addFeedback.getDuplicatedFactors().isEmpty()) {
			log.info(FEEDBACK_FACTOR_ADDED_SUCCESSFULLY);
			return new ResponseEntity<>(new SuccessResponse(false, FEEDBACK_FACTOR_ADDED_SUCCESSFULLY, addFeedback),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new SuccessResponse(false, FEEDBACK_FACTOR_ADDED_EXCEPT_DUPLICATES, addFeedback), HttpStatus.OK);
		}

	}

	@PostMapping("/checklist-factor")
	public ResponseEntity<SuccessResponse> checklistFactor(@RequestBody CompanyChecklistDTO checklistFactor) {
		CompanyChecklistDTO checklistFactorPersist = service.checklistFactor(checklistFactor);
		if (checklistFactorPersist.getDuplicateChecklistFactors() == null
				|| checklistFactorPersist.getDuplicateChecklistFactors().isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, CHECKLIST_FACTOR_ADDED_SUCCESSFULLY, checklistFactorPersist),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new SuccessResponse(false, CHECKLISTFACTOR_FACTOR_ADDED_EXCEPT_DUPLICATES, checklistFactorPersist),
					HttpStatus.OK);
		}
	}

	@PutMapping("/interview-round")
	public ResponseEntity<SuccessResponse> editInterviewDetails(@RequestBody EditInterviewRoundDto interviewdetails) {
		AddInterviewRoundDto editInterviewRoundDetails = service.editInterviewRoundDetails(interviewdetails);
		return new ResponseEntity<>(
				new SuccessResponse(false, INTERVIEW_DETAILS_UPDATED_SUCCESSFULLY, editInterviewRoundDetails),
				HttpStatus.OK);
	}
	
	@DeleteMapping("/interview-round")
	public ResponseEntity<SuccessResponse> deleteInterviewRound(@RequestBody DeleteConfigurationDTO deleteConfigurationDTO) {
		AddInterviewRoundDto deleteInterviewRound = service.deleteInterviewRound(getCompanyId(), deleteConfigurationDTO.getDeletedValue());
		return new ResponseEntity<>(
				new SuccessResponse(false, INTERVIEW_ROUND_DELETE_SUCCESSFULLY, deleteInterviewRound), HttpStatus.OK);
	}

	@PutMapping("/feedback")
	public ResponseEntity<SuccessResponse> editFeedback(@RequestBody AddFeedbackDTO feedback) {
		AddFeedbackDTO addFeedback = service.editFeedback(feedback);
		if (addFeedback.getDuplicatedFactors() == null || addFeedback.getDuplicatedFactors().isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY, addFeedback),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new SuccessResponse(false, FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES, addFeedback),
					HttpStatus.OK);
		}
	}
	

	@DeleteMapping("/feedback-factor")
	public ResponseEntity<SuccessResponse> deleteFeedback(@RequestBody DeleteConfigurationDTO deleteConfigurationDTO) {
		service.deleteFeedbackFactor(getCompanyId(), deleteConfigurationDTO.getDeletedValue(), deleteConfigurationDTO.getType());
		return new ResponseEntity<>(new SuccessResponse(false, FEEDBACK_FACTOR_DELETED_SUCCESSFULLY, null),
				HttpStatus.OK);

	}

	@PutMapping("/checklist-factors")
	public ResponseEntity<SuccessResponse> editChecklistFactor(@RequestBody CompanyChecklistDTO checklistFactor) {
		CompanyChecklistDTO editChecklistFactor = service.editChecklistFactor(checklistFactor);
		if (editChecklistFactor.getDuplicateChecklistFactors() == null
				|| editChecklistFactor.getDuplicateChecklistFactors().isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY, editChecklistFactor),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false,
					CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES, editChecklistFactor), HttpStatus.OK);
		}
	}
	

	@DeleteMapping("/checklist-factor")
	public ResponseEntity<SuccessResponse> deleteChecklistFactor( @RequestBody DeleteConfigurationDTO deleteConfigurationDTO) {
		service.deleteChecklistFactor(getCompanyId(), deleteConfigurationDTO.getDeletedValue());
		return new ResponseEntity<>(new SuccessResponse(false, CHECKLIST_FACTOR_DELETE_SUCCESSFULLY, null),
				HttpStatus.OK);

	}

	@GetMapping
	public ResponseEntity<SuccessResponse> configuration(@RequestParam Long companyId) {
		ConfigurationDto configuration = service.configuration(companyId);
		return new ResponseEntity<>(new SuccessResponse(false, "Configuration", configuration), HttpStatus.OK);

	}
	@GetMapping("/interview-round-list")
	public ResponseEntity<SuccessResponse> interviewRoundList(@RequestParam Long companyId){
		AddInterviewRoundDto interviewRoundList = service.interviewRoundList(companyId);
		
		return new ResponseEntity<>(new SuccessResponse(false, "InterviewRoundList", interviewRoundList), HttpStatus.OK);
		
	}
	@GetMapping("/entry-feedback-factor")
	public ResponseEntity<SuccessResponse> entryFeedbackFactor(@RequestParam Long companyId,@RequestParam String feedbackType){
		AddFeedbackDTO entryFeedbackFactor = service.entryFeedbackFactor(companyId,feedbackType);
		return new ResponseEntity<>(new SuccessResponse(false, "Feedback Factor List for Interview", entryFeedbackFactor), HttpStatus.OK);
		
	}
	
}
