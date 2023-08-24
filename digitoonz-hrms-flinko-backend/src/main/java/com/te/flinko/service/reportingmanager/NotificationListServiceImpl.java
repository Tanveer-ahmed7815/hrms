package com.te.flinko.service.reportingmanager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.reportingmanager.ApprisalMeetingDTO;
import com.te.flinko.dto.reportingmanager.EditInterviewDTO;
import com.te.flinko.dto.reportingmanager.EligibleEmployeeDetailsDTO;
import com.te.flinko.dto.reportingmanager.EmployeeDetailsDTO;
import com.te.flinko.dto.reportingmanager.ProjectDetailsDTO;
import com.te.flinko.dto.reportingmanager.ScheduleInterviewDTO;
import com.te.flinko.entity.employee.ApprisalMeetingInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.entity.employee.EmployeeReviseSalary;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.exception.admin.NoDataPresentException;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.employee.ApprisalMeetingInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;

@Service
public class NotificationListServiceImpl implements NotificationListService {

	@Autowired
	EmployeeReportingInfoRepository reportingInfoRepository;

	@Autowired
	EmployeePersonalInfoRepository infoRepository;

	@Autowired
	ApprisalMeetingInfoRepository meetingInfoRepository;

	@Override
	public List<EligibleEmployeeDetailsDTO> getEmployeeList(Long employeeInfoId, Long companyId) {
		List<EmployeeReportingInfo> employeeReportingInfoList = reportingInfoRepository
				.findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeeReportingInfoList.isEmpty()) {
			throw new NoDataPresentException("Info Not Found");

		}

		List<EligibleEmployeeDetailsDTO> detailsDTOList = new ArrayList<>();

