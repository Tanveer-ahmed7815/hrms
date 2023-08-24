package com.te.flinko.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.CompanyStockCategoriesDTO;
import com.te.flinko.dto.admin.CompanyStockGroupDTO;
import com.te.flinko.dto.admin.CompanyStockUnitsDTO;
import com.te.flinko.dto.admin.StockCategoriesDTO;
import com.te.flinko.dto.admin.StockGroupDTO;
import com.te.flinko.dto.admin.StockUnitDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.StockManagementService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/stock-management")
@Slf4j
public class StockManagementController extends BaseConfigController {

	@Autowired
	private StockManagementService stockManagementService;

	@PutMapping("/stock-group")
	public ResponseEntity<SuccessResponse> saveStockGroup(@RequestBody List<CompanyStockGroupDTO> companyStockGroupDTOs) {
		StockGroupDTO stockGroupDTO = new StockGroupDTO(companyStockGroupDTOs);
		log.info(
				"controller method of StockManagementController class,company id is : {}, company Stock Group details : {}",
				getCompanyId(), stockGroupDTO);
		StockGroupDTO stockGroup = stockManagementService.saveStockGroup(getCompanyId(), stockGroupDTO);
		log.info("returned vales of StockManagementController class, stock Group details : {}", stockGroup);
		return new ResponseEntity<>(new SuccessResponse(false, "stock group details saved", stockGroup), HttpStatus.OK);
	}

	@GetMapping("/stock-group")
	public ResponseEntity<SuccessResponse> getStockGroup() {
		log.info("controller method of StockManagementController class, company id is : {}", getCompanyId());
		List<CompanyStockGroupDTO> stockGroup = stockManagementService.getStockGroup(getCompanyId());
		log.info("returned vales of StockManagementController class, stock Group details : {}", stockGroup);
		return new ResponseEntity<>(new SuccessResponse(false, "stock group details", stockGroup), HttpStatus.OK);
	}

	@PutMapping("/stock-units")
	public ResponseEntity<SuccessResponse> saveStockUnits(@RequestBody List<CompanyStockUnitsDTO> companyStockUnitsDTOs) {
		StockUnitDto companyStockUnitsDTO = new StockUnitDto(companyStockUnitsDTOs);
		log.info(
				"controller method of StockManagementController class,company id is : {}, company Stock unit details : {}",
				getCompanyId(), companyStockUnitsDTO);
		StockUnitDto stockUnits = stockManagementService.saveStockUnits(getCompanyId(), companyStockUnitsDTO);
		log.info("returned vales of StockManagementController class, stock units details : {}", stockUnits);
		return new ResponseEntity<>(new SuccessResponse(false, "stock unit details saved", stockUnits), HttpStatus.OK);
	}

	@GetMapping("/stock-units")
	public ResponseEntity<SuccessResponse> getStockUnits() {

		log.info("controller method of StockManagementController class, company id is : {}", getCompanyId());
		List<CompanyStockUnitsDTO> stockUnits = stockManagementService.getStockUnits(getCompanyId());
		log.info("returned vales of StockManagementController class, stock units details : {}", stockUnits);
		return new ResponseEntity<>(new SuccessResponse(false, "stock unit details", stockUnits), HttpStatus.OK);
	}

	@PutMapping("/stock-categories")
	public ResponseEntity<SuccessResponse> saveStockCategories(@RequestBody List<CompanyStockCategoriesDTO> companyStockCategoriesDTOs) {
		 StockCategoriesDTO stockCategoriesDTO= new StockCategoriesDTO(companyStockCategoriesDTOs);
		log.info(
				"controller method of StockManagementController class,company id is : {}, company Stock category details : {}",
				getCompanyId(), stockCategoriesDTO);
		StockCategoriesDTO stockCategory = stockManagementService.saveStockCategory(getCompanyId(), stockCategoriesDTO);
		log.info("returned vales of StockManagementController class, stock category details : {}", stockCategory);
		return new ResponseEntity<>(new SuccessResponse(false, "stock categories details saved", stockCategory),
				HttpStatus.OK);
	}


	
	@GetMapping("/stock-categories")
	public ResponseEntity<SuccessResponse> getAllStockCategories() {
		log.info(
				"controller method of StockManagementController class,company id is : {}",
				getCompanyId());
		
		List<CompanyStockCategoriesDTO> stockCategory = stockManagementService.getListStockCategory(getCompanyId());
		log.info("returned vales of StockManagementController class, stock category details : {}", stockCategory);
		return new ResponseEntity<>(new SuccessResponse(false, "stock categories details", stockCategory),
				HttpStatus.OK);
	}

}
