package com.example.candiflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDTO {
    @Email(message = "L'email n'est pas valide")
    private String email;

    @Size(min = 3, max = 30, message = "Le pseudo doit contenir entre 3 et 30 caractères")
    @Pattern(regexp = "^[^\\p{So}\\p{Cs}]*$", message = "Les emojis ne sont pas autorisés")
    private String username;
}
