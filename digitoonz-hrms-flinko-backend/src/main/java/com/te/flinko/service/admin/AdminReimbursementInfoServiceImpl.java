package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_REIMBURSEMENT_APPROVAL;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.APPROVE_REIMBURSEMENT_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.INVALID_STATUS_PLEASE_PROVIDE_VALID_STATUS;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.REJECT_REIMBURSEMENT_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.SOMETHING_WENT_WRONG;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.STATUS_IS_NOT_VALIED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_ADD_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_ADD_EMPLOYEE_REIMBURSEMENT_METHOD_END;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_GET_ALL_ADMIN_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_GET_ALL_ADMIN_EMPLOYEE_REIMBURSEMENT_METHOD_END;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_GET_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.THE_GET_EMPLOYEE_REIMBURSEMENT_METHOD_END;

import java.util.Collection;
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
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.admin.StatusNotFound;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReimbursementInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class AdminReimbursementInfoServiceImpl implements AdminReimbursementInfoService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeReimbursementInfoRepository employeeReimbursementInfoRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId, String status) {

		log.info(THE_GET_ALL_ADMIN_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS, companyId, " And Status ", status);

		List<String> reimbursementLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(reimbursementLevel -> optional.filter(x -> reimbursementLevel.getReimbursement().contains(ADMIN))
						.map(y -> reimbursementLevel.getReimbursement()).orElseThrow(() -> {
							log.error(ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_REIMBURSEMENT_APPROVAL);
							return new DataNotFoundException(
									ADMIN_NOT_ADDED_TO_VERIFY_THE_EMPLOYEE_REIMBURSEMENT_APPROVAL);
						}))
				.orElseThrow(() -> {
					log.error(COMPANY_NOT_FOUND);
					return new DataNotFoundException(COMPANY_NOT_FOUND);
				});

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeReimbursementInfoListStatus(companyId, Optional.of(status)
						.filter(s -> List.of(PENDING, APPROVED, REJECTED).contains(status)).orElseThrow(() -> {
							log.error(INVALID_STATUS_PLEASE_PROVIDE_VALID_STATUS);
							return new StatusNotFound(INVALID_STATUS_PLEASE_PROVIDE_VALID_STATUS);
						}))
				.filter(employeeList -> !employeeList.isEmpty()).orElseThrow(() -> {
					log.error(EMPLOYEE_NOT_FOUND);
					return new DataNotFoundException(EMPLOYEE_NOT_FOUND);
				});

		Set<EmployeePersonalInfo> employeeList = employees.stream().map(employee -> {
			employee.setEmployeeReimbursementInfoList(employee.getEmployeeReimbursementInfoList().stream()
					.filter(employeeAdvanceSalaryLevel -> reimbursementLevels.size() == 1 || employeeAdvanceSalaryLevel
							.getApprovedBy().keySet().contains(reimbursementLevels.get(reimbursementLevels.size() - 2)))
					.collect(Collectors.toList()));
			return employee;
		}).collect(Collectors.toSet());

		List<EmployeeReimbursementInfoDTO> employeeReimbursementInfoDtos = Optional.ofNullable(employeeList)
				.filter(emps -> !emps.isEmpty()).map(empList -> empList.stream().map(employee -> {
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();

					return employee.getEmployeeReimbursementInfoList().stream()
							.filter(st -> st.getStatus().equalsIgnoreCase(status))
							.map(employeeReimbursment -> EmployeeReimbursementInfoDTO.builder()
									.employeeId(employeeOfficialInfo.getEmployeeId())
									.employeeInfoId(employee.getEmployeeInfoId())
									.employeeReimbursementId(employeeReimbursment.getReimbursementId())
									.employeeName(employee.getFirstName() + " " + employee.getLastName())
									.department(employeeOfficialInfo.getDepartment())
									.status(employeeReimbursment.getStatus())
									.designation(employeeOfficialInfo.getDesignation())
									.reason(employeeReimbursment.getRejectedReason())
									.amount(employeeReimbursment.getAmount()).build())
							.collect(Collectors.toList());
				}).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList()))
				.orElseThrow(() -> {
					log.error(SOMETHING_WENT_WRONG);
					return new DataNotFoundException(SOMETHING_WENT_WRONG);
				});
		log.info(THE_GET_ALL_ADMIN_EMPLOYEE_REIMBURSEMENT_METHOD_END, employeeReimbursementInfoDtos);
		return employeeReimbursementInfoDtos;
	}

	@Override
	public EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId) {
		log.info(THE_GET_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS, companyId, " And Reimbursement Id ",
				employeeReimbursementId);
		EmployeeReimbursementInfoDTO reimbursementInfoDto = employeeReimbursementInfoRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeReimbursementId, companyId)
				.map(employeeReimbursment -> {
					EmployeePersonalInfo employeePersonalInfo = employeeReimbursment.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					return EmployeeReimbursementInfoDTO.builder().employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeReimbursementId(employeeReimbursementId)
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.amount(employeeReimbursment.getAmount())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.description(employeeReimbursment.getDescription()).status(employeeReimbursment.getStatus())
							.reason(employeeReimbursment.getRejectedReason())
							.expenseDate(employeeReimbursment.getExpenseDate())
							.rejectedBy(employeeReimbursment.getRejectedBy())
							.reimbursementType(
									employeeReimbursment.getCompanyExpenseCategories().getExpenseCategoryName())
							.link(employeeReimbursment.getAttachmentUrl()).build();
				}).orElseThrow(() -> {
					log.error(SOMETHING_WENT_WRONG);
					return new DataNotFoundException(SOMETHING_WENT_WRONG);
				});
		log.info(THE_GET_EMPLOYEE_REIMBURSEMENT_METHOD_END, reimbursementInfoDto);
		return reimbursementInfoDto;
	}

	@Transactional
	@Override
	public String addEmployeeReimbursement(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto) {
		log.info(THE_ADD_EMPLOYEE_REIMBURSEMENT_METHOD_BEGINS, companyId, " ,Reimbursement Id", employeeReimbursementId,
				" ,Employee Id ", employeeId, " And AdminApprovedRejectDto {} ", adminApprovedRejectDto);
		String addEmployeeReimbursementStatus = employeeReimbursementInfoRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoId(
						employeeReimbursementId, companyId, employeeInfoId)
				.filter(Objects::nonNull).filter(reimbursement -> !reimbursement.getStatus().equals(REJECTED)
						&& !reimbursement.getStatus().equals(APPROVED))
				.map(employeeReimbursement -> {
					employeeReimbursement.setStatus(adminApprovedRejectDto.getStatus());
					return optional.filter(r -> adminApprovedRejectDto.getStatus().equals(REJECTED)).map(u -> {
						employeeReimbursement.setRejectedBy(ADMIN);
						employeeReimbursement.setRejectedReason(adminApprovedRejectDto.getReason());
						return REJECT_REIMBURSEMENT_SUCCESSFULLY;
					}).orElseGet(
							() -> optional.filter(y -> adminApprovedRejectDto.getStatus().equals(APPROVED)).map(i -> {
								employeeReimbursement.getApprovedBy().put(ADMIN, employeeId);
								return APPROVE_REIMBURSEMENT_SUCCESSFULLY;
							}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> {
					log.error(DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT);
					return new DataNotFoundException(
							DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED_THE_APPLIED_FOR_REIMBURSEMENT);
				});
		log.info(THE_ADD_EMPLOYEE_REIMBURSEMENT_METHOD_END + addEmployeeReimbursementStatus);
		return addEmployeeReimbursementStatus;
	}

}
