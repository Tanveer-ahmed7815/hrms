package com.te.flinko.service.reportingmanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.reportingmanager.AppraisalMeetingFeedbackDTO;
import com.te.flinko.dto.reportingmanager.AppraisalMeetingListDto;
import com.te.flinko.dto.reportingmanager.EmployeeDetailsDTO;
import com.te.flinko.entity.employee.ApprisalMeetingInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.entity.employee.EmployeeReviseSalary;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.employee.ApprisalMeetingInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReviseSalaryRepository;
import com.te.flinko.repository.employee.ReportingManagerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportingManagerServiceImpl implements ReportingManagerService {
	@Autowired
	private ReportingManagerRepository employeeReportingManagerRepo;
	@Autowired
	private ApprisalMeetingInfoRepository apprisalMeetingInfoRepo;
	@Autowired
	private EmployeeReviseSalaryRepository employeeReviseSalaryRepo;

	@Override
	public List<AppraisalMeetingListDto> team(Long userId, String date) {
		List<AppraisalMeetingListDto> apprisal = new ArrayList<>();
		List<EmployeeReportingInfo> reportingData = employeeReportingManagerRepo
				.findByReportingManagerEmployeeInfoId(userId);
		for (EmployeeReportingInfo employeeReportingInfo : reportingData) {
			if (employeeReportingInfo != null && employeeReportingInfo.getEmployeePersonalInfo() != null
					&& employeeReportingInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null
					&& (!employeeReportingInfo.getEmployeePersonalInfo().getApprisalMeetingInfoList().isEmpty())) {
				if (date.equalsIgnoreCase("today") && employeeReportingInfo.getEmployeePersonalInfo()
						.getApprisalMeetingInfoList().get(0).getMeetingDate().equals(LocalDate.now())) {
					if (employeeReportingInfo.getEmployeePersonalInfo().getEmployeeReviseSalaryList().get(0)
							.getReason() == (null)
							&& employeeReportingInfo.getEmployeePersonalInfo().getEmployeeReviseSalaryList().get(0)
									.getAmount() == (null)) {
						addData(apprisal, employeeReportingInfo, false);
					} else {
						addData(apprisal, employeeReportingInfo, true);

					}
				}
				if (date.equalsIgnoreCase("previous") && employeeReportingInfo.getEmployeePersonalInfo()
						.getApprisalMeetingInfoList().get(0).getMeetingDate().isBefore(LocalDate.now())) {
					if (employeeReportingInfo.getEmployeePersonalInfo().getEmployeeReviseSalaryList().get(0)
							.getReason() == (null)
							&& employeeReportingInfo.getEmployeePersonalInfo().getEmployeeReviseSalaryList().get(0)
									.getAmount() == (null)) {

						addData(apprisal, employeeReportingInfo, false);
					} else {
						addData(apprisal, employeeReportingInfo, true);
					}
				}
				if (date.equalsIgnoreCase("upcoming") && employeeReportingInfo.getEmployeePersonalInfo()
						.getApprisalMeetingInfoList().get(0).getMeetingDate().isAfter(LocalDate.now())) {
					addData(apprisal, employeeReportingInfo, false);
				}

			}

		}
		log.info("Appraisal details fetched successfully");
		return apprisal;
	}

	private void addData(List<AppraisalMeetingListDto> apprisal, EmployeeReportingInfo employeeReportingInfo,
			boolean b) {

		apprisal.add(new AppraisalMeetingListDto(
				employeeReportingInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId(),
				employeeReportingInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId(),
				employeeReportingInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDesignation(),
				employeeReportingInfo.getEmployeePersonalInfo().getApprisalMeetingInfoList().get(0).getMeetingType(),
				employeeReportingInfo.getEmployeePersonalInfo().getApprisalMeetingInfoList().get(0).getMeetingId(), b));

	}

	@Override
	public EmployeeDetailsDTO employeeDetail(Long meetingId, Long userId) {
		ApprisalMeetingInfo meetingInfo = apprisalMeetingInfoRepo.findById(meetingId)
				.orElseThrow(() -> new CustomExceptionForHr("Apprisal meeting details not found"));
		EmployeePersonalInfo employeePersonalInfo = meetingInfo.getEmployeeReviseSalary().getEmployeePersonalInfo();
		EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
		BeanUtils.copyProperties(meetingInfo, employeeDetailsDTO);
		employeeDetailsDTO.setDepartment(employeePersonalInfo.getEmployeeOfficialInfo().getDepartment());
		employeeDetailsDTO.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
		employeeDetailsDTO.setEmployeeId(employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId());
		employeeDetailsDTO.setFullName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
		ArrayList<String> arrayList = new ArrayList<>();
		employeeDetailsDTO.setOfficialEmailId(employeePersonalInfo.getEmployeeOfficialInfo().getOfficialEmailId());
		employeeDetailsDTO.setMobileNumber(employeePersonalInfo.getMobileNumber());
		List<ProjectDetails> allocatedProjectList = employeePersonalInfo.getAllocatedProjectList();
		for (ProjectDetails projectDetails : allocatedProjectList) {
			arrayList.add(projectDetails.getProjectName());
		}
		if (meetingInfo.getEmployeeReviseSalary().getAmount() == null
				&& meetingInfo.getEmployeeReviseSalary().getReason() == null) {
			employeeDetailsDTO.setIsFeedbackGiven(false);
		}
		if ((meetingInfo.getEmployeeReviseSalary().getAmount() != null)
				&& (meetingInfo.getEmployeeReviseSalary().getReason() != null)) {
			employeeDetailsDTO.setIsFeedbackGiven(true);
		}

		employeeDetailsDTO.setProjectList(arrayList);
		employeeDetailsDTO.setDuration(meetingInfo.getDuration());
		log.info("Appraisal details fetched successfully");
		return employeeDetailsDTO;
	}

	@Override
	public EmployeeReviseSalaryDTO appraisalMeetingFeedback(AppraisalMeetingFeedbackDTO feedbackDTO) {
		EmployeeReviseSalaryDTO employeeReviseSalaryDTO = new EmployeeReviseSalaryDTO();
		ApprisalMeetingInfo meetingInfo = apprisalMeetingInfoRepo.findById(feedbackDTO.getMeetingId())
				.orElseThrow(() -> new CustomExceptionForHr("Apprisal meeting details not found"));
		if ((meetingInfo.getEmployeeReviseSalary().getReason() != null)
				&& (meetingInfo.getEmployeeReviseSalary().getAmount() != null)) {
			log.info("Appraisal meeting Feedback already given");
			throw new CustomExceptionForHr("Appraisal meeting Feedback already given");
		}
		if ((meetingInfo.getMeetingDate().isBefore(LocalDate.now()))
				|| (meetingInfo.getMeetingDate().isEqual(LocalDate.now()))) {
			if (meetingInfo.getStartTime().isBefore(LocalTime.now())
					|| meetingInfo.getStartTime() == (LocalTime.now())) {
				EmployeeReviseSalary employeeReviseSalary = employeeReviseSalaryRepo
						.findById(meetingInfo.getEmployeeReviseSalary().getReviseSalaryId())
						.orElseThrow(() -> new CustomExceptionForHr("Employee revised salary details not found"));

				employeeReviseSalary.setReason(feedbackDTO.getReason());
				employeeReviseSalary.setAmount(feedbackDTO.getRevisedSalary());
				EmployeeReviseSalary reviseSalaryInfo = employeeReviseSalaryRepo.save(employeeReviseSalary);

				BeanUtils.copyProperties(reviseSalaryInfo, employeeReviseSalaryDTO);
			} else {
				log.info("Appraisal meeting is not yet Started");
				throw new CustomExceptionForHr("Appraisal meeting is not yet started");
			}
		} else {
			log.info("Upcomming appraisal feedback is not possible");
			throw new CustomExceptionForHr("Appraisal meeting is not yet completed");
		}
		log.info("Appraisal meeting feedback details updated successfully");
		return employeeReviseSalaryDTO;
	}
}
