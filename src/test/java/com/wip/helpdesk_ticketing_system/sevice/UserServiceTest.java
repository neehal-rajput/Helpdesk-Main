package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setName("Alice");
        mockUser.setEmail("alice@test.com");
        mockUser.setPasswordHash("encoded");
        mockUser.setRole(Role.AGENT);

        userDto = new UserDto();
        userDto.setName("Alice");
        userDto.setEmail("alice@test.com");
        userDto.setPassword("pass123");
        userDto.setRole(Role.AGENT);
    }

    @Test
    void addUser_shouldCreateUser() {
        when(userRepository.findByEmail("alice@test.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123"))
                .thenReturn("encoded");
        when(userRepository.save(any()))
                .thenReturn(mockUser);

        User result = userService.addUser(userDto);

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getRole()).isEqualTo(Role.AGENT);
    }

    @Test
    void addUser_shouldFailIfEmailExists() {
        when(userRepository.findByEmail("alice@test.com"))
                .thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> userService.addUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void getUserById_shouldThrowIfNotFound() {
        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_shouldWork() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        userService.deleteUser(1L);

        verify(userRepository).delete(mockUser);
    }
}