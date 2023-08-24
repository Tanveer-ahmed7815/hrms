package com.te.flinko.service.hr.mongo;

import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.HR;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.HR_APPROVAL_NOT_REQUIRED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.RM;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.ADMIN;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.APPROVED;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.PERSONAL_DETAILS_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.REJECTED;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.STATUS_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_GET_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_UPDATE_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_APPROVED_SUCCESSFULLY;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_DOES_NOT_EXIST_WITH_EMPLOYEE_ID;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_REJECTED_BY_ADMIN;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.UPDATE_EMPLOYEE_TIMESHEET;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.mongo.EmployeeTimeSheetDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.mongo.EmployeeTimesheetDetails;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;
import com.te.flinko.repository.employee.mongo.EmployeeTimesheetDetailsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class HRTimesheetApprovalServiceImpli implements HRTimesheetApprovalService {

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final EmployeeTimesheetDetailsRepository employeeTimesheetDetailsRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status,
			Long employeeInfoId) {
		List<String> timesheetLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getTimeSheet).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!timesheetLevels.contains(HR)) {
			throw new DataNotFoundException(HR_APPROVAL_NOT_REQUIRED);
		}

		List<String> employeeIdList = employeeReportingInfoRepository.findByReportingHREmployeeInfoId(employeeInfoId)
				.stream().filter(employee -> employee.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(employee -> employee.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId())
				.collect(Collectors.toList());

		Optional<List<EmployeeTimesheetDetails>> timeSheetOptionalList = employeeTimesheetDetailsRepository
				.findByCompanyIdAndEmployeeIdIn(companyId, employeeIdList);

		List<EmployeeTimeSheetDTO> employeeTimeSheetDTOList = new ArrayList<>();

		if (timeSheetOptionalList.isPresent()) {
			List<EmployeeTimesheetDetails> timeSheetList = timeSheetOptionalList.get();
			timeSheetList = filterTimesheetBasedOnCondition(status, timeSheetList, timesheetLevels);

			for (EmployeeTimesheetDetails employeeTimesheetDetails : timeSheetList) {
				Optional<List<EmployeePersonalInfo>> employeePersonalInfoOptional = employeePersonalInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId,
								employeeTimesheetDetails.getEmployeeId());
				if (!employeePersonalInfoOptional.isEmpty()) {
					EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoOptional.get().get(0);
					employeeTimeSheetDTOList
							.add(getTimesheetDetails(employeePersonalInfo, status, employeeTimesheetDetails));
				}
			}
		}
		return employeeTimeSheetDTOList;
	}

	private EmployeeTimeSheetDTO getTimesheetDetails(EmployeePersonalInfo employeePersonalInfo, String status,
			EmployeeTimesheetDetails employeeTimesheetDetails) {
		String pendingAt = null;

		if (status.equalsIgnoreCase(PENDING)) {
			pendingAt = (employeeTimesheetDetails.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
		}
		return EmployeeTimeSheetDTO.builder()
				.employeeName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
				.designation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation())
				.timesheetObjectId(employeeTimesheetDetails.getTimesheetObjectId())
				.department(employeePersonalInfo.getEmployeeOfficialInfo().getDepartment())
				.employeeId(employeeTimesheetDetails.getEmployeeId())
				.startDate(employeeTimesheetDetails.getTimesheets().get(0).getDate())
				.endDate(employeeTimesheetDetails.getTimesheets()
						.get(employeeTimesheetDetails.getTimesheets().size() - 1).getDate())
				.reason(employeeTimesheetDetails.getRejectionReason())
				.rejectedBy(employeeTimesheetDetails.getRejectionReason()).pendingAt(pendingAt)
				.isActionRequired(status.equalsIgnoreCase(PENDING)
						? !(employeeTimesheetDetails.getApprovedBy().keySet().contains(HR))
						: null)
				.build();
	}

	private List<EmployeeTimesheetDetails> filterTimesheetBasedOnCondition(String status,
			List<EmployeeTimesheetDetails> timeSheetList, List<String> levels) {
		if (status.equalsIgnoreCase(APPROVED)) {
			return timeSheetList.stream()
					.filter(timesheet -> timesheet.getApprovedBy().keySet().contains(levels.get(levels.size() - 1)))
					.collect(Collectors.toList());
		} else if (status.equalsIgnoreCase(PENDING)) {
			return timeSheetList.stream().filter(timesheet -> timesheet.getRejectedBy() == null
					&& !timesheet.getIsApproved() && timesheet.getApprovedBy().keySet().contains(RM))
					.collect(Collectors.toList());
		} else {
			return timeSheetList.stream().filter(timesheet -> timesheet.getRejectedBy() != null)
					.collect(Collectors.toList());
		}
	}

	@Override
	public EmployeeTimeSheetDTO getEmployeeTimesheetDetails(String timesheetObjectId, Long companyId) {

		EmployeeTimeSheetDTO timeSheetDto = employeeTimesheetDetailsRepository
				.findByTimesheetObjectIdAndCompanyId(timesheetObjectId, companyId)
				.map(x -> employeePersonalInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId, x.getEmployeeId())
						.map(yy -> {
							EmployeePersonalInfo y=yy.get(0);
							String pendingAt = null;

							if (!x.getIsApproved().booleanValue() && x.getRejectedBy() == null) {
								pendingAt = (x.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
							}
							EmployeeOfficialInfo employeeOfficialInfo = y.getEmployeeOfficialInfo();
							return EmployeeTimeSheetDTO.builder().timesheetId(x.getTimesheetId())
									.employeeId(x.getEmployeeId()).timesheetObjectId(x.getTimesheetObjectId())
									.department(employeeOfficialInfo.getDepartment())
									.designation(employeeOfficialInfo.getDesignation())
									.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
									.employeeName(y.getFirstName() + " " + y.getLastName())
									.startDate(x.getTimesheets().get(0).getDate())
									.endDate(x.getTimesheets().get(x.getTimesheets().size() - 1).getDate())
									.timesheets(x.getTimesheets()).reason(x.getRejectionReason())
									.rejectedBy(x.getRejectionReason())
									.isActionRequired((!x.getIsApproved().booleanValue() && x.getRejectedBy() == null)
											? !(x.getApprovedBy().keySet().contains(HR))
											: null)
									.pendingAt(pendingAt).build();
						}).orElseThrow(() -> {
							log.error(TIMESHEET_DOES_NOT_EXIST_WITH_EMPLOYEE_ID + x.getEmployeeId());
							return new DataNotFoundException(
									TIMESHEET_DOES_NOT_EXIST_WITH_EMPLOYEE_ID + x.getEmployeeId());
						}))
				.orElseThrow(() -> {
					log.error(TIMESHEET_DOES_NOT_EXIST);
					return new DataNotFoundException(TIMESHEET_DOES_NOT_EXIST);
				});
		log.info(THE_GET_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END);
		return timeSheetDto;
	}

	@Override
	public String updateEmployeeTimesheetDetails(Long companyId, String timesheetObjectId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {

		List<String> timesheetLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getTimeSheet).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		String updateStatus = employeeTimesheetDetailsRepository
				.findByTimesheetObjectIdAndCompanyId(timesheetObjectId, companyId).filter(y -> !y.getIsApproved())
				.map(x -> employeePersonalInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId, x.getEmployeeId())
						.filter(z -> x.getRejectedBy() == null).map(y -> {
							EmployeeTimesheetDetails timesheetDetails = optional
									.filter(o -> adminApprovedRejectDto.getStatus().equals(REJECTED)).map(a -> {
										x.setRejectedBy(ADMIN);
										x.setRejectionReason(adminApprovedRejectDto.getReason());
										x.setIsApproved(false);
										return x;
									}).orElseGet(() -> optional
											.filter(d -> adminApprovedRejectDto.getStatus().equals(APPROVED)).map(p -> {
												Map<String, String> previousAprovedBy = (x.getApprovedBy().isEmpty())
														? new LinkedHashMap<>()
														: x.getApprovedBy();
												previousAprovedBy.put(HR, employeeId);
												x.setApprovedBy(previousAprovedBy);
												if (timesheetLevels.get(timesheetLevels.size() - 1)
														.equalsIgnoreCase(HR)) {
													x.setIsApproved(true);
												}
												return x;
											}).orElseThrow(() -> new DataNotFoundException(STATUS_DOES_NOT_EXIST)));
							employeeTimesheetDetailsRepository.save(timesheetDetails);
							log.info(UPDATE_EMPLOYEE_TIMESHEET);
							return x.getIsApproved().booleanValue() ? TIMESHEET_APPROVED_SUCCESSFULLY
									: TIMESHEET_REJECTED_BY_ADMIN;
						}).orElseThrow(() -> {
							log.error(PERSONAL_DETAILS_DOES_NOT_EXIST);
							return new DataNotFoundException(PERSONAL_DETAILS_DOES_NOT_EXIST);
						}))
				.orElseThrow(() -> {
					log.error(TIMESHEET_DOES_NOT_EXIST);
					return new DataNotFoundException(TIMESHEET_DOES_NOT_EXIST);
				});
		log.info(THE_UPDATE_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END + updateStatus);
		return updateStatus;
	}

}