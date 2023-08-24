package com.te.flinko.dto.account;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanySalesOrderDTO {
    private String salesOrderOwner;
    private Long companyClientInfoID; // private String dealName;
    private ProductType productType;
    private Long stockGroupId;
    private String subject;
    private String purchaseOrder;
    private Long customerNumber;
    private LocalDate dueDate;
    private Long clientContactPersonID; // private String contactName;
    private String pending;
    private String exciseDuty;
    private String carrier;
    private String status;
    private BigDecimal salesCommission;
}
