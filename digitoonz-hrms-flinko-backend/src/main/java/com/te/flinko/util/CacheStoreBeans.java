package com.te.flinko.util;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.te.flinko.dto.employee.Registration;
import com.te.flinko.entity.employee.EmployeeLoginInfo;

@Configuration
public class CacheStoreBeans {

	@Bean
	public CacheStore<EmployeeLoginInfo> cacheStoreEmployeeLogin() {
		return new CacheStore<>(15, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<Long> cacheStoreOTP() {
		return new CacheStore<>(5, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<Boolean> cacheStoreValidOTP() {
		return new CacheStore<>(12, TimeUnit.MINUTES);
	}

	@Bean
	public CacheStore<Registration> cacheStoreEmployeeRegistrationDto() {
		return new CacheStore<>(15, TimeUnit.MINUTES);
	}
}