package com.banco.santander.services;

public interface FieldValueExists {
    /**
     * Checks whether a given value exists for a given field
     *
     * @param value The value to check for
     * @param fieldName The name of the field for which to check if the value exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean fieldValueExists(String value, String fieldName) throws UnsupportedOperationException;
}
