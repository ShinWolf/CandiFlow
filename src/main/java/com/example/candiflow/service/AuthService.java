package com.example.candiflow.service;

import com.example.candiflow.dto.RegisterRequestDTO;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.Role;
import com.example.candiflow.exception.UserException;
import com.example.candiflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserException.EmailAlreadyUsedException("Email already used");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}
