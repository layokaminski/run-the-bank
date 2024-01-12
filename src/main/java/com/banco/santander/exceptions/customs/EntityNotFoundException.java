package com.banco.santander.exceptions.customs;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
