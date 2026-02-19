package com.paypalclone.merchant_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMerchantRequest {


    @NotBlank
    @Size(max = 255)
    private String businessName;

    @NotBlank
    @Size(max = 50)
    private String businessType;

    @NotBlank
    @Size(min = 2, max = 2)
    private String country;
}
