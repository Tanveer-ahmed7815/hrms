package com.te.flinko.service.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.EmployeeProjectListDTO;
import com.te.flinko.dto.employee.TimesheetDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTaskListDTO;
import com.te.flinko.dto.employee.mongo.EmployeeTimesheetDetailsDTO;
import com.te.flinko.dto.employee.mongo.Timesheet;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.mongo.EmployeeTimesheetDetails;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.project.mongo.ProjectTaskDetails;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.EmployeeTimeSheetCannottEditedException;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.mongo.EmployeeTimesheetDetailsRepository;
import com.te.flinko.repository.project.mongo.ProjectTaskDetailsRepository;

@Service
public class EmployeeTimeSheetServiceImpl implements EmployeeTimeSheetService {

	@Autowired
	EmployeeTimesheetDetailsRepository timeSheetRepository;

	@Autowired
	EmployeePersonalInfoRepository personalInfoRepository;

	@Autowired
	ProjectTaskDetailsRepository taskRepository;

	@Override
	public List<EmployeeProjectListDTO> getProjectList(Long employeeInfoId, Long companyId) {

		ArrayList<EmployeeProjectListDTO> projectListDTO = new ArrayList<>();
		List<EmployeePersonalInfo> employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (!employeePersonalInfo.isEmpty()) {

			List<ProjectDetails> allotedProjectList = employeePersonalInfo.get(0).getAllocatedProjectList();

			if (allotedProjectList.isEmpty()) {

				throw new DataNotFoundException("Project Not Assigned");
			}
			for (ProjectDetails projectDetails : allotedProjectList) {

				EmployeeProjectListDTO DTO = new EmployeeProjectListDTO();
				DTO.setProjectId(projectDetails.getProjectId());
				DTO.setProjectName(projectDetails.getProjectName());

				projectListDTO.add(DTO);
			}
			return projectListDTO;
		} else {
			throw new DataNotFoundException("Employee Not Found");
		}
	}

//TODO taskId should be Autogenerated
	@Override
	public ProjectTaskDetails saveProjectTaskDetails(ProjectTaskDetails projectTaskDetails) {

		return taskRepository.save(projectTaskDetails);
	}

	@Override
	public List<EmployeeTaskListDTO> getTaskList(Long employeeInfoId, List<Long> projectIdList, Long companyId) {

		List<EmployeeTaskListDTO> taskListDTO = new ArrayList<>();

		List<EmployeePersonalInfo> personalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (personalInfo.isEmpty())
			throw new DataNotFoundException("Employee Not Found");

		if (personalInfo.get(0).getEmployeeOfficialInfo() == null) {
			throw new DataNotFoundException("Employee OfficialInfo Not Found");
		}
		List<ProjectDetails> allocatedProjectList = personalInfo.get(0).getAllocatedProjectList();

		if (allocatedProjectList.isEmpty())
			throw new DataNotFoundException("Project Not Allocated");

		for (ProjectDetails projectDetail : allocatedProjectList) {

			List<ProjectTaskDetails> taskLists = taskRepository.findByProjectId(projectDetail.getProjectId());

			for (ProjectTaskDetails taskDetail : taskLists) {
				if ((taskDetail.getAssignedEmployee()!=null)&&(taskDetail.getAssignedEmployee())
						.equals((personalInfo.get(0).getEmployeeOfficialInfo().getEmployeeId()))) {
					taskListDTO.add(new EmployeeTaskListDTO(taskDetail.getTaskId(), taskDetail.getTaskName()));
				}
			}
		}
		if (taskListDTO.isEmpty())
			throw new DataNotFoundException("Task Not Assigned");

		return taskListDTO;
	}

	// TODO Timesheet Id has to be auto-generated
	@Override
	public EmployeeTimesheetDetailsDTO saveEmployeeTimesheetDetails(
			EmployeeTimesheetDetailsDTO employeeTimesheetDetailsDTO, Long employeeInfoId, Long companyId) {

		List<EmployeePersonalInfo> employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeePersonalInfo.isEmpty()) {
			throw new DataNotFoundException("PersonalInfo Not Found");
		}
		if (employeePersonalInfo.get(0).getEmployeeOfficialInfo() == null) {
			throw new DataNotFoundException("OfficialInfo Not Found");
		}
		EmployeeTimesheetDetails employeeTimesheetDetails = new EmployeeTimesheetDetails();
		List<Timesheet> dtoTimesheets = employeeTimesheetDetailsDTO.getTimesheets();
		for (int i = 0; i < dtoTimesheets.size(); i++) {
			Timesheet timesheet = dtoTimesheets.get(i);
			timesheet.setId(Integer.toString(i+1));
		}
		BeanUtils.copyProperties(employeeTimesheetDetailsDTO, employeeTimesheetDetails);
		
		employeeTimesheetDetails.setEmployeeId(employeePersonalInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
		employeeTimesheetDetails.setCompanyId(companyId);
		employeeTimesheetDetails.setIsApproved(Boolean.FALSE);
		employeeTimesheetDetails.setRejectedBy(employeeTimesheetDetailsDTO.getRejectedBy());
		employeeTimesheetDetails.setRejectionReason(employeeTimesheetDetailsDTO.getRejectionReason());
		BeanUtils.copyProperties(timeSheetRepository.save(employeeTimesheetDetails), employeeTimesheetDetailsDTO);
		employeeTimesheetDetailsDTO.setId(employeeTimesheetDetails.getTimesheetObjectId());

		return employeeTimesheetDetailsDTO;
	}

