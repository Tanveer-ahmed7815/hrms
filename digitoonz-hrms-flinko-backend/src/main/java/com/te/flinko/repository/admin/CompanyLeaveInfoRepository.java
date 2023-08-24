package com.te.flinko.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyLeaveInfo;

public interface CompanyLeaveInfoRepository extends JpaRepository<CompanyLeaveInfo, Long> {

}
