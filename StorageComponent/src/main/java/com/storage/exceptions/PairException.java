package com.storage.exceptions;

public class PairException extends Exception {
    private PairException(String message) {
        super(message);
    }

    public static PairException AlreadyExistValueException() {
        return new PairException("Pair with the same value already exists");
    }
}
