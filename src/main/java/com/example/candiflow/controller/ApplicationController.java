package com.example.candiflow.controller;

import com.example.candiflow.dto.ApplicationPageResponseDTO;
import com.example.candiflow.dto.ApplicationRequestDTO;
import com.example.candiflow.dto.ApplicationResponseDTO;
import com.example.candiflow.enums.ApplicationStatus;
import com.example.candiflow.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> create(
            @Valid
            @RequestBody ApplicationRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.create(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<ApplicationPageResponseDTO> getAll(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return ResponseEntity.ok(
                applicationService.getAll(authentication.getName(), status, company, page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.getById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> update(
            @Valid
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
