package com.paypalclone.user_service.dtos;

import com.paypalclone.user_service.enums.UserStatus;

public record UpdateUserStatusRequestDto(
        UserStatus status
) {
}
