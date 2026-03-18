package com.example.candiflow.service;

import com.example.candiflow.dto.ApplicationPageResponseDTO;
import com.example.candiflow.dto.ApplicationRequestDTO;
import com.example.candiflow.dto.ApplicationResponseDTO;
import com.example.candiflow.entity.Application;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.ApplicationStatus;
import com.example.candiflow.exception.ApplicationException;
import com.example.candiflow.repository.ApplicationRepository;
import com.example.candiflow.repository.UserRepository;
import com.example.candiflow.specification.ApplicationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private ApplicationResponseDTO toDTO(Application app) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(app.getId());
        dto.setCompany(app.getCompany());
        dto.setJobTitle(app.getJobTitle());
        dto.setStatus(app.getStatus());
        dto.setNotes(app.getNotes());
        dto.setOfferUrl(app.getOfferUrl());
        dto.setAppliedAt(app.getAppliedAt());
        return dto;
    }

    public ApplicationResponseDTO create(ApplicationRequestDTO request, String email) {
        User user = getUser(email);

        Application app = new Application();
        app.setCompany(request.getCompany());
        app.setJobTitle(request.getJobTitle());
        app.setStatus(request.getStatus());
        app.setNotes(request.getNotes());
        app.setOfferUrl(request.getOfferUrl());
        app.setAppliedAt(request.getAppliedAt());
        app.setUser(user);

        return toDTO(applicationRepository.save(app));
    }

    public ApplicationPageResponseDTO getAll(String email, ApplicationStatus status, String company, int page, int size) {
        User user = getUser(email);

        Specification<Application> spec = Specification
                .where(ApplicationSpecification.hasUserId(user.getId()));

        if (status != null) {
            spec = spec.and(ApplicationSpecification.hasStatus(status));
        }

        if (company != null && !company.isBlank()) {
            spec = spec.and(ApplicationSpecification.hasCompany(company));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<Application> result = applicationRepository.findAll(spec, pageable);

        return new ApplicationPageResponseDTO(
                result.getContent().stream().map(this::toDTO).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    public ApplicationResponseDTO getById(Long id, String email) {
        User user = getUser(email);
        Application app = applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(ApplicationException.NotFoundException::new);
        return toDTO(app);
    }

    public ApplicationResponseDTO update(Long id, ApplicationRequestDTO request, String email) {
        User user = getUser(email);
        Application app = applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(ApplicationException.NotFoundException::new);

        app.setCompany(request.getCompany());
        app.setJobTitle(request.getJobTitle());
        app.setStatus(request.getStatus());
        app.setNotes(request.getNotes());
        app.setOfferUrl(request.getOfferUrl());
        app.setAppliedAt(request.getAppliedAt());

        return toDTO(applicationRepository.save(app));
    }

    public void delete(Long id, String email) {
        User user = getUser(email);
        Application app = applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(ApplicationException.NotFoundException::new);
        applicationRepository.delete(app);
    }
}
