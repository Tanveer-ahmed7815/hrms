package com.te.flinko.service.admindept;

import java.util.Comparator;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constant.admindept.AdminDeptConstants;
import com.te.flinko.dto.admindept.CreateOtherStockGroupItemDTO;
import com.te.flinko.dto.admindept.EditOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.GetOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.admindept.StockGroupDTO;
import com.te.flinko.dto.admindept.SubjectDTO;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.account.PurchaseOrderItems;
import com.te.flinko.entity.account.SalesOrderItems;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyStockGroup;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;
import com.te.flinko.exception.CompanyIdNotFoundException;
import com.te.flinko.exception.admindept.DuplicateProductNameException;
import com.te.flinko.exception.admindept.ProductNameNotFoundException;
import com.te.flinko.exception.admindept.PurchaseOrderItemNotFoundException;
import com.te.flinko.exception.admindept.PurchaseOrderNotFoundException;
import com.te.flinko.exception.admindept.QuantityNotMatchedException;
import com.te.flinko.exception.admindept.SalesOrderNotFoundException;
import com.te.flinko.exception.admindept.StockGroupItemNotFoundException;
import com.te.flinko.exception.admindept.StockGroupNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyStockGroupRepository;
import com.te.flinko.repository.admindept.CompanyStockGroupItemsRepository;
import com.te.flinko.repository.admindept.PurchaseOrderItemsRepository;
import com.te.flinko.repository.admindept.SalesOrderItemsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@Slf4j
@Service
public class OtherStockGroupServiceImpl implements OtherStockGroupService {

	@Autowired
	private CompanyStockGroupItemsRepository companyStockGroupItemsRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private PurchaseOrderItemsRepository purchaseOrderItemsRepository;

	@Autowired
	private SalesOrderItemsRepository salesOrderItemsRepository;

	@Autowired
	private CompanyStockGroupRepository companyStockGroupRepository;

	static CompanyPurchaseOrder findCompanyPurchaseOrder(CompanyStockGroup companyStockGroup, Long subjectId,
			Long productId, String productName) {
		Optional<CompanyPurchaseOrder> purchaseOrder = companyStockGroup.getCompanyPurchaseOrderList().stream()
				.filter(x -> x.getPurchaseOrderId().equals(subjectId)).findFirst();
		if (purchaseOrder.isEmpty()) {
			throw new PurchaseOrderNotFoundException("Subject id not found");
		}

		Optional<PurchaseOrderItems> purchaseOrderItem = purchaseOrder.get().getPurchaseOrderItemsList().stream()
				.filter(x -> x.getPurchaseItemId().equals(productId)).findFirst();
		if (purchaseOrderItem.isEmpty()) {
			throw new PurchaseOrderItemNotFoundException("Product id not found");
		}

		if (!purchaseOrderItem.get().getProductName().equals(productName)) {
			
			throw new ProductNameNotFoundException(AdminDeptConstants.PRODUCT_NAME_NOT_FOUND);

		}

		return purchaseOrder.get();
	}

	static CompanySalesOrder findCompanySalesOrder(CompanyStockGroup companyStockGroup, Long subjectId, Long productId,
			String productName) {
		Optional<CompanySalesOrder> salesOrder = companyStockGroup.getCompanySalesOrderList().stream()
				.filter(x -> x.getSalesOrderId().equals(subjectId)).findFirst();
		if (salesOrder.isEmpty()) {
			throw new SalesOrderNotFoundException("Subject id not found");
		}

		Optional<SalesOrderItems> salesOrderItem = salesOrder.get().getSalesOrderItemsList().stream()
				.filter(x -> x.getSaleItemId().equals(productId)).findFirst();
		if (salesOrderItem.isEmpty()) {
			throw new PurchaseOrderItemNotFoundException("Product id is not found");
		}

		if (!salesOrderItem.get().getProductName().equals(productName)) {
			throw new ProductNameNotFoundException(AdminDeptConstants.PRODUCT_NAME_NOT_FOUND);
		}

		return salesOrder.get();
	}

