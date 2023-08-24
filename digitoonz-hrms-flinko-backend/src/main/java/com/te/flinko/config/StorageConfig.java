package com.te.flinko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class StorageConfig {

	@Value("${amazonProperties.accessKey}")
	private String accessKye;

	@Value("${amazonProperties.secretKey}")
	private String accessSecret;

	@Value("${amazonProperties.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKye, this.accessSecret);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();

	}

}
