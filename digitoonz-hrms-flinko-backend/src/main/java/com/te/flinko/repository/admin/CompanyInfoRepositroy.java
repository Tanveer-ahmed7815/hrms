package com.te.flinko.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admin.CompanyInfo;
@Repository
public interface CompanyInfoRepositroy extends JpaRepository<CompanyInfo, Long> {

}
