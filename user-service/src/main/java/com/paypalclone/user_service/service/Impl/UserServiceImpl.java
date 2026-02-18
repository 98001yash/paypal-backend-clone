package com.paypalclone.user_service.service.Impl;


import com.paypalclone.user_service.auth.UserContextHolder;
import com.paypalclone.user_service.dtos.UserResponseDto;
import com.paypalclone.user_service.entity.User;
import com.paypalclone.user_service.exceptions.ResourceNotFoundException;
import com.paypalclone.user_service.exceptions.UserNotFoundException;
import com.paypalclone.user_service.repository.UserRepository;
import com.paypalclone.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserResponseDto getCurrentUser() {
        Long currentUserId = UserContextHolder.getCurrentUserId();

        if(currentUserId==null){
            log.error("Current user id is null");
            throw new ResourceNotFoundException(null);
        }

        log.debug("Fetching current user id with authUserId={}",currentUserId);


        User user = userRepository.findByExternalAuthId(currentUserId.toString())
                .orElseThrow(() -> {
                    log.warn("User not found for authUserId={}", currentUserId);
                    return new UserNotFoundException(currentUserId);
                });

        log.info("User fetched successfully for authUserId={}", currentUserId);

        return mapToDto(user);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {

        log.debug("Fetching user by internal userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for internal userId={}", userId);
                    return new UserNotFoundException(userId);
                });

        return mapToDto(user);
    }

    // ---------- mapping ----------

    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getExternalAuthId(),
                user.getEmail(),
                user.getUserType(),
                user.getStatus(),
                user.getKycLevel(),
                user.getRiskState(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
