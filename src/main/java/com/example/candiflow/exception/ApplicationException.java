package com.example.candiflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ApplicationException extends RuntimeException {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
            super("Application not found");
        }
    }
}
