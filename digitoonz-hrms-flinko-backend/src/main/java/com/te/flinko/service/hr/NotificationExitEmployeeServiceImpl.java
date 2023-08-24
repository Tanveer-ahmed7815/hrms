package com.te.flinko.service.hr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.hr.NotificationExitEmployeeDTO;
import com.te.flinko.dto.hr.NotificationExitInterviewDTO;
import com.te.flinko.dto.hr.NotificationExitInterviewDropdownDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeeAnnualSalary;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.entity.employee.EmployeeResignationDiscussion;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.admin.NoDataPresentException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.CompanyEmployeeResignationDetailsRepository;
import com.te.flinko.repository.employee.EmployeeAnnualSalaryRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;
import com.te.flinko.repository.employee.EmployeeResignationDiscussionRepository;
import com.te.flinko.repository.employee.ReportingManagerRepository;

@Service
public class NotificationExitEmployeeServiceImpl implements NotificationExitEmployeeService {

	@Autowired
	private EmployeeResignationDiscussionRepository employeeScheduleInterviewRepository;

	@Autowired
	private CompanyEmployeeResignationDetailsRepository exitEmployeeListRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfo;

	@Autowired
	private ReportingManagerRepository reportingRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private EmployeeReportingInfoRepository infoRepository;

	@Autowired
	private EmployeeAnnualSalaryRepository employeeAnnualSalaryRepository;

	// Api for saving the ExitInterview Schedule

	@Override
	public NotificationExitInterviewDTO scheduleInterview(NotificationExitInterviewDTO scheduleInterviewdto,
			Long resignationId) {

		CompanyEmployeeResignationDetails optionalResignationDetails = exitEmployeeListRepository
				.findById(resignationId).get();

		if (optionalResignationDetails != null) {

			EmployeeResignationDiscussion resignationDiscussions = new EmployeeResignationDiscussion();

			BeanUtils.copyProperties(scheduleInterviewdto, resignationDiscussions);
			resignationDiscussions.setStatus("Pending");

			List<Long> employeeInfoIdList = scheduleInterviewdto.getEmployeeInfoIdList();
			if (employeeInfoIdList == null || employeeInfoIdList.isEmpty()) {
				throw new DataNotFoundException("Attendees information cannot be empty");
			}

			List<EmployeePersonalInfo> employeePersonalInfo = personalInfo.findAllById(employeeInfoIdList);

			employeePersonalInfo.forEach(pi -> {
				List<EmployeeResignationDiscussion> listtemp = pi.getEmployeeResignationDiscussionList();
//				if(listtemp == null) {
//					listtemp = new ArrayList<>();
//				}
				listtemp.add(resignationDiscussions);
				pi.setEmployeeResignationDiscussionList(listtemp);
			});

			resignationDiscussions.setEmployeePersonalInfoList(employeePersonalInfo);
			resignationDiscussions.setCompanyEmployeeResignationDetails(optionalResignationDetails);

			BeanUtils.copyProperties(employeeScheduleInterviewRepository.save(resignationDiscussions),
					scheduleInterviewdto);

			return scheduleInterviewdto;

		} else {
			throw new DataNotFoundException("ResignationId Not Present");
		}

	}

	// Api for fetching the employees who have applied for resignation for a
	// company

