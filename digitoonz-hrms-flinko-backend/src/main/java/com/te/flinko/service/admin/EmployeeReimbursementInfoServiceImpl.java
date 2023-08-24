package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVE_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECT_LEAVE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.STATUS_IS_NOT_VALIED;

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
import com.te.flinko.dto.admin.EmployeeReimbursementInfoDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReimbursementInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeReimbursementInfoServiceImpl implements EmployeeReimbursementInfoService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeReimbursementInfoRepository employeeReimbursementInfoRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId) {

		List<String> reimbursementLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.filter(reimbursementLevel -> reimbursementLevel.contains(ADMIN)).orElseThrow();

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeReimbursementInfoListStatusIn(companyId,
						List.of(PENDING, APPROVED, REJECTED))
				.filter(employeeList -> !employeeList.isEmpty())
				.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

		Set<EmployeePersonalInfo> employeeList = employees.stream().map(employee -> {
			employee.setEmployeeAdvanceSalaryList(employee.getEmployeeAdvanceSalaryList().stream()
					.filter(employeeAdvanceSalaryLevel -> reimbursementLevels.size() == 1 || employeeAdvanceSalaryLevel
							.getApprovedBy().keySet().contains(reimbursementLevels.get(reimbursementLevels.size() - 2)))
					.collect(Collectors.toList()));
			return employee;
		}).collect(Collectors.toSet());

		return Optional.ofNullable(employeeList).filter(emps -> !emps.isEmpty())
				.map(empList -> empList.stream().map(employee -> {
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();

					return employee.getEmployeeReimbursementInfoList().stream()
							.map(employeeReimbursment -> EmployeeReimbursementInfoDTO.builder()
									.employeeId(employeeOfficialInfo.getEmployeeId())
									.employeeInfoId(employee.getEmployeeInfoId())
									.employeeReimbursementId(employeeReimbursment.getReimbursementId())
									.employeeName(employee.getFirstName() + " " + employee.getLastName())
									.department(employeeOfficialInfo.getDepartment())
									.status(employeeReimbursment.getStatus())
									.description(employeeOfficialInfo.getDesignation())
									.amount(employeeReimbursment.getAmount()).build())
							.collect(Collectors.toList());
				}).collect(Collectors.toList()).stream().flatMap(Collection::stream)
						.sorted(Comparator.comparingInt(emp -> -emp.getStatus().charAt(2)
								- emp.getStatus().charAt(emp.getStatus().length() - 1)))
						.collect(Collectors.toList()))
				.orElseThrow();
	}

	@Override
	public EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId) {
		return employeeReimbursementInfoRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeReimbursementId, companyId)
				.map(employeeReimbursment -> {
					EmployeePersonalInfo employeePersonalInfo = employeeReimbursment.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					return EmployeeReimbursementInfoDTO.builder().employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeReimbursementId(employeeReimbursementId)
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.amount(employeeReimbursment.getAmount())
							.branch(employeePersonalInfo.getCompanyInfo().getCompanyBranchInfoList().stream()
									.filter(company -> company.getCompanyInfo().getCompanyId().equals(companyId))
									.findFirst().orElseThrow().getBranchName())
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.description(employeeReimbursment.getDescription())
							.expenseDate(employeeReimbursment.getExpenseDate())
							.reimbursementType(
									employeeReimbursment.getCompanyExpenseCategories().getExpenseCategoryName())
							.link(employeeReimbursment.getAttachmentUrl()).build();
				}).orElseThrow();
	}

	@Transactional
	@Override
	public String addEmployeeReimbursement(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto) {

		return employeeReimbursementInfoRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoId(
						employeeReimbursementId, companyId, employeeInfoId)
				.filter(Objects::nonNull).filter(reimbursement -> !reimbursement.getStatus().equals(REJECTED)
						&& !reimbursement.getStatus().equals(APPROVED))
				.map(employeeLeaveApplied -> {
					employeeLeaveApplied.setStatus(adminApprovedRejectDto.getStatus());
					return optional.filter(r -> adminApprovedRejectDto.getStatus().equals(REJECTED)).map(u -> {
						employeeLeaveApplied.setRejectedBy(ADMIN); // confusion
						employeeLeaveApplied.setRejectedReason(adminApprovedRejectDto.getReason());
						return REJECT_LEAVE_SUCCESSFULLY;
					}).orElseGet(
							() -> optional.filter(y -> adminApprovedRejectDto.getStatus().equals(APPROVED)).map(i -> {
								employeeLeaveApplied.getApprovedBy().put(ADMIN, employeeId);
								return APPROVE_LEAVE_SUCCESSFULLY;
							}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(
						DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT));
	}

}
