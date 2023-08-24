package com.te.flinko.repository.account;

import com.te.flinko.entity.account.mongo.CompanyVendorInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyVendorInfoRepository extends MongoRepository<CompanyVendorInfo, Long> {
    Optional<CompanyVendorInfo> findByVendorName(String contactName);
}
