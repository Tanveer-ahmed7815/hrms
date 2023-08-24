package com.te.flinko.service.reportingmanager;

import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.APPROVED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.HR;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.PENDING;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.REJECTED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.RM;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.ADMIN;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.RM_APPROVAL_NOT_REQUIRED;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.STATUS_IS_NOT_VALIED;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;
import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.SOMETHING_WENT_WRONG;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.dto.admin.AdvancedSalaryDTO;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.entity.employee.EmployeeAdvanceSalary;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeAdvanceSalaryRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class ReportingManagerAdvanceSalaryApprovalServiceImpl implements ReportingManagerAdvanceSalaryApprovalService {

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private final EmployeeAdvanceSalaryRepository employeeAdvanceSalaryRepository;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<AdvancedSalaryDTO> getAdvanceSalaryByStatus(Long companyId, String status, Long employeeInfoId) {

		List<String> advanceSalaryLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!advanceSalaryLevels.contains(RM)) {
			throw new DataNotFoundException(RM_APPROVAL_NOT_REQUIRED);
		}
		List<Long> employeeInfoIdList = employeeReportingInfoRepository
				.findByReportingManagerEmployeeInfoId(employeeInfoId).stream()
				.map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId()).collect(Collectors.toList());

		List<EmployeeAdvanceSalary> employeeAdvanceSalaryList = employeeAdvanceSalaryRepository
				.findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(status,
						companyId, employeeInfoIdList);

		employeeAdvanceSalaryList = filterAdvanceSalaryBasedOnCondition(status, employeeAdvanceSalaryList,
				advanceSalaryLevels);

		return employeeAdvanceSalaryList.stream()
				.filter(advanceSalary -> advanceSalary.getEmployeePersonalInfo() != null
						&& advanceSalary.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(employeeAdvanceSalary -> {
					EmployeePersonalInfo employee = employeeAdvanceSalary.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
					String pendingAt = null;
					if (employeeAdvanceSalary.getStatus().equalsIgnoreCase(PENDING)) {

						if(advanceSalaryLevels.size() == 1) {
							pendingAt = RM;
						}else {
						pendingAt = (employeeAdvanceSalary.getApprovedBy().keySet().contains(RM)
								&& employeeAdvanceSalary.getApprovedBy().keySet().contains(HR)) ? ADMIN
										: (employeeAdvanceSalary.getApprovedBy().keySet().contains(RM)) ? advanceSalaryLevels.get(1) : RM;
						;
						}
					}
					List<EmployeeReportingInfo> employeeInfoList = employee.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if(!employeeInfoList.isEmpty()) {
						reportingManagerDetails =employeeInfoList.get(employeeInfoList.size()-1).getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					return AdvancedSalaryDTO.builder().advanceSalaryId(employeeAdvanceSalary.getAdvanceSalaryId())
							.employeeInfoId(employee.getEmployeeInfoId())
							.advanceSalaryId(employeeAdvanceSalary.getAdvanceSalaryId())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(employee.getFirstName() + " " + employee.getLastName())
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.amount(employeeAdvanceSalary.getAmount())
							.emi(employeeAdvanceSalary.getEmi())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.requestedOn(employeeAdvanceSalary.getCreatedDate())
							.isActionRequired(status.equalsIgnoreCase(PENDING)
									? !(employeeAdvanceSalary.getApprovedBy().keySet().contains(RM))
									: null)
							.reportingManager(reportingManager)
							.pendingAt(pendingAt).rejectedBy(employeeAdvanceSalary.getRejectedBy())
							.status(employeeAdvanceSalary.getStatus()).reason(employeeAdvanceSalary.getReason())
							.rejectionReason(employeeAdvanceSalary.getRejectedReason()).build();
				}).collect(Collectors.toList());
	}

	private List<EmployeeAdvanceSalary> filterAdvanceSalaryBasedOnCondition(String status,
			List<EmployeeAdvanceSalary> employeeAdvanceSalaryList, List<String> levels) {
		if (status.equalsIgnoreCase(PENDING) || status.equalsIgnoreCase(REJECTED)) {
			return employeeAdvanceSalaryList.stream().filter(advanceSalary -> levels.get(0).equalsIgnoreCase(RM))
					.collect(Collectors.toList());
		} else {
			return employeeAdvanceSalaryList;
		}
	}

	@Override
	public AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeAdvanceSalaryId) {

		List<String> level = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeAdvanceSalaryId, companyId)
				.map(employeeAdvanceSalary -> {
					EmployeePersonalInfo employeePersonalInfo = employeeAdvanceSalary.getEmployeePersonalInfo();
					if (employeePersonalInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					String pendingAt = null;
					if (employeeAdvanceSalary.getStatus().equalsIgnoreCase(PENDING)) {

						if (level.size() == 1) {
							pendingAt = RM;
						} else {
							pendingAt = (employeeAdvanceSalary.getApprovedBy().keySet().contains(RM)
									&& employeeAdvanceSalary.getApprovedBy().keySet().contains(HR))
											? ADMIN
											: (employeeAdvanceSalary.getApprovedBy().keySet().contains(RM))
													? level.get(1)
													: RM;
							;
						}
					}
					
					List<EmployeeReportingInfo> employeeInfoList = employeePersonalInfo.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if(!employeeInfoList.isEmpty()) {
						reportingManagerDetails = employeeInfoList.get(employeeInfoList.size()-1).getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName() + " " + reportingManagerDetails.getLastName();
					
					return AdvancedSalaryDTO.builder().employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.advanceSalaryId(employeeAdvanceSalary.getAdvanceSalaryId())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.department(employeeOfficialInfo.getDepartment())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.designation(employeeOfficialInfo.getDesignation())
							.requestedOn(employeeAdvanceSalary.getCreatedDate())
							.amount(employeeAdvanceSalary.getAmount())
							.emi(employeeAdvanceSalary.getEmi())
							.reason(employeeAdvanceSalary.getReason())
							.reportingManager(reportingManager)
							.isActionRequired(employeeAdvanceSalary.getStatus().equalsIgnoreCase(PENDING)
									? !(employeeAdvanceSalary.getApprovedBy().keySet().contains(RM))
									: null)
							.pendingAt(pendingAt).rejectedBy(employeeAdvanceSalary.getRejectedBy())
							.rejectionReason(employeeAdvanceSalary.getRejectedReason()).build();
				}).orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG));
	}

	@Override
	@Transactional
	public String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {
		List<String> levelsOfApproval = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));
		if (!(levelsOfApproval.contains(RM))) {
			throw new DataNotFoundException("RM Response Not Required");
		}
		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						advanceSalaryId, employeeInfoId, companyId)
				.filter(advanceSalary -> advanceSalary.getStatus().equalsIgnoreCase(PENDING))
				.map(employeeAdvanceSalary -> {
					return optional
							.filter(rejectStatus -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(REJECTED))
							.map(r -> {
								employeeAdvanceSalary.setStatus(adminApprovedRejectDto.getStatus());
								employeeAdvanceSalary.setRejectedBy(RM);
								employeeAdvanceSalary.setRejectedReason(adminApprovedRejectDto.getReason());

								return "Request is rejected";
							})
							.orElseGet(() -> optional.filter(
									selectStatus -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(APPROVED))
									.map(a -> {

										employeeAdvanceSalary.setStatus(adminApprovedRejectDto.getStatus());

										LinkedHashMap<String, String> approvedBy = new LinkedHashMap<>();
										approvedBy.put(RM, employeeId);
										employeeAdvanceSalary.setApprovedBy(approvedBy);
										if (levelsOfApproval.size() == 1 && levelsOfApproval.contains(RM)) {
											employeeAdvanceSalary.setStatus(APPROVED);
										}
										return "Request is Approved";

									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException("data not found or request got responsed already"));
	}

}