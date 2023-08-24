package com.te.flinko.service.admindept;

import java.util.List;

import com.te.flinko.dto.admindept.CreateOtherStockGroupItemDTO;
import com.te.flinko.dto.admindept.EditOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.GetOtherStockGroupItemDto;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.admindept.StockGroupDTO;
import com.te.flinko.dto.admindept.SubjectDTO;



/**
 * @author Tapas
 *
 */

public interface OtherStockGroupService {
	public String createOtherStockGroupItem(CreateOtherStockGroupItemDTO createOtherStockGroupItemDTO,Long companyId);
	
	public List<GetOtherStockGroupItemDto> getAllOtherStockGroupItem(Long companyId);
	
	public GetOtherStockGroupItemDto getOtherStockGroupItem(Long stockGroupItemId);

	public String editOtherStockGroupItem(EditOtherStockGroupItemDto stockGroupItemDto,Long companyId);
	
	public List<StockGroupDTO> getAllStockGroup(Long companyId);
	
	public List<SubjectDTO> getAllSubject(Long stcokGroupId,String inOut);
	
	public List<ProductNameDTO> getAllProductName(Long companyId,Long subjectId,String inOut);
	
}
