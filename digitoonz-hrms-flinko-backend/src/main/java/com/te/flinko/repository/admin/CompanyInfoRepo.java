package com.te.flinko.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyInfo;

public interface CompanyInfoRepo extends JpaRepository<CompanyInfo, Long>{

}
