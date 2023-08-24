package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyStockCategoriesDTO;
import com.te.flinko.dto.admin.CompanyStockGroupDTO;
import com.te.flinko.dto.admin.CompanyStockUnitsDTO;
import com.te.flinko.dto.admin.StockCategoriesDTO;
import com.te.flinko.dto.admin.StockGroupDTO;
import com.te.flinko.dto.admin.StockUnitDto;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyStockCategories;
import com.te.flinko.entity.admin.CompanyStockGroup;
import com.te.flinko.entity.admin.CompanyStockUnits;
import com.te.flinko.exception.admin.CategoryChildPresentException;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.admin.DuplicateCompanyStockCategoriesNameException;
import com.te.flinko.exception.admin.DuplicateStockGroupNameException;
import com.te.flinko.exception.admin.DuplicateStockUnitSymbolException;
import com.te.flinko.exception.admin.IsSubmittedException;
import com.te.flinko.exception.admin.StockCategoryNotFoundException;
import com.te.flinko.exception.admin.StockGroupNotFound;
import com.te.flinko.exception.admin.StockUnitNotFoundException;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyStockCategoriesRepository;
import com.te.flinko.repository.admin.CompanyStockGroupRepository;
import com.te.flinko.repository.admin.CompanyStockUnitsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@Slf4j
@Service
public class StockManagementServiceImpl implements StockManagementService {

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyStockUnitsRepository companyStockUnitsRepository;

	@Autowired
	private CompanyStockGroupRepository companyStockGroupRepository;

	@Autowired
	private CompanyStockCategoriesRepository companyStockCategoriesRepository;

