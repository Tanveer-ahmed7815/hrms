package com.te.flinko.service.reportingmanager;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.reportingmanager.AppraisalMeetingFeedbackDTO;
import com.te.flinko.dto.reportingmanager.AppraisalMeetingListDto;
import com.te.flinko.dto.reportingmanager.EmployeeDetailsDTO;

public interface ReportingManagerService {

	List<AppraisalMeetingListDto> team(Long userId, String date);

	EmployeeDetailsDTO employeeDetail(Long meetingId, Long userId);

	EmployeeReviseSalaryDTO appraisalMeetingFeedback(AppraisalMeetingFeedbackDTO feedbackDTO);

}
