package com.example.candiflow.controller;

import com.example.candiflow.dto.ApplicationRequestDTO;
import com.example.candiflow.dto.ApplicationResponseDTO;
import com.example.candiflow.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> create(
            @RequestBody ApplicationRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.create(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponseDTO>> getAll(Authentication authentication) {
        return ResponseEntity.ok(applicationService.getAll(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.getById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ApplicationRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.update(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        applicationService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
