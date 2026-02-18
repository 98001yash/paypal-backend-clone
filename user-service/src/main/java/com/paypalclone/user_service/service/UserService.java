package com.paypalclone.user_service.service;

import com.paypalclone.user_service.dtos.UserResponseDto;

public interface UserService {

    UserResponseDto getCurrentUser();
    UserResponseDto getUserById(Long userId);
}
