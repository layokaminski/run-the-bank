package com.banco.santander.exceptions.customs;

public class IllegalArgumentException extends Exception {
    public IllegalArgumentException(final String errorMessage) {
        super(errorMessage);
    }
}
