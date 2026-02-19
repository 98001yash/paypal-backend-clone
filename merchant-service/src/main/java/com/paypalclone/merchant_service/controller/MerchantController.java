package com.paypalclone.merchant_service.controller;

import com.paypalclone.merchant_service.auth.UserContextHolder;
import com.paypalclone.merchant_service.dtos.CreateMerchantRequest;
import com.paypalclone.merchant_service.dtos.MerchantResponse;
import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {


    private final MerchantService merchantService;

    @PostMapping
    public MerchantResponse createMerchant(
            @Valid @RequestBody CreateMerchantRequest request
    ) {
        Long userId = UserContextHolder.getCurrentUserId();

        Merchant merchant = merchantService.createMerchant(
                userId,
                request.getBusinessName(),
                request.getBusinessType(),
                request.getCountry()
        );

        return MerchantResponse.from(merchant);
    }

    @GetMapping("/me")
    public MerchantResponse getMyMerchant() {
        Long userId = UserContextHolder.getCurrentUserId();
        Merchant merchant = merchantService.getMerchantForUser(userId);
        return MerchantResponse.from(merchant);
    }
}
