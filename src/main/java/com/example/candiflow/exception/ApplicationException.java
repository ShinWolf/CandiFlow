package com.example.candiflow.exception;

public class ApplicationException extends RuntimeException {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
            super("Application not found");
        }
    }
}
