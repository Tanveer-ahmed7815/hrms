package com.te.flinko.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.account.WorkOrderDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.account.AccountDepartmentService;

@RestController
@RequestMapping("/api/v1/workorder")
public class WorkOrderController extends BaseConfigController {
	
	@Autowired
	AccountDepartmentService accountDepartmentService; 
	
	@PostMapping("/")
	public ResponseEntity<SuccessResponse> createWorkOrder(@RequestBody WorkOrderDTO workOrderDTO){
		WorkOrderDTO createWorkOrder = accountDepartmentService.createWorkOrder(workOrderDTO,getCompanyId());
		if(createWorkOrder==null) 
			return	ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Work Order is not created").build());
		return ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Work Order Successfully created").data(createWorkOrder).build());
	}
	
}