	@Override
	@Transactional
	public String createOtherStockGroupItem(CreateOtherStockGroupItemDTO createOtherStockGroupItemDTO, Long companyId) {
		
		log.info("service method \"creating item\" of OtherStockGroupServiceImpl class , company id is : {}",
				companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));
		
		createOtherStockGroupItemDTO.setFree(createOtherStockGroupItemDTO.getQuantity());

		createOtherStockGroupItemDTO.setWorking(createOtherStockGroupItemDTO.getQuantity());

		CompanyStockGroupItems companyStockGroupItems = new CompanyStockGroupItems();

		Long stockGroupId = createOtherStockGroupItemDTO.getStockGroupId();

		CompanyStockGroup companyStockGroup = findCompanyStockGroup(companyInfo, stockGroupId);

		OtherStockGroupServiceImpl otherStockGroupServiceImpl = new OtherStockGroupServiceImpl();

		if (createOtherStockGroupItemDTO.getSubjectId() != null) {
			if (createOtherStockGroupItemDTO.getInOut().equalsIgnoreCase("in")) {

				CompanyPurchaseOrder purchaseOrder = findCompanyPurchaseOrder(companyStockGroup,
						createOtherStockGroupItemDTO.getSubjectId(), createOtherStockGroupItemDTO.getProductId(),
						createOtherStockGroupItemDTO.getProductName());

				otherStockGroupServiceImpl.duplicateProductNameCreation(companyStockGroupItemsRepository,
						createOtherStockGroupItemDTO.getProductName(), purchaseOrder, null, companyStockGroup,
						createOtherStockGroupItemDTO.getInOut());

				companyStockGroupItems.setCompanyPurchaseOrder(purchaseOrder);

			} else if (createOtherStockGroupItemDTO.getInOut().equalsIgnoreCase("out")) {
				CompanySalesOrder salesOrder = findCompanySalesOrder(companyStockGroup,
						createOtherStockGroupItemDTO.getSubjectId(), createOtherStockGroupItemDTO.getProductId(),
						createOtherStockGroupItemDTO.getProductName());

				otherStockGroupServiceImpl.duplicateProductNameCreation(companyStockGroupItemsRepository,
						createOtherStockGroupItemDTO.getProductName(), null, salesOrder, companyStockGroup,
						createOtherStockGroupItemDTO.getInOut());

				companyStockGroupItems.setCompanySalesOrder(salesOrder);
				

			}
		} else {
			otherStockGroupServiceImpl.duplicateProductNameCreation(companyStockGroupItemsRepository,
					createOtherStockGroupItemDTO.getProductName(), null, null, companyStockGroup,
					createOtherStockGroupItemDTO.getInOut());

		}

		BeanUtils.copyProperties(createOtherStockGroupItemDTO, companyStockGroupItems);
		

		companyStockGroupItems.setCompanyStockGroup(companyStockGroup);
		companyStockGroupItems.setProductName(createOtherStockGroupItemDTO.getProductName());
		companyStockGroupItems.setCompanyInfo(companyInfo);
		companyStockGroupItemsRepository.save(companyStockGroupItems);

