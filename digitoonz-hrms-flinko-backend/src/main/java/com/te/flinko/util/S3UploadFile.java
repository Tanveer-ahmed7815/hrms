package com.te.flinko.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.te.flinko.exception.FailedToUploadException;

@Service
public class S3UploadFile {

	/**
	 * This is the end point url for amazon s3 bucket
	 */
	@Value("${amazonProperties.endpoint-url}")
	private String endpointUrl;

	/**
	 * This is the bucketName for amazon s3 bucket
	 */
	@Value("${amazonProperties.bucketName}")
	public String bucketName;

	/**
	 * This enables automatic dependency injection of AmazonS3 interface. This
	 * object is used by methods in the SimpleProductServiceImplementation to call
	 * the respective methods.
	 */
	@Autowired
	private AmazonS3 s3client;

	public String uploadFile(MultipartFile multipartFile) {
		String folder = "";
		String contentType = multipartFile.getContentType();
		if (contentType != null && contentType.contains("image")) {
			folder = "hrms-backend/img/";
		} else if (contentType != null && contentType.contains("video")) {
			folder = "hrms-backend/video/";
		} else if (contentType != null && contentType.contains("pdf") || contentType != null && contentType.contains("ms-excel")  || contentType != null && contentType.contains("openxmlformats-officedocument.spreadsheetml.sheet")) {
			folder = "hrms-backend/file/";
		} else {
			throw new FailedToUploadException(contentType + "File Not Supported");
		}

		String fileUrl = "";
		try {
			File file = convertMultiPartFiletoFile(multipartFile);

			String fileName = generateFileName(multipartFile);

			fileUrl = uploadFileTos3bucketConfig(file, fileName, folder, multipartFile.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailedToUploadException("Failed to upload.");
		}
		return fileUrl;
	}

	private File convertMultiPartFiletoFile(MultipartFile file) throws IllegalStateException, IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
		file.transferTo(convFile);
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {

		String fileName = multiPart.getOriginalFilename();
		if (fileName != null) {
			return new Date().getTime() + File.separator + fileName.replace(" ", "_");
		} else {
			throw new FailedToUploadException("File name is empty.");
		}

	}

	public String uploadFileTos3bucketConfig(File file, String fileName, String folder, String originalName) {
		String filePath = folder + fileName;
		s3client.putObject(
				new PutObjectRequest(bucketName, filePath, file).withCannedAcl(CannedAccessControlList.PublicRead));
		String url = s3client.getUrl(bucketName, filePath).toString();
		Path path = Paths.get((System.getProperty("java.io.tmpdir") + File.separator + originalName));
		try {
			Files.delete(path);
		} catch (IOException e) {
			return url;
		}
		return url;
	}

	public void deleteS3Folder(String folderPath) {
		if (folderPath != null && folderPath.length() > 1) {
			String path = folderPath.replace("https://strongerme-asset.s3.ap-south-1.amazonaws.com/", "");
			for (S3ObjectSummary file : s3client.listObjects(bucketName, path).getObjectSummaries()) {
				s3client.deleteObject(bucketName, file.getKey());
			}
		}
	}

}
