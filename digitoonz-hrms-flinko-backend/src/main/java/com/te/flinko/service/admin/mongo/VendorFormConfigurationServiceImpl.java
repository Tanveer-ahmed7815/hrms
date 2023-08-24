package com.te.flinko.service.admin.mongo;

import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.ALREADY_COMPANY_CONFIGURE_THE_VENDOR_FORM;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.COMPANY_NOT_EXIST;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.DUPLICATE_NAME_CAN_NOT_BE_ALLOWED;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.FETCH_VENDOR_FORM_CONFIGURATION_SUCCESSFULLY;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.NO_DATA_PRESENT_WITH_THIS_FORM_CONFIGURATION_ID;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.THE_ADD_VENDOR_FORM_CONFIGURATION_METHOD_BEGINS;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.THE_ADD_VENDOR_FORM_CONFIGURATION_METHOD_END;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.VALUE_CAN_NOT_BE_NULL_OR_EMPTY;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.VENDOR_FORM_CONFIGURATION_CREATED;
import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.VENDOR_FORM_CONFIGURATION_UPDATE_SUCCESSFULLY;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.audit.common.db.DBConstants;
import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.dto.admin.mongo.Attribute;
import com.te.flinko.dto.admin.mongo.VendorFormConfigurationDto;
import com.te.flinko.entity.admin.mongo.VendorFormConfiguration;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.mongo.VendorFormConfigurationRepository;
import com.te.flinko.util.Generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@RequiredArgsConstructor
@Slf4j
public class VendorFormConfigurationServiceImpl implements VendorFormConfigurationService {

	private final VendorFormConfigurationRepository formConfigurationRepository;

	private final CompanyInfoRepository companyInfoRepository;

	private final Generator generator;

	@Override
	public String addVendorFormConfiguration(VendorFormConfigurationDto vendorFormConfigurationDto, Long companyId) {
		log.info(THE_ADD_VENDOR_FORM_CONFIGURATION_METHOD_BEGINS, vendorFormConfigurationDto, " And Company Id ",
				companyId);
		String addVendorConfigurationStatus = Optional
				.ofNullable(Optional.ofNullable(vendorFormConfigurationDto.getAttributeList()).filter(x -> !x.isEmpty())
						.map(y -> companyInfoRepository.findById(companyId)
								.map(company -> Optional.of(company)
										.filter(com -> com.getIsSubmited() == null || !com.getIsSubmited()).map(x -> y)
										.orElseThrow(() -> {
											log.error(ALREADY_COMPANY_CONFIGURE_THE_VENDOR_FORM);
											return new DataNotFoundException(ALREADY_COMPANY_CONFIGURE_THE_VENDOR_FORM);
										}))
								.orElseThrow(() -> {
									log.error(COMPANY_NOT_EXIST);
									return new DataNotFoundException(COMPANY_NOT_EXIST);
								}))
						.map(x -> x.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getType,
								(k1, k2) -> k2, LinkedHashMap::new)))
						.orElseThrow(() -> {
							log.error(VALUE_CAN_NOT_BE_NULL_OR_EMPTY);
							return new DataNotFoundException(VALUE_CAN_NOT_BE_NULL_OR_EMPTY);
						}))
				.filter(att -> att.size() == vendorFormConfigurationDto.getAttributeList().size()).map(
						attribute -> formConfigurationRepository
								.findByFormConfigurationObjectIdAndCompanyId(
										vendorFormConfigurationDto.getFormConfigurationObjectId(), companyId)
								.map(form -> {
									form.setAttributes(attribute);
									formConfigurationRepository.save(form);
									log.info(VENDOR_FORM_CONFIGURATION_UPDATE_SUCCESSFULLY);
									return VENDOR_FORM_CONFIGURATION_UPDATE_SUCCESSFULLY;
								}).orElseGet(() -> {
									vendorFormConfigurationDto.setFormConfigurationId(generator
											.generateSequence(DBConstants.VENDOR_FORM_CONFIGURATION_SEQUENCE_NAME));
									vendorFormConfigurationDto.setCompanyId(companyId);
									vendorFormConfigurationDto.setAttributes(attribute);
									formConfigurationRepository.save(BeanCopy.objectProperties(
											vendorFormConfigurationDto, VendorFormConfiguration.class));
									log.info(VENDOR_FORM_CONFIGURATION_CREATED);
									return VENDOR_FORM_CONFIGURATION_CREATED;
								}))
				.orElseThrow(() -> {
					log.error(DUPLICATE_NAME_CAN_NOT_BE_ALLOWED);
					return new DataNotFoundException(DUPLICATE_NAME_CAN_NOT_BE_ALLOWED);
				});
		log.info(THE_ADD_VENDOR_FORM_CONFIGURATION_METHOD_END, addVendorConfigurationStatus);
		return addVendorConfigurationStatus;

	}

	@Override
	public VendorFormConfigurationDto getVendorFormConfiguration(Long companyId) {
		log.info("The getVendorFormConfiguration Method Begins With Company Id ", companyId);
		VendorFormConfiguration vendorFormConfiguration = formConfigurationRepository.findByCompanyId(companyId)
				.orElseThrow(() -> new DataNotFoundException(NO_DATA_PRESENT_WITH_THIS_FORM_CONFIGURATION_ID));
		return companyInfoRepository.findByCompanyId(companyId).map(c -> {
			VendorFormConfigurationDto vendorFormConfigurationDto = new VendorFormConfigurationDto();
			BeanUtils.copyProperties(vendorFormConfiguration, vendorFormConfigurationDto);
			vendorFormConfigurationDto.setIsSubmited(c.getIsSubmited());
			log.info(FETCH_VENDOR_FORM_CONFIGURATION_SUCCESSFULLY);
			return vendorFormConfigurationDto;
		}).orElseThrow(() -> {
			log.error(COMPANY_NOT_EXIST);
			return new DataNotFoundException(COMPANY_NOT_EXIST);
		});

	}

}
