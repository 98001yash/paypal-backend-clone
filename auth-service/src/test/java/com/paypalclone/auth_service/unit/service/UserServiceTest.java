package com.paypalclone.auth_service.unit.service;


import com.paypalclone.auth.UserRegisteredEvent;
import com.paypalclone.auth_service.entity.Role;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.kafka.EventPublisher;
import com.paypalclone.auth_service.kafka.KafkaTopics;
import com.paypalclone.auth_service.repository.RoleRepository;
import com.paypalclone.auth_service.repository.UserRepository;
import com.paypalclone.auth_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully_andPublishEvent(){

        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User result = userService.register(email, rawPassword);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        assertThat(result.getRoles())
                .extracting(Role::getName)
                .containsExactly("USER");

        verify(userRepository).existsByEmail(email);
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));

        ArgumentCaptor<UserRegisteredEvent> eventCaptor =
                ArgumentCaptor.forClass(UserRegisteredEvent.class);

        verify(eventPublisher).publish(
                eq(KafkaTopics.USER_REGISTERED),
                eq("1"),
                eventCaptor.capture()
        );

        UserRegisteredEvent event = eventCaptor.getValue();
        assertThat(event.getUserId()).isEqualTo(1L);
        assertThat(event.getEmail()).isEqualTo(email);
        assertThat(event.getEventType()).isEqualTo("USER_REGISTERED");
        assertThat(event.getEventVersion()).isEqualTo(1);

        verifyNoMoreInteractions(eventPublisher);
    }


    @Test
    void shouldThrowException_whenEmailAlreadyExists_andNotPublishEvent() {
        // GIVEN
        String email = "existing@test.com";
        String rawPassword = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        //  WHEN / THEN
        assertThatThrownBy(() -> userService.register(email, rawPassword))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(email);


        verify(userRepository).existsByEmail(email);

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(eventPublisher, never()).publish(any(), any(), any());
    }

}
