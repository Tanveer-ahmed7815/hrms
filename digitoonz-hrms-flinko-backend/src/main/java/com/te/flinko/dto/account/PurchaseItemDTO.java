package com.te.flinko.dto.account;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseItemDTO {
    private Long serialNo;
    private String productName;
    private long quantity;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal payableAmount;
    private String description;
}
