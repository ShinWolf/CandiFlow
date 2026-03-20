package com.example.candiflow.service;

import com.example.candiflow.dto.UpdatePasswordRequestDTO;
import com.example.candiflow.dto.UpdateProfileRequestDTO;
import com.example.candiflow.dto.UserProfileDTO;
import com.example.candiflow.entity.User;
import com.example.candiflow.exception.UserException;
import com.example.candiflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserProfileDTO getProfile(String email) {
        User user = getUser(email);
        return new UserProfileDTO(user.getUsername(), user.getEmail());
    }

    public UserProfileDTO updateProfile(String email, UpdateProfileRequestDTO request) {
        User user = getUser(email);

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new UserException.EmailAlreadyUsedException("Email déjà utilisé");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new UserException.UsernameAlreadyUsedException("Pseudo déjà utilisé");
            }
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);

        return new UserProfileDTO(user.getUsername(), user.getEmail());
    }

    public void updatePassword(String email, UpdatePasswordRequestDTO request) {
        User user = getUser(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UserException.InvalidPasswordException("Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
