package com.paypalclone.user_service.dtos;

import com.paypalclone.user_service.enums.UserType;

public record CreateUserRequestDto(

        String externalAuthId,
        String email,
        UserType userType
) {
}
