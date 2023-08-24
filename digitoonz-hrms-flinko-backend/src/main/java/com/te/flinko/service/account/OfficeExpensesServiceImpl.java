package com.te.flinko.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.account.OfficeExpensesDTO;
import com.te.flinko.dto.account.OfficeExpensesTotalCostDTO;
import com.te.flinko.entity.admin.CompanyExpenseCategories;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyExpenseCategoriesRepository;
import com.te.flinko.repository.employee.EmployeePersonelInfoRepository;
import com.te.flinko.repository.employee.EmployeeReimbursementInfoRepository;
import com.te.flinko.util.S3UploadFile;

@Service
public class OfficeExpensesServiceImpl implements OfficeExpenesesService {

	@Autowired
	EmployeeReimbursementInfoRepository employeeReimbursementInfoRepository;

	@Autowired
	EmployeePersonelInfoRepository employeePersonelInfoRepository;

	@Autowired
	CompanyExpenseCategoriesRepository categoriesRepository;

	@Autowired
	S3UploadFile uploadFile;

	@Override
	public List<OfficeExpensesTotalCostDTO> getOfficeExpenseDetails(Long companyId) {
		List<EmployeeReimbursementInfo> employeeReimbursementInfos = employeeReimbursementInfoRepository
				.findByCompanyExpenseCategoriesCompanyInfoCompanyId(companyId);
		List<CompanyExpenseCategories> listOfCompanyExpensesCategories = categoriesRepository
				.findByCompanyInfoCompanyId(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company expense Categories details not found"));
		if (listOfCompanyExpensesCategories.isEmpty())
			throw new DataNotFoundException("No details found");
		Map<String, List<EmployeeReimbursementInfo>> groupedCategories = new HashMap<>();
		listOfCompanyExpensesCategories.stream().forEach((i) -> {
			List<EmployeeReimbursementInfo> listOfReibursement = new ArrayList<>();
			employeeReimbursementInfos.forEach((r) -> {
				if (i.getExpenseCategoryName().equals(r.getCompanyExpenseCategories().getExpenseCategoryName())) {
					listOfReibursement.add(r);
				}
			});
			groupedCategories.put(i.getExpenseCategoryName(), listOfReibursement);
		});

		List<OfficeExpensesTotalCostDTO> response = new ArrayList<>();
		groupedCategories.forEach((n, l) -> {
			OfficeExpensesTotalCostDTO officeExpensesTotalCostDTO = new OfficeExpensesTotalCostDTO();
			officeExpensesTotalCostDTO.setType(n);
			officeExpensesTotalCostDTO.setTotalCost(l.stream().mapToDouble((i) -> i.getAmount().doubleValue()).sum());
			response.add(officeExpensesTotalCostDTO);
		});
		return response;
	}

	@Override
	public OfficeExpensesDTO addOfficeExpenses(OfficeExpensesDTO addOfficeExpensesDTO, MultipartFile multipartFile,Long companyId) {
		EmployeePersonalInfo employeePersonalInfo = employeePersonelInfoRepository
				.findById(addOfficeExpensesDTO.getEmployeeInfoId())
				.orElseThrow(() -> new DataNotFoundException("Employee Details Not Found"));

		CompanyExpenseCategories companyExpenseCategories = categoriesRepository
				.findByExpenseCategoryIdAndCompanyInfoCompanyId(addOfficeExpensesDTO.getExpenseCategoryId(), companyId)
				.orElseThrow(() -> new DataNotFoundException("Category not found"));

		EmployeeReimbursementInfo employeeReimbursementInfo = new EmployeeReimbursementInfo();

		BeanUtils.copyProperties(addOfficeExpensesDTO, employeeReimbursementInfo);
		employeeReimbursementInfo.setEmployeePersonalInfo(employeePersonalInfo);
		employeeReimbursementInfo.setCompanyExpenseCategories(companyExpenseCategories);

		employeeReimbursementInfo.setAttachmentUrl(uploadFile.uploadFile(multipartFile));

		EmployeeReimbursementInfo saved = employeeReimbursementInfoRepository.save(employeeReimbursementInfo);

		OfficeExpensesDTO responseDTO = new OfficeExpensesDTO();
		BeanUtils.copyProperties(saved, responseDTO);
		return responseDTO;
	}

	@Override
	public OfficeExpensesDTO getReimbursementById(Long reimbursementId) {
		EmployeeReimbursementInfo reimbursementInfo = employeeReimbursementInfoRepository.findById(reimbursementId)
				.orElseThrow(() -> new DataNotFoundException("Reimbursment Info Not Found"));
		OfficeExpensesDTO officeExpensesDTO = new OfficeExpensesDTO();
		BeanUtils.copyProperties(reimbursementInfo, officeExpensesDTO);
		officeExpensesDTO.setExpenseCategoryName(reimbursementInfo.getCompanyExpenseCategories().getExpenseCategoryName());
		return officeExpensesDTO;
	}

}
