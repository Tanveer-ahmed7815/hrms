package com.te.flinko.service.admindept;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.constants.admindept.AdminDeptConstants;
import com.te.flinko.dto.admindept.CompanyHardwareItemsDTO;
import com.te.flinko.dto.admindept.EmployeePersonalInfoDTO;
import com.te.flinko.dto.admindept.HistoryDTO;
import com.te.flinko.dto.admindept.IssuesDTO;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.admindept.SubjectDTO;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroy;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroyDto;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.account.PurchaseOrderItems;
import com.te.flinko.entity.account.SalesOrderItems;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.helpandsupport.mongo.CompanyItTickets;
import com.te.flinko.entity.it.CompanyHardwareItems;
import com.te.flinko.exception.CompanyIdNotFoundException;
import com.te.flinko.exception.admin.CompanyNotExistException;
import com.te.flinko.exception.admin.EmployeeNotFoundException;
import com.te.flinko.exception.admin.NoEmployeeOfficialInfoException;
import com.te.flinko.exception.admindept.DuplicateProductNameException;
import com.te.flinko.exception.admindept.EmployeeListNotFoundException;
import com.te.flinko.exception.admindept.InOutNotSelectedException;
import com.te.flinko.exception.admindept.NoHardwarePresentException;
import com.te.flinko.exception.admindept.PurchaseIdNotPresentException;
import com.te.flinko.exception.admindept.PurchaseOrderItemNotFoundException;
import com.te.flinko.exception.admindept.SalesIdNotPresentException;
import com.te.flinko.exception.admindept.SalesOrderItemNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admindept.CompanyHardwareItemsRepository;
import com.te.flinko.repository.admindept.CompanyItTicketsRepository;
import com.te.flinko.repository.admindept.PurchaseOrderItemsRepository;
import com.te.flinko.repository.admindept.SalesOrderItemsRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Brunda
 *
 *
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyHardwareItemsServiceImpl implements CompanyHardwareItemsService {

	private final CompanyHardwareItemsRepository companyHardwareItemsRepository;

	private final CompanyInfoRepository companyInfoRepository;

	@Autowired
	private PurchaseOrderItemsRepository purchaseOrderItemsRepository;

	@Autowired
	private CompanyItTicketsRepository companyItTicketsRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private SalesOrderItemsRepository salesOrderItemsRepository;

	private static final String WORKING = "Working";
	private static final String NOT_WORKING = "Not Working";

	@Override
	public List<ProductNameDTO> getAllProducts(Long companyId, Long subjectId, String inOut) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotExistException(AdminConstants.COMPANY_NOT_EXIST));

		if (inOut.equalsIgnoreCase("In")) {

			List<PurchaseOrderItems> purchaseOrder = companyInfo.getCompanyPurchaseOrderList().stream()
					.filter(x -> x.getPurchaseOrderId().equals(subjectId))
					.flatMap(y -> y.getPurchaseOrderItemsList().stream()).collect(Collectors.toList());

			return purchaseOrder.stream().filter(
					x -> !(x.getProductName().equalsIgnoreCase("PC") || x.getProductName().equalsIgnoreCase("Laptop")))
					.map(x -> {
						ProductNameDTO productNameDTO = new ProductNameDTO();
						productNameDTO.setProductId(x.getPurchaseItemId());
						productNameDTO.setProductName(x.getProductName());
						return productNameDTO;
					}).collect(Collectors.toList());

		} else if (inOut.equalsIgnoreCase("Out")) {

			List<SalesOrderItems> salesOrder = companyInfo.getCompanySalesOrderList().stream()
					.filter(x -> x.getSalesOrderId().equals(subjectId))
					.flatMap(y -> y.getSalesOrderItemsList().stream()).collect(Collectors.toList());
			return salesOrder.stream().filter(
					x -> !(x.getProductName().equalsIgnoreCase("PC") || x.getProductName().equalsIgnoreCase("Laptop")))
					.map(x -> {
						ProductNameDTO productNameDTO = new ProductNameDTO();
						productNameDTO.setProductId(x.getSaleItemId());
						productNameDTO.setProductName(x.getProductName());
						return productNameDTO;
					}).collect(Collectors.toList());

		} else {
			throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTED);
		}

	}

	@Override
	public List<CompanyHardwareItemsDTO> getAllCompanyHardwareDetails(Long companyId) {

		Optional<CompanyInfo> companyInfo = companyInfoRepository.findById(companyId);

		if (companyInfo.isEmpty()) {
			throw new CompanyNotExistException("company not found");
		}

		List<CompanyHardwareItems> companyHardwareItemsList1 = companyHardwareItemsRepository
				.findByCompanyInfoCompanyId(companyId);

		List<CompanyHardwareItems> companyHardwareItemsList = companyHardwareItemsList1.stream()
				.sorted(Comparator.comparing(CompanyHardwareItems::getCreatedDate).reversed())
				.collect(Collectors.toList());

		return getAllDetailsOfHardware(companyHardwareItemsList);

	}

	List<CompanyHardwareItemsDTO> getAllDetailsOfHardware(List<CompanyHardwareItems> companyHardwareItemsList) {
		return companyHardwareItemsList.stream().map(x -> {

			CompanyHardwareItemsDTO companyHardwareItemsDTO = new CompanyHardwareItemsDTO();

			BeanUtils.copyProperties(x, companyHardwareItemsDTO);

			if (x.getCompanyPurchaseOrder() != null || x.getCompanySalesOrder() != null) {

				if (x.getInOut().equalsIgnoreCase("IN")) {

					getPurchaseDetailsOfHardware(companyHardwareItemsDTO, x);

				} else if (x.getInOut().equalsIgnoreCase("OUT")) {
					getSalesDetailsOfHardware(companyHardwareItemsDTO, x);
				}
			} else {
				companyHardwareItemsDTO.setProductName(x.getProductName());
			}
			companyHardwareItemsDTO.setStatus(Boolean.TRUE.equals(x.getIsWorking()) ? WORKING : NOT_WORKING);
			return companyHardwareItemsDTO;
		}).collect(Collectors.toList());
	}

	CompanyHardwareItemsDTO getSalesDetailsOfHardware(CompanyHardwareItemsDTO companyHardwareItemsDTO,
			CompanyHardwareItems x) {
		companyHardwareItemsDTO.setSalesOrderId(x.getCompanySalesOrder().getSalesOrderId());
		companyHardwareItemsDTO.setSubject(x.getCompanySalesOrder().getSubject());
		companyHardwareItemsDTO.setStatus(Boolean.TRUE.equals(x.getIsWorking()) ? WORKING : NOT_WORKING);
		List<SalesOrderItems> findByCompanySalesOrderSalesOrderId = salesOrderItemsRepository
				.findByCompanySalesOrderSalesOrderId(x.getCompanySalesOrder().getSalesOrderId());
		if (findByCompanySalesOrderSalesOrderId == null) {
			throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);
		}
		SalesOrderItems findByProductNameAndCompanySalesOrderSalesOrderId = salesOrderItemsRepository
				.findByProductNameAndCompanySalesOrderSalesOrderId(x.getProductName(),
						x.getCompanySalesOrder().getSalesOrderId());
		if (findByProductNameAndCompanySalesOrderSalesOrderId == null) {
			throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);
		}
		companyHardwareItemsDTO.setProductId(findByProductNameAndCompanySalesOrderSalesOrderId.getSaleItemId());

		return companyHardwareItemsDTO;
	}

	CompanyHardwareItemsDTO getPurchaseDetailsOfHardware(CompanyHardwareItemsDTO companyHardwareItemsDTO,
			CompanyHardwareItems x) {
		companyHardwareItemsDTO.setPurchaseOrderId(x.getCompanyPurchaseOrder().getPurchaseOrderId());

		companyHardwareItemsDTO.setSubject(x.getCompanyPurchaseOrder().getSubject());

		companyHardwareItemsDTO.setStatus(Boolean.TRUE.equals(x.getIsWorking()) ? WORKING : NOT_WORKING);

		List<PurchaseOrderItems> findByCompanyPurchaseOrderPurchaseOrderId = purchaseOrderItemsRepository

				.findByCompanyPurchaseOrderPurchaseOrderId(

						x.getCompanyPurchaseOrder().getPurchaseOrderId());

		if (findByCompanyPurchaseOrderPurchaseOrderId == null) {

			throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);

		}
		companyHardwareItemsDTO

				.setProductId(

						purchaseOrderItemsRepository

								.findByProductNameAndCompanyPurchaseOrderPurchaseOrderId(x.getProductName(),

										x.getCompanyPurchaseOrder().getPurchaseOrderId())

								.getPurchaseItemId());

		return companyHardwareItemsDTO;
	}

	@Override
	public CompanyHardwareItemsDTO getCompanyHardwareDetails(Long companyId, String indentificationNumber) {

		CompanyHardwareItems hardwareindentificationNumber = companyHardwareItemsRepository
				.findByIndentificationNumberAndCompanyInfoCompanyId(indentificationNumber, companyId)
				.orElseThrow(() -> new NoHardwarePresentException(AdminDeptConstants.NO_HARDWARE_PRESENT));

		CompanyHardwareItemsDTO companyHardwareItemsDTO2 = new CompanyHardwareItemsDTO();
		BeanUtils.copyProperties(hardwareindentificationNumber, companyHardwareItemsDTO2);

		EmployeePersonalInfo employeePersonalInfo = hardwareindentificationNumber.getEmployeePersonalInfo();
		if (employeePersonalInfo != null) {
			EmployeePersonalInfoDTO employeePersonalInfoDTO = new EmployeePersonalInfoDTO();
			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			if (employeeOfficialInfo == null) {
				throw new NoEmployeeOfficialInfoException("employee official details not found");
			}
			employeePersonalInfoDTO.setEmployeeId(employeeOfficialInfo.getEmployeeId());
			employeePersonalInfoDTO
					.setFullName(employeePersonalInfo.getFirstName() + employeePersonalInfo.getLastName());
			companyHardwareItemsDTO2.setEmployeePersonalInfoDto(employeePersonalInfoDTO);

		}

		CompanyHardwareItems x = hardwareindentificationNumber;

		if (x.getCompanyPurchaseOrder() != null || x.getCompanySalesOrder() != null) {

			if (x.getInOut().equalsIgnoreCase("IN")) {
				getPurchaseDetailsOfHardware(companyHardwareItemsDTO2, x);
			} else if (x.getInOut().equalsIgnoreCase("OUT")) {
				getSalesDetailsOfHardware(companyHardwareItemsDTO2, x);
			}
		} else {
			companyHardwareItemsDTO2.setProductName(x.getProductName());
		}

		Map<String, Map<String, String>> chiHistory = hardwareindentificationNumber.getChiHistory();

		if (chiHistory == null) {
			companyHardwareItemsDTO2.setHistoryDTOs(null);
		} else {

			companyHardwareItemsDTO2.setHistoryDTOs(getAllHistory(chiHistory));
		}
		companyHardwareItemsDTO2
				.setStatus(Boolean.TRUE.equals(companyHardwareItemsDTO2.getIsWorking()) ? WORKING : NOT_WORKING);

		List<CompanyItTickets> findByCompanyId = companyItTicketsRepository.findByCompanyIdAndIdentificationNumber(
				companyId, companyHardwareItemsDTO2.getIndentificationNumber());

		if (findByCompanyId.isEmpty()) {
			companyHardwareItemsDTO2.setIssuesDTOs(null);
		} else {
			companyHardwareItemsDTO2.setIssuesDTOs(getAllIssues(findByCompanyId, companyId));
		}

		return companyHardwareItemsDTO2;
	}

	List<HistoryDTO> getAllHistory(Map<String, Map<String, String>> chiHistory) {
		List<HistoryDTO> historyDTOs2 = new ArrayList<>();

		Set<Entry<String, Map<String, String>>> entrySet = chiHistory.entrySet();

		for (Entry<String, Map<String, String>> entry : entrySet) {

			HistoryDTO historyDTO = new HistoryDTO();

			Set<Entry<String, String>> entrySet2 = entry.getValue().entrySet();
			for (Entry<String, String> entry2 : entrySet2) {
				historyDTO.setDeAllocatedDate(entry2.getKey());
				EmployeePersonalInfo employeePersonalInfoHistory = employeePersonalInfoRepository
						.findById(Long.parseLong(entry2.getValue()))
						.orElseThrow(() -> new EmployeeNotFoundException("employee not found"));

				EmployeeOfficialInfo employeeOfficialInfo2 = employeePersonalInfoHistory.getEmployeeOfficialInfo();

				if (employeeOfficialInfo2 == null) {
					throw new NoEmployeeOfficialInfoException("employee official details not found");
				}

				historyDTO.setEmployeeId(employeeOfficialInfo2.getEmployeeId());

				historyDTO.setName(
						employeePersonalInfoHistory.getFirstName() + employeePersonalInfoHistory.getLastName());
			}
			historyDTO.setAllocatedDate(entry.getKey());

			historyDTOs2.add(historyDTO);
		}
		return historyDTOs2;
	}

	List<IssuesDTO> getAllIssues(List<CompanyItTickets> findByCompanyId, Long companyId) {
		List<IssuesDTO> issuesDTOs = new ArrayList<>();
		for (CompanyItTickets companyItTickets : findByCompanyId) {
			IssuesDTO issuesDTO = new IssuesDTO();
			List<TicketHistroyDto> ticketHistroyDtos = new ArrayList<>();
			List<TicketHistroy> ticketHistroys = companyItTickets.getTicketHistroys();
			for (TicketHistroy ticketHistroy : ticketHistroys) {
				TicketHistroyDto ticketHistroyDto = new TicketHistroyDto();
				BeanUtils.copyProperties(ticketHistroy, ticketHistroyDto);

				Long by = ticketHistroyDto.getBy();
				List<EmployeePersonalInfo> findByCompanyInfoCompanyId = employeePersonalInfoRepository
						.findByCompanyInfoCompanyId(companyId);
				Optional<EmployeePersonalInfo> findFirst = findByCompanyInfoCompanyId.stream()
						.filter(x1 -> x1.getEmployeeInfoId().equals(by)).findFirst();
				if (findFirst.isPresent()) {
					String name = "";
					String firstName = findFirst.get().getFirstName();
					String lastName = findFirst.get().getLastName();
					name = firstName + lastName;
					issuesDTO.setResolvedBy(name);
				} else {
					issuesDTO.setResolvedBy(null);
				}
				ticketHistroyDtos.add(ticketHistroyDto);
			}

			BeanUtils.copyProperties(companyItTickets, issuesDTO);
			issuesDTO.setTicketHistroys(ticketHistroyDtos);
			issuesDTOs.add(issuesDTO);

		}
		return issuesDTOs;

	}

	@Override
	public List<SubjectDTO> getAllSubjects(Long companyId, String inOut, Integer status) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotExistException(AdminConstants.COMPANY_NOT_EXIST));

		if (inOut.equalsIgnoreCase("in")) {

			return companyInfo.getCompanyPurchaseOrderList().stream()
					.filter(y -> Objects.equals(y.getIsHardware(), status)).map(x -> {
						SubjectDTO subjectDTO = new SubjectDTO();
						subjectDTO.setSubjectId(x.getPurchaseOrderId());
						subjectDTO.setSubject(x.getSubject());
						return subjectDTO;
					}).collect(Collectors.toList());

		} else if (inOut.equalsIgnoreCase("out")) {

			return companyInfo.getCompanySalesOrderList().stream()
					.filter(y -> Objects.equals(y.getIsHardware(), status)).map(x -> {
						SubjectDTO subjectDTO = new SubjectDTO();
						subjectDTO.setSubjectId(x.getSalesOrderId());
						subjectDTO.setSubject(x.getSubject());
						return subjectDTO;
					}).collect(Collectors.toList());

		} else {
			throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTED);
		}
	}

