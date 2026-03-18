package com.example.candiflow.repository;

import com.example.candiflow.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByUserId(Long userId);
    Optional<Application> findByIdAndUserId(Long id, Long userId);
}
