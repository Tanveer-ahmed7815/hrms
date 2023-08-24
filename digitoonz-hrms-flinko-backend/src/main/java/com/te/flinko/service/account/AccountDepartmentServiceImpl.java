package com.te.flinko.service.account;

import static com.te.flinko.dto.account.AddressType.BILLING;
import static com.te.flinko.dto.account.AddressType.SHIPPING;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.te.flinko.dto.account.BillingAddressDTO;
import com.te.flinko.dto.account.BillingShippingAddressDTO;
import com.te.flinko.dto.account.CompanyPurchaseOrderDTO;
import com.te.flinko.dto.account.CompanySalesOrderDTO;
import com.te.flinko.dto.account.PurchaseItemsDTO;
import com.te.flinko.dto.account.SalesItemsDTO;
import com.te.flinko.dto.account.ShippingAddressDTO;
import com.te.flinko.dto.account.WorkOrderDTO;
import com.te.flinko.dto.account.WorkOrderResourcesDTO;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.account.CompanyWorkOrder;
import com.te.flinko.entity.account.PurchaseBillingShippingAddress;
import com.te.flinko.entity.account.PurchaseOrderItems;
import com.te.flinko.entity.account.SalesBillingShippingAddress;
import com.te.flinko.entity.account.SalesOrderItems;
import com.te.flinko.entity.account.WorkOrderResources;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.sales.CompanyClientInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.account.SubjectNotUniqueException;
import com.te.flinko.repository.account.ClientContactPersonDetailsRepository;
import com.te.flinko.repository.account.CompanyVendorInfoRepository;
import com.te.flinko.repository.account.CompanyWorkOrderRepository;
import com.te.flinko.repository.account.PurchaseBillingShippingAddressRepository;
import com.te.flinko.repository.account.SalesBillingShippingAddressRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyStockGroupRepository;
import com.te.flinko.repository.admindept.CompanyPurchaseOrderRepository;
import com.te.flinko.repository.admindept.CompanySalesOrderRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.sales.CompanyClientInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDepartmentServiceImpl implements AccountDepartmentService {

	private final CompanyPurchaseOrderRepository companyPurchaseOrderRepository;
	private final CompanySalesOrderRepository companySalesOrderRepository;
	private final CompanyStockGroupRepository companyStockGroupRepository;
	private final PurchaseBillingShippingAddressRepository purchaseBillingShippingAddressRepository;
	private final SalesBillingShippingAddressRepository salesBillingShippingAddressRepository;
	private final CompanyInfoRepository companyInfoRepository;
	private final CompanyClientInfoRepository companyClientInfoRepository;

	private final CompanyVendorInfoRepository companyVendorInfoRepository;

	private final ClientContactPersonDetailsRepository clientContactPersonDetailsRepository;
	private final CompanyWorkOrderRepository companyWorkOrderRepository;
	private final CompanyClientInfoRepository clientInfoRepository;
	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Transactional
	@Override
	public Long createPurchaseOrder(CompanyPurchaseOrderDTO companyPurchaseOrderDTO, Long companyId) {
		if (companyPurchaseOrderRepository.findBySubject(companyPurchaseOrderDTO.getSubject()).size() > 0)
			throw new SubjectNotUniqueException("Subject needs to be unique");
		return companyPurchaseOrderRepository.saveAndFlush(CompanyPurchaseOrder.builder()
				.companyStockGroup(companyStockGroupRepository.findById(companyPurchaseOrderDTO.getStockGroupId())
						.orElseThrow(() -> new DataNotFoundException(
								"Stock group data with the given stockGroupId could not be found")))
				.companyInfo(companyInfoRepository.findByCompanyId(companyId).orElseThrow(
						() -> new DataNotFoundException("Company data with the given companyId could not be found")))
				.vendorContactPersonId(
						String.valueOf(companyVendorInfoRepository.findById(companyPurchaseOrderDTO.getVendorId())
								.orElseThrow(() -> new DataNotFoundException(
										"Vendor data with the given vendorId could not be found"))
								.getVendorInfoId()))
				.purchaseOrderNumber(companyPurchaseOrderDTO.getPurchaseOrderNumber())
				.type(companyPurchaseOrderDTO.getProductType().name()).subject(companyPurchaseOrderDTO.getSubject())
				.vendorId(companyPurchaseOrderDTO.getVendorId())
				.requisitionNumber(companyPurchaseOrderDTO.getRequisitionNumber())
				.trackingNumber(companyPurchaseOrderDTO.getTrackingNumber())
				.dueDate(companyPurchaseOrderDTO.getDueDate()).carrier(companyPurchaseOrderDTO.getCarrier())
				.exciseDuty(companyPurchaseOrderDTO.getExciseDuty())
				.salesCommission(companyPurchaseOrderDTO.getSalesCommission())
				.status(companyPurchaseOrderDTO.getStatus()).poDate(companyPurchaseOrderDTO.getPurchaseOrderDate())
				.build()).getPurchaseOrderId();
	}

	@Transactional
	@Override
	public Long addPurchaseBillingShippingAddress(BillingShippingAddressDTO billingShippingAddressDTO,
			Long purchaseOrderId) {
		BillingAddressDTO billingAddressDTO = billingShippingAddressDTO.getBillingAddress();
		ShippingAddressDTO shippingAddressDTO = billingShippingAddressDTO.getShippingAddress();
		purchaseBillingShippingAddressRepository.deleteByCompanyPurchaseOrderPurchaseOrderId(purchaseOrderId);
		CompanyPurchaseOrder companyPurchaseOrder = companyPurchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new DataNotFoundException("Purchase order could not be found"));
		companyPurchaseOrder.setPurchaseBillingShippingAddressList(Lists.newArrayList(
				PurchaseBillingShippingAddress.builder().addressType(BILLING.name()).city(billingAddressDTO.getCity())
						.state(billingAddressDTO.getState()).addressDetails(billingAddressDTO.getAddressDetails())
						.pinCode(billingAddressDTO.getPinCode()).companyPurchaseOrder(companyPurchaseOrder).build(),
				PurchaseBillingShippingAddress.builder().addressType(SHIPPING.name()).city(shippingAddressDTO.getCity())
						.state(shippingAddressDTO.getState()).addressDetails(shippingAddressDTO.getAddressDetails())
						.pinCode(shippingAddressDTO.getPinCode()).companyPurchaseOrder(companyPurchaseOrder).build()));
		return companyPurchaseOrderRepository.save(companyPurchaseOrder).getPurchaseOrderId();
	}

	@Transactional
	@Override
	public Long addPurchasedItems(PurchaseItemsDTO purchaseItemsDTO, Long purchaseOrderId) {
		CompanyPurchaseOrder companyPurchaseOrder = companyPurchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new DataNotFoundException("Purchase order could not be found"));
		purchaseItemsDTO.getPurchaseItems().forEach(purchaseItemDTO -> {
			companyPurchaseOrder.getPurchaseOrderItemsList().add(PurchaseOrderItems.builder()
					.productName(purchaseItemDTO.getProductName()).quantity(purchaseItemDTO.getQuantity())
					.amount(purchaseItemDTO.getAmount()).discount(purchaseItemDTO.getDiscount())
					.tax(purchaseItemDTO.getTax()).payableAmount(purchaseItemDTO.getPayableAmount())
					.description(purchaseItemDTO.getDescription()).companyPurchaseOrder(companyPurchaseOrder).build());
		});
		return companyPurchaseOrderRepository.save(companyPurchaseOrder).getPurchaseOrderId();
	}

	@Transactional
	@Override
	public Long createSalesOrder(CompanySalesOrderDTO companySalesOrderDTO, Long companyId) {
		if (companySalesOrderRepository.findBySubject(companySalesOrderDTO.getSubject()).size() > 0)
			throw new SubjectNotUniqueException("Subject needs to be unique");
		return companySalesOrderRepository.saveAndFlush(CompanySalesOrder.builder()
				.companyStockGroup(companyStockGroupRepository.findById(companySalesOrderDTO.getStockGroupId())
						.orElseThrow(() -> new DataNotFoundException(
								"Stock group data with the given stockGroupId is not present")))
				.companyInfo(companyInfoRepository.findByCompanyId(companyId).orElseThrow(
						() -> new DataNotFoundException("Company data could not be found with the given ID")))
				.companyClientInfo(companyClientInfoRepository.findById(companySalesOrderDTO.getCompanyClientInfoID())
						.orElseThrow(() -> new DataNotFoundException(
								"Company client data could not be found with the given ID.")))
				.clientContactPersonDetails(
						clientContactPersonDetailsRepository.findById(companySalesOrderDTO.getClientContactPersonID())
								.orElseThrow(() -> new DataNotFoundException(
										"Company contact person data could not be found with the given ID.")))
				.type(companySalesOrderDTO.getProductType().name()).subject(companySalesOrderDTO.getSubject())
				.purchaseOrder(companySalesOrderDTO.getPurchaseOrder())
				.customerNumber(companySalesOrderDTO.getCustomerNumber()).dueDate(companySalesOrderDTO.getDueDate())
				.pending(companySalesOrderDTO.getPending()).exciseDuty(companySalesOrderDTO.getExciseDuty())
				.carrier(companySalesOrderDTO.getExciseDuty()).status(companySalesOrderDTO.getStatus())
				.salesCommission(companySalesOrderDTO.getSalesCommission()).build()).getSalesOrderId();
	}

	@Transactional
	@Override
	public Long addSalesBillingShippingAddress(BillingShippingAddressDTO billingShippingAddressDTO, Long salesOrderId) {
		BillingAddressDTO billingAddressDTO = billingShippingAddressDTO.getBillingAddress();
		ShippingAddressDTO shippingAddressDTO = billingShippingAddressDTO.getShippingAddress();
		salesBillingShippingAddressRepository.deleteByCompanySalesOrderSalesOrderId(salesOrderId);
		CompanySalesOrder companySalesOrder = companySalesOrderRepository.findById(salesOrderId)
				.orElseThrow(() -> new DataNotFoundException("Sales order could not be found"));
		companySalesOrder.setSalesBillingShippingAddressList(Lists.newArrayList(
				SalesBillingShippingAddress.builder().addressType(BILLING.name()).city(billingAddressDTO.getCity())
						.state(billingAddressDTO.getState()).addressDetails(billingAddressDTO.getAddressDetails())
						.pinCode(billingAddressDTO.getPinCode()).companySalesOrder(companySalesOrder).build(),
				SalesBillingShippingAddress.builder().addressType(SHIPPING.name()).city(shippingAddressDTO.getCity())
						.state(shippingAddressDTO.getState()).addressDetails(shippingAddressDTO.getAddressDetails())
						.pinCode(shippingAddressDTO.getPinCode()).companySalesOrder(companySalesOrder).build()));
		return companySalesOrderRepository.save(companySalesOrder).getSalesOrderId();
	}

	@Transactional
	@Override
	public Long addOrderedItems(SalesItemsDTO salesItemsDTO, Long salesOrderId) {
		CompanySalesOrder companySalesOrder = companySalesOrderRepository.findById(salesOrderId)
				.orElseThrow(() -> new DataNotFoundException("Sales order could not be found"));
		salesItemsDTO.getSalesItems().forEach(salesItemDTO -> {
			companySalesOrder.getSalesOrderItemsList()
					.add(SalesOrderItems.builder().productName(salesItemDTO.getProductName())
							.quantity(salesItemDTO.getQuantity()).amount(salesItemDTO.getAmount())
							.discount(salesItemDTO.getDiscount()).tax(salesItemDTO.getTax())
							.receivableAmount(salesItemDTO.getPayableAmount())
							.description(salesItemDTO.getDescription()).companySalesOrder(companySalesOrder).build());
		});
		return companySalesOrderRepository.save(companySalesOrder).getSalesOrderId();
	}

	@Override
	public WorkOrderDTO createWorkOrder(WorkOrderDTO workOrderDTO, Long companyId) {
		CompanyClientInfo clientInformation = clientInfoRepository
				.findByClientIdAndCompanyInfoCompanyId(workOrderDTO.getCompanyClientInfoId(), companyId)
				.orElseThrow(() -> new DataNotFoundException("The client information is not present"));
		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(workOrderDTO.getRequestTo(), companyId).get(0);
		CompanyWorkOrder companyWorkOrder = new CompanyWorkOrder();
		BeanUtils.copyProperties(workOrderDTO, companyWorkOrder);
		if (Boolean.FALSE.equals(workOrderDTO.getIsCostEstimated()))
			companyWorkOrder.setEstimatedCost(null);
		companyWorkOrder.setCompanyClientInfo(clientInformation);
		companyWorkOrder.setEmployeePersonalInfo(employeePersonalInfo);
		companyWorkOrder.setCompanyInfo(
				companyInfoRepository.findById(companyId).orElseThrow(() -> new DataNotFoundException(null)));
		List<WorkOrderResources> workOrderResourcesList = new ArrayList<>();
		workOrderDTO.getWorkOrderResources().forEach((i) -> {
			WorkOrderResources workOrderResources = new WorkOrderResources();
			BeanUtils.copyProperties(i, workOrderResources);
			workOrderResourcesList.add(workOrderResources);
		});
		companyWorkOrder.setWorkOrderResourcesList(workOrderResourcesList);
		CompanyWorkOrder save = companyWorkOrderRepository.save(companyWorkOrder);
		WorkOrderDTO responseDTO = new WorkOrderDTO();
		List<WorkOrderResourcesDTO> workOrderResourcesDTOList = new ArrayList<>();
		BeanUtils.copyProperties(save, responseDTO);
		save.getWorkOrderResourcesList().forEach((i) -> {
			WorkOrderResourcesDTO workOrderResources = new WorkOrderResourcesDTO();
			BeanUtils.copyProperties(i, workOrderResources);
			workOrderResourcesDTOList.add(workOrderResources);
		});
		responseDTO.setWorkOrderResources(workOrderResourcesDTOList);
		return responseDTO;
	}
}
