package com.paypalclone.user_service.controller;


import com.paypalclone.user_service.dtos.UserResponseDto;
import com.paypalclone.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {

        log.debug("Received request for /users/me");
        UserResponseDto response = userService.getCurrentUser();
        return ResponseEntity.ok(response);
    }
}
