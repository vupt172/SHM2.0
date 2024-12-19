package com.vupt.SHM.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class SQLException extends DataIntegrityViolationException {
    public SQLException(String message) {
        super(message);
    }

    public SQLException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

