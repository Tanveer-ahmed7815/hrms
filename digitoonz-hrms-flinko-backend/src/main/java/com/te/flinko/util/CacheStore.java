package com.te.flinko.util;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.te.flinko.common.employee.EmployeeRegistrationConstants;
import com.te.flinko.exception.employee.DataNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheStore<T> {
	private Cache<String, T> cache;

	public CacheStore(int expiryDuration, TimeUnit timeUnit) {
		cache = CacheBuilder.newBuilder().expireAfterWrite(expiryDuration, timeUnit)
				.concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
	}

	public T get(String key) {
		try {
			return cache.getIfPresent(key);
		}catch (Exception e) {
			throw new DataNotFoundException(EmployeeRegistrationConstants.SESSION_TIME_EXPIRED);
		}
	}

	public void add(String key, T value) {
		if (key != null && value != null) {
			cache.put(key, value);
			log.info("Record stored in " + value.getClass().getSimpleName() + " Cache with Key = " + key);
		}
	}

	public void invalidate(String key) {
		if (key != null) {
			cache.invalidate(key);
			log.info("Employee Id " + key + " removed from cache!!! ");
		}
	}

	public void invalidateAll() {
		cache.invalidateAll();
		log.info("Removed All cache!!! ");
	}
}