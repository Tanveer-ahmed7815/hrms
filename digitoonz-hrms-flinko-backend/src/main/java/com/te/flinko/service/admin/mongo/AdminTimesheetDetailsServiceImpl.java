package com.te.flinko.service.admin.mongo;

import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.ADMIN;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_TIMESHEET_APPROVAL;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.APPROVED;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.PENDING;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.PERSONAL_DETAILS_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.REJECTED;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.SOMETHING_WENT_WRONG;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.STATUS_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_GET_ALL_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_GET_ALL_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_GET_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_GET_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_UPDATE_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.THE_UPDATE_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_APPROVED_SUCCESSFULLY;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_DOES_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_DOES_NOT_EXIST_WITH_EMPLOYEE_ID;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.TIMESHEET_REJECTED_BY_ADMIN;
import static com.te.flinko.common.admin.mongo.EmployeeTimesheetDetailsConstants.UPDATE_EMPLOYEE_TIMESHEET;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.mongo.EmployeeTimeSheetDTO;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.mongo.EmployeeTimesheetDetails;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.mongo.EmployeeTimesheetDetailsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Validated
@Slf4j
public class AdminTimesheetDetailsServiceImpl implements AdminTimesheetDetailsService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeTimesheetDetailsRepository employeeTimesheetDetailsRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status) {

		log.info(THE_GET_ALL_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS, companyId, " And Status", status);

		List<String> timesheetLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(timesheetLevel -> optional.filter(x -> timesheetLevel.getTimeSheet().contains(ADMIN))
						.map(y -> timesheetLevel.getTimeSheet()).orElseThrow(() -> {
							log.error(ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_TIMESHEET_APPROVAL);
							return new DataNotFoundException(ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_TIMESHEET_APPROVAL);
						}))
				.orElseThrow(() -> {
					log.error(COMPANY_NOT_FOUND);
					return new CompanyNotFound(COMPANY_NOT_FOUND);
				});

		List<EmployeeTimesheetDetails> employeeTimesheetDetails = employeeTimesheetDetailsRepository
				.findByCompanyIdAndIsApproved(companyId, Optional.of(status)
						.filter(s -> List.of(PENDING, APPROVED, REJECTED).contains(s) && s.equalsIgnoreCase(APPROVED))
						.map(x -> Boolean.TRUE).orElseGet(() -> Boolean.FALSE))
				.filter(employeeList -> !employeeList.isEmpty()).orElseThrow(() -> {
					log.error(TIMESHEET_DOES_NOT_EXIST);
					return new DataNotFoundException(TIMESHEET_DOES_NOT_EXIST);
				});

		Set<EmployeeTimesheetDetails> timesheetDetails = employeeTimesheetDetails.stream()
				.filter(timesheet -> timesheet.getApprovedBy()!=null && (timesheetLevels.size() == 1
						|| timesheet.getApprovedBy().keySet().contains(timesheetLevels.get(timesheetLevels.size() - 2))))
				.collect(Collectors.toSet());

		List<EmployeeTimeSheetDTO> timesheetDetail = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeIdIn(companyId,
						timesheetDetails.stream().map(EmployeeTimesheetDetails::getEmployeeId)
								.collect(Collectors.toList()))
				.map(employeePersonal -> employeePersonal.stream().map(employee -> {
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
					return timesheetDetails.stream()
							.filter(timesheet -> timesheet.getEmployeeId().equals(employeeOfficialInfo.getEmployeeId()))
							.map(w -> {
								if ((status.equalsIgnoreCase("PENDING") && w.getRejectedBy() == null) 
										|| (w.getRejectedBy() != null && status.equalsIgnoreCase("Rejected"))
										|| status.equalsIgnoreCase("APPROVED")) {
									return EmployeeTimeSheetDTO.builder()
											.employeeName(employee.getFirstName() + " " + employee.getLastName())
											.designation(employeeOfficialInfo.getDesignation())
											.timesheetObjectId(w.getTimesheetObjectId()).timesheetId(w.getTimesheetId())
											.department(employeeOfficialInfo.getDepartment())
											.employeeId(employeeOfficialInfo.getEmployeeId())
											.startDate(w.getTimesheets().get(0).getDate())
											.lastModifiedDate(w.getLastModifiedDate())
											.endDate(w.getTimesheets().get(w.getTimesheets().size() - 1).getDate())
											.status(Optional.ofNullable(w).map(z -> status.toUpperCase())
													.orElseGet(status::toUpperCase))
											.build();
								}
								return null;
							}).collect(Collectors.toList());
				}).flatMap(Collection::stream).filter(Objects::nonNull)
						.sorted(Comparator.comparing(EmployeeTimeSheetDTO::getLastModifiedDate).reversed())
						.collect(Collectors.toList()))
				.orElseThrow(() -> {
					log.error(SOMETHING_WENT_WRONG);
					return new DataNotFoundException(SOMETHING_WENT_WRONG);
				});
		log.info(THE_GET_ALL_EMPLOYEE_TIMESHEET_DETAILS_METHOD_END);
		return timesheetDetail;
	}

	@Override
	public EmployeeTimeSheetDTO getEmployeeTimesheetDetails(String timesheetObjectId, Long companyId) {

		log.info(THE_GET_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS, timesheetObjectId, " And Company Id", companyId);

		EmployeeTimeSheetDTO timeSheetDto = employeeTimesheetDetailsRepository
				.findByTimesheetObjectIdAndCompanyId(timesheetObjectId, companyId)
				.map(x -> employeePersonalInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId, x.getEmployeeId())
						.map(xx -> {
							EmployeePersonalInfo y = xx.get(0);
							EmployeeOfficialInfo employeeOfficialInfo = y.getEmployeeOfficialInfo();
							return EmployeeTimeSheetDTO.builder().timesheetId(x.getTimesheetId())
									.employeeId(x.getEmployeeId()).timesheetObjectId(x.getTimesheetObjectId())
									.department(employeeOfficialInfo.getDepartment())
									.designation(employeeOfficialInfo.getDesignation())
									.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
									.employeeName(y.getFirstName() + " " + y.getLastName())
									.startDate(x.getTimesheets().get(0).getDate())
									.reason(x.getRejectionReason())
									.endDate(x.getTimesheets().get(x.getTimesheets().size() - 1).getDate())
									.timesheets(x.getTimesheets()).build();
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

		log.info(THE_UPDATE_EMPLOYEE_TIMESHEET_DETAILS_METHOD_BEGINS, companyId, " ,Timesheet Id", timesheetObjectId,
				" And Employee Id", employeeId);

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
												x.getApprovedBy().put(ADMIN, employeeId);
												x.setIsApproved(true);
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
