package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.ApprisalMeetingInfo;

@Repository
public interface ApprisalMeetingInfoRepository extends JpaRepository<ApprisalMeetingInfo, Long>{

	List<ApprisalMeetingInfo> findByEmployeeReviseSalaryReviseSalaryIdIn(List<Long> salaryId);

	List<ApprisalMeetingInfo> findByEmployeeReviseSalaryEmployeePersonalInfoEmployeeInfoId(Long meetingId);
	
	ApprisalMeetingInfo findByMeetingIdAndEmployeeReviseSalaryCompanyInfoCompanyId(Long meetingId,Long companyId);

}
