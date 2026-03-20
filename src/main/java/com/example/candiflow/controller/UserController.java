package com.example.candiflow.controller;

import com.example.candiflow.dto.UpdatePasswordRequestDTO;
import com.example.candiflow.dto.UpdateProfileRequestDTO;
import com.example.candiflow.dto.UserProfileDTO;
import com.example.candiflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordRequestDTO request,
            Authentication authentication) {
        userService.updatePassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }
}