	@Override
	public List<EmployeeTimesheetDetailsDTO> getTimeSheet(List<String> projectList, Long CompanyId, Long employeeInfoId, int year,
			int month) {

		List<EmployeePersonalInfo> employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, CompanyId);

		if (employeePersonalInfo.isEmpty()) {
			throw new DataNotFoundException("PersonalInfo Not found");
		}
		Optional<List<EmployeeTimesheetDetails>> employeeTimeSheetList = timeSheetRepository
				.findByCompanyIdAndEmployeeId(CompanyId,
						employeePersonalInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
		
		if (employeeTimeSheetList.isEmpty()) {
			throw new DataNotFoundException("EmployeeTimeSheet Details Not Found");
		}
		List<EmployeeTimesheetDetailsDTO> employeeTimesheetDetailsDTOList = new ArrayList();
			
		List<Timesheet> timesheetlist = new ArrayList<>();
		
		for (EmployeeTimesheetDetails employeeTimesheetDetails : employeeTimeSheetList.get()) {
			
			EmployeeTimesheetDetailsDTO employeeTimesheetDetailsDTO = new EmployeeTimesheetDetailsDTO();
			for (Timesheet timesheet : employeeTimesheetDetails.getTimesheets()) {
	
				Timesheet timesheetDTO = new Timesheet();
				if (timesheet.getDate().getYear() == year) {
					if (timesheet.getDate().getMonthValue() == month) {
						if (!projectList.isEmpty()) {
							for (String projectName : projectList) {
								if (timesheet.getProject().equalsIgnoreCase(projectName)) {
									BeanUtils.copyProperties(timesheet, timesheetDTO);
									
									timesheetlist.add(timesheetDTO);
								}
							}
						} else {
							BeanUtils.copyProperties(timesheet, timesheetDTO);

						}
					}
				}
			}
			BeanUtils.copyProperties(employeeTimesheetDetails, employeeTimesheetDetailsDTO);
			employeeTimesheetDetailsDTO.setId(employeeTimesheetDetails.getTimesheetObjectId());
			employeeTimesheetDetailsDTOList.add(employeeTimesheetDetailsDTO);
		}
		return employeeTimesheetDetailsDTOList;
	}

	@Override
	public List<Timesheet> editEmployeeTimeSheet(Timesheet timesheetDTO, Long employeeInfoId, String timesheetObjectId,
			Long companyId) {

		List<EmployeePersonalInfo> employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (employeePersonalInfo.isEmpty()) {
			throw new DataNotFoundException("PersonalInfo Not Found");
		}
		EmployeeTimesheetDetails employeeTimeSheetDetails = timeSheetRepository.
				findByTimesheetObjectIdAndCompanyId(timesheetObjectId, companyId).get();

		if (employeeTimeSheetDetails == null) {
			throw new DataNotFoundException("Timesheet does Not Found");
		}
		if (employeeTimeSheetDetails.getIsApproved() == Boolean.FALSE) {

			List<Timesheet> newTimeSheets = new ArrayList<>();

			if (!employeeTimeSheetDetails.getTimesheets().isEmpty()) {
				List<Timesheet> oldTimesheets = employeeTimeSheetDetails.getTimesheets();
				for (Timesheet oldtimesheet : oldTimesheets) {
					if (oldtimesheet.getId().equals(timesheetDTO.getId())) {
						newTimeSheets.add(timesheetDTO);
					} else {
						newTimeSheets.add(oldtimesheet);
					}
				}
				employeeTimeSheetDetails.setTimesheets(newTimeSheets);
			} else {
				newTimeSheets.add(timesheetDTO);
				employeeTimeSheetDetails.setTimesheets(newTimeSheets);
			}
			timeSheetRepository.save(employeeTimeSheetDetails);

			return newTimeSheets;
		} else {
			throw new EmployeeTimeSheetCannottEditedException("Request cant be edited");
		}
	}

	@Override
	public void deleteEmployeeTimeSheet(Long employeeInfoId, String timesheetObjectId, String id, Long companyId) {

		List<EmployeePersonalInfo> employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeePersonalInfo.isEmpty()) {
			throw new DataNotFoundException("PersonalInfo No Found");
		}
		EmployeeTimesheetDetails employeeTimesheetDetails = timeSheetRepository.
				findByTimesheetObjectIdAndCompanyId(timesheetObjectId, companyId).get();
		if (employeeTimesheetDetails == null) {
			throw new DataNotFoundException("Employee Timesheet Details Not Found");
		}
		List<Timesheet> oldTimesheets = employeeTimesheetDetails.getTimesheets();
		List<Timesheet> newTimesheets = new ArrayList();
		for (Timesheet timesheet : oldTimesheets) {
			if (!(timesheet.getId().equals(id))) {
				newTimesheets.add(timesheet);
			}
		}
		employeeTimesheetDetails.setTimesheets(newTimesheets);
		timeSheetRepository.save(employeeTimesheetDetails);
	}
}