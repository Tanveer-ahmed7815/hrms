package com.te.flinko.service.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.hr.ReferralRewardsListDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeReferenceInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeeReferenceInfoRepository;

@Service
public class ReferralRewardsServiceImpl implements ReferralRewardsService {
	@Autowired
	private EmployeeReferenceInfoRepository referenceInfoRepository;
	@Autowired
	private CompanyInfoRepository companyInfoRepository;
	//Api for displaying referralRewardseligibleEmployees
	@Override
	public List<ReferralRewardsListDTO> referralRewardseligibleemployee(Long companyId) {

		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {

			List<ReferralRewardsListDTO> referralRewardsListdtoList = new ArrayList<>();

			List<EmployeeReferenceInfo> referenceinfo = referenceInfoRepository
					.findByEmployeePersonalInfoCompanyInfoCompanyId(companyId);
			if (!referenceinfo.isEmpty()) {
				for (EmployeeReferenceInfo employeeReferenceInfo : referenceinfo) {

					ReferralRewardsListDTO rewardsListdto = new ReferralRewardsListDTO();

					rewardsListdto.setEmployeeId(
							employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
					rewardsListdto.setFullName(employeeReferenceInfo.getEmployeePersonalInfo().getFirstName()
							+ employeeReferenceInfo.getEmployeePersonalInfo().getLastName());
					rewardsListdto.setDesignation(
							employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDesignation());
					rewardsListdto.setDepartment(
							employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDepartment());
					rewardsListdto.setReferredTo(employeeReferenceInfo.getReferralName());

					referralRewardsListdtoList.add(rewardsListdto);
				}
				return referralRewardsListdtoList;
			} else {
				throw new DataNotFoundException("Reference Employees Not Available");
			}
		} else {
			throw new DataNotFoundException("CompanyId Not Present");
		}

	}
	//Api for displaying reference employee based on reference id
	@Override
	public ReferralRewardsListDTO referalRewardforEmployee(Long referenceId) {
		Optional<EmployeeReferenceInfo> optionalDetails = referenceInfoRepository.findById(referenceId);
		
		if (optionalDetails.isPresent()) {
			EmployeeReferenceInfo employeeReferenceInfo = optionalDetails.get();
			ReferralRewardsListDTO rewardsListdto=new ReferralRewardsListDTO();
			rewardsListdto.setEmployeeId(
					employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
			rewardsListdto.setFullName(employeeReferenceInfo.getEmployeePersonalInfo().getFirstName()
					+ employeeReferenceInfo.getEmployeePersonalInfo().getLastName());
			rewardsListdto.setDesignation(
					employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDesignation());
			rewardsListdto.setDepartment(
					employeeReferenceInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getDepartment());
			rewardsListdto.setReferredTo(employeeReferenceInfo.getReferralName());
			return rewardsListdto;
		}else {
			throw new DataNotFoundException("RefereanceId Not Found");
			
		}
	}

}
