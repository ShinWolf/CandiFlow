package com.example.candiflow.service;

import com.example.candiflow.dto.DashboardStatsDTO;
import com.example.candiflow.entity.User;
import com.example.candiflow.enums.ApplicationStatus;
import com.example.candiflow.repository.ApplicationRepository;
import com.example.candiflow.repository.UserRepository;
import com.example.candiflow.specification.ApplicationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public DashboardStatsDTO getStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var userSpec = ApplicationSpecification.hasUserId(user.getId());

        long total    = applicationRepository.count(userSpec);
        long applied  = applicationRepository.count(userSpec.and(ApplicationSpecification.hasStatus(ApplicationStatus.APPLIED)));
        long interview = applicationRepository.count(userSpec.and(ApplicationSpecification.hasStatus(ApplicationStatus.INTERVIEW)));
        long offer    = applicationRepository.count(userSpec.and(ApplicationSpecification.hasStatus(ApplicationStatus.OFFER)));
        long rejected = applicationRepository.count(userSpec.and(ApplicationSpecification.hasStatus(ApplicationStatus.REJECTED)));

        double interviewRate = total > 0 ? Math.round((double) interview / total * 1000.0) / 10.0 : 0;
        double offerRate     = total > 0 ? Math.round((double) offer / total * 1000.0) / 10.0 : 0;

        return new DashboardStatsDTO(total, applied, interview, offer, rejected, interviewRate, offerRate);
    }
}
