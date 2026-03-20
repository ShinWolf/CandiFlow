package com.example.candiflow.dto;

import com.example.candiflow.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationRequestDTO {

    @NotBlank(message = "L'entreprise est obligatoire")
    @Size(max = 100, message = "L'entreprise ne peut pas dépasser 100 caractères")
    private String company;

    @NotBlank(message = "Le poste est obligatoire")
    @Size(max = 100, message = "Le poste ne peut pas dépasser 100 caractères")
    private String jobTitle;

    @NotNull(message = "Le statut est obligatoire")
    private ApplicationStatus status;

    @Size(max = 500, message = "Les notes ne peuvent pas dépasser 500 caractères")
    private String notes;

    @URL(message = "Le lien doit être une URL valide")
    private String offerUrl;

    @NotNull(message = "La date est obligatoire")
    private LocalDate appliedAt;
}
