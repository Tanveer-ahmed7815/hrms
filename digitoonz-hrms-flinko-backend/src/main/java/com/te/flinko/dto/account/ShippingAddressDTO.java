package com.te.flinko.dto.account;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShippingAddressDTO {
    @NotBlank(message = "Please provide the shipping address details")
    private String addressDetails;
    @NotBlank(message = "Please provide the city")
    private String city;
    @NotBlank(message = "Please provide the state")
    private String state;
    @NotBlank(message = "Please provide the country")
    private String country;
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pin-code is not valid")
    private String pinCode;
}
