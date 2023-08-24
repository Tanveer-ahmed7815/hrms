package com.te.flinko.repository.hr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.te.flinko.entity.hr.CompanyEventDetails;

@Repository
public interface CompanyEventDetailsRepository extends JpaRepository<CompanyEventDetails,Long>{
	//@Modifying
	//@Query("from CompanyEventDetails c where c.eventId=:event")
	CompanyEventDetails findByEventId(@Param("event") Long eventId);
	
	List<CompanyEventDetails> findByCompanyInfoCompanyId(Long companyId);
	
	
	
}
