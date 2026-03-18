package com.example.candiflow;

import com.example.candiflow.dto.ApplicationPageResponseDTO;
import com.example.candiflow.dto.ApplicationRequestDTO;
import com.example.candiflow.dto.ApplicationResponseDTO;
import com.example.candiflow.entity.Application;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.ApplicationStatus;
import com.example.candiflow.exception.ApplicationException;
import com.example.candiflow.repository.ApplicationRepository;
import com.example.candiflow.repository.UserRepository;
import com.example.candiflow.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User user;
    private Application application;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        application = new Application();
        application.setId(1L);
        application.setCompany("Google");
        application.setJobTitle("Dev Full Stack");
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDate.now());
        application.setUser(user);
    }

    @Test
    void shouldCreateApplication() {
        ApplicationRequestDTO request = new ApplicationRequestDTO();
        request.setCompany("Google");
        request.setJobTitle("Dev Full Stack");
        request.setStatus(ApplicationStatus.APPLIED);
        request.setAppliedAt(LocalDate.now());

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplicationResponseDTO response = applicationService.create(request, "test@test.com");

        assertThat(response.getCompany()).isEqualTo("Google");
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.APPLIED);
    }

    @Test
    void shouldReturnPaginatedApplications() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(applicationRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(application)));

        ApplicationPageResponseDTO response = applicationService.getAll(
                "test@test.com", null, null, 0, 10
        );

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldReturnApplicationById() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(applicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(application));

        ApplicationResponseDTO response = applicationService.getById(1L, "test@test.com");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCompany()).isEqualTo("Google");
    }

    @Test
    void shouldThrowWhenApplicationNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(applicationRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getById(99L, "test@test.com"))
                .isInstanceOf(ApplicationException.NotFoundException.class);
    }

    @Test
    void shouldDeleteApplication() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(applicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(application));

        applicationService.delete(1L, "test@test.com");

        verify(applicationRepository, times(1)).delete(application);
    }
}
