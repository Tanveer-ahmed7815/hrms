package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.hr.BranchNameFetchDTO;
import com.te.flinko.dto.hr.EventManagementAddEventDTO;
import com.te.flinko.dto.hr.EventManagementAddWinnerDTO;
import com.te.flinko.dto.hr.EventManagementDepartmentNameDTO;
import com.te.flinko.dto.hr.EventManagementDisplayEventDTO;
import com.te.flinko.dto.hr.EventManagementEditEventDTO;
import com.te.flinko.dto.hr.EventManagementNameFetchDTO;
import com.te.flinko.dto.hr.EventManagementParticipantslistDTO;

public interface CompanyEventDetailsService {
	// Adding New Event Api...
	public EventManagementAddEventDTO adddetails(EventManagementAddEventDTO eventdto,Long companyId);
	// Department names getting Api..
	public List<EventManagementDepartmentNameDTO> fetchDepartmentNames();
	// Event displaying api based on eventId..
	public EventManagementDisplayEventDTO displayEventDetails(Long eventId);
	// for uploading the winner Api..
	public EventManagementAddWinnerDTO addWinners(EventManagementAddWinnerDTO addWinnerdto,Long eventId);
	// For Editing the Event
	public EventManagementEditEventDTO editEvent(EventManagementEditEventDTO editEventdto,Long eventId);
	// for fetching the employee from EmployeePersonelInfo Api..	
	public List<EventManagementNameFetchDTO> fetchEmployeeNames(Long companyId);
	// For Deleting the Event Api..
	public void deleteEvent(Long eventId);
	// Fetching participants list for selecting Winners Api..
	public List<EventManagementParticipantslistDTO> getParticipantsList(Long eventId);
	//for fetching employees from list of Department
	public List<EventManagementNameFetchDTO> fetchMultipleDepartmentEmployees(Long companyId,List<String> department);
	//for fetching brandid 
	public List<BranchNameFetchDTO> fetchBranchNames(Long companyId);
	//for displaying eventList 
	public List<EventManagementDisplayEventDTO> displayEventList(Long companyId,int year,int month);
	
}
