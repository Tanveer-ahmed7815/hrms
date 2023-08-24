package com.te.flinko.exception.account;

public class SubjectNotUniqueException extends RuntimeException {
    public SubjectNotUniqueException(String message) {
        super(message);
    }
}
