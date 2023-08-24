package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.admin.CompanyExpenseCategoriesDTO;
import com.te.flinko.entity.admin.CompanyExpenseCategories;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.exception.admin.CompanyNotExistException;
import com.te.flinko.exception.admin.NoExpensePresentException;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyExpenseCategoriesRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@Service
@RequiredArgsConstructor
public class CompanyExpenseCategoriesServiceImpl implements CompanyExpenseCategoriesService {

	@Autowired
	private final CompanyExpenseCategoriesRepository companyExpenseCategoriesRepository;

	@Autowired
	private final CompanyInfoRepository companyInfoRepository;

	@Override
	public List<CompanyExpenseCategoriesDTO> getExpense(CompanyExpenseCategoriesDTO companyExpenseCategoriesDto,
			Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotExistException("No Company Present With This ID"));

		List<CompanyExpenseCategories> companyExpenseCategoriesList = Optional
				.ofNullable(companyInfo.getCompanyExpenseCategoriesList())
				.orElseThrow(() -> new NoExpensePresentException("No expences available"));

		List<CompanyExpenseCategoriesDTO> companyExpenseCategoriesDtoList = new ArrayList<>();

		for (CompanyExpenseCategories companyExpenseCategories : companyExpenseCategoriesList) {

			CompanyExpenseCategoriesDTO companyExpenseCategoriesDto1 = new CompanyExpenseCategoriesDTO();
			companyExpenseCategoriesDto1.setExpenseCategoryId(companyExpenseCategories.getExpenseCategoryId());
			companyExpenseCategoriesDto1.setExpenseCategoryName(companyExpenseCategories.getExpenseCategoryName());
			companyExpenseCategoriesDto1.setIsSubmited(companyInfo.getIsSubmited());
			companyExpenseCategoriesDtoList.add(companyExpenseCategoriesDto1);
		}

		return companyExpenseCategoriesDtoList;
	}

	@Override
	public String updateExpense(List<CompanyExpenseCategoriesDTO> companyExpenseCategoriesDto, Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.map(com -> Optional.of(com.getIsSubmited() == null ? Boolean.FALSE : com.getIsSubmited()).filter(x -> !x)
						.map(y -> com).orElseThrow(() -> new CompanyNotExistException("Company Already Configured!!!")))
				.orElseThrow(() -> new CompanyNotExistException("No Company Present With This ID"));

		List<Long> expensesIdDto = new ArrayList<>();
		List<String> expensesNameDto = new ArrayList<>();
		Set<String> expensesNameDtoSet = new HashSet<>();
		companyExpenseCategoriesDto.forEach(expenses -> {

			if (!expensesNameDtoSet.add(expenses.getExpenseCategoryName())) {
				throw new DataNotFoundException("expenses name is same");
			}
			expensesNameDto.add(expenses.getExpenseCategoryName());
			expensesIdDto.add(expenses.getExpenseCategoryId());
		});

		List<Long> expensesIddb = new ArrayList<>();
		List<String> expensesNameDb = new ArrayList<>();
		companyInfo.getCompanyExpenseCategoriesList().forEach(expenses -> {
			expensesIddb.add(expenses.getExpenseCategoryId());
			expensesNameDb.add(expenses.getExpenseCategoryName());
		});

		List<CompanyExpenseCategories> companyExpenseCategoriesList = new ArrayList<>();
		companyExpenseCategoriesDto.forEach(expenses -> {
			CompanyExpenseCategories companyExpenseCategories = new CompanyExpenseCategories();
			BeanUtils.copyProperties(expenses, companyExpenseCategories);
			companyExpenseCategories.setCompanyInfo(companyInfo);
			companyExpenseCategoriesList.add(companyExpenseCategories);
		});

		List<Long> duplicateValue = duplicateValue(expensesIddb, expensesIdDto);
		companyExpenseCategoriesRepository.deleteAllByIdInBatch(duplicateValue);
		companyExpenseCategoriesRepository.saveAll(companyExpenseCategoriesList);

		return expensesIddb.isEmpty() ? "Add Company Expense Successfully!!!"
				: "Update Company Expense Successfully!!!";
	}

	public static List<Long> duplicateValue(List<Long> expensesIddb, List<Long> expensesIdDto) {
		Set<Long> set1 = new HashSet<>(expensesIddb);
		Set<Long> set2 = new HashSet<>(expensesIdDto);
		List<Long> duplicateList = new ArrayList<>();
		for (Long long1 : set1) {
			boolean contains = set2.contains(long1);
			if (!contains) {
				duplicateList.add(long1);
			}
		}
		return duplicateList;

	}
}
