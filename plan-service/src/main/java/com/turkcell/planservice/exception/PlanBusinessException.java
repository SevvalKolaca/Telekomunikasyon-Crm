package com.turkcell.planservice.exception;

import io.github.ergulberke.exception.BusinessException;

public class PlanBusinessException extends BusinessException {

    public PlanBusinessException(String message) {
        super(message);
    }

    public PlanBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 