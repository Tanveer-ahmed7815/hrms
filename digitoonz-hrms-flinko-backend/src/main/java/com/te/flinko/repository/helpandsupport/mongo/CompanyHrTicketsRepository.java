package com.te.flinko.repository.helpandsupport.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.flinko.entity.helpandsupport.mongo.CompanyHrTickets;

public interface CompanyHrTicketsRepository extends MongoRepository<CompanyHrTickets,String> {
	
    List<CompanyHrTickets> findByCompanyId(Long companyId);	
     
     CompanyHrTickets findByHrTicketId(Long hrTicketId);	
     
   //List<CompanyHrTickets> findByEmployeeId(String employeeId);

}
