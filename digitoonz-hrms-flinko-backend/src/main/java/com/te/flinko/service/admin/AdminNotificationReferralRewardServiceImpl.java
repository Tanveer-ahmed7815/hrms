package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.EMPLOYEE_REFERRAL_REWARD_UPDATE_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.FAILED_TO_UPDATE_EMPLOYEE_REFERRAL_REWARD;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.NO_EMPLOYEE_FOUND_FOR_REWARD;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.REFERRED_EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.SOMETHING_WENT_WRONG;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.EmployeeNotificationReferralRewardDto;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class AdminNotificationReferralRewardServiceImpl implements AdminNotificationReferralRewardService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Override
	public Set<EmployeeNotificationReferralRewardDto> getAllEmployeeNotificationReferralReward(Long companyId) {
		return employeePersonalInfoRepository
				.findByIsActiveTrueAndCompanyInfoCompanyIdAndEmployeeReferenceInfoListRewardAmountNullAndEmployeeReferenceInfoListEmployeePersonalInfoNotNullAndEmployeeReferenceInfoListEmployeePersonalInfoIsActiveTrueAndEmployeeReferenceInfoListEmployeePersonalInfoEmployeeOfficialInfoDojLessThanEqual(
						companyId, LocalDate.now().minusDays(90))
				.filter(x -> !x.isEmpty())
				.map(employees -> employees.stream()
						.map(employee -> employee.getEmployeeReferenceInfoList().stream().filter(Objects::nonNull)
								.filter(xx -> xx.getEmployeePersonalInfo() != null && xx.getEmployeePersonalInfo().getEmployeeOfficialInfo()!=null
										&& xx.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDoj() != null
										)
								.filter(x -> x.getEmployeePersonalInfo() != null && x.getRewardAmount() == null
										&& x.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDoj()
												.isBefore(LocalDate.now().minusDays(90)))
								.map(refEmp -> {
									EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
									EmployeePersonalInfo employeePersonalInfo = refEmp.getEmployeePersonalInfo();
									return EmployeeNotificationReferralRewardDto.builder()
											.referenceId(refEmp.getReferenceId())
											.employeeId(employeeOfficialInfo.getEmployeeId())
											.employeeName(employee.getFirstName() + " " + employee.getLastName())
											.designation(employeeOfficialInfo.getDesignation())
											.department(employeeOfficialInfo.getDepartment())
											.referredTo(employeePersonalInfo.getFirstName() + " "
													+ employeePersonalInfo.getLastName())
											.build();
								}).collect(Collectors.toSet()))
						.flatMap(Collection::stream).collect(Collectors.toSet()))
				.orElseThrow(() -> new DataNotFoundException(NO_EMPLOYEE_FOUND_FOR_REWARD));
	}

	@Override
	public EmployeeNotificationReferralRewardDto getEmployeeNotificationReferralReward(Long companyId,
			Long referenceId) {

		return employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeReferenceInfoListReferenceId(companyId, referenceId)
				.filter(y -> y.getEmployeeReferenceInfoList().stream()
						.filter(x -> x.getReferenceId().equals(referenceId) && x.getEmployeePersonalInfo() != null
								&& x.getRewardAmount() == null && x.getEmployeePersonalInfo().getEmployeeOfficialInfo()
										.getDoj().isBefore(LocalDate.now().minusDays(90)))
						.count() > 0)
				.map(employee -> {
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
					return EmployeeNotificationReferralRewardDto.builder().referenceId(referenceId)
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(employee.getFirstName() + " " + employee.getLastName())
							.designation(employeeOfficialInfo.getDesignation())
							.department(employeeOfficialInfo.getDepartment())
							.referredTo(employee.getEmployeeReferenceInfoList().stream()
									.filter(emp -> Objects.equals(emp.getReferenceId(), referenceId)).map(empl -> {
										EmployeePersonalInfo employeePersonalInfo = empl.getEmployeePersonalInfo();
										return employeePersonalInfo.getFirstName() + " "
												+ employeePersonalInfo.getLastName();
									}).findFirst()
									.orElseThrow(() -> new DataNotFoundException(REFERRED_EMPLOYEE_NOT_FOUND)))
							.build();
				}).orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG));
	}

	@Override
	@Transactional
	public String updateEmployeeNotificationReferralReward(Long companyId,
			EmployeeNotificationReferralRewardDto employeeNotificationReferralRewardDto) {

		return Optional.ofNullable(employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeReferenceInfoListReferenceId(companyId,
						employeeNotificationReferralRewardDto.getReferenceId())
				.filter(y -> y.getEmployeeReferenceInfoList().stream()
						.filter(x -> x.getReferenceId().equals(employeeNotificationReferralRewardDto.getReferenceId())
								&& x.getEmployeePersonalInfo() != null && x.getRewardAmount() == null
								&& x.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDoj()
										.isBefore(LocalDate.now().minusDays(90)))
						.count() > 0)
				.map(x -> x.getEmployeeReferenceInfoList().stream().filter(emp -> Objects.equals(emp.getReferenceId(),
						employeeNotificationReferralRewardDto.getReferenceId())).map(employeeRef -> {
							employeeRef.setRewardUpdatedDate(LocalDateTime.now());
							employeeRef.setRewardAmount(employeeNotificationReferralRewardDto.getRewardAmount());
							employeeRef
									.setIsIncludedInSalary(employeeNotificationReferralRewardDto.getIncludeInSalary());
							return employeeRef;
						}).findFirst().orElseThrow(() -> new DataNotFoundException(REFERRED_EMPLOYEE_NOT_FOUND)))
				.orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG)))
				.map(msg -> EMPLOYEE_REFERRAL_REWARD_UPDATE_SUCCESSFULLY)
				.orElseThrow(() -> new DataNotFoundException(FAILED_TO_UPDATE_EMPLOYEE_REFERRAL_REWARD));
	}

}
