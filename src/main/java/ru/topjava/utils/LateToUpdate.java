package ru.topjava.utils;

public class LateToUpdate extends RuntimeException {
    public LateToUpdate(String message) {
        super(message);
    }
}
