package com.te.flinko.service.reportingmanager;

import java.util.List;

import com.te.flinko.dto.reportingmanager.ApprisalMeetingDTO;
import com.te.flinko.dto.reportingmanager.EditInterviewDTO;
import com.te.flinko.dto.reportingmanager.EligibleEmployeeDetailsDTO;
import com.te.flinko.dto.reportingmanager.ScheduleInterviewDTO;

public interface NotificationListService {

	public List<EligibleEmployeeDetailsDTO> getEmployeeList(Long employeeInfoId, Long companyId);

	public ApprisalMeetingDTO getEmployee(Long employeeInfoId, Long companyId);
	
	public ScheduleInterviewDTO addInterview(ScheduleInterviewDTO interviewDTO, Long employeeInfoId,Long companyId,Long userId);
	
	public EditInterviewDTO editInterview(EditInterviewDTO editInterviewDTO,Long employeeInfoId,Long meetingId,Long companyId,Long userId);

}