//	Post API

	@Override
	public CompanyHardwareItemsDTO addHardware(CompanyHardwareItemsDTO companyHardwareItemsDTO, Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		CompanyHardwareItems companyHardwareItems = new CompanyHardwareItems();

		Optional<CompanyHardwareItems> companyHardwareItem = companyHardwareItemsRepository
				.findByIndentificationNumberAndCompanyInfoCompanyId(companyHardwareItemsDTO.getIndentificationNumber(),
						companyId);

		if (companyHardwareItem.isPresent()) {
			throw new NoHardwarePresentException("hardware items already present");
		}

		if (companyHardwareItemsDTO.getSubjectId() != null) {
			if (companyHardwareItemsDTO.getInOut().equalsIgnoreCase("IN")) {

				CompanyPurchaseOrder purchaseOrder = findCompanyPurchaseOrder(companyHardwareItemsDTO, companyInfo);

				createProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(),
						purchaseOrder, null, companyHardwareItemsDTO.getIndentificationNumber());

				companyHardwareItems.setCompanyPurchaseOrder(purchaseOrder);

			} else if (companyHardwareItemsDTO.getInOut().equalsIgnoreCase("OUT")) {
				CompanySalesOrder salesOrder = findCompanySalesOrder(companyHardwareItemsDTO, companyInfo);

				createProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(), null,
						salesOrder, companyHardwareItemsDTO.getIndentificationNumber());

				companyHardwareItems.setCompanySalesOrder(salesOrder);

			} else {
				throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
			}
		} else {
			createProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(), null, null,
					companyHardwareItemsDTO.getIndentificationNumber());
		}

		companyHardwareItemsDTO.setStatus(WORKING);
		companyHardwareItemsDTO.setIsWorking(Boolean.TRUE);
		BeanUtils.copyProperties(companyHardwareItemsDTO, companyHardwareItems);
		companyHardwareItems.setCompanyInfo(companyInfo);
		companyHardwareItemsRepository.save(companyHardwareItems);
		return null;

	}