	@Override
	public List<CompanyStockGroupDTO> getStockGroup(Long companyId) {
		log.info("service method getStockGroup of StockManagementServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyStockGroupDTO> companyStockGroupDTOs = new ArrayList<>();
		for (CompanyStockGroup companyStockGroup : companyInfo.getCompanyStockGroupList()) {
			CompanyStockGroupDTO companyStockGroupDTO = new CompanyStockGroupDTO();
			BeanUtils.copyProperties(companyStockGroup, companyStockGroupDTO);
			companyStockGroupDTO.setIsSubmited(companyInfo.getIsSubmited());
			companyStockGroupDTOs.add(companyStockGroupDTO);
		}
		
		if (companyStockGroupDTOs.isEmpty()) {
			if (companyInfo.getIsSubmited() == null || !companyInfo.getIsSubmited()) {
				CompanyStockGroup companyStockGroup=new CompanyStockGroup();
				companyStockGroup.setStockGroupName("IT");
				companyStockGroup.setCompanyInfo(companyInfo);
				CompanyStockGroup savedCompanyStockGroup = companyStockGroupRepository.save(companyStockGroup);
				CompanyStockGroupDTO companyStockGroupDTO = new CompanyStockGroupDTO();
				BeanUtils.copyProperties(savedCompanyStockGroup, companyStockGroupDTO);
				companyStockGroupDTOs.add(companyStockGroupDTO);
			}
			else {
				throw new IsSubmittedException(AdminConstants.SUBMITTED_ALREADY);
			}

		}
		
		log.info("service method returned values--- stock group details of company : {}", companyStockGroupDTOs);
		return companyStockGroupDTOs;

	}

	@Override
	@Transactional
	public StockGroupDTO saveStockGroup(Long companyId, StockGroupDTO stockGroupDTO) {
		log.info(
				"service method saveStockGroup of StockManagementServiceImpl class company id is : {},stock group details : {}",
				companyId, stockGroupDTO);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		if (companyInfo.getIsSubmited() == null || !companyInfo.getIsSubmited()) {

			List<String> listOfStockGroupNameDto = stockGroupDTO.getCompanyStockGroupDTOs().stream()
					.map(CompanyStockGroupDTO::getStockGroupName).collect(Collectors.toList());

			Set<String> setOfStockGroupNameDto = new HashSet<>(listOfStockGroupNameDto);

			if (setOfStockGroupNameDto.size() < listOfStockGroupNameDto.size()) {
				throw new DuplicateStockGroupNameException("duplicate stock group name are present");
			}

			
			
			List<Long> listOfStockGroupIdDto = stockGroupDTO.getCompanyStockGroupDTOs().stream()
					.map(CompanyStockGroupDTO::getStockGroupId).collect(Collectors.toList());

			
			
			

			
			
			List<Long> listOfStockGroup = companyInfo.getCompanyStockGroupList().stream().map(CompanyStockGroup::getStockGroupId).collect(Collectors.toList());
			
			List<Long> deleteIds = deleteStockId(listOfStockGroupIdDto, listOfStockGroup);


			try {
				companyStockGroupRepository.deleteAllByIdInBatch(deleteIds);
			} catch (Exception e) {
				throw new CategoryChildPresentException(
						"stock group can not be deleted !! stock category exist for stock group ");
			}

			List<CompanyStockGroupDTO> listOfCompanyStockGroupDTOs=stockGroupDTO.getCompanyStockGroupDTOs().stream().filter(x -> !x.getStockGroupName().equals("IT")).collect(Collectors.toList());
			
			StockGroupDTO stockGroupDTO2 = new StockGroupDTO();
			List<CompanyStockGroupDTO> companyStockGroupDTOs = new ArrayList<>();

			for (int i = 0; i < listOfCompanyStockGroupDTOs.size(); i++) {

				CompanyStockGroupDTO companyStockGroupDTO = listOfCompanyStockGroupDTOs.get(i);

				CompanyStockGroup companyStockGroup = new CompanyStockGroup();

				BeanUtils.copyProperties(companyStockGroupDTO, companyStockGroup);

				companyStockGroup.setCompanyInfo(companyInfo);

				companyStockGroup = companyStockGroupRepository.save(companyStockGroup);

				BeanUtils.copyProperties(companyStockGroup, companyStockGroupDTO);

				companyStockGroupDTOs.add(companyStockGroupDTO);
			}

			stockGroupDTO2.setCompanyStockGroupDTOs(companyStockGroupDTOs);

			return stockGroupDTO2;

		} else {
			throw new IsSubmittedException(AdminConstants.SUBMITTED_ALREADY);
		}

	}

	@Override
	public List<CompanyStockUnitsDTO> getStockUnits(Long companyId) {
		log.info("service method getStockUnits of StockManagementServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyStockUnitsDTO> companyStockUnitsDTOs = new ArrayList<>();
		for (CompanyStockUnits companyStockUnits : companyInfo.getCompanyStockUnitsList()) {
			CompanyStockUnitsDTO companyStockUnitsDTO = new CompanyStockUnitsDTO();
			BeanUtils.copyProperties(companyStockUnits, companyStockUnitsDTO);
			companyStockUnitsDTO.setIsSubmited(companyInfo.getIsSubmited());
			companyStockUnitsDTOs.add(companyStockUnitsDTO);
		}
		log.info("service method returned values--- stock unit details of company : {}", companyStockUnitsDTOs);
		return companyStockUnitsDTOs;

	}

	@Override
	@Transactional
	public StockUnitDto saveStockUnits(Long companyId, StockUnitDto stockUnitDTO) {
		log.info(
				"service method saveStockGroup of StockManagementServiceImpl class company id is : {},stock group details : {}",
				companyId, stockUnitDTO);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		if (companyInfo.getIsSubmited() == null || !companyInfo.getIsSubmited()) {

			List<String> listOfStockunitSymbolDto = stockUnitDTO.getCompanyStockUnitsDTOs().stream()
					.map(CompanyStockUnitsDTO::getSymbol).collect(Collectors.toList());

			Set<String> setOfStockunitSymbolDto = new HashSet<>(listOfStockunitSymbolDto);

			if (setOfStockunitSymbolDto.size() < listOfStockunitSymbolDto.size()) {
				throw new DuplicateStockUnitSymbolException("duplicate stock unit symbol are present");
			}

			List<String> listOfStockunitUqcDto = stockUnitDTO.getCompanyStockUnitsDTOs().stream()
					.map(CompanyStockUnitsDTO::getUqc).collect(Collectors.toList());

			Set<String> setOfStockunitUqcDto = new HashSet<>(listOfStockunitUqcDto);

			if (setOfStockunitUqcDto.size() < listOfStockunitUqcDto.size())
				throw new DuplicateStockUnitSymbolException("duplicate stock unit uqc are present");

			List<Long> listOfStockunitIdDto = stockUnitDTO.getCompanyStockUnitsDTOs().stream()
					.map(CompanyStockUnitsDTO::getUnitId).collect(Collectors.toList());

			
			
			List<Long> listOfStockUnit = companyInfo.getCompanyStockUnitsList().stream().map(CompanyStockUnits::getUnitId).collect(Collectors.toList());
			
			List<Long> deleteIds = deleteStockId(listOfStockunitIdDto, listOfStockUnit);

			try {
				companyStockUnitsRepository.deleteAllByIdInBatch(deleteIds);
			} catch (Exception e) {
				throw new CategoryChildPresentException(
						"stock unit can not be deleted !! stock category exist for stock unit ");
			}

			StockUnitDto stockUnitDTO2 = new StockUnitDto();
			List<CompanyStockUnitsDTO> companyStockUnitsDTO = new ArrayList<>();

			for (int i = 0; i < stockUnitDTO.getCompanyStockUnitsDTOs().size(); i++) {

				CompanyStockUnitsDTO companyStockUnitDTO = stockUnitDTO.getCompanyStockUnitsDTOs().get(i);

				CompanyStockUnits companyStockUnit = new CompanyStockUnits();

				BeanUtils.copyProperties(companyStockUnitDTO, companyStockUnit);

				companyStockUnit.setCompanyInfo(companyInfo);

				companyStockUnit = companyStockUnitsRepository.save(companyStockUnit);

				BeanUtils.copyProperties(companyStockUnit, companyStockUnitDTO);

				companyStockUnitsDTO.add(companyStockUnitDTO);
			}

			stockUnitDTO2.setCompanyStockUnitsDTOs(companyStockUnitsDTO);

			return stockUnitDTO2;

		} else {
			throw new IsSubmittedException(AdminConstants.SUBMITTED_ALREADY);
		}

	}
	
	static List<Long> deleteStockId(List<Long> listOfStockDto,List<Long> listOfStock){

		List<Long> deleteIds = new ArrayList<>();
		
		for (int i = 0; i < listOfStock.size(); i++) {
			if (!listOfStockDto.contains(listOfStock.get(i))) {
				deleteIds.add(listOfStock.get(i));
			}
		}
		return deleteIds;
	}

	@Override
	@Transactional
	public StockCategoriesDTO saveStockCategory(Long companyId, StockCategoriesDTO stockCategoriesDTO) {
		log.info(
				"service method saveStockCategory of StockManagementServiceImpl class company id is : {},stock categories details : {}",
				companyId, stockCategoriesDTO);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		if (!(companyInfo.getIsSubmited() == null || !companyInfo.getIsSubmited())) {
			throw new IsSubmittedException(AdminConstants.SUBMITTED_ALREADY);
		}

		List<Long> listOfStockGroupIdDto = stockCategoriesDTO.getCompanyStockCategoriesDTOs().stream()
				.map(CompanyStockCategoriesDTO::getStockGroupId).collect(Collectors.toList());

		Map<Long, Long> collectionOfStockGroupId = listOfStockGroupIdDto.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		for (Long stockGroupId : collectionOfStockGroupId.keySet()) {
			List<String> collectionOfMatchingCategoryName = stockCategoriesDTO.getCompanyStockCategoriesDTOs().stream()
					.filter(x -> x.getStockGroupId().equals(stockGroupId))
					.map(CompanyStockCategoriesDTO::getStockCategoryName).collect(Collectors.toList());
			Set<String> setOfMatchingCategoryName = new HashSet<>(collectionOfMatchingCategoryName);

			if (setOfMatchingCategoryName.size() < collectionOfMatchingCategoryName.size()) {
				throw new DuplicateCompanyStockCategoriesNameException("duplicate stock category name are present");
			}
		}

		List<Long> listOfStockCategoriesIdDto = stockCategoriesDTO.getCompanyStockCategoriesDTOs().stream()
				.map(CompanyStockCategoriesDTO::getStockCategoryId).collect(Collectors.toList());

		List<Long> deleteIds = new ArrayList<>();

		for (int i = 0; i < companyInfo.getCompanyStockGroupList().size(); i++) {

			List<CompanyStockCategories> companyStockCategoriesList = companyInfo.getCompanyStockGroupList().get(i)
					.getCompanyStockCategoriesList();

			List<Long> deleteListOfStockCategoriesIdDto = companyStockCategoriesList.stream()

					.map(CompanyStockCategories::getStockCategoryId).collect(Collectors.toList());

			deleteListOfStockCategoriesIdDto.removeAll(listOfStockCategoriesIdDto);

			deleteIds.addAll(deleteListOfStockCategoriesIdDto);
		}

		Set<Long> deleteSet = new HashSet<>(deleteIds);

		companyStockCategoriesRepository.deleteAllByIdInBatch(deleteSet);

		StockCategoriesDTO stockCategoriesDTO2 = new StockCategoriesDTO();
		List<CompanyStockCategoriesDTO> companyStockCategoriesDTOs = new ArrayList<>();

		for (int i = 0; i < stockCategoriesDTO.getCompanyStockCategoriesDTOs().size(); i++) {

			CompanyStockCategoriesDTO companyStockCategoriesDTO = stockCategoriesDTO.getCompanyStockCategoriesDTOs()
					.get(i);

			CompanyStockGroup companyStockGroup1 = StockManagementServiceImpl.findCompanyStockGroup(companyInfo,
					companyStockCategoriesDTO);

			CompanyStockUnits companyStockUnits1 = StockManagementServiceImpl.findCompanyStockUnit(companyInfo,
					companyStockCategoriesDTO);

			CompanyStockCategories companyStockCategories = new CompanyStockCategories();

			if (companyStockCategoriesDTO.getStockCategoryId() != null) {
				Optional<CompanyStockCategories> companyStockCategoriesOptional = companyStockCategoriesRepository
						.findById(companyStockCategoriesDTO.getStockCategoryId());
				if (!companyStockCategoriesOptional.isPresent()) {
					throw new StockCategoryNotFoundException("stock category id not found");
				}
				companyStockCategories = companyStockCategoriesOptional.get();
				BeanUtils.copyProperties(companyStockCategoriesDTO, companyStockCategories);
				companyStockCategories.setCompanyStockGroup(companyStockGroup1);
				companyStockCategories.setCompanyStockUnits(companyStockUnits1);
			} else {
				BeanUtils.copyProperties(companyStockCategoriesDTO, companyStockCategories);

				companyStockCategories.setCompanyStockGroup(companyStockGroup1);

				companyStockCategories.setCompanyStockUnits(companyStockUnits1);

				companyStockCategories = companyStockCategoriesRepository.save(companyStockCategories);
			}

			BeanUtils.copyProperties(companyStockCategories, companyStockCategoriesDTO);

			companyStockCategoriesDTOs.add(companyStockCategoriesDTO);
		}

		stockCategoriesDTO2.setCompanyStockCategoriesDTOs(companyStockCategoriesDTOs);

		return stockCategoriesDTO2;

	}

	public static CompanyStockGroup findCompanyStockGroup(CompanyInfo companyInfo,
			CompanyStockCategoriesDTO companyStockCategoriesDTO) {
		CompanyStockGroup companyStockGroup1 = new CompanyStockGroup();
		for (CompanyStockGroup companyStockGroup : companyInfo.getCompanyStockGroupList()) {
			if (companyStockCategoriesDTO.getStockGroupId().equals(companyStockGroup.getStockGroupId())) {
				companyStockGroup1 = companyStockGroup;
				break;
			}
		}

		if (!companyStockCategoriesDTO.getStockGroupId().equals(companyStockGroup1.getStockGroupId())) {
			throw new StockGroupNotFound("stock group name not found");
		}
		return companyStockGroup1;
	}

	public static CompanyStockUnits findCompanyStockUnit(CompanyInfo companyInfo,
			CompanyStockCategoriesDTO companyStockCategoriesDTO) {
		CompanyStockUnits companyStockUnit1 = new CompanyStockUnits();
		for (CompanyStockUnits companyStockUnit : companyInfo.getCompanyStockUnitsList()) {
			if (companyStockCategoriesDTO.getUnitId().equals(companyStockUnit.getUnitId())) {
				companyStockUnit1 = companyStockUnit;
				break;
			}
		}

		if (!companyStockCategoriesDTO.getUnitId().equals(companyStockUnit1.getUnitId())) {
			throw new StockUnitNotFoundException("stock unit name not found");
		}
		return companyStockUnit1;
	}



	@Override
	public List<CompanyStockCategoriesDTO> getListStockCategory(Long companyId) {
		Optional<CompanyInfo> companyInfo = companyInfoRepository.findById(companyId);

		if (companyInfo.isEmpty()) {
			throw new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND);
		}

		List<CompanyStockCategories>  listOfCompanyStockCategories = companyStockCategoriesRepository.findByCompanyStockGroupCompanyInfoCompanyId(companyId);

		List<CompanyStockCategoriesDTO> companyStockCategoriesDTOs = new ArrayList<>();
		
			for (CompanyStockCategories companyStockCategories : listOfCompanyStockCategories) {
				CompanyStockCategoriesDTO companyStockCategoriesDTO = new CompanyStockCategoriesDTO();
				BeanUtils.copyProperties(companyStockCategories, companyStockCategoriesDTO);
				BeanUtils.copyProperties(companyStockCategories.getCompanyStockGroup(), companyStockCategoriesDTO);
				BeanUtils.copyProperties(companyStockCategories.getCompanyStockUnits(), companyStockCategoriesDTO);
				companyStockCategoriesDTO.setIsSubmited(companyInfo.get().getIsSubmited());
				companyStockCategoriesDTOs.add(companyStockCategoriesDTO);
		}
		

		return companyStockCategoriesDTOs;
	}

}
