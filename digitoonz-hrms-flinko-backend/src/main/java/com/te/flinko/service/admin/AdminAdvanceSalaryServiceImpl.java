package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.APPROVE_ADVANCE_SALARY_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.INVALID_STATUS_PLEASE_PROVIDE_VALID_STATUS;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.REJECT_ADVANCE_SALARY_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.SOMETHING_WENT_WRONG;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.STATUS_IS_NOT_VALIED;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.AdvancedSalaryDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.admin.StatusNotFound;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeAdvanceSalaryRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class AdminAdvanceSalaryServiceImpl implements AdminAdvanceSalaryService {
	
	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeAdvanceSalaryRepository employeeAdvanceSalaryRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<AdvancedSalaryDTO> getAllEmployeeAdvanceSalary(Long companyId,String status) {

		List<String> advanceSalaryLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.filter(advanceSalaryLevel -> advanceSalaryLevel.contains(ADMIN)).orElseThrow();

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndCompanyInfoCompanyPayrollInfoListIsAdvanceSalaryEnabledAndEmployeeAdvanceSalaryListStatus(
						companyId, Boolean.TRUE, Optional.of(status).filter(s -> List.of(PENDING, APPROVED, REJECTED).contains(status))
						.orElseThrow(()->new StatusNotFound(INVALID_STATUS_PLEASE_PROVIDE_VALID_STATUS)))
				.filter(emps -> !emps.isEmpty()).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND));

		Set<EmployeePersonalInfo> employeeList = employees.stream().map(employee -> {
			employee.setEmployeeAdvanceSalaryList(employee.getEmployeeAdvanceSalaryList().stream()
					.filter(employeeAdvanceLevel -> advanceSalaryLevels.size() == 1 || employeeAdvanceLevel
							.getApprovedBy().keySet().contains(advanceSalaryLevels.get(advanceSalaryLevels.size() - 2)))
					.collect(Collectors.toList()));
			return employee;
		}).collect(Collectors.toSet());

		return Optional.ofNullable(employeeList).filter(emps -> !emps.isEmpty())
				.map(empList -> empList.stream().map(employee -> {
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();

					return employee.getEmployeeAdvanceSalaryList().stream()
							.filter(st -> st.getStatus().equalsIgnoreCase(status))
							.map(employeeAdvancedSalary -> AdvancedSalaryDTO.builder()
									.employeeInfoId(employee.getEmployeeInfoId())
									.advanceSalaryId(employeeAdvancedSalary.getAdvanceSalaryId())
									.employeeId(employeeOfficialInfo.getEmployeeId())
									.employeeName(employee.getFirstName() + " " + employee.getLastName())
									.department(employeeOfficialInfo.getDepartment())
									.designation(employeeOfficialInfo.getDesignation())
									.amount(employeeAdvancedSalary.getAmount()).build())
							.collect(Collectors.toList());
				}).collect(Collectors.toList()).stream().flatMap(Collection::stream)
						.collect(Collectors.toList()))
				.orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG));
	}

	@Override
	public AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeReimbursementId) {

		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeReimbursementId, companyId)
				.map(employeeAdvanceSalary -> {
					EmployeePersonalInfo employeePersonalInfo = employeeAdvanceSalary.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					return AdvancedSalaryDTO.builder().employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.advanceSalaryId(employeeAdvanceSalary.getAdvanceSalaryId())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.department(employeeOfficialInfo.getDepartment())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.designation(employeeOfficialInfo.getDesignation())
							.requestedOn(employeeAdvanceSalary.getCreatedDate()).emi(employeeAdvanceSalary.getEmi())
							.reason(employeeAdvanceSalary.getReason()).amount(employeeAdvanceSalary.getAmount())
							.build();
				}).orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG));
	}

	@Transactional
	@Override
	public String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {
		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						advanceSalaryId, employeeInfoId, companyId)
				.filter(advanceSalary -> !advanceSalary.getStatus().equals(REJECTED)
						&& !advanceSalary.getStatus().equals(APPROVED))
				.map(employeeAdvanceSalary -> {
					employeeAdvanceSalary.setStatus(adminApprovedRejectDto.getStatus());
					return optional.filter(rejectStatus -> adminApprovedRejectDto.getStatus().equals(REJECTED)).map(t -> {
						employeeAdvanceSalary.setRejectedBy(ADMIN); // confusion
						employeeAdvanceSalary.setRejectedReason(adminApprovedRejectDto.getReason());
						return REJECT_ADVANCE_SALARY_SUCCESSFULLY;
					}).orElseGet(
							() -> optional.filter(approvedStatus -> adminApprovedRejectDto.getStatus().equals(APPROVED)).map(o -> {
								employeeAdvanceSalary.getApprovedBy().put(ADMIN, employeeId);
								return APPROVE_ADVANCE_SALARY_SUCCESSFULLY;
							}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(
						DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED));
	}

}

