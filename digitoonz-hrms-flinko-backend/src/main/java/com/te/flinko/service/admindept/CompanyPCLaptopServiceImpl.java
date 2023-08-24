package com.te.flinko.service.admindept;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.flinko.constants.admindept.AdminDeptConstants;
import com.te.flinko.dto.admindept.CompanyPCLaptopDTO;
import com.te.flinko.dto.admindept.EmployeePersonalInfoDTO;
import com.te.flinko.dto.admindept.HistoryDTO;
import com.te.flinko.dto.admindept.IssuesDTO;
import com.te.flinko.dto.admindept.PcLaptopSoftwareDetailsDTO;
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
import com.te.flinko.entity.it.CompanyPcLaptopDetails;
import com.te.flinko.exception.CompanyIdNotFoundException;
import com.te.flinko.exception.admin.EmployeeNotFoundException;
import com.te.flinko.exception.admin.NoEmployeeOfficialInfoException;
import com.te.flinko.exception.admindept.CompanyPCLaptopDetailsAlreadyPresentException;
import com.te.flinko.exception.admindept.CompanyPCLaptopDetailsNotFoundException;
import com.te.flinko.exception.admindept.EmployeeListNotFoundException;
import com.te.flinko.exception.admindept.InOutNotSelectedException;
import com.te.flinko.exception.admindept.NoSerialNumberListException;
import com.te.flinko.exception.admindept.PurchaseOrderDoesNotExistsException;
import com.te.flinko.exception.admindept.PurchaseOrderItemNotFoundException;
import com.te.flinko.exception.admindept.SalesOrderDoesNotExistsException;
import com.te.flinko.exception.admindept.SalesOrderItemNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admindept.CompanyItTicketsRepository;
import com.te.flinko.repository.admindept.CompanyPCLaptopRepository;
import com.te.flinko.repository.admindept.CompanyPurchaseOrderRepository;
import com.te.flinko.repository.admindept.CompanySalesOrderRepository;
import com.te.flinko.repository.admindept.PurchaseOrderItemsRepository;
import com.te.flinko.repository.admindept.SalesOrderItemsRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Brunda
 *
 * 
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyPCLaptopServiceImpl implements CompanyPCLaptopService {

	@Autowired
	CompanyInfoRepository companyInfoRepository;

	@Autowired
	CompanyPurchaseOrderRepository companyPurchaseOrderRepository;

	@Autowired
	CompanySalesOrderRepository companySalesOrderRepository;

	@Autowired
	CompanyPCLaptopRepository companyPCLaptopRepository;

	@Autowired
	PurchaseOrderItemsRepository purchaseOrderItemsRepository;

	@Autowired
	SalesOrderItemsRepository salesOrderItemsRepository;

	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	CompanyItTicketsRepository companyItTicketsRepository;

	private static final String AVAILABLE = "Available";
	private static final String ALLOCATED = "Allocated";
	private static final String WORKING = "Working";
	private static final String NOT_WORKING = "Not Working";

	public static CompanyPurchaseOrder findCompanyPurchaseOrder(CompanyPCLaptopDTO companyPCLaptopDTO,
			CompanyInfo companyInfo) {

		Optional<CompanyPurchaseOrder> purchase = companyInfo.getCompanyPurchaseOrderList().stream()
				.filter(x -> x.getPurchaseOrderId().equals(companyPCLaptopDTO.getSubjectId())).findFirst();

		if (purchase.isEmpty()) {
			throw new PurchaseOrderDoesNotExistsException(AdminDeptConstants.PURCHASE_ORDER_DOES_NOT_EXISTS);
		}

		Optional<PurchaseOrderItems> purchaseOrderItem = purchase.get().getPurchaseOrderItemsList().stream()
				.filter(x -> x.getPurchaseItemId().equals(companyPCLaptopDTO.getProductItemId())).findFirst();

		if (purchaseOrderItem.isEmpty()) {
			throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);
		}

		return purchase.get();
	}

	public static CompanySalesOrder findCompanySalesOrder(CompanyPCLaptopDTO companyPCLaptopDTO,
			CompanyInfo companyInfo) {
		Optional<CompanySalesOrder> sales = companyInfo.getCompanySalesOrderList().stream()
				.filter(x -> x.getSalesOrderId().equals(companyPCLaptopDTO.getSubjectId())).findFirst();

		if (sales.isEmpty()) {
			throw new SalesOrderDoesNotExistsException(AdminDeptConstants.SALES_ORDER_DOES_NOT_EXISTS);
		}

		Optional<SalesOrderItems> salesOrderItem = sales.get().getSalesOrderItemsList().stream()
				.filter(x -> x.getSaleItemId().equals(companyPCLaptopDTO.getProductItemId())).findFirst();

		if (salesOrderItem.isEmpty()) {
			throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);
		}

		return sales.get();
	}

	//post api
	@Override
	@Transactional
	public CompanyPCLaptopDTO createCompanyPCLaptop(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Create Company PC/Laptop");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		CompanyPcLaptopDetails companyPcLaptopDetails = new CompanyPcLaptopDetails();

		Optional<CompanyPcLaptopDetails> dbSerialNumber = companyInfo.getCompanyPcLaptopDetailsList().stream()
				.filter(x -> x.getSerialNumber().equalsIgnoreCase(companyPCLaptopDTO.getSerialNumber())).findFirst();

		if (dbSerialNumber.isPresent()) {
			throw new CompanyPCLaptopDetailsAlreadyPresentException(
					AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_ALREADY_PRESENT);
		}

		if (companyPCLaptopDTO.getSubjectId() != null) {
			if (companyPCLaptopDTO.getInOut().equalsIgnoreCase("IN")) {

				CompanyPurchaseOrder purchaseOrder = findCompanyPurchaseOrder(companyPCLaptopDTO, companyInfo);

				companyPcLaptopDetails.setCompanyPurchaseOrder(purchaseOrder);

			} else if (companyPCLaptopDTO.getInOut().equalsIgnoreCase("OUT")) {
				CompanySalesOrder salesOrder = findCompanySalesOrder(companyPCLaptopDTO, companyInfo);

				companyPcLaptopDetails.setCompanySalesOrder(salesOrder);

			} else {
				throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
			}
		} else {
			companyPcLaptopDetails.setProductName(companyPCLaptopDTO.getProductName());
		}

		companyPCLaptopDTO.setStatus(AVAILABLE);
		companyPCLaptopDTO.setCpldIsWorking(Boolean.TRUE);
		BeanUtils.copyProperties(companyPCLaptopDTO, companyPcLaptopDetails);
		companyPcLaptopDetails.setCompanyInfo(companyInfo);
		companyPCLaptopRepository.save(companyPcLaptopDetails);
		log.info("Company PC/Laptop is created successfully");
		return companyPCLaptopDTO;
	}
	
	//put api
	@Transactional
	@Override
	public CompanyPCLaptopDTO updateCompanyPCLaptop(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Update Company PC/Laptop");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		CompanyPcLaptopDetails companyPcLaptopDetails = companyPCLaptopRepository
				.findBySerialNumberAndCompanyInfoCompanyId(companyPCLaptopDTO.getSerialNumber(), companyId)
				.orElseThrow(() -> new CompanyPCLaptopDetailsNotFoundException(
						AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_NOT_FOUND));

		if (companyPCLaptopDTO.getSubjectId() != null) {
			if (companyPCLaptopDTO.getInOut().equalsIgnoreCase("IN")) {

				CompanyPurchaseOrder findCompanyPurchaseOrder = findCompanyPurchaseOrder(companyPCLaptopDTO,
						companyInfo);

				companyPcLaptopDetails.setCompanyPurchaseOrder(findCompanyPurchaseOrder);
				companyPcLaptopDetails.setCompanySalesOrder(null);

			} else if (companyPCLaptopDTO.getInOut().equalsIgnoreCase("OUT")) {

				CompanySalesOrder findCompanySalesOrder = findCompanySalesOrder(companyPCLaptopDTO, companyInfo);

				companyPcLaptopDetails.setCompanySalesOrder(findCompanySalesOrder);
				companyPcLaptopDetails.setCompanyPurchaseOrder(null);
			} else {
				throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
			}
		} else {
			companyPcLaptopDetails.setProductName(companyPCLaptopDTO.getProductName());
			companyPcLaptopDetails.setCompanySalesOrder(null);
			companyPcLaptopDetails.setCompanyPurchaseOrder(null);
		}

		companyPCLaptopDTO.setStatus(AVAILABLE);
		companyPCLaptopDTO.setCpldIsWorking(Boolean.TRUE);
		companyPCLaptopDTO.setAvailability(WORKING);
		companyPcLaptopDetails.setInOut(companyPCLaptopDTO.getInOut());
		companyPcLaptopDetails.setProductName(companyPCLaptopDTO.getProductName());
		companyPcLaptopDetails.setMemory(companyPCLaptopDTO.getMemory());
		companyPcLaptopDetails.setProcessor(companyPCLaptopDTO.getProcessor());
		companyPcLaptopDetails.setOs(companyPCLaptopDTO.getOs());
		companyPcLaptopDetails.setStorage(companyPCLaptopDTO.getStorage());
		companyPcLaptopDetails.setAmount(companyPCLaptopDTO.getAmount());
		companyPcLaptopDetails.setSerialNumber(companyPCLaptopDTO.getSerialNumber());

		companyPcLaptopDetails.setCompanyInfo(companyInfo);
		log.info("Company PC/Laptop Updated successfully");
		return companyPCLaptopDTO;
	}

	@Transactional
	@Override
	public String updateCompanyPcLaptopEmployeeInfo(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Allocate or deallocate employee for Company PC/Laptop");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		CompanyPcLaptopDetails companyPcLaptopDetails = companyPCLaptopRepository
				.findBySerialNumberAndCompanyInfoCompanyId(companyPCLaptopDTO.getSerialNumber(), companyId)
				.orElseThrow(() -> new CompanyPCLaptopDetailsNotFoundException(
						AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_NOT_FOUND));

		String returnMessage = "";
		if (companyPCLaptopDTO.getEmployeePersonalInfo() != null && companyPcLaptopDetails.getAllocatedDate() == null
				&& companyPcLaptopDetails.getEmployeePersonalInfo() == null) {

			if (Boolean.FALSE.equals(companyPcLaptopDetails.getCpldIsWorking())) {
				throw new CompanyIdNotFoundException("Product is not working , can't be allocated");
			}

			Optional<CompanyPcLaptopDetails> findByEmployeePersonalInfoEmployeeInfoId = companyPCLaptopRepository
					.findByEmployeePersonalInfoEmployeeInfoId(
							companyPCLaptopDTO.getEmployeePersonalInfo().getEmployeeInfoId());

			if (findByEmployeePersonalInfoEmployeeInfoId.isPresent()) {
				throw new CompanyIdNotFoundException("Employee is already allocated");
			}

			Long employeeInfoId2 = companyPCLaptopDTO.getEmployeePersonalInfo().getEmployeeInfoId();
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
			companyPCLaptopDTO.setEmployeePersonalInfo(dto);
			
			companyPcLaptopDetails.setEmployeePersonalInfo(employeeOptional.get());

			Map<String, Map<String, String>> historySet = companyPcLaptopDetails.getCpldHistory();

			Map<String, String> historyValue = new LinkedHashMap<>();

			historyValue.put(null, companyPcLaptopDetails.getEmployeePersonalInfo().getEmployeeInfoId().toString());

			log.info("check all key" + historySet.keySet().toString());

			historySet.put(LocalDateTime.now().toString(), historyValue);

			log.info("check all key" + historySet.keySet().toString());

			companyPcLaptopDetails.setAllocatedDate(LocalDate.now());
			log.info("Employee is allocated for Company PC/Laptop");
			returnMessage = "Employee Allocated";

		} else if (companyPCLaptopDTO.getEmployeePersonalInfo() == null
				&& companyPcLaptopDetails.getAllocatedDate() != null
				&& companyPcLaptopDetails.getEmployeePersonalInfo() != null) {

			Map<String, Map<String, String>> historySet = companyPcLaptopDetails.getCpldHistory();

			Set<String> keySet = historySet.keySet();

			List<String> listKeys = new ArrayList<>(keySet);

			Map<String, String> historyValue = new LinkedHashMap<>();

			historyValue.put(LocalDateTime.now().toString(),
					companyPcLaptopDetails.getEmployeePersonalInfo().getEmployeeInfoId().toString());

			historySet.put(listKeys.get(listKeys.size() - 1), historyValue);

			companyPcLaptopDetails.setEmployeePersonalInfo(null);

			companyPcLaptopDetails.setAllocatedDate(null);
			log.info("Employee is deallocated for Company PC/Laptop");
			returnMessage = "Employee Deallocated";
		}

		companyPcLaptopDetails.setCompanyInfo(companyInfo);
		return returnMessage;
	}

	public CompanyPCLaptopDTO dto(CompanyPcLaptopDetails x, CompanyPCLaptopDTO companyPCLaptopDTO) {

		if (x.getInOut().equalsIgnoreCase("IN")) {
			companyPCLaptopDTO.setPurchaseOrderId(x.getCompanyPurchaseOrder().getPurchaseOrderId());
			companyPCLaptopDTO.setSubject(x.getCompanyPurchaseOrder().getSubject());
			companyPCLaptopDTO.setStatus(Boolean.TRUE.equals(x.getCpldIsWorking()) ? WORKING : NOT_WORKING);

			List<PurchaseOrderItems> findByCompanyPurchaseOrderPurchaseOrderId = purchaseOrderItemsRepository
					.findByCompanyPurchaseOrderPurchaseOrderId(x.getCompanyPurchaseOrder().getPurchaseOrderId());
			if (findByCompanyPurchaseOrderPurchaseOrderId == null) {
				throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);
			}
			PurchaseOrderItems findByProductNameAndCompanyPurchaseOrderPurchaseOrderId = purchaseOrderItemsRepository
					.findByProductNameAndCompanyPurchaseOrderPurchaseOrderId(x.getProductName(),
							x.getCompanyPurchaseOrder().getPurchaseOrderId());

			if (findByProductNameAndCompanyPurchaseOrderPurchaseOrderId == null) {
				throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);
			}

			companyPCLaptopDTO
					.setProductItemId(findByProductNameAndCompanyPurchaseOrderPurchaseOrderId.getPurchaseItemId());

		} else if (x.getInOut().equalsIgnoreCase("OUT")) {
			companyPCLaptopDTO.setSalesOrderId(x.getCompanySalesOrder().getSalesOrderId());
			companyPCLaptopDTO.setSubject(x.getCompanySalesOrder().getSubject());
			companyPCLaptopDTO.setStatus(Boolean.TRUE.equals(x.getCpldIsWorking()) ? WORKING : NOT_WORKING);

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
			companyPCLaptopDTO.setProductItemId(findByProductNameAndCompanySalesOrderSalesOrderId.getSaleItemId());
		}

		return companyPCLaptopDTO;
	}

	
	//get api
	@Override
	public List<CompanyPCLaptopDTO> companyPCLaptopDetails(Long companyId) {
		log.info("List of Company PC/Laptop details");
		List<CompanyPcLaptopDetails> companyPcLaptopDetailsList = companyPCLaptopRepository
				.findByCompanyInfoCompanyId(companyId);

		if (companyPcLaptopDetailsList.isEmpty()) {
			return Collections.emptyList();
		}

		List<CompanyPcLaptopDetails> pcLaptopDetailsList = companyPcLaptopDetailsList.stream()
				.sorted(Comparator.comparing(CompanyPcLaptopDetails::getCreatedDate).reversed())
				.collect(Collectors.toList());

		return pcLaptopDetailsList.stream().map(x -> {
			CompanyPCLaptopDTO companyPCLaptopDTO = new CompanyPCLaptopDTO();
			BeanUtils.copyProperties(x, companyPCLaptopDTO);
			if (x.getCompanyPurchaseOrder() != null || x.getCompanySalesOrder() != null) {
				dto(x, companyPCLaptopDTO);
			} else {
				companyPCLaptopDTO.setProductName(x.getProductName());
			}
			companyPCLaptopDTO.setStatus(Boolean.TRUE.equals(x.getCpldIsWorking()) ? WORKING : NOT_WORKING);
			companyPCLaptopDTO.setAvailability(x.getEmployeePersonalInfo() == null ? AVAILABLE : ALLOCATED);
			return companyPCLaptopDTO;
		}).collect(Collectors.toList());
	}

	public List<IssuesDTO> issueList(List<IssuesDTO> issuesDTOs, List<CompanyItTickets> findByCompanyId,
			Long companyId) {

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
				Optional<EmployeePersonalInfo> findById = findByCompanyInfoCompanyId.stream()
						.filter(x1 -> x1.getEmployeeInfoId().equals(by)).findFirst();

				if (findById.isPresent()) {
					String name = "";
					String firstName = findById.get().getFirstName();
					String lastName = findById.get().getLastName();
					name = firstName + " " + lastName;
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

	public CompanyPCLaptopDTO historyDtoList(Map<String, Map<String, String>> cpldHistory,
			List<HistoryDTO> historyDTOs2, CompanyPCLaptopDTO companyPCLaptopDTO) {
		if (cpldHistory == null) {
			companyPCLaptopDTO.setHistoryDTOs(null);
		} else {

			Set<Entry<String, Map<String, String>>> entrySet = cpldHistory.entrySet();

			for (Entry<String, Map<String, String>> entry : entrySet) {

				HistoryDTO historyDTO = new HistoryDTO();

				Set<Entry<String, String>> entrySet2 = entry.getValue().entrySet();
				for (Entry<String, String> entry2 : entrySet2) {
					historyDTO.setDeAllocatedDate(entry2.getKey());
					EmployeePersonalInfo employeePersonalInfoHistory = employeePersonalInfoRepository
							.findById(Long.parseLong(entry2.getValue()))
							.orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

					EmployeeOfficialInfo employeeOfficialInfo2 = employeePersonalInfoHistory.getEmployeeOfficialInfo();

					if (employeeOfficialInfo2 == null) {
						throw new NoEmployeeOfficialInfoException("Employee official details not found");
					}

					historyDTO.setEmployeeId(employeeOfficialInfo2.getEmployeeId());

					historyDTO.setName(
							employeePersonalInfoHistory.getFirstName() + employeePersonalInfoHistory.getLastName());
				}
				historyDTO.setAllocatedDate(entry.getKey());

				historyDTOs2.add(historyDTO);
			}
			companyPCLaptopDTO.setHistoryDTOs(historyDTOs2);
		}
		return companyPCLaptopDTO;
	}

	//get api
	@Override
	public CompanyPCLaptopDTO getcompanyPCLaptop(Long companyId, String serialNumber) {
		log.info("Company PC/Laptop details with respect to serial number");
		CompanyPcLaptopDetails pcLaptopSerialNumber = companyPCLaptopRepository
				.findBySerialNumberAndCompanyInfoCompanyId(serialNumber, companyId)
				.orElseThrow(() -> new CompanyPCLaptopDetailsNotFoundException(
						AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_NOT_FOUND));

		CompanyPCLaptopDTO companyPCLaptopDTO = new CompanyPCLaptopDTO();
		BeanUtils.copyProperties(pcLaptopSerialNumber, companyPCLaptopDTO);

		List<PcLaptopSoftwareDetailsDTO> collect = pcLaptopSerialNumber.getPcLaptopSoftwareDetailsList().stream()
				.map(x -> {
					PcLaptopSoftwareDetailsDTO softwareDetailsDTO = new PcLaptopSoftwareDetailsDTO();
					BeanUtils.copyProperties(x, softwareDetailsDTO);
					return softwareDetailsDTO;
				}).collect(Collectors.toList());

		EmployeePersonalInfo employeePersonalInfo = pcLaptopSerialNumber.getEmployeePersonalInfo();
		if (employeePersonalInfo != null) {
			EmployeePersonalInfoDTO employeePersonalInfoDTO = new EmployeePersonalInfoDTO();
			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			if (employeeOfficialInfo == null) {
				throw new NoEmployeeOfficialInfoException("Employee official details not found");
			}
			employeePersonalInfoDTO.setEmployeeId(employeeOfficialInfo.getEmployeeId());
			employeePersonalInfoDTO
					.setFullName(employeePersonalInfo.getFirstName() + employeePersonalInfo.getLastName());
			companyPCLaptopDTO.setEmployeePersonalInfo(employeePersonalInfoDTO);

		}

		CompanyPcLaptopDetails x = pcLaptopSerialNumber;
		if (x.getCompanyPurchaseOrder() != null || x.getCompanySalesOrder() != null) {

			dto(x, companyPCLaptopDTO);
		} else {
			companyPCLaptopDTO.setProductName(x.getProductName());
		}

		
		List<CompanyItTickets> findByCompanyId = companyItTicketsRepository
				.findByCompanyIdAndIdentificationNumber(companyId, companyPCLaptopDTO.getSerialNumber());

		List<IssuesDTO> issuesDTOs = new ArrayList<>();

		List<IssuesDTO> issueList = issueList(issuesDTOs, findByCompanyId, companyId);

		companyPCLaptopDTO.setIssuesDTO(issueList);

		Map<String, Map<String, String>> cpldHistory = pcLaptopSerialNumber.getCpldHistory();
		List<HistoryDTO> historyDTOs2 = new ArrayList<>();

		historyDtoList(cpldHistory, historyDTOs2, companyPCLaptopDTO);
		
		companyPCLaptopDTO
				.setStatus(Boolean.TRUE.equals(companyPCLaptopDTO.getCpldIsWorking()) ? WORKING : NOT_WORKING);
		companyPCLaptopDTO.setPcLaptopSoftwareDetailsList(collect);
		companyPCLaptopDTO.setAvailability(companyPCLaptopDTO.getAllocatedDate() == null ? AVAILABLE : ALLOCATED);
		log.info("Company PC/Laptop details fetched with respect to serial number");
		return companyPCLaptopDTO;
	}

	@Override
	public List<String> getAllSerialNumber() {
		log.info("List of serial number");
		List<CompanyPcLaptopDetails> findAllById = companyPCLaptopRepository.findAll();
		if (findAllById.isEmpty()) {
			throw new NoSerialNumberListException(AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_NOT_FOUND);
		}

		return findAllById.stream().map(CompanyPcLaptopDetails::getSerialNumber).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<SubjectDTO> getAllSubjects(Long companyId, String inOut, Integer status) {
		log.info("List of subjects");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		if (inOut.equalsIgnoreCase("IN")) {
			return companyInfo.getCompanyPurchaseOrderList().stream()
					.filter(y -> Objects.equals(y.getIsHardware(), status)).map(x -> {
						SubjectDTO subjectDTO = new SubjectDTO();
						subjectDTO.setPurchaseOrderId(x.getPurchaseOrderId());
						subjectDTO.setSubject(x.getSubject());
						return subjectDTO;
					}).collect(Collectors.toList());
		} else if (inOut.equalsIgnoreCase("OUT")) {
			return companyInfo.getCompanySalesOrderList().stream()
					.filter(y -> Objects.equals(y.getIsHardware(), status)).map(x -> {
						SubjectDTO subjectDTO = new SubjectDTO();
						subjectDTO.setSalesOrderId(x.getSalesOrderId());
						subjectDTO.setSubject(x.getSubject());
						return subjectDTO;
					}).collect(Collectors.toList());

		} else {
			throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
		}

	}

	//get api - products
	@Override
	public List<ProductNameDTO> getProducts(Long companyId, String inOut, Long subjectId) {
		log.info("List of products");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		if (inOut.equalsIgnoreCase("IN")) {
			List<PurchaseOrderItems> purchaseOrder = companyInfo.getCompanyPurchaseOrderList().stream()
					.filter(x -> x.getPurchaseOrderId().equals(subjectId))
					.flatMap(y -> y.getPurchaseOrderItemsList().stream()).collect(Collectors.toList());

			Optional<Long> findFirst2 = purchaseOrder.stream().map(PurchaseOrderItems::getPurchaseItemId).findFirst();
			if (findFirst2.isEmpty()) {
				throw new PurchaseOrderItemNotFoundException(AdminDeptConstants.PURCHASE_ORDER_ITEM_NOT_FOUND);
			}

			return purchaseOrder.stream().filter(
					x -> (x.getProductName().equalsIgnoreCase("PC") || x.getProductName().equalsIgnoreCase("Laptop")))
					.map(x -> {
						ProductNameDTO productNameDTO = new ProductNameDTO();
						productNameDTO.setPurchaseItemId(x.getPurchaseItemId());
						productNameDTO.setProductName(x.getProductName());
						return productNameDTO;
					}).collect(Collectors.toList());
		} else if (inOut.equalsIgnoreCase("OUT")) {

			List<SalesOrderItems> salesOrder = companyInfo.getCompanySalesOrderList().stream()
					.filter(x -> x.getSalesOrderId().equals(subjectId))
					.flatMap(y -> y.getSalesOrderItemsList().stream()).collect(Collectors.toList());

			Optional<Long> findFirst = salesOrder.stream().map(SalesOrderItems::getSaleItemId).findFirst();
			if (findFirst.isEmpty()) {
				throw new SalesOrderItemNotFoundException(AdminDeptConstants.SALES_ORDER_ITEM_NOT_FOUND);
			}

			return salesOrder.stream().filter(
					x -> (x.getProductName().equalsIgnoreCase("PC") || x.getProductName().equalsIgnoreCase("Laptop")))
					.map(x -> {
						ProductNameDTO productNameDTO = new ProductNameDTO();
						productNameDTO.setSaleItemId(x.getSaleItemId());
						productNameDTO.setProductName(x.getProductName());
						return productNameDTO;
					}).collect(Collectors.toList());

		} else {
			throw new InOutNotSelectedException(AdminDeptConstants.IN_OUT_NOT_SELECTD);
		}

	}

	@Override
	@Transactional
	public String markNotWorking(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Mark Company PC/Laptop as working or not working");
		CompanyPcLaptopDetails companyPcLaptopDetails = companyPCLaptopRepository
				.findBySerialNumberAndCompanyInfoCompanyId(companyPCLaptopDTO.getSerialNumber(), companyId)
				.orElseThrow(() -> new CompanyPCLaptopDetailsNotFoundException(
						AdminDeptConstants.COMPANY_PC_LAPTOP_DETAILS_NOT_FOUND));

		
		if (Boolean.TRUE.equals(companyPCLaptopDTO.getCpldIsWorking())) {
			log.info("Company PC/Laptop marked as working");
			companyPcLaptopDetails.setCpldIsWorking(companyPCLaptopDTO.getCpldIsWorking());
			return "Marked as Working";
		} else {
			if (companyPcLaptopDetails.getEmployeePersonalInfo() != null) {
				updateCompanyPcLaptopEmployeeInfo(companyId, companyPCLaptopDTO);
				
			}
			companyPcLaptopDetails.setCpldIsWorking(companyPCLaptopDTO.getCpldIsWorking());
		log.info("Company PC/Laptop marked as not working");
			return "Marked as not Working";
		}
	}
}
