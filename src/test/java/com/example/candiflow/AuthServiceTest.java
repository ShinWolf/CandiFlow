package com.example.candiflow;

import com.example.candiflow.dto.AuthResponseDTO;
import com.example.candiflow.dto.LoginRequestDTO;
import com.example.candiflow.dto.RegisterRequestDTO;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.Role;
import com.example.candiflow.exception.UserException;
import com.example.candiflow.repository.UserRepository;
import com.example.candiflow.service.AuthService;
import com.example.candiflow.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUser() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashed");

        authService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyUsed() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserException.EmailAlreadyUsedException.class);
    }

    @Test
    void shouldLoginAndReturnToken() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("test@test.com");
        user.setRole(Role.USER);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("test@test.com")).thenReturn("mocked.jwt.token");

        AuthResponseDTO response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
    }
}
