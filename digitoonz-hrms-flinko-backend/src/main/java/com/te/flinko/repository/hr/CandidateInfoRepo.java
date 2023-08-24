package com.te.flinko.repository.hr;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.hr.CandidateInfo;

public interface CandidateInfoRepo extends JpaRepository<CandidateInfo, Long>{

}
