package com.te.flinko.controller.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.account.OfficeExpensesDTO;
import com.te.flinko.dto.account.OfficeExpensesTotalCostDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.account.OfficeExpenesesService;

@RestController
@RequestMapping("api/v1/officeexpenses")
public class OfficeExpensesController extends BaseConfigController {

	@Autowired
	OfficeExpenesesService expenesesService;

	@GetMapping("/")
	public ResponseEntity<SuccessResponse> getTotalCostBasedOnType() {
		List<OfficeExpensesTotalCostDTO> officeExpenseDetails = expenesesService
				.getOfficeExpenseDetails(getCompanyId());
		if (officeExpenseDetails == null) {
			return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Data not present").build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Data Fetched Succesfully")
				.data(officeExpenseDetails).build());
	}

	@PostMapping("/")
	public ResponseEntity<SuccessResponse> addOfficeExpenses(@RequestParam String data,
			@RequestParam MultipartFile multipartFile) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		OfficeExpensesDTO officeExpensesDTO = mapper.readValue(data, OfficeExpensesDTO.class);
		OfficeExpensesDTO addOfficeExpenses = expenesesService.addOfficeExpenses(officeExpensesDTO, multipartFile,getCompanyId());
		if (addOfficeExpenses == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(false).message("Data not add successfully").build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Data Added Succesfully")
				.data(addOfficeExpenses).build());
	}

	@GetMapping("/employee/{reimbursementId}")
	public ResponseEntity<SuccessResponse> getReimbursmentInfoById(@PathVariable Long reimbursementId) {
		OfficeExpensesDTO officeExpensesDTO = expenesesService.getReimbursementById(reimbursementId);
		if (officeExpensesDTO == null) {
			return ResponseEntity
					.ok(SuccessResponse.builder().error(false).message("Data feteched successfully").build());
		}
		return ResponseEntity.ok(SuccessResponse.builder().error(false).message("Data feteched Succesfully")
				.data(officeExpensesDTO).build());
	}
}
