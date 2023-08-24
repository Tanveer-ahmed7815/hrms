package com.te.flinko.dto.account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductType {
    HARDWARE("Hardware"), SOFTWARE("Software");
    private final String type;
}
