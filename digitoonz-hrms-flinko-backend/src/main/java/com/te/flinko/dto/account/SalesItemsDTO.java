package com.te.flinko.dto.account;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SalesItemsDTO {
    private List<SalesItemDTO> salesItems;
}
