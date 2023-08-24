package com.te.flinko.repository.admin.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.flinko.entity.admin.mongo.VendorFormConfiguration;

public interface VendorFormConfigurationRepository extends MongoRepository<VendorFormConfiguration, Long> {
	
	Optional<VendorFormConfiguration> findByFormConfigurationObjectIdAndCompanyId(String formConfigurationObjectId,Long companyId);

	Optional<VendorFormConfiguration> findByCompanyId(Long companyId);
}