		for (EmployeeReportingInfo employeeReportingInfo : employeeReportingInfoList) {

			EligibleEmployeeDetailsDTO detailsDTO = new EligibleEmployeeDetailsDTO();
			EmployeePersonalInfo employeePersonalInfo = employeeReportingInfo.getEmployeePersonalInfo();
			if (employeePersonalInfo != null) {
				List<EmployeeReviseSalary> employeeReviseSalary = employeePersonalInfo.getEmployeeReviseSalaryList();
				EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
				if (employeeOfficialInfo != null) {

					if (!employeePersonalInfo.getEmployeeReviseSalaryList().isEmpty()) {

						EmployeeReviseSalary reviseSalary = employeeReviseSalary.get(employeeReviseSalary.size() - 1);
						if (reviseSalary.getAmount() != null) {
							if (reviseSalary.getAmount().intValue() == 0 && reviseSalary.getReason() == null) {
								detailsDTO.setEmployeeId(employeeReportingInfo.getEmployeePersonalInfo()
										.getEmployeeOfficialInfo().getEmployeeId());
								detailsDTO.setEmployeeInfoId(
										employeeReportingInfo.getEmployeePersonalInfo().getEmployeeInfoId());
								detailsDTO.setFullName(employeeReportingInfo.getEmployeePersonalInfo().getFirstName()
										+ " " + employeeReportingInfo.getEmployeePersonalInfo().getLastName());
								detailsDTO.setEmailId(employeeReportingInfo.getEmployeePersonalInfo()
										.getEmployeeOfficialInfo().getOfficialEmailId());
								detailsDTO.setDepartment(employeeReportingInfo.getEmployeePersonalInfo()
										.getEmployeeOfficialInfo().getDepartment());
								detailsDTO.setDesignation(employeeReportingInfo.getEmployeePersonalInfo()
										.getEmployeeOfficialInfo().getDesignation());
								detailsDTOList.add(detailsDTO);
							}

						}
					}
				}
			}
		}
		return detailsDTOList;
	}

	@Override
	public ApprisalMeetingDTO getEmployee(Long employeeInfoId, Long companyId) {

		List<EmployeePersonalInfo> personalInfos = infoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (personalInfos.isEmpty()) {
			throw new DataNotFoundException("Info Not Found");
		}
		EmployeeOfficialInfo employeeOfficialInfo = personalInfos.get(0).getEmployeeOfficialInfo();

		if (employeeOfficialInfo == null) {
			throw new DataNotFoundException("OfficialInfo Not Found");
		}
		ApprisalMeetingDTO detailsDTO = new ApprisalMeetingDTO();

		BeanUtils.copyProperties(employeeOfficialInfo, detailsDTO);
		detailsDTO.setFullName(personalInfos.get(0).getFirstName() + " " + personalInfos.get(0).getLastName());
		detailsDTO.setEmployeeInfoId(personalInfos.get(0).getEmployeeInfoId());

		List<EmployeeReviseSalary> employeeReviseSalaryList = personalInfos.get(0).getEmployeeReviseSalaryList();
		EmployeeReviseSalary employeeReviseSalary = employeeReviseSalaryList.get(employeeReviseSalaryList.size() - 1);
		detailsDTO.setDueDate(employeeReviseSalary.getApprisalDate());

		detailsDTO.setMobileNumber(personalInfos.get(0).getMobileNumber());
		List<ProjectDetails> allocatedProjectList = personalInfos.get(0).getAllocatedProjectList();
		List<ProjectDetailsDTO> projectDetailsDTOs = new ArrayList<>();
		for (ProjectDetails projectDetails : allocatedProjectList) {
			ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO();
			projectDetailsDTO.setProjectId(projectDetails.getProjectId());
			projectDetailsDTO.setProjectName(projectDetails.getProjectName());
			projectDetailsDTOs.add(projectDetailsDTO);
		}

		detailsDTO.setProject(projectDetailsDTOs);
		detailsDTO.setDueDate(personalInfos.get(0).getEmployeeReviseSalaryList().get(0).getApprisalDate());

		return detailsDTO;
	}

	@Override
	public ScheduleInterviewDTO addInterview(ScheduleInterviewDTO interviewDTO, Long employeeInfoId, Long companyId,
			Long userId) {

		List<EmployeePersonalInfo> personalInfos = infoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (personalInfos.isEmpty()) {
			throw new DataNotFoundException("Info Not Found");
		}
		EmployeePersonalInfo info = personalInfos.get(0);
		if (info.getEmployeeReviseSalaryList()==null || info.getEmployeeReviseSalaryList().isEmpty()) {
			throw new DataNotFoundException("RevisedSalary Info Not Found");
		}

		EmployeeOfficialInfo employeeOfficialInfo = info.getEmployeeOfficialInfo();

		if (employeeOfficialInfo == null) {
			throw new DataNotFoundException("OfficialInfo Not Found");
		}

		ApprisalMeetingInfo apprisalMeetingInfo = new ApprisalMeetingInfo();

		BeanUtils.copyProperties(interviewDTO, apprisalMeetingInfo);

		List<Long> employeeInfoIdList = interviewDTO.getEmployeeInfoIdList();
		if (employeeInfoIdList.isEmpty()) {
			throw new DataNotFoundException("Attendees information cannot be empty");
		}
		employeeInfoIdList.add(userId);
		

		List<EmployeePersonalInfo> employeePersonalInfo = infoRepository.findAllById(employeeInfoIdList);
		employeePersonalInfo.forEach(pi -> {
			List<ApprisalMeetingInfo> listtemp = pi.getApprisalMeetingInfoList();
//			if(listtemp == null) {
//				listtemp = new ArrayList<>();
//			}
			listtemp.add(apprisalMeetingInfo);
			pi.setApprisalMeetingInfoList(listtemp);
		});
		apprisalMeetingInfo.setStatus("Pending");
		apprisalMeetingInfo.setEmployeePersonalInfoList(employeePersonalInfo);
		apprisalMeetingInfo.setEmployeeReviseSalary(info.getEmployeeReviseSalaryList().get(0));
		BeanUtils.copyProperties(meetingInfoRepository.save(apprisalMeetingInfo), interviewDTO);

		return interviewDTO;
	}

	@Override
	public EditInterviewDTO editInterview(EditInterviewDTO editInterviewDTO, Long employeeInfoId, Long meetingId,
			Long companyId, Long userId) {

		ApprisalMeetingInfo meetingInfo = meetingInfoRepository
				.findByMeetingIdAndEmployeeReviseSalaryCompanyInfoCompanyId(meetingId, companyId);

		if (meetingInfo == null) {
			throw new DataNotFoundException("Meeting Details Not Found");
		} else {

			List<EmployeePersonalInfo> personalInfos = infoRepository
					.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

			if (personalInfos.isEmpty()) {
				throw new DataNotFoundException("Info Not Found");
			}
			EmployeePersonalInfo info = personalInfos.get(0);
			if (info.getEmployeeReviseSalaryList().isEmpty()) {
				throw new DataNotFoundException("Revised Salary Info Not Found");
			}
			EmployeeOfficialInfo employeeOfficialInfo = info.getEmployeeOfficialInfo();

			if (employeeOfficialInfo == null) {
				throw new DataNotFoundException("OfficialInfo Not Found");
			}

			List<Long> employeeInfoIdList = editInterviewDTO.getEmployeeInfoIdList();
			if (employeeInfoIdList.isEmpty()) {
				throw new DataNotFoundException("Attendees information cannot be empty");
			}
			employeeInfoIdList.add(userId);
			

			List<EmployeePersonalInfo> newAttendiesInfo = infoRepository.findAllById(employeeInfoIdList);
			List<EmployeePersonalInfo> meetingAttendeesList = meetingInfo.getEmployeePersonalInfoList();

			meetingAttendeesList.stream().forEach(employee -> {
				if (!newAttendiesInfo.contains(employee)) {
					employee.getApprisalMeetingInfoList().remove(meetingInfo);
				} else if (newAttendiesInfo.contains(employee)) {
					newAttendiesInfo.remove(employee);
				}
			});

			newAttendiesInfo.stream().forEach(attendies -> {
				attendies.getApprisalMeetingInfoList().add(meetingInfo);
			});

			BeanUtils.copyProperties(editInterviewDTO, meetingInfo);

			meetingInfo.setStatus("Pending");
			meetingInfo.setEmployeePersonalInfoList(newAttendiesInfo);
			meetingInfo.setEmployeeReviseSalary(info.getEmployeeReviseSalaryList().get(0));
			BeanUtils.copyProperties(meetingInfoRepository.save(meetingInfo), editInterviewDTO);

			return editInterviewDTO;
		}
	}

}