		return "company-stock-group-items-saved";

	}

	@Override
	public List<GetOtherStockGroupItemDto> getAllOtherStockGroupItem(Long companyId) {
		log.info("service method of OtherStockGroupServiceImpl class company id is : {}", companyId);

		List<CompanyStockGroupItems> listOfOtherStockGroupItem1 = companyStockGroupItemsRepository
				.findByCompanyInfoCompanyId(companyId);
		
		List<CompanyStockGroupItems> listOfOtherStockGroupItem = listOfOtherStockGroupItem1.stream()
				.sorted(Comparator.comparing(CompanyStockGroupItems::getLastModifiedDate).reversed())
				.collect(Collectors.toList());

		return listOfOtherStockGroupItem.stream().map(x -> {
			GetOtherStockGroupItemDto getOtherStockGroupItemDTO = new GetOtherStockGroupItemDto();
			BeanUtils.copyProperties(x, getOtherStockGroupItemDTO);
			getOtherStockGroupItemDTO.setStockGroupId(x.getCompanyStockGroup().getStockGroupId());
			getOtherStockGroupItemDTO.setStockGroupName(x.getCompanyStockGroup().getStockGroupName());

			if (x.getCompanyPurchaseOrder() != null) {
				getOtherStockGroupItemDTO.setSubject(x.getCompanyPurchaseOrder().getSubject());
				getOtherStockGroupItemDTO.setPurchaseOrderId(x.getCompanyPurchaseOrder().getPurchaseOrderId());
				getOtherStockGroupItemDTO.setProductId(purchaseOrderItemsRepository
						.findByProductNameAndCompanyPurchaseOrderPurchaseOrderId(x.getProductName(),
								x.getCompanyPurchaseOrder().getPurchaseOrderId())
						.getPurchaseItemId());
			} else if (x.getCompanySalesOrder() != null) {
				getOtherStockGroupItemDTO.setSubject(x.getCompanySalesOrder().getSubject());
				getOtherStockGroupItemDTO.setSalesOrderId(x.getCompanySalesOrder().getSalesOrderId());
				getOtherStockGroupItemDTO.setProductId(
						salesOrderItemsRepository.findByProductNameAndCompanySalesOrderSalesOrderId(x.getProductName(),
								x.getCompanySalesOrder().getSalesOrderId()).getSaleItemId());
			} else {
				getOtherStockGroupItemDTO.setProductName(x.getProductName());
			}
			return getOtherStockGroupItemDTO;
		}).collect(Collectors.toList());

	}

	@Override
	public GetOtherStockGroupItemDto getOtherStockGroupItem(Long stockGroupItemId) {
		CompanyStockGroupItems x = companyStockGroupItemsRepository.findById(stockGroupItemId)
				.orElseThrow(() -> new StockGroupItemNotFoundException("stock group item not found"));
		GetOtherStockGroupItemDto getOtherStockGroupItemDTO = new GetOtherStockGroupItemDto();
		BeanUtils.copyProperties(x, getOtherStockGroupItemDTO);
		getOtherStockGroupItemDTO.setStockGroupId(x.getCompanyStockGroup().getStockGroupId());
		getOtherStockGroupItemDTO.setStockGroupName(x.getCompanyStockGroup().getStockGroupName());
//		x.getProductName()
		if (x.getCompanyPurchaseOrder() != null) {
			getOtherStockGroupItemDTO.setSubject(x.getCompanyPurchaseOrder().getSubject());
			getOtherStockGroupItemDTO.setPurchaseOrderId(x.getCompanyPurchaseOrder().getPurchaseOrderId());
			getOtherStockGroupItemDTO
					.setProductId(purchaseOrderItemsRepository.findByProductNameAndCompanyPurchaseOrderPurchaseOrderId(
							x.getProductName(), x.getCompanyPurchaseOrder().getPurchaseOrderId()).getPurchaseItemId());
		} else if (x.getCompanySalesOrder() != null) {
			getOtherStockGroupItemDTO.setSubject(x.getCompanySalesOrder().getSubject());
			getOtherStockGroupItemDTO.setSalesOrderId(x.getCompanySalesOrder().getSalesOrderId());
			getOtherStockGroupItemDTO.setProductId(
					salesOrderItemsRepository.findByProductNameAndCompanySalesOrderSalesOrderId(x.getProductName(),
							x.getCompanySalesOrder().getSalesOrderId()).getSaleItemId());
		} else {
			getOtherStockGroupItemDTO.setProductName(x.getProductName());
		}
		
		return getOtherStockGroupItemDTO;
	}

	@Override
	@Transactional
	public String editOtherStockGroupItem(EditOtherStockGroupItemDto editOtherStockGroupItemDto, Long companyId) {
		log.info("service method of OtherStockGroupServiceImpl class company id is : {}", companyId);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));
		
		int quantity = editOtherStockGroupItemDto.getQuantity();
		
		if (quantity<editOtherStockGroupItemDto.getFree() || quantity<editOtherStockGroupItemDto.getWorking() && quantity<editOtherStockGroupItemDto.getInUse()) {
			throw new QuantityNotMatchedException("provided value is more than present quantity");
		}

		if (editOtherStockGroupItemDto.getStockGroupItemId() == null) {
			throw new StockGroupItemNotFoundException("stock group item id not found");
		}

		CompanyStockGroupItems otherStockGroupItem = companyStockGroupItemsRepository
				.findByCompanyInfoCompanyIdAndStockGroupItemId(companyId,
						editOtherStockGroupItemDto.getStockGroupItemId());

		if (otherStockGroupItem == null) {
			throw new StockGroupItemNotFoundException("Stock group id not found");
		}

		Long stockGroupId = editOtherStockGroupItemDto.getStockGroupId();

		CompanyStockGroup companyStockGroup = findCompanyStockGroup(companyInfo, stockGroupId);

		OtherStockGroupServiceImpl otherStockGroupServiceImpl = new OtherStockGroupServiceImpl();

		if (editOtherStockGroupItemDto.getSubjectId() != null) {
			if (editOtherStockGroupItemDto.getInOut().equalsIgnoreCase("in")) {

				CompanyPurchaseOrder purchaseOrder = findCompanyPurchaseOrder(companyStockGroup,
						editOtherStockGroupItemDto.getSubjectId(), editOtherStockGroupItemDto.getProductId(),
						editOtherStockGroupItemDto.getProductName());

				otherStockGroupServiceImpl.duplicateProductNameEditing(companyStockGroupItemsRepository,
						editOtherStockGroupItemDto.getProductName(), purchaseOrder, null, companyStockGroup,
						otherStockGroupItem.getStockGroupItemId(), editOtherStockGroupItemDto.getInOut());

				otherStockGroupItem.setCompanyPurchaseOrder(purchaseOrder);
				otherStockGroupItem.setCompanySalesOrder(null);

			} else if (editOtherStockGroupItemDto.getInOut().equalsIgnoreCase("out")) {

				CompanySalesOrder salesOrder = findCompanySalesOrder(companyStockGroup,
						editOtherStockGroupItemDto.getSubjectId(), editOtherStockGroupItemDto.getProductId(),
						editOtherStockGroupItemDto.getProductName());

				otherStockGroupServiceImpl.duplicateProductNameEditing(companyStockGroupItemsRepository,
						editOtherStockGroupItemDto.getProductName(), null, salesOrder, companyStockGroup,
						otherStockGroupItem.getStockGroupItemId(), editOtherStockGroupItemDto.getInOut());

				otherStockGroupItem.setCompanySalesOrder(salesOrder);
				otherStockGroupItem.setCompanyPurchaseOrder(null);
			}
		}

		else {

			otherStockGroupServiceImpl.duplicateProductNameEditing(companyStockGroupItemsRepository,
					editOtherStockGroupItemDto.getProductName(), null, null, companyStockGroup,
					otherStockGroupItem.getStockGroupItemId(), editOtherStockGroupItemDto.getInOut());

			otherStockGroupItem.setCompanyPurchaseOrder(null);
			otherStockGroupItem.setCompanySalesOrder(null);
		}

		
		
		BeanUtils.copyProperties(editOtherStockGroupItemDto, otherStockGroupItem);

		otherStockGroupItem.setCompanyStockGroup(companyStockGroup);

		otherStockGroupItem.setProductName(editOtherStockGroupItemDto.getProductName());

		return "Company-stock-group-items-updated";
	}

	public void duplicateProductNameEditing(CompanyStockGroupItemsRepository companyStockGroupItemsRepository,
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			CompanyStockGroup companyStockGroup, Long stockGroupItemId, String inOut) {

		List<CompanyStockGroupItems> companyStockGroupItem = companyStockGroupItemsRepository
				.findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndCompanyStockGroupAndInOut(productName,
						companyPurchaseOrder, companySalesOrder, companyStockGroup, inOut);

		if (!companyStockGroupItem.isEmpty() && (companyStockGroupItem.size() > 1
				|| !companyStockGroupItem.get(0).getStockGroupItemId().equals(stockGroupItemId))) {

			throw new DuplicateProductNameException("Product name already present");

		}
	}

	public void duplicateProductNameCreation(CompanyStockGroupItemsRepository companyStockGroupItemsRepository,
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			CompanyStockGroup companyStockGroup, String inOut) {
		List<CompanyStockGroupItems> companyStockGroupItem = companyStockGroupItemsRepository
				.findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndCompanyStockGroupAndInOut(productName,
						companyPurchaseOrder, companySalesOrder, companyStockGroup, inOut);

		if (!companyStockGroupItem.isEmpty()) {

			throw new DuplicateProductNameException("Product name already present!!!");

		}
	}

	public static CompanyStockGroup findCompanyStockGroup(CompanyInfo companyInfo, Long stockGroupId) {
		Optional<CompanyStockGroup> stockGroup = companyInfo.getCompanyStockGroupList().stream()
				.filter(x -> x.getStockGroupId().equals(stockGroupId)).findFirst();

		if (stockGroup.isEmpty())
			throw new StockGroupNotFoundException("Stock group not found");

		return stockGroup.get();

	}

	@Override
	public List<StockGroupDTO> getAllStockGroup(Long companyId) {
		log.info("service method \"getting all stock group\" of OtherStockGroupServiceImpl class , company id is : {}",
				companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		return companyInfo.getCompanyStockGroupList().stream().filter(x -> !x.getStockGroupName().equalsIgnoreCase("IT")).map(x -> {
			StockGroupDTO stockGroupDTO = new StockGroupDTO();
			BeanUtils.copyProperties(x, stockGroupDTO);
			return stockGroupDTO;
		}).collect(Collectors.toList());

	}

	@Override
	public List<SubjectDTO> getAllSubject(Long stockGroupId, String inOut) {
		log.info("service method of OtherStockGroupServiceImpl class , stock-Group-Id id is : {}", stockGroupId);
		log.info("service method of OtherStockGroupServiceImpl class , in/out : {}", inOut);
		CompanyStockGroup companyStockGroup = companyStockGroupRepository.findById(stockGroupId)
				.orElseThrow(() -> new StockGroupNotFoundException("Stock group id not found"));

		if (inOut.equalsIgnoreCase("in")) {
			return companyStockGroup.getCompanyPurchaseOrderList().stream().map(x -> {
				SubjectDTO subjectDto = new SubjectDTO();
				subjectDto.setSubject(x.getSubject());
				subjectDto.setPurchaseOrderId(x.getPurchaseOrderId());
				return subjectDto;
			}).collect(Collectors.toList());
		} else {
			return companyStockGroup.getCompanySalesOrderList().stream().map(x -> {
				SubjectDTO subjectDto = new SubjectDTO();
				subjectDto.setSubject(x.getSubject());
				subjectDto.setSalesOrderId(x.getSalesOrderId());
				return subjectDto;
			}).collect(Collectors.toList());
		}

	}

	@Override
	public List<ProductNameDTO> getAllProductName(Long companyId, Long subjectId, String inOut) {
		log.info("service method of OtherStockGroupServiceImpl class , company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(AdminDeptConstants.COMPANY_NOT_FOUND));

		if (inOut.equalsIgnoreCase("in")) {
			List<PurchaseOrderItems> purchaseOrderItems = companyInfo.getCompanyPurchaseOrderList().stream()
					.filter(x -> x.getPurchaseOrderId().equals(subjectId))
					.flatMap(y -> y.getPurchaseOrderItemsList().stream()).collect(Collectors.toList());
			return purchaseOrderItems.stream().map(x -> {
				ProductNameDTO productNameDTO = new ProductNameDTO();
				productNameDTO.setProductName(x.getProductName());
				productNameDTO.setPurchaseItemId(x.getPurchaseItemId());
				return productNameDTO;
			}).collect(Collectors.toList());
		}

		else {
			List<SalesOrderItems> salesOrderItems = companyInfo.getCompanySalesOrderList().stream()
					.filter(x -> x.getSalesOrderId().equals(subjectId))
					.flatMap(y -> y.getSalesOrderItemsList().stream()).collect(Collectors.toList());
			return salesOrderItems.stream().map(x -> {
				ProductNameDTO productNameDTO = new ProductNameDTO();
				productNameDTO.setProductName(x.getProductName());
				productNameDTO.setSaleItemId(x.getSaleItemId());
				return productNameDTO;
			}).collect(Collectors.toList());
		}

	}

}
