package com.te.flinko.service.hr;

import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.HrConstants.EMPLOYEE_SALARY_RECORD_NOT_FOUND;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class NotificationReviseSalaryServiceImpl  implements NotificationReviseSalaryService{
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
		log.info("company info verify");
		ArrayList<EmployeeReviseSalaryDTO> dropDownList = new ArrayList<>();
		List<EmployeeReviseSalary> employeedetails = employeeReviseSalaryRepo
				.findByCompanyInfoCompanyIdAndAmountNotNullAndRevisedDateNullAndReasonNotNull(
						companyInfo.getCompanyId());

		for (EmployeeReviseSalary employeeReviseSalary : employeedetails) {
			EmployeePersonalInfo employeePersonalInfo = employeeReviseSalary.getEmployeePersonalInfo();
			EmployeeOfficialInfo employeeOfficialInfo = employeeReviseSalary.getEmployeePersonalInfo()
					.getEmployeeOfficialInfo();

			if (employeePersonalInfo != null && employeeOfficialInfo != null) {
				dropDownList.add(new EmployeeReviseSalaryDTO(employeeReviseSalary.getReviseSalaryId(),
						employeeOfficialInfo.getEmployeeId(), companyId,
						employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName(),
						employeeOfficialInfo.getDesignation(), employeeOfficialInfo.getDepartment(),
						employeeReviseSalary.getAmount(), employeeReviseSalary.getReason()));

			}

		}
		return dropDownList;
	}

	@Override
	public ReviseSalaryByIdDTO reviseSalaryById(Long companyId, Long reviseSalaryId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info("company info verify");
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
		return updateReviseSalaryDTO;
	}

	@Override
	public UpdateReviseSalaryDTO updateRevisedsalary(UpdateReviseSalaryDTO reviseSalaryDTO) {
		UpdateReviseSalaryDTO updateReviseSalaryDTO = new UpdateReviseSalaryDTO();
		CompanyInfo companyInfo = companyInfoRepo.findById(reviseSalaryDTO.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info("company info verify");
		List<EmployeeReviseSalary> reviseSalary = employeeReviseSalaryRepo.findByCompanyInfoCompanyIdAndReviseSalaryId(
				companyInfo.getCompanyId(), reviseSalaryDTO.getReviseSalaryId());
		if (reviseSalary.isEmpty()) {
			log.error(EMPLOYEE_SALARY_RECORD_NOT_FOUND);
			throw new CustomExceptionForHr("employee salary Details not found");
		}
		EmployeeReviseSalary employeeReviseSalary = reviseSalary.get(0);
		List<EmployeeAnnualSalary> annualSalary = annualSalaryRepo
				.findByEmployeePersonalInfoEmployeeInfoId(employeeReviseSalary.getEmployeePersonalInfo().getEmployeeInfoId());
						
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
