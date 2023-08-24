package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.CompanyStockCategoriesDTO;
import com.te.flinko.dto.admin.CompanyStockGroupDTO;
import com.te.flinko.dto.admin.CompanyStockUnitsDTO;
import com.te.flinko.dto.admin.StockCategoriesDTO;
import com.te.flinko.dto.admin.StockGroupDTO;
import com.te.flinko.dto.admin.StockUnitDto;

/**
 * @author Tapas
 *
 */
public interface StockManagementService {

	public List<CompanyStockGroupDTO> getStockGroup(Long companyId);

	public StockGroupDTO saveStockGroup(Long companyId, StockGroupDTO stockGroupDTO);

	public List<CompanyStockUnitsDTO> getStockUnits(Long companyId);

	public StockUnitDto saveStockUnits(Long companyId, StockUnitDto stockUnitDto);


	public List<CompanyStockCategoriesDTO> getListStockCategory(Long companyId);
	
	public StockCategoriesDTO saveStockCategory(Long companyId, StockCategoriesDTO stockCategoriesDTO);
	
	

}
