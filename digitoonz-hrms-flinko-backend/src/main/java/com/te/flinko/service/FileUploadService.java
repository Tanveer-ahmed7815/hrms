package com.te.flinko.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	
	public String uploadFile(MultipartFile file);

}
