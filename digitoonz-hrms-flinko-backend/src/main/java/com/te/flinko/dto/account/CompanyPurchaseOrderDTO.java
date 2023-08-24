package com.te.flinko.dto.account;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanyPurchaseOrderDTO {
    private String purchaseOrderOwner;
    private String purchaseOrderNumber;
    private ProductType productType;
    private Long stockGroupId;
    private String subject;
    private Long vendorId;
    private String contactName;
    private LocalDate purchaseOrderDate;
    private String requisitionNumber;
    private String trackingNumber;
    private LocalDate dueDate;
    private String carrier;
    private String exciseDuty;
    private BigDecimal salesCommission;
    private String status;
}
