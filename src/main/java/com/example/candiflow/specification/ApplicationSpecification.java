package com.example.candiflow.specification;

import com.example.candiflow.entity.Application;
import com.example.candiflow.enums.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

public class ApplicationSpecification {

    public static Specification<Application> hasUserId(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Application> hasCompany(String company) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("company")), "%" + company.trim().toLowerCase() + "%");
    }

    public static Specification<Application> hasHadInterview() {
        return (root, query, cb) -> cb.isTrue(root.get("hasHadInterview"));
    }

    public static Specification<Application> hasHadOffer() {
        return (root, query, cb) -> cb.isTrue(root.get("hasHadOffer"));
    }
}
