package com.te.flinko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/file-upload")
@RequiredArgsConstructor
public class FileUploadController {
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping
	public ResponseEntity<SuccessResponse> addEmployeeAdvanceSalary(@RequestBody MultipartFile file) {
		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse.builder().error(Boolean.FALSE).message("File Uploaded Successfully")
								.data(fileUploadService.uploadFile(file))
								.build());
	}

}