//	Put API
	@Transactional
	@Override
	public String updateHardware(CompanyHardwareItemsDTO companyHardwareItemsDTO, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		CompanyHardwareItems companyHardwareItems = companyHardwareItemsRepository
				.findByIndentificationNumberAndCompanyInfoCompanyId(companyHardwareItemsDTO.getIndentificationNumber(),
						companyId)
				.orElseThrow(() -> new NoHardwarePresentException(AdminDeptConstants.NO_HARDWARE_PRESENT));

		if (companyHardwareItemsDTO.getSubjectId() != null) {
			if (companyHardwareItemsDTO.getInOut().equalsIgnoreCase("IN")) {

				CompanyPurchaseOrder findCompanyPurchaseOrder = findCompanyPurchaseOrder(companyHardwareItemsDTO,
						companyInfo);

				editProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(),
						findCompanyPurchaseOrder, null, companyHardwareItemsDTO.getIndentificationNumber());

				companyHardwareItems.setCompanyPurchaseOrder(findCompanyPurchaseOrder);

				companyHardwareItems.setCompanySalesOrder(null);

			} else if (companyHardwareItemsDTO.getInOut().equalsIgnoreCase("OUT")) {

				CompanySalesOrder findCompanySalesOrder = findCompanySalesOrder(companyHardwareItemsDTO, companyInfo);

				editProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(), null,
						findCompanySalesOrder, companyHardwareItemsDTO.getIndentificationNumber());

				companyHardwareItems.setCompanySalesOrder(findCompanySalesOrder);

				companyHardwareItems.setCompanyPurchaseOrder(null);
			} else {
				throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
			}
		} else {
			editProductName(companyHardwareItemsRepository, companyHardwareItemsDTO.getProductName(), null, null,
					companyHardwareItemsDTO.getIndentificationNumber());
			companyHardwareItems.setCompanySalesOrder(null);
			companyHardwareItems.setCompanyPurchaseOrder(null);
		}

		companyHardwareItemsDTO.setStatus(WORKING);
		companyHardwareItemsDTO.setIsWorking(Boolean.TRUE);

		companyHardwareItems.setInOut(companyHardwareItemsDTO.getInOut());
		companyHardwareItems.setProductName(companyHardwareItemsDTO.getProductName());
		companyHardwareItems.setIndentificationNumber(companyHardwareItemsDTO.getIndentificationNumber());
		companyHardwareItems.setAmount(companyHardwareItemsDTO.getAmount());

		companyHardwareItems.setCompanyInfo(companyInfo);
		return null;
	}

	@Override
	@Transactional
	public String markNotWorking(Long companyId, CompanyHardwareItemsDTO companyHardwareItemsDTO) {
		log.info("Mark Company Hardware as working or not working");
		CompanyHardwareItems companyHardwareItems = companyHardwareItemsRepository
				.findByIndentificationNumberAndCompanyInfoCompanyId(companyHardwareItemsDTO.getIndentificationNumber(),
						companyId)
				.orElseThrow(() -> new NoHardwarePresentException(AdminDeptConstants.NO_HARDWARE_PRESENT));

		companyHardwareItems.setIsWorking(companyHardwareItemsDTO.getIsWorking());
		if (Boolean.TRUE.equals(companyHardwareItemsDTO.getIsWorking())) {
			companyHardwareItems.setIsWorking(companyHardwareItemsDTO.getIsWorking());
			log.info("Company Hardware item marked as working");
			return "Marked as Working";
		} else {
			if (companyHardwareItems.getEmployeePersonalInfo() != null) {
				updateHardwareEmployeeInfo(companyId, companyHardwareItemsDTO);

			}
			companyHardwareItems.setIsWorking(companyHardwareItemsDTO.getIsWorking());
			log.info("Company Hardware item marked as not working");
			return "Marked as not Working";
		}
	}

	// update api
	@Transactional
	@Override
	public String updateHardwareEmployeeInfo(Long companyId, CompanyHardwareItemsDTO companyHardwareItemsDTO) {

		CompanyHardwareItems companyHardwareItems = companyHardwareItemsRepository
				.findByIndentificationNumberAndCompanyInfoCompanyId(companyHardwareItemsDTO.getIndentificationNumber(),
						companyId)
				.orElseThrow(() -> new NoHardwarePresentException(AdminDeptConstants.NO_HARDWARE_PRESENT));

		// EmployeeInfo

		String returnMessage = "";

		if (companyHardwareItemsDTO.getEmployeePersonalInfoDto() != null
				&& companyHardwareItems.getAllocatedDate() == null
				&& companyHardwareItems.getEmployeePersonalInfo() == null) {

			if (Boolean.FALSE.equals(companyHardwareItems.getIsWorking())) {
				throw new CompanyIdNotFoundException("Product is not working , can't be allocated");
			}

			Optional<CompanyHardwareItems> companyHardwareItemsEmployee = companyHardwareItemsRepository
					.findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndIndentificationNumberAndEmployeePersonalInfoEmployeeInfoId(
							companyHardwareItems.getProductName(), companyHardwareItems.getCompanyPurchaseOrder(),
							companyHardwareItems.getCompanySalesOrder(),
							companyHardwareItemsDTO.getIndentificationNumber(),
							companyHardwareItemsDTO.getEmployeePersonalInfoDto().getEmployeeInfoId());

			if (!companyHardwareItemsEmployee.isEmpty()) {
				throw new NoHardwarePresentException("Employee is already allocated");
			}

			Long employeeInfoId2 = companyHardwareItemsDTO.getEmployeePersonalInfoDto().getEmployeeInfoId();
			List<EmployeePersonalInfo> listEmployee = employeePersonalInfoRepository
					.findByCompanyInfoCompanyId(companyId);
			if (listEmployee.isEmpty()) {
				throw new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND);
			}
			Optional<EmployeePersonalInfo> employeeOptional = listEmployee.stream()
					.filter(x -> x.getEmployeeInfoId().equals(employeeInfoId2)).findFirst();
			if (employeeOptional.isEmpty()) {
				throw new EmployeeListNotFoundException(AdminDeptConstants.EMPLOYEE_PERSONAL_INFO_NOT_FOUND);
			}

			if (employeeOptional.get().getIsActive() == null
					|| Boolean.FALSE.equals(employeeOptional.get().getIsActive())) {
				throw new EmployeeListNotFoundException("Employee Is Inactive");
			}

			EmployeePersonalInfoDTO dto = new EmployeePersonalInfoDTO();
			BeanUtils.copyProperties(employeeOptional.get(), dto);
			companyHardwareItems.setEmployeePersonalInfo(employeeOptional.get());

			companyHardwareItemsDTO.setEmployeePersonalInfoDto(dto);
			Map<String, Map<String, String>> historySet = companyHardwareItems.getChiHistory();

			Map<String, String> historyValue = new LinkedHashMap<>();

			historyValue.put(null, companyHardwareItems.getEmployeePersonalInfo().getEmployeeInfoId().toString());

			log.info("check all key" + historySet.keySet().toString());

			historySet.put(LocalDateTime.now().toString(), historyValue);

			log.info("check all key" + historySet.keySet().toString());

			companyHardwareItems.setAllocatedDate(LocalDate.now());

			returnMessage = "Employee Allocated";

		} else if (companyHardwareItemsDTO.getEmployeePersonalInfoDto() == null
				&& companyHardwareItems.getAllocatedDate() != null
				&& companyHardwareItems.getEmployeePersonalInfo() != null) {

			Map<String, Map<String, String>> historySet = companyHardwareItems.getChiHistory();

			Set<String> keySet = historySet.keySet();

			List<String> listKeys = new ArrayList<>(keySet);

			Map<String, String> historyValue = new LinkedHashMap<>();

			historyValue.put(LocalDateTime.now().toString(),
					companyHardwareItems.getEmployeePersonalInfo().getEmployeeInfoId().toString());

			historySet.put(listKeys.get(listKeys.size() - 1), historyValue);

			companyHardwareItems.setEmployeePersonalInfo(null);

			companyHardwareItems.setAllocatedDate(null);

			returnMessage = "Employee Deallocated";
		}

		return returnMessage;
	}

	public static void editProductName(CompanyHardwareItemsRepository companyHardwareItemsRepository,
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			String indentificationNumber) {

		List<CompanyHardwareItems> hardwareDetails = companyHardwareItemsRepository
				.findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndIndentificationNumber(productName,
						companyPurchaseOrder, companySalesOrder, indentificationNumber);

		if (!hardwareDetails.isEmpty() && (hardwareDetails.size() > 1
				|| !hardwareDetails.get(0).getIndentificationNumber().equals(indentificationNumber))) {
			throw new DuplicateProductNameException(AdminDeptConstants.DUPLICATE_PRODUCT_NAME);

		}

	}

	private CompanyPurchaseOrder findCompanyPurchaseOrder(CompanyHardwareItemsDTO companyHardwareItemsDTO,
			CompanyInfo companyInfo) {

		Optional<CompanyPurchaseOrder> purchase = companyInfo.getCompanyPurchaseOrderList().stream()
				.filter(x -> x.getPurchaseOrderId().equals(companyHardwareItemsDTO.getSubjectId())).findFirst();

		if (purchase.isEmpty()) {

			throw new PurchaseIdNotPresentException(AdminDeptConstants.NO_PURCHASE_ID_PRESENT);

		}

		Optional<PurchaseOrderItems> purchaseOrderItem = purchase.get().getPurchaseOrderItemsList().stream()
				.filter(x -> x.getPurchaseItemId().equals(companyHardwareItemsDTO.getProductId())).findFirst();

		if (purchaseOrderItem.isEmpty()) {

			throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);
		}

		if (!purchaseOrderItem.get().getProductName().equals(companyHardwareItemsDTO.getProductName())) {

			throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);

		}

		return purchase.get();

	}

	private CompanySalesOrder findCompanySalesOrder(CompanyHardwareItemsDTO companyHardwareItemsDTO,
			CompanyInfo companyInfo) {

		Optional<CompanySalesOrder> sales = companyInfo.getCompanySalesOrderList().stream()
				.filter(x -> x.getSalesOrderId().equals(companyHardwareItemsDTO.getSubjectId())).findFirst();

		if (sales.isEmpty()) {

			throw new SalesIdNotPresentException(AdminDeptConstants.NO_SALES_ID_PRESENT);

		}

		Optional<SalesOrderItems> salesOrderItem = sales.get().getSalesOrderItemsList().stream()
				.filter(x -> x.getSaleItemId().equals(companyHardwareItemsDTO.getProductId())).findFirst();

		if (salesOrderItem.isEmpty()) {

			throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);
		}

		if (!salesOrderItem.get().getProductName().equals(companyHardwareItemsDTO.getProductName())) {

			throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);

		}

		return sales.get();
	}

	public static void createProductName(CompanyHardwareItemsRepository companyHardwareItemsRepository,
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			String indentificationNumber) {

		List<CompanyHardwareItems> hardwareItemsDetails = companyHardwareItemsRepository
				.findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndIndentificationNumber(productName,
						companyPurchaseOrder, companySalesOrder, indentificationNumber);

		if (!hardwareItemsDetails.isEmpty()) {

			throw new DuplicateProductNameException(AdminDeptConstants.DUPLICATE_PRODUCT_NAME);

		}

	}

	@Override
	public List<String> getAllIndentification() {
		List<CompanyHardwareItems> findAllById = companyHardwareItemsRepository.findAll();
		if (findAllById.isEmpty()) {
			return new ArrayList<>();
		}

		return findAllById.stream().map(CompanyHardwareItems::getIndentificationNumber).collect(Collectors.toList());
	}

}
