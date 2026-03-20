package com.example.candiflow.repository;

import com.example.candiflow.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    List<Application> findAllByUserId(Long userId);
    Optional<Application> findByIdAndUserId(Long id, Long userId);
}
