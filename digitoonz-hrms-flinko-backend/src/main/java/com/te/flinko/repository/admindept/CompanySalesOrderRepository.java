package com.te.flinko.repository.admindept;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyInfo;

public interface CompanySalesOrderRepository extends JpaRepository<CompanySalesOrder, Long> {

	Optional<CompanyInfo> findBySalesOrderIdAndCompanyInfoCompanyId(Long salesOrderId, Long companyId);

    List<CompanySalesOrder> findBySubject(String subject);
}
