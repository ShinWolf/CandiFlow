package com.example.candiflow.service;

import com.example.candiflow.dto.ApplicationRequestDTO;
import com.example.candiflow.dto.ApplicationResponseDTO;
import com.example.candiflow.entity.Application;
import com.example.candiflow.entity.User;
import com.example.candiflow.exception.ApplicationException;
import com.example.candiflow.repository.ApplicationRepository;
import com.example.candiflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<ApplicationResponseDTO> getAll(String email) {
        User user = getUser(email);
        return applicationRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
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
