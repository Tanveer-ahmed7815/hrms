package com.te.flinko.service.hr;

import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVE_REIMBURSEMENT_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_OFFICIAL_RECORD_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.HR;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.HR_APPROVAL_NOT_REQUIRED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECT_REIMBURSEMENT_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.RM;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.SOMETHING_WENT_WRONG;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.STATUS_IS_NOT_VALIED;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.EmployeeReimbursementInfoDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReimbursementInfoRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class HRReimbursementApprovalServiceImpl implements HRReimbursementApprovalService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeReimbursementInfoRepository employeeReimbursementInfoRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId, String status,
			Long employeeInfoId) {

		List<String> reimbursementLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!reimbursementLevels.contains(HR)) {
			throw new DataNotFoundException(HR_APPROVAL_NOT_REQUIRED);
		}

		List<Long> employeeInfoIdList = employeeReportingInfoRepository.findByReportingHREmployeeInfoId(employeeInfoId)
				.stream().map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId())
				.collect(Collectors.toList());

		List<EmployeeReimbursementInfo> employeeReimbursementInfoList = employeeReimbursementInfoRepository
				.findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(status,
						companyId, employeeInfoIdList);

		employeeReimbursementInfoList = filterReimbursementBasedOnCondition(status, employeeReimbursementInfoList,
				reimbursementLevels);

		return employeeReimbursementInfoList.stream()
				.filter(reimbursement -> reimbursement.getEmployeePersonalInfo() != null
						&& reimbursement.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(employeeReimbursment -> {
					EmployeePersonalInfo employee = employeeReimbursment.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
					List<EmployeeReportingInfo> employeeInfoList = employee.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if (!employeeInfoList.isEmpty()) {
						reportingManagerDetails = employeeInfoList.get(employeeInfoList.size() - 1)
								.getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					String pendingAt = null;
					if (employeeReimbursment.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (employeeReimbursment.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}
					return EmployeeReimbursementInfoDTO.builder().employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeInfoId(employee.getEmployeeInfoId())
							.employeeReimbursementId(employeeReimbursment.getReimbursementId())
							.employeeName(employee.getFirstName() + " " + employee.getLastName())
							.department(employeeOfficialInfo.getDepartment()).status(employeeReimbursment.getStatus())
							.designation(employeeOfficialInfo.getDesignation()).amount(employeeReimbursment.getAmount())
							.isActionRequired(status.equalsIgnoreCase(PENDING)
									? !(employeeReimbursment.getApprovedBy().keySet().contains(HR))
									: null)
							.pendingAt(pendingAt).rejectedBy(employeeReimbursment.getRejectedBy())
							.reportingManager(reportingManager).reason(employeeReimbursment.getRejectedReason())
							.build();
				}).collect(Collectors.toList());

	}

	private List<EmployeeReimbursementInfo> filterReimbursementBasedOnCondition(String status,
			List<EmployeeReimbursementInfo> employeeReimbursementInfoList, List<String> levels) {
		if (status.equalsIgnoreCase(PENDING) || status.equalsIgnoreCase(REJECTED)) {
			return employeeReimbursementInfoList.stream()
					.filter(employeeReimburesment -> levels.get(0).equalsIgnoreCase(HR)
							|| employeeReimburesment.getApprovedBy().keySet().contains(RM))
					.collect(Collectors.toList());

		} else {
			return employeeReimbursementInfoList;
		}
	}

	@Override
	public EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId) {
		return employeeReimbursementInfoRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeReimbursementId, companyId)
				.map(employeeReimbursment -> {
					EmployeePersonalInfo employeePersonalInfo = employeeReimbursment.getEmployeePersonalInfo();
					if (employeePersonalInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}

					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_OFFICIAL_RECORD_NOT_FOUND);
					}

					String expenseCategories = (employeeReimbursment.getCompanyExpenseCategories() != null)
							? employeeReimbursment.getCompanyExpenseCategories().getExpenseCategoryName()
							: null;

					String pendingAt = null;
					if (employeeReimbursment.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (employeeReimbursment.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}

					String companyBranchName = employeeOfficialInfo.getCompanyBranchInfo() == null ? null
							: employeeOfficialInfo.getCompanyBranchInfo().getBranchName();

					return EmployeeReimbursementInfoDTO.builder().employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeReimbursementId(employeeReimbursementId)
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.amount(employeeReimbursment.getAmount()).branch(companyBranchName)
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.description(employeeReimbursment.getDescription())
							.expenseDate(employeeReimbursment.getExpenseDate()).reimbursementType(expenseCategories)
							.status(employeeReimbursment.getStatus()).link(employeeReimbursment.getAttachmentUrl())
							.isActionRequired(employeeReimbursment.getStatus().equalsIgnoreCase(PENDING)
									? !(employeeReimbursment.getApprovedBy().keySet().contains(HR))
									: null)
							.pendingAt(pendingAt).rejectedBy(employeeReimbursment.getRejectedBy())
							.reason(employeeReimbursment.getRejectedReason()).build();
				}).orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG));
	}

	@Override
	@Transactional
	public String updateReimbursementStatus(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto) {

		List<String> reimbursementLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));
		return employeeReimbursementInfoRepository.findById(employeeReimbursementId).filter(Objects::nonNull)
				.filter(reimbursement -> reimbursement.getStatus().equalsIgnoreCase(PENDING))
				.map(employeeLeaveApplied -> {
					return optional.filter(r -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(REJECTED))
							.map(u -> {
								employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
								employeeLeaveApplied.setRejectedBy(HR);
								employeeLeaveApplied.setRejectedReason(adminApprovedRejectDto.getReason());
								return REJECT_REIMBURSEMENT_SUCCESSFULLY;
							})
							.orElseGet(() -> optional
									.filter(y -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(APPROVED))
									.map(i -> {
										if (reimbursementLevels.get(reimbursementLevels.size() - 1)
												.equalsIgnoreCase(HR)) {
											employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
										}
										LinkedHashMap<String, String> previousAprovedBy = (employeeLeaveApplied
												.getApprovedBy().isEmpty()) ? new LinkedHashMap<>()
														: employeeLeaveApplied.getApprovedBy();
										previousAprovedBy.put(HR, employeeId);
										employeeLeaveApplied.setApprovedBy(previousAprovedBy);
										return APPROVE_REIMBURSEMENT_SUCCESSFULLY;
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(
						DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT));

	}

}
