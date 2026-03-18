package com.example.candiflow;

import com.example.candiflow.entity.Application;
import com.example.candiflow.enums.ApplicationStatus;
import com.example.candiflow.specification.ApplicationSpecification;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationSpecificationTest {

    @Mock private Root<Application> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Path<Object> path;
    @Mock private Expression<String> expression;
    @Mock private Predicate predicate;

    @Test
    void shouldBuildHasUserIdSpec() {
        when(root.get("user")).thenReturn(path);
        when(path.get("id")).thenReturn(path);
        when(cb.equal(any(), eq(1L))).thenReturn(predicate);

        Specification<Application> spec = ApplicationSpecification.hasUserId(1L);
        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldBuildHasStatusSpec() {
        when(root.get("status")).thenReturn(path);
        when(cb.equal(any(), eq(ApplicationStatus.APPLIED))).thenReturn(predicate);

        Specification<Application> spec = ApplicationSpecification.hasStatus(ApplicationStatus.APPLIED);
        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldBuildHasCompanySpec() {
        when(root.get("company")).thenReturn(path);
        when(cb.lower(any())).thenReturn(expression);
        when(cb.like(any(), eq("%google%"))).thenReturn(predicate);

        Specification<Application> spec = ApplicationSpecification.hasCompany("Google");
        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }
}
