package com.example.candiflow.service;

import com.example.candiflow.dto.AuthResponseDTO;
import com.example.candiflow.dto.LoginRequestDTO;
import com.example.candiflow.dto.RegisterRequestDTO;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.Role;
import com.example.candiflow.exception.UserException;
import com.example.candiflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserException.EmailAlreadyUsedException("Email already used");
        }

        if (request.getUsername() != null && userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserException.UsernameAlreadyUsedException("Pseudo déjà utilisé");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }


    public AuthResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token);
    }
}
