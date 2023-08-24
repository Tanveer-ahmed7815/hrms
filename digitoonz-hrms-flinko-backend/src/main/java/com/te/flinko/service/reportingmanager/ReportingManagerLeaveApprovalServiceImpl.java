package com.te.flinko.service.reportingmanager;

import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVE_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.EMPLOYEE_LEAVE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.HR;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.RM_APPROVAL_NOT_REQUIRED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECT_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.RM;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.STATUS_IS_NOT_VALIED;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeLeaveAllocated;
import com.te.flinko.entity.employee.EmployeeLeaveApplied;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class ReportingManagerLeaveApprovalServiceImpl implements ReportingManagerLeaveApprovalService {

	private final EmployeeLeaveAppliedRepository leaveAppliedRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeLeaveAllocatedRepository leaveAllocatedRepository;

	private final EmployeeReportingInfoRepository reportingInfoRepository;

	@Override
	public List<EmployeeLeaveInfoDTO> getLeaveDetailsByStatus(Long companyId, String status, Long employeeInfoId) {

		List<String> leaveApprovalLevel = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!leaveApprovalLevel.contains(RM)) {
			throw new DataNotFoundException(RM_APPROVAL_NOT_REQUIRED);
		}
		List<Long> employeeInfoIList = reportingInfoRepository
				.findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(employeeInfoId, companyId)
				.stream().map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId())
				.collect(Collectors.toList());

		List<EmployeeLeaveApplied> leaveAppliedList = leaveAppliedRepository
				.findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(status,
						companyId, employeeInfoIList);

		List<EmployeeLeaveApplied> filteredList = leaveAppliedList.stream()
				.filter(leaveRequest -> leaveRequest.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());

		return filteredList.stream()
				.filter(leaveRequest -> leaveRequest.getEmployeePersonalInfo() != null
						&& leaveRequest.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(leaveRequest -> {

					EmployeePersonalInfo employeePersonalInfo = leaveRequest.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					String pendingAt = null;

					if (leaveRequest.getStatus().equalsIgnoreCase(PENDING)) {
						if (leaveApprovalLevel.size() == 1) {
							pendingAt = RM;
						} else {
							pendingAt = (leaveRequest.getApprovedBy().keySet().contains(RM)
									&& leaveRequest.getApprovedBy().keySet().contains(HR))
											? ADMIN
											: leaveRequest.getApprovedBy().keySet().contains(RM)
													? leaveApprovalLevel.get(1)
													: RM;
						}
					}

					List<EmployeeReportingInfo> employeeInfoList = employeePersonalInfo.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if (!employeeInfoList.isEmpty()) {
						reportingManagerDetails = employeeInfoList.get(employeeInfoList.size() - 1)
								.getReportingManager();
					}
					String reportingManagerName = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					return EmployeeLeaveInfoDTO.builder().leaveAppliedId(leaveRequest.getLeaveAppliedId())
							.employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.name(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.reportingManager(reportingManagerName).department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.employeeId(employeeOfficialInfo.getEmployeeId()).startDate(leaveRequest.getStartDate())
							.status(leaveRequest.getStatus()).endDate(leaveRequest.getEndDate())
							.isActionRequired(status.equalsIgnoreCase(PENDING)
									? !(leaveRequest.getApprovedBy().keySet().contains(RM))
									: null)
							.leaveOfType(leaveRequest.getLeaveOfType())
							.pendingAt(pendingAt).rejectedBy(leaveRequest.getRejectedBy())
							.rejectedReason(leaveRequest.getRejectionReason()).build();

				}).collect(Collectors.toList());
	}

//	private List<EmployeeLeaveApplied> filterLeaveBasedOnCondition(String status,
//			List<EmployeeLeaveApplied> employeeLeaveAppliedList, List<String> levels) {
//		if (status.equalsIgnoreCase(PENDING) || status.equalsIgnoreCase(REJECTED)) {
//			return employeeLeaveAppliedList.stream().filter(
//					leaves -> (levels.get(0).equalsIgnoreCase(HR) || leaves.getApprovedBy().keySet().contains(RM)))
//					.collect(Collectors.toList());
//		} else {
//			return employeeLeaveAppliedList;
//		}
//	}

	@Override
	public EmployeeLeaveInfoDTO getLeaveDetailsById(Long leaveAppliedId, Long employeeInfoId, Long companyId) {

		List<String> leaveApprovalLevel = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		return leaveAppliedRepository
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

					List<EmployeeReportingInfo> employeeInfoList = employeePersonalInfo.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if (!employeeInfoList.isEmpty()) {
						reportingManagerDetails = employeeInfoList.get(employeeInfoList.size() - 1)
								.getReportingManager();
					}
					String reportingManagerName = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					String pending = null;
					if (leave.getStatus().equalsIgnoreCase(PENDING)) {
						if (leaveApprovalLevel.size() == 1) {
							pending = RM;
						} else {
							pending = (leave.getApprovedBy().keySet().contains(RM)
									&& leave.getApprovedBy().keySet().contains(HR)) ? ADMIN
											: leave.getApprovedBy().keySet().contains(RM) ? leaveApprovalLevel.get(1)
													: RM;
						}
					}
					return EmployeeLeaveInfoDTO.builder().leaveAppliedId(leaveAppliedId)
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.name(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.department(employeeOfficialInfo.getDepartment()).reportingManager(reportingManagerName)
							.designation(employeeOfficialInfo.getDesignation()).status(leave.getStatus())
							.startDate(leave.getStartDate()).endDate(leave.getEndDate())
							.days(ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate().plusDays(1)))
							.leaveOfType(leave.getLeaveOfType()).reason(leave.getReason()).pendingAt(pending)
							.isActionRequired(leave.getStatus().equalsIgnoreCase(PENDING)
									? !(leave.getApprovedBy().keySet().contains(RM))
									: null)
							.rejectedBy(leave.getRejectedBy()).rejectedReason(leave.getRejectionReason())
							.pendingLeaves(getEmployeeLeaveAvialable(leave.getLeaveOfType(), List.of(APPROVED, PENDING),
									employeeInfoId, companyId))
							.build();

				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

	}

	private Long getEmployeeLeaveAvialable(String leaveOfType, List<String> status, Long employeeInfoId,
			Long companyId) {

		String noOfLeaves = leaveAllocatedRepository.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId)
				.map(EmployeeLeaveAllocated::getLeavesDetails)
				.orElseThrow(() -> new DataNotFoundException(EMPLOYEE_LEAVE_NOT_FOUND)).get(leaveOfType);

		return Optional.ofNullable(Long.parseLong(noOfLeaves == null ? "0" : noOfLeaves) - leaveAppliedRepository
				.findByLeaveOfTypeAndStatusInAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						leaveOfType, status, employeeInfoId, companyId)
				.filter(leave -> !leave.isEmpty())
				.map(leaveApplied -> leaveApplied.stream()
						.mapToLong(leaveInfo -> ChronoUnit.DAYS.between(leaveInfo.getStartDate(),
								leaveInfo.getEndDate().plusDays(1)))
						.reduce(0, Long::sum))
				.orElseThrow(() -> new DataNotFoundException(NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT)))
				.filter(availLeave -> availLeave >= 0)
				.orElseThrow(() -> new DataNotFoundException("Leave Not Available"));
	}

	@Override
	@Transactional
	public String updateLeaveStatus(Long companyId, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {

		List<String> leaveApprovalLevel = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!leaveApprovalLevel.contains(RM)) {
			throw new DataNotFoundException(RM_APPROVAL_NOT_REQUIRED);
		}

		return leaveAppliedRepository.findById(leaveAppliedId)
				.filter(leaveApplied -> List.of(APPROVED, REJECTED, PENDING).contains(leaveApplied.getStatus()))
				.filter(leaveApplied -> leaveApplied.getStatus().equalsIgnoreCase(PENDING))
				.map(employeeleaveApplied -> {
					return Optional.ofNullable(adminApprovedRejectDto.getStatus())
							.filter(status -> status.equalsIgnoreCase(REJECTED)).map(r -> {
								employeeleaveApplied.setStatus(adminApprovedRejectDto.getStatus());
								employeeleaveApplied.setRejectedBy(RM);
								employeeleaveApplied.setRejectionReason(adminApprovedRejectDto.getReason());
								return REJECT_LEAVE_SUCCESSFULLY;
							})
							.orElseGet(() -> Optional.of(adminApprovedRejectDto.getStatus())
									.filter(status -> status.equalsIgnoreCase(APPROVED)
											&& getEmployeeLeaveAvialable(employeeleaveApplied.getLeaveOfType(),
													List.of(APPROVED, PENDING), employeeInfoId, companyId) >= 0)
									.map(a -> {
										LinkedHashMap<String, String> approvedBy = new LinkedHashMap<>();
										approvedBy.put(RM, employeeId);
										employeeleaveApplied.setApprovedBy(approvedBy);
										if (leaveApprovalLevel.size() == 1) {
											employeeleaveApplied.setStatus(APPROVED);
										}
										return APPROVE_LEAVE_SUCCESSFULLY;
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));

				}).orElseThrow(() -> new DataNotFoundException("Data Not Available"));
	}
}
