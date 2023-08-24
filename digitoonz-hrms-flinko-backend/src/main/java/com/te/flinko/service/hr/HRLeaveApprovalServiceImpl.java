package com.te.flinko.service.hr;

import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVE_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.EMPLOYEE_LEAVE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.HR;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.HR_APPROVAL_NOT_REQUIRED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECT_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.RM;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.STATUS_IS_NOT_VALIED;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeLeaveAllocated;
import com.te.flinko.entity.employee.EmployeeLeaveApplied;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class HRLeaveApprovalServiceImpl implements HRLeaveApprovalService {

	private final EmployeeLeaveAppliedRepository employeeLeaveAppliedRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeLeaveAllocatedRepository employeeLeaveAllocatedRepository;
	
	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	@Override
	@Transactional
	public String updateLeaveStatus(Long companyId, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {

		List<String> levels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		return employeeLeaveAppliedRepository.findById(leaveAppliedId)
				.filter(leave -> List.of(PENDING, APPROVED, REJECTED).contains(leave.getStatus()))
				.filter(leave -> leave.getStatus().equals(PENDING)).map(employeeLeaveApplied -> {
					return Optional.ofNullable(adminApprovedRejectDto.getStatus())
							.filter(status -> status.equals(REJECTED)).map(v -> {
								employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
								employeeLeaveApplied.setRejectedBy(HR);
								employeeLeaveApplied.setRejectionReason(adminApprovedRejectDto.getReason());
								return REJECT_LEAVE_SUCCESSFULLY;
							})
							.orElseGet(() -> Optional.of(adminApprovedRejectDto.getStatus())
									.filter(status -> status.equals(APPROVED)
											&& getEmployeeLeaveAvialable(employeeLeaveApplied.getLeaveOfType(),
													List.of(APPROVED, PENDING), employeeInfoId, companyId) >= 0)
									.map(s -> {
										if (levels.get(levels.size() - 1).equalsIgnoreCase(HR)) {
											employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
										}
										LinkedHashMap<String, String> previousAprovedBy = (employeeLeaveApplied
												.getApprovedBy().isEmpty()) ? new LinkedHashMap<>()
														: employeeLeaveApplied.getApprovedBy();
										previousAprovedBy.put(HR, employeeId);
										employeeLeaveApplied.setApprovedBy(previousAprovedBy);
										return APPROVE_LEAVE_SUCCESSFULLY;
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

	}

	@Override
	public List<EmployeeLeaveInfoDTO> getLeaveDetailsByStatus(Long companyId, String status, Long employeeInfoId) {
		List<String> levels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!levels.contains(HR)) {
			throw new DataNotFoundException(HR_APPROVAL_NOT_REQUIRED);
		}
		
		List<Long> employeeInfoIdList = employeeReportingInfoRepository.findByReportingHREmployeeInfoId(employeeInfoId)
				.stream().map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId())
				.collect(Collectors.toList());

		List<EmployeeLeaveApplied> employeeLeaveAppliedList = employeeLeaveAppliedRepository
				.findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(status, companyId,employeeInfoIdList);

		employeeLeaveAppliedList = filterLeaveBasedOnCondition(status, employeeLeaveAppliedList, levels);

		return employeeLeaveAppliedList.stream().filter(leave -> leave.getEmployeePersonalInfo() != null
				&& leave.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null).map(leave -> {
					String pendingAt = null;
					EmployeePersonalInfo employee = leave.getEmployeePersonalInfo();
					EmployeeOfficialInfo officialInfo = employee.getEmployeeOfficialInfo();
					if (leave.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (leave.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}
					
					EmployeeReportingInfo reportingEmployeeInfo = leave.getEmployeeReportingInfo();
					EmployeePersonalInfo reportingManagerDetails = null;
					if (reportingEmployeeInfo!=null) {
						reportingManagerDetails = reportingEmployeeInfo.getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					return EmployeeLeaveInfoDTO.builder().leaveAppliedId(leave.getLeaveAppliedId())
							.name(employee.getFirstName() + " " + employee.getLastName())
							.designation(officialInfo.getDesignation()).employeeInfoId(employee.getEmployeeInfoId())
							.employeeId(officialInfo.getEmployeeId()).leaveOfType(leave.getLeaveOfType())
							.startDate(leave.getStartDate()).status(leave.getStatus()).endDate(leave.getEndDate())
							.isActionRequired(
									status.equalsIgnoreCase(PENDING) ? !(leave.getApprovedBy().keySet().contains(HR))
											: null)
							.reportingManager(reportingManager)
							.pendingAt(pendingAt).rejectedBy(leave.getRejectedBy()).rejectionReason(leave.getRejectionReason())
							.build();
				}).collect(Collectors.toList());

	}

	private List<EmployeeLeaveApplied> filterLeaveBasedOnCondition(String status,
			List<EmployeeLeaveApplied> employeeLeaveAppliedList, List<String> levels) {
		if (status.equalsIgnoreCase(PENDING) || status.equalsIgnoreCase(REJECTED)) {
			return employeeLeaveAppliedList.stream().filter(
					leaves -> (levels.get(0).equalsIgnoreCase(HR) || leaves.getApprovedBy().keySet().contains(RM)))
					.collect(Collectors.toList());
		} else {
			return employeeLeaveAppliedList;
		}
	}

	@Override
	public EmployeeLeaveInfoDTO getLeaveDetailsById(Long leaveAppliedId, Long employeeInfoId, Long companyId) {
		return employeeLeaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyId(leaveAppliedId, companyId)
				.filter(Objects::nonNull).map(leave -> {
					EmployeePersonalInfo employeePersonalInfo = leave.getEmployeePersonalInfo();

					if (employeePersonalInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}

					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}

					EmployeePersonalInfo reportingManager = (leave.getEmployeeReportingInfo() == null) ? null
							: leave.getEmployeeReportingInfo().getReportingManager();
					String reportingManagerName = (reportingManager == null) ? null
							: reportingManager.getFirstName() + " " + reportingManager.getLastName();

					String pendingAt = null;
					if (leave.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (leave.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}

					return EmployeeLeaveInfoDTO.builder().leaveAppliedId(leave.getLeaveAppliedId())
							.name(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.designation(employeeOfficialInfo.getDesignation())
							.employeeId(employeeOfficialInfo.getEmployeeId()).reportingManager(reportingManagerName)
							.leaveOfType(leave.getLeaveOfType())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.startDate(leave.getStartDate()).endDate(leave.getEndDate()).reason(leave.getReason())
							.department(employeeOfficialInfo.getDepartment())
							.days(ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate().plusDays(1)))
							.pendingLeaves(getEmployeeLeaveAvialable(leave.getLeaveOfType(),
									List.of(APPROVED, PENDING, REJECTED), employeeInfoId, companyId))
							.status(leave.getStatus())
							.isActionRequired(leave.getStatus().equalsIgnoreCase(PENDING)
									? !(leave.getApprovedBy().keySet().contains(HR))
									: null)
							.pendingAt(pendingAt).rejectedBy(leave.getRejectedBy()).rejectionReason(leave.getRejectionReason())
							.build();
				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

	}

	// cal pending
	private Long getEmployeeLeaveAvialable(String leaveOfType, List<String> status, Long employeeInfoId,
			Long companyId) {
		String noOfLeave = employeeLeaveAllocatedRepository.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId)
				.map(EmployeeLeaveAllocated::getLeavesDetails)
				.orElseThrow(() -> new DataNotFoundException(EMPLOYEE_LEAVE_NOT_FOUND)).get(leaveOfType);
		return Optional.ofNullable(Long.parseLong(noOfLeave == null ? "0" : noOfLeave) - employeeLeaveAppliedRepository
				.findByLeaveOfTypeAndStatusInAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						leaveOfType, status, employeeInfoId, companyId)
				.filter(leaves -> !leaves.isEmpty())
				.map(employeeLeaveApplied -> employeeLeaveApplied.stream()
						.mapToLong(leaveInfo -> ChronoUnit.DAYS.between(leaveInfo.getStartDate(),
								leaveInfo.getEndDate().plusDays(1)))
						.reduce(0, Long::sum))
				.orElseThrow(() -> new DataNotFoundException(NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT)))
				.filter(avilLeave -> avilLeave >= 0).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

	}

}
