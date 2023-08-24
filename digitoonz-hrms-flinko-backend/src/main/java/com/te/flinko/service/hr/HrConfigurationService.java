package com.te.flinko.service.hr;

import com.te.flinko.dto.hr.AddFeedbackDTO;
import com.te.flinko.dto.hr.AddInterviewRoundDto;
import com.te.flinko.dto.hr.CompanyChecklistDTO;
import com.te.flinko.dto.hr.ConfigurationDto;
import com.te.flinko.dto.hr.EditInterviewRoundDto;


public interface HrConfigurationService {
	AddInterviewRoundDto addInterviewRounds(AddInterviewRoundDto interviewdetails);

	AddFeedbackDTO addFeedback(AddFeedbackDTO feedback);

	CompanyChecklistDTO checklistFactor(CompanyChecklistDTO checklistFactor);

	
	AddInterviewRoundDto editInterviewRoundDetails(EditInterviewRoundDto interviewdetails);

	AddInterviewRoundDto deleteInterviewRound(Long companyId, String roundName);

	AddFeedbackDTO editFeedback(AddFeedbackDTO feedback);

	AddFeedbackDTO deleteFeedbackFactor(Long companyId, String feedbackFactor,String feedbackType);

	CompanyChecklistDTO editChecklistFactor(CompanyChecklistDTO checklistFactor);

	CompanyChecklistDTO deleteChecklistFactor(Long companyId, String checklistFactor);

	ConfigurationDto configuration(long companyId);

	AddInterviewRoundDto interviewRoundList(Long companyId);

	AddFeedbackDTO entryFeedbackFactor(Long companyId, String feedbackType);

}
