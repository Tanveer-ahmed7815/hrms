package com.te.flinko.repository.account;

import com.te.flinko.entity.sales.ClientContactPersonDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientContactPersonDetailsRepository extends JpaRepository<ClientContactPersonDetails, Long> {

}
