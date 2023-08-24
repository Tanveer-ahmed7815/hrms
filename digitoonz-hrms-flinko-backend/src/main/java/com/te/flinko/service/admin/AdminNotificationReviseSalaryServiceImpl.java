package com.te.flinko.service.admin;

import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.employee.ReviseSalaryByIdDTO;
import com.te.flinko.dto.hr.UpdateReviseSalaryDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeAnnualSalary;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReviseSalary;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeeAnnualSalaryRepository;
import com.te.flinko.repository.employee.EmployeeReviseSalaryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminNotificationReviseSalaryServiceImpl implements AdminNotificationReviseSalaryService {
	private static final String COMPANY_INFO_VERIFY = "company info verify";
	@Autowired
	CompanyInfoRepository companyInfoRepo;
	@Autowired
	EmployeeAnnualSalaryRepository annualSalaryRepo;
	@Autowired
	private EmployeeReviseSalaryRepository employeeReviseSalaryRepo;

	@Override
	public List<EmployeeReviseSalaryDTO> reviseSalary(Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFO_VERIFY);
		ArrayList<EmployeeReviseSalaryDTO> dropDownList = new ArrayList<>();
		List<EmployeeReviseSalary> employeedetails = employeeReviseSalaryRepo
				.findByCompanyInfoCompanyId(companyInfo.getCompanyId());

		for (EmployeeReviseSalary employeeReviseSalary : employeedetails) {
			EmployeePersonalInfo employeePersonalInfo = employeeReviseSalary.getEmployeePersonalInfo();
			EmployeeOfficialInfo employeeOfficialInfo = employeeReviseSalary.getEmployeePersonalInfo()
					.getEmployeeOfficialInfo();

			if (employeePersonalInfo != null && employeeOfficialInfo != null
					&& employeePersonalInfo.getCompanyInfo().getCompanyId().equals(companyId)
					&& employeePersonalInfo.getIsActive() != null
					&& employeePersonalInfo.getIsActive().booleanValue()) {
				dropDownList.add(new EmployeeReviseSalaryDTO(employeeReviseSalary.getReviseSalaryId(),
						employeeOfficialInfo.getEmployeeId(), companyId,
						employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName(),
						employeeOfficialInfo.getDesignation(), employeeOfficialInfo.getDepartment(),
						employeeReviseSalary.getAmount(), employeeReviseSalary.getApprisalDate(),
						employeeReviseSalary.getReason(),
						employeeReviseSalary.getRevisedDate() == null ? "PENDING" : "REVISED"));
			}

		}
		return dropDownList;
	}

	@Override
	public ReviseSalaryByIdDTO reviseSalaryById(Long companyId, Long reviseSalaryId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFO_VERIFY);
		List<EmployeeReviseSalary> salaryinfo = employeeReviseSalaryRepo
				.findByCompanyInfoCompanyIdAndReviseSalaryId(companyInfo.getCompanyId(), reviseSalaryId);
		if (salaryinfo.isEmpty()) {
			log.error("employee record not found");
			throw new CustomExceptionForHr("employee record not found");
		}
		EmployeeReviseSalary employeeReviseSalary = salaryinfo.get(0);
		ReviseSalaryByIdDTO updateReviseSalaryDTO = new ReviseSalaryByIdDTO();
		updateReviseSalaryDTO
				.setBranch(employeeReviseSalary.getCompanyInfo().getCompanyBranchInfoList().get(0).getBranchName());
		updateReviseSalaryDTO.setDepartment(
				employeeReviseSalary.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDepartment());
		updateReviseSalaryDTO.setDesignation(
				employeeReviseSalary.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDesignation());
		updateReviseSalaryDTO.setDueDate(employeeReviseSalary.getApprisalDate());
		updateReviseSalaryDTO.setEmployeeId(
				employeeReviseSalary.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
		updateReviseSalaryDTO.setFullName(employeeReviseSalary.getEmployeePersonalInfo().getFirstName() + " "
				+ employeeReviseSalary.getEmployeePersonalInfo().getLastName());
		updateReviseSalaryDTO.setRevisedSalary(employeeReviseSalary.getAmount());
		updateReviseSalaryDTO.setReason(employeeReviseSalary.getReason());
		updateReviseSalaryDTO.setRevisedDate(employeeReviseSalary.getRevisedDate());
		updateReviseSalaryDTO.setCompanyId(employeeReviseSalary.getCompanyInfo().getCompanyId());
		updateReviseSalaryDTO.setReviseSalaryId(employeeReviseSalary.getReviseSalaryId());
		updateReviseSalaryDTO.setStatus(employeeReviseSalary.getRevisedDate() == null ? "PENDING" : "REVISED");
		return updateReviseSalaryDTO;
	}

	@Transactional
	@Override
	public UpdateReviseSalaryDTO updateRevisedsalary(UpdateReviseSalaryDTO reviseSalaryDTO) {
		UpdateReviseSalaryDTO updateReviseSalaryDTO = new UpdateReviseSalaryDTO();
		CompanyInfo companyInfo = companyInfoRepo.findById(reviseSalaryDTO.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFO_VERIFY);
		List<EmployeeReviseSalary> reviseSalary = employeeReviseSalaryRepo.findByCompanyInfoCompanyIdAndReviseSalaryId(
				companyInfo.getCompanyId(), reviseSalaryDTO.getReviseSalaryId());
		if (reviseSalary.isEmpty()) {
			log.error("employee salary Details not found");
			throw new CustomExceptionForHr("employee salary Details not found");
		}
		EmployeeReviseSalary employeeReviseSalary = reviseSalary.get(0);
		List<EmployeeAnnualSalary> annualSalary = annualSalaryRepo.findByEmployeePersonalInfoEmployeeInfoId(
				employeeReviseSalary.getEmployeePersonalInfo().getEmployeeInfoId());

		if (annualSalary.isEmpty()) {
			log.error("employee annual salary record not found");
			throw new CustomExceptionForHr("employee annual salary details not found");
		}
		employeeReviseSalary.setRevisedDate(LocalDate.now());
		employeeReviseSalary.setAmount(reviseSalaryDTO.getRevisedSalary());
		employeeReviseSalary.setReason(reviseSalaryDTO.getReason());
		EmployeeReviseSalary saveReviseSalary = employeeReviseSalaryRepo.save(employeeReviseSalary);

		BeanUtils.copyProperties(saveReviseSalary, updateReviseSalaryDTO);

		EmployeeAnnualSalary employeeAnnualSalary = annualSalary.get(0);
		employeeAnnualSalary.setAnnualSalary(reviseSalaryDTO.getRevisedSalary());
		EmployeeAnnualSalary updatedsalary = annualSalaryRepo.save(employeeAnnualSalary);
		updateReviseSalaryDTO.setRevisedSalary(updatedsalary.getAnnualSalary());
		updateReviseSalaryDTO.setCompanyId(companyInfo.getCompanyId());
		return updateReviseSalaryDTO;
	}

}
