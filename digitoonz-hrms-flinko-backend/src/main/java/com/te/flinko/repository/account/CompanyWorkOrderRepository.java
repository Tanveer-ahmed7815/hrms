package com.te.flinko.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.account.CompanyWorkOrder;

public interface CompanyWorkOrderRepository extends JpaRepository<CompanyWorkOrder, Long>{

}
