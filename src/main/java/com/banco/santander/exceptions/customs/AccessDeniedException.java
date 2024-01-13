package com.banco.santander.exceptions.customs;

public class AccessDeniedException extends Exception {
    public AccessDeniedException(final String errorMessage) {
        super(errorMessage);
    }
}
