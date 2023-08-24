package com.te.flinko.controller.admindept;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admindept.CreateOtherStockGroupItemDTO;
import com.te.flinko.dto.admindept.EditOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.GetOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.admindept.StockGroupDTO;
import com.te.flinko.dto.admindept.SubjectDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admindept.OtherStockGroupService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/create-item")
@Slf4j
public class OtherStockGroupController extends BaseConfigController {
	@Autowired
	private OtherStockGroupService otherStockGroupService;
	
	@PostMapping("/other-stock-group-item")
	public ResponseEntity<SuccessResponse> create(@RequestBody CreateOtherStockGroupItemDTO createOtherStockGroupItemDTO) {
		log.info(" create method \"post\" of controller ");
		String createOtherStockGroupItem = otherStockGroupService.createOtherStockGroupItem(createOtherStockGroupItemDTO,getCompanyId());
	
		return new ResponseEntity<>(new SuccessResponse(false,"Stock item details saved successfully",createOtherStockGroupItem),HttpStatus.OK);
	}
	
	@GetMapping("/other-stock-group-all-item")
	public ResponseEntity<SuccessResponse> getAll() {
		log.info("getAll method \"get\" of controller ");
		List<GetOtherStockGroupItemDto> getAllOtherStockGroupItem = otherStockGroupService.getAllOtherStockGroupItem(getCompanyId());
		return new ResponseEntity<>(new SuccessResponse(false,"list of stock item details displayed",getAllOtherStockGroupItem),HttpStatus.OK);
	}
	
	@GetMapping("/other-stock-group-item/{stockGroupItemId}")
	public ResponseEntity<SuccessResponse> get(@PathVariable Long stockGroupItemId) {
		log.info("getAll method \"get\" of controller ");
		GetOtherStockGroupItemDto getOtherStockGroupItem = otherStockGroupService.getOtherStockGroupItem(stockGroupItemId);
		return new ResponseEntity<>(new SuccessResponse(false,"list of stock item details displayed",getOtherStockGroupItem),HttpStatus.OK);
	}
	
	@PutMapping("/other-stock-group-item")
	public ResponseEntity<SuccessResponse> edit(@RequestBody EditOtherStockGroupItemDto editOtherStockGroupItemDto) {
		log.info("update method \"put\" of controller ");
		String editOtherStockGroupItem = otherStockGroupService.editOtherStockGroupItem(editOtherStockGroupItemDto,getCompanyId());
		return new ResponseEntity<>(new SuccessResponse(false,"Stock item details updated successfully",editOtherStockGroupItem),HttpStatus.OK);
	}
	
	@GetMapping("/stock-group")
	public ResponseEntity<SuccessResponse> getAllStockGroup() {
		log.info("getAllStockGroup method \"get\" of controller ");
		List<StockGroupDTO> allStockGroup = otherStockGroupService.getAllStockGroup(getCompanyId());
		return new ResponseEntity<>(new SuccessResponse(false,"list of stock groups",allStockGroup),HttpStatus.OK);
	}
	
	@GetMapping("/subject/{stockGroupId}/{inOut}")
	public ResponseEntity<SuccessResponse> getAllSubject(@PathVariable Long stockGroupId,@PathVariable String inOut) {
		log.info("getAllSubject method \"get\" of controller ");
		List<SubjectDTO> allSubject = otherStockGroupService.getAllSubject(stockGroupId, inOut);
		return new ResponseEntity<>(new SuccessResponse(false,"list of subjects for : "+inOut,allSubject),HttpStatus.OK);
	}
	
	@GetMapping("/product-name/{subjectId}/{inOut}")
	public ResponseEntity<SuccessResponse> getAllProductName(@PathVariable Long subjectId,@PathVariable String inOut) {
		log.info("getAllProductName method \"get\" of controller ");
		List<ProductNameDTO> allProductName = otherStockGroupService.getAllProductName(getCompanyId(),subjectId,inOut);
		return new ResponseEntity<>(new SuccessResponse(false,"list of products with respect to subjectId : "+subjectId,allProductName),HttpStatus.OK);
	}
}
