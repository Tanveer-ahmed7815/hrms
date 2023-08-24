package com.te.flinko.dto.account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AddressType {
    BILLING("Billing"), SHIPPING("Shipping");
    private final String type;
}
