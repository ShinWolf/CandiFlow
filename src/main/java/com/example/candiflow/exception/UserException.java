package com.example.candiflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserException extends RuntimeException {

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class EmailAlreadyUsedException extends RuntimeException {
        public EmailAlreadyUsedException(String email) {
            super("L'adresse email '" + email + "' est déjà utilisée.");
        }
    }
}
