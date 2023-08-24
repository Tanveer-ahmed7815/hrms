package com.te.flinko.service.hr;

import static com.te.flinko.common.hr.HrConstants.FIXED;
import static com.te.flinko.common.hr.HrConstants.ONE_HUNDRED;
import static com.te.flinko.common.hr.HrConstants.PERCENTAGE;
import static com.te.flinko.common.hr.HrConstants.REIMBURSEMENT;
import static com.te.flinko.common.hr.HrConstants.REWARDS;
import static com.te.flinko.common.hr.HrConstants.STATUS_APPROVED;
import static com.te.flinko.common.hr.HrConstants.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.hr.EmployeeSalaryAllDetailsDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyPayrollDeduction;
import com.te.flinko.entity.admin.CompanyPayrollEarning;
import com.te.flinko.entity.employee.EmployeeAnnualSalary;
import com.te.flinko.entity.employee.EmployeeBonus;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReferenceInfo;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;
import com.te.flinko.entity.employee.EmployeeReviseSalary;
import com.te.flinko.entity.employee.EmployeeSalaryDetails;
import com.te.flinko.entity.employee.EmployeeVariablePay;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeeAnnualSalaryRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReviseSalaryRepository;
import com.te.flinko.repository.employee.EmployeeSalaryDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeSalaryCalcServiceImpl implements EmployeeSalaryCalcService {

	@Autowired
	EmployeeAnnualSalaryRepository employeeAnnualSalaryRepo;

	@Autowired
	EmployeeSalaryDetailsRepository employeeSalaryDetailsRepo;
	
	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepo;
	
	@Autowired
	CompanyInfoRepository companyInfoRepository;
	
	@Autowired
	EmployeeReviseSalaryRepository employeeReviseSalaryRepository;

	@Override
	public Boolean calculateAllEmployeeSalary() {

		List<EmployeeAnnualSalary> employeeAnnualSalaryList = employeeAnnualSalaryRepo
				.findAll();
		Month currMonth = LocalDate.now().getMonth();
		int currMonthValue = LocalDate.now().getMonthValue();
		int currYear = LocalDate.now().getYear();

		// get Annual salary info
		if (employeeAnnualSalaryList == null || employeeAnnualSalaryList.isEmpty())
			return Boolean.FALSE;

		List<EmployeeSalaryDetails> employeeSalaryDetailsList = new ArrayList<>();

		// Calculate salary for all employee one by one
		employeeAnnualSalaryList.forEach(empAnnualSalary -> {

			EmployeePersonalInfo employeelInfo = empAnnualSalary.getEmployeePersonalInfo();

			// Check if salary disabled
			if (Boolean.TRUE.equals(empAnnualSalary.getIsSalaryStopped())
					&& empAnnualSalary.getSalaryStoppedFrom().getMonth() == currMonth) {
				return;
			}

			EmployeeSalaryDetails employeeSalaryDetails = new EmployeeSalaryDetails();

			BigDecimal yearlyCTC = empAnnualSalary.getAnnualSalary();
			Map<String, String> deductions = new HashMap<>();
			Map<String, String> earnings = new HashMap<>();
			Map<String, String> additional = new HashMap<>();
			// check for variable pay
			BigDecimal totalSalary = ZERO;
			BigDecimal netPay = totalSalary;
			// Variable Pay
			if (Boolean.TRUE.equals(empAnnualSalary.getIsPayEnabled())) {
				EmployeeVariablePay variablePay = empAnnualSalary.getEmployeeVariablePay();
				yearlyCTC = yearlyCTC.subtract(variablePay.getAmount());
				List<String> variablePayMonthList = variablePay.getEffectiveMonth();

				for (String monthValue : variablePayMonthList) {
					if (currMonthValue == Integer.parseInt(monthValue)) {
						BigDecimal ammount = variablePay.getAmount().divide(new BigDecimal(variablePayMonthList.size()),
								RoundingMode.HALF_EVEN);
						totalSalary = totalSalary.add(ammount);
						additional.put(variablePay.getDescription(),
								ammount.setScale(2, RoundingMode.HALF_EVEN).toString());
					}
				}
			}

			BigDecimal monthlyCTC = yearlyCTC.divide(new BigDecimal(12), RoundingMode.HALF_EVEN);

			if (empAnnualSalary.getCompanyPayrollInfo() == null) {
				log.error("Error! Unable to find payroll configuration for the employee Id: "+ employeelInfo.getEmployeeInfoId() + "Company Id:" + employeelInfo.getCompanyInfo().getCompanyId());
				
			}
			List<CompanyPayrollEarning> earningList = empAnnualSalary.getCompanyPayrollInfo()
					.getCompanyPayrollEarningList();
			// Earning
			for (CompanyPayrollEarning e : earningList) {
				BigDecimal earningAmount = ZERO;
				if (e.getType().equalsIgnoreCase(PERCENTAGE)) {
					earningAmount = monthlyCTC.multiply(e.getValue()).divide(ONE_HUNDRED);
				} else if (e.getType().equalsIgnoreCase(FIXED)) {
					earningAmount = e.getValue();
				}
				totalSalary = totalSalary.add(earningAmount);
				earnings.put(e.getSalaryComponent(), earningAmount.setScale(2, RoundingMode.HALF_EVEN).toString());
			}

			if (Boolean.TRUE.equals(empAnnualSalary.getIsBonusEnabled())) {
				for (EmployeeBonus b : empAnnualSalary.getEmployeeBonusList()) {
					if (b.getEffectiveDate().getMonth() == currMonth && b.getEffectiveDate().getYear() == currYear) {
						totalSalary = totalSalary.add(b.getAmount());
						additional.put(b.getReason(), b.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString());
					}
				}
			}

			List<EmployeeReimbursementInfo> employeeReimbursementInfos = employeelInfo
					.getEmployeeReimbursementInfoList();

			if (employeeReimbursementInfos != null && !employeeReimbursementInfos.isEmpty()) {
				for (EmployeeReimbursementInfo rInfo : employeeReimbursementInfos) {
					if (rInfo.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
						totalSalary = totalSalary.add(rInfo.getAmount());
						additional.put(REIMBURSEMENT + rInfo.getDescription(),
								rInfo.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString());
					}
				}
			}

			List<EmployeeReferenceInfo> employeeReferenceInfo = employeelInfo.getEmployeeReferenceInfoList();
			BigDecimal rewards = ZERO;

			for (EmployeeReferenceInfo refInfo : employeeReferenceInfo) {
				if (refInfo.getRefferalEmployeePersonalInfo() != null && refInfo.getRewardAmount() != null
						&& refInfo.getIsIncludedInSalary() == Boolean.FALSE) {
					rewards = rewards.add(refInfo.getRewardAmount());
					refInfo.setIsIncludedInSalary(Boolean.TRUE);
				}
			}
			if (rewards != ZERO) {
				additional.put(REWARDS, rewards.toString());
				totalSalary = totalSalary.add(rewards);
				employeelInfo.setEmployeeReferenceInfoList(employeeReferenceInfo);
			}
			netPay = totalSalary;

			List<CompanyPayrollDeduction> deductionList = empAnnualSalary.getCompanyPayrollInfo()
					.getCompanyPayrollDeductionList();

			for (CompanyPayrollDeduction d : deductionList) {
				BigDecimal deductionAmount = ZERO;
				if (d.getType().equalsIgnoreCase(PERCENTAGE)) {
					deductionAmount = monthlyCTC.multiply(d.getValue()).divide(ONE_HUNDRED);
				} else if (d.getType().equalsIgnoreCase(FIXED)) {
					deductionAmount = d.getValue();
				}
				netPay = netPay.subtract(deductionAmount);
				deductions.put(d.getTitle(), deductionAmount.setScale(2, RoundingMode.HALF_EVEN).toString());
			}

			employeeSalaryDetails.setEmployeePersonalInfo(employeelInfo);
			employeeSalaryDetails.setDeduction(deductions);
			employeeSalaryDetails.setEarning(earnings);
			employeeSalaryDetails.setAdditional(additional);
			employeeSalaryDetails.setMonth(currMonthValue);
			employeeSalaryDetails.setYear(currYear);
			employeeSalaryDetails.setTotalSalary(totalSalary);
			employeeSalaryDetails.setNetPay(netPay);
			employeeSalaryDetails.setCompanyInfo(employeelInfo.getCompanyInfo());
			employeeSalaryDetails.setIsPaid(Boolean.FALSE);
			employeeSalaryDetails.setIsFinalized(Boolean.FALSE);
			employeeSalaryDetails.setIsPayslipGenerated(Boolean.FALSE);

			employeeSalaryDetailsList.add(employeeSalaryDetails);
		});

		employeeSalaryDetailsRepo.saveAll(employeeSalaryDetailsList);

		return Boolean.TRUE;

	}

	@Override
	public EmployeeSalaryAllDetailsDTO editEmployeeSalary(EmployeeSalaryAllDetailsDTO employeeSalaryAllDetailsDTO) {
		EmployeeSalaryDetails employeeSalaryDetails = employeeSalaryDetailsRepo
				.findById(employeeSalaryAllDetailsDTO.getEmployeeSalaryId())
				.orElseThrow(() -> new CustomExceptionForHr("Salary details not found"));

		Map<String, String> additionalMap = new HashMap<>(employeeSalaryAllDetailsDTO.getAdditional());
		additionalMap.putAll(employeeSalaryAllDetailsDTO.getReimbursements());
		
		employeeSalaryDetails.setAdditional(additionalMap);
		employeeSalaryDetails.setDeduction(employeeSalaryAllDetailsDTO.getDeduction());
		employeeSalaryDetails.setRemarks(employeeSalaryAllDetailsDTO.getRemarks());
		
		BigDecimal totalSalary = BigDecimal
				.valueOf(employeeSalaryDetails.getEarning().values().stream().mapToDouble(Double::valueOf).sum()
						+ additionalMap.values().stream().mapToDouble(Double::valueOf).sum());

		BigDecimal netPay = totalSalary.subtract(BigDecimal.valueOf(
				employeeSalaryAllDetailsDTO.getDeduction().values().stream().mapToDouble(Double::valueOf).sum()));

		employeeSalaryDetails.setTotalSalary(totalSalary);
		employeeSalaryDetails.setNetPay(netPay);

		EmployeeSalaryDetails updatedSalaryDetails = employeeSalaryDetailsRepo.save(employeeSalaryDetails);

		EmployeeSalaryAllDetailsDTO updatedSalaryDetailsDTO = new EmployeeSalaryAllDetailsDTO();
		BeanUtils.copyProperties(updatedSalaryDetails, updatedSalaryDetailsDTO);
		updatedSalaryDetailsDTO.setGrossPay(updatedSalaryDetails.getTotalSalary());
		updatedSalaryDetailsDTO.setEmployeeId(updatedSalaryDetails.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
		updatedSalaryDetailsDTO.setEmployeeName(updatedSalaryDetails.getEmployeePersonalInfo().getFirstName() + " " + updatedSalaryDetails.getEmployeePersonalInfo().getLastName());
		updatedSalaryDetailsDTO.setReimbursements(employeeSalaryAllDetailsDTO.getReimbursements());
		updatedSalaryDetailsDTO.setRemarks(updatedSalaryDetails.getRemarks());
		
		updatedSalaryDetailsDTO.setAdditional(employeeSalaryAllDetailsDTO.getAdditional());
		
		return updatedSalaryDetailsDTO;
	}

	@Override
	public Boolean calculateReviseSalary() {
		LocalDate currDate = LocalDate.now();
		List<CompanyInfo> companyInfoList =  companyInfoRepository.findAll();
		List<EmployeeReviseSalary> employeeReviseSalaries = new ArrayList<>();
		
		companyInfoList.stream().forEach(company -> {
			if(company.getCompanyRuleInfo()== null || company.getCompanyRuleInfo().getApprisalCycle() == null) {
				log.error("No apprisal configuration for company Id: "+ company.getCompanyId());
				return;
			}
			Integer apprisalCycle = Integer.parseInt(company.getCompanyRuleInfo().getApprisalCycle()) ;
			
			company.getEmployeePersonalInfoList().stream().forEach(employee ->{
				
				LocalDate lastRevisionDate;
				List<EmployeeReviseSalary> pastReviseSalaries = employee.getEmployeeReviseSalaryList();
				if(pastReviseSalaries==null|| pastReviseSalaries.isEmpty()) {
					if(employee.getEmployeeOfficialInfo() == null) {
						lastRevisionDate=null;
					}
					else {
					lastRevisionDate = employee.getEmployeeOfficialInfo().getDoj();
					}
				}
				else {
					lastRevisionDate = pastReviseSalaries.get(pastReviseSalaries.size() - 1).getRevisedDate();
				}
				if(lastRevisionDate == null) { 
					log.error("Unable to find last revise date for employee Id: " + employee.getEmployeeInfoId());
					return;
				}
				
				if(lastRevisionDate.plusMonths(apprisalCycle).minusDays(7).compareTo(currDate)!=0) {
					return;
				}
				EmployeeReviseSalary employeeReviseSalary = new EmployeeReviseSalary();
				employeeReviseSalary.setApprisalDate(currDate.plusDays(7));
				employeeReviseSalary.setApprisalDate(lastRevisionDate.plusMonths(apprisalCycle));
				employeeReviseSalary.setEmployeePersonalInfo(employee);
				employeeReviseSalary.setCompanyInfo(company);
				employeeReviseSalaries.add(employeeReviseSalary);
			});
		});
		employeeReviseSalaryRepository.saveAll(employeeReviseSalaries);
		return Boolean.TRUE;

	}

}