	@Override
	public List<NotificationExitEmployeeDTO> resignationDetails(Long employeeInfoId, Long companyId) {

		List<Long> employeeInfoIdList = infoRepository.findByReportingHREmployeeInfoId(employeeInfoId).stream()
				.map(info -> info.getEmployeePersonalInfo().getEmployeeInfoId()).collect(Collectors.toList());

		List<CompanyEmployeeResignationDetails> resignationDetailsList = exitEmployeeListRepository
				.findByCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(companyId, employeeInfoIdList);
		
		List<NotificationExitEmployeeDTO> exitEmployeedtos = new ArrayList<>();

		for (CompanyEmployeeResignationDetails companyEmployeeResignationDetails : resignationDetailsList) {

			NotificationExitEmployeeDTO employeedto = new NotificationExitEmployeeDTO();

			EmployeePersonalInfo employeePersonalInfo = companyEmployeeResignationDetails.getEmployeePersonalInfo();
			if (employeePersonalInfo != null) {

				EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
				if (employeeOfficialInfo != null) {

					employeedto.setResignationId(companyEmployeeResignationDetails.getResignationId());

					employeedto.setEmployeeId(employeeOfficialInfo.getEmployeeId());
					employeedto.setFullName(
							employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
					employeedto.setOfficialEmailId(employeeOfficialInfo.getOfficialEmailId());
					employeedto.setDepartment(employeeOfficialInfo.getDepartment());
					employeedto.setDesignation(employeeOfficialInfo.getDesignation());
					List<EmployeeReportingInfo> reportingMangerInfoList = reportingRepository
							.findByEmployeePersonalInfo(companyEmployeeResignationDetails.getEmployeePersonalInfo());
					String reportingManagerName = null;
					if (reportingMangerInfoList.size() > 1) {
						EmployeeReportingInfo employeeReportingInfo = reportingMangerInfoList
								.get(reportingMangerInfoList.size() - 1);
						
						reportingManagerName = (employeeReportingInfo.getReportingManager() == null) ? null
								: employeeReportingInfo.getReportingManager().getFirstName() + " "
										+ employeeReportingInfo.getReportingManager().getLastName();
					} else {
						EmployeeReportingInfo employeeReportingInfo = reportingMangerInfoList.get(0);
						reportingManagerName = (employeeReportingInfo.getReportingManager() == null) ? null
								: employeeReportingInfo.getReportingManager().getFirstName() + " "
										+ employeeReportingInfo.getReportingManager().getLastName();
					}

					employeedto.setReportingManager(reportingManagerName);
					employeedto.setMobileNumber(employeePersonalInfo.getMobileNumber());

					exitEmployeedtos.add(employeedto);
				}
			}
		}
		return exitEmployeedtos;

	}

	// Api for fetching the employees for organizers dropdown
	@Override
	public List<NotificationExitInterviewDropdownDTO> getExitInterviewDropdowndtoList(Long companyId) {

		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {

			List<NotificationExitInterviewDropdownDTO> companyEmployeeList = new ArrayList<>();

			List<EmployeePersonalInfo> employeePersonalInfos = personalInfo.findByCompanyInfoCompanyId(companyId);
			if (!employeePersonalInfos.isEmpty()) {

				for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfos) {

					NotificationExitInterviewDropdownDTO companyEmployee = new NotificationExitInterviewDropdownDTO();

					companyEmployee.setEmployeeId(employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId());
					companyEmployee
							.setEmployeeName(employeePersonalInfo.getFirstName() + employeePersonalInfo.getLastName());
					companyEmployee.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());

					companyEmployeeList.add(companyEmployee);
				}

				return companyEmployeeList;
			}
		}
		throw new DataNotFoundException("Comapany Id Not present");

	}

	// Api for fetching the details of employees who have applied for resignation
	@Override
	public NotificationExitEmployeeDTO exitEmployeedto(Long resignatinId) {
		Optional<CompanyEmployeeResignationDetails> optionalDetails = exitEmployeeListRepository.findById(resignatinId);
		if (optionalDetails.isPresent()) {

			CompanyEmployeeResignationDetails employeeResignationDiscussion = optionalDetails.get();

			EmployeePersonalInfo employeePersonalInfo = employeeResignationDiscussion.getEmployeePersonalInfo();
			if (employeePersonalInfo == null) {
				throw new DataNotFoundException("Employee Details Not Found");
			}

			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

			if (employeeOfficialInfo == null) {
				throw new DataNotFoundException("Employee Oficial Details Not Found");
			}

			NotificationExitEmployeeDTO exitEmployeedto = new NotificationExitEmployeeDTO();
			exitEmployeedto.setResignationId(employeeResignationDiscussion.getResignationId());

			exitEmployeedto.setEmployeeId(employeeOfficialInfo.getEmployeeId());
			exitEmployeedto.setFullName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
			exitEmployeedto.setOfficialEmailId(employeeOfficialInfo.getOfficialEmailId());
			exitEmployeedto.setDepartment(employeeOfficialInfo.getDepartment());
			exitEmployeedto.setDesignation(employeeOfficialInfo.getDesignation());

			exitEmployeedto.setResignationReason(employeeResignationDiscussion.getResignationReason());
			List<EmployeeReportingInfo> reportingMangerInfoList = reportingRepository
					.findByEmployeePersonalInfo(employeePersonalInfo);
			String reportingManagerName = null;
			if (reportingMangerInfoList.size() > 1) {
				EmployeeReportingInfo employeeReportingInfo = reportingMangerInfoList
						.get(reportingMangerInfoList.size() - 1);
				reportingManagerName = (employeeReportingInfo.getReportingManager() == null) ? null
						: employeeReportingInfo.getReportingManager().getFirstName() + " "
								+ employeeReportingInfo.getReportingManager().getLastName();
			} else {
				EmployeeReportingInfo employeeReportingInfo = reportingMangerInfoList.get(0);
				reportingManagerName = (employeeReportingInfo.getReportingManager() == null) ? null
						: employeeReportingInfo.getReportingManager().getFirstName() + " "
								+ employeeReportingInfo.getReportingManager().getLastName();
			}
			exitEmployeedto.setReportingManager(reportingManagerName);
			exitEmployeedto.setMobileNumber(employeePersonalInfo.getMobileNumber());

			EmployeeAnnualSalary annualSalary = employeeAnnualSalaryRepository
					.findByEmployeePersonalInfo(employeePersonalInfo);
			exitEmployeedto.setAnnualSalary(annualSalary != null ? annualSalary.getAnnualSalary() : new BigDecimal(0));

			return exitEmployeedto;

		} else {
			throw new DataNotFoundException("ResignationId Not Found");
		}

	}

}