package com.example.candiflow.dto;

import com.example.candiflow.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationResponseDTO {
    private Long id;
    private String company;
    private String jobTitle;
    private ApplicationStatus status;
    private String notes;
    private String offerUrl;
    private LocalDate appliedAt;
}
