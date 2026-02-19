package com.paypalclone.merchant_service.controller;

import com.paypalclone.merchant_service.auth.UserContextHolder;
import com.paypalclone.merchant_service.dtos.CreateMerchantRequest;
import com.paypalclone.merchant_service.dtos.MerchantResponse;
import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.repository.MerchantUserMappingRepository;
import com.paypalclone.merchant_service.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final MerchantUserMappingRepository mappingRepository;

    @PostMapping
    public MerchantResponse createMerchant(
            @Valid @RequestBody CreateMerchantRequest request
    ) {
        // 🔑 AUTH USER ID (from gateway / JWT)
        String authUserId =
                UserContextHolder.getCurrentUserId().toString();

        // 🔑 MAP → INTERNAL USER ID
        Long internalUserId =
                mappingRepository.findByExternalAuthId(authUserId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "User not yet created in user-service"
                                )
                        )
                        .getUserId();

        Merchant merchant = merchantService.createMerchant(
                internalUserId,
                request.getBusinessName(),
                request.getBusinessType(),
                request.getCountry()
        );

        return MerchantResponse.from(merchant);
    }

    @GetMapping("/me")
    public MerchantResponse getMyMerchant() {

        String authUserId =
                UserContextHolder.getCurrentUserId().toString();

        Long internalUserId =
                mappingRepository.findByExternalAuthId(authUserId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "User not yet created in user-service"
                                )
                        )
                        .getUserId();

        Merchant merchant =
                merchantService.getMerchantForUser(internalUserId);

        return MerchantResponse.from(merchant);
    }
}
