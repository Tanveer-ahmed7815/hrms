package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.APPROVE_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.EMPLOYEE_LEAVE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.REJECT_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeLeaveDetailsConstants.STATUS_IS_NOT_VALIED;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeLeaveAllocated;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeLeaveDetailsServiceImpl implements EmployeeLeaveDetailsService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final EmployeeLeaveAppliedRepository employeeLeaveAppliedRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeLeaveAllocatedRepository employeeLeaveAllocatedRepository;

	@Override
	public List<EmployeeLeaveInfoDTO> leaveApprovals(Long companyId) {
		List<String> levels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getLeave).filter(level -> level.contains(ADMIN)).orElseThrow();

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeLeaveAppliedListStatusIn(companyId,
						List.of(PENDING, APPROVED, REJECTED))
				.filter(employeeList -> !employeeList.isEmpty())
				.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

		Set<EmployeePersonalInfo> employeeList = employees.stream().map(employee -> {
			employee.setEmployeeLeaveAppliedList(
					employee.getEmployeeLeaveAppliedList().stream()
							.filter(leaves -> levels.size() == 1
									|| leaves.getApprovedBy().keySet().contains(levels.get(levels.size() - 2)))
							.collect(Collectors.toList()));
			return employee;
		}).collect(Collectors.toSet());

		return employeeList.stream()
				.map(employee -> employee.getEmployeeLeaveAppliedList().stream()
						.map(leave -> EmployeeLeaveInfoDTO.builder().leaveAppliedId(leave.getLeaveAppliedId())
								.name(employee.getFirstName() + " " + employee.getLastName())
								.designation(employee.getEmployeeOfficialInfo().getDesignation())
								.employeeInfoId(employee.getEmployeeInfoId())
								.employeeId(employee.getEmployeeOfficialInfo().getEmployeeId())
								.leaveOfType(leave.getLeaveOfType()).startDate(leave.getStartDate())
								.status(leave.getStatus()).endDate(leave.getEndDate()).build())
						.collect(Collectors.toList()))
				.collect(Collectors.toList()).stream().flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(
						emp -> -emp.getStatus().charAt(2) - emp.getStatus().charAt(emp.getStatus().length() - 1)))
				.collect(Collectors.toList());
	}

	@Override
	public EmployeeLeaveInfoDTO leaveApproval(Long leaveAppliedId, Long employeeInfoId, Long companyId) {

		return employeeLeaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyId(leaveAppliedId, companyId)
				.filter(Objects::nonNull).map(leave -> {
					EmployeePersonalInfo employeePersonalInfo = leave.getEmployeePersonalInfo();
					EmployeePersonalInfo reportingManager = leave.getEmployeeReportingInfo().getReportingManager();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

					return EmployeeLeaveInfoDTO.builder().leaveAppliedId(leave.getLeaveAppliedId())
							.name(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.designation(employeeOfficialInfo.getDesignation())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.reportingManager(reportingManager.getFirstName() + " " + reportingManager.getLastName())
							.leaveOfType(leave.getLeaveOfType())
							.branch(employeePersonalInfo.getCompanyInfo().getCompanyBranchInfoList().stream()
									.filter(company -> company.getCompanyInfo().getCompanyId().equals(companyId))
									.findFirst().orElseThrow().getBranchName())
							.startDate(leave.getStartDate()).endDate(leave.getEndDate()).reason(leave.getReason())
							.department(employeeOfficialInfo.getDepartment())
							.days(ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()))
							.pendingLeaves(getEmployeeLeaveAvialable(leave.getLeaveOfType(), List.of(APPROVED),
									employeeInfoId, companyId))
							.build();
				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));
	}

	@Transactional
	@Override
	public String addEmployeeLeaveDetails(Long companyId, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {

		return employeeLeaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyId(leaveAppliedId, companyId)
				.filter(leave->List.of(PENDING, APPROVED, REJECTED).contains(leave.getStatus()))
				.filter(leave -> !leave.getStatus().equals(REJECTED) && !leave.getStatus().equals(APPROVED))
				.map(employeeLeaveApplied -> {
					employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
					return	Optional.ofNullable(adminApprovedRejectDto.getStatus()).filter(status -> status.equals(REJECTED))
							.map(v -> {
								employeeLeaveApplied.setRejectedBy(ADMIN); // confusion
								employeeLeaveApplied.setRejectionReason(adminApprovedRejectDto.getReason());
								return REJECT_LEAVE_SUCCESSFULLY;
							})
							.orElseGet(() -> Optional.of(adminApprovedRejectDto.getStatus())
									.filter(status -> status.equals(APPROVED)
											&& getEmployeeLeaveAvialable(employeeLeaveApplied.getLeaveOfType(),
													List.of(APPROVED, PENDING), employeeInfoId, companyId) >= 0)
									.map(s -> {
										employeeLeaveApplied.getApprovedBy().put(ADMIN, employeeId);
										return APPROVE_LEAVE_SUCCESSFULLY;
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));
	}

	private Long getEmployeeLeaveAvialable(String leaveOfType, List<String> status, Long employeeInfoId,
			Long companyId) {
		return Optional
				.ofNullable(Long
						.parseLong(employeeLeaveAllocatedRepository
								.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId)
								.map(EmployeeLeaveAllocated::getLeavesDetails)
								.orElseThrow(() -> new DataNotFoundException(EMPLOYEE_LEAVE_NOT_FOUND)).get(leaveOfType))
						- employeeLeaveAppliedRepository
								.findByLeaveOfTypeAndStatusInAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
										leaveOfType, status, employeeInfoId, companyId)
								.filter(leaves -> !leaves.isEmpty())
								.map(employeeLeaveApplied -> employeeLeaveApplied.stream()
										.mapToLong(leaveInfo -> ChronoUnit.DAYS.between(leaveInfo.getStartDate(),
												leaveInfo.getEndDate()))
										.reduce(0, Long::sum))
								.orElseThrow(() -> new DataNotFoundException(NUMBER_OF_LEAVE_IS_NOT_SUFFICIENT)))
				.filter(avilLeave -> avilLeave >= 0).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

	}
}


